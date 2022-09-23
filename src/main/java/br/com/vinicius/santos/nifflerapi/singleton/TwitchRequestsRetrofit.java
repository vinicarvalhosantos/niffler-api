package br.com.vinicius.santos.nifflerapi.singleton;

import br.com.vinicius.santos.nifflerlib.request.TwitchRequests;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class TwitchRequestsRetrofit {

    private static TwitchRequestsRetrofit singleInstance = null;

    public TwitchRequests twitchAuthRequests;

    public TwitchRequests twitchHelixRequests;

    private TwitchRequestsRetrofit() throws IOException {
        final String BASE_AUTH_URL = System.getenv("TWITCH_AUTH_BASE_URL");
        final String BASE_HELIX_URL = System.getenv("TWITCH_HELIX_BASE_URL");

        this.twitchAuthRequests = this.buildRetrofit(BASE_AUTH_URL).create(TwitchRequests.class);
        this.twitchHelixRequests = this.buildRetrofit(BASE_HELIX_URL).create(TwitchRequests.class);
    }

    public static TwitchRequestsRetrofit getInstance() throws IOException {
        if (singleInstance == null) {
            singleInstance = new TwitchRequestsRetrofit();
        }

        return singleInstance;
    }

    private Retrofit buildRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
