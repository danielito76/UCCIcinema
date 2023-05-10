package com.example.cinemaprovafragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cinemaprovafragment.Adapter.ProiezioneAdapter;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.Connection.UserLocation;
import com.example.cinemaprovafragment.Interface.MyLocationCallback;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.Models.Proiezione;
import com.example.cinemaprovafragment.schede.SchedaCinema;
import com.example.cinemaprovafragment.schede.SchedaFilm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ProiezioneAdapter.OnTapProiezioneListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Film> films;
    ArrayList<SlideModel> imageList;
    ImageSlider imageSlider;
    Cinema cinema;
    HttpHandler handler;
    ArrayList<Proiezione> proiezioni;
    ProiezioneAdapter adapter;
    Button btnScheda;
    TextView name, indirizzo, citta;
    UserLocation location;
    RecyclerView recyclerView;
    TokenManager tokenManager;
    ImageView cardBackgroundImage;
    ProgressBar progressBar;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        bindViews(view);
        //instanza gli array e recyclerview
        initializeValues();
        getTredingFilm();
        getUserLocation();

        btnScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cinema!= null){
                    Intent intent = new Intent(getContext(), SchedaCinema.class);
                    intent.putExtra("cinema",cinema);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void bindViews(View view) {
        name = view.findViewById(R.id.nome);
        indirizzo = view.findViewById(R.id.indirizzo);
        citta = view.findViewById(R.id.servizi);
        btnScheda = view.findViewById(R.id.button);
        recyclerView = view.findViewById(R.id.recyclerview);
        imageSlider= view.findViewById(R.id.image_slider);
        cardBackgroundImage = view.findViewById(R.id.background_image);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void initializeValues() {
        imageList = new ArrayList<>();
        films = new ArrayList<>();
        handler = new HttpHandler(getContext());
        tokenManager = new TokenManager(getContext());

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        adapter = new ProiezioneAdapter(getContext(),this::OnTapProiezione);
        recyclerView.setAdapter(adapter);
    }

    private void getUserLocation() {
        location = new UserLocation(getContext());
        location.getLastLocation(new MyLocationCallback() {
            @Override
            public void onComplete(Location location) {
                tokenManager.storeLocation(location.getLatitude(),location.getLongitude());
                handler.getNearestCinema(location.getLatitude(), location.getLongitude(), new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    if (!result.isEmpty()) {
                        Gson gson = new Gson();
                        cinema = gson.fromJson(result, Cinema.class);
                        buildCinemaLayout();
                    }
                }
                @Override
                public void onError(String result) {
                    Log.i("httphandler", result);
                }
            });
            }
        });
    }

    private void buildCinemaLayout() {
        name.setText(cinema.getName());
        indirizzo.setText(cinema.getAddress());
        citta.setText(cinema.getCity()+" "+cinema.getProvince());
        Picasso.get().load(cinema.getImage()).into(cardBackgroundImage);

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
                progressBar.setVisibility(View.GONE);
                Log.i("httphandler", result);
            }
        });
    }

    /**
     * richiesta server per avere i film per lo slide
     */
    private void getTredingFilm() {
        handler.getAllData(HttpHandler.GET_ALLFILM+"5", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //Log.i("httpHandler on success ", "success");
                try {
                    Gson gson = new Gson();
                    Type founderListType = new TypeToken<ArrayList<Film>>(){}.getType();
                    films = gson.fromJson(result, founderListType);
                    loadImagetoSlider();

                } catch (Exception e) {
                    //Log.i("httpHandler exception", "" + e);
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getContext(), ""+result, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * aggiunge le immagini nello slider
     */
    private void loadImagetoSlider() {
        for (Film f: films){
            imageList.add(new SlideModel( f.getImage(), ScaleTypes.CENTER_CROP));
        }

        imageSlider.setImageList(imageList);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                // You can listen here.
                Intent intent = new Intent(getContext(), SchedaFilm.class);
                intent.putExtra("film", films.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnTapProiezione(int position) {
        Intent intent = new Intent(getContext(), SchedaFilm.class);
        intent.putExtra("film",proiezioni.get(position).getFilm());
        startActivity(intent);
    }
}