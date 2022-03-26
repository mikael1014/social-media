package com.example.social_media.services;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    // AVD manager 10.0.2.2:8080
    // phone devise 192.168.194.38
    public static final String BASE_URL = "http://10.0.2.2:8081/";
    //public static final String BASE_URL = "http://192.168.1.66:8081/";
    //public static final String BASE_URL = "http://192.168.1.66:8080/social-media/api/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit setInterceptor(Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
            return retrofit;
    }

}
