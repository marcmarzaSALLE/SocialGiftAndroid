package com.example.socialgift.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListGiftsWishListAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Gift;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddListFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack, imgBtnAddGift, imgBtnAddList;
    private TextView txtListName, txtDeleteList, txtAddGift, txtNoGifts;
    private EditText edtTxtListName, edtTxtDescription, edtTxtDate;
    private Button btnSaveList;
    private DaoSocialGift daoSocialGift;
    private ArrayList<GiftWishList> giftWishLists;
    private RecyclerView rvGifts;
    private Wishlist wishlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        daoSocialGift = DaoSocialGift.getInstance(requireContext());
        syncronizeViewToolbar();
        syncronizeViewWidgets(view);
        showInfoToolbar();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        edtTxtDate.setOnClickListener(v -> {
            showDateDialog();
        });
        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    if (dateIsCorrect()) {
                        wishlist = new Wishlist();
                        daoSocialGift.addNewList(edtTxtListName.getText().toString(), edtTxtDescription.getText().toString(), edtTxtDate.getText().toString(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    wishlist.setId(response.getInt("id"));
                                    wishlist.setNameList(response.getString("name"));
                                    wishlist.setDescriptionList(response.getString("description"));
                                    wishlist.setEndDateList(response.getString("end_date"));
                                    AddListFragment.this.setWishlist(wishlist);
                                    Toast.makeText(requireActivity().getApplicationContext(), requireContext().getResources().getString(R.string.wishlist_created), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(requireActivity().getApplicationContext(), requireContext().getResources().getString(R.string.error_create_wishlist), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });

        imgBtnAddGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlist == null) {
                    Toast.makeText(requireActivity().getApplicationContext(), requireContext().getResources().getString(R.string.have_create_wishlist), Toast.LENGTH_SHORT).show();
                } else {
                    replace(new AddGiftFragment());
                }
            }
        });

        txtAddGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlist == null) {
                    Toast.makeText(requireActivity().getApplicationContext(), requireContext().getResources().getString(R.string.have_create_wishlist), Toast.LENGTH_SHORT).show();
                } else {
                    replace(new AddGiftFragment());
                }
            }
        });
        return view;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    private void syncronizeViewToolbar() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarAddList);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back_add_list);
        txtListName = (TextView) toolbar.findViewById(R.id.toolbar_title_add_list);
        txtDeleteList = (TextView) toolbar.findViewById(R.id.txtDeleteList);
    }

    private void syncronizeViewWidgets(View view) {
        edtTxtListName = (EditText) view.findViewById(R.id.edtNameList);
        edtTxtDescription = (EditText) view.findViewById(R.id.edtDescription);
        edtTxtDate = (EditText) view.findViewById(R.id.edtEndDate);
        btnSaveList = (Button) view.findViewById(R.id.btnSaveList);
        imgBtnAddGift = (ImageButton) view.findViewById(R.id.btnAddGift);
        txtAddGift = (TextView) view.findViewById(R.id.txtAddGift);
        rvGifts = (RecyclerView) view.findViewById(R.id.recyclerViewGiftsWishList);
        txtNoGifts = (TextView) view.findViewById(R.id.txtListNoGifts);

    }

    private void showDateDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                String format = "yyyy-MM-dd";
                selectedDate = new SimpleDateFormat(format).format(cal.getTime());
                edtTxtDate.setText(selectedDate);
            }
        });

        newFragment.show(getParentFragmentManager(), "datePicker");
    }

    private void showInfoToolbar() {
        txtListName.setText(getResources().getString(R.string.add_list));
        txtDeleteList.setVisibility(View.GONE);
        txtDeleteList.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    private boolean checkData() {
        if (edtTxtListName.getText().toString().isEmpty()) {
            edtTxtListName.setError(getResources().getText(R.string.name_list_required));
            return false;
        } else if (edtTxtDescription.getText().toString().isEmpty()) {
            edtTxtDescription.setError(getResources().getString(R.string.description_required));
            return false;

        } else if (edtTxtDate.getText().toString().isEmpty()) {
            edtTxtDate.setError(getResources().getString(R.string.end_date_required));
            return false;
        } else {
            return true;
        }
    }

    private boolean dateIsCorrect() {
        boolean correctDate = false;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dateTimeCal = Calendar.getInstance();
        String dateText = edtTxtDate.getText().toString();
        String timeText = "11:59:59";
        String finalDate = dateText + " " + timeText;
        try {
            dateTimeCal.setTime(Objects.requireNonNull(dateFormat.parse(finalDate)));
            if (dateTimeCal.compareTo(calendar) > 0) {
                correctDate = true;
            } else {
                edtTxtDate.setError(getResources().getString(R.string.errorDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return correctDate;
    }

    private void replace(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("wishlist", wishlist);

        AddGiftFragment addGiftFragment = (AddGiftFragment) fragment;
        addGiftFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameAddList, fragment);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncronizeViewToolbar();
        Bundle bundle = getArguments();
        if (bundle != null) {
            wishlist = (Wishlist) bundle.getSerializable("wishlist");
            daoSocialGift.getWishlistById(wishlist.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        giftWishLists = new ArrayList<>();
                        JSONArray gifts = response.getJSONArray("gifts");
                        for (int i = 0; i < gifts.length(); i++) {
                            JSONObject gift = gifts.getJSONObject(i);
                            GiftWishList gift1 = new GiftWishList();
                            gift1.setId(gift.getInt("id"));
                            gift1.setIdWishList(gift.getInt("wishlist_id"));
                            gift1.setPriority(gift.getInt("priority"));
                            gift1.setBooked(false);
                            gift1.setProductLink(gift.getString("product_url"));
                            giftWishLists.add(gift1);
                        }
                        wishlist.setGifts(giftWishLists);
                        setInfoWishlist(wishlist);
                        addDataRecyclerViewGift(wishlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Utiliza el itemList como desees
        }
    }

    private void setInfoWishlist(Wishlist wishlist) {
        edtTxtListName.setHint(wishlist.getNameList());
        edtTxtDescription.setHint(wishlist.getDescriptionList());
        edtTxtDate.setHint(wishlist.getEndDateList());
        btnSaveList.setVisibility(View.GONE);
    }

    private void addDataRecyclerViewGift(Wishlist wishlist) {
        if (wishlist.getGifts().isEmpty()) {
            txtNoGifts.setVisibility(View.VISIBLE);
            rvGifts.setVisibility(View.GONE);
        } else {
            txtNoGifts.setVisibility(View.GONE);
            rvGifts.setVisibility(View.VISIBLE);
            rvGifts.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            rvGifts.addItemDecoration(itemDecoration);
            rvGifts.addItemDecoration(itemDecoration);
            rvGifts.setHasFixedSize(true);
            rvGifts.setLayoutManager(new LinearLayoutManager(requireActivity()));
            rvGifts.setAdapter(new ListGiftsWishListAdapter(txtNoGifts, rvGifts, wishlist, wishlist.getGifts(), requireActivity(), new ListGiftsWishListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Wishlist wishlist, int position) {

                }
            }));
        }
    }
}