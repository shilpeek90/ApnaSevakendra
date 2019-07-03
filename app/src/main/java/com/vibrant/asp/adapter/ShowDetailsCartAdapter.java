package com.vibrant.asp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.ShowDetailCartModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class ShowDetailsCartAdapter extends RecyclerView.Adapter<ShowDetailsCartAdapter.MyHolder> {
    private static final String TAG = "ShowDetailsCartAdapter";
    ProgressDialog pd;
    private List<ShowDetailCartModel> arrayList;
    Context mContext;
    String mCartId = "";
    String CartId = "";
    String CGST = "";
    String Amount = "";
    String SGST = "";
    String Quantity = "";

    public ShowDetailsCartAdapter(Context mContext, List<ShowDetailCartModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_detail_cart_row, parent, false);
        return new ShowDetailsCartAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvSeller.setText(arrayList.get(position).getSeller().toUpperCase());
        holder.text_number.setText(String.valueOf(arrayList.get(position).getQuantity()));
        holder.tvAmount.setText(String.valueOf(arrayList.get(position).getAmount()));
        holder.tvCGST.setText(String.valueOf(arrayList.get(position).getCGST()));
        holder.tvSGST.setText(String.valueOf(arrayList.get(position).getSGST()));
        holder.tvProductName.setText(arrayList.get(position).getProductName());
        holder.tvCartDate.setText(String.valueOf(arrayList.get(position).getCartDate()));

        holder.pro_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartId = String.valueOf(arrayList.get(position).getCartId());
                CGST = String.valueOf(arrayList.get(position).getCGST());
                Amount = String.valueOf(arrayList.get(position).getAmount());
                SGST = String.valueOf(arrayList.get(position).getSGST());
                Quantity = String.valueOf(arrayList.get(position).getQuantity());

               // getUpdateCart();
            }
        });
        holder.pro_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartId = String.valueOf(arrayList.get(position).getCartId());
               // getDeleteCart();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvSeller, tvQuantity, tvAmount, tvCGST, tvSGST, tvProductName, tvCartDate, text_number;
        LinearLayout pro_plus, pro_minus;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvSeller = itemView.findViewById(R.id.tvSeller);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            text_number = itemView.findViewById(R.id.text_number);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCGST = itemView.findViewById(R.id.tvCGST);
            tvSGST = itemView.findViewById(R.id.tvSGST);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCartDate = itemView.findViewById(R.id.tvCartDate);
            pro_plus = itemView.findViewById(R.id.pro_plus);
            pro_minus = itemView.findViewById(R.id.pro_minus);
        }
    }

    private void getUpdateCart() {
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CartId", CartId);
            jsonObject.put("CGST", CGST);
            jsonObject.put("Amount", Amount);
            jsonObject.put("SGST", SGST);
            jsonObject.put("Quantity", Quantity);
            Log.d(TAG, "getShowDetailCart: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Cons.GET_UPADATE_CART, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equalsIgnoreCase("true")) {
                      notifyDataSetChanged();
                        Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
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

    private void getDeleteCart() {
        pd = ProgressDialog.show(mContext, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CartId", mCartId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getDeleteCart: " + jsonObject);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Cons.GET_DELETE_CART, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equalsIgnoreCase("true")){
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
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
