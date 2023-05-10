package com.example.cinemaprovafragment.ricerche;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cinemaprovafragment.Adapter.FilmAdapter;
import com.example.cinemaprovafragment.Connection.SQLiteHelper;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.schede.SchedaFilm;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferitiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferitiFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Film> films;
    FilmAdapter recyclerAdapter;
    ProgressBar progressBar;
    SQLiteHelper helper;
    RecyclerView recyclerView;


    public PreferitiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferitiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferitiFragment newInstance(String param1, String param2) {
        PreferitiFragment fragment = new PreferitiFragment();
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
        View view = inflater.inflate(R.layout.fragment_preferiti, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewPreferiti);
        progressBar = view.findViewById(R.id.progressBar);
        films = new ArrayList<>();
        recyclerAdapter = new FilmAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);
        loadPreferiti();
        int span = getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE ? 5 : 3 ;

        GridLayoutManager grid = new GridLayoutManager(getContext(),span,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(grid);

        return view;
    }
    private void loadPreferiti() {
        helper = new SQLiteHelper(getContext());
        films = helper.readFilms();
        if(films.size()==0){
            //Toast.makeText(getContext(), "No favourite found", Toast.LENGTH_LONG).show();
        }
        recyclerAdapter.setItems(films);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public  void onResume() {
        super.onResume();
        loadPreferiti();
    }
}