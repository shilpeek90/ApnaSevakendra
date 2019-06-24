package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibrant.asp.R;
import com.vibrant.asp.activity.AllProductImagViewActivity;
import com.vibrant.asp.model.CancelOrderModel;
import com.vibrant.asp.model.GetAllProductsForRenter;

import java.util.List;

public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.MyHolder> {
    Context mContext;
    private List<CancelOrderModel> arrayList;

    public CancelOrderAdapter(Context mContext, List<CancelOrderModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_order_item_row, parent, false);
        return new CancelOrderAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.tvName.setText(arrayList.get(position).getRenteeName().toUpperCase());
        holder.tvCancelledBy.setText(arrayList.get(position).getCancelledBy());
        holder.tvProductName.setText(arrayList.get(position).getProductName());
        holder.tvAmount.setText(arrayList.get(position).getAmount());
        holder.tvOrderQuantity.setText(arrayList.get(position).getOrderQuantity());
        holder.tvComminAmount.setText(arrayList.get(position).getCommissionAmount());
        holder.tvDistrictName.setText(arrayList.get(position).getDistrictName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvCancelledBy,tvProductName,tvAmount,tvOrderQuantity,tvComminAmount,tvDistrictName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCancelledBy = itemView.findViewById(R.id.tvCancelledBy);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvOrderQuantity = itemView.findViewById(R.id.tvOrderQuantity);
            tvComminAmount = itemView.findViewById(R.id.tvComminAmount);
            tvDistrictName = itemView.findViewById(R.id.tvDistrictName);
        }
    }
}
