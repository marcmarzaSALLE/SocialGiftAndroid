package com.example.socialgift.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GiftMercadoExpressAdapter;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Category;
import com.example.socialgift.model.Gift;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddGiftFragment extends Fragment {
    EditText edtSearchGift;
    Spinner spnCategory;
    DaoMercadoExpress daoMercadoExpress;
    ArrayList<Category> categories;
    Toolbar toolbar;
    TextView txtNoGifts;
    ImageButton imgBtnBack;
    TextView txtAddGiftToolbar, txtCreateGiftToolbar;
    RecyclerView rvGifts;
    ArrayList<Gift> gifts;
    GiftMercadoExpressAdapter adapter;
    ArrayList<Gift> giftsSelected;
    DaoSocialGift daoSocialGift;
    ArrayList<GiftWishList> giftWishLists;
    private int idWishlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gift, container, false);
        daoMercadoExpress = DaoMercadoExpress.getInstance(requireContext());
        syncronizedWidgets(view);
        syncronizedToolbar();
        setInfoToolbar();
        getCategory();
        daoSocialGift = DaoSocialGift.getInstance(requireActivity());
        giftsSelected = new ArrayList<>();

        imgBtnBack.setOnClickListener(v -> {
            Bundle wishlistBundle = getArguments();
            if (wishlistBundle != null) {
                Wishlist wishlist = (Wishlist) wishlistBundle.getSerializable("wishlist");
                if (wishlist != null) {
                    giftWishLists = new ArrayList<>();
                    for (Gift g : giftsSelected) {

                        daoSocialGift.createGift(g.getId(), wishlist.getId(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    GiftWishList giftWishList = new GiftWishList();
                                    giftWishList.setIdWishList(response.getInt("wishlist_id"));
                                    giftWishList.setId(response.getInt("id"));
                                    giftWishList.setProductLink(response.getString("product_url"));
                                    giftWishList.setPriority(response.getInt("priority"));
                                    giftWishList.setBooked(false);
                                    giftWishLists.add(giftWishList);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                    }

                    setInfoWishlist(wishlist);

                }
            }else{
                Toast.makeText(requireContext(), "No se pudo agregar el regalo", Toast.LENGTH_SHORT).show();
            }
        });

        edittextAction();
        spinnerAction();

        return view;
    }

    public void setInfoWishlist(Wishlist wishlist){

        Bundle bundle = new Bundle();
        bundle.putSerializable("wishlist", wishlist);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        AddListFragment addListFragment = new AddListFragment();
        addListFragment.setArguments(bundle);
        transaction.replace(R.id.frameAddList, addListFragment);
        transaction.commit();
    }
    private void syncronizedToolbar() {
        toolbar = requireActivity().findViewById(R.id.toolbarAddList);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back_add_list);
        txtCreateGiftToolbar = (TextView) toolbar.findViewById(R.id.txtDeleteList);
        txtAddGiftToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title_add_list);
    }

    private void spinnerAction() {
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Category categorySelected = (Category) parent.getItemAtPosition(position);
                    ArrayList<Gift> aux = new ArrayList<>();
                    for (Gift g : gifts) {
                        if (g.getCategories() != null) {
                            for (int i = 0; i < g.getCategories().size(); i++) {
                                for (Category c : g.getCategories()) {
                                    if (c.getId() == categorySelected.getId()) {
                                        aux.add(g);
                                    }
                                }
                            }
                        }
                        adapter.setGifts(aux);
                    }
                } else {
                    showAllGifts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones adicionales cuando no se selecciona nada
            }
        });
    }


    private void edittextAction() {
        edtSearchGift.setSingleLine(true);
        edtSearchGift.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_NULL) {
                    if (edtSearchGift.getText().toString().isEmpty()) {
                        showAllGifts();
                    } else {
                        showGiftsBySearch(edtSearchGift.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        edtSearchGift.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                showAllGifts();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //showGiftsBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                showGiftsBySearch(s.toString());
            }
        });
    }

    private void setInfoToolbar() {
        txtAddGiftToolbar.setText(requireContext().getResources().getString(R.string.add_gift));
        txtCreateGiftToolbar.setText(requireContext().getResources().getString(R.string.create_gift));

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_green_24, null);
        txtCreateGiftToolbar.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        txtCreateGiftToolbar.setVisibility(View.VISIBLE);

    }

    private void syncronizedWidgets(View view) {
        edtSearchGift = view.findViewById(R.id.edtSearchGift);
        spnCategory = view.findViewById(R.id.spnCategory);
        rvGifts = view.findViewById(R.id.rvGifts);
        txtNoGifts = view.findViewById(R.id.txtNoGiftMercadoExpress);
    }

    private void setCategorySpinner() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(requireContext(), R.layout.spinner_layout, categories) {
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
                categories = new ArrayList<>();
                Category defaultCategory = new Category(0, requireContext().getResources().getString(R.string.all_categories));
                categories.add(defaultCategory);
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

    private void showAllGifts() {
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
                        JSONArray idCategories = jsonObject.getJSONArray("categoryIds");
                        ArrayList<Category> categories = new ArrayList<>();
                        for (int j = 0; j < idCategories.length(); j++) {
                            Log.d("TAG", "CATEGORIA DEL PRODUCTO: " + idCategories.getInt(j));
                            Category category = new Category(idCategories.getInt(j), "");
                            categories.add(category);
                        }
                        gift.setIdCategory(categories);

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


    private void showGiftsBySearch(String search) {
        ArrayList<Gift> giftsSearch = new ArrayList<>();
        daoMercadoExpress.getGiftsBySearch(search, new Response.Listener<JSONArray>() {
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
                        JSONArray idCategories = jsonObject.getJSONArray("categoryIds");
                        ArrayList<Category> categories = new ArrayList<>();
                        for (int j = 0; j < idCategories.length(); j++) {
                            Log.d("TAG", "CATEGORIA DEL PRODUCTO: " + idCategories.getInt(j));
                            Category category = new Category(idCategories.getInt(j), "");
                            categories.add(category);
                        }
                        gift.setIdCategory(categories);

                        gifts.add(gift);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.setGifts(gifts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.error_load_gifts), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterGifts(ArrayList<Gift> gifts) {
        rvGifts.setAdapter(null);
        adapter = new GiftMercadoExpressAdapter(txtNoGifts, rvGifts, gifts, requireActivity(), new GiftMercadoExpressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Gift gift, int position) {
                if (gift.isAdded()) {
                    giftsSelected.add(gift);
                } else {
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
        rvGifts.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        rvGifts.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        rvGifts.setAdapter(adapter);
    }


}