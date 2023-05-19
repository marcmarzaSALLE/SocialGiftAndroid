package com.example.socialgift.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DaoMercadoExpress {
    private final String urlMercadoExpress = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1";
    private final RequestQueue requestQueue;
    private final JSONObject jsonBody;
    private Context context;

    public DaoMercadoExpress(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        jsonBody = new JSONObject();
    }

    public void getGiftWishList(String url, Response.Listener<JSONObject> giftWishList, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, giftWishList, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
}
