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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
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

public class GiftFriendAdapter extends RecyclerView.Adapter<GiftFriendAdapter.ViewHolder> {
    private ArrayList<GiftWishList> giftWishLists;
    private Wishlist wishlist;
    private LayoutInflater inflater;
    private ListGiftsWishListAdapter.OnItemClickListener listener;
    private Context context;
    private DaoSocialGift daoSocialGift;
    RequestQueue requestQueue;

    public interface OnItemClickListener {
        void onItemClick(Wishlist wishlist, int position);
    }

    public GiftFriendAdapter(Wishlist wishlist, ArrayList<GiftWishList> giftWishLists, Context context, ListGiftsWishListAdapter.OnItemClickListener listener) {
        this.wishlist = wishlist;
        this.giftWishLists = giftWishLists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        daoSocialGift = DaoSocialGift.getInstance(context);
    }

    @Override
    public int getItemCount() {
        return giftWishLists.size();
    }

    @Override
    public GiftFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_gift_wishlist, null);
        return new GiftFriendAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GiftFriendAdapter.ViewHolder holder, int position) {
        holder.bindData(giftWishLists.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGift;
        TextView txtNameGift, txtGiftId, txtWishlistId, txtGiftBooked;
        Button btnReserveGift;
        DaoMercadoExpress daoMercadoExpress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGift = itemView.findViewById(R.id.imgGift);
            txtNameGift = itemView.findViewById(R.id.txtGiftName);
            txtGiftId = itemView.findViewById(R.id.txtGiftId);
            txtWishlistId = itemView.findViewById(R.id.txtWishlistName);
            txtGiftBooked = itemView.findViewById(R.id.txtGiftBooked);
            btnReserveGift = itemView.findViewById(R.id.btnDeleteGift);
            daoMercadoExpress = DaoMercadoExpress.getInstance(context);
        }

        public void bindData(GiftWishList giftWishList) {
            daoMercadoExpress.getGiftWishList(giftWishList.getProductLink(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("GiftFriendAdapter", "REGALO SOCIALGIFT: " + giftWishList.getProductLink());
                    Log.wtf("GiftFriendAdapter", "REGALO MERCADO EXPRESS: " + response.toString());
                    try {
                        Glide.with(context).load(response.getString("photo")).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_giftcard_green_24, null)).into(imgGift);
                        txtGiftId.setText(context.getResources().getString(R.string.gift_id, giftWishList.getId()));
                        txtWishlistId.setText(context.getResources().getString(R.string.wishlist_id, wishlist.getId()));
                        txtNameGift.setText(context.getResources().getString(R.string.gift_name, response.getString("name")));
                        if (giftWishList.isBooked()) {
                            btnReserveGift.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_booked_gift_button, null));
                            btnReserveGift.setText(context.getResources().getString(R.string.gift_booked));
                        } else {
                            btnReserveGift.setText(context.getResources().getString(R.string.reserve));
                            btnReserveGift.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_reserve_gift_button, null));

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            btnReserveGift.setOnClickListener(v -> {
                if (giftWishList.isBooked()) {
                    confirmDeleteReservation(giftWishList);
                } else {
                    confirmReservation(giftWishList);
                }
            });

        }

        private void createReservation(GiftWishList giftWishList) {
            daoSocialGift.createReservationGift(giftWishList, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, context.getResources().getString(R.string.reservation_created), Toast.LENGTH_SHORT).show();
                    giftWishList.setBooked(true);
                    btnReserveGift.setText(context.getResources().getString(R.string.gift_booked));
                    btnReserveGift.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_booked_gift_button, null));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.reservation_not_belong_you), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteReservation(GiftWishList giftWishList) {
            daoSocialGift.deleteReservationGift(giftWishList.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, context.getResources().getString(R.string.reservation_created), Toast.LENGTH_SHORT).show();
                    giftWishList.setBooked(false);
                    btnReserveGift.setText(context.getResources().getString(R.string.reserve));
                    btnReserveGift.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_reserve_gift_button, null));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.reservation_not_belong_you), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void confirmReservation(GiftWishList giftWishList) {
            if (context != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.confirm_reservation))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                createReservation(giftWishList);
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
        }

        private void confirmDeleteReservation(GiftWishList giftWishList) {
            if (context != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.confirm_delete_reservation))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteReservation(giftWishList);
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

        }
    }
}
