package com.example.socialgift.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.model.GiftWishList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DaoSocialGift {
    private final String urlSocialGift = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    private final String userParameter = "/users";
    private final String productParameter = "/products";

    private final String searchParameter = "/search?s=";
    private final String loginParameter = "/login";
    private final String friendParameter = "/friends";
    private final String requestParameter = "/requests";
    private final String whishlistParameter = "/wishlists";

    private final String giftsParameter = "/gifts";
    private final String bookParameter = "/book";
    private final String reservedParameter = "/reserved";
    private final RequestQueue queue;
    private final JSONObject jsonBody;
    private final Context context;
    private static DaoSocialGift daoSocialGift;
    Cache cache;
    Network network;

    private final SharedPreferencesController sharedPreferencesController;
    public static DaoSocialGift getInstance(Context context){
        if(daoSocialGift == null){
            synchronized (DaoSocialGift.class) {
                if (daoSocialGift == null) {
                    daoSocialGift = new DaoSocialGift(context);
                }
            }
        }
        return daoSocialGift;
    }

    public DaoSocialGift(Context context) {
        this.context = context;
        jsonBody = new JSONObject();
        sharedPreferencesController = new SharedPreferencesController();
        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
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

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
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

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
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

                    headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void editMyUser(String name, String last_name, String email, String image, Response.Listener<JSONObject> editMyUser, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.userParameter;
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", last_name);
            jsonBody.put("email", email);
            jsonBody.put("image", image);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, createUrl, jsonBody, editMyUser, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
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
                    headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
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

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void getFriendRequest(Response.Listener<JSONArray> getFriendRequest, Response.ErrorListener errorListener) {
        String createUrl = this.urlSocialGift + this.friendParameter + this.requestParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, createUrl, null, getFriendRequest, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void acceptRequestFriend(int idFriendRequest, Response.Listener<JSONObject> acceptRequestFriend, Response.ErrorListener errorListener) {
        String url = this.urlSocialGift + this.friendParameter + "/" + idFriendRequest;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, acceptRequestFriend, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void declineRequestFriend(int id, Response.Listener<JSONObject> declineRequestFriend, Response.ErrorListener errorListener) {
        String url = this.urlSocialGift + this.friendParameter + "/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, declineRequestFriend, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getMyFriends(Response.Listener<JSONArray> getMyFriends, Response.ErrorListener errorListener) {
        String url = this.urlSocialGift + this.friendParameter;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, getMyFriends, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void createReservationGift(GiftWishList giftWishList, Response.Listener<JSONObject> createReservationGift, Response.ErrorListener errorListener) {
        String createReservationUrl = this.urlSocialGift + this.giftsParameter + "/" + giftWishList.getId() + this.bookParameter;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createReservationUrl, null, createReservationGift, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void deleteReservationGift(int giftId, Response.Listener<JSONObject> deleteReservationGift, Response.ErrorListener errorListener) {
        String deleteReservationUrl = this.urlSocialGift + this.giftsParameter + "/" + giftId + this.bookParameter;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteReservationUrl, null, deleteReservationGift, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getMyGiftsBooked(Response.Listener<JSONArray> listener,Response.ErrorListener errorListener){
        String getMyGiftsBookedUrl = this.urlSocialGift +this.userParameter+"/"+sharedPreferencesController.loadUserIdSharedPreferences(context)+ this.giftsParameter + this.reservedParameter;
        Log.wtf("URL","URL"+getMyGiftsBookedUrl);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getMyGiftsBookedUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void deleteFriend(int friendId,Response.Listener<JSONObject> deleteFriendListener,Response.ErrorListener errorListener){
        String deleteFriendUrl = this.urlSocialGift +this.friendParameter+"/"+friendId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteFriendUrl, null, deleteFriendListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void deleteWishlist(int id, Response.Listener<JSONObject>deleteWishlistListener,Response.ErrorListener errorListener){
        String deleteWishlistUrl = this.urlSocialGift +this.whishlistParameter+"/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteWishlistUrl, null, deleteWishlistListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void deleteGiftWishlist(int id , Response.Listener<JSONObject> deleteGiftListener,Response.ErrorListener errorListener){
        String deleteGiftUrl = this.urlSocialGift +this.giftsParameter+"/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteGiftUrl, null, deleteGiftListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferencesController.loadTokenSharedPreferences(context));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }


    public void updateWishlist(String nameList, String descriptionList, String endDate, Response.Listener<JSONObject>updateListListener, Response.ErrorListener errorListener){

    }
}


