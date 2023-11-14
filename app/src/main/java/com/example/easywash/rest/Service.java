package com.example.easywash.rest;

import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("id")
    private String id;
    @SerializedName("client")
    private String client;
    @SerializedName("car")
    private String car;
    @SerializedName("service")
    private String service;
    @SerializedName("date")
    private String date;
    @SerializedName("total")
    private String total;
    @SerializedName("status")
    private String status;
    @SerializedName("paymethod")
    private String paymethod;

    public Service(String client, String car, String service, String total) {
        this.client = client;
        this.car = car;
        this.service = service;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }
}
