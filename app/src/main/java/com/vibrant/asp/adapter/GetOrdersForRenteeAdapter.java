package com.vibrant.asp.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.model.GetOrdersForRentee;
import java.util.List;

public class GetOrdersForRenteeAdapter extends RecyclerView.Adapter<GetOrdersForRenteeAdapter.MyHolder> {
    private List<GetOrdersForRentee> arrayList;
    Context mContext;

    public GetOrdersForRenteeAdapter(Context mContext, List<GetOrdersForRentee> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GetOrdersForRenteeAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rantee_item_row, parent, false);
        return new GetOrdersForRenteeAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GetOrdersForRenteeAdapter.MyHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getRenter().toUpperCase());
      //  holder.tvMobileNumber.setText(arrayList.get(position).getMobno());
        holder.tvAmount.setText(String.valueOf(arrayList.get(position).getAmount()));
        holder.tvCommitAmount.setText(String.valueOf(arrayList.get(position).getCommissionAmount()));
        holder.tvStateName.setText(arrayList.get(position).getStateName());
        holder.tvDistrictName.setText(arrayList.get(position).getDistrictName());
        holder.tvBookingDate.setText(arrayList.get(position).getBookingDate());
        holder.tvBookedTill.setText(arrayList.get(position).getBookedTill());
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobileNumber, tvAmount,tvCommitAmount, tvStateName, tvDistrictName,tvBookingDate, tvBookedTill;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCommitAmount = itemView.findViewById(R.id.tvCommitAmount);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvDistrictName = itemView.findViewById(R.id.tvDistrictName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookedTill = itemView.findViewById(R.id.tvBookedTill);
        }
    }
}
