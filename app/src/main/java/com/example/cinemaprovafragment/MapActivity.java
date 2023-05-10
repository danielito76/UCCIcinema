package com.example.cinemaprovafragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.cinemaprovafragment.Models.Cinema;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Cinema cinema;
    ArrayList<Cinema> cinemas;
    boolean allCinema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        allCinema = false;
        cinema = getIntent().getParcelableExtra("cinema");
        if(cinema == null){
            allCinema= true;
            cinemas = getIntent().getParcelableArrayListExtra("cinemas");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if(allCinema){
            Log.i("httphandler", "all cinema");
            for(Cinema c: cinemas){
                mMap.addMarker(new MarkerOptions().position(c.getLatLng()).title(c.getName()));
            }
//            LatLngBounds cinemaBounds = new LatLngBounds(
//                    new LatLng(44.6488366, 10.9200867), // SW bounds
//                    new LatLng(44.6488366, 10.9200867)  // NE bounds
//            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cinemas.get(0).getLatLng(), 6));
        }else {
            Log.i("httphandler", "no all cinema");
            mMap.addMarker(new MarkerOptions().position(cinema.getLatLng()).title(cinema.getName()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cinema.getLatLng(), 10));
        }
    }
}