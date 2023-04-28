package com.example.socialgift.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyRequest {
    private final String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String userParameter = "/users";
    private final String loginParameter = "/login";
    private final RequestQueue queue;
    private final JSONObject jsonBody;
    private final Context context;

    public VolleyRequest(Context context) {
        this.context=context;
        queue = Volley.newRequestQueue(context);
        jsonBody = new JSONObject();
    }


    public void registerUser(String name, String last_name,String email,String password,String image, Response.Listener<JSONObject> registerListener, Response.ErrorListener errorListener){
        String createUrl = this.url + this.userParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", last_name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonBody, registerListener,errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    public void createUser() {
        String createUserUrl = this.url + this.userParameter;
        try {
            jsonBody.put("email", "johnsalchicon@mail.es");
            jsonBody.put("password", "password");


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUserUrl, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    Log.wtf("VolleyRequest", "onResponse: ");
                    Log.wtf("VolleyRequest", "onResponse: " + jsonBody.toString());
                    System.out.println("" + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.wtf("VolleyRequest", "onResponse: " + jsonBody.toString());

                    Log.wtf("VolleyRequest", "onErrorResponse: " + error.getMessage());
                }
            });
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonBody, loginActivity,errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
