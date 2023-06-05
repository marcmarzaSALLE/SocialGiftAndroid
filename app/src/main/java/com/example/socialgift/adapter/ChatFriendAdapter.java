package com.example.socialgift.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.socialgift.activities.ChatFriendActivity;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFriendAdapter extends RecyclerView.Adapter<ChatFriendAdapter.ViewHolder>{
    private ArrayList<Friend> friendsList;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private TextView txtNoFriends;
    private ChatFriendAdapter.OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(Friend friend, int position);
    }

    public ChatFriendAdapter(TextView txtNoFriends,RecyclerView recyclerView,ArrayList<Friend> friendsList, Context context, ChatFriendAdapter.OnItemClickListener listener) {
        this.friendsList = friendsList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.txtNoFriends = txtNoFriends;
    }

    public void setFriendsList(String name) {
        ArrayList<Friend> friends = new ArrayList<>();
        for (Friend friend: friendsList) {
            if(friend.getName().toLowerCase().contains(name.toLowerCase()) || friend.getLast_name().toLowerCase().contains(name.toLowerCase()) || friend.getEmail().toLowerCase().contains(name.toLowerCase())){
                friends.add(friend);
            }
        }
        if(friends.isEmpty()){
            txtNoFriends.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            txtNoFriends.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            friendsList = friends;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @Override
    public ChatFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_friends_chat,null);
        return new ChatFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatFriendAdapter.ViewHolder holder, int position) {
        holder.bindData(friendsList.get(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriend;
        TextView txtNameFriend, txtEmailFriend;
        Button btnChatFriend;
        DaoSocialGift daoSocialGift;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            imgFriend = itemView.findViewById(R.id.imgBtnFriend);
            txtNameFriend = itemView.findViewById(R.id.txtViewFriendName);
            btnChatFriend = itemView.findViewById(R.id.btnChatFriend);
            txtEmailFriend = itemView.findViewById(R.id.txtViewFriendEmail);
            daoSocialGift = DaoSocialGift.getInstance(context);
        }
        public void bindData(Friend friend) {
            Glide.with(context).load(friend.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriend);
            txtNameFriend.setText(context.getResources().getString(R.string.username,friend.getName() ,friend.getLast_name()));
            txtEmailFriend.setText(friend.getEmail());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(friend,friend.getId());
                }
            });
            imgFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateImage(imgFriend);
                }
            });

            btnChatFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatFriendActivity.class);
                    intent.putExtra("friend",friend);
                    context.startActivity(intent);
                }
            });
        }
        private void animateImage(ImageView imageView) {
            imageView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .setDuration(200)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imageView.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(200)
                                    .start();
                        }
                    })
                    .start();
        }
    }
}
