package com.example.socialgift.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Friend;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListFriendUserAdapter extends RecyclerView.Adapter<ListFriendUserAdapter.ViewHolder> {
    private ArrayList<Friend> friendsList;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private TextView txtNoFriends;
    private ListFriendUserAdapter.OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(Friend friend, int position);
    }

    public ListFriendUserAdapter(TextView txtNoFriends,RecyclerView recyclerView,ArrayList<Friend> friendsList, Context context, ListFriendUserAdapter.OnItemClickListener listener) {
        this.friendsList = friendsList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.txtNoFriends = txtNoFriends;
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
        TextView txtNameFriend, txtEmailFriend;
        Button btnUnfollowFriend;
        DaoSocialGift daoSocialGift;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            imgFriend = itemView.findViewById(R.id.imgBtnFriend);
            txtNameFriend = itemView.findViewById(R.id.txtViewFriendName);
            btnUnfollowFriend = itemView.findViewById(R.id.btnUnfollowFriend);
            txtEmailFriend = itemView.findViewById(R.id.txtViewFriendEmail);
            daoSocialGift = DaoSocialGift.getInstance(context);
        }
        public void bindData(Friend friend) {
            Glide.with(context).load(friend.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriend);
            txtNameFriend.setText(context.getResources().getString(R.string.username,friend.getName() ,friend.getLast_name()));
            txtEmailFriend.setText(friend.getEmail());
            btnUnfollowFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.friend_deleted,friend.getName() + " " + friend.getLast_name()))
                            .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    daoSocialGift.deleteFriend(friend.getId(), new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            notifyItemRemoved(friendsList.indexOf(friend));
                                            notifyItemRangeChanged(friendsList.indexOf(friend),getItemCount());
                                            friendsList.remove(friend);
                                            if(friendsList.isEmpty()){
                                                recyclerView.setVisibility(View.GONE);
                                                txtNoFriends.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Acciones al hacer clic en el botón "Cancelar"
                                }
                            });
                    // Crear el diálogo y mostrarlo
                    builder.create().show();

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
