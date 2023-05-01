package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.fragments.EditUserFragment;
import com.example.socialgift.fragments.FriendsUserFragment;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtViewToolbar;
    private ImageButton imgBtnToolbar, imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        syncronizeView();
        replace(new EditUserFragment());
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

    private void replace(EditUserFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.editUserFragment, fragment)
                .commit();
    }


}