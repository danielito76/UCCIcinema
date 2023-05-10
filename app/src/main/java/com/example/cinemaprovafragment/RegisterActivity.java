package com.example.cinemaprovafragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinemaprovafragment.Connection.HttpHandler;
import com.example.cinemaprovafragment.Interface.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextUser, editTextPassword;
    Button buttonReg;
    ProgressBar progressBar;
    FloatingActionButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i("MYTEST","REGISTER_ACTIVITY");

//        mAuth = FirebaseAuth.getInstance();
        editTextUser = findViewById(R.id.user);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);

        btn_back = findViewById(R.id.btn_Back);

        btn_back.setOnClickListener(view -> finish());
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user, password;
                user = String.valueOf(editTextUser.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(user)){
                    Toast.makeText(RegisterActivity.this, "Enter user", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                HttpHandler handler = new HttpHandler(getApplicationContext());
                handler.register(user, password, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Registered ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onError(String result) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}