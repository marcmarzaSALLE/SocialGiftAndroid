package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtViewToolbar;
    private ImageButton imgBtnToolbar, imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        syncronizeView();
        imgBtnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void syncronizeView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_left);

    }


}