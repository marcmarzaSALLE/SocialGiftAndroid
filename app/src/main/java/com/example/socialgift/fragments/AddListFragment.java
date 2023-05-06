package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.socialgift.R;

public class AddListFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack, imgBtnAddGift, imgBtnAddList;
    private TextView txtListName, txtDeleteList, txtAddGift;
    private EditText edtTxtListName, edtTxtDescription, edtTxtDate;
    private Button btnSaveList, btnAddGift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        syncronizeView();
        showInfoToolbar();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("AddListFragment", "Back button clicked");
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new ListFragment()).commit();
            }
        });
        return view;
    }

    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtListName = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtDeleteList = (TextView) toolbar.findViewById(R.id.txtAddList);
        imgBtnAddList = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
    }

    private void showInfoToolbar() {
        imgBtnAddList.setVisibility(View.INVISIBLE);
        imgBtnBack.setVisibility(View.VISIBLE);
        txtDeleteList.setText(getResources().getText(R.string.delete_list));
    }
}