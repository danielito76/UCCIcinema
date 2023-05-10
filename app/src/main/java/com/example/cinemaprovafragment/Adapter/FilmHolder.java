package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.schede.SchedaFilm;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView imageView;
    TextView titleView, durataView, genereView, castView, regiaView;
    //FilmAdapter.OnTapFilmListener onTapFilmListener;
    Film currentFilm;
    Context context;
    public FilmHolder(@NonNull View itemView, Context context) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);
        titleView = itemView.findViewById(R.id.title);
        //durataView = itemView.findViewById(R.id.durata);
        // genereView = itemView.findViewById(R.id.genere);
        // castView = itemView.findViewById(R.id.cast);
        //regiaView = itemView.findViewById(R.id.regia);
        this.context = context;

        itemView.setOnClickListener(this);
    }

    public void bind(Film item){
        titleView.setText(item.getTitle());
        Picasso.get().load(item.getImage()).into(imageView);
        currentFilm = item;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, SchedaFilm.class);
        intent.putExtra("film",currentFilm);
        context.startActivity(intent);
        //onTapFilmListener.OnTapFilm(getAbsoluteAdapterPosition());
    }
}
