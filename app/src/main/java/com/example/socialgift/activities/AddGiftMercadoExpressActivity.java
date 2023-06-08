package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.dao.DaoRepositoryImages;
import com.example.socialgift.model.Category;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AddGiftMercadoExpressActivity extends AppCompatActivity {
    EditText edtNameGift, edtPriceGift, edtDescriptionGift, edtLinkGift;
    TextView txtChangeImageGift;
    Spinner spnCategoryGift;
    ImageView imgViewGift;
    Toolbar toolbar;
    ImageButton btnBack;
    Button btnAddGift;
    DaoMercadoExpress daoMercadoExpress;
    DaoRepositoryImages daoRepositoryImages;
    ArrayList<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift_mercado_express);
        daoMercadoExpress = DaoMercadoExpress.getInstance(this);
        daoRepositoryImages = new DaoRepositoryImages();
        syncronizedToolbar();
        syncronizedViews();
        getCategory();
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void syncronizedToolbar(){
        toolbar = findViewById(R.id.toolbarCreateGiftMercadoExpress);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBackCreateGiftMercadoExpress);
    }

    private void syncronizedViews(){
        spnCategoryGift = (Spinner) findViewById(R.id.spnCategoryGiftMercadoExpress);
        edtNameGift = (EditText) findViewById(R.id.edtNameGiftMercadoExpress);
        edtDescriptionGift = (EditText) findViewById(R.id.edtDescriptionGiftMercadoExpress);
        edtLinkGift = (EditText) findViewById(R.id.edtLinkGiftMercadoExpress);
        edtPriceGift = (EditText) findViewById(R.id.edtPriceGiftMercadoExpress);
        txtChangeImageGift = (TextView) findViewById(R.id.txtChangeImageGiftMercadoExpress);
        imgViewGift = (ImageView) findViewById(R.id.imgViewGiftMercadoExpress);
        btnAddGift = (Button) findViewById(R.id.btnSaveGift);
    }

    private void getCategory() {

        daoMercadoExpress.getCategories(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Log.d("TAG", "onResponse: NULL");
                    return;
                }
                categories = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String categoryName = response.optJSONObject(i).getString("name");
                        int id = response.optJSONObject(i).getInt("id");
                        Category category = new Category(id, categoryName);
                        categories.add(category);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                setCategorySpinner();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error.getMessage());
            }
        });
    }

    private void setCategorySpinner() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, R.layout.spinner_layout, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Category category = categories.get(position);
                if (category != null) {
                    ((TextView) v).setText(category.getName());
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView == null) {
                    textView = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                } else {
                    textView = (TextView) convertView;
                }
                Category category = getItem(position);
                if (category != null) {
                    textView.setText(category.getName());
                }
                return textView;
            }

        };
        spnCategoryGift.setAdapter(adapter);
    }

    private void changeImageGift(){

    }
}