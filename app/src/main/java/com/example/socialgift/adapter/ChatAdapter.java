package com.example.socialgift.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgift.R;
import com.example.socialgift.controller.SharedPreferencesController;
import com.example.socialgift.model.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private int MY_MESSAGE = 0, FRIEND_MESSAGE = 1;
    private List<Message> messages;
    private LayoutInflater inflater;
    private SharedPreferencesController sharedPreferencesController;


    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        sharedPreferencesController = new SharedPreferencesController();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view ;
        if (viewType == MY_MESSAGE) {
            view = inflater.inflate(R.layout.item_message_me, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_message_friend, parent, false);
        }
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
       holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getUser_id_send() == sharedPreferencesController.loadUserIdSharedPreferences(context)) {
            return MY_MESSAGE;
        } else {
            return FRIEND_MESSAGE;
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMessageMe,txtHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageMe = itemView.findViewById(R.id.text_content);
            txtHour = itemView.findViewById(R.id.txt_time);
        }

        public void bind(Message message){
            txtMessageMe.setText(message.getContent());
            String time = message.getTimeStamp();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            LocalDateTime timeStamp = LocalDateTime.parse(time, formatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedTime = timeStamp.format(outputFormatter);

            txtHour.setText(formattedTime);
        }
    }
}
