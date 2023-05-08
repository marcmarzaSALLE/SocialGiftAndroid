package com.example.socialgift.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.socialgift.R;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;

import java.util.Objects;

public class EditPasswordFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack;

    private VolleyRequest volleyRequest;

    private SharedPreferencesController sharedPreferencesController;
    private Button btnSave;

    private EditText edtCurrentPassword, edtNewPassword, edtNewPasswordConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);
        sharedPreferencesController = new SharedPreferencesController();
        syncronizeView(view);
        imgBtnBack.setOnClickListener(v -> {
            Log.wtf("EditPasswordFragment", "onClick: ");
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.editUserFragment, new EditUserFragment())
                    .commit();
        });

        btnSave.setOnClickListener(v -> {
            savePassword();
        });
        return view;
    }

    private void syncronizeView(View view) {
        btnSave = (Button) view.findViewById(R.id.saveBtnPassword);
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarEditProfile);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_edit_button_back);
        edtCurrentPassword = (EditText) view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = (EditText) view.findViewById(R.id.edtNewPassword);
        edtNewPasswordConfirm = (EditText) view.findViewById(R.id.edtNewPasswordConfirm);

    }

    private void savePassword(){
        if (checkData()){
            Log.wtf("holaa","Si");
        }
        Log.wtf("holaa","no");


    }

    private boolean checkData(){
        if(edtCurrentPassword.getText().toString().matches("") ){
            edtCurrentPassword.setError("Current password required");
            return false;

        }else if (edtNewPassword.getText().toString().matches("")){
                edtNewPassword.setError("New password required");
                return false;

        }else if (edtNewPasswordConfirm.getText().toString().matches("")){
                edtNewPasswordConfirm.setError("Confirm password required");
                return false;
        }else if(!edtNewPassword.getText().toString().equals(edtNewPasswordConfirm.getText().toString())){
            edtNewPasswordConfirm.setError("Passwords don't match");
            return false;

        }else if(!sharedPreferencesController.passwordEncrypt(edtCurrentPassword.getText().toString()).equals(sharedPreferencesController.loadPasswordSharedPreferences(requireContext().getApplicationContext()))){
            edtCurrentPassword.setError("Incorrect password");
            return false;

        }
        return true;
    }
}