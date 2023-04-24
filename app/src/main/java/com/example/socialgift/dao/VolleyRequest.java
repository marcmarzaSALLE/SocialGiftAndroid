package com.example.socialgift.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgift.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class VolleyRequest {
    private final String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String userParameter = "/users";
    private final RequestQueue queue;
    private final JSONObject jsonBody;

    public VolleyRequest(Context context) {
        queue = Volley.newRequestQueue(context);
        jsonBody = new JSONObject();
    }

    public void createUser() {
        String createUserUrl = this.url + this.userParameter;
        try {
            String name = "John";
            String last_name = "Doe";
            String email = "email";
            String password = "password";
            String image = "image";
            User user = new User(name, last_name, email, password);

            jsonBody.put("user", user);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUserUrl, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
                    queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
