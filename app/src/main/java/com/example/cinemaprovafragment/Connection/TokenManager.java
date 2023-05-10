package com.example.cinemaprovafragment.Connection;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenManager {

    public static final String SHAREDPREF = "LOGGED_USER";
    SharedPreferences sharedPreferences;
    public static final String USER = "username";
    public static final String TOKEN = "token";
    public static final String LAT = "lat";
    public static final String LOG = "log";
    Context context;

    SharedPreferences.Editor editor;

    public TokenManager(Context context){
        this.context = context;
        sharedPreferences=context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE) ;
        editor =sharedPreferences.edit();
    }
    public void storeToken(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            editor.putString(TOKEN, jsonObject.getString("token"));
            editor.putString(USER, jsonObject.getString("username"));
            editor.commit();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeLocation(double lat, double log){
        editor.putLong(LAT,Double.doubleToRawLongBits(lat));
        editor.putLong(LOG,Double.doubleToRawLongBits(log));
        editor.commit();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
    }

    public String getUserName(){
        return sharedPreferences.getString(USER,"");
    }

    public double getLat(){
        return Double.longBitsToDouble(sharedPreferences.getLong(LAT,0));
    }
    public double getLog(){
        return Double.longBitsToDouble(sharedPreferences.getLong(LOG,0));
    }
    public void logout(){
        editor.clear();
        editor.commit();
    }

}
