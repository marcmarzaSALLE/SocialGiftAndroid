package com.example.socialgift.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialgift.R;
import com.example.socialgift.model.Wishlist;

import java.util.ArrayList;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ViewHolder> {
    private ArrayList<Wishlist> wishlists;
    private LayoutInflater inflater;
    private Context context;
    final ListFragmentAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Wishlist list);
    }

    public ListFragmentAdapter(ArrayList<Wishlist> wishlists, Context context, ListFragmentAdapter.OnItemClickListener listener) {
        this.wishlists = wishlists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return wishlists.size();
    }


    @Override
    public ListFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_list,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFragmentAdapter.ViewHolder holder, int position) {
        holder.bindData(wishlists.get(position));
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameList, txtDescriptionList, txtDateList, txtBookedGiftsList;
        ViewHolder(View itemView) {
            super(itemView);
            txtNameList = itemView.findViewById(R.id.txtNameList);
            txtDescriptionList = itemView.findViewById(R.id.txtDescriptionList);
            txtDateList = itemView.findViewById(R.id.txtDateList);
            txtBookedGiftsList = itemView.findViewById(R.id.txtBookedGiftsList);
        }

        public void bindData(final Wishlist wishlist){
            txtNameList.setText(wishlist.getNameList());
            txtDescriptionList.setText(wishlist.getDescriptionList());
            txtDateList.setText(itemView.getResources().getString(R.string.date_list, wishlist.getCreatedDateList(), wishlist.getEndDateList()));
            if(wishlist.getGifts().isEmpty()){
                Log.d("TAG", "bindData: " + wishlist.getGifts().size());
                txtBookedGiftsList.setText(itemView.getResources().getString(R.string.booked_gift_list,wishlist.getBookedGifts()));
            }else {
                Log.d("TAG", "bindData: " + wishlist.getGifts().size());
                txtBookedGiftsList.setText(itemView.getResources().getString(R.string.booked_gift_list, wishlist.getBookedGifts()));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(wishlist);
                }
            });
        }
    }
}
