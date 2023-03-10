package com.princemartbd.team.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.princemartbd.team.R;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.model.HistoryModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private final Activity activity;
    private final ArrayList<HistoryModel> historyModelArrayList;

    public HistoryAdapter(Activity activity, ArrayList<HistoryModel> historyModelArrayList) {
        this.activity = activity;
        this.historyModelArrayList = historyModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_history_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel model = historyModelArrayList.get(position);
        holder.itemIdTV.setText("ID. #" + model.getId());
        holder.nameTV.setText(model.getName());
        String email = model.getEmail();
        holder.emailTV.setText(email.replaceAll("(^[^@]{2}|(?!^)\\G)[^@]", "$1*"));
        String mobile = model.getMobile();
        holder.phoneTV.setText(mobile.replaceAll("\\b(\\d{3})(\\d{5})(\\d{3})", "$1xxxxx$3"));
        switch (model.getStatus()) {
            case "0":
                holder.statusTV.setText(Constant.NOT_APPROVED);
                holder.statusTV.setBackgroundColor(ContextCompat.getColor(activity, R.color.active_yellow));
                break;
            case "1":
                holder.statusTV.setText(Constant.APPROVED);
                holder.statusTV.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary_green));
                break;
            case "2":
                holder.statusTV.setText(Constant.CANCEL);
                holder.statusTV.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                break;
        }

        if (model.getStore_name().equals("store_name")){
            holder.storeNameTV.setVisibility(View.GONE);
        }else {
            holder.storeNameTV.setText(model.getStore_name());
        }

        Glide.with(activity).load(model.getLogo()).error(activity.getDrawable(R.mipmap.ic_launcher)).placeholder(activity.getDrawable(R.mipmap.ic_launcher)).into(holder.logoImageIV);
    }

    @Override
    public int getItemCount() {
        return historyModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemIdTV;
        private final TextView nameTV;
        private final TextView storeNameTV;
        private final ImageView logoImageIV;
        private final TextView emailTV;
        private final TextView phoneTV;
        private final TextView statusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemIdTV = itemView.findViewById(R.id.itemIdTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            logoImageIV = itemView.findViewById(R.id.logoImageIV);
            emailTV = itemView.findViewById(R.id.emailTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            statusTV = itemView.findViewById(R.id.statusTV);
        }
    }
}
