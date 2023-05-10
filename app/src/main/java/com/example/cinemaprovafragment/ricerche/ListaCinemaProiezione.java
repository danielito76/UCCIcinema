package com.example.cinemaprovafragment.ricerche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.cinemaprovafragment.Adapter.CinemaAdapter;
import com.example.cinemaprovafragment.BaseActivity;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaCinemaProiezione extends BaseActivity {
    ProgressBar progressBar;
    ArrayList<Cinema> cinemas;
    CinemaAdapter cinemaAdapter;
    String idFilm;
    RecyclerView recyclerView;
    FloatingActionButton back;
    TokenManager tokenManager;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cinema_proiezione);

        bindViews();
        initializeValues();

        configureHeader(tokenManager,constraintLayout);
        getCinemaByFilm(idFilm);

        back.setOnClickListener(view -> finish());
    }

    private void initializeValues() {
        idFilm = getIntent().getStringExtra("idFilm");
        tokenManager = new TokenManager(this);
        cinemas = new ArrayList<>();
        cinemaAdapter = new CinemaAdapter(this);
        LinearLayoutManager layout = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(cinemaAdapter);
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        constraintLayout = findViewById(R.id.parent_id);
        back=findViewById(R.id.btn_Back);
    }

    /**
     * dato idFilm e citta dell'user ritorna i cinema della città che hanno in programma questo film
     * @param idFilm
     */
    private void getCinemaByFilm(String idFilm){
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(tokenManager.getLat(), tokenManager.getLog(), 1);
            String city = addresses.get(0).getLocality();;
            HttpHandler handler = new HttpHandler(this);
            handler.getCinemaFromFilm(idFilm, city, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        Gson gson = new Gson();
                        Type founderListType = new TypeToken<ArrayList<Cinema>>(){}.getType();
                        cinemas = gson.fromJson(result, founderListType);
                        cinemaAdapter.setItems(cinemas);
                    } catch (Exception e) {
                        Log.i("httpHandler exception", "" + e);
                    }
                }

                @Override
                public void onError(String result) {
                    progressBar.setVisibility(View.GONE);
                    Log.i("httpHandler exception", "" + result);
                }
            });

        }catch (Exception e){

        }
    }
}