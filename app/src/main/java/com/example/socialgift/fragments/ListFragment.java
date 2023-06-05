package com.example.socialgift.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.activities.AddListActivity;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFragmentAdapter;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private Toolbar toolbar;
    private TextView txtViewToolbar, txtAddList, txtNoList;

    private RecyclerView recyclerViewList;
    private ImageButton imgBtnToolbar, imgBtnBackToolbar;
    private ArrayList<Wishlist> wishlists;
    private DaoSocialGift daoSocialGift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        syncronizeViewToolbar();
        syncronizeViewWidgets(view);
        changeInformationToolbar();
        addData();
        txtAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddListActivity.class);
                startActivity(intent);
            }
        });
        imgBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void syncronizeViewToolbar() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
        imgBtnBackToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);


    }

    private void syncronizeViewWidgets(View view) {
        recyclerViewList = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        txtNoList = (TextView) view.findViewById(R.id.txtNoList);
    }

    private void changeInformationToolbar() {
        txtViewToolbar.setText(getResources().getText(R.string.my_list));
        txtAddList.setText(getResources().getText(R.string.add_list));
        imgBtnToolbar.setVisibility(View.VISIBLE);
        imgBtnToolbar.setImageResource(R.drawable.ic_add_green_24);
        txtAddList.setVisibility(View.VISIBLE);
        imgBtnBackToolbar.setVisibility(View.GONE);

    }

    private void addData() {
        SharedPreferencesController sharedPreferencesController = new SharedPreferencesController();
        daoSocialGift.getWishListUser(sharedPreferencesController.loadUserIdSharedPreferences(requireActivity()), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
                            int booked = 0;
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject giftObject = jsonArray.getJSONObject(j);
                                GiftWishList giftWishList = new GiftWishList(giftObject.getInt("id"),giftObject.getInt("wishlist_id"),giftObject.getString("product_url"),giftObject.getInt("priority"));
                                if (giftObject.getInt("booked")==1) {
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
            recyclerViewList.setVisibility(View.GONE);
        } else {
            recyclerViewList.setVisibility(View.VISIBLE);
            txtNoList.setVisibility(View.GONE);
            ListFragmentAdapter listFragmentAdapter = new ListFragmentAdapter(wishlists, requireActivity(), new ListFragmentAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Wishlist list) {
                    Intent intent = new Intent(getActivity(), AddListActivity.class);
                    intent.putExtra("wishlist", list);
                    startActivity(intent);
                }
            });

            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewList.addItemDecoration(itemDecoration);
            recyclerViewList.addItemDecoration(itemDecoration);
            recyclerViewList.setHasFixedSize(true);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewList.setAdapter(listFragmentAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        changeInformationToolbar();
        addData();
    }
}