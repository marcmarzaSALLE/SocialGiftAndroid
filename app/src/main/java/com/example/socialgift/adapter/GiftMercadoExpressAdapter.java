package com.example.socialgift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.socialgift.R;
import com.example.socialgift.dao.DaoMercadoExpress;
import com.example.socialgift.dao.DaoSocialGift;
import com.example.socialgift.model.Gift;

import org.json.JSONObject;

import java.util.ArrayList;

public class GiftMercadoExpressAdapter extends RecyclerView.Adapter<GiftMercadoExpressAdapter.ViewHolder>{
    ArrayList<Gift>gifts;
    LayoutInflater inflater;
    GiftMercadoExpressAdapter.OnItemClickListener listener;
    DaoMercadoExpress daoMercadoExpress;
    TextView txtNoGifts;
    RecyclerView recyclerView;
    Context context;
    int wishListId;
    DaoSocialGift daoSocialGift;
    public interface OnItemClickListener {
        void onItemClick(Gift gift, int position);
    }

    public GiftMercadoExpressAdapter(int wishList,TextView textView,RecyclerView recyclerView,ArrayList<Gift> gifts, Context context, GiftMercadoExpressAdapter.OnItemClickListener listener) {
        this.gifts = gifts;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        daoMercadoExpress = DaoMercadoExpress.getInstance(context);
        this.txtNoGifts = textView;
        this.recyclerView = recyclerView;
        defaultRecyclerText();
        daoSocialGift = DaoSocialGift.getInstance(context);
        this.wishListId = wishList;
    }

    private void defaultRecyclerText(){
        txtNoGifts.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    public void setGifts(ArrayList<Gift> gifts) {
        if(gifts.isEmpty()){
            txtNoGifts.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            txtNoGifts.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            this.gifts = gifts;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return gifts.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_gift_mercadoexpress, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder( GiftMercadoExpressAdapter.ViewHolder holder, int position) {
        holder.bindData(gifts.get(position));
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGift;
        TextView txtNameGift, txtPriceGift, txtDescriptionGift;
        Button btnAddGift;
        public ViewHolder( View itemView) {
            super(itemView);
            imgGift = itemView.findViewById(R.id.imgViewGiftMercadoExpress);
            txtNameGift = itemView.findViewById(R.id.txtGiftNameMercadoExpress);
            txtPriceGift = itemView.findViewById(R.id.txtGiftPriceMercadoExpress);
            txtDescriptionGift = itemView.findViewById(R.id.txtGiftDescriptionMercadoExpress);
            btnAddGift = itemView.findViewById(R.id.btnAddGiftMercadoExpress);
        }

        public void bindData(Gift gift) {
            txtNameGift.setText(context.getResources().getString(R.string.gift_name,gift.getName()));
            txtPriceGift.setText(context.getResources().getString(R.string.price_gift,gift.getPrice()));
            txtDescriptionGift.setText(context.getResources().getString(R.string.gift_description,gift.getDescription()));
            Glide.with(context).load(gift.getUrlImage()).error(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_giftcard_green_24,null)).into(imgGift);
            btnAddGift.setOnClickListener(v->{
                selectGift(gift);
            });
        }

        private void selectGift(Gift gift) {
            if (!gift.isAdded()){
                daoSocialGift.createGift(gift.getId(), wishListId, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btnAddGift.setClickable(false);
                        btnAddGift.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.background_reserve_gift_button,null));
                        gift.setAdded(true);
                        listener.onItemClick(gift,getAdapterPosition());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error al agregar el regalo", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
