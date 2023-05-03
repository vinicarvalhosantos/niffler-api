package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.dto.UserDto;
import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchRequestsRetrofit;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchToken;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Value("${twitch.client.id}")
    String CLIENT_ID;

    @Override
    public ResponseEntity<Response> createUser(UserDto userDto) throws IOException {
        if (!userDto.isValidDto()) {
            ErrorResponse errorResponse = new ErrorResponse("User displayname or user id cannot be null, empty or zero.",
                    400, HttpStatus.BAD_REQUEST.name());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(errorResponse));
        }

        UserEntity user;
        if (userDto.isValidUserId()) {
            user = fetchUserByUserId(userDto.getUserId());
        } else {
            user = fetchUserByUsername(userDto.getDisplayName().toLowerCase()); //All username is the display name but in lower case.
        }

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse("User not found!", HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.name());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }

        SuccessResponse successResponse = new SuccessResponse(List.of(user), "User were created or updated!");

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(successResponse));
    }

    @Override
    public ResponseEntity<Response> getAllUsers() {
        List<UserEntity> userEntities = IteratorUtils.toList(userRepository.findAll().iterator());

        if (userEntities.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Any users in our database", 404, HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }

        SuccessResponse successResponse = new SuccessResponse(formatRecords(userEntities), "Users were found in our database");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public UserEntity findUserByUserId(Long id) {
        return null;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return null;
    }

    @Override
    public UserEntity fetchUserByUsername(String username) throws IOException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        TwitchUserModel twitchUser = this.getTwitchUser(username);

        return fetchUser(userEntity, twitchUser);
    }

    @Override
    public UserEntity fetchUserByUserId(Long userId) throws IOException {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);
        TwitchUserModel twitchUser = this.getTwitchUser(userId);

        return fetchUser(userEntity, twitchUser);
    }

    private UserEntity fetchUser(Optional<UserEntity> userEntity, TwitchUserModel twitchUser) {

        if (twitchUser.getData().isEmpty())
            return null;

        return getUserFetched(userEntity, twitchUser);
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
        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUserByLogin(username, AUTH_HEADER, CLIENT_ID);

        return twitchUserRequest.execute().body();
    }

    @Override
    public TwitchUserModel getTwitchUser(Long userId) throws IOException {
        TwitchToken twitchToken = TwitchToken.getInstance();
        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);
        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUserById(userId.toString(), AUTH_HEADER, CLIENT_ID);

        return twitchUserRequest.execute().body();
    }

    private List<Object> formatRecords(List<UserEntity> userEntityList) {
        //Make the list of list to just one list
        return Stream.of(userEntityList).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
