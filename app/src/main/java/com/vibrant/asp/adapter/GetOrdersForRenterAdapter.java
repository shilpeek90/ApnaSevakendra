package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import com.vibrant.asp.R;
import com.vibrant.asp.activity.DashboardActivity;
import com.vibrant.asp.model.GetOrdersForRenter;

import java.util.List;

public class GetOrdersForRenterAdapter extends RecyclerView.Adapter<GetOrdersForRenterAdapter.MyHolder> {
    private List<GetOrdersForRenter> arrayList;
    Context mContext;
    AlertDialog.Builder builder;

    public GetOrdersForRenterAdapter(Context mContext, List<GetOrdersForRenter> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GetOrdersForRenterAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_order_item_row, parent, false);
        return new GetOrdersForRenterAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GetOrdersForRenterAdapter.MyHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getRentee().toUpperCase());
        holder.tvMobileNumber.setText(arrayList.get(position).getMobno());
        holder.tvAmount.setText(String.valueOf(arrayList.get(position).getAmount()));
        holder.tvStateName.setText(arrayList.get(position).getStateName());
        holder.tvDistrictName.setText(arrayList.get(position).getDistrictName());
        holder.tvBookingDate.setText(arrayList.get(position).getBookingDate());
        holder.tvBookedTill.setText(arrayList.get(position).getBookedTill());
        holder.tvDistance.setText(arrayList.get(position).getDistrictName());
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(mContext);
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Are you sure want to confirm ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                // Toast.makeText(mContext, "C", Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialog");
                alert.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobileNumber, tvAmount, tvStateName, tvDistrictName, tvBookingDate, tvBookedTill, tvDistance;
        Button btnConfirm;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvDistrictName = itemView.findViewById(R.id.tvDistrictName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookedTill = itemView.findViewById(R.id.tvBookedTill);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);

        }
    }
}
