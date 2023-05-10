package com.example.cinemaprovafragment.Models;

public class Provincia{
    String _id;
    String name;
    Regione region;

    public Provincia(String _id,String name,Regione region) {
        this._id = _id;
        this.name = name;
        this.region =region;
    }

    public String get_id(){
        return _id;
    }

    public String getName() {
        return name;
    }

    public Regione getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return name;
    }
}
