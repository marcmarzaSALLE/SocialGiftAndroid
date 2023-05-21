package com.example.socialgift.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoMercadoExpress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AddGiftFragment extends Fragment {
    EditText edtSearchGift;
    Spinner spnCategory;
    DaoMercadoExpress daoMercadoExpress;
    ArrayList<String> nameCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gift, container, false);
        daoMercadoExpress = DaoMercadoExpress.getInstance(requireContext());
        syncronizedWidgets(view);
        getCategory();
        return view;
    }

    private void syncronizedWidgets(View view) {
        edtSearchGift = view.findViewById(R.id.edtSearchGift);
        spnCategory = view.findViewById(R.id.spnCategory);
    }

    private void getCategory() {

        daoMercadoExpress.getCategories(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Log.d("TAG", "onResponse: NULL");
                    return;
                }
                nameCategories = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String category = response.optJSONObject(i).getString("name");
                        Log.d("TAG", "onResponse: " + category);
                        nameCategories.add(category);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error.getMessage());
            }
        });

    }


}