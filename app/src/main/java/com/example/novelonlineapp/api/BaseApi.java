package com.example.novelonlineapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApi {
    public static final String BASE_URL = "http://18.162.110.201";
    public static Retrofit retrofit = null;


    public static Retrofit getClient(){

        Gson gson = new GsonBuilder().setLenient().create();
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
