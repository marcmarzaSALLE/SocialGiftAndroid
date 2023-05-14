package com.example.socialgift.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.socialgift.activities.AddListActivity;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListFragmentAdapter;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyListUserFragment extends Fragment {

    private RecyclerView recyclerViewList;
    private TextView txtNoList;

    private ArrayList<Wishlist> wishlists;
    private VolleyRequest volleyRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        syncronizeViewWidgets(view);
        volleyRequest = new VolleyRequest(requireActivity().getApplicationContext());
        addData();

        return view;
    }

    private void syncronizeViewWidgets(View view) {
        recyclerViewList = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        txtNoList = (TextView) view.findViewById(R.id.txtNoList);
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

    private void addData() {
        SharedPreferencesController sharedPreferencesController = new SharedPreferencesController();
        volleyRequest.getWishListUser(sharedPreferencesController.loadUserIdSharedPreferences(requireActivity()), new Response.Listener<JSONArray>() {
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
                        if (jsonArray.length() != 0) {
                            int booked = 0;
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject giftObject = jsonArray.getJSONObject(j);
                                if (giftObject.getInt("booked")==1) {
                                    booked++;
                                }
                                //getGiftsFromMercadoExpress(wishlists, list, giftObject.getString("product_url"));
                            }
                            list.setBookedGifts(booked);

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
}