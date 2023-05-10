package com.example.cinemaprovafragment.Models;

import java.util.Date;

public class Recensione {
    String _id;
    User user;
    float rating;
    String comment;
    Date date;
    String film;

    public Recensione(String _id, User user, float rating, String comment, Date date, String film) {
        this._id = _id;
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.film = film;
    }

    public String get_id() {
        return _id;
    }

    public User getUser() {
        return user;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }
}
