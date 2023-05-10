package com.example.cinemaprovafragment.ricerche;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Adapter.CinemaAdapter;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.MapActivity;
import com.example.cinemaprovafragment.MapsActivity2;
import com.example.cinemaprovafragment.Models.Cinema;
import com.example.cinemaprovafragment.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaCinemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaCinemaFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar progressBar;
    ArrayList<Cinema> cinemas;
    CinemaAdapter cinemaAdapter;
    SearchView searchView;
    FloatingActionButton btnMap;
    RecyclerView recyclerView;
    public ListaCinemaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaCinemaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaCinemaFragment newInstance(String param1, String param2) {
        ListaCinemaFragment fragment = new ListaCinemaFragment();
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
        View view = inflater.inflate(R.layout.fragment_lista_cinema, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        btnMap = view.findViewById(R.id.btn_Map);
        searchView.clearFocus();

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        cinemaAdapter = new CinemaAdapter(getContext());
        recyclerView.setAdapter(cinemaAdapter);

        cinemas = new ArrayList<>();

        getAllCinema();

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity2.class);
                intent.putParcelableArrayListExtra("cinemas", cinemas);
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return view;
    }
    private void filter(String text) {
        ArrayList<Cinema> filteredlist = new ArrayList<>();
        if(cinemas!=null){
            for (Cinema item : cinemas) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase()) ||
                        item.getCity().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
            cinemaAdapter.filterList(filteredlist);
        }

    }

    /**
     * richiesta di tutti i film dal server
     */
    private void getAllCinema() {
        HttpHandler handler = new HttpHandler(getContext());
        handler.getAllData(HttpHandler.GET_ALLCINEMAS, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    Type founderListType = new TypeToken<ArrayList<Cinema>>(){}.getType();
                    cinemas = gson.fromJson(result, founderListType);
                    cinemaAdapter.setItems(cinemas);
                    recyclerView.scheduleLayoutAnimation();
                } catch (Exception e) {
                    Log.i("httpHandler exception", "" + e);
                }
            }

            @Override
            public void onError(String result) {

            }
        });
    }
}