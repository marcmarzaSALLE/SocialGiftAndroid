package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFragmentAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendFragment extends Fragment {
    ImageView imgFriend;
    ImageButton imgBtnBackToolbar;
    TextView txtFriendName, txtListFriend, txtNoListFriend, txtNameListToolbar;
    DaoSocialGift daoSocialGift;
    Friend friend;
    RecyclerView recyclerViewListFriend;
    Toolbar toolbar;
    ArrayList<Wishlist> wishlists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        syncronizeView(view);
        syncronizedToolbar();
        getIntentData();
        loadData();
        return view;
    }

    private void syncronizedToolbar() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarFriendProfile);
        imgBtnBackToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtNameListToolbar = (TextView) toolbar.findViewById(R.id.toolbar_name);
    }

    private void changeInfoToolbar() {
        txtNameListToolbar.setText(requireContext().getResources().getString(R.string.username, friend.getName(), friend.getLast_name()));
        imgBtnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
    }

    private void syncronizeView(View view) {
        imgFriend = (ImageView) view.findViewById(R.id.imageViewFriend);
        txtFriendName = (TextView) view.findViewById(R.id.txtEmailFriend);
        txtListFriend = (TextView) view.findViewById(R.id.txtListFriendsNumber);
        recyclerViewListFriend = (RecyclerView) view.findViewById(R.id.recyclerViewListFriend);
        txtNoListFriend = (TextView) view.findViewById(R.id.txtNoListFriend);
    }

    private void getIntentData() {
        friend = (Friend) requireActivity().getIntent().getSerializableExtra("friend");
        txtFriendName.setText(friend.getEmail());
        Glide.with(requireContext()).load(friend.getImage()).error(ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.ic_person_24, null)).apply(RequestOptions.circleCropTransform()).into(imgFriend);

    }

    private void loadData() {
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        daoSocialGift.getWishListUser(friend.getId(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                txtListFriend.setText(getResources().getString(R.string.list_friend, response.length()));
                wishlists = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Wishlist list = new Wishlist();
                        list.setId(jsonObject.getInt("id"));
                        list.setNameList(jsonObject.getString("name"));
                        list.setDescriptionList(jsonObject.getString("description"));
                        String[] split = jsonObject.getString("creation_date").split("T");

                        list.setCreatedDateList(split[0]);
                        split = jsonObject.getString("end_date").split("T");
                        list.setEndDateList(split[0]);
                        JSONArray jsonArray = jsonObject.getJSONArray("gifts");
                        ArrayList<GiftWishList> giftsWishLists = new ArrayList<>();
                        if (jsonArray.length() != 0) {
                            Log.d("jsonArray", jsonArray.toString());
                            int booked = 0;
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject giftObject = jsonArray.getJSONObject(j);
                                GiftWishList giftWishList = new GiftWishList(giftObject.getInt("id"), giftObject.getInt("wishlist_id"), giftObject.getString("product_url"), giftObject.getInt("priority"));
                                if (giftObject.getInt("booked") == 1) {
                                    giftWishList.setBooked(true);
                                    booked++;
                                }
                                giftsWishLists.add(giftWishList);
                                //getGiftsFromMercadoExpress(wishlists, list, giftObject.getString("product_url"));
                            }
                            list.setBookedGifts(booked);
                            list.setGifts(giftsWishLists);
                        }
                        wishlists.add(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setAdapterRecyclerview(wishlists);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setAdapterRecyclerview(ArrayList<Wishlist> wishlists) {
        if (wishlists.isEmpty()) {
            recyclerViewListFriend.setVisibility(View.GONE);
            txtNoListFriend.setVisibility(View.VISIBLE);
        } else {
            recyclerViewListFriend.setVisibility(View.VISIBLE);
            txtNoListFriend.setVisibility(View.GONE);
            ListFragmentAdapter listFragmentAdapter = new ListFragmentAdapter(wishlists, requireActivity(), new ListFragmentAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Wishlist list) {
                    if (list.getGifts().isEmpty()) {
                        Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.list_gift_empty), Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", list);
                        GiftsListFriendFragment giftsListFriendFragment = new GiftsListFriendFragment();
                        giftsListFriendFragment.setArguments(bundle);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.friendFragment, giftsListFriendFragment).addToBackStack(null).commit();
                    }
                }
            });

            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewListFriend.addItemDecoration(itemDecoration);
            recyclerViewListFriend.addItemDecoration(itemDecoration);
            recyclerViewListFriend.setHasFixedSize(true);
            recyclerViewListFriend.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewListFriend.setAdapter(listFragmentAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeInfoToolbar();
    }
}