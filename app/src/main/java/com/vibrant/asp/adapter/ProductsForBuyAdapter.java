package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vibrant.asp.R;
import com.vibrant.asp.activity.BookNowActivity;
import com.vibrant.asp.activity.BuyImageViewActivity;
import com.vibrant.asp.activity.MapActivity;
import com.vibrant.asp.activity.ViewImageActivity;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.AllProductModel;
import com.vibrant.asp.model.ProductsForBuy;

import java.util.List;

import static com.vibrant.asp.constants.Util.roundTwoDecimals;

public class ProductsForBuyAdapter extends RecyclerView.Adapter<ProductsForBuyAdapter.MyHolder> {
    private List<ProductsForBuy> arrayList;
    Context mContext;
    String mImage1 = "";
    String mImage2 = "";
    AlertDialog.Builder builderCancel;
    ProgressDialog pd;
    String mOrderId = "";

    public ProductsForBuyAdapter(Context mContext, List<ProductsForBuy> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_buy_row_item_row, parent, false);
        return new ProductsForBuyAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getSeller().toUpperCase());
        holder.tvStateName.setText(arrayList.get(position).getStateName());
        holder.tvDistrictName.setText(arrayList.get(position).getDistrictName());
        holder.tvAddress.setText(arrayList.get(position).getAddress());
        holder.tvRate.setText(String.valueOf(arrayList.get(position).getRate()));
        holder.tvProductName.setText(arrayList.get(position).getProductName());
        holder.tvBalanceQuantity.setText(String.valueOf(arrayList.get(position).getBalancedQty()));


        holder.llViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImage1 = arrayList.get(position).getImage1();
                mImage2 = arrayList.get(position).getImage2();
                Intent intent = new Intent(mContext, BuyImageViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Image1", mImage1);
                bundle.putString("Image2", mImage2);
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });

        holder.btnBookProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mOrderId =String.valueOf(arrayList.get(position).getOrderId());

                builderCancel = new AlertDialog.Builder(mContext);
                builderCancel.setMessage(R.string.dialog_message_cancel)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CancelOrderByRentee();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builderCancel.create();
                alert.setTitle(R.string.app_name);
                alert.show();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStateName, tvDistrictName, tvAddress, tvProductName, tvRate, tvBalanceQuantity;
        LinearLayout llViewImg;
        Button btnBookProduct;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvDistrictName = itemView.findViewById(R.id.tvDistrictName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvBalanceQuantity = itemView.findViewById(R.id.tvBalanceQuantity);
            llViewImg = itemView.findViewById(R.id.llViewImg);
            btnBookProduct = itemView.findViewById(R.id.btnBookProduct);
        }
    }
}
