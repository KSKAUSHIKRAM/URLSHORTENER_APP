package com.epicgamers.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/shorten")
    Call<UrlResponse> shortenUrl(@Body UrlRequest request);
}