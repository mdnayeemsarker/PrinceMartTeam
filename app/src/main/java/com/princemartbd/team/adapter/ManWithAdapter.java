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
import com.princemartbd.team.model.ManWithModel;

import java.util.ArrayList;

public class ManWithAdapter extends RecyclerView.Adapter<ManWithAdapter.ViewHolder>  {

    private final Activity activity;
    private final ArrayList<ManWithModel> manWithModelArrayList;

    public ManWithAdapter(Activity activity, ArrayList<ManWithModel> manWithModelArrayList) {
        this.activity = activity;
        this.manWithModelArrayList = manWithModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_withdraw, parent, false);
        return new ManWithAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amount.setText("৳"+manWithModelArrayList.get(position).getAmount());
        holder.typeId.setText("ID. #" + manWithModelArrayList.get(position).getType_id());
        holder.type.setText("Type: " + manWithModelArrayList.get(position).getType());
        holder.message.setText(manWithModelArrayList.get(position).getMessage());
        holder.dateAndTime.setText("Date: "+manWithModelArrayList.get(position).getDate_created());

        if (manWithModelArrayList.get(position).getLast_updated().equals("null")){
            holder.updateDateAndTime.setText("N/A");
        }else {
            holder.updateDateAndTime.setText(manWithModelArrayList.get(position).getLast_updated());
        }

        switch (manWithModelArrayList.get(position).getStatus()) {
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
        return manWithModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView typeId;
        private final TextView amount;
        private final TextView type;
        private final TextView message;
        private final TextView dateAndTime;
        private final TextView updateDateAndTime;
        private final TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            typeId = itemView.findViewById(R.id.typedIdId);
            amount = itemView.findViewById(R.id.amountId);
            type = itemView.findViewById(R.id.typeId);
            message = itemView.findViewById(R.id.messageId);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeId);
            updateDateAndTime = itemView.findViewById(R.id.updateDateAndTimeId);
            status = itemView.findViewById(R.id.statusId);
        }
    }
}
