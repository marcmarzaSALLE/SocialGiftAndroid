package com.example.socialgift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.socialgift.dao.VolleyRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.wtf("MainActivity", String.valueOf(getApplicationContext()));
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext());
        volleyRequest.createUser();
        Log.d("MainActivity", "onCreate: ");
    }
}