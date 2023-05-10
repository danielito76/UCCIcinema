package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.Models.Recensione;
import com.example.cinemaprovafragment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmHolder> {

    Context context;
    ArrayList<Film> films;
    public FilmAdapter(Context context){
        this.context = context;
        films = new ArrayList<>();
    }

    public ArrayList<Film> getFilms(){
        return films;
    }

    public void filterList(ArrayList<Film> filterlist) {
        films = filterlist;
        notifyDataSetChanged();
    }
    public void setItems(ArrayList<Film> emp)
    {
        films.clear();
        films.addAll(emp);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmHolder(LayoutInflater.from(context).inflate(R.layout.item_view_film,parent, false),context);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmHolder holder, int position) {
//        holder.titleView.setText(films.get(position).getTitle());
//        Picasso.get().load(films.get(position).getImage()).into(holder.imageView);

        Film film = films.get(position);
        holder.bind(film);
    }

    @Override
    public int getItemCount() {
        return films.size();
    }


}


