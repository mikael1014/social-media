package com.example.social_media.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SessionManager {

    public  SharedPreferences refs;
    private Context context;
    private  String token;
    private JWT jwt;

    public SessionManager(Context context) {
        this.context = context;
        this.refs = context.getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);;

    }

    public void saveAuthToken(String jwtToken) {
        this.token = jwtToken;
        this.jwt = new JWT(token);
        refs.edit().putString("user_token", jwtToken).apply();
    }

    public void saveUserDetails() {
        List<String> info = this.getUserDetails();
        refs.edit().putLong("id", Long.parseLong(info.get(0))).apply();
        refs.edit().putString("email", this.getUserName()).apply();
        refs.edit().putString("nom", info.get(1)).apply();
        refs.edit().putString("prenom", info.get(2)).apply();
        refs.edit().putString("fullName", this.getFullName()).apply();
        refs.edit().putString("role", this.getRole()).apply();
        refs.edit().putString("description", info.get(3)).apply();
        refs.edit().putString("avatar", info.get(4)).apply();
    }

    private String getFullName() {
        List<String> info = this.getUserDetails();
        return FunctionsUtils.capitalizeFullName(info.get(1) , info.get(2));
    }

    public String getAuthToken() {
        return refs.getString("user_token", null);
    }

    public String getUserName() {
        if (this.token != null) {
            return jwt.getSubject();
        }
        return null;

    }

    public String getRole() {
        if (this.token != null) {
            return jwt.getClaim("role").asString();
        }
        return null;

    }

    public List<String> getUserDetails() {
        if (this.token != null) {
            return jwt.getClaim("user").asList(String.class);
        }
        return null;

    }
}
