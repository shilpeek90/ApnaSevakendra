package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.activity.DashboardActivity;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.GetOrdersForRentee;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.vibrant.asp.constants.Util.getPreference;

public class GetOrdersForRenteeAdapter extends RecyclerView.Adapter<GetOrdersForRenteeAdapter.MyHolder> {
    private static final String TAG = "GetOrdersForRenteeAdapt";
    private List<GetOrdersForRentee> arrayList;
    Context mContext;
    AlertDialog.Builder builderCancel;
    ProgressDialog pd;
    String mOrderId = "";

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
        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                alert.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobileNumber, tvAmount, tvCommitAmount, tvStateName, tvDistrictName, tvBookingDate, tvBookedTill;
        Button btnCancelOrder;

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
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }

    private void CancelOrderByRentee() {
        String url = Cons.CANCEL_ORDER_BY_RENTEE;
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(mContext, "Id");
            if (mRenteeId != null) {
                jsonObject.put("UserId", mRenteeId);
            }
            jsonObject.put("OrderId", "");
            Log.d(TAG, "CancelOrderByRentee: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equalsIgnoreCase("true")) {
                        mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                    } else {
                        Toast.makeText(mContext, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                pd.dismiss();
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjReq);
    }
}
