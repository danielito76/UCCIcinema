package com.example.cinemaprovafragment.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.R;

public class RecensioneHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView usernameView, commentView,dateView;
    RatingBar ratingBar;

    public RecensioneHolder(@NonNull View itemView) {
        super(itemView);

        usernameView = itemView.findViewById(R.id.username);
        commentView = itemView.findViewById(R.id.commentView);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        dateView = itemView.findViewById(R.id.date);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
