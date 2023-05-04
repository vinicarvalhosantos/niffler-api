package br.com.santos.vinicius.nifflerapi.singleton;

import br.com.santos.vinicius.nifflerapi.model.TwitchTokenModel;
import br.com.santos.vinicius.nifflerapi.request.TwitchRequests;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TwitchToken {

    private final String CLIENT_ID = System.getenv("CLIENT_ID");


    private final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");

    private static final Long PRE_TOKEN_EXPIRED = 5L; //In minutes

    private static TwitchToken singleInstance = null;

    public String token;

    private static Date expiresAt;

    private TwitchToken() throws IOException {
        TwitchTokenModel twitchTokenModel = this.getTwitchToken();

        Calendar expiresAt = Calendar.getInstance();
        Date now = new Date();

        expiresAt.setTimeInMillis(now.getTime() + twitchTokenModel.getExpires_in());
        setExpiresAt(expiresAt.getTime());
        this.token = twitchTokenModel.getAccess_token();
        log.info("New valid twitch token stored.");
    }

    public static TwitchToken getInstance() throws IOException {

        log.info("Checking if there is a valid twitch token.");
        if (singleInstance == null && isExpiring()) {
            singleInstance = new TwitchToken();
        }

        return singleInstance;
    }

    private static boolean isExpiring() {
        Date now = new Date();
        if (expiresAt == null || expiresAt.after(now)) {
            log.info("Twitch token expired.");
            return true;
        }


        long differenceInSeconds = (now.getTime() - expiresAt.getTime()) / 1000;

        return differenceInSeconds <= (60 * PRE_TOKEN_EXPIRED);
    }

    private TwitchTokenModel getTwitchToken() throws IOException {
        log.info("Requesting a new valid twitch token.");

        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        TwitchRequests twitchRequests = requestsRetrofit.twitchAuthRequests;

        String GRANT_TYPE = "client_credentials";
        Call<TwitchTokenModel> twitchTokenModelCall = twitchRequests.getTwitchToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);


        return twitchTokenModelCall.execute().body();
    }

    public static void setExpiresAt(Date expiresAt) {
        TwitchToken.expiresAt = expiresAt;
    }
}
