package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.model.Friend;

public class ChatFriendActivity extends AppCompatActivity {
    TextView txtNameFriend;
    ImageButton imgBtnToolbar;
    Toolbar toolbar;
    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend);
        getFriend();
        syncronizedToolbar();
        addInformationToolbar();
        imgBtnToolbar.setOnClickListener(v -> finish());
    }

    private void getFriend() {
        friend = (Friend) getIntent().getSerializableExtra("friend");
    }

    private void syncronizedToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbarChatFriend);
        txtNameFriend = toolbar.findViewById(R.id.toolbar_name_chat_friend);
        imgBtnToolbar = toolbar.findViewById(R.id.toolbar_button_back_chat);
    }

    private void addInformationToolbar(){
        txtNameFriend.setText(getResources().getString(R.string.username,friend.getName(),friend.getLast_name()));
    }
}