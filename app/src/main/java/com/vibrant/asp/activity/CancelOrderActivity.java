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
import com.vibrant.asp.adapter.CancelOrderAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.CancelOrderModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class CancelOrderActivity extends AppCompatActivity {
    private static final String TAG = "CancelOrderActivity";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    ProgressDialog pd;
    List<CancelOrderModel> cancelOrderArray = new ArrayList<>();
    RecyclerView recyclerView;
    CancelOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        init();
    }
    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.cancel_orders));
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
            getCancelOrder();
        } else {
            showToast(CancelOrderActivity.this, getResources().getString(R.string.check_network));
        }
    }

    private void getCancelOrder() {
        String url = Cons.GET_CANCEL_ORDER;
        pd = ProgressDialog.show(CancelOrderActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(CancelOrderActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("RenterId", mRenteeId);
            }
            Log.d(TAG, "getOrderForRenter: " + jsonObject);
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
                    cancelOrderArray.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CancelOrderModel getOrdersForRenter = new CancelOrderModel();
                            getOrdersForRenter.setRenteeName(jsonArray.getJSONObject(i).getString("RenteeName"));
                            getOrdersForRenter.setCancelledBy(jsonArray.getJSONObject(i).getString("CancelledBy"));
                            getOrdersForRenter.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                            getOrdersForRenter.setAmount(jsonArray.getJSONObject(i).getString("Amount"));
                            getOrdersForRenter.setOrderQuantity(jsonArray.getJSONObject(i).getString("OrderQuantity"));
                            getOrdersForRenter.setCommissionAmount(jsonArray.getJSONObject(i).getString("CommissionAmount"));
                            getOrdersForRenter.setDistrictName(jsonArray.getJSONObject(i).getString("DistrictName"));
                            cancelOrderArray.add(getOrdersForRenter);
                        }
                        if (cancelOrderArray.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new CancelOrderAdapter(CancelOrderActivity.this, cancelOrderArray);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CancelOrderActivity.this);
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
                showToast(CancelOrderActivity.this, "Something went wrong");
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

