package com.example.socialgift.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.activities.EditProfileActivity;
import com.example.socialgift.activities.LoginActivity;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.DaoSocialGift;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFragment extends Fragment {
    private Button btnFriends, btnBooking, btnMyLists;
    private Toolbar toolbar;
    private TextView txtViewToolbar,txtAddList,txtViewEmailUser,txtViewEditProfile;
    private ImageButton imgBtnLogOut;
    private ImageView imgViewProfile;
    private DaoSocialGift daoSocialGift;
    private SharedPreferencesController sharedPreferencesController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        syncronizeView(view);
        changeInformationToolbar();
        getInformationUser();
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
                sharedPreferencesController.deleteDateSharedPreferences(requireActivity().getApplicationContext());
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
        imgViewProfile = (ImageView) view.findViewById(R.id.imageViewUser);
        txtViewEmailUser = (TextView) view.findViewById(R.id.txtEmailUser);

        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnLogOut = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);

        sharedPreferencesController = new SharedPreferencesController();
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
    }
    private void changeBackgroundPressButton(Button button){
        button.setBackgroundResource(R.drawable.btn_background_menu_user_press);
    }

    private void changeBackgroundWithOutPressButton(Button button,Button button2){
        button.setBackgroundResource(R.drawable.btn_background_menu_user_without_press);
        button2.setBackgroundResource(R.drawable.btn_background_menu_user_without_press);
    }
    private void changeInformationToolbar(){
        imgBtnLogOut.setVisibility(View.VISIBLE);
        imgBtnLogOut.setImageResource(R.drawable.ic_logout_black_24);
        txtAddList.setVisibility(View.GONE);
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentUserContainer,fragment);
        transaction.commit();
    }

    private void getInformationUser(){
        daoSocialGift.getMyUser(sharedPreferencesController.loadUserIdSharedPreferences(requireActivity().getApplicationContext()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    txtViewToolbar.setText(getResources().getString(R.string.username,response.getString("name") ,response.getString("last_name")));

                    txtViewEmailUser.setText(response.getString("email"));
                    ShapeDrawable borderDrawable = new ShapeDrawable();
                    borderDrawable.setShape(new OvalShape());
                    borderDrawable.getPaint().setColor(ContextCompat.getColor(requireActivity().getApplicationContext(), R.color.green_button_user));
                    borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
                    borderDrawable.getPaint().setStrokeWidth(5);
                    imgViewProfile.setBackground(borderDrawable);
                    Glide.with(requireContext()).load(response.getString("image")).error(ResourcesCompat.getDrawable(requireActivity().getResources(),R.drawable.ic_person_24,null)).apply(RequestOptions.circleCropTransform()).into(imgViewProfile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity().getApplicationContext(), getResources().getString(R.string.error_get_user_info), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getInformationUser();
    }
}