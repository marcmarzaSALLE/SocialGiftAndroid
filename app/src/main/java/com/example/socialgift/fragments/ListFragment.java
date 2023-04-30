package com.example.socialgift.fragments;

import android.app.ActionBar;
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

import com.example.socialgift.R;

import java.util.Objects;


public class ListFragment extends Fragment {

    private Toolbar toolbar;
    private TextView txtViewToolbar, txtAddList;
    private ImageButton imgBtnToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        syncronizeView();
        changeInformationToolbar();
        txtAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add List", Toast.LENGTH_SHORT).show();
            }
        });
        imgBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add List", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);

    }

    private void changeInformationToolbar() {
        txtViewToolbar.setText(getResources().getText(R.string.my_list));
        imgBtnToolbar.setVisibility(View.VISIBLE);
        imgBtnToolbar.setImageResource(R.drawable.ic_add_green_24);
        txtAddList.setVisibility(View.VISIBLE);
    }
}