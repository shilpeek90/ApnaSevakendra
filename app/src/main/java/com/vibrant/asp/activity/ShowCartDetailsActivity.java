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
import com.vibrant.asp.adapter.ShowDetailsCartAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.ShowDetailCartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class ShowCartDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ShowCartDetailsActivity";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    ProgressDialog pd;
    RecyclerView recyclerView;
    ShowDetailsCartAdapter mAdapter;
    List<ShowDetailCartModel> cartArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart_details);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.show_cart_details));
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowCartDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isInternetConnected(getApplicationContext())) {
            getShowDetailCart();
        } else {
            showToast(ShowCartDetailsActivity.this, getResources().getString(R.string.check_network));
        }
    }

    private void getShowDetailCart() {
        pd = ProgressDialog.show(ShowCartDetailsActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenterId = getPreference(ShowCartDetailsActivity.this, "renterId");
            if (mRenterId != null) {
                jsonObject.put("BuyerId", mRenterId);
            }
            Log.d(TAG, "getShowDetailCart: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Cons.GET_SHOW_DEATIL_CART, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    cartArrayList.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ShowDetailCartModel getOrdersForRenter = new ShowDetailCartModel(
                                    jsonArray.getJSONObject(i).getInt("CartId"),
                                    jsonArray.getJSONObject(i).getString("Seller"),
                                    jsonArray.getJSONObject(i).getInt("Quantity"),
                                    jsonArray.getJSONObject(i).getInt("Amount"),
                                    jsonArray.getJSONObject(i).getDouble("CGST"),
                                    jsonArray.getJSONObject(i).getDouble("SGST"),
                                    jsonArray.getJSONObject(i).getString("ProductName"),
                                    jsonArray.getJSONObject(i).getString("CartDate"));
                            cartArrayList.add(getOrdersForRenter);
                        }
                        if (cartArrayList.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new ShowDetailsCartAdapter(ShowCartDetailsActivity.this, cartArrayList);
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
                showToast(ShowCartDetailsActivity.this, "Something went wrong");
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
