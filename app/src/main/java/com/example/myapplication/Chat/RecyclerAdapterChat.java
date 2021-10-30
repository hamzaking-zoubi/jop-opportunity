package com.example.myapplication.Chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerAdapterChat extends RecyclerView.Adapter<RecyclerAdapterChat.Holder> {
    ArrayList<Message> messages = new ArrayList<>();

    public RecyclerAdapterChat(ArrayList<Message> m) {
        this.messages = m;
    }


    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessages(Message f) {
        messages.add(f);

    }

    public void clear() {
        messages.clear();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message, null, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapterChat.Holder holder, int position) {
        Message message = messages.get(position);

        if (message.getPhotoUrl() != null && !message.getPhotoUrl().equals("")) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(holder.imageView.getContext()).load(message.getPhotoUrl()).into(holder.imageView);
            Log.d("picasso1", message.getPhotoUrl());

            //  holder.imageView.setImageURI(Uri.parse(message.getPhotoUrl()));
            holder.tv.setVisibility(View.GONE);
        } else {
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(message.getText());
            holder.imageView.setVisibility(View.GONE);
        }
        holder.name.setText(message.getName());
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView tv;
       // TextView  cut;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_tv);
            imageView = itemView.findViewById(R.id.custom_iv);
            tv = itemView.findViewById(R.id.custom_text);
         //  cut=itemView.findViewById(R.id.custom_cut);
        }
    }
}
