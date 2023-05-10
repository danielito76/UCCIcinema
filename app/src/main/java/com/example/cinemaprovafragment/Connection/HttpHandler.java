package com.example.cinemaprovafragment.Connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinemaprovafragment.Interface.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpHandler {
    Context context;

    private final static String BASE_URL = "https://green-ox-gown.cyclic.app";
    //private final static String BASE_URL = "http://192.168.1.12:3003";
    public final static String GET_ALLCINEMAS = "/api/cinemas";
    public final static String GET_NEAREST_CINEMA = "/api/cinemas/location/";
    public final static String GET_ALLFILM = "/api/films/";
    public final static String REVIEWS_CALL = "/api/reviews/";
    public final static String GET_CINEMAS_BY_FILM = "/api/films/cinemas/";
    public final static String GET_FILMPROGRAM_BY_CINEMA = "/api/projections/";
    public final static String GET_ALL_REGIONS = "/api/regions/";
    public final static String GET_ALL_PROVINCES = "/api/provinces/";
    public final static String LOGIN = "/api/login/";
    public final static String REGISTER = "/api/users";
    TokenManager tokenManager;

    public HttpHandler(Context context)
    {
        this.context = context;
        tokenManager = new TokenManager(context);
    }

    /**
     * richiesta GET per prendere tutti i dati in base all'url
     * @param url della richiesta
     * @param callback
     */
    public void getAllData(String url, final VolleyCallback callback){
        getRequest(BASE_URL+url,callback);
    }

    /**
     * Dando idCinema ritorna tutte le proiezioni del cinema
     * @param idCinema
     * @param callback
     */
    public void getProjectionFromCinema(String idCinema, final VolleyCallback callback){
        String urlWithPar = BASE_URL+GET_FILMPROGRAM_BY_CINEMA+"?id="+idCinema;
        getRequest(urlWithPar, callback);
    }


    /**
     * Dato citta ritorna tutte i cinema che hanno proiezione di idFilm
     * @param idFilm
     * @param city
     * @param callback
     */
    public void getCinemaFromFilm(String idFilm, String city, final VolleyCallback callback){
        String urlWithPar = BASE_URL+GET_CINEMAS_BY_FILM+"?idFilm="+idFilm+"&city="+city;
        getRequest(urlWithPar, callback);
    }

    /**
     * dato idFilm ritorna tutte le sue recensioni
     * @param idFilm
     * @param callback
     */
    public void getReviewsOfFilm(String idFilm,  final VolleyCallback callback){
        String urlWithPar = BASE_URL+ REVIEWS_CALL +"?film="+idFilm;
        getRequest(urlWithPar,callback);
    }

    /**
     * richiede il cinema più vicino in base alla latitudine e longitudine
     * @param lat
     * @param log
     * @param callback
     */
    public void getNearestCinema(double lat, double log, final VolleyCallback callback){
        String urlWithPar = BASE_URL+GET_NEAREST_CINEMA+"?lat="+lat+"&log="+log;
        getRequest(urlWithPar, callback);
    }

    public void writeReview(String idFilm, float rating, String comment,final VolleyCallback callback){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("film", idFilm);
            jsonBodyObj.put("rating",rating);
            jsonBodyObj.put("comment",comment);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String requestBody = jsonBodyObj.toString();
        postRequest(BASE_URL+ REVIEWS_CALL,requestBody, callback);
    }

    public void login(String user, String password,final VolleyCallback callback){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("username", user);
            jsonBodyObj.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String requestBody = jsonBodyObj.toString();

        postRequest(BASE_URL+LOGIN,requestBody,callback);
    }

    public void register(String user, String password,final VolleyCallback callback){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("username", user);
            jsonBodyObj.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String requestBody = jsonBodyObj.toString();
        postRequest(BASE_URL+REGISTER,requestBody,callback);
    }

    /**
     * metodo per fare richieste POST
     * @param url
     * @param requestBody
     * @param callback
     */
    private void postRequest(String url,String requestBody, VolleyCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(url.contains(LOGIN) || url.contains(REGISTER)) {
                            tokenManager.storeToken(response);
                        }
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                String body;
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    callback.onError(body);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                if(url.contains(LOGIN) || url.contains(REVIEWS_CALL)){
                    params.put("Authorization","bearer "+tokenManager.getToken());
                }
                return params;
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.i("HttpHandler", "unsopport enconding");
                    return null;
                }
            }
        };
        queue.add(myRequest);
    }

    /**
     * metodo per fare richieste GET
     * @param urlWithPar
     * @param callback
     */
    private void getRequest(String urlWithPar, VolleyCallback callback) {
        StringRequest myRequest = new StringRequest(Request.Method.GET, urlWithPar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                String body;
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    callback.onError(body);
                } catch (UnsupportedEncodingException e) {

                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(myRequest);
    }
}
