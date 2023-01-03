package br.com.vinicius.santos.nifflerapi.service.impl;

import br.com.vinicius.santos.nifflerapi.service.BlacklistService;
import br.com.vinicius.santos.nifflerapi.singleton.TwitchRequestsRetrofit;
import br.com.vinicius.santos.nifflerapi.singleton.TwitchToken;
import br.com.vinicius.santos.nifflerlib.model.TwitchUserModel;
import br.com.vinicius.santos.nifflerlib.model.TwitchUserModelData;
import br.com.vinicius.santos.nifflerlib.model.dto.BlacklistDto;
import br.com.vinicius.santos.nifflerlib.model.entity.BlacklistEntity;
import br.com.vinicius.santos.nifflerlib.model.entity.UserEntity;
import br.com.vinicius.santos.nifflerlib.repository.BlacklistRepository;
import br.com.vinicius.santos.nifflerlib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<BlacklistEntity> addUserInBlacklist(BlacklistDto blacklistDto) throws IOException {

        String formattedUsername = blacklistDto.getUsername().toLowerCase(Locale.ROOT);

        boolean isUserInBlacklist = this.fetchIsUserInBlacklist(formattedUsername);

        if (isUserInBlacklist) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }

        final String CLIENT_ID = System.getenv("CLIENT_ID");

        TwitchToken twitchToken = TwitchToken.getInstance();
        final String TOKEN = twitchToken.token;

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        final String AUTH_HEADER = String.format("Bearer %s", TOKEN);

        Call<TwitchUserModel> listCall = requestsRetrofit.twitchHelixRequests.getTwitchUser(formattedUsername, AUTH_HEADER, CLIENT_ID);
        TwitchUserModel twitchUserModel = listCall.execute().body();

        if (twitchUserModel == null || twitchUserModel.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserEntity userEntity = getUserEntity(twitchUserModel.getData().get(0));
        userEntity.setPointsToAdd(0.0);

        BlacklistEntity blacklistEntity = new BlacklistEntity(userEntity.getUsername(), userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.blacklistRepository.save(blacklistEntity));
    }

    @Override
    public ResponseEntity<List<BlacklistEntity>> getBlacklist() {
        List<BlacklistEntity> blacklistEntityList = this.blacklistRepository.findAll(Sort.by(Sort.Direction.ASC, "user_id"));

        return ResponseEntity.status(HttpStatus.OK).body(blacklistEntityList);
    }

    private UserEntity getUserEntity(TwitchUserModelData twitchUserModel) {

        Long userId = Long.valueOf(twitchUserModel.getId());
        String username = twitchUserModel.getLogin();
        String displayName = twitchUserModel.getDisplay_name();
        Double pointsToAdd = 0.0;
        Double pointsAdded = 0.0;
        Optional<UserEntity> userEntity = this.userRepository.findById(userId);

        return userEntity.orElseGet(() -> userRepository.save(new UserEntity(userId, username, displayName, pointsToAdd, pointsAdded)));
    }

    private boolean fetchIsUserInBlacklist(String username) {
        Optional<UserEntity> userEntityOptional = this.userRepository.findUserEntityByUsername(username);
        Optional<BlacklistEntity> blacklistEntityOptional = this.blacklistRepository.findBlacklistEntityByUsername(username);

        if (userEntityOptional.isPresent() && blacklistEntityOptional.isPresent()) {
            return true;
        }

        if (!userEntityOptional.isPresent() && blacklistEntityOptional.isPresent()) {

            BlacklistEntity blacklistEntity = blacklistEntityOptional.get();
            Optional<UserEntity> entityOptional = this.userRepository.findById(blacklistEntity.getUser().getId());

            if (entityOptional.isPresent()) {

                UserEntity userEntity = entityOptional.get();
                blacklistEntity.setUser(userEntity);
                blacklistEntity.setUsername(userEntity.getUsername());

                this.blacklistRepository.save(blacklistEntity);

                return true;
            }

        } else if (userEntityOptional.isPresent()) {

            UserEntity userEntity = userEntityOptional.get();
            Optional<BlacklistEntity> entityOptional = this.blacklistRepository.findBlacklistEntityByUser_Id(userEntity.getId());

            if (entityOptional.isPresent()) {

                BlacklistEntity blacklistEntity = entityOptional.get();
                blacklistEntity.setId(blacklistEntity.getId());
                blacklistEntity.setUser(userEntity);
                blacklistEntity.setUsername(userEntity.getUsername());

                this.blacklistRepository.save(blacklistEntity);

                return true;

            }
        }

        return false;
    }

}
