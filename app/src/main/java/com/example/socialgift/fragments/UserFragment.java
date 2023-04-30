package com.example.socialgift.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgift.R;
import com.example.socialgift.activities.EditProfileActivity;
import com.example.socialgift.activities.LoginActivity;

import java.util.Objects;

public class UserFragment extends Fragment {
    private Button btnFriends, btnBooking, btnMyLists;
    private Toolbar toolbar;
    private TextView txtViewToolbar,txtAddList;
    private ImageButton imgBtnLogOut;
    private TextView txtViewEditProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        syncronizeView(view);
        changeInformationToolbar();
        replace(new FriendsUserFragment());
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnFriends);
                changeBackgroundWithOutPressButton(btnBooking,btnMyLists);
                replace(new FriendsUserFragment());
            }
        });
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnBooking);
                changeBackgroundWithOutPressButton(btnFriends,btnMyLists);
                replace(new BookingUserFragment());
            }
        });
        btnMyLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnMyLists);
                changeBackgroundWithOutPressButton(btnFriends,btnBooking);
                replace(new MyListUserFragment());
            }
        });

        txtViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        imgBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });


        return view;
    }

    private void syncronizeView(View view) {
        btnFriends = (Button) view.findViewById(R.id.btnFriends);
        btnBooking = (Button) view.findViewById(R.id.btnBooking);
        btnMyLists = (Button) view.findViewById(R.id.btnMyLists);
        txtViewEditProfile = (TextView) view.findViewById(R.id.txtEditProfile);

        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnLogOut = (ImageButton) toolbar.findViewById(R.id.toolbar_button);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);
    }
    private void changeBackgroundPressButton(Button button){
        button.setBackgroundResource(R.drawable.btn_background_menu_user_press);
    }

    private void changeBackgroundWithOutPressButton(Button button,Button button2){
        button.setBackgroundResource(R.drawable.btn_background_menu_user_without_press);
        button2.setBackgroundResource(R.drawable.btn_background_menu_user_without_press);
    }
    private void changeInformationToolbar(){
        txtViewToolbar.setText(getResources().getText(R.string.username));
        imgBtnLogOut.setVisibility(View.VISIBLE);
        imgBtnLogOut.setImageResource(R.drawable.ic_logout_black_24);
        txtAddList.setVisibility(View.GONE);
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentUserContainer,fragment);
        transaction.commit();
    }
}