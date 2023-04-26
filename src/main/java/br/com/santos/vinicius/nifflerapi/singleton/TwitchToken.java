package br.com.santos.vinicius.nifflerapi.singleton;

import br.com.santos.vinicius.nifflerapi.model.TwitchTokenModel;
import br.com.santos.vinicius.nifflerapi.request.TwitchRequests;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import retrofit2.Call;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Indexed
public class TwitchToken {

    private final String CLIENT_ID = System.getenv("CLIENT_ID");


    private final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");

    private static final Long PRE_TOKEN_EXPIRED = 5L; //In minutes

    private static TwitchToken singleInstance = null;

    public String token;

    private static Date expiresAt;

    private TwitchToken() throws IOException {
        log.debug("Requesting a new valid twitch token");

        TwitchTokenModel twitchTokenModel = this.getTwitchToken();

        Calendar expiresAt = Calendar.getInstance();
        Date now = new Date();

        log.debug("Extracting informations from twitch's api response ");
        expiresAt.setTimeInMillis(now.getTime() + twitchTokenModel.getExpires_in());
        setExpiresAt(expiresAt.getTime());
        this.token = twitchTokenModel.getAccess_token();
        log.debug("New valid twitch token stored");
    }

    public static TwitchToken getInstance() throws IOException {

        log.debug("Checking if there is a valid twitch token");
        if (singleInstance == null && isExpiring()) {
            singleInstance = new TwitchToken();
        }

        return singleInstance;
    }

    private static boolean isExpiring() {
        log.info("Checking if the token expired");
        Date now = new Date();
        if (expiresAt == null) {
            return true;
        }

        if (expiresAt.after(now)) {
            return true;
        }


        long differenceInSeconds = (now.getTime() - expiresAt.getTime()) / 1000;

        return differenceInSeconds <= (60 * PRE_TOKEN_EXPIRED);
    }

    private TwitchTokenModel getTwitchToken() throws IOException {

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        TwitchRequests twitchRequests = requestsRetrofit.twitchAuthRequests;

        log.debug("Sending a request to oauth twitch api");
        String GRANT_TYPE = "client_credentials";
        Call<TwitchTokenModel> twitchTokenModelCall = twitchRequests.getTwitchToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);


        return twitchTokenModelCall.execute().body();
    }

    public static void setExpiresAt(Date expiresAt) {
        TwitchToken.expiresAt = expiresAt;
    }
}
