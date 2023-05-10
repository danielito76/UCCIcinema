package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Proiezione;
import com.example.cinemaprovafragment.Models.Recensione;
import com.example.cinemaprovafragment.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecensioneAdapter extends RecyclerView.Adapter<RecensioneHolder>{

    Context context;
    ArrayList<Recensione> recensioni;

    public RecensioneAdapter(Context context){
        this.context = context;
        recensioni = new ArrayList<>();
    }

    public ArrayList<Recensione> getRecensioni(){
        return recensioni;
    }

    public void setItems(ArrayList<Recensione> emp)
    {
        recensioni.addAll(emp);
        notifyDataSetChanged();
    }

    public void addItem(Recensione emp){
        recensioni.add(emp);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecensioneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecensioneHolder(LayoutInflater.from(context).inflate(R.layout.item_view_recensione,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecensioneHolder holder, int position) {
        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(recensioni.get(position).getDate());
        holder.commentView.setText(recensioni.get(position).getComment());
        holder.usernameView.setText(recensioni.get(position).getUser().getUsername());
        holder.ratingBar.setRating(recensioni.get(position).getRating());
        holder.dateView.setText(date);
    }

    @Override
    public int getItemCount() {
        return recensioni.size();
    }
}
