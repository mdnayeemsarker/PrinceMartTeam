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
import com.princemartbd.team.model.StatementModel;

import java.util.ArrayList;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.ViewHolder>{
    private final Activity activity;
    private final ArrayList<StatementModel> statementModelArrayList;

    public StatementAdapter(Activity activity, ArrayList<StatementModel> statementModelArrayList) {
        this.activity = activity;
        this.statementModelArrayList = statementModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_statement_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StatementModel model = statementModelArrayList.get(position);

        holder.itemId.setText("Id. #" + model.getId());
        holder.amount.setText("৳" + model.getAmount());
        holder.postAmount.setText(" -> ৳" + model.getPost_balance());
        holder.transactionId.setText("Trx ID. #" + model.getTrx());
        holder.dateAndTime.setText("Date: " + model.getDate_created());
        if (model.getLast_updated().equals("null")){
            holder.updateDateAndTime.setText("Update: N/A");
        }else {
            holder.updateDateAndTime.setText(model.getLast_updated());
        }

        if (model.getTrx_type().equals("seller")){
            holder.message.setText("Merchant");
        }else {
            holder.message.setText("Customer");
        }
        holder.message.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));


        switch (model.getStatus()) {
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
        return statementModelArrayList.size();
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
