package com.example.cinemaprovafragment.schede;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Adapter.RecensioneAdapter;
import com.example.cinemaprovafragment.BaseActivity;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Connection.SQLiteHelper;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.LoginActivity;
import com.example.cinemaprovafragment.MainActivity;
import com.example.cinemaprovafragment.Models.Recensione;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.ricerche.ListaCinemaProiezione;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import android.view.ViewGroup.LayoutParams;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SchedaFilm extends BaseActivity {

    Film film;
    TokenManager tokenManager;
    RecensioneAdapter recyclerAdapter;
    ProgressBar progressBar;
    ArrayList<Recensione> recensioni;
    boolean isfavourite;
    FloatingActionButton btnBack;
    TextView title, anno, story, director, cast, genre, durata;
    ImageView image, img_sfondo;
    RecyclerView recyclerView;
    Button showMore,btnWriteReview, btnFavourite,button_film_to_cinema;
    SQLiteHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheda_film);

        film = getIntent().getParcelableExtra("film");

        bindViews();
        //inizializza gli array e recyclerview
        initialize();
        //configureHeader(tokenManager,constraintLayout);
        setUpFilmInfo();
        loadReviews();

        btnFavourite.setOnClickListener(onClickFavourite);
        btnWriteReview.setOnClickListener(onClickWriteReview);
        btnBack.setOnClickListener(view -> finish());;
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showMore.getText().toString().equalsIgnoreCase("ALTRO"))
                {
                    story.setMaxLines(Integer.MAX_VALUE);
                    showMore.setText("MENO");
                }
                else
                {
                    story.setMaxLines(3);
                    showMore.setText("ALTRO");
                }

            }
        });

        button_film_to_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ListaCinemaProiezione.class);
                intent.putExtra("idFilm", film.get_id());
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerAdapter = new RecensioneAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
        recensioni = new ArrayList<>();
        dbHelper = new SQLiteHelper(this);
        tokenManager = new TokenManager(this);
        isfavourite = false;
    }

    private void setUpFilmInfo()
    {
        Picasso.get().load(film.getImage()).into(image);
        Picasso.get().load(film.getImage()).into(img_sfondo);
        title.setText(film.getTitle());
        anno.setText(film.getYear()+"");
        story.setText(film.getStory());
        director.setText(film.getDirection());
        cast.setText(film.getCast());
        genre.setText(film.getGenres());
        durata.setText(film.getDuration());

        //se il film è nei preferiti, colora il cuore
        if(dbHelper.isInFavorite(film.get_id())){
            btnFavourite.setBackground(ContextCompat.getDrawable(this,R.drawable.heart_filled));
            isfavourite = true;
        }
    }

    private void bindViews() {
        image = findViewById(R.id.copertina_imageView);
        title = findViewById(R.id.nome_film);
        anno = findViewById(R.id.anno);
        story = findViewById(R.id.story);
        director = findViewById(R.id.director_text);
        cast = findViewById(R.id.cast_text);
        genre = findViewById(R.id.genre_text);
        durata = findViewById(R.id.durata);
        img_sfondo = findViewById(R.id.img_sfondo);

        showMore = findViewById(R.id.btShowmore);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        btnBack = findViewById(R.id.btn_Back);
        btnFavourite = findViewById(R.id.iconButton);
        button_film_to_cinema = findViewById(R.id.button_film_to_cinema);

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
    }


    private void loadReviews() {
        HttpHandler handler = new HttpHandler(this);
        handler.getReviewsOfFilm(film.get_id(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //Log.i("httphandler", ""+result);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Recensione>>(){}.getType();
                recensioni = gson.fromJson(result, founderListType);
                recyclerAdapter.setItems(recensioni);
            }

            @Override
            public void onError(String result) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * se è già nei preferiti lo cancella
     * se non lo è nei preferiti lo aggiunge
     */
    private View.OnClickListener onClickFavourite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isfavourite){
                isfavourite = false;
                dbHelper.delete(film.get_id());
                btnFavourite.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.heart));
                Toast.makeText(getApplicationContext(), "Cancellato dai preferiti!", Toast.LENGTH_SHORT).show();
            }else{
                isfavourite = true;
                dbHelper.addNewFilm(film);
                btnFavourite.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.heart_filled));
                Toast.makeText(getApplicationContext(), "Aggiunto ai preferiti!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * apre un dialog Box per scrivere una recensione
     */
    private  View.OnClickListener onClickWriteReview = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (tokenManager.getToken()!="") {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                RatingBar ratingBar1 = new RatingBar(SchedaFilm.this);
                ratingBar1.setLayoutParams(params);
                ratingBar1.setNumStars(5);
                ratingBar1.setStepSize((float) 0.5);

                EditText text = new EditText(SchedaFilm.this);
                text.setInputType(InputType.TYPE_CLASS_TEXT);
                text.setHint("Scrivi commento qua ...");
                text.setBackground(null);
                text.setLines(3);

                LinearLayout layout = new LinearLayout(SchedaFilm.this);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.setMargins(10,10,10,10);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(params);
                layout.setPadding(20,20,20,20);

                layout.addView(ratingBar1);
                layout.addView(text);

                new MaterialAlertDialogBuilder(SchedaFilm.this)
                        .setView(layout)
                        .setPositiveButton("MANDA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HttpHandler handler = new HttpHandler(getApplicationContext());
                                handler.writeReview(film.get_id(), ratingBar1.getRating(), text.getText().toString(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Toast.makeText(getApplicationContext(), "Recensione mandata!", Toast.LENGTH_SHORT).show();
                                        Gson gson = new Gson();
                                        Recensione recensione = gson.fromJson(result,Recensione.class);
                                        recyclerAdapter.addItem(recensione);
                                    }

                                    @Override
                                    public void onError(String result) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("CANCELLA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }else {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        }
    };

}