package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFriendRequestAdapter;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;
import com.example.socialgift.model.Friend;

import org.json.JSONArray;

import java.util.ArrayList;

public class FriendsRequestActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtViewToolbar,txtNoFriendsRequest;
    private ImageButton imgBtnBack;
    private RecyclerView recyclerViewFriendsRequest;
    private VolleyRequest volleyRequest;
    private ArrayList<Friend> friendsRequestList;
    private SharedPreferencesController sharedPreferencesController;
    ListFriendRequestAdapter listFriendRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request);
        volleyRequest = new VolleyRequest(this);
        sharedPreferencesController = new SharedPreferencesController();
        syncronizeView();
        addData();
        imgBtnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void syncronizeView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        recyclerViewFriendsRequest = (RecyclerView) findViewById(R.id.recyclerViewFriendsRequest);
        recyclerViewFriendsRequest.setLayoutManager(new LinearLayoutManager(this));
        txtNoFriendsRequest = (TextView) findViewById(R.id.txtNoFriendsRequest);
    }

    private void addData() {
        volleyRequest.getFriendRequest(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                friendsRequestList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("TAG", "onResponse: " + response.getJSONObject(i).getString("name"));
                        friendsRequestList.add(new Friend(response.getJSONObject(i).getInt("id"),
                                response.getJSONObject(i).getString("name"),
                                response.getJSONObject(i).getString("last_name"),
                                response.getJSONObject(i).getString("email"),
                                response.getJSONObject(i).getString("image")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setAdapterRecyclerView(friendsRequestList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setAdapterRecyclerView(ArrayList<Friend> friendsRequestList) {
        if (friendsRequestList.isEmpty()) {
            recyclerViewFriendsRequest.setVisibility(View.GONE);
            txtNoFriendsRequest.setVisibility(View.VISIBLE);
            Log.d("TAG", "setAdapterRecyclerView: " + friendsRequestList.size());
        } else {
            recyclerViewFriendsRequest.setVisibility(View.VISIBLE);
            txtNoFriendsRequest.setVisibility(View.GONE);
            listFriendRequestAdapter = new ListFriendRequestAdapter(friendsRequestList, this, new ListFriendRequestAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Friend friend, int position) {
                    friendsRequestList.remove(position);
                    addData();
                    Log.d("TAG", "onItemClick: " + friend.getName());
                }
            });
            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewFriendsRequest.addItemDecoration(itemDecoration);
            recyclerViewFriendsRequest.setHasFixedSize(true);
            recyclerViewFriendsRequest.setAdapter(listFriendRequestAdapter);
        }
    }
}