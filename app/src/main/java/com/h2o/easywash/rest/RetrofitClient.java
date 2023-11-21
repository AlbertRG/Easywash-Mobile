package com.h2o.easywash.rest;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public  Retrofit retrofit;

    public  Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://easyrest.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d("COMMENT",retrofit.baseUrl().toString());
        return retrofit;
    }
}
