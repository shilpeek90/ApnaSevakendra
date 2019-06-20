package com.vibrant.asp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class BookNowActivity extends AppCompatActivity {
    private static final String TAG = "BookNowActivity";
    TextView tvHeader, tvCommission, tvAmount;
    ImageView ivBack;
    ProgressDialog pd;
    EditText editQnt;
    String error_message = "";
    Button btnSubmit;
    String SubscriptionId = "";
    String mProductId = "";
    String mRenterId = "";
    int mRate = 0;
    String entered="";
    int calAmount =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        hideKeyboard(BookNowActivity.this);
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                SubscriptionId = bundle.getString("SubscriptionId");
                mRenterId = bundle.getString("mRenterId");
                mProductId = bundle.getString("mProductId");
                mRate = bundle.getInt("mRate");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.book_now));
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editQnt = findViewById(R.id.editQnt);
        tvCommission = findViewById(R.id.tvCommission);
        tvAmount = findViewById(R.id.tvAmount);
        btnSubmit = findViewById(R.id.btnSubmit);

        entered = editQnt.getText().toString();
        tvAmount.setText(String.valueOf(getAmount(entered)));
        tvCommission.setText(String.valueOf(getCommistion(getAmount(entered))));

        editQnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
                entered = s.toString();
                Log.d(TAG, "onTextChanged: "+">>>>>"+entered);
                tvAmount.setText(String.valueOf(getAmount(entered)));
               tvCommission.setText(String.valueOf(getCommistion(getAmount(entered))));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    getBookNow();
                } else {
                    showToast(BookNowActivity.this, getResources().getString(R.string.check_network));
                }
            }
        });
    }

    private int getAmount(String entered) {
        int amt=0;
        if (entered.length()>0){
           amt= Integer.parseInt(entered);
        }
         calAmount = (Integer.valueOf(mRate)*amt);
        return calAmount;
    }

    private void getBookNow() {
        String url = Cons.GET_BOOK_PRODUCT;
        pd = ProgressDialog.show(BookNowActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {

            String mRenteeId = getPreference(BookNowActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("RenteeId", mRenteeId);
            }
            jsonObject.put("RenterId", mRenterId);
            jsonObject.put("ProductId", mProductId);
            jsonObject.put("Amount", tvAmount.getText().toString());
            jsonObject.put("Count", editQnt.getText().toString());
            jsonObject.put("SubscriptionId", SubscriptionId);
            jsonObject.put("CommissionAmount", tvCommission.getText().toString());

            Log.d(TAG, "getBookNow: " + jsonObject);
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
                    if (status.equals("true")) {
                        showToast(BookNowActivity.this, "Successfully Booked");
                       startActivity(new Intent(BookNowActivity.this, DashboardActivity.class));
                       finish();
                    } else {
                        showToast(BookNowActivity.this, "Something went wrong");
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
                showToast(BookNowActivity.this, "Something went wrong");
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
    public double getCommistion(int amount) {
        int commit =(amount*5);
        double total= Double.valueOf((double)commit/100);
        return total;
    }
}
