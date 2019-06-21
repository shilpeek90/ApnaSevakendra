package com.vibrant.asp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.QuantityAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.QuantityModel;
import com.vibrant.asp.model.RangeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class BookNowActivity extends AppCompatActivity {
    private static final String TAG = "BookNowActivity";
    TextView tvHeader, tvCommission, tvAmount;
    ImageView ivBack;
    ProgressDialog pd;
    EditText editSubscription;
    Button btnSubmit;
    String SubscriptionId = "";
    String mProductId = "";
    String mRenterId = "";
    int mRate = 0;
    String entered="";
    int calAmount =0;
    Spinner spinnerQuantity;
    QuantityAdapter adapter;
    List<QuantityModel> quantityArray = new ArrayList<>();
    String mResponse = "{\"d\":[  \n" +
            "    {\"range\":\"0 to 5\"},  \n" +
            "    {\"range\":\"5 to 10\"},        \n" +
            "    {\"range\":\"10 to 15\"},  \n" +
            "    {\"range\":\"15 to 20\"},  \n" +
            "    {\"range\":\"20 to 25\"},  \n" +
            "    {\"range\":\"25 to 30\"},  \n" +
            "    {\"range\":\"30 to 35\"},  \n" +
            "    {\"range\":\"35 to 40\"},  \n" +
            "    {\"range\":\"40 to 45\"},  \n" +
            "    {\"range\":\"45 to 50\"},  \n" +
            "    {\"range\":\"50 to 55\"},  \n" +
            "    {\"range\":\"55 to 60\"},  \n" +
            "    {\"range\":\"60 to 65\"},  \n" +
            "    {\"range\":\"65 to 70\"},  \n" +
            "    {\"range\":\"70 to 75\"},  \n" +
            "    {\"range\":\"75 to 80\"},  \n" +
            "    {\"range\":\"80 to 85\"},  \n" +
            "    {\"range\":\"85 to 90\"},  \n" +
            "    {\"range\":\"90 to 95\"},  \n" +
            "    {\"range\":\"95 to 100\"}   \n" +
            "]}  ";
    private String selectedQuantity ="";

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

        editSubscription = findViewById(R.id.editSubscription);
        tvCommission = findViewById(R.id.tvCommission);
        tvAmount = findViewById(R.id.tvAmount);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinnerQuantity = findViewById(R.id.spinnerQuantity);


        entered = editSubscription.getText().toString();
        tvAmount.setText(String.valueOf(getAmount(entered)));
        tvCommission.setText(String.valueOf(getCommistion(getAmount(entered))));

        editSubscription.addTextChangedListener(new TextWatcher() {
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
        //getQuantity();

        spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuantityModel quantityModel = adapter.getItem(position);
                selectedQuantity = quantityModel.getQuantity();
                Log.d(TAG, "onItemSelected: " + quantityModel.getQuantity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void getQuantity() {
        try {
            JSONObject jsonObject = new JSONObject(mResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("d");
            quantityArray.clear();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    QuantityModel quantityModel = new QuantityModel();
                    quantityModel.setQuantity(jsonArray.getJSONObject(i).getString("range"));
                    quantityArray.add(quantityModel);
                }
                if (quantityArray.size() > 0) {
                    adapter = new QuantityAdapter(getApplicationContext(), quantityArray);
                    spinnerQuantity.setAdapter(adapter);
                    // rangeAdapter.notifyDataSetChanged();
                }
            } else {
                showToast(BookNowActivity.this, "Data not found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            jsonObject.put("Count", editSubscription.getText().toString());
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
