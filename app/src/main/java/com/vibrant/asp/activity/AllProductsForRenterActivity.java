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
import com.vibrant.asp.adapter.AllProductsForRenterAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.GetAllProductsForRenter;
import com.vibrant.asp.model.GetOrdersForRentee;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class AllProductsForRenterActivity extends AppCompatActivity {
    private static final String TAG = "AllProductsForRenterAct";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    ProgressDialog pd;
    List<GetAllProductsForRenter> getOrdersForRenters = new ArrayList<>();
    RecyclerView recyclerView;
    AllProductsForRenterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_for_renter);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.my_products));
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
            GetAllProductsForRenter();
        } else {
            showToast(AllProductsForRenterActivity.this, getResources().getString(R.string.check_network));
        }
    }

    private void GetAllProductsForRenter() {
        String url = Cons.GET_ALL_PRODUCT_FOR_RENTER;
        pd = ProgressDialog.show(AllProductsForRenterActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(AllProductsForRenterActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("RenterId", mRenteeId);
                //jsonObject.put("RenterId", "1");
            }
            Log.d(TAG, "GetAllProductsForRenter: "+jsonObject);

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
                            GetAllProductsForRenter getOrdersForRentee = new GetAllProductsForRenter();
                            getOrdersForRentee.setName(jsonArray.getJSONObject(i).getString("name"));
                            getOrdersForRentee.setSubName(jsonArray.getJSONObject(i).getString("SubName"));
                            getOrdersForRentee.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                            getOrdersForRentee.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                            getOrdersForRentee.setImage1(jsonArray.getJSONObject(i).getString("Image1"));
                            getOrdersForRentee.setImage2(jsonArray.getJSONObject(i).getString("Image2"));
                            getOrdersForRentee.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                            getOrdersForRentee.setConfirmed(jsonArray.getJSONObject(i).getString("Confirmed"));
                            getOrdersForRentee.setStatus(jsonArray.getJSONObject(i).getString("Status"));
                            getOrdersForRentee.setBookedTill(jsonArray.getJSONObject(i).getString("BookedTill"));
                            getOrdersForRenters.add(getOrdersForRentee);
                        }
                        if (getOrdersForRenters.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new AllProductsForRenterAdapter(AllProductsForRenterActivity.this, getOrdersForRenters);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllProductsForRenterActivity.this);
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
                showToast(AllProductsForRenterActivity.this, "Something went wrong");
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
