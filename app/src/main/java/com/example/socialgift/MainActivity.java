package com.example.socialgift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import androidx.appcompat.widget.Toolbar;

import com.example.socialgift.activities.LoginActivity;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.fragments.ChatFragment;
import com.example.socialgift.fragments.ListFragment;
import com.example.socialgift.fragments.UserFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferencesController sharedPreferencesController;
    private SmoothBottomBar bottomNavigationView;

    private Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        sharedPreferencesController = new SharedPreferencesController();
        if(sharedPreferencesController.loadDateSharedPreferences(getApplicationContext()) != null){
            replace(new ListFragment());
            bottomNavigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public boolean onItemSelect(int i) {
                    switch (i){
                        case 0:
                            replace(new ListFragment());
                            break;

                        case 1:
                            replace(new ChatFragment());
                            break;

                        case 2:
                            replace(new UserFragment());
                            break;
                    }
                    return true;
                }
            });
        }
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }

    private void getPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
}