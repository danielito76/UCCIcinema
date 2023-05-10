package com.example.cinemaprovafragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Connection.UserLocation;
import com.example.cinemaprovafragment.Interface.MyLocationCallback;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.databinding.ActivityMaps2Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cinemaprovafragment.databinding.ActivityMaps2Binding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Connection.UserLocation;
import com.example.cinemaprovafragment.Interface.MyLocationCallback;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.databinding.ActivityMapBinding;
import com.example.cinemaprovafragment.schede.SchedaCinema;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private GoogleMap mMap;
    Cinema cinema;
    ArrayList<Cinema> cinemas;
    boolean allCinema;


    // Daniele prima implementazione 1/..
    private Marker marker;

    private LocationManager locationManager;
    private ActivityMapBinding binding;

    UserLocation location;
    TokenManager tokenManager;
    // Daniele prima implementazione 1/..



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Daniele prima implementazione 2/..
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cinemas = getIntent().getParcelableArrayListExtra("cinemas");


        //Checking self permissions
        if (ContextCompat.checkSelfPermission(com.example.cinemaprovafragment.MapsActivity2.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(com.example.cinemaprovafragment.MapsActivity2.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(com.example.cinemaprovafragment.MapsActivity2.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        location = new UserLocation(this);
        location.getLastLocation(new MyLocationCallback() {
            @Override
            public void onComplete(Location location) {
                tokenManager.storeLocation(location.getLatitude(),location.getLongitude());

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap myGoogleMap) {
        mMap = myGoogleMap;

        // Daniele prima implementazione 3/..
        //Pop-up al caricamento
        Toast.makeText(com.example.cinemaprovafragment.MapsActivity2.this, "Ciao, la tua mappa è in caricamento!",
                Toast.LENGTH_SHORT).show();
        // Daniele prima implementazione 3/..

        for (Cinema item : cinemas) {


            // Daniele prima implementazione 4/..

            marker = mMap.addMarker(new MarkerOptions().position(item.getLatLng()).title(item.getName()).snippet(item.getAddress()));
            mMap.setOnMarkerClickListener(this);

            cinema = item;

        }

        location.getLastLocation(new MyLocationCallback() {
            @Override
            public void onComplete(Location location) {
                LatLngBounds cinemaBounds = null;

                cinemaBounds = new LatLngBounds(
                    /*//Test Sidney
                    new LatLng(-36.597889133070204, 509.06250000000006), // SW bounds
                    new LatLng(-36.597889133070204, 509.06250000000006)  // NE bounds
                    */

                        // Effettive coordinate del diaspositivo
                        new LatLng(location.getLatitude(), location.getLongitude()), // SW bounds
                        new LatLng(location.getLatitude(), location.getLongitude())  // NE bounds
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cinemaBounds.getCenter(), 10));

            }

        });

    }


    public boolean onMarkerClick(@NonNull Marker marker) {

        /*Cinema currentCinema = Arrays.stream(cinemas).filter(x -> x.getLatLng() = marker.getPosition()).toArray();
        //d => [8, 7, 5]*/

        AlertDialog.Builder alert = new AlertDialog.Builder(this,  R.style.CustomDialog);
        alert.setTitle(marker.getTitle());
        alert.setMessage("Clicca Ok per andare alla scheda del cinema");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                for (Cinema currentCinema : cinemas) {
                    if (currentCinema.getLatLng().equals(marker.getPosition())) {
                        //Qui il codice da eseguire
                        Intent intent = new Intent(getBaseContext(), SchedaCinema.class);
                        intent.putExtra("cinema", currentCinema);
                        startActivity(intent);
                        break;
                    }
                }



            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();
        return false;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}


