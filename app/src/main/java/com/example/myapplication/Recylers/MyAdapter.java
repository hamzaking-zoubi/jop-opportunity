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

import com.bumptech.glide.Glide;
import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Advertisement> mList;
    private Context context;
    private OnRecyclerviewItemClickListener listener;

    public MyAdapter(ArrayList<Advertisement> mList, Context context, OnRecyclerviewItemClickListener listener) {
        this.mList = mList;
        this.context = context;
        this.listener = listener;
    }
    CalculateTime s;

    public MyAdapter(Context context, ArrayList<Advertisement> mList) {

        this.context = context;
        this.mList = mList;
    }

    public void setJobs(ArrayList<Advertisement> cars) {
        this.mList = cars;
    }

    public void addJob(Advertisement f) {
        mList.add(f);

    }

    public void clear() {
        mList.clear();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_jobs, parent, false);


        return new MyViewHolder(v, parent.getContext().getResources());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mList.get(position).getImageURL() != null && !mList.get(position).getImageURL().equals(""))


            Glide.with(context).
                    load(mList.get(position).getImageURL()).
                    centerCrop().
                    placeholder(R.drawable.ic_launcher_foreground).
                    into(holder.imageView);


        holder.tv_card_title.setText(mList.get(position).getTitle());
        holder.id = mList.get(position).getId();

        holder.calculateTime.setMillisecond(mList.get(position).getTimeSendMaseege());

        holder.id = mList.get(position).getId();
        holder.idCompany = mList.get(position).getIdCompany();
        holder.tv_card_fewDit.setText(mList.get(position).getFewDetail());
        holder.tv_card_category.setText(mList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_card_title;
        TextView tv_card_time;
        TextView tv_card_category;
        TextView tv_card_fewDit;
        String id;
        String idCompany;
        private CalculateTime calculateTime;


        public MyViewHolder(@NonNull View itemView, Resources r) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_img);

            tv_card_title = itemView.findViewById(R.id.card_title);
            tv_card_fewDit = itemView.findViewById(R.id.card_fewDetail);
             tv_card_time = itemView.findViewById(R.id.card_time);
            tv_card_category = itemView.findViewById(R.id.card_category);
            calculateTime = new CalculateTime((TextView) itemView.findViewById(R.id.card_time), System.currentTimeMillis(), r);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(id,idCompany);

                }
            });
        }
    }
}
