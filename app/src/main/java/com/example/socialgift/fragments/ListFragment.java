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
import androidx.appcompat.widget.Toolbar;

import com.example.socialgift.R;

import java.util.Objects;


public class ListFragment extends Fragment {

    private Toolbar toolbar;
    private TextView txtViewToolbar;
    private ImageButton imgBtnToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        syncronizeView();
        changeInformationToolbar();
        // Inflate the layout for this fragment
        return view;
    }

    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button);

    }

    private void changeInformationToolbar(){
        txtViewToolbar.setText(getResources().getText(R.string.my_list));
        imgBtnToolbar.setVisibility(View.INVISIBLE);
    }
}