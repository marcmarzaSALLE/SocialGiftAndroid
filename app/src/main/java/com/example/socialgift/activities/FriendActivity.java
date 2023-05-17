package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.fragments.EditUserFragment;
import com.example.socialgift.fragments.FriendFragment;
import com.example.socialgift.model.Friend;

public class FriendActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton imgBtnBack;
    TextView txtToolbarName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        syncronizedWidgets();
        replace(new FriendFragment());
        getFriendIdIntent();
        imgBtnBack.setOnClickListener(v -> {
            finish();
        });
    }
    private void syncronizedWidgets(){
        toolbar = (Toolbar) findViewById(R.id.toolbarFriendProfile);
        imgBtnBack = (ImageButton) findViewById(R.id.toolbar_button_back);
        txtToolbarName = (TextView) findViewById(R.id.toolbar_name);
    }

    private void getFriendIdIntent(){
        Friend friend = (Friend) getIntent().getSerializableExtra("friend");
        txtToolbarName.setText(friend.getName());
    }
    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.friendFragment, fragment)
                .commit();
    }

}