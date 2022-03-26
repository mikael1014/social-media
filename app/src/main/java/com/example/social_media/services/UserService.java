package com.example.social_media.services;

import com.example.social_media.models.LoginRequest;
import com.example.social_media.models.Utilisateur;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
    @POST(Config.BASE_URL + "login")
    Call<Void> login(@Body LoginRequest loginRequest);

    @Multipart
    @POST(Config.BASE_URL + "api/register")
    Call<ResponseBody> register(@Part("user") Utilisateur user, @Part MultipartBody.Part avatar);

    @GET(Config.BASE_URL + "api/demandes/friends")
    Call<Map<String, Object>> getListAmis();

    @GET(Config.BASE_URL + "api/publication/mespublications?size=20")
    Call<Map<String, Object>> getListPublication();

    @Multipart
    @PUT(Config.BASE_URL + "api/profile")
    Call<ResponseBody> updateUser(@Part("user") Utilisateur user, @Part MultipartBody.Part avatar);
}
