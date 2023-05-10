package com.example.cinemaprovafragment.ricerche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Adapter.CinemaAdapter;
import com.example.cinemaprovafragment.Adapter.ProvinceAdapter;
import com.example.cinemaprovafragment.BaseActivity;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.Models.Provincia;
import com.example.cinemaprovafragment.Models.Regione;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.schede.SchedaCinema;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class Ricerca extends BaseActivity {

    FloatingActionButton btnBack;
    AutoCompleteTextView regioneTextView, provinciaTextView;
    RecyclerView recyclerView;
    HttpHandler handler;
    ArrayList<Regione> regiones;
    ArrayList<Provincia> provinces;
    ArrayList<Cinema> cinemas, filteredCinema;
    CinemaAdapter cinemaAdapter;
    ArrayAdapter<Regione> regionAdapter;
    ProvinceAdapter provinceAdapter;
    boolean isSelectProvincia;
    TokenManager tokenManager;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca);

        bindViews();
        initializeValues();
        configureHeader(tokenManager,constraintLayout);
        getRegions();
        getProvinces();
        getCinemas();

        regioneTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectProvincia=false;
                provinciaTextView.setText("Provincia");
                Regione r = (Regione) adapterView.getItemAtPosition(i);
                provinceAdapter.getFilter().filter(r.get_id());
                filteredCinema.clear();
                filter(r.getName());
            }
        });

        provinciaTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectProvincia = true;
                Provincia r = (Provincia) adapterView.getItemAtPosition(i);
                filter(r.getName());
            }
        });

        btnBack.setOnClickListener(view -> finish());
    }

    private void bindViews() {
        constraintLayout = findViewById(R.id.parent_id);
        btnBack=findViewById(R.id.button_back);
        regioneTextView = findViewById(R.id.regione);
        provinciaTextView = findViewById(R.id.provincia);
        recyclerView = findViewById(R.id.ricerca_recycler_view);
    }
    private void initializeValues() {
        handler = new HttpHandler(this);
        regiones = new ArrayList<>();
        provinces = new ArrayList<>();
        cinemas = new ArrayList<>();
        filteredCinema = new ArrayList<>();
        tokenManager = new TokenManager(this);
        isSelectProvincia = false;

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        cinemaAdapter = new CinemaAdapter(this);
        recyclerView.setAdapter(cinemaAdapter);
    }
    /**
     * ritorna le regioni che hanno i cinema
     */
    private void getRegions() {
        handler.getAllData(HttpHandler.GET_ALL_REGIONS, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Regione>>(){}.getType();
                regiones = gson.fromJson(result, founderListType);
                regionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.region_dropdown_item,regiones);
                regioneTextView.setAdapter(regionAdapter);
            }

            @Override
            public void onError(String result) {
                Log.i("httphandler", result);
            }
        });
    }

    private void getProvinces() {
        handler.getAllData(HttpHandler.GET_ALL_PROVINCES, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Provincia>>(){}.getType();
                provinces = gson.fromJson(result, founderListType);
                provinceAdapter = new ProvinceAdapter(getApplicationContext(),R.layout.region_dropdown_item, provinces);
                provinciaTextView.setAdapter(provinceAdapter);

            }
            @Override
            public void onError(String result) {
                Log.i("httphandler", result);
            }
        });
    }

    private void getCinemas() {
        handler.getAllData(HttpHandler.GET_ALLCINEMAS, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Cinema>>(){}.getType();
                cinemas = gson.fromJson(result, founderListType);
                cinemaAdapter.setItems(cinemas);
            }

            @Override
            public void onError(String result) {

            }
        });
    }

    /**
     * filtra i cinema in base alla regione e provincia scelta
     * @param text
     */
    private void filter(String text) {
        if(filteredCinema.isEmpty()){
            if(isSelectProvincia){
                for (Cinema item : cinemas) {
                    if (item.getProvince().contains(text)) {
                        filteredCinema.add(item);
                    }
                }
            }else{
                for (Cinema item : cinemas) {
                    if (item.getRegion().contains(text)) {
                        filteredCinema.add(item);
                    }
                }
            }

            cinemaAdapter.filterList(filteredCinema);
        }else{
            ArrayList<Cinema> provFilter = new ArrayList<>();
            if(isSelectProvincia){
                for (Cinema item : cinemas) {
                    if (item.getProvince().contains(text)) {
                        provFilter.add(item);
                    }
                }
            }else{
                for (Cinema item : filteredCinema) {
                    if (item.getProvince().contains(text)) {
                        provFilter.add(item);
                    }
                }
            }
            cinemaAdapter.filterList(provFilter);
        }
    }
}