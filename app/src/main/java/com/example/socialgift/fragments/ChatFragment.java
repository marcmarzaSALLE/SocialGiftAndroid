package com.example.socialgift.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.activities.FriendActivity;
import com.example.socialgift.adapter.ChatFriendAdapter;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFriendUserAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;

import org.json.JSONArray;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    private Toolbar toolbar;
    private TextView txtViewToolbar,txtAddList;
    private ImageButton imgBtnToolbar;
    EditText edtSearchFriend;
    RecyclerView recyclerViewFriends;
    TextView txtNoFriends;
    DaoSocialGift daoSocialGift;
    ArrayList<Friend> friends;
    ChatFriendAdapter chatFriendAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        daoSocialGift = DaoSocialGift.getInstance(requireActivity().getApplicationContext());
        syncronizedToolbar();
        syncronizeView(view);
        changeInformationToolbar();
        loadFriends();
        searchFriend();
        return view;
    }

    private void searchFriend(){
        edtSearchFriend.setSingleLine(true);
        edtSearchFriend.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_NULL) {
                        hideKeyboard();
                        if (edtSearchFriend.getText().toString().isEmpty()) {
                            loadFriends();
                        } else {
                            chatFriendAdapter.setFriendsList(edtSearchFriend.getText().toString());
                        }
                        return true;
                    }
                    return false;
                }
            });
        edtSearchFriend.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    loadFriends();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //showGiftsBySearch(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    chatFriendAdapter.setFriendsList(edtSearchFriend.getText().toString());
                }
            });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSearchFriend.getWindowToken(), 0);
    }
    private void syncronizeView(View view) {
        recyclerViewFriends = (RecyclerView) view.findViewById(R.id.rvFriendsChat);
        txtNoFriends = (TextView) view.findViewById(R.id.txtNoFriendsChat);
        edtSearchFriend = (EditText) view.findViewById(R.id.edtSearchFriend);

    }
    private void syncronizedToolbar(){
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
        txtAddList = (TextView) toolbar.findViewById(R.id.txtAddList);
    }

    private void changeInformationToolbar(){
        txtViewToolbar.setText(getResources().getText(R.string.messages));
        imgBtnToolbar.setVisibility(View.GONE);
        txtAddList.setVisibility(View.GONE);
    }
    private void loadFriends(){
        daoSocialGift.getUsersChat(new Response.Listener<JSONArray>() {
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
        },new Response.ErrorListener() {
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

            chatFriendAdapter = new ChatFriendAdapter(txtNoFriends,recyclerViewFriends,friends, requireActivity(), new ChatFriendAdapter.OnItemClickListener() {
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
            recyclerViewFriends.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewFriends.setAdapter(chatFriendAdapter);
        }
    }

}