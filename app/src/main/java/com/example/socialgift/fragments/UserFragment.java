package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgift.R;

public class UserFragment extends Fragment {
    private Button btnFriends, btnBooking, btnMyLists;
    private Toolbar toolbar;
    private TextView txtViewToolbar;
    private ImageButton imgBtnToolbar;
    private TextView txtViewEditProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        syncronizeView(view);
        changeInformationToolbar();
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnFriends);
                changeBackgroundWithOutPressButton(btnBooking,btnMyLists);
            }
        });
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnBooking);
                changeBackgroundWithOutPressButton(btnFriends,btnMyLists);
            }
        });
        btnMyLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundPressButton(btnMyLists);
                changeBackgroundWithOutPressButton(btnFriends,btnBooking);
            }
        });

        txtViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Edit Profile",Toast.LENGTH_SHORT).show();
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
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button);
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
        imgBtnToolbar.setVisibility(View.VISIBLE);
    }
}