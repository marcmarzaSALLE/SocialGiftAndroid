package com.example.socialgift.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import com.example.socialgift.model.Message;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;

    public ChatMessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
