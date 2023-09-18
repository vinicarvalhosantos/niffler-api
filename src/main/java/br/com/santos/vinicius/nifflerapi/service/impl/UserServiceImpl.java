package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.UserFetchModel;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import br.com.santos.vinicius.nifflerapi.runnable.RunnableExecutor;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchRequestsRetrofit;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchToken;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final RunnableExecutor runnableExecutor;

    final LastUserMessageService lastUserMessageService;

    final BlacklistService blacklistService;

    TwitchToken twitchToken;

    @Value("${twitch.client.id}")
    String clientId;

    @Value("${threads.to.be.used}")
    int numberOfThreads;

    public UserServiceImpl(UserRepository userRepository, RunnableExecutor runnableExecutor,
                           LastUserMessageService lastUserMessageService, @Lazy BlacklistService blacklistService) {
        Assert.notNull(userRepository, "UserRepository must not be null");
        Assert.notNull(runnableExecutor, "RunnableExecutor must not be null");
        Assert.notNull(lastUserMessageService, "LastUserMessageService must not be null");
        Assert.notNull(blacklistService, "BlacklistService must not be null");
        this.userRepository = userRepository;
        this.runnableExecutor = runnableExecutor;
        this.lastUserMessageService = lastUserMessageService;
        this.blacklistService = blacklistService;
    }

    @Deprecated
    @Override
    public ResponseEntity<Response> getAllUsers() {
        log.info("Getting all users.");
        List<UserEntity> userEntities = IteratorUtils.toList(userRepository.findAll().iterator());

        if (userEntities.isEmpty()) {
            log.info("Any users in our database.");
            throw new NoSuchElementFoundException("Any users in our database.");
        }

        SuccessResponse successResponse = new SuccessResponse(formatRecords(userEntities), "Users were found in our database");
        log.info("Users were found in our database.");
        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public ResponseEntity<Response> getAllUsers(int page, int limit) {
        log.info("Getting all users.");
        Page<UserEntity> userEntityPage = userRepository.findAll(PageRequest.of((page - 1), limit, Sort.by("id").ascending()));
        int totalPages = userEntityPage.getTotalPages();
        Long totalElements = userEntityPage.getTotalElements();
        List<UserEntity> userEntityList = userEntityPage.get().collect(Collectors.toList());

        if (userEntityList.isEmpty()) {
            log.info("Any users in our database.");
            throw new NoSuchElementFoundException("Any users in our database.");
        }

        SuccessResponse successResponse = new SuccessResponse(formatRecords(userEntityList), "Users were found in our database", page, totalPages, totalElements, limit);
        log.info("Users were found in our database.");
        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Transactional
    @Override
    public ResponseEntity<Response> fetchAllUsers() throws IOException {
        log.info("Starting to fetch all users.");
        Page<UserEntity> userEntityPage = userRepository.findAllByDeletedIsFalse(PageRequest.of(0, 100, Sort.by("createdAt", "id").ascending()));

        UserFetchModel userFetchModel = collectTwitchUsersList(userEntityPage);

        executeThreads(userFetchModel.getThreadList());

        log.info("Check the result from twitch and fetch users in database");
        return fetchUsersExisting(userFetchModel.getTwitchUsers(), userFetchModel.getUserEntityList());
    }

    @Override
    public UserEntity fetchUserByUsername(String username) throws IOException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        TwitchUserModel twitchUser = this.getTwitchUser(username);

        return fetchUser(userEntity, twitchUser);
    }

    @Override
    public UserEntity fetchFromUserMessage(UserMessageDto userMessageDto) {
        log.info("Starting to fetch user from user message.");
        log.info("Checking if user exists in our database.");
        Optional<UserEntity> userEntity = userRepository.findById(userMessageDto.getUserId());

        if (userEntity.isEmpty()) {
            log.info("User does not exists in our database. Creating it.");
            UserEntity user = new UserEntity(userMessageDto.getUserId(),
                    userMessageDto.getUsername(),
                    userMessageDto.getDisplayName(),
                    BigDecimal.ZERO, BigDecimal.ZERO);
            return userRepository.save(user);
        }

        UserEntity user = userEntity.get();

        log.info("Fetching user within user message if is needed.");
        user.fetchUserFromMessage(userMessageDto);

        log.info("User fetched.");
        return user;
    }

    private ResponseEntity<Response> fetchUsersExisting(TwitchUserModel twitchUsers, List<UserEntity> userEntityList) {
        int numberOfUsersUpdated = fetchUsers(twitchUsers.getData(), userEntityList);
        String message = extractMessage(numberOfUsersUpdated);

        SuccessResponse successResponse = new SuccessResponse(Collections.emptyList(), message);

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    private UserEntity fetchUser(Optional<UserEntity> userEntity, TwitchUserModel twitchUser) {

        log.info("Checking if twitch user exists.");
        if (twitchUser.getData().isEmpty()) {
            log.info("Twitch user does not exists.");
            return null;
        }

        return getUserFetched(userEntity, twitchUser);
    }

    private int fetchUsers(List<TwitchUserModelData> twitchUserModelDataList, List<UserEntity> userEntityList) {
        List<UserEntity> usersDifferent = extractDifferentUsers(twitchUserModelDataList, userEntityList);
        List<UserEntity> usersDeleted = extractDeletedUsers(twitchUserModelDataList, userEntityList);

        if (!usersDifferent.isEmpty()) {
            saveAllUsers(usersDifferent);
        }

        if (!usersDeleted.isEmpty()) {
            deleteAllUsersDeleted(usersDeleted);
        }

        return usersDifferent.size() + usersDeleted.size();
    }

    private UserEntity getUserFetched(Optional<UserEntity> userEntity, TwitchUserModel twitchUser) {
        UserEntity user = new UserEntity();
        if (userEntity.isEmpty()) {
            user.setId(Long.parseLong(twitchUser.getData().get(0).getId()));
            user.setUsername(twitchUser.getData().get(0).getLogin());
            user.setDisplayName(twitchUser.getData().get(0).getDisplay_name());

            userRepository.save(user);
            return user;
        }

        user = userEntity.get();
        if (!user.equalsTwitchUser(twitchUser.getData().get(0))) {
            user.setUsername(twitchUser.getData().get(0).getLogin());
            user.setDisplayName(twitchUser.getData().get(0).getDisplay_name());
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public TwitchUserModel getTwitchUser(String username) throws IOException {
        TwitchToken twitchToken = TwitchToken.getInstance();
        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);
        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUserByLogin(AUTH_HEADER, clientId, username);

        return twitchUserRequest.execute().body();

    }

    @Override
    public TwitchUserModel getTwitchUsersByIds(List<UserEntity> userEntityList) throws IOException {

        String[] userIds = userEntityList.stream().map(userEntity -> userEntity.getId().toString()).toArray(String[]::new);

        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);

        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUsersByIds(AUTH_HEADER, clientId, userIds);

        return twitchUserRequest.execute().body();

    }

    @Override
    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(UserEntity user) {
        log.info("Saving user.");
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> findAllUsersMarked(List<String> usernames) {
        log.info("Looking for all users marked in the message");

        List<UserEntity> userEntityList = userRepository.findAllByUsernameIn(usernames);

        if (userEntityList.isEmpty()) {
            log.info("Any user were marked.");
        }

        return userEntityList;
    }

    private UserFetchModel collectTwitchUsersList(Page<UserEntity> userEntityPage) throws IOException {
        TwitchUserModel twitchUsers = new TwitchUserModel();
        int totalPages = userEntityPage.getTotalPages();
        List<UserEntity> userEntityList = new ArrayList<>(userEntityPage.toList());
        int pagesPerThread = (totalPages / numberOfThreads) + 1;
        List<Thread> threads = new ArrayList<>();
        this.twitchToken = TwitchToken.getInstance();
        log.info("There are {} pages of users. ~{} users each page. Will be execute in {} threads", totalPages,
                userEntityPage.getSize(), numberOfThreads);

        log.info("Collecting twitch users from users id.");
        for (int i = 0; i < numberOfThreads; i++) {
            int threadLastPage;
            if (i == (numberOfThreads - 1)) {
                threadLastPage = totalPages - 1;
            } else {
                threadLastPage = (pagesPerThread * (i + 1)) - 1;
            }

            int threadFirstPage = pagesPerThread * i;

            Thread thread = collectThreadTwitchUserExecution(twitchUsers, userEntityList, userEntityPage, threadFirstPage, threadLastPage);

            threads.add(thread);
        }

        return new UserFetchModel(twitchUsers, userEntityList, threads);
    }

    private Thread collectThreadTwitchUserExecution(TwitchUserModel twitchUsers, List<UserEntity> userEntityList,
                                                    Page<UserEntity> userEntityPage, int threadFirstPage, int threadLastPage) {
        return new Thread(() -> {
            for (int page = threadFirstPage; page <= threadLastPage; page++) {
                try {
                    Page<UserEntity> tempUserEntityPage;

                    if (page != 0) {
                        tempUserEntityPage = userRepository.findAllByDeletedIsFalse(PageRequest.of(page, 100, Sort.by("createdAt", "id").ascending()));
                    } else {
                        tempUserEntityPage = userEntityPage;
                    }

                    TwitchUserModel tempTwitchUserModel = getTwitchUsersByIds(tempUserEntityPage.toList());

                    if (tempTwitchUserModel != null) {
                        twitchUsers.getData().addAll(tempTwitchUserModel.getData());
                        userEntityList.addAll(tempUserEntityPage.toList());
                    } else {
                        log.warn("For some reason a TwitchUserModel was null. {}", userEntityList);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void executeThreads(List<Thread> threads) {
        try {
            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {

                thread.join();

            }
        } catch (InterruptedException e) {
            log.error("An error occurred when tried to execute {} threads: {}", threads.size(), e.getMessage());
        }
    }

    private List<UserEntity> extractDifferentUsers(List<TwitchUserModelData> twitchUserModelDataList, List<UserEntity> userEntityList) {
        log.info("Checking if there are any users to be updated.");
        List<UserEntity> usersDifferent = new ArrayList<>();

        for (TwitchUserModelData userModelData : twitchUserModelDataList) {
            List<UserEntity> userDifferent = userEntityList.stream()
                    .filter(user -> user.getId() == Long.parseLong(userModelData.getId()) && !user.equalsTwitchUser(userModelData))
                    .collect(Collectors.toList());

            if (!userDifferent.isEmpty()) {
                userDifferent.get(0).fetchUserFromTwitchUser(userModelData);
                usersDifferent.add(userDifferent.get(0));
            }
        }

        log.info("Found {} users to be updated.", usersDifferent.size());
        return usersDifferent;
    }

    private List<UserEntity> extractDeletedUsers(List<TwitchUserModelData> twitchUserModelDataList, List<UserEntity> userEntityList) {
        log.info("Checking if there are any users to be deleted.");
        if (twitchUserModelDataList.size() == userEntityList.size()) {
            log.info("Any users to be deleted.");
            return Collections.emptyList();
        }

        List<UserEntity> usersDeleted = new ArrayList<>();

        for (UserEntity user : userEntityList) {
            boolean userInList = twitchUserModelDataList.stream()
                    .anyMatch(userModel -> Long.parseLong(userModel.getId()) == user.getId());

            if (!userInList) {
                usersDeleted.add(user);
            }
        }

        log.info("Found " + usersDeleted.size() + " users to be deleted.");
        return usersDeleted;
    }

    private void saveAllUsers(List<UserEntity> usersToBeUpdated) {
        log.info("Updating all different users.");
        userRepository.saveAll(usersToBeUpdated);

        log.info("Updated all users.");
    }

    private void deleteAllUsersDeleted(List<UserEntity> usersToBeSetDeleted) {
        log.info("Setting as deleted all deleted twitch users.");
        lastUserMessageService.deleteUserLastMessageByUsers(usersToBeSetDeleted);
        blacklistService.deleteAllUsers(usersToBeSetDeleted);
        usersToBeSetDeleted.forEach(user -> user.setDeleted(true));
        usersToBeSetDeleted.forEach(user -> user.setPointsToAdd(BigDecimal.ZERO));
        usersToBeSetDeleted.forEach(user -> user.setPointsAdded(BigDecimal.ZERO));
        userRepository.saveAll(usersToBeSetDeleted);

        log.info("All deleted users set as deleted.");
    }

    private String extractMessage(int numberOfUsersUpdated) {
        if (numberOfUsersUpdated == 1) return "A user was updated.";
        if (numberOfUsersUpdated > 1) return String.format("%d users were updated.", numberOfUsersUpdated);
        return "Any users were found to be updated.";
    }

    private List<Object> formatRecords(List<UserEntity> userEntityList) {
        //Make the list of list to just one list
        return Stream.of(userEntityList).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
