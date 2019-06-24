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
import com.vibrant.asp.adapter.PendingOrdersAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.PendingOrdersModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class PendingOrdersActivity extends AppCompatActivity {
    private static final String TAG = "PendingOrdersActivity";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    ProgressDialog pd;
    List<PendingOrdersModel> pendingOrderArray = new ArrayList<>();
    RecyclerView recyclerView;
    PendingOrdersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.pending_orders));
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
            getPendingOrder();
        } else {
            showToast(PendingOrdersActivity.this, getResources().getString(R.string.check_network));
        }
    }

    private void getPendingOrder() {
        String url = Cons.GET_PENDING_ORDER;
        pd = ProgressDialog.show(PendingOrdersActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(PendingOrdersActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("RenterId", mRenteeId);
            }
            Log.d(TAG, "getPendingOrder: " + jsonObject);
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
                    pendingOrderArray.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PendingOrdersModel pendingOrdersModel = new PendingOrdersModel();
                            pendingOrdersModel.setBookedTill(jsonArray.getJSONObject(i).getString("BookedTill"));
                            pendingOrdersModel.setConfirmed(jsonArray.getJSONObject(i).getString("Confirmed"));
                            pendingOrderArray.add(pendingOrdersModel);
                        }

                        if (pendingOrderArray.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new PendingOrdersAdapter(PendingOrdersActivity.this, pendingOrderArray);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PendingOrdersActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        // showToast(OrdersForRenterActivity.this, "No Record Found");
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
                showToast(PendingOrdersActivity.this, "Something went wrong");
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
