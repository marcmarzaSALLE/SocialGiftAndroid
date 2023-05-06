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
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.dao.VolleyRequest;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AddListFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton imgBtnBack, imgBtnAddGift, imgBtnAddList;
    private TextView txtListName, txtDeleteList, txtAddGift;
    private EditText edtTxtListName, edtTxtDescription, edtTxtDate;
    private Button btnSaveList, btnAddGift;
    private VolleyRequest volleyRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        volleyRequest = new VolleyRequest(requireActivity().getApplicationContext());
        syncronizeViewToolbar();
        syncronizeViewWidgets(view);
        showInfoToolbar();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("AddListFragment", "Back button clicked");
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new ListFragment()).commit();
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
                        volleyRequest.addNewList(edtTxtListName.getText().toString(), edtTxtDescription.getText().toString(), edtTxtDate.getText().toString(), new Response.Listener<JSONObject>() {
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
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        txtListName = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgBtnBack = (ImageButton) toolbar.findViewById(R.id.toolbar_button_back);
        txtDeleteList = (TextView) toolbar.findViewById(R.id.txtAddList);
        imgBtnAddList = (ImageButton) toolbar.findViewById(R.id.toolbar_main_button_logout);
    }

    private void syncronizeViewWidgets(View view){
        edtTxtListName = (EditText) view.findViewById(R.id.edtNameList);
        edtTxtDescription = (EditText) view.findViewById(R.id.edtDescription);
        edtTxtDate = (EditText) view.findViewById(R.id.edtEndDate);
        btnSaveList = (Button) view.findViewById(R.id.btnSaveList);
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
        imgBtnAddList.setVisibility(View.INVISIBLE);
        imgBtnBack.setVisibility(View.VISIBLE);
        txtDeleteList.setText(getResources().getText(R.string.delete_list));
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
}