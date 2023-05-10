package com.example.cinemaprovafragment.Models;

public class Regione {
    private  String _id;
    private String name;

    public Regione(String _id,String name) {
        this._id = _id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return name;
    }
}
