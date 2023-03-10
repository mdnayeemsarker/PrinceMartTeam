package com.princemartbd.team.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.princemartbd.team.R;
import com.princemartbd.team.activity.Base.HistoryActivity;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.model.AllMarketerModel;

import java.util.ArrayList;

public class AllMarketerAdapter extends RecyclerView.Adapter<AllMarketerAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<AllMarketerModel> allMarketerModelArrayList;

    public AllMarketerAdapter(Activity activity, ArrayList<AllMarketerModel> allMarketerModelArrayList) {
        this.activity = activity;
        this.allMarketerModelArrayList = allMarketerModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_all_marketer, parent, false);
        return new ViewHolder(view);
       }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemId.setText("ID. #" + allMarketerModelArrayList.get(position).getId());
        holder.name.setText(allMarketerModelArrayList.get(position).getName());
        String email = allMarketerModelArrayList.get(position).getEmail();
        holder.email.setText(email.replaceAll("(^[^@]{2}|(?!^)\\G)[^@]", "$1*"));
        String mobile = allMarketerModelArrayList.get(position).getMobile();
        holder.phone.setText(mobile.replaceAll("\\b(\\d{3})(\\d{5})(\\d{3})", "$1xxxxx$3"));
        holder.dateAndTime.setText("Date: " + allMarketerModelArrayList.get(position).getDate_create());
        holder.referralCode.setText(allMarketerModelArrayList.get(position).getReferral_code());

        if (allMarketerModelArrayList.get(position).getCountry_code().equals("88")){
            holder.countryCode.setText("BD");
        }
        holder.balance.setText("৳"+allMarketerModelArrayList.get(position).getBalance());
        switch (allMarketerModelArrayList.get(position).getStatus()) {
            case "0":
                holder.status.setText(Constant.NOT_APPROVED);
                holder.status.setBackgroundColor(ContextCompat.getColor(activity, R.color.active_yellow));
                break;
            case "1":
                holder.status.setText(Constant.APPROVED);
                holder.status.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary_green));
                break;
            case "2":
                holder.status.setText(Constant.CANCEL);
                holder.status.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                break;
        }

        holder.rootCV.setOnClickListener(v -> {
            Intent intent = new Intent(activity, HistoryActivity.class);
            intent.putExtra("userType", allMarketerModelArrayList.get(position).getId());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return allMarketerModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemId;
        private final TextView name;
        private final TextView email;
        private final TextView phone;
        private final TextView countryCode;
        private final TextView dateAndTime;
        private final TextView referralCode;
        private final TextView balance;
        private final TextView status;
        private final CardView rootCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemId = itemView.findViewById(R.id.itemId);
            name = itemView.findViewById(R.id.nameId);
            email = itemView.findViewById(R.id.emailId);
            phone = itemView.findViewById(R.id.phoneId);
            referralCode = itemView.findViewById(R.id.referralCodeId);
            countryCode = itemView.findViewById(R.id.countryCodeId);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeId);
            balance = itemView.findViewById(R.id.balanceId);
            status = itemView.findViewById(R.id.statusId);
            rootCV = itemView.findViewById(R.id.rootCV);
        }
    }
}
