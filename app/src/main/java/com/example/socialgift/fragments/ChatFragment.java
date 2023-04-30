package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgift.R;


public class ChatFragment extends Fragment {
    private Toolbar toolbar;
    private TextView txtViewToolbar,txtAddList;
    private ImageButton imgBtnToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        syncronizeView();
        changeInformationToolbar();
        return view;
    }
    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtViewToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnToolbar = (ImageButton) toolbar.findViewById(R.id.toolbar_button);
        txtAddList = (TextView) requireActivity().findViewById(R.id.txtAddList);

    }

    private void changeInformationToolbar(){
        txtViewToolbar.setText(getResources().getText(R.string.messages));
        imgBtnToolbar.setVisibility(View.GONE);
        txtAddList.setVisibility(View.GONE);
    }
}