package com.example.cinemaprovafragment.Connection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.cinemaprovafragment.Interface.MyLocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserLocation {
public int PERMISSION_ID = 44;
Context context;
    FusedLocationProviderClient mFusedLocationClient;
    private double lat;
    private double log;
    public UserLocation(Context context){
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }
    public void getLastLocation(final MyLocationCallback callback) {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        try{
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                callback.onComplete(location);
                                lat = location.getLatitude();
                                log = location.getLongitude();
                                Log.i("httphandler lat",location.getLatitude() + "");
                                Log.i("httphandler log",location.getLongitude() + "");
                            }
                        }catch (Exception e){
                            Log.i("httphandler",""+e.getMessage());
                        }
                    }
                });
            } else {
                Toast.makeText(context, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = LocationRequest.create()
                .setInterval(5)
                .setFastestInterval(0)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);
//        LocationRequest mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5)
//                .setInterval(5)
//                .setFastestInterval(0)
//                .setNumUpdates(1).build();
//        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
//                .setWaitForAccurateLocation(false)
//                .setMinUpdateIntervalMillis(locationFastestInterval)
//                .setMaxUpdateDelayMillis(locationMaxWaitTime)
//                .build()

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude();
            log = mLastLocation.getLongitude();
            Log.i("httphandler lat2",mLastLocation.getLatitude() + "");
            Log.i("httphandler log2",mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    public void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
