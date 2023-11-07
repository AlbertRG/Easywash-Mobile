package com.example.easywash.rest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    Call<Car[]> getCarsList(@Query("format") String format);
    @POST("/easyrest/cars/")
    Call<Car> sendCar(@Body Car car);
    @GET("/easyrest/cars/{id}")
    Call<Car> getCarById(@Path("id") String carId);
    @PATCH("/easyrest/cars/{id}/")
    Call<Car> updateCar(@Path("id") String carId, @Body Car updatedCar);
    @DELETE("/easyrest/cars/{id}/")
    Call<Void> deleteCar(@Path("id") String carId);


}
