package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;
import com.example.socialgift.adapter.GiftFriendAdapter;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListGiftsWishListAdapter;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

public class GiftsListFriendFragment extends Fragment {
    private TextView txtGiftsListNumber,txtNameListToolbar;
    private RecyclerView recyclerViewGiftsList;
    private ImageButton imgBtnBackToolbar;
    private Toolbar toolbar;
    Wishlist wishlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gifts_list_friend, container, false);
        syncronizedWidgets(view);
        syncronizedToolbar();
        addData();
        addDataRecyclerViewGift();
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
            wishlist = (Wishlist) bundle.getSerializable("list");
            txtGiftsListNumber.setText(getResources().getString(R.string.gifts_list_friend,wishlist.getGifts().size()));
            txtNameListToolbar.setText(wishlist.getNameList());
        }
    }
    private void addDataRecyclerViewGift(){
        if(wishlist.getGifts().isEmpty()){
            recyclerViewGiftsList.setVisibility(View.GONE);
        }else{
            recyclerViewGiftsList.setVisibility(View.VISIBLE);
            recyclerViewGiftsList.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewGiftsList.addItemDecoration(itemDecoration);
            recyclerViewGiftsList.addItemDecoration(itemDecoration);
            recyclerViewGiftsList.setHasFixedSize(true);
            recyclerViewGiftsList.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewGiftsList.setAdapter(new GiftFriendAdapter(wishlist, wishlist.getGifts(), getActivity(), new ListGiftsWishListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Wishlist wishlist, int position) {

                }
            }));
        }
    }
}