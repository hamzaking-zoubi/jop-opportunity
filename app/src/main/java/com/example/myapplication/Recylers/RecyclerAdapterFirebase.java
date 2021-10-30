package com.example.myapplication.Recylers;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class RecyclerAdapterFirebase extends FirebaseRecyclerAdapter<Advertisement, RecyclerAdapterFirebase.Holder> {
    private Context context;
    private OnRecyclerviewItemClickListener listener;
    private ProgressBar barr;
    CalculateTime g;
    public RecyclerAdapterFirebase(@NonNull @NotNull FirebaseRecyclerOptions<Advertisement> options, Context context, OnRecyclerviewItemClickListener listener) {
        super(options);
        this.context = context;
        this.listener = listener;
    }

    public RecyclerAdapterFirebase(@NonNull @NotNull FirebaseRecyclerOptions<Advertisement> options, ProgressBar barr, Context context, OnRecyclerviewItemClickListener listener) {
        super(options);
        this.context = context;
        this.listener = listener;
        this.barr = barr;

    }

    @Override
    public void onDataChanged() {
        if (barr != null) {
            barr.setVisibility(View.GONE);
        }
    }


    @NonNull
    @Override
    public Advertisement getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Advertisement jobs) {

        if (jobs.getImageURL() != null && !jobs.getImageURL().equals(""))

            Glide.with(context).
                    load(jobs.getImageURL()).
//                    centerCrop().
                    placeholder(R.drawable.ic_launcher_foreground).
                    into(holder.imageView);

       holder.calculateTime.setMillisecond(jobs.getTimeSendMaseege());
        holder.tv_card_title.setText(jobs.getTitle());
        holder.id = jobs.getId();
        holder.companyId=jobs.getIdCompany();
        holder.tv_card_fewDit.setText(jobs.getFewDetail());
        holder.tv_card_category.setText(jobs.getCategory());
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_jobs, parent, false);
        return new RecyclerAdapterFirebase.Holder(v, parent.getContext().getResources());

    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_card_title;
        TextView tv_card_time;
        TextView tv_card_category;
        TextView tv_card_fewDit;
        String id;
        String companyId;
        private CalculateTime calculateTime;

        public Holder(@NonNull View itemView, Resources r) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_img);
            tv_card_title = itemView.findViewById(R.id.card_title);
            tv_card_fewDit = itemView.findViewById(R.id.card_fewDetail);
            tv_card_category = itemView.findViewById(R.id.card_category);
            calculateTime = new CalculateTime((TextView) itemView.findViewById(R.id.card_time), System.currentTimeMillis(), r);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(id,companyId);
                }
            });
        }
    }
}