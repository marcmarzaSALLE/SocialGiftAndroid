package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialgift.R;

import java.util.Objects;

public class EditUserFragment extends Fragment {

    private TextView txtViewChangePassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        syncronizeWidgets(view);
        txtViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPasswordFragment changePasswordFragment = new EditPasswordFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.editUserFragment, changePasswordFragment).commit();
            }
        });
        return view;
    }

    private void syncronizeWidgets(View view){
        txtViewChangePassword = view.findViewById(R.id.txtViewChangePassword);
    }
}