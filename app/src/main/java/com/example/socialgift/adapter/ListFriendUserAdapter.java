package com.example.socialgift.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialgift.R;
import com.example.socialgift.model.Friend;

import java.util.ArrayList;

public class ListFriendUserAdapter extends RecyclerView.Adapter<ListFriendUserAdapter.ViewHolder> {
    private ArrayList<Friend> friendsList;
    private LayoutInflater inflater;
    private ListFriendUserAdapter.OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(Friend friend, int position);
    }

    public ListFriendUserAdapter(ArrayList<Friend> friendsList, Context context, ListFriendUserAdapter.OnItemClickListener listener) {
        this.friendsList = friendsList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @Override
    public ListFriendUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_friend_user,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFriendUserAdapter.ViewHolder holder, int position) {
        holder.bindData(friendsList.get(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriend;
        TextView txtNameFriend;
        Button btnUnfollowFriend;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            imgFriend = itemView.findViewById(R.id.imgBtnFriend);
            txtNameFriend = itemView.findViewById(R.id.txtViewFriendName);
            btnUnfollowFriend = itemView.findViewById(R.id.btnUnfollowFriend);
        }
        public void bindData(Friend friend) {
            Glide.with(context).load(friend.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriend);
            txtNameFriend.setText(friend.getEmail());
            btnUnfollowFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.wtf("Unfollow", "Unfollow");
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(friend,friend.getId());
                }
            });
        }
    }
}
