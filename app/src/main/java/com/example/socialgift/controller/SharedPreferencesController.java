package com.example.socialgift.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
        //Integer.parseInt(sharedPref.getString("id", "0"));
    }

    public void deleteUserIdSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("id");
        editor.apply();
    }

    public void savePasswordSharedPreferences(String password, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("passwordHash", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public String loadPasswordSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("passwordHash", MODE_PRIVATE);
        return sharedPref.getString("password", null);
    }

        public String passwordEncrypt(String password) {
            try {
                // Obtener instancia de MessageDigest para SHA-256
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // Aplicar hash a la contraseña
                byte[] hashedPassword = md.digest(password.getBytes());

                // Convertir el hash a una cadena hexadecimal
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedPassword) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
}
