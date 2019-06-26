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
    TextView tvHeader, tvCommission, tvAmount,tvSubName;
    ImageView ivBack;
    ProgressDialog pd;
    EditText editSubscription;
    Button btnSubmit;
    String SubscriptionId = "";
    String mProductId = "";
    String mRenteeId = "";
    String mSubName = "";
    int mRate = 0;
    String entered="";
    int calAmount =0;
    Spinner spinnerQuantity;
    QuantityAdapter adapter;
    List<QuantityModel> quantityArray = new ArrayList<>();
    String mResponse = "{\"d\":[  \n" +
            "    {\"quantity\":\"1\"},  \n" +
            "    {\"quantity\":\"2\"},  \n" +
            "    {\"quantity\":\"3\"},  \n" +
            "    {\"quantity\":\"4\"},  \n" +
            "    {\"quantity\":\"5\"},  \n" +
            "    {\"quantity\":\"6\"},  \n" +
            "    {\"quantity\":\"7\"},  \n" +
            "    {\"quantity\":\"8\"},  \n" +
            "    {\"quantity\":\"9\"},  \n" +
            "    {\"quantity\":\"10\"},  \n" +
            "    {\"quantity\":\"11\"},  \n" +
            "    {\"quantity\":\"12\"},  \n" +
            "    {\"quantity\":\"13\"},  \n" +
            "    {\"quantity\":\"14\"},  \n" +
            "    {\"quantity\":\"15\"},  \n" +
            "    {\"quantity\":\"16\"},  \n" +
            "    {\"quantity\":\"17\"},  \n" +
            "    {\"quantity\":\"18\"},  \n" +
            "    {\"quantity\":\"19\"},  \n" +
            "    {\"quantity\":\"20\"},  \n" +
            "    {\"quantity\":\"21\"},  \n" +
            "    {\"quantity\":\"22\"},  \n" +
            "    {\"quantity\":\"23\"},  \n" +
            "    {\"quantity\":\"24\"},  \n" +
            "    {\"quantity\":\"25\"},  \n" +
            "    {\"quantity\":\"26\"},  \n" +
            "    {\"quantity\":\"27\"},  \n" +
            "    {\"quantity\":\"28\"},  \n" +
            "    {\"quantity\":\"29\"},  \n" +
            "    {\"quantity\":\"30\"},  \n" +
            "    {\"quantity\":\"31\"},  \n" +
            "    {\"quantity\":\"32\"},  \n" +
            "    {\"quantity\":\"33\"},  \n" +
            "    {\"quantity\":\"34\"},  \n" +
            "    {\"quantity\":\"35\"},  \n" +
            "    {\"quantity\":\"36\"},  \n" +
            "    {\"quantity\":\"37\"},  \n" +
            "    {\"quantity\":\"38\"},  \n" +
            "    {\"quantity\":\"39\"},  \n" +
            "    {\"quantity\":\"40\"},  \n" +
            "    {\"quantity\":\"41\"},  \n" +
            "    {\"quantity\":\"42\"},  \n" +
            "    {\"quantity\":\"43\"},  \n" +
            "    {\"quantity\":\"44\"},  \n" +
            "    {\"quantity\":\"45\"},  \n" +
            "    {\"quantity\":\"46\"},  \n" +
            "    {\"quantity\":\"47\"},  \n" +
            "    {\"quantity\":\"48\"},  \n" +
            "    {\"quantity\":\"49\"},  \n" +
            "    {\"quantity\":\"50\"},  \n" +
            "    {\"quantity\":\"51\"},  \n" +
            "    {\"quantity\":\"52\"},  \n" +
            "    {\"quantity\":\"53\"},  \n" +
            "    {\"quantity\":\"54\"},  \n" +
            "    {\"quantity\":\"55\"},  \n" +
            "    {\"quantity\":\"56\"},  \n" +
            "    {\"quantity\":\"57\"},  \n" +
            "    {\"quantity\":\"58\"},  \n" +
            "    {\"quantity\":\"59\"},  \n" +
            "    {\"quantity\":\"60\"},  \n" +
            "    {\"quantity\":\"61\"},  \n" +
            "    {\"quantity\":\"62\"},  \n" +
            "    {\"quantity\":\"63\"},  \n" +
            "    {\"quantity\":\"64\"},  \n" +
            "    {\"quantity\":\"65\"},  \n" +
            "    {\"quantity\":\"66\"},  \n" +
            "    {\"quantity\":\"67\"},  \n" +
            "    {\"quantity\":\"68\"},  \n" +
            "    {\"quantity\":\"69\"},  \n" +
            "    {\"quantity\":\"70\"},  \n" +
            "    {\"quantity\":\"71\"},  \n" +
            "    {\"quantity\":\"72\"},  \n" +
            "    {\"quantity\":\"73\"},  \n" +
            "    {\"quantity\":\"74\"},  \n" +
            "    {\"quantity\":\"75\"},  \n" +
            "    {\"quantity\":\"76\"},  \n" +
            "    {\"quantity\":\"78\"},  \n" +
            "    {\"quantity\":\"79\"},  \n" +
            "    {\"quantity\":\"80\"},  \n" +
            "    {\"quantity\":\"81\"},  \n" +
            "    {\"quantity\":\"82\"},  \n" +
            "    {\"quantity\":\"83\"},  \n" +
            "    {\"quantity\":\"84\"},  \n" +
            "    {\"quantity\":\"85\"},  \n" +
            "    {\"quantity\":\"86\"},  \n" +
            "    {\"quantity\":\"87\"},  \n" +
            "    {\"quantity\":\"88\"},  \n" +
            "    {\"quantity\":\"89\"},  \n" +
            "    {\"quantity\":\"90\"},  \n" +
            "    {\"quantity\":\"91\"},  \n" +
            "    {\"quantity\":\"92\"},  \n" +
            "    {\"quantity\":\"93\"},  \n" +
            "    {\"quantity\":\"94\"},  \n" +
            "    {\"quantity\":\"95\"},  \n" +
            "    {\"quantity\":\"96\"},  \n" +
            "    {\"quantity\":\"97\"},  \n" +
            "    {\"quantity\":\"98\"},  \n" +
            "    {\"quantity\":\"99\"},  \n" +
            "    {\"quantity\":\"100\"} \n" +
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
                mRenteeId = bundle.getString("mRenteeId");
                mProductId = bundle.getString("mProductId");
                mSubName = bundle.getString("subName");
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
        tvSubName = findViewById(R.id.tvSubName);

        entered = editSubscription.getText().toString();
        tvSubName.setText(mSubName);
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
        getQuantity();
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
                    quantityModel.setQuantity(jsonArray.getJSONObject(i).getString("quantity"));
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
            String mRenteerId = getPreference(BookNowActivity.this, "renterId");
            if (mRenteerId != null) {
                jsonObject.put("RenterId", mRenteerId);
            }
            jsonObject.put("RenteeId", mRenteeId);
            jsonObject.put("ProductId", mProductId);
            jsonObject.put("Amount", tvAmount.getText().toString());
            jsonObject.put("Count", editSubscription.getText().toString());
            jsonObject.put("SubscriptionId", SubscriptionId);
            jsonObject.put("Quantity", selectedQuantity);
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
