package com.vibrant.asp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.model.ConfirmOrderModel;
import java.util.List;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.MyHolder> {
    Context mContext;
    private List<ConfirmOrderModel> arrayList;

    public ConfirmOrderAdapter(Context mContext, List<ConfirmOrderModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_order_item_row, parent, false);
        return new ConfirmOrderAdapter.MyHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getName().toUpperCase());
        holder.tvProductName.setText(arrayList.get(position).getProductName());
        holder.tvBookedTill.setText(arrayList.get(position).getBookedTill());
        holder.tvDescription.setText(arrayList.get(position).getDescription());
        holder.tvConfirmed.setText(arrayList.get(position).getConfirmed());
        if (arrayList.get(position).getStatus().equalsIgnoreCase("Available")) {
            holder.tvStatus.setText(arrayList.get(position).getStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#228B22"));
        } else {
            holder.tvStatus.setText(arrayList.get(position).getStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#808080"));
        }
        holder.tvRate.setText(String.valueOf(arrayList.get(position).getRate()) + " " + arrayList.get(position).getSubName());
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvProductName, tvBookedTill, tvDescription, tvConfirmed, tvStatus;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBookedTill = itemView.findViewById(R.id.tvBookedTill);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
