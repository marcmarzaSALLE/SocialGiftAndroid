package com.example.socialgift.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import com.example.socialgift.R;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;

    EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        syncronizedWigets();
        btnLogin.setOnClickListener(v -> {
            login();
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }

    private void login() {
    }

    public void openRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void syncronizedWigets() {
        btnLogin = (Button) findViewById(R.id.loginButton);
        btnRegister = (Button) findViewById(R.id.SignUpButton);
        edtEmail = (EditText) findViewById(R.id.emailText);
        edtPassword = (EditText) findViewById(R.id.passwordText);

    }
}