package com.princemartbd.team.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.princemartbd.team.R;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.model.ManTranModel;

import java.util.ArrayList;

public class ManTranAdapter extends RecyclerView.Adapter<ManTranAdapter.ViewHolder> {
    
    private final Activity activity;
    private final ArrayList<ManTranModel> manTranModelArrayList;

    public ManTranAdapter(Activity activity, ArrayList<ManTranModel> manTranModelArrayList) {
        this.activity = activity;
        this.manTranModelArrayList = manTranModelArrayList;
    }

    @NonNull
    @Override
    public ManTranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_all_transactions, parent, false);
        return new ManTranAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ManTranAdapter.ViewHolder holder, int position) {
        holder.itemId.setText("Id. #" + manTranModelArrayList.get(position).getId());
        holder.amount.setText("৳" + manTranModelArrayList.get(position).getAmount());
        holder.postAmount.setText("-> ৳" + manTranModelArrayList.get(position).getPost_balance());
        holder.transactionId.setText("Trx ID. #" + manTranModelArrayList.get(position).getTrx());
        holder.dateAndTime.setText("Date: " + manTranModelArrayList.get(position).getDate_created());
        if (manTranModelArrayList.get(position).getLast_updated().equals("null")){
            holder.updateDateAndTime.setText("Update: N/A");
        }else {
            holder.updateDateAndTime.setText(manTranModelArrayList.get(position).getLast_updated());
        }

        if (manTranModelArrayList.get(position).getTrx_type().equals("affiliaters")){
            holder.message.setText(Constant.CREDIT_MESSAGE);
//            holder.message.setBackgroundColor(getResources().getColor(R.color.primary_green));
            holder.message.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary_green));
        }else if (manTranModelArrayList.get(position).getTrx_type().equals("withdraw")){
            holder.message.setText(Constant.DEBIT);
            holder.message.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_blue_dark));
        }else {
            holder.message.setText("N/A");
            holder.message.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }

        switch (manTranModelArrayList.get(position).getStatus()) {
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
    }

    @Override
    public int getItemCount() {
        return manTranModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemId;
        private final TextView amount;
        private final TextView postAmount;
        private final TextView transactionId;
        private final TextView dateAndTime;
        private final TextView updateDateAndTime;
        private final TextView message;
        private final TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemId = itemView.findViewById(R.id.itemId);
            amount = itemView.findViewById(R.id.amountId);
            postAmount = itemView.findViewById(R.id.postAmountId);
            transactionId = itemView.findViewById(R.id.transactionId);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeId);
            updateDateAndTime = itemView.findViewById(R.id.updateDateAndTimeId);
            message = itemView.findViewById(R.id.messageId);
            status = itemView.findViewById(R.id.statusId);
        }
    }
}
