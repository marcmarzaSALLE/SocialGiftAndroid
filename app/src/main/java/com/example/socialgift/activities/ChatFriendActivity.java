package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.model.Friend;

public class ChatFriendActivity extends AppCompatActivity {
    TextView txtNameFriend;
    ImageButton imgBtnToolbar;
    ImageView imgFriend;
    Toolbar toolbar;
    Friend friend;
    EditText edtMessage;
    RecyclerView recyclerViewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend);
        getFriend();
        syncronizedToolbar();
        syncronizedWidgets();
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
        imgFriend = toolbar.findViewById(R.id.toolbar_image_chat_friend);
    }

    private void addInformationToolbar(){
        txtNameFriend.setText(getResources().getString(R.string.username,friend.getName(),friend.getLast_name()));
        Glide.with(this).load(friend.getImage()).error(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_person_24, null)).apply(RequestOptions.circleCropTransform()).into(imgFriend);
    }

    private void syncronizedWidgets(){
        edtMessage = findViewById(R.id.edit_text_chat_friend);
        recyclerViewChat = findViewById(R.id.recycler_view_chat_friend);
    }
}