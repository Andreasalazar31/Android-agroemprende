package com.example.interfazandroid.modelApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    Call<Token> getlogin(
            @Field("email") String email,
            @Field("contrasena") String contrasena
    );
    @POST("auth/usuario")
    Call<Void> registerUser (@Body RegisterRequest registerRequest);

    @GET("auth/profile")
    Call<UserDetails> getUserDetails(@Header("Authorization") String token);
}
