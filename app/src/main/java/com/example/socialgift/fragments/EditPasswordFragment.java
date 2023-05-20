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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.controller.Manager;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.controller.UserData;
import com.example.socialgift.dao.DaoSocialGift;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPasswordFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack,imgBtnLogout;

    ImageView imgViewProfile;

    UserData userData;

    private DaoSocialGift daoSocialGift;

    private SharedPreferencesController sharedPreferencesController;
    private Manager manager;
    private Button btnSave;

    private EditText edtCurrentPassword, edtNewPassword, edtNewPasswordConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);
        sharedPreferencesController = new SharedPreferencesController();
        manager = new Manager();
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
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
        imgBtnLogout = (ImageButton) toolbar.findViewById(R.id.toolbar_button_logout);
        imgBtnLogout.setVisibility(View.GONE);
        edtCurrentPassword = (EditText) view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = (EditText) view.findViewById(R.id.edtNewPassword);
        edtNewPasswordConfirm = (EditText) view.findViewById(R.id.edtNewPasswordConfirm);
        userData = UserData.getInstance();


    }

    private void savePassword() {
        if (checkData()) {

            if (userData.isInitialized()) {
                daoSocialGift.editMyUser(userData.getName(), userData.getLast_name(), userData.getEmail(), edtNewPassword.getText().toString(), userData.getImage(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String passwordHash = manager.passwordEncrypt(edtNewPassword.getText().toString());
                        sharedPreferencesController.savePasswordSharedPreferences(passwordHash, requireContext().getApplicationContext());
                        Toast.makeText(requireContext().getApplicationContext(), getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

            } else {

                daoSocialGift.getMyUser(sharedPreferencesController.loadUserIdSharedPreferences(requireActivity()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            String lastName = response.getString("last_name");
                            String email = response.getString("email");
                            String password = edtNewPasswordConfirm.getText().toString();
                            String urlImage = response.getString("image");
                            editMyUser(name, lastName, email, password, urlImage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("EditPasswordFragment", "onErrorResponse: " + error.getMessage());
                    }
                });
            }
        }
    }

    public void editMyUser(String name, String lastName, String email, String password, String urlImage){
        daoSocialGift.editMyUser(name, lastName, email, password, urlImage, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String passwordHash = manager.passwordEncrypt(password);
                sharedPreferencesController.savePasswordSharedPreferences(passwordHash, requireContext().getApplicationContext());
                Toast.makeText(requireContext().getApplicationContext(), getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("EditPasswordFragment", "onErrorResponse: " + error.getMessage());
            }
        });
    }

    private boolean checkData() {
        if (edtCurrentPassword.getText().toString().matches("")) {
            edtCurrentPassword.setError(getResources().getString(R.string.current_password_required));
            return false;

        } else if (edtNewPassword.getText().toString().matches("")) {
            edtNewPassword.setError(getResources().getString(R.string.new_password_required));
            return false;

        } else if (edtNewPasswordConfirm.getText().toString().matches("")) {
            edtNewPasswordConfirm.setError(getResources().getString(R.string.confirm_password_required));
            return false;
        } else if (!edtNewPassword.getText().toString().equals(edtNewPasswordConfirm.getText().toString())) {
            edtNewPasswordConfirm.setError(getResources().getString(R.string.password_confirm));
            return false;

        } else if (!manager.passwordEncrypt(edtCurrentPassword.getText().toString()).equals(sharedPreferencesController.loadPasswordSharedPreferences(requireContext().getApplicationContext()))) {
            edtCurrentPassword.setError(getResources().getString(R.string.password_incorrect));
            return false;
        }
        return true;
    }
}