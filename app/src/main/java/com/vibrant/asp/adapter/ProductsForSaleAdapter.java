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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vibrant.asp.R;
import com.vibrant.asp.model.ProductsForSale;

import java.util.List;


public class ProductsForSaleAdapter extends RecyclerView.Adapter<ProductsForSaleAdapter.MyHolder> {
    private List<ProductsForSale> arrayList;
    Context mContext;
    private double latitude;
    private double longitude;
    String mImage1 = "";
    String mImage2 = "";

    public ProductsForSaleAdapter(Context mContext, List<ProductsForSale> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_sale_item_row, parent, false);
        return new ProductsForSaleAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
       // holder.tvName.setText(arrayList.get(position).getName().toUpperCase());


       /* holder.llBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mSubscriptionId = arrayList.get(position).getSubscriptionId();
                String mRenteeId = arrayList.get(position).getRenterId();
                String mProductId = arrayList.get(position).getProductId();
                int mRate = arrayList.get(position).getRate();
                String subName = arrayList.get(position).getSubName();
                Intent intent = new Intent(mContext, BookNowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SubscriptionId", mSubscriptionId);
                bundle.putString("mRenteeId", mRenteeId);
                bundle.putString("mProductId", mProductId);
                bundle.putInt("mRate", mRate);
                bundle.putString("subName", subName);
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobileNumber, tvStateName, tvDistrict, tvAddress, tvSubscription,
                tvRate, tvDistance, tvBookedTill, tvStatus, tvQuantity;
        LinearLayout llViewMap, llViewImage, llBookNow;
        RelativeLayout rlayoutQuantity;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            // tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
        }
    }
}
