package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.MainActivity;
import com.example.socialgift.R;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;

    EditText edtEmail, edtPassword;

    private VolleyRequest volleyRequest;
    private SharedPreferencesController sharedPreferencesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        syncronizedWigets();
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void login() {
        if(checkData()){
            volleyRequest.loginUser(edtEmail.getText().toString(), edtPassword.getText().toString(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Login correcte", Toast.LENGTH_SHORT).show();
                    try {
                        saveUserId();
                        sharedPreferencesController.saveDateSharedPreferences(response.getString("accessToken"), getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Login incorrecte", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void saveUserId(){
        volleyRequest.getMyUserId(edtEmail.getText().toString(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int id = response.getJSONObject(0).getInt("id");
                    sharedPreferencesController.saveUserIdSharedPreferences(id, getApplicationContext());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Login incorrecte", Toast.LENGTH_SHORT).show();
                Log.wtf("LoginActivity", "onErrorResponse: " + error.toString());
            }
        });
    }

    private boolean checkData(){
        if(edtEmail.getText().toString().matches("") ){
            edtEmail.setError(getResources().getString(R.string.email_required));
            return false;

        }else{
            if(edtPassword.getText().toString().matches("")){
                edtPassword.setError(getResources().getString(R.string.password_required));
                return false;
            }
        }
        return true;
    }
    private void syncronizedWigets() {
        btnLogin = (Button) findViewById(R.id.loginButton);
        btnRegister = (Button) findViewById(R.id.SignUpButton);
        edtEmail = (EditText) findViewById(R.id.emailText);
        edtPassword = (EditText) findViewById(R.id.passwordText);

        volleyRequest = new VolleyRequest(getApplicationContext());
        sharedPreferencesController = new SharedPreferencesController();

    }
}