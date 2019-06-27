package com.vibrant.asp.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.activity.ConfirmedImageViewActivity;
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
        holder.tvName.setText(arrayList.get(position).getRentee().toUpperCase());
        holder.tvProductName.setText(arrayList.get(position).getProductName());
        holder.tvQuantity.setText(arrayList.get(position).getQuantity());

        holder.llViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mImage1 = arrayList.get(position).getImage1();
                String mImage2 = arrayList.get(position).getImage2();
                Intent intent = new Intent(mContext, ConfirmedImageViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("image1", mImage1);
                bundle.putString("image2", mImage2);
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvProductName;
        LinearLayout llViewImg;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            llViewImg = itemView.findViewById(R.id.llViewImg);
        }
    }
}
