package com.example.cinemaprovafragment.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Location implements Parcelable {
    String type;
    double[] coordinates;

    public Location(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    protected Location(Parcel in) {
        type = in.readString();
        coordinates = in.createDoubleArray();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public double getLog(){
        return coordinates[0];
    }

    public double getLat(){
        return coordinates[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeDoubleArray(coordinates);
    }
}
