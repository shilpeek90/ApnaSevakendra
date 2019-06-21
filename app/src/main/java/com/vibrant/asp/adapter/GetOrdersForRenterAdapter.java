package com.vibrant.asp.adapter;
import android.content.Context;
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

public class GetOrdersForRenterAdapter extends RecyclerView.Adapter<GetOrdersForRenterAdapter.MyHolder> {
    private static final String TAG = "GetOrdersForRenterAdapt";
    private List<GetOrdersForRenter> arrayList;
    Context mContext;
    AlertDialog.Builder builder;
    ProgressDialog pd;

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
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                builder.setMessage("Are you sure want to confirm ?")
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
                AlertDialog alert = builder.create();
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

    private void UpdateOrderStatus() {
        String url = Cons.GET_UPDATE_ORDER_STATUS;
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrderId", "");
            jsonObject.put("Status", "");
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
