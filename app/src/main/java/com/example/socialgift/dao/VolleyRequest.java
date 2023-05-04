package com.example.socialgift.dao;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgift.controller.SharedPreferencesController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    private final String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String userParameter = "/users";

    private final String searchParameter = "/search?s=";
    private final String loginParameter = "/login";
    private final RequestQueue queue;
    private final JSONObject jsonBody;
    private final Context context;

    private final SharedPreferencesController sharedPreferencesController;

    public VolleyRequest(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        jsonBody = new JSONObject();
        sharedPreferencesController = new SharedPreferencesController();
    }


    public void registerUser(String name, String last_name, String email, String password, String image, Response.Listener<JSONObject> registerListener, Response.ErrorListener errorListener) {
        String createUrl = this.url + this.userParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", last_name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonBody, registerListener, errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void loginUser(String email, String password, Response.Listener<JSONObject> loginActivity, Response.ErrorListener errorListener) {
        String createUrl = this.url + this.userParameter + this.loginParameter;
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonBody, loginActivity, errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMyUserId(String email, Response.Listener<JSONArray> searchUser, Response.ErrorListener errorListener) {
        String createUrl = this.url + this.userParameter + this.searchParameter + email;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, createUrl, null, searchUser, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void getMyUser(int id,Response.Listener<JSONObject> findMyUser,Response.ErrorListener errorListener){
        String createUrl = this.url + this.userParameter + "/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, createUrl,null,findMyUser,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();

                headers.put("Authorization","Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void editMyUser(String name,String last_name,String email,String password,String image,Response.Listener<JSONObject> editMyUser,Response.ErrorListener errorListener){
        String createUrl = this.url + this.userParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", last_name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, createUrl, jsonBody, editMyUser, errorListener){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();

                    headers.put("Authorization","Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
