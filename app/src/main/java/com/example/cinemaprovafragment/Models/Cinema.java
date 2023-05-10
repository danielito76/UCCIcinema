package com.example.cinemaprovafragment.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Cinema implements Parcelable{
    String _id;
    String name;
    String address;
    String services;
    String image;
    int room;
    String city;
    String province;
    String cap;
    Location location;
    private LatLng cinemaLatLng;
    private String region;

    public Cinema(String _id, String name, String address, String services, String image, int room, String city, String province, String cap, Location location, String region) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.services = services;
        this.image = image;
        this.room = room;
        this.city = city;
        this.province = province;
        this.cap = cap;
        this.location = location;
        this.region = region;
    }

    protected Cinema(Parcel in) {
        _id = in.readString();
        name = in.readString();
        address = in.readString();
        services = in.readString();
        image = in.readString();
        room = in.readInt();
        city = in.readString();
        province = in.readString();
        cap = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        region = in.readString();
    }

    public static final Creator<Cinema> CREATOR = new Creator<Cinema>() {
        @Override
        public Cinema createFromParcel(Parcel in) {
            return new Cinema(in);
        }

        @Override
        public Cinema[] newArray(int size) {
            return new Cinema[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getServices() {
        return services;
    }

    public String getImage() {
        return image;
    }

    public int getRoom() {
        return room;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCap() {
        return cap;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(_id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(services);
        parcel.writeString(image);
        parcel.writeInt(room);
        parcel.writeString(city);
        parcel.writeString(province);
        parcel.writeString(cap);
        parcel.writeParcelable(location, i);
        parcel.writeString(region);
    }

    public LatLng getLatLng(){
        return new LatLng(location.getLat(), location.getLog());
    }

    public String getRegion(){
        return region;
    }
}
