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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListGiftsWishListAdapter extends RecyclerView.Adapter<ListGiftsWishListAdapter.ViewHolder> {
    private ArrayList<GiftWishList> giftWishLists;
    private Wishlist wishlist;
    private LayoutInflater inflater;
    private ListGiftsWishListAdapter.OnItemClickListener listener;
    private Context context;
    private DaoMercadoExpress daoMercadoExpress;
    private RecyclerView recyclerView;
    private TextView txtNoGifts;

    public interface OnItemClickListener {
        void onItemClick(Wishlist wishlist, int position);
    }

    public ListGiftsWishListAdapter(TextView txtNoGifts, RecyclerView recyclerView, Wishlist wishlist, ArrayList<GiftWishList> giftWishLists, Context context, ListGiftsWishListAdapter.OnItemClickListener listener) {
        this.wishlist = wishlist;
        this.giftWishLists = giftWishLists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.daoMercadoExpress = new DaoMercadoExpress(context);
        this.recyclerView = recyclerView;
        this.txtNoGifts = txtNoGifts;
    }

    @Override
    public int getItemCount() {
        return giftWishLists.size();
    }

    @Override
    public ListGiftsWishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_gift_wishlist, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListGiftsWishListAdapter.ViewHolder holder, int position) {
        holder.bindData(giftWishLists.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGift;
        TextView txtNameGift, txtGiftId, txtWishlistId, txtGiftBooked;
        Button btnDeleteGift;
        DaoSocialGift daoSocialGift;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGift = itemView.findViewById(R.id.imgGift);
            txtNameGift = itemView.findViewById(R.id.txtGiftName);
            txtGiftId = itemView.findViewById(R.id.txtGiftId);
            txtWishlistId = itemView.findViewById(R.id.txtWishlistName);
            txtGiftBooked = itemView.findViewById(R.id.txtGiftBooked);
            btnDeleteGift = itemView.findViewById(R.id.btnDeleteGift);
            daoSocialGift = new DaoSocialGift(context);
        }

        public void bindData(GiftWishList giftWishList) {
            daoMercadoExpress.getGiftWishList(giftWishList.getProductLink(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Glide.with(context).load(response.getString("photo")).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_giftcard_green_24, null)).into(imgGift);
                        txtGiftId.setText(context.getResources().getString(R.string.gift_id, giftWishList.getId()));
                        txtWishlistId.setText(context.getResources().getString(R.string.wishlist_id, wishlist.getId()));
                        txtNameGift.setText(context.getResources().getString(R.string.gift_name, response.getString("name")));
                        txtGiftBooked.setText(context.getResources().getString(R.string.booked_gift_wishlist, String.valueOf(giftWishList.isBooked())));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            btnDeleteGift.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.delete_gift_confirmation, txtNameGift.getText().toString()))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                daoSocialGift.deleteGiftWishlist(giftWishList.getId(), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(context, context.getResources().getString(R.string.gift_deleted_list), Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(giftWishLists.indexOf(giftWishList));
                                        notifyItemRangeChanged(giftWishLists.indexOf(giftWishList), getItemCount());
                                        giftWishLists.remove(giftWishList);
                                        if (giftWishLists.isEmpty()) {
                                            recyclerView.setVisibility(View.GONE);
                                            txtNoGifts.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                });
                            }
                        }).setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Acciones al hacer clic en el botón "Cancelar"
                            }
                        });
                // Crear el diálogo y mostrarlo
                builder.create().show();

            });
        }
    }
}
