package com.example.socialgift.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.socialgift.dao.VolleyRequest;
import com.example.socialgift.model.Friend;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListFriendRequestAdapter extends RecyclerView.Adapter<ListFriendRequestAdapter.ViewHolder>{
    private ArrayList<Friend>friendsRequestList;
    private LayoutInflater inflater;
    private ListFriendRequestAdapter.OnItemClickListener listener;
    private Context context;
    public interface OnItemClickListener{
        void onItemClick(Friend friend, int position);
    }

    public ListFriendRequestAdapter(ArrayList<Friend> friendsRequestList,Context context ,ListFriendRequestAdapter.OnItemClickListener listener) {
        this.friendsRequestList = friendsRequestList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return friendsRequestList.size();
    }

    @Override
    public ListFriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_friend_notification,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFriendRequestAdapter.ViewHolder holder, int position) {
        holder.bindData(friendsRequestList.get(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriendRequest;
        TextView txtEmailFriendRequest;
        ImageButton btnAcceptFriendRequest, btnDeclineFriendRequest;
        VolleyRequest volleyRequest;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            volleyRequest = new VolleyRequest(context);
            imgFriendRequest = itemView.findViewById(R.id.imgBtnFriendRequest);
            txtEmailFriendRequest = itemView.findViewById(R.id.txtViewFriendName);
            btnAcceptFriendRequest = itemView.findViewById(R.id.imgBtnAccept);
            btnDeclineFriendRequest = itemView.findViewById(R.id.imgBtnDecline);
        }
        public void bindData(final Friend friend){
            Glide.with(context).load(friend.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriendRequest);
            txtEmailFriendRequest.setText(friend.getEmail());
            btnAcceptFriendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volleyRequest.acceptRequestFriend(friend.getId(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(context, "You are now friend with "+friend.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            btnDeclineFriendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volleyRequest.declineRequestFriend(friend.getId(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemChanged(getAdapterPosition());
                            Toast.makeText(context, "You declined "+friend.getEmail()+"'s friend request", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
}
