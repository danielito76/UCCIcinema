package com.example.cinemaprovafragment.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.R;

public class ProiezioneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    TextView nameView, dataView, genereView, durataView, castView, regiaView, orarioView;
    private ProiezioneAdapter.OnTapProiezioneListener onTapProiezioneListener;

    public ProiezioneHolder(@NonNull View itemView,ProiezioneAdapter.OnTapProiezioneListener onTapProiezioneListener) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.text_name);
        dataView = itemView.findViewById(R.id.dataView);
        durataView = itemView.findViewById(R.id.text_durata);
        genereView = itemView.findViewById(R.id.text_genere);
//        castView = itemView.findViewById(R.id.text_cast);
//        regiaView = itemView.findViewById(R.id.text_regia);
        orarioView = itemView.findViewById(R.id.text_orario);
        itemView.setOnClickListener(this);
        this.onTapProiezioneListener = onTapProiezioneListener;
    }

    @Override
    public void onClick(View view) {
        onTapProiezioneListener.OnTapProiezione(getAbsoluteAdapterPosition());
    }
}