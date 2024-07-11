package com.example.interfazandroid.modelApi;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
