package com.h2o.easywash.rest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/easyrest/clients/{id}")
    Call<User> getUsertById(@Path("id") String userId);

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
    @PATCH("/easyrest/clients/{id}/")
    Call<User> updateUser(@Path("id") String userId, @Body User updatedUser);
    @DELETE("/easyrest/clients/{id}/")
    Call<Void> deleteUser(@Path("id") String userid);
    @GET("/easyrest/service_tickets")
    Call<Service[]> getServiceList(@Query("format") String format);
    @GET("/easyrest/service_tickets/{id}")
    Call<Service> getTicketById(@Path("id") String ticketId);


}
