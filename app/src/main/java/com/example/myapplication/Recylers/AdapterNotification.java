package com.example.myapplication.Recylers;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Notification.Data;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.Holder> {
    private Context context;
    private OnRecyclerviewItemClickListener listener;

    CalculateTime g;
    ArrayList<Data> data;

    public AdapterNotification(ArrayList<Data> options, Context context, OnRecyclerviewItemClickListener listener) {

        this.context = context;
        this.listener = listener;
        this.data = options;
    }


    public void setJobs(ArrayList<Data> cars) {
        this.data = cars;
    }

    public void addJob(Data f) {
        data.add(f);

    }

    public void clear() {
        data.clear();
    }


    public Data getItem(int position) {
        return data.get(position);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_adapter, parent, false);
        return new AdapterNotification.Holder(v, parent.getContext().getResources());

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {

        holder.tv_Name.setText(data.get(position).getMessage());
        holder.id = data.get(position).getAdvId();
        holder.textViewDescription.setText(data.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_Name;
        TextView textViewDescription;
        String id;

        //private CalculateTime calculateTime;


        public Holder(@NonNull View itemView, Resources r) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCompany);

            tv_Name = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            //    calculateTime = new CalculateTime((TextView) itemView.findViewById(R.id.textCalculateTime), System.currentTimeMillis(), r);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(id);
                }
            });
        }
    }
}