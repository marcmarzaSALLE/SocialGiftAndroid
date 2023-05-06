package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.socialgift.R;


public class ListFragment extends Fragment {

    private Toolbar toolbar;
    private TextView txtViewToolbar, txtAddList;
    private ImageButton imgBtnToolbar,imgBtnBackToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        syncronizeView();
        changeInformationToolbar();
        txtAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new AddListFragment()).commit();
            }
        });
        imgBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new AddListFragment()).commit();
            }
        });
        return view;
    }

    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
        imgBtnBackToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);

    }

    private void changeInformationToolbar() {
        txtViewToolbar.setText(getResources().getText(R.string.my_list));
        txtAddList.setText(getResources().getText(R.string.add_list));
        imgBtnToolbar.setVisibility(View.VISIBLE);
        imgBtnToolbar.setImageResource(R.drawable.ic_add_green_24);
        txtAddList.setVisibility(View.VISIBLE);
        imgBtnBackToolbar.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        changeInformationToolbar();
    }
}