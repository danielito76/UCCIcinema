package com.example.cinemaprovafragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    Button buttonLogin;
    FloatingActionButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        buttonLogin = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_Back);

        Button textView = findViewById(R.id.registerNow);

        btn_back.setOnClickListener(view -> finish());
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user, password;
                user = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(user)){
                    Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                HttpHandler handler = new HttpHandler(getApplicationContext());
                handler.login(user, password, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}