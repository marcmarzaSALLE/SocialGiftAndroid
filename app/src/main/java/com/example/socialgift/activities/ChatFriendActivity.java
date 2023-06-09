package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.adapter.ChatAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;
import com.example.socialgift.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFriendActivity extends AppCompatActivity {
    TextView txtNameFriend;
    ImageButton imgBtnToolbar,btnSendMessage;
    ImageView imgFriend;
    Toolbar toolbar;
    Friend friend;
    EditText edtMessage;
    RecyclerView listViewChat;
    DaoSocialGift daoSocialGift;
    ArrayList<Message>messages;
    ArrayAdapter<Message> adapter;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend);
        daoSocialGift = DaoSocialGift.getInstance(this);
        getFriend();
        syncronizedToolbar();
        syncronizedWidgets();
        addInformationToolbar();
        getMessages();
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                sendMessage();
            }
        });
        imgBtnToolbar.setOnClickListener(v -> finish());
    }
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
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
        listViewChat = findViewById(R.id.recycler_view_chat_friend);
        btnSendMessage = findViewById(R.id.button_send_chat_friend);
    }

    private void getMessages(){
        daoSocialGift.getMessageChat(friend.getId(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                messages = new ArrayList<>();
                for(int i = 0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Message message = new Message();
                        message.setId(jsonObject.optInt("id"));
                        message.setContent(jsonObject.getString("content"));
                        message.setUser_id_send(jsonObject.getInt("user_id_send"));
                        message.setUser_id_receive(jsonObject.getInt("user_id_recived"));
                        message.setTimeStamp(jsonObject.getString("timeStamp"));
                        messages.add(message);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                setAdapterChat(messages);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setAdapterChat(ArrayList<Message>messagesFriend){
        chatAdapter = new ChatAdapter(this, messagesFriend);
        chatAdapter.notifyDataSetChanged();
        listViewChat.setLayoutManager(new LinearLayoutManager(this));
        listViewChat.setAdapter(chatAdapter);
        listViewChat.postDelayed(new Runnable() {
            @Override
            public void run() {
                listViewChat.scrollToPosition(messagesFriend.size() - 1);
            }
        }, 100);
    }

    private void sendMessage(){
        if(!edtMessage.getText().toString().isEmpty()){
            daoSocialGift.sendMessage(edtMessage.getText().toString(),friend.getId(),new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    edtMessage.setText("");
                    getMessages();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }
}