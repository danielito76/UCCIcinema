package com.example.cinemaprovafragment.Models;

public class User {
    String _id;
    String username;

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public User(String _id, String username) {
        this._id = _id;
        this.username = username;
    }
}
