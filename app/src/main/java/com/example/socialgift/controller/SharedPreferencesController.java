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

    public void saveUserIdSharedPreferences(int userId, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id", userId);
        editor.apply();
    }

    public int loadUserIdSharedPreferences( Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("userId", MODE_PRIVATE);
        return sharedPref.getInt("id", 0);
    }

    public void deleteUserIdSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("id");
        editor.apply();
    }
}
