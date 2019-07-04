package com.vibrant.asp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.vibrant.asp.myInterface.OnRefreshViewListner;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


import static com.vibrant.asp.constants.Util.getAmountDecr;
import static com.vibrant.asp.constants.Util.getAmountIncre;
import static com.vibrant.asp.constants.Util.getCommissionDecr;
import static com.vibrant.asp.constants.Util.getCommissionIncr;

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
    public int mQuantity =0;
    int totalAmount = 0;
    int mAmount = 0;

    AlertDialog.Builder builderUpdate;
    AlertDialog.Builder builderCancel;
    private OnRefreshViewListner mRefreshListner;


    public ShowDetailsCartAdapter(Context mContext, List<ShowDetailCartModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mRefreshListner = (OnRefreshViewListner)mContext;
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
                mQuantity = Integer.parseInt(holder.text_number.getText().toString());
                mQuantity= mQuantity+1;
                mAmount =Integer.parseInt(holder.tvAmount.getText().toString());

                totalAmount= getAmountIncre(mAmount,mQuantity);
                holder.text_number.setText(String.valueOf(mQuantity));

                holder.tvAmount.setText(String.valueOf(totalAmount));
                holder.tvCGST.setText(String.valueOf(getCommissionIncr(totalAmount)));
                holder.tvSGST.setText(String.valueOf(getCommissionIncr(totalAmount)));

                Log.d(TAG, "onClick: "+"total"+totalAmount);
                Log.d(TAG, "onClick: "+mQuantity);
                Log.d(TAG, "onClick: "+String.valueOf(getAmountIncre(mAmount,mQuantity)));
                Log.d(TAG, "onClick: "+String.valueOf(getCommissionIncr(totalAmount)));


              /*  CartId = String.valueOf(arrayList.get(position).getCartId());
                CGST = String.valueOf(arrayList.get(position).getCGST());
                Amount = String.valueOf(arrayList.get(position).getAmount());
                SGST = String.valueOf(arrayList.get(position).getSGST());
                Quantity = String.valueOf(arrayList.get(position).getQuantity());

                builderUpdate = new AlertDialog.Builder(mContext);
                builderUpdate.setMessage(R.string.dialog_message_update)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getUpdateCart();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builderUpdate.create();
                alert.setTitle(R.string.app_name);
                alert.show();*/
            }
        });
        holder.pro_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             int mQuantity1 = Integer.parseInt(holder.text_number.getText().toString());
                mQuantity1= mQuantity1-1;
                int mAmount1 =Integer.parseInt(holder.tvAmount.getText().toString());
                int totalAmount1= getAmountDecr(mAmount1,mQuantity1);
                holder.text_number.setText(String.valueOf(mQuantity1));

                holder.tvAmount.setText(String.valueOf(totalAmount1));
                holder.tvCGST.setText(String.valueOf(getCommissionDecr(totalAmount1)));
                holder.tvSGST.setText(String.valueOf(getCommissionDecr(totalAmount1)));

                Log.d(TAG, "onClick: "+mQuantity);
                Log.d(TAG, "onClick: "+String.valueOf(getAmountDecr(mAmount,mQuantity)));
                Log.d(TAG, "onClick: "+String.valueOf(getCommissionDecr(totalAmount)));


               /* mCartId = String.valueOf(arrayList.get(position).getCartId());
                builderCancel = new AlertDialog.Builder(mContext);
                builderCancel.setMessage(R.string.dialog_message_delete)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                 getDeleteCart();
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

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartId = String.valueOf(arrayList.get(position).getCartId());
                builderCancel = new AlertDialog.Builder(mContext);
                builderCancel.setMessage(R.string.dialog_message_delete)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getDeleteCart();
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

   /* private int getAmount(int amount,int quantity) {
        return amount*quantity;
    }

    public double getCommission(int amount) {
        int commit = (amount * 18);
        double total = Double.valueOf((double) commit / 100);
        return total;
    }*/


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvSeller, tvQuantity, tvAmount, tvCGST, tvSGST, tvProductName, tvCartDate, text_number;
        LinearLayout pro_plus, pro_minus;
      //  ImageView plus,minus;
        Button btnRemove;

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
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    private void getUpdateCart() {
       // pd = ProgressDialog.show(mContext, "Please Wait...");
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
              //  pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equalsIgnoreCase("true")) {
                        mRefreshListner.refreshView();
                        Toast.makeText(mContext, "Item Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
               // pd.dismiss();
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
      //  pd = ProgressDialog.show(mContext, "Please Wait...");
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
               // pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equalsIgnoreCase("true")){
                        mRefreshListner.refreshView();
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
               // pd.dismiss();
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
