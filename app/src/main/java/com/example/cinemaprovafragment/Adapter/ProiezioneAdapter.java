package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.Models.Proiezione;
import com.example.cinemaprovafragment.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProiezioneAdapter extends RecyclerView.Adapter<ProiezioneHolder> {

    Context context;
    ArrayList<Proiezione> proiezioni;
    private OnTapProiezioneListener onTapProiezioneListener;
    public ProiezioneAdapter(Context context,OnTapProiezioneListener onTapProiezioneListener){
        this.context = context;
        proiezioni = new ArrayList<>();
        this.onTapProiezioneListener = onTapProiezioneListener;
    }

    public ArrayList<Proiezione> getProiezioni(){
        return proiezioni;
    }

    public void setItems(ArrayList<Proiezione> emp)
    {
        proiezioni.addAll(emp);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProiezioneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProiezioneHolder(LayoutInflater.from(context).inflate(R.layout.item_view_proiezione,parent, false),onTapProiezioneListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProiezioneHolder holder, int position) {
        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(proiezioni.get(position).getDate());
        holder.nameView.setText(proiezioni.get(position).getFilm().getTitle());
        holder.dataView.setText(date);
        holder.durataView.setText(proiezioni.get(position).getFilm().getDuration());
        holder.genereView.setText(proiezioni.get(position).getFilm().getGenres());
//        holder.castView.setText(proiezioni.get(position).getFilm().getCast());
//        holder.regiaView.setText(proiezioni.get(position).getFilm().getDirection());
        holder.orarioView.setText(" "+String.join("  ",proiezioni.get(position).getTime()));
        Picasso.get().load(proiezioni.get(position).getFilm().getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return proiezioni.size();
    }

    public interface OnTapProiezioneListener {
        void OnTapProiezione(int position);
    }

}
