package com.example.easywash.rest;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class CarParcelable implements Parcelable {
    private String id;
    private String owner;
    private String plate;
    private String model;
    private String year;
    private String color;

    // Constructor
    public CarParcelable(String owner, String plate, String model, String year, String color) {
        this.owner = owner;
        this.plate = plate;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    // Métodos Getter y Setter (puedes generarlos automáticamente en tu IDE)

    // Implementación de Parcelable
    protected CarParcelable(Parcel in) {
        owner = in.readString();
        plate = in.readString();
        model = in.readString();
        year = in.readString();
        color = in.readString();
    }

    public static final Creator<CarParcelable> CREATOR = new Creator<CarParcelable>() {
        @Override
        public CarParcelable createFromParcel(Parcel in) {
            return new CarParcelable(in);
        }

        @Override
        public CarParcelable[] newArray(int size) {
            return new CarParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(owner);
        dest.writeString(plate);
        dest.writeString(model);
        dest.writeString(year);
        dest.writeString(color);
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