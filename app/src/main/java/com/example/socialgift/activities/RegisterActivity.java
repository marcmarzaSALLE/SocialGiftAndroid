package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoSocialGift;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView edtEmail, edtPassword, edtConfirmPassword, edtName, edtLastName;
    private DaoSocialGift daoSocialGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        syncronizedWigets();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        if (checkData()) {
            String image ="https://balandrau.salle.url.edu/i3/repositoryimages/photo/e17ef3a4-f2f0-494a-9584-8190492eb6c8.png";
            daoSocialGift.registerUser(edtName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString(),image ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Register correcte", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Register incorrecte", Toast.LENGTH_SHORT).show();
                    Log.wtf("RegisterActivity", "onErrorResponse: " + error.getMessage());
                }
            });
        }
    }

    private boolean checkData() {
        if (edtEmail.getText().toString().matches("")) {
            edtEmail.setError(getResources().getString(R.string.email_required));
        } else if (edtName.getText().toString().matches("")) {
            edtName.setError(getResources().getString(R.string.name_required));
        } else if (edtLastName.getText().toString().matches("")) {
            edtLastName.setError(getResources().getString(R.string.lastname_required));
        } else if (edtPassword.getText().toString().matches("")) {
            edtPassword.setError(getResources().getString(R.string.password_required));
        } else if (edtConfirmPassword.getText().toString().matches("")) {
            edtConfirmPassword.setError(getResources().getString(R.string.confirm_password_required));
        } else if (!edtPassword.getText().toString().matches(edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError(getResources().getString(R.string.password_confirm));
            edtPassword.setError(getResources().getString(R.string.password_confirm));
        } else {
            return true;
        }
        return false;
    }


    private void syncronizedWigets() {
        daoSocialGift = DaoSocialGift.getInstance(getApplicationContext());
        btnRegister = (Button) findViewById(R.id.registerButton);
        edtEmail = (EditText) findViewById(R.id.emailText);
        edtPassword = (EditText) findViewById(R.id.passwordText);
        edtConfirmPassword = (EditText) findViewById(R.id.confirmPasswordText);
        edtName = (EditText) findViewById(R.id.nameText);
        edtLastName = (EditText) findViewById(R.id.lastnameText);
    }
}