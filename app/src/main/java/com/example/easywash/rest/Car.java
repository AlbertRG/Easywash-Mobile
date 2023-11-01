package com.example.easywash.rest;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("id")
    String id;
    @SerializedName("owner")
    String owner;
    @SerializedName("plate")
    String plate;
    @SerializedName("model")
    String model;
    @SerializedName("year")
    String year;
    @SerializedName("color")
    String color;

    public Car(String owner, String plate, String model, String year, String color) {
        this.owner = owner;
        this.plate = plate;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
