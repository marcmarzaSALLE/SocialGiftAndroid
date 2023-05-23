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
import com.example.socialgift.fragments.BookingUserFragment;
import com.example.socialgift.model.GiftWishList;
import com.example.socialgift.model.Wishlist;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListGiftBookedAdapter extends RecyclerView.Adapter<ListGiftBookedAdapter.ViewHolder> {
    private ArrayList<GiftWishList> giftWishLists;
    private LayoutInflater inflater;
    private ListGiftBookedAdapter.OnItemClickListener listener;

    private BookingUserFragment bookingUserFragment;
    private Context context;
    private DaoMercadoExpress daoMercadoExpress;
    private DaoSocialGift daoSocialGift;

    public interface OnItemClickListener {
        void onItemClick(Wishlist wishlist, int position);
    }

    public ListGiftBookedAdapter(ArrayList<GiftWishList> giftWishLists, Context context, OnItemClickListener listener) {
        this.giftWishLists = giftWishLists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.daoMercadoExpress = new DaoMercadoExpress(context);
        this.daoSocialGift = new DaoSocialGift(context);
        this.bookingUserFragment = new BookingUserFragment();
    }



    @Override
    public int getItemCount() {
        return giftWishLists.size();
    }

    @Override
    public ListGiftBookedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_gift_wishlist, null);
        return new ListGiftBookedAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListGiftBookedAdapter.ViewHolder holder, int position) {
        holder.bindData(giftWishLists.get(holder.getAdapterPosition()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGift;
        TextView txtNameGift, txtGiftId, txtWishlistId, txtGiftBooked;
        Button btnDeleteGift;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGift = itemView.findViewById(R.id.imgGift);
            txtNameGift = itemView.findViewById(R.id.txtGiftName);
            txtGiftId = itemView.findViewById(R.id.txtGiftId);
            txtWishlistId = itemView.findViewById(R.id.txtWishlistName);
            txtGiftBooked = itemView.findViewById(R.id.txtGiftBooked);
            btnDeleteGift = itemView.findViewById(R.id.btnDeleteGift);
            btnDeleteGift.setText(context.getResources().getString(R.string.unbook));
        }

        public void bindData(GiftWishList giftWishList) {
            daoMercadoExpress.getGiftWishList(giftWishList.getProductLink(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Glide.with(context).load(response.getString("photo")).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_giftcard_green_24, null)).into(imgGift);
                        txtGiftId.setText(context.getResources().getString(R.string.gift_id, giftWishList.getId()));
                        txtWishlistId.setText(context.getResources().getString(R.string.wishlist_id, giftWishList.getIdWishList()));
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
                confirmDeleteReservation(giftWishList);
            });
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

    private void deleteReservation(GiftWishList giftWishList) {
        daoSocialGift.deleteReservationGift(giftWishList.getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Se ha eliminado el regalo de la lista", Toast.LENGTH_SHORT).show();
                giftWishLists.remove(giftWishList);
                notifyItemRemoved(giftWishLists.indexOf(giftWishList));
                notifyItemRangeChanged(giftWishLists.indexOf(giftWishList), getItemCount());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al eliminar el regalo de la lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFragment(){
        if (bookingUserFragment != null) {
            bookingUserFragment.refreshFragment();
        }
    }



}
