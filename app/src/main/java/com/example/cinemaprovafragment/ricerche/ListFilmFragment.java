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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Adapter.FilmAdapter;
import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.example.cinemaprovafragment.Models.Film;
import com.example.cinemaprovafragment.R;
import com.example.cinemaprovafragment.schede.SchedaFilm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFilmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFilmFragment extends Fragment{

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
    SearchView searchView;
    RecyclerView recyclerView;
    public ListFilmFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFilmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFilmFragment newInstance(String param1, String param2) {
        ListFilmFragment fragment = new ListFilmFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_film, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        int span = getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE ? 5 : 3 ;

        GridLayoutManager grid = new GridLayoutManager(getContext(),span,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(grid);
        recyclerAdapter = new FilmAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);
        makeApicall();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //viene richiamato se cambia orientamento anche se non ho scritto niente
                    filter(newText);
                return true;
            }
        });

        return view;
    }
    private void filter(String text) {
        if(films!=null){
            ArrayList<Film> filteredlist = new ArrayList<>();

            for (Film item : films) {
                if (item.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                        item.getGenres().toLowerCase().contains(text.toLowerCase()) ||
                        item.getCast().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
            recyclerAdapter.filterList(filteredlist);
        }

    }

    private void makeApicall() {
        HttpHandler sh = new HttpHandler(getContext());
        sh.getAllData(HttpHandler.GET_ALLFILM, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //Log.i("httpHandler on success ", "success");
                progressBar.setVisibility(View.GONE);
                try {
                    Gson gson = new Gson();
                    Type founderListType = new TypeToken<ArrayList<Film>>(){}.getType();
                    films = gson.fromJson(result, founderListType);
                    recyclerAdapter.setItems(films);
                    recyclerView.scheduleLayoutAnimation();
                } catch (Exception e) {
                    //Log.i("httpHandler exception", "" + e);
                }
            }

            @Override
            public void onError(String result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+result, Toast.LENGTH_LONG).show();
            }
        });
    }
}