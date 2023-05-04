package com.example.socialgift.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.socialgift.R;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.VolleyRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class EditUserFragment extends Fragment {

    private TextView txtViewChangePassword;
    private EditText edtTxtName, edtTxtLastName, edtTxtEmail;
    private ImageView imgViewProfile;
    private TextInputLayout txtInputLayoutName, txtInputLayoutLastName, txtInputLayoutEmail;
    private VolleyRequest volleyRequest;
    private SharedPreferencesController sharedPreferencesController;

    private Button btnSaveEdit;

    private String name, lastName, email,password, urlImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        syncronizeWidgets(view);
        getInformationUser();

        edtTxtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtInputLayoutName.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                    txtInputLayoutName.setHint(getResources().getString(R.string.name));
                }else{
                    txtInputLayoutName.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    if (edtTxtName.getText().toString().isEmpty()){
                        txtInputLayoutName.setHint(name);
                        edtTxtName.setHint("");
                    }else{
                        txtInputLayoutName.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                        txtInputLayoutName.setHint(getResources().getString(R.string.name));
                    }
                }
            }
        });
        edtTxtLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtInputLayoutLastName.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                    txtInputLayoutLastName.setHint(getResources().getString(R.string.lastname));
                }else{
                    txtInputLayoutLastName.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    if (edtTxtLastName.getText().toString().isEmpty()){
                        txtInputLayoutLastName.setHint(lastName);
                        edtTxtLastName.setHint("");
                    }else{
                        txtInputLayoutLastName.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                        txtInputLayoutLastName.setHint(getResources().getString(R.string.lastname));
                    }
                }
            }
        });

        edtTxtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtInputLayoutEmail.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                    txtInputLayoutEmail.setHint(getResources().getString(R.string.email));
                }else{
                    txtInputLayoutEmail.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    if (edtTxtEmail.getText().toString().isEmpty()){
                        txtInputLayoutEmail.setHint(email);
                        edtTxtEmail.setHint("");
                    }else{
                        txtInputLayoutEmail.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                        txtInputLayoutEmail.setHint(getResources().getString(R.string.email));
                    }
                }
            }
        });
        txtViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPasswordFragment changePasswordFragment = new EditPasswordFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.editUserFragment, changePasswordFragment).commit();
            }
        });

        btnSaveEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!edtTxtName.getText().toString().isEmpty()) {
                    name = edtTxtName.getText().toString();
                }
                if (!edtTxtLastName.getText().toString().isEmpty()) {
                    lastName = edtTxtLastName.getText().toString();
                }
                if (!edtTxtEmail.getText().toString().isEmpty()) {
                    email = edtTxtEmail.getText().toString();
                }
                volleyRequest.editMyUser(name, lastName, email, "password", urlImage, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requireActivity().finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });
            }
        });

        return view;
    }

    private void syncronizeWidgets(View view){
        txtViewChangePassword = view.findViewById(R.id.txtViewChangePassword);
        edtTxtName = view.findViewById(R.id.editTextUsername);
        edtTxtLastName = view.findViewById(R.id.edtTextLastName);
        edtTxtEmail = view.findViewById(R.id.edtTxtEmail);
        imgViewProfile = view.findViewById(R.id.imageViewEdit);
        txtInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        txtInputLayoutLastName = view.findViewById(R.id.textInputLayoutLastName);
        txtInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        btnSaveEdit = view.findViewById(R.id.saveBtn);

        volleyRequest = new VolleyRequest(requireActivity().getApplicationContext());
        sharedPreferencesController = new SharedPreferencesController();
    }

    private void getInformationUser(){
        volleyRequest.getMyUser(sharedPreferencesController.loadUserIdSharedPreferences(requireActivity().getApplicationContext()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name = response.getString("name");
                    lastName = response.getString("last_name");
                    email = response.getString("email");
                    urlImage = response.getString("image");
                    edtTxtName.setHint(name);
                    edtTxtLastName.setHint(lastName);
                    edtTxtEmail.setHint(email);

                    Glide.with(requireContext()).load(response.getString("image")).into(imgViewProfile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("error",error.toString());
                Toast.makeText(requireActivity().getApplicationContext(), "Error al cargar la información del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}