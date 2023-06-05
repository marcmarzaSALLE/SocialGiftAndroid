package com.example.socialgift.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;
import com.example.socialgift.model.Gift;
import com.example.socialgift.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> {
    private ArrayList<User> userArrayList;
    private LayoutInflater inflater;
    private AllUsersAdapter.OnItemClickListener listener;
    private Context context;
    private SharedPreferencesController sharedPreferencesController;


    public interface OnItemClickListener {
        void onItemClick(Friend friend, int position);
    }

    public AllUsersAdapter(ArrayList<User> userArrayList, Context context, AllUsersAdapter.OnItemClickListener listener) {
        this.userArrayList = userArrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        sharedPreferencesController = new SharedPreferencesController();
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public AllUsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_all_users, null);
        return new AllUsersAdapter.ViewHolder(view);
    }

    public void setAllUsers(ArrayList<User> users) {
        this.userArrayList = users;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersAdapter.ViewHolder holder, int position) {
        holder.bindData(userArrayList.get(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriend;
        TextView txtNameFriend, txtEmailFriend;
        Button btnFollowFriend;
        DaoSocialGift daoSocialGift;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            imgFriend = itemView.findViewById(R.id.imageViewUser);
            txtNameFriend = itemView.findViewById(R.id.txtViewUserName);
            btnFollowFriend = itemView.findViewById(R.id.btnFollowFriend);
            txtEmailFriend = itemView.findViewById(R.id.txtViewUserEmail);
            daoSocialGift = DaoSocialGift.getInstance(context);
        }


        public void bindData(User user) {
            Glide.with(context).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriend);
            txtNameFriend.setText(context.getResources().getString(R.string.username, user.getName(), user.getLast_name()));
            txtEmailFriend.setText(user.getEmail());
            if (sharedPreferencesController.loadUserIdSharedPreferences(context) == user.getId()) {
                btnFollowFriend.setVisibility(View.GONE);
            }
            checkFriendUser(user.getEmail());

            btnFollowFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoSocialGift.sendRequestUser(user.getId(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            btnFollowFriend.setVisibility(View.GONE);
                            Toast.makeText(context, context.getResources().getString(R.string.send_request), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btnFollowFriend.setVisibility(View.GONE);
                            Toast.makeText(context, context.getResources().getString(R.string.error_send_request), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        private void checkFriendUser(String email) {
            daoSocialGift.getMyFriends(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            if (response.getJSONObject(i).getString("email").equals(email)) {
                                btnFollowFriend.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }
}
