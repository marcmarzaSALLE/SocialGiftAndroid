package com.example.socialgift.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgift.controller.UserData;

import org.json.JSONObject;

public class DaoMercadoExpress {
    private final String urlMercadoExpress = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1";
    private final RequestQueue requestQueue;
    private final JSONObject jsonBody;
    private Context context;
    private static  DaoMercadoExpress daoMercadoExpress;
    public static DaoMercadoExpress getInstance(Context context) {
        if (daoMercadoExpress == null) {
            synchronized (DaoMercadoExpress.class) {
                if (daoMercadoExpress == null) {
                    daoMercadoExpress = new DaoMercadoExpress(context);
                }
            }
        }
        return daoMercadoExpress;
    }
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
