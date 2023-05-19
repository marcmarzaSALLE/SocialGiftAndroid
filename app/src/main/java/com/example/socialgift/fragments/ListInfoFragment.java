package com.example.socialgift.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.adapter.GridSpacingDecoration;
import com.example.socialgift.adapter.ListGiftsWishListAdapter;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Wishlist;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ListInfoFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack, imgBtnAddGift, imgBtnAddList;
    private TextView txtListName, txtDeleteList, txtAddGift,txtNoGifts;
    private EditText edtTxtListName, edtTxtDescription, edtTxtDate;
    private Button btnSaveList, btnAddGift;
    private RecyclerView recyclerViewGifts;
    private Wishlist wishlist;
    private DaoSocialGift daoSocialGift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        daoSocialGift = new DaoSocialGift(requireActivity().getApplicationContext());
        syncronizeViewToolbar();
        syncronizeViewWidgets(view);
        showInfoWidgets();
        addDataRecyclerViewGift();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        edtTxtDate.setOnClickListener(v ->{
            showDateDialog();
        });
        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("AddListFragment", "Save button clicked");
                if(checkData()){
                    if(dateIsCorrect()){
                        daoSocialGift.addNewList(edtTxtListName.getText().toString(), edtTxtDescription.getText().toString(), edtTxtDate.getText().toString(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(requireActivity().getApplicationContext(), "Lista creada correctamente", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.frame, new ListFragment()).commit();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(requireActivity().getApplicationContext(), "Error al crear la lista", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        return view;
    }

    private void syncronizeViewToolbar() {
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbarAddList);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtListName = (TextView) toolbar.findViewById(R.id.toolbar_title_add_list);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back_add_list);
        txtDeleteList = (TextView) toolbar.findViewById(R.id.txtDeleteList);
    }

    private void syncronizeViewWidgets(View view){
        edtTxtListName = (EditText) view.findViewById(R.id.edtNameList);
        edtTxtDescription = (EditText) view.findViewById(R.id.edtDescription);
        edtTxtDate = (EditText) view.findViewById(R.id.edtEndDate);
        btnSaveList = (Button) view.findViewById(R.id.btnSaveList);
        recyclerViewGifts = (RecyclerView) view.findViewById(R.id.recyclerViewGiftsWishList);
        txtNoGifts = (TextView) view.findViewById(R.id.txtListNoGifts);
    }

    private void showInfoWidgets(){
        Intent intent = requireActivity().getIntent();
        wishlist = (Wishlist) intent.getSerializableExtra("wishlist");
        Log.wtf("AddListFragment", "Wishlist: " + wishlist.toString());
        Log.wtf("AddListFragment", "Wishlist: ");
        txtListName.setText(wishlist.getNameList());
        edtTxtListName.setHint(wishlist.getNameList());
        edtTxtDescription.setHint(wishlist.getDescriptionList());
        edtTxtDate.setHint(wishlist.getEndDateList());

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


    private boolean checkData(){
        if(edtTxtListName.getText().toString().isEmpty()){
            edtTxtListName.setError(getResources().getText(R.string.name_list_required));
            return false;
        }else if(edtTxtDescription.getText().toString().isEmpty()){
            edtTxtDescription.setError(getResources().getString(R.string.description_required));
            return false;

        } else if (edtTxtDate.getText().toString().isEmpty()) {
            edtTxtDate.setError(getResources().getString(R.string.end_date_required));
            return false;
        } else {
            return true;
        }
    }

    private boolean dateIsCorrect(){
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

    private void addDataRecyclerViewGift(){
        if(wishlist.getGifts().isEmpty()){
            txtNoGifts.setVisibility(View.VISIBLE);
            recyclerViewGifts.setVisibility(View.GONE);
        }else{
            txtNoGifts.setVisibility(View.GONE);
            recyclerViewGifts.setVisibility(View.VISIBLE);
            recyclerViewGifts.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

            int spanCount = 1;
            int spacing = 3;
            GridSpacingDecoration itemDecoration = new GridSpacingDecoration(spanCount, spacing);
            recyclerViewGifts.addItemDecoration(itemDecoration);
            recyclerViewGifts.addItemDecoration(itemDecoration);
            recyclerViewGifts.setHasFixedSize(true);
            recyclerViewGifts.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewGifts.setAdapter(new ListGiftsWishListAdapter(wishlist, wishlist.getGifts(), requireActivity().getApplicationContext(), new ListGiftsWishListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Wishlist wishlist, int position) {

                }
            }));
        }
    }
}