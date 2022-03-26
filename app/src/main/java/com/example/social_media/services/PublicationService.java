package com.example.social_media.services;

import com.example.social_media.models.Publication;
import com.example.social_media.models.Utilisateur;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PublicationService {

    @GET(Config.BASE_URL + "api/publication?size=20")
    Call<Map<String, Object>> getAllPublication();

    @GET(Config.BASE_URL + "api/publication/{id}")
    Call<Publication> getPublicationById(@Path(value = "id") long id);

    @Multipart
    @PUT(Config.BASE_URL + "api/publication/{id}")
    Call<Void> updatePublication(@Path(value = "id") long id, @Part("publication") Publication pub, @Part MultipartBody.Part media);


    @Multipart
    @POST(Config.BASE_URL + "api/publication")
    Call<ResponseBody> savePublication(@Part("publication") Publication pub, @Part MultipartBody.Part media);

    @DELETE(Config.BASE_URL + "api/publication/{id}")
    Call<Void> deletePublication(@Path(value = "id") long id);
}
