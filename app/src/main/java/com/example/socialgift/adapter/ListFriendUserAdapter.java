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
        DaoSocialGift daoSocialGift;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            imgFriend = itemView.findViewById(R.id.imgBtnFriend);
            txtNameFriend = itemView.findViewById(R.id.txtViewFriendName);
            btnUnfollowFriend = itemView.findViewById(R.id.btnUnfollowFriend);
            daoSocialGift = DaoSocialGift.getInstance(context);
        }
        public void bindData(Friend friend) {
            Glide.with(context).load(friend.getImage()).apply(RequestOptions.circleCropTransform()).into(imgFriend);
            txtNameFriend.setText(friend.getEmail());
            btnUnfollowFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoSocialGift.deleteFriend(friend.getId(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getResources().getString(R.string.friend_deleted,friend.getName() + " " + friend.getLast_name()))
                                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            friendsList.remove(friend);
                                            notifyItemRemoved(friendsList.indexOf(friend));
                                            notifyItemRangeChanged(friendsList.indexOf(friend),getItemCount());

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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

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
