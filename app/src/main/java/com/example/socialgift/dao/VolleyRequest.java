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
import com.example.socialgift.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class VolleyRequest {
    private final String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String userParameter = "/users";
    private final RequestQueue queue;
    private final JSONObject jsonBody;
    private final Context context;

    public VolleyRequest(Context context) {
        this.context=context;
        queue = Volley.newRequestQueue(context);
        jsonBody = new JSONObject();
    }

    public void createUser() {
        String createUserUrl = this.url + this.userParameter;
        try {
            jsonBody.put("name", "string");
            jsonBody.put("last_name", "string");
            jsonBody.put("email", "johnsalchicon@mail.es");
            jsonBody.put("password", "password");
            jsonBody.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUserUrl, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show();
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
}
