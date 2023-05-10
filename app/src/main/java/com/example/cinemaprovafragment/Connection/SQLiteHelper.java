package com.example.cinemaprovafragment.Connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cinemaprovafragment.Models.Film;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "preferitiDb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Film";
    private static final String ID = "id";
    private static final String TITLE = "title";

    private static final String STORY = "story";

    private static final String CAST = "cast";
    private static final String DURATION = "duration";
    private static final String DIRECTION = "direction";
    private static final String GENRES = "genres";
    private static final String IMAGE = "image";
    private static final String YEAR = "year";

    public SQLiteHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " TEXT, "
                + TITLE + " TEXT,"
                + STORY + " TEXT, "
                + CAST + " TEXT, "
                + DURATION + " TEXT, "
                + DIRECTION + " TEXT, "
                + GENRES + " TEXT, "
                + IMAGE + " TEXT, "
                + YEAR + " INTEGER)";
        db.execSQL(query);
    }


    public void addNewFilm(Film film) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID, film.get_id());
        values.put(TITLE, film.getTitle());
        values.put(STORY, film.getStory());
        values.put(CAST, film.getCast());
        values.put(DURATION, film.getDuration());
        values.put(DIRECTION, film.getDirection());
        values.put(GENRES, film.getGenres());
        values.put(IMAGE, film.getImage());
        values.put(YEAR, film.getYear());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Film> readFilms()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Film> filmsmodalarraylist = new ArrayList<>();


        if (cursorCourses.moveToFirst()) {
            do {
                filmsmodalarraylist.add(new Film(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(5),
                        cursorCourses.getString(6),
                        cursorCourses.getString(7),
                        cursorCourses.getInt(8)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return filmsmodalarraylist;
    }

    public boolean isInFavorite(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = ?";
        Cursor cursorCourses = db.rawQuery(query,new String[]{id});

        if(cursorCourses.getCount() == 0){
            cursorCourses.close();
            return false;
        }
        cursorCourses.close();
        return true;
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, ID + "= ?", new String[]{id});
    }
}
