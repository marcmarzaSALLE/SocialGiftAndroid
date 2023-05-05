package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.socialgift.R;

import java.util.Objects;

public class EditPasswordFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);
        syncronizeView();
        imgBtnBack.setOnClickListener(v -> {
            Log.wtf("EditPasswordFragment", "onClick: ");
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.editUserFragment, new EditUserFragment())
                    .commit();
        });
        return view;
    }

    private void syncronizeView() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarEditProfile);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_edit_button_back);

    }
}