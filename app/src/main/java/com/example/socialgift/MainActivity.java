package com.example.socialgift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.socialgift.activities.LoginActivity;
import com.example.socialgift.dao.VolleyRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Per fer proves del Login i Register
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        Log.wtf("MainActivity", String.valueOf(getApplicationContext()));
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext());
        volleyRequest.createUser();
        Log.d("MainActivity", "onCreate: ");
    }
}