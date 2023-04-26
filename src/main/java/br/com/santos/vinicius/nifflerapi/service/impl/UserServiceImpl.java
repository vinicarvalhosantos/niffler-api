package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchRequestsRetrofit;
import br.com.santos.vinicius.nifflerapi.singleton.TwitchToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Value("${twitch.client.id}")
    String CLIENT_ID;

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
        Call<TwitchUserModel> twitchUserRequest = requestsRetrofit.twitchHelixRequests.getTwitchUser(username, AUTH_HEADER, CLIENT_ID);

        return twitchUserRequest.execute().body();
    }
}
