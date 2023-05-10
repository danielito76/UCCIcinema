package com.example.cinemaprovafragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.schede.SchedaCinema;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CinemaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView imageView;
    TextView nameView, addressView,servicesView, roomView, cityView;
    Cinema currentCinema;
    Context context;
    public CinemaHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.text_name);
        addressView = itemView.findViewById(R.id.text_address);
        servicesView = itemView.findViewById(R.id.text_services);
        roomView = itemView.findViewById(R.id.text_room);
        cityView = itemView.findViewById(R.id.text_city);

        itemView.setOnClickListener(this);
    }

    public void bind (Cinema item) {
        nameView.setText(item.getName());
        addressView.setText(item.getAddress());
        cityView.setText(item.getCity()+" "+item.getProvince());
        servicesView.setText("Servizi: "+item.getServices());
        roomView.setText("Sale: "+item.getRoom());
        Picasso.get().load(item.getImage()).into(imageView);
        currentCinema = item; //<-- keep a reference to the current item
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, SchedaCinema.class);
        intent.putExtra("cinema",currentCinema);
        context.startActivity(intent);
    }

}