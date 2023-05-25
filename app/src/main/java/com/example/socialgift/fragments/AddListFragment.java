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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoSocialGift;

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
    private Button btnSaveList;
    private DaoSocialGift daoSocialGift;

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
                        daoSocialGift.addNewList(edtTxtListName.getText().toString(), edtTxtDescription.getText().toString(), edtTxtDate.getText().toString(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                requireActivity().finish();
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

        imgBtnAddGift.setOnClickListener(v-> {
                replace(new AddGiftFragment());

        });
        txtAddGift.setOnClickListener(v -> {
            replace(new AddGiftFragment());
        });
        return view;
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
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameAddList, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncronizeViewToolbar();
    }
}