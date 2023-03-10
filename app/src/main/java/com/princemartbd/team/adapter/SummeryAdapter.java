package com.princemartbd.team.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.princemartbd.team.R;
import com.princemartbd.team.activity.Base.StatementActivity;
import com.princemartbd.team.activity.Marketer.MarSummeryActivity;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.model.SummeryModel;

import java.util.ArrayList;

public class SummeryAdapter extends RecyclerView.Adapter<SummeryAdapter.ViewHolder>{

    private final Activity activity;
    private final ArrayList<SummeryModel> summeryModelArrayList;

    public SummeryAdapter(Activity activity, ArrayList<SummeryModel> summeryModelArrayList) {
        this.activity = activity;
        this.summeryModelArrayList = summeryModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_summery_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SummeryModel model = summeryModelArrayList.get(position);
        holder.itemIdTV.setText("ID. #" + model.getId());
        holder.nameTV.setText(model.getName());
        holder.balanceTV.setText("৳" + model.getBalance());
        if (model.getIncome().equals("null") || model.getIncome() == null){
            holder.incomeTV.setVisibility(View.GONE);
        }else {
            holder.incomeTV.setText(model.getIncome());
        }

        holder.rootCV.setOnClickListener(v -> {
            if (model.getType().equals("coordinators")){
                activity.startActivity(new Intent(activity, MarSummeryActivity.class).putExtra("userType", model.getId()));
            }else if (model.getType().equals("seller")){
                activity.startActivity(new Intent(activity, StatementActivity.class).putExtra(Constant.PAGE, "transactions_from_type").putExtra(Constant.TYPE, model.getType()).putExtra(Constant.TYPE_ID, model.getId()).putExtra(Constant.USER_ID, model.getUserId()));
            }else {
                activity.startActivity(new Intent(activity, StatementActivity.class).putExtra(Constant.PAGE, "transactions_from_type").putExtra(Constant.TYPE, model.getType()).putExtra(Constant.TYPE_ID, model.getId()).putExtra(Constant.USER_ID, model.getUserId()));
            }
        });

        Glide.with(activity).load(model.getProfile()).error(activity.getDrawable(R.drawable.ic_avater)).placeholder(activity.getDrawable(R.mipmap.ic_launcher)).into(holder.logoImageIV);
    }

    @Override
    public int getItemCount() {
        return summeryModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemIdTV;
        private final TextView nameTV;
        private final TextView balanceTV;
        private final TextView incomeTV;
        private final ImageView logoImageIV;
        private final CardView rootCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemIdTV = itemView.findViewById(R.id.itemIdTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            balanceTV = itemView.findViewById(R.id.balanceTV);
            incomeTV = itemView.findViewById(R.id.incomeTV);
            logoImageIV = itemView.findViewById(R.id.logoImageIV);
            rootCV = itemView.findViewById(R.id.rootCV);
        }
    }
}
