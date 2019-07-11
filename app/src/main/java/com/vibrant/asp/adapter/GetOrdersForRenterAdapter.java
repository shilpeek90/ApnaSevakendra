package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
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
import com.vibrant.asp.model.GetOrdersForRenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.vibrant.asp.constants.Util.getPreference;

public class GetOrdersForRenterAdapter extends RecyclerView.Adapter<GetOrdersForRenterAdapter.MyHolder> {
    private static final String TAG = "GetOrdersForRenterAdapt";
    private List<GetOrdersForRenter> arrayList;
    Context mContext;
    AlertDialog.Builder builderConfirm;
    AlertDialog.Builder builderCancel;
    ProgressDialog pd;
    String mOrderId = "";
    String mOrderIdCancel = "";

    public GetOrdersForRenterAdapter(Context mContext, List<GetOrdersForRenter> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GetOrdersForRenterAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_for_renter_row, parent, false);
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

        if (arrayList.get(position).getConfirmed().equalsIgnoreCase("Yes")) {
            holder.tvStatus.setText(arrayList.get(position).getConfirmed());
            holder.tvStatus.setTextColor(Color.parseColor("#17a75f"));
        } else {
            holder.tvStatus.setText(arrayList.get(position).getConfirmed());
            holder.tvStatus.setTextColor(Color.parseColor("#000000"));
        }

        holder.llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderId = String.valueOf(arrayList.get(position).getOrderId());
                builderConfirm = new AlertDialog.Builder(mContext);
                builderConfirm.setMessage(R.string.dialog_message_confirm)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                UpdateOrderStatus();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builderConfirm.create();
                alert.setTitle(R.string.app_name);
                alert.show();
            }
        });

        holder.llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderIdCancel = String.valueOf(arrayList.get(position).getOrderId());
                builderCancel = new AlertDialog.Builder(mContext);
                builderCancel.setMessage(R.string.dialog_message_cancel)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CancelOrderByRenter();
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
        TextView tvName, tvMobileNumber, tvAmount, tvStateName, tvDistrictName, tvBookingDate, tvBookedTill, tvStatus, btnConfirm, btnCancelOrder;
        LinearLayout llConfirm, llCancel;

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
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);

            llConfirm = itemView.findViewById(R.id.llConfirm);
            llCancel = itemView.findViewById(R.id.llCancel);


        }
    }

    private void UpdateOrderStatus() {
        String url = Cons.GET_UPDATE_ORDER_STATUS;
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrderId", mOrderId);
            Log.d(TAG, "UpdateOrderStatus: " + jsonObject);
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
                        Toast.makeText(mContext, "Your order is successfully confirmed", Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                    } else {
                        Toast.makeText(mContext, "Your order is not confirmed", Toast.LENGTH_SHORT).show();
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

    private void CancelOrderByRenter() {
        String url = Cons.CANCEL_ORDER_BY_RENTER;
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenterId = getPreference(mContext, "renterId");
            if (mRenterId != null) {
                jsonObject.put("UserId", mRenterId);
            }
            jsonObject.put("OrderId", mOrderIdCancel);
            Log.d(TAG, "CancelOrderByRenter: " + jsonObject);
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
                        Toast.makeText(mContext, "Your order is successfully cancelled", Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                    } else {
                        Toast.makeText(mContext, "Your order is not cancelled", Toast.LENGTH_SHORT).show();
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
