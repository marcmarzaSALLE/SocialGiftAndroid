package com.example.socialgift.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgift.controller.UserData;
import com.example.socialgift.model.Gift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DaoMercadoExpress {
    private final String urlMercadoExpress = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1";
    private final String categoryParameter = "/categories";
    private final String giftParameter = "/products";
    private final String searchParameter= "/search?s=";
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

    public void getCategories(Response.Listener<JSONArray> categories, Response.ErrorListener errorListener) {
        String categoriesUrl = this.urlMercadoExpress + this.categoryParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, categoriesUrl, null, categories, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void getAllGift(Response.Listener<JSONArray>gifts,Response.ErrorListener errorListener){
        String allGiftsUrl = this.urlMercadoExpress + "/products";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allGiftsUrl, null, gifts, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void getGiftsByCategory(String category, Response.Listener<JSONArray>gifts,Response.ErrorListener errorListener){
        String allGiftsUrl = this.urlMercadoExpress + "/products?category=" + category;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allGiftsUrl, null, gifts, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void getGiftsBySearch(String search, Response.Listener<JSONArray>gifts,Response.ErrorListener errorListener){
        String allGiftsUrl = this.urlMercadoExpress + giftParameter + searchParameter + search;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allGiftsUrl, null, gifts, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void createGiftMercadoExpress(Gift gift, Response.Listener<JSONObject>createGiftListener,Response.ErrorListener errorListener){
        String urlCreateProduct = this.urlMercadoExpress + this.giftParameter;
        try {
            jsonBody.put("name", gift.getName());
            jsonBody.put("description",gift.getDescription());
            jsonBody.put("link",gift.getLink());
            jsonBody.put("photo",gift.getUrlImage());
            jsonBody.put("price",gift.getPrice());
            jsonBody.put("categoryIds",gift.getIdCategory());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlCreateProduct,jsonBody,createGiftListener,errorListener);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
