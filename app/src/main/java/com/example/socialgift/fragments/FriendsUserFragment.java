package com.example.socialgift.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.activities.FriendActivity;
import com.example.socialgift.activities.FriendsRequestActivity;
import com.example.socialgift.adapter.AllUsersAdapter;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFriendUserAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;
import com.example.socialgift.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsUserFragment extends Fragment {
    private ImageButton imgBtnFriendsRequest;
    private TextView txtNoFriends;
    private RecyclerView recyclerViewFriends, recyclerViewAllUsers;
    ArrayList<Friend> friends;
    ArrayList<User> allUsers;
    DaoSocialGift daoSocialGift;
    ListFriendUserAdapter listFriendUserAdapter;
    AllUsersAdapter allUsersAdapter;
    EditText searchUser;
    ProgressBar progressBarFriends;
    ProgressBar progressBarAllUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_user, container, false);
        syncronizeView(view);
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        addDataFriends();
        addDataAllUsers();
        imgBtnFriendsRequest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendsRequestActivity.class);
            startActivity(intent);
        });
        edittextAction();
        return view;

    }

    private void syncronizeView(View view) {
        imgBtnFriendsRequest = (ImageButton) view.findViewById(R.id.imgBtnFriendRequest);
        searchUser = (EditText) view.findViewById(R.id.edtSearchFriendUser);
        txtNoFriends = (TextView) view.findViewById(R.id.txtNoFriends);
        recyclerViewFriends = (RecyclerView) view.findViewById(R.id.recyclerViewFriends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewAllUsers = (RecyclerView) view.findViewById(R.id.recyclerViewUsers);
        recyclerViewAllUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBarFriends = (ProgressBar) view.findViewById(R.id.progressBarFriends);
        progressBarAllUsers = (ProgressBar) view.findViewById(R.id.progressBarAllUsers);
    }

    private void addDataFriends() {
        progressBarFriends.setVisibility(View.VISIBLE);
        txtNoFriends.setVisibility(View.GONE);
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
                progressBarFriends.setVisibility(View.GONE);
                setAdapterRecyclerView(friends);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarFriends.setVisibility(View.GONE);
            }
        });
    }

    private void addDataAllUsers() {
        progressBarAllUsers.setVisibility(View.VISIBLE);
        daoSocialGift.getAllUsers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                allUsers = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        allUsers.add(new User(response.getJSONObject(i).getInt("id"),
                                response.getJSONObject(i).getString("name"),
                                response.getJSONObject(i).getString("last_name"),
                                response.getJSONObject(i).getString("email"),
                                response.getJSONObject(i).getString("image")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                progressBarAllUsers.setVisibility(View.GONE);
                setAdapterRecyclerViewAllUsers(allUsers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarAllUsers.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapterRecyclerViewAllUsers(ArrayList<User> users) {
        recyclerViewAllUsers.setVisibility(View.VISIBLE);
        allUsersAdapter = new AllUsersAdapter(users, getActivity(), new AllUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend friend, int position) {

            }
        });
        int spanCount = 1;
        int spacing = 3;
        GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
        recyclerViewAllUsers.addItemDecoration(itemDecoration);
        recyclerViewAllUsers.setHasFixedSize(true);
        recyclerViewAllUsers.setAdapter(allUsersAdapter);

    }

    private void setAdapterRecyclerView(ArrayList<Friend> friends) {
        if (friends.isEmpty()) {
            txtNoFriends.setVisibility(View.VISIBLE);
            recyclerViewFriends.setVisibility(View.GONE);
        } else {
            txtNoFriends.setVisibility(View.GONE);
            recyclerViewFriends.setVisibility(View.VISIBLE);

            listFriendUserAdapter = new ListFriendUserAdapter(txtNoFriends, recyclerViewFriends, friends, getActivity(), new ListFriendUserAdapter.OnItemClickListener() {
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

    private void edittextAction() {
        searchUser.setSingleLine(true);
        searchUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_NULL) {
                    hideKeyboard();
                    if (searchUser.getText().toString().isEmpty()) {
                        addDataAllUsers();
                        addDataFriends();
                    } else {
                        showUsersBySearch(searchUser.getText().toString());
                        listFriendUserAdapter.searchFriend(searchUser.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addDataAllUsers();
                addDataFriends();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //showGiftsBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                showUsersBySearch(s.toString());
                listFriendUserAdapter.searchFriend(searchUser.getText().toString());
                ;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchUser.getWindowToken(), 0);
    }

    private void showUsersBySearch(String search) {
        daoSocialGift.searchUser(search, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                allUsers = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("last_name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("image"));
                        allUsers.add(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allUsersAdapter.setAllUsers(allUsers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.error_load_gifts), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        addDataFriends();
        addDataAllUsers();
    }
}