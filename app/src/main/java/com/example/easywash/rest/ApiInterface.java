package com.example.easywash.rest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("/easyrest/clients")
    Call<User[]> getUserInformation(@Field("name") String name, @Field("last_name") String lastname, @Field("phone") String phone, @Field("email") String email, @Field("password") String password);

    @Headers("Content-Type: application/json")
    @POST("/easyrest/clients/")
    Call<User> enviarDatos(@Body User user);
    @GET("/easyrest/clients")
    Call<User[]> getClientsList(@Query("format") String format);

    @GET("/easyrest/cars")
    Call<User[]> getCarsList(@Query("format") String format);
    @POST("/easyrest/cars/")
    Call<User> sendCar(@Body Car car);

}
