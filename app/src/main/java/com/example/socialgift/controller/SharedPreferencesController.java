package com.example.socialgift.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesController {

    public SharedPreferencesController() {
    }

    public String loadDateSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        return sharedPref.getString("token", null);
    }

    public void saveDateSharedPreferences(String token, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public void deleteDateSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.apply();
    }
}
