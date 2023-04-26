package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.dao.VolleyRequest;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView edtEmail, edtPassword, edtConfirmPassword, edtName, edtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        syncronizedWigets();
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext());
        volleyRequest.createUser();

    }


    private void syncronizedWigets() {
        Log.wtf("RegisterActivity", "syncronizedWigets: ");
        btnRegister = (Button) findViewById(R.id.registerButton);
        edtEmail = (EditText) findViewById(R.id.emailText);
        edtPassword = (EditText) findViewById(R.id.passwordText);
        edtConfirmPassword = (EditText) findViewById(R.id.confirmPasswordText);
        edtName = (EditText) findViewById(R.id.nameText);
        edtLastName = (EditText) findViewById(R.id.lastnameText);
    }
}