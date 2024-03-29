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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.activity.BookNowActivity;
import com.vibrant.asp.activity.MapActivity;
import com.vibrant.asp.fragment.ImgViewFragment;
import com.vibrant.asp.model.AllProductModel;
import java.util.List;
import static com.vibrant.asp.constants.Util.roundTwoDecimals;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.MyHolder> {
    private List<AllProductModel> arrayList;
    Context mContext;
    private double latitude;
    private double longitude;
    String mImage1 = "";
    String mImage2 = "";

    public AllProductAdapter(Context mContext, List<AllProductModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_item_row, parent, false);
        return new AllProductAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getName().toUpperCase());
        holder.tvBookedTill.setText(arrayList.get(position).getBookedTill());

        if (arrayList.get(position).getBalanceQuantity() > 0) {
            holder.rlayoutQuantity.setVisibility(View.VISIBLE);
            holder.llBookNow.setEnabled(true);
            holder.tvQuantity.setText(String.valueOf(arrayList.get(position).getBalanceQuantity()));
        } else {
            holder.llBookNow.setEnabled(false);
            holder.rlayoutQuantity.setVisibility(View.GONE);
        }
        if (arrayList.get(position).getStatus().equalsIgnoreCase("Available")) {
            holder.tvStatus.setText(arrayList.get(position).getStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#17a75f"));
        } else {
            holder.tvStatus.setText(arrayList.get(position).getStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#808080"));
        }
        holder.tvRate.setText(String.valueOf(arrayList.get(position).getRate()) + " " + arrayList.get(position).getSubName());
        holder.tvDistance.setText(String.valueOf(roundTwoDecimals((arrayList.get(position).getDistance()))) + " " + "km");

        holder.llViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = Double.parseDouble(arrayList.get(position).getLatitude());
                longitude = Double.parseDouble(arrayList.get(position).getLongitude());
                Intent intent = new Intent(mContext, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mLatCurrent", String.valueOf(latitude));
                bundle.putString("mLngCurrent", String.valueOf(longitude));
                bundle.putString("name", arrayList.get(position).getName());
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });
        holder.llViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImage1 = arrayList.get(position).getImage1();
                mImage2 = arrayList.get(position).getImage2();
                Bundle bundle = new Bundle();
                bundle.putString("Image1", mImage1);
                bundle.putString("Image2", mImage2);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ImgViewFragment fragment = new ImgViewFragment();
                fragment.setArguments(bundle);
                fragment.show(activity.getSupportFragmentManager(), fragment.getTag());
            }
        });

        holder.llBookNow.setOnClickListener(new View.OnClickListener() {
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
        });
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
            tvRate = itemView.findViewById(R.id.tvRate);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            llViewMap = itemView.findViewById(R.id.llViewMap);
            llViewImage = itemView.findViewById(R.id.llViewImage);
            llBookNow = itemView.findViewById(R.id.llBookNow);
            tvBookedTill = itemView.findViewById(R.id.tvBookedTill);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            rlayoutQuantity = itemView.findViewById(R.id.rlayoutQuantity);
        }
    }

    public void updateList(List<AllProductModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}
