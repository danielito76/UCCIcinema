package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaHolder>{
    Context context;
    ArrayList<Cinema> cinemas;

    public CinemaAdapter(Context context){
        this.context = context;
        cinemas = new ArrayList<>();
    }

    public ArrayList<Cinema> getCinemas(){
        return cinemas;
    }

    public void setItems(ArrayList<Cinema> emp)
    {
        cinemas.addAll(emp);
        notifyDataSetChanged();
    }
    public void filterList(ArrayList<Cinema> filterer) {
        cinemas = filterer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CinemaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CinemaHolder(LayoutInflater.from(context).inflate(R.layout.item_view_cinema,parent, false),context);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaHolder holder, int position) {
        Cinema cinema = cinemas.get(position);
        holder.bind(cinema);
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }

}
