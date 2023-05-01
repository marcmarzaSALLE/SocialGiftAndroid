package com.example.socialgift.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.socialgift.R;
import com.example.socialgift.activities.FriendsRequestActivity;

public class FriendsUserFragment extends Fragment {
    private ImageButton imgBtnFriendsRequest;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friends_user, container, false);
        syncronizeView(view);

        imgBtnFriendsRequest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendsRequestActivity.class);
            startActivity(intent);
        });
        return view;

    }

    private void syncronizeView(View view) {
        imgBtnFriendsRequest = (ImageButton) view.findViewById(R.id.imgBtnFriendRequest);
        searchView = (SearchView) view.findViewById(R.id.searchView);
    }


}