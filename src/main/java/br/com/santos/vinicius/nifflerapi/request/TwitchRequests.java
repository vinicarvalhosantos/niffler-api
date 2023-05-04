package br.com.santos.vinicius.nifflerapi.request;

import br.com.santos.vinicius.nifflerapi.model.TwitchTokenModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitchRequests {

    @POST("token")
    Call<TwitchTokenModel> getTwitchToken(@Query("client_id") String clientId, @Query("client_secret") String clientSecret,
                                          @Query("grant_type") String grantType);

    @GET("users")
    Call<TwitchUserModel> getTwitchUserByLogin(@Header("Authorization") String authHeader, @Header("client-id") String clientId,
                                               @Query("login") String username);

    @GET("users")
    Call<TwitchUserModel> getTwitchUserById(@Header("Authorization") String authHeader, @Header("client-id") String clientId,
                                            @Query("id") String userId);

    @GET("users")
    Call<TwitchUserModel> getTwitchUsersByIds(@Header("Authorization") String authHeader, @Header("client-id") String clientId,
                                              @Query("id") String... userId);

}
