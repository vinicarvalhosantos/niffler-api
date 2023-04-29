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
    Call<TwitchUserModel> getTwitchUserByLogin(@Query("login") String username, @Header("Authorization") String authHeader,
                                        @Header("client-id") String clientId);

    @GET("users")
    Call<TwitchUserModel> getTwitchUserById(@Query("id") String userId, @Header("Authorization") String authHeader,
                                        @Header("client-id") String clientId);

}
