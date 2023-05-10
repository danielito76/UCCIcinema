package com.example.cinemaprovafragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cinemaprovafragment.Connection.TokenManager;
import com.example.cinemaprovafragment.ricerche.Ricerca;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar topAppBar, loggedTopAppBar;
    TokenManager tokenManager;
    boolean isLogged;

    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        topAppBar = findViewById(R.id.topAppBar);
        loggedTopAppBar = findViewById(R.id.topAppBar_logged);
    }

    /**
     * evento click icona search
     * @param view
     */
    public void search(View view){
        Intent intent = new Intent(getBaseContext(), Ricerca.class);
        startActivity(intent);
    }

    /**
     * evento click icona login
     * @param view
     */
    public void login(View view){
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * evento click icona logout
     * @param view
     */
    public void logout(View view){
        tokenManager.logout();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void configureHeader(TokenManager tokenManager, ConstraintLayout constraintLayout){
        this.tokenManager = tokenManager;
        if (tokenManager.getToken()!="") {
            //logged
            Log.i("httphandler","logged");
            getLayoutInflater().inflate(R.layout.tool_bar_logged, constraintLayout);
            constraintLayout.removeView(topAppBar);
            TextView username = findViewById(R.id.username);
            username.setText(tokenManager.getUserName().toUpperCase().charAt(0)+"");
            isLogged = true;
        }else {
            //not logged
            isLogged = false;
            getLayoutInflater().inflate(R.layout.tool_bar, constraintLayout);
            constraintLayout.removeView(loggedTopAppBar);
        }
    }
}
