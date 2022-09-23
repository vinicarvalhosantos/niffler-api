package br.com.vinicius.santos.nifflerapi.singleton;

import br.com.vinicius.santos.nifflerlib.model.TwitchTokenModel;
import br.com.vinicius.santos.nifflerlib.request.TwitchRequests;
import retrofit2.Call;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TwitchToken {

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
    }

    public static TwitchToken getInstance() throws IOException {
        Date now = new Date();
        if (singleInstance == null && isExpiring(now)) {
            singleInstance = new TwitchToken();
        }

        return singleInstance;
    }

    private static boolean isExpiring(Date now) {
        if (expiresAt == null) {
            return true;
        }

        if (expiresAt.after(now)) {
            return true;
        }


        long differenceInSeconds = (now.getTime() - expiresAt.getTime()) / 1000;

        return differenceInSeconds <= 15;
    }

    private TwitchTokenModel getTwitchToken() throws IOException {
        final String CLIENT_ID = System.getenv("CLIENT_ID");
        final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
        final String GRANT_TYPE = "client_credentials";
        TwitchRequestsRetrofit requestsRetrofit = TwitchRequestsRetrofit.getInstance();

        TwitchRequests twitchRequests = requestsRetrofit.twitchAuthRequests;

        Call<TwitchTokenModel> twitchTokenModelCall = twitchRequests.getTwitchToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);

        return twitchTokenModelCall.execute().body();
    }

    public static void setExpiresAt(Date expiresAt) {
        TwitchToken.expiresAt = expiresAt;
    }
}
