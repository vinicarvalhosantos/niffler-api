package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import br.com.santos.vinicius.nifflerapi.runnable.UserDeleteRunnable;
import br.com.santos.vinicius.nifflerapi.runnable.UserSaveRunnable;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchRequestsRetrofit;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Value("${twitch.client.id}")
    String CLIENT_ID;

    @Value("${threads.to.be.used}")
    private int NUMBER_OF_TRHEADS;

    @Override
    public ResponseEntity<Response> getAllUsers() {
        log.info("Getting all users.");
        List<UserEntity> userEntities = IteratorUtils.toList(userRepository.findAll().iterator());

        if (userEntities.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Any users in our database.", 404, HttpStatus.NOT_FOUND.name());
            log.info("Any users in our database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }

        SuccessResponse successResponse = new SuccessResponse(formatRecords(userEntities), "Users were found in our database");
        log.info("Users were found in our database.");
        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public ResponseEntity<Response> fetchAllUsers() throws IOException {
        log.info("Fetching all users in database.");
        List<UserEntity> userEntityList = IteratorUtils.toList(userRepository.findAll().iterator());

        log.info("Trying to find all twitch users by UserID");
        TwitchUserModel twitchUsers = getTwitchUsersByIds(userEntityList);


        log.info("Check the result from twitch and fetch users in database");
        if (twitchUsers != null) {
            return fetchUsersExisting(twitchUsers, userEntityList);
        }

        ErrorResponse errorResponse = new ErrorResponse("Twitch bad request.", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(errorResponse));
    }

    @Override
    public UserEntity fetchUserByUsername(String username) throws IOException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        TwitchUserModel twitchUser = this.getTwitchUser(username);

        return fetchUser(userEntity, twitchUser);
    }

    private ResponseEntity<Response> fetchUsersExisting(TwitchUserModel twitchUsers, List<UserEntity> userEntityList) {
        int numberOfUsersUpdated = fetchUsers(twitchUsers.getData(), userEntityList);
        String message = extractMessage(numberOfUsersUpdated);

        SuccessResponse successResponse = new SuccessResponse(Collections.emptyList(), message);

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    private UserEntity fetchUser(Optional<UserEntity> userEntity, TwitchUserModel twitchUser) {

        if (twitchUser.getData().isEmpty())
            return null;

        return getUserFetched(userEntity, twitchUser);
    }

    private int fetchUsers(List<TwitchUserModelData> twitchUserModelDataList, List<UserEntity> userEntityList) {
        List<UserEntity> usersDifferent = extractDifferentUsers(twitchUserModelDataList, userEntityList);
        List<UserEntity> usersDeleted = extractDeletedUsers(twitchUserModelDataList, userEntityList);

        if (usersDifferent.size() != 0) {
            saveAllUsers(usersDifferent);
        }

        if (usersDeleted.size() != 0) {
            deleteAllUsersDeleted(usersDeleted);
        }

        return usersDifferent.size() + usersDeleted.size();
    }

    private UserEntity getUserFetched(Optional<UserEntity> userEntity, TwitchUserModel twitchUser) {
        UserEntity user = new UserEntity();
        if (userEntity.isEmpty()) {
            user.setUserId(Long.parseLong(twitchUser.getData().get(0).getId()));
            user.setUsername(twitchUser.getData().get(0).getLogin());
            user.setDisplayName(twitchUser.getData().get(0).getDisplay_name());

            userRepository.save(user);
            return user;
        }

        user = userEntity.get();
        if (!user.equalsTwitchUser(twitchUser.getData().get(0))) {
            user.setUsername(twitchUser.getData().get(0).getLogin());
            user.setDisplayName(twitchUser.getData().get(0).getDisplay_name());
        }

        return user;
    }

    @Override
    public TwitchUserModel getTwitchUser(String username) throws IOException {
        TwitchToken twitchToken = TwitchToken.getInstance();
        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);
        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUserByLogin(AUTH_HEADER, CLIENT_ID, username);

        return twitchUserRequest.execute().body();
    }

    @Override
    public TwitchUserModel getTwitchUsersByIds(List<UserEntity> userEntityList) throws IOException {
        String[] userIds = userEntityList.stream().map(userEntity -> userEntity.getUserId().toString()).toArray(String[]::new);
        TwitchToken twitchToken = TwitchToken.getInstance();
        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);

        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUsersByIds(AUTH_HEADER, CLIENT_ID, userIds);

        return twitchUserRequest.execute().body();
    }

    private List<UserEntity> extractDifferentUsers(List<TwitchUserModelData> twitchUserModelDataList, List<UserEntity> userEntityList) {
        log.info("Checking if there are any users to be updated.");
        List<UserEntity> usersDifferent = new ArrayList<>();

        for (TwitchUserModelData userModelData : twitchUserModelDataList) {
            List<UserEntity> userDifferent = userEntityList.stream()
                    .filter(user -> user.getUserId() == Long.parseLong(userModelData.getId()) && !user.equalsTwitchUser(userModelData))
                    .collect(Collectors.toList());

            if (!userDifferent.isEmpty())
                usersDifferent.add(userDifferent.get(0).fetchUserFromTwitchUser(userModelData));
        }

        log.info("Found " + usersDifferent.size() + " users to be updated.");
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
                    .anyMatch(userModel -> Long.parseLong(userModel.getId()) == user.getUserId());

            if (!userInList) {
                usersDeleted.add(user);
            }
        }

        log.info("Found " + usersDeleted.size() + " users to be deleted.");
        return usersDeleted;
    }

    private void saveAllUsers(List<UserEntity> usersToBeUpdated) {
        log.info("Updating all different users.");
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_TRHEADS);

        try {
            Runnable worker = new UserSaveRunnable(usersToBeUpdated, userRepository);
            executor.execute(worker);
            executor.shutdown();

            executor.awaitTermination(10000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Updated all users.");
    }

    private void deleteAllUsersDeleted(List<UserEntity> usersToBeUpdated) {
        log.info("Deleting all deleted users from twitch.");
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_TRHEADS);

        try {
            Runnable worker = new UserDeleteRunnable(usersToBeUpdated, userRepository);
            executor.execute(worker);
            executor.shutdown();

            executor.awaitTermination(10000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Deleted all users.");
    }

    private String extractMessage(int numberOfUsersUpdated) {
        return (numberOfUsersUpdated == 1) ? "A user was updated."
                : (numberOfUsersUpdated > 1) ? String.format("%d users were updated.", numberOfUsersUpdated)
                : "Any users were found to be updated.";
    }

    private List<Object> formatRecords(List<UserEntity> userEntityList) {
        //Make the list of list to just one list
        return Stream.of(userEntityList).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
