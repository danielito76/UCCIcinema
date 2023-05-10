package com.example.cinemaprovafragment.schede;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cinemaprovafragment.Adapter.ProiezioneAdapter;
import com.example.cinemaprovafragment.BaseActivity;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.MapActivity;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.Models.Proiezione;
import com.example.cinemaprovafragment.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SchedaCinema extends BaseActivity implements ProiezioneAdapter.OnTapProiezioneListener{

    Cinema cinema;
    ArrayList<Proiezione> proiezioni;
    ProgressBar progressBar;
    ProiezioneAdapter adapter;
    TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheda_cinema);

        tokenManager = new TokenManager(this);
        ConstraintLayout constraintLayout= findViewById(R.id.parent_id);
        configureHeader(tokenManager,constraintLayout);
        cinema = getIntent().getParcelableExtra("cinema");

        makeApiCall();

        TextView name = findViewById(R.id.nome_cinema);
        TextView indirizzo = findViewById(R.id.indirizzo);
        TextView servizi = findViewById(R.id.servizi);
        TextView sale = findViewById(R.id.sale);
        TextView city = findViewById(R.id.city);
        FloatingActionButton buttonMap = findViewById(R.id.button_map);

        name.setText(cinema.getName());
        indirizzo.setText(cinema.getAddress());
        city.setText(cinema.getCity()+" "+cinema.getProvince());
        servizi.setText("Servizi: "+cinema.getServices());
        sale.setText("Sale "+cinema.getRoom());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        adapter = new ProiezioneAdapter(this,this::OnTapProiezione);
        recyclerView.setAdapter(adapter);

        FloatingActionButton back=findViewById(R.id.button_back);

        back.setOnClickListener(view -> finish());

        buttonMap.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                intent.putExtra("cinema", cinema);
                startActivity(intent);
            }
        });
    }

    /**
     * manda una richiesta al server con l'id del cinema per ottenere le proiezioni
     */
    private void makeApiCall() {
        HttpHandler handler = new HttpHandler(this);
        handler.getProjectionFromCinema(cinema.get_id(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Proiezione>>(){}.getType();
                proiezioni = gson.fromJson(result, founderListType);
                adapter.setItems(proiezioni);
            }

            @Override
            public void onError(String result) {

            }
        });
    }

    /**
     * cliccando la proiezione viene mandata sulla scheda film
     * @param position
     */
    @Override
    public void OnTapProiezione(int position) {
        Intent intent = new Intent(this, SchedaFilm.class);
        intent.putExtra("film",proiezioni.get(position).getFilm());
        startActivity(intent);
    }
}