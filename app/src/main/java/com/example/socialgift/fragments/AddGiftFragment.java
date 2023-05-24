package com.example.socialgift.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GiftMercadoExpressAdapter;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.model.Gift;
import com.example.socialgift.model.Wishlist;

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
    Toolbar toolbar;
    ImageButton imgBtnBack;
    TextView txtAddGiftToolbar,txtCreateGiftToolbar;
    RecyclerView rvGifts;
    ArrayList<Gift>gifts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gift, container, false);
        daoMercadoExpress = DaoMercadoExpress.getInstance(requireContext());
        syncronizedWidgets(view);
        syncronizedToolbar();
        setInfoToolbar();
        getCategory();
        showAllGifts();

        imgBtnBack.setOnClickListener(v->{
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameAddList, new AddListFragment());
            transaction.commit();

        });
        return view;
    }
    private void syncronizedToolbar(){
        toolbar = requireActivity().findViewById(R.id.toolbarAddList);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back_add_list);
        txtCreateGiftToolbar = (TextView) toolbar.findViewById(R.id.txtDeleteList);
        txtAddGiftToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title_add_list);
    }

    private void setInfoToolbar(){
        txtAddGiftToolbar.setText(requireContext().getResources().getString(R.string.add_gift));
        txtCreateGiftToolbar.setText(requireContext().getResources().getString(R.string.create_gift));

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_24, null);
        txtCreateGiftToolbar.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


    }
    private void syncronizedWidgets(View view) {
        edtSearchGift = view.findViewById(R.id.edtSearchGift);
        spnCategory = view.findViewById(R.id.spnCategory);
        rvGifts = view.findViewById(R.id.rvGifts);
    }
    private void setCategorySpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_layout, nameCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
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
                        Log.d("TAG", "CATEGORIAS: " + category);
                        nameCategories.add(category);
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

    private void showAllGifts(){
        daoMercadoExpress.getAllGift(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                gifts = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Gift gift = new Gift();
                        gift.setId(jsonObject.getInt("id"));
                        gift.setName(jsonObject.getString("name"));
                        gift.setDescription(jsonObject.getString("description"));
                        gift.setLink(jsonObject.getString("link"));
                        gift.setUrlImage(jsonObject.getString("photo"));
                        gift.setPrice(jsonObject.getDouble("price"));
                        //gift.setIdCategory(jsonObject.getInt("categoryIds"));

                        gifts.add(gift);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setAdapterGifts(gifts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.error_load_gifts), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterGifts(ArrayList<Gift>gifts){
        ArrayList<Gift>giftsSelected = new ArrayList<>();
        GiftMercadoExpressAdapter adapter = new GiftMercadoExpressAdapter(gifts, requireActivity(), new GiftMercadoExpressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Gift gift, int position) {
                if(gift.isAdded()){
                    giftsSelected.add(gift);
                }else{
                    giftsSelected.remove(gift);
                }

            }
        });
        int spanCount = 1;
        int spacing = 3;
        GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
        rvGifts.addItemDecoration(itemDecoration);
        rvGifts.addItemDecoration(itemDecoration);
        rvGifts.setHasFixedSize(true);
        rvGifts.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvGifts.setAdapter(adapter);
    }


}