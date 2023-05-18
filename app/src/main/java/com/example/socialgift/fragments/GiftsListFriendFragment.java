package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

public class GiftsListFriendFragment extends Fragment {
    private TextView txtGiftsListNumber,txtNameListToolbar;
    private RecyclerView recyclerViewGiftsList;
    private ImageButton imgBtnBackToolbar;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gifts_list_friend, container, false);
        syncronizedWidgets(view);
        syncronizedToolbar();
        addData();
        imgBtnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        return view;
    }

    private void syncronizedToolbar(){
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarFriendProfile);
        imgBtnBackToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtNameListToolbar = (TextView) toolbar.findViewById(R.id.toolbar_name);

    }
    private void syncronizedWidgets(View view) {
        txtGiftsListNumber = (TextView) view.findViewById(R.id.txtGiftNumber);
        recyclerViewGiftsList = (RecyclerView) view.findViewById(R.id.recyclerViewGiftsListFriend);
    }

    private void addData(){
        Bundle bundle = getArguments();
        if(bundle != null){
            Wishlist wishlist = (Wishlist) bundle.getSerializable("list");
            txtGiftsListNumber.setText(getResources().getString(R.string.gifts_list_friend,wishlist.getGifts().size()));
            txtNameListToolbar.setText(wishlist.getNameList());
        }
    }
}