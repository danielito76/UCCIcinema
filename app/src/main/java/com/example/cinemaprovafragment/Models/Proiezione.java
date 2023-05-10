package com.example.cinemaprovafragment.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Proiezione{
    String _id;
    Date date;
    Film film;
    Cinema cinema;
    private ArrayList<String> time;

    public Proiezione(){}
    public Proiezione(String _id, Date date, Film film, Cinema cinema, ArrayList<String> time) {
        this._id = _id;
        this.date = date;
        this.film = film;
        this.cinema = cinema;
        this.time = time;
    }


    public String get_id() {
        return _id;
    }

    public Date getDate() {
        return date;
    }

    public Film getFilm() {
        return film;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public ArrayList<String> getTime() {
        return time;
    }
}