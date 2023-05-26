package com.example.socialgift.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.activities.FriendActivity;
import com.example.socialgift.activities.FriendsRequestActivity;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFriendUserAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;

import org.json.JSONArray;

import java.util.ArrayList;

public class FriendsUserFragment extends Fragment {
    private ImageButton imgBtnFriendsRequest;
    private TextView txtNoFriends;
    private RecyclerView recyclerViewFriends;
    ArrayList<Friend> friends;
    DaoSocialGift daoSocialGift;
    ListFriendUserAdapter listFriendUserAdapter;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_user, container, false);
        syncronizeView(view);
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        addData();
        imgBtnFriendsRequest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendsRequestActivity.class);
            startActivity(intent);
        });
        return view;

    }

    private void syncronizeView(View view) {
        imgBtnFriendsRequest = (ImageButton) view.findViewById(R.id.imgBtnFriendRequest);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        txtNoFriends = (TextView) view.findViewById(R.id.txtNoFriends);
        recyclerViewFriends = (RecyclerView) view.findViewById(R.id.recyclerViewFriends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void addData() {
        daoSocialGift.getMyFriends(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                friends = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        friends.add(new Friend(response.getJSONObject(i).getInt("id"),
                                response.getJSONObject(i).getString("name"),
                                response.getJSONObject(i).getString("last_name"),
                                response.getJSONObject(i).getString("email"),
                                response.getJSONObject(i).getString("image")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setAdapterRecyclerView(friends);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setAdapterRecyclerView(ArrayList<Friend> friends) {
        if (friends.isEmpty()) {
            txtNoFriends.setVisibility(View.VISIBLE);
            recyclerViewFriends.setVisibility(View.GONE);
        } else {
            txtNoFriends.setVisibility(View.GONE);
            recyclerViewFriends.setVisibility(View.VISIBLE);

            listFriendUserAdapter = new ListFriendUserAdapter(txtNoFriends,recyclerViewFriends,friends, getActivity(), new ListFriendUserAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Friend friend, int position) {
                    Intent intent = new Intent(getActivity(), FriendActivity.class);
                    intent.putExtra("friend", friend);
                    startActivity(intent);
                }
            });
            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewFriends.addItemDecoration(itemDecoration);
            recyclerViewFriends.setHasFixedSize(true);
            recyclerViewFriends.setAdapter(listFriendUserAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addData();
    }
}