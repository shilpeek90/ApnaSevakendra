package com.vibrant.asp.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.fragment.ImgViewFragment;
import com.vibrant.asp.model.GetAllProductsForRenter;
import java.util.List;

public class AllProductsForRenterAdapter extends RecyclerView.Adapter<AllProductsForRenterAdapter.MyHolder> {
    Context mContext;
    private List<GetAllProductsForRenter> arrayList;

    public AllProductsForRenterAdapter(Context mContext, List<GetAllProductsForRenter> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_renter_item_row, parent, false);
        return new AllProductsForRenterAdapter.MyHolder(itemView);
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
            holder.tvStatus.setTextColor(Color.parseColor("#000000"));
        }
        holder.tvRate.setText(String.valueOf(arrayList.get(position).getRate()) + " " + arrayList.get(position).getSubName());

        holder.llViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mImage1 = arrayList.get(position).getImage1();
                String mImage2 = arrayList.get(position).getImage2();
                Bundle bundle = new Bundle();
                bundle.putString("Image1", mImage1);
                bundle.putString("Image2", mImage2);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ImgViewFragment fragment = new ImgViewFragment();
                fragment.setArguments(bundle);
                fragment.show(activity.getSupportFragmentManager(), fragment.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvProductName, tvBookedTill, tvDescription, tvConfirmed, tvStatus;
        LinearLayout llViewImg;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBookedTill = itemView.findViewById(R.id.tvBookedTill);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            llViewImg = itemView.findViewById(R.id.llViewImg);
        }
    }
}
