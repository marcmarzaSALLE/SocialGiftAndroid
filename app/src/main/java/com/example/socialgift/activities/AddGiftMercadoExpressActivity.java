package com.example.socialgift.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.dao.DaoRepositoryImages;
import com.example.socialgift.fragments.EditUserFragment;
import com.example.socialgift.model.Category;
import com.example.socialgift.model.Gift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    Gift gift;


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

        txtChangeImageGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                someActivityResultLauncher.launch(intent);
            }
        });
        btnAddGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void syncronizedToolbar() {
        toolbar = findViewById(R.id.toolbarCreateGiftMercadoExpress);
        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBackCreateGiftMercadoExpress);
    }

    private void syncronizedViews() {
        spnCategoryGift = (Spinner) findViewById(R.id.spnCategoryGiftMercadoExpress);
        edtNameGift = (EditText) findViewById(R.id.edtNameGiftMercadoExpress);
        edtDescriptionGift = (EditText) findViewById(R.id.edtDescriptionGiftMercadoExpress);
        edtLinkGift = (EditText) findViewById(R.id.edtLinkGiftMercadoExpress);
        edtPriceGift = (EditText) findViewById(R.id.edtPriceGiftMercadoExpress);
        txtChangeImageGift = (TextView) findViewById(R.id.txtChangeImageGiftMercadoExpress);
        imgViewGift = (ImageView) findViewById(R.id.imgGiftMercadoExpress);
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
                    ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
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

    private void changeImageGift() {

    }

    private void checkFields() {
        if (edtNameGift.getText().toString().isEmpty()) {
            edtNameGift.setError(getResources().getString(R.string.name_empty));
        } else if (edtDescriptionGift.getText().toString().isEmpty()) {
            edtDescriptionGift.setError(getResources().getString(R.string.description_empty));
        } else if (edtLinkGift.getText().toString().isEmpty()) {
            edtLinkGift.setError(getResources().getString(R.string.link_empty));
        } else if (edtPriceGift.getText().toString().isEmpty()) {
            edtPriceGift.setError(getResources().getString(R.string.price_empty));
        }else {
            addGift();
        }
    }

    private void addGift(){
        int idCategory=0;
        String category = spnCategoryGift.getSelectedItem().toString();
        for(Category c: categories){
            if(c.getName().equalsIgnoreCase(category)){
                idCategory = c.getId();
            }
        }
        String name = edtNameGift.getText().toString();
        String description = edtDescriptionGift.getText().toString();
        String link = edtLinkGift.getText().toString();
        String price = edtPriceGift.getText().toString();

        Gift newGift = new Gift();
        newGift.setName(name);
        newGift.setDescription(description);
        newGift.setLink(link);
        newGift.setPrice(Double.parseDouble(price));
        newGift.setIdCategory(idCategory);

        daoMercadoExpress.createGiftMercadoExpress(newGift, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    private File getFileFromUriImage(Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return new File(filePath);
        }
        return null;
    }
    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        String path = getFileFromUri(selectedImage);
                        imgViewGift.setImageURI(selectedImage);
                        daoRepositoryImages.uploadFile(new File(path),new DaoRepositoryImages.DaoRepositoryImagesListener() {
                            @Override
                            public void onSuccess(String url) {
                                AddGiftMercadoExpressActivity.this.setImage(url);
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("Error", error);
                            }
                        });

                    }
                }
            });

    private String getFileFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }
    public void setImage(String url){
        gift.setUrlImage(url);
    }
}