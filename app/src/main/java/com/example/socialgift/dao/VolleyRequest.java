package com.example.socialgift.dao;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class VolleyRequest {
    private final String urlSocialGift = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String urlMercadoExpress = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1";
    private final String userParameter = "/users";
    private final String productParameter = "/products";

    private final String searchParameter = "/search?s=";
    private final String loginParameter = "/login";
    private final String friendParameter = "/friends";
    private final String requestParameter = "/requests";
    private final String whishlistParameter = "/wishlists";
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
        String createUrl = this.urlSocialGift + this.userParameter;
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
        String createUrl = this.urlSocialGift + this.userParameter + this.loginParameter;
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
        String createUrl = this.urlSocialGift + this.userParameter + this.searchParameter + email;
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

    public void getMyUser(int id, Response.Listener<JSONObject> findMyUser, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.userParameter + "/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, createUrl, null, findMyUser, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void editMyUser(String name, String last_name, String email, String password, String image, Response.Listener<JSONObject> editMyUser, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.userParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", last_name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, createUrl, jsonBody, editMyUser, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void addNewList(String name, String description, String date, Response.Listener<JSONObject> addNewList, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.whishlistParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("end_date", date);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonBody, addNewList, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void getWishListUser(int id, Response.Listener<JSONArray> getWishListUser, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.userParameter + "/" + id + this.whishlistParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, createUrl, null, getWishListUser, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void getGiftByUserMercadoExpress(int id, Response.Listener<JSONArray> getGiftByUserMercadoExpress, Response.ErrorListener errorListener) {
        String createUrl = this.urlMercadoExpress + this.productParameter + "/" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, createUrl, null, getGiftByUserMercadoExpress, errorListener);
        queue.add(jsonArrayRequest);
    }

    public void uploadFile(File image) {
        Log.wtf("IMAGE NAME", image.getName());
        Log.wtf("IMAGE PATH", image.getAbsolutePath());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("myFile", image.getName(),
                                RequestBody.create(new File(image.getAbsolutePath()),MediaType.parse("application/octet-stream")))
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://balandrau.salle.url.edu/i3/repositoryimages/uploadfile")
                        .method("POST", body)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    Log.wtf("RESPONSE", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getFriendRequest(Response.Listener<JSONArray> getFriendRequest, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.friendParameter + this.requestParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,createUrl,null,getFriendRequest,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();

                headers.put("Authorization","Bearer "+sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void acceptRequestFriend(int idFriendRequest, Response.Listener<JSONObject> acceptRequestFriend, Response.ErrorListener errorListener) {
        String url = this.urlSocialGift + this.friendParameter + "/" + idFriendRequest;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,url,null,acceptRequestFriend,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();

                headers.put("Authorization","Bearer "+sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void declineRequestFriend(int id, Response.Listener<JSONObject>declineRequestFriend, Response.ErrorListener errorListener){
        String url = this.urlSocialGift + this.friendParameter + "/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,url,null,declineRequestFriend,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();

                headers.put("Authorization","Bearer "+sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getMyFriends(Response.Listener<JSONArray> getMyFriends, Response.ErrorListener errorListener){
        String url = this.urlSocialGift + this.friendParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,getMyFriends,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();

                headers.put("Authorization","Bearer "+sharedPreferencesController.loadDateSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }
}

