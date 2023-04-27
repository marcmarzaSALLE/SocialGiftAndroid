package com.example.socialgift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.socialgift.activities.LoginActivity;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;

public class MainActivity extends AppCompatActivity {

    private SharedPreferencesController sharedPreferencesController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferencesController = new SharedPreferencesController();
        if(sharedPreferencesController.loadDateSharedPreferences(getApplicationContext()) != null){
            Toast.makeText(getApplicationContext(), "AUTORIZADO", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}