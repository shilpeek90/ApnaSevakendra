package com.vibrant.asp.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.GetOrdersForRenteeAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.GetOrdersForRentee;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class OrdersForRenteeActivity extends AppCompatActivity {
    private static final String TAG = "GetOrdersForRenteeActiv";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    ProgressDialog pd;
    List<GetOrdersForRentee> getOrdersForRenters = new ArrayList<>();
    RecyclerView recyclerView;
    GetOrdersForRenteeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_for_rentee);
        init();
    }
    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.all_order_for_rantee));
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvNoRecord = findViewById(R.id.tvNoRecord);
        recyclerView = findViewById(R.id.recyclerView);
        if (isInternetConnected(getApplicationContext())) {
            getOrderForRentee();
        } else {
            showToast(OrdersForRenteeActivity.this, getResources().getString(R.string.check_network));
        }
    }

    private void getOrderForRentee() {
        String url = Cons.GET_ORDER_FOR_RENTEE;
        pd = ProgressDialog.show(OrdersForRenteeActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(OrdersForRenteeActivity.this, "Id");
            if (mRenteeId != null) {
               jsonObject.put("RenteeId", mRenteeId);
               // jsonObject.put("RenteeId", "2");
            }
            Log.d(TAG, "getOrderForRentee: " + jsonObject);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    getOrdersForRenters.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GetOrdersForRentee getOrdersForRentee = new GetOrdersForRentee();
                            getOrdersForRentee.setOrderId(jsonArray.getJSONObject(i).getInt("OrderId"));
                            getOrdersForRentee.setRenter(jsonArray.getJSONObject(i).getString("Renter"));
                            getOrdersForRentee.setMobno(jsonArray.getJSONObject(i).getString("Mobno"));
                            getOrdersForRentee.setAmount(jsonArray.getJSONObject(i).getString("Amount"));
                            getOrdersForRentee.setCommissionAmount(jsonArray.getJSONObject(i).getString("CommissionAmount"));
                            getOrdersForRentee.setStateName(jsonArray.getJSONObject(i).getString("StateName"));
                            getOrdersForRentee.setDistrictName(jsonArray.getJSONObject(i).getString("DistrictName"));
                            getOrdersForRentee.setBookingDate(jsonArray.getJSONObject(i).getString("BookingDate"));
                            getOrdersForRentee.setBookedTill(jsonArray.getJSONObject(i).getString("BookedTill"));
                            getOrdersForRentee.setCancelledBy(jsonArray.getJSONObject(i).getString("CancelledBy"));
                            getOrdersForRentee.setConfirmed(jsonArray.getJSONObject(i).getString("Confirmed"));
                            getOrdersForRenters.add(getOrdersForRentee);
                        }
                        if (getOrdersForRenters.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new GetOrdersForRenteeAdapter(OrdersForRenteeActivity.this, getOrdersForRenters);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrdersForRenteeActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
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
                showToast(OrdersForRenteeActivity.this, "Something went wrong");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
