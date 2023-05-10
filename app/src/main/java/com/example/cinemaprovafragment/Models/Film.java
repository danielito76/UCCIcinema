package com.example.cinemaprovafragment.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable{
    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
    private String title;
    private String story;
    private String cast;
    private String duration;
    private String direction;
    private String genres;
    private String image;
    private String _id;
    private int year;

    public Film(){}
    public Film(String _id, String title, String story, String cast, String duration, String direction, String genres, String image, int year) {
        this._id = _id;
        this.title = title;
        this.story = story;
        this.cast = cast;
        this.duration = duration;
        this.direction = direction;
        this.genres = genres;
        this.image = image;
        this.year = year;
    }

    protected Film(Parcel in) {
        _id = in.readString();
        title = in.readString();
        story = in.readString();
        genres = in.readString();
        cast = in.readString();
        image = in.readString();
        duration = in.readString();
        year = in.readInt();
        direction = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(story);
        dest.writeString(genres);
        dest.writeString(cast);
        dest.writeString(image);
        dest.writeString(duration);
        dest.writeInt(year);
        dest.writeString(direction);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String getTitle() {
        return title;
    }

    public String getStory() {
        return story;
    }

    public String getCast() {
        return cast;
    }

    public String getDuration() {
        return duration;
    }

    public String getDirection() {
        return direction;
    }

    public String getGenres() {
        return genres;
    }

    public String getImage() {
        return image;
    }

    public String get_id() {
        return _id;
    }

    public int getYear() {
        return year;
    }
}
