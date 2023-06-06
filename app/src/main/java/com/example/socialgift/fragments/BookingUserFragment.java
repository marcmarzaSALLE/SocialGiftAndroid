package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListGiftBookedAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookingUserFragment extends Fragment {
    TextView txtNoBookings;
    RecyclerView rvBookings;
    DaoSocialGift daoSocialGift;
    ArrayList<GiftWishList>giftsBooked;
    ProgressBar progressBarBooking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_user, container, false);
        syncronizedData(view);
        daoSocialGift = new DaoSocialGift(requireContext());
        getData();


        return view;
    }
    private void syncronizedData(View view){
        txtNoBookings = view.findViewById(R.id.txt_no_booking);
        rvBookings = view.findViewById(R.id.recycler_view_booking_user);
        progressBarBooking = view.findViewById(R.id.progressBarBookingUser);
    }

    private void getData(){
        progressBarBooking.setVisibility(View.VISIBLE);
        daoSocialGift.getMyGiftsBooked(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                giftsBooked = new ArrayList<>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        GiftWishList giftWishList = new GiftWishList();
                        giftWishList.setId(jsonObject.getInt("id"));
                        giftWishList.setIdWishList(jsonObject.getInt("wishlist_id"));
                        giftWishList.setProductLink(jsonObject.getString("product_url"));
                        giftWishList.setPriority(jsonObject.getInt("priority"));
                        giftWishList.setBooked(jsonObject.getInt("booked") == 1);
                        giftsBooked.add(giftWishList);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                progressBarBooking.setVisibility(View.GONE);
                setAdapterRecyclerView(giftsBooked);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarBooking.setVisibility(View.GONE);
                Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.error_get_reserved_gift), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterRecyclerView(ArrayList<GiftWishList>giftsBooked){
        if (giftsBooked.isEmpty()) {
            rvBookings.setVisibility(View.GONE);
        } else {
            rvBookings.setVisibility(View.VISIBLE);
            txtNoBookings.setVisibility(View.GONE);
            ListGiftBookedAdapter listGiftBookedAdapter = new ListGiftBookedAdapter(giftsBooked, requireContext(), new ListGiftBookedAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Wishlist wishlist, int position) {

                }
            });
            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            rvBookings.addItemDecoration(itemDecoration);
            rvBookings.addItemDecoration(itemDecoration);
            rvBookings.setHasFixedSize(true);
            rvBookings.setLayoutManager(new LinearLayoutManager(requireActivity()));
            rvBookings.setAdapter(listGiftBookedAdapter);

        }
    }
    public void refreshFragment(){
        getData();
    }
}