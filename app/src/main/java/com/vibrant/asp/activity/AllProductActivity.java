package com.vibrant.asp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.vibrant.asp.adapter.AllProductAdapter;
import com.vibrant.asp.adapter.RangeAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.AllProductModel;
import com.vibrant.asp.model.RangeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.showToast;

public class AllProductActivity extends AppCompatActivity {
    private static final String TAG = "AllProductActivity";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    EditText editSearch;
    ProgressDialog pd;
    List<AllProductModel> allProductArrayList = new ArrayList<>();
    Spinner spinnerRange;
    RecyclerView recyclerView;
    RangeAdapter rangeAdapter;
    String selectedRange = "";
    String mFromKm = "";
    String mToKm = "";
    List<RangeModel> rangeMainArray = new ArrayList<>();
    AllProductAdapter mAdapter;
    private double mLatCurrent;
    private double mLngCurrent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        hideKeyboard(AllProductActivity.this);
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                mLatCurrent = Double.parseDouble(bundle.getString("mLatCurrent"));
                mLngCurrent = Double.parseDouble(bundle.getString("mLngCurrent"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.all_product));
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        tvNoRecord = findViewById(R.id.tvNoRecord);

        spinnerRange = findViewById(R.id.spinnerRange);
        recyclerView = findViewById(R.id.recyclerView);


        editSearch = findViewById(R.id.editSearch);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
                /*if (s.length()>0) {
                    mAdapter.getFilter().filter(s);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        getRange();
        spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RangeModel rangeModel = rangeAdapter.getItem(position);
                selectedRange = rangeModel.getRange();
                Log.d(TAG, "onItemSelected: " + rangeModel.getRange());
                String[] parts = selectedRange.split("to");
                mFromKm = parts[0]; // 004
                mToKm = parts[1];
                Log.d(TAG, "onItemSelected: " + mFromKm);
                Log.d(TAG, "onItemSelected: " + mToKm);
                getNearByProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void filter(String text) {
        List<AllProductModel> filteredArray = new ArrayList();
        for (AllProductModel d : allProductArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            // if(d.getName().contains(text)){
            if (d.getName().contains(text)) {
                tvNoRecord.setVisibility(View.GONE);
                filteredArray.add(d);
            } else {
                tvNoRecord.setVisibility(View.VISIBLE);
            }
        }
        //update recyclerview
        mAdapter.updateList(filteredArray);
    }


    private void getRange() {
        try {
            JSONObject jsonObject = new JSONObject(mResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("d");
            rangeMainArray.clear();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    RangeModel rangeModel = new RangeModel();
                    rangeModel.setRange(jsonArray.getJSONObject(i).getString("range"));
                    rangeMainArray.add(rangeModel);
                }
                if (rangeMainArray.size() > 0) {
                    rangeAdapter = new RangeAdapter(getApplicationContext(), rangeMainArray);
                    spinnerRange.setAdapter(rangeAdapter);
                    // rangeAdapter.notifyDataSetChanged();
                }
            } else {
                showToast(AllProductActivity.this, "Data not found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNearByProduct() {
        String url = Cons.GET_NEAR_BY_PRODUCT;
        pd = ProgressDialog.show(AllProductActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(AllProductActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("UserId", mRenteeId);
            }
            jsonObject.put("FromKm", mFromKm);
            jsonObject.put("ToKm", mToKm);
            jsonObject.put("Latitude", String.valueOf(mLatCurrent));
            jsonObject.put("Longitude", String.valueOf(mLngCurrent));
            Log.d(TAG, "getNearByProduct: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d(">>>>>>>>>>>>>", response.toString());
                pd.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    allProductArrayList.clear();
                    if (jsonArray.length()>0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AllProductModel allProductModel = new AllProductModel();
                            allProductModel.setRenterId(jsonArray.getJSONObject(i).getString("RenterId"));
                            allProductModel.setProductId(jsonArray.getJSONObject(i).getString("ProductId"));
                            allProductModel.setName(jsonArray.getJSONObject(i).getString("name"));
                            allProductModel.setMobile(jsonArray.getJSONObject(i).getString("mobno"));
                            allProductModel.setStateName(jsonArray.getJSONObject(i).getString("StateName"));
                            allProductModel.setDistrictName(jsonArray.getJSONObject(i).getString("DistrictName"));
                            allProductModel.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                            allProductModel.setSubName(jsonArray.getJSONObject(i).getString("SubName"));
                            allProductModel.setRate(jsonArray.getJSONObject(i).getInt("Rate"));
                            allProductModel.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                            allProductModel.setSubscriptionId(jsonArray.getJSONObject(i).getString("SubscriptionId"));
                            allProductModel.setImage1(jsonArray.getJSONObject(i).getString("Image1"));
                            allProductModel.setImage2(jsonArray.getJSONObject(i).getString("Image2"));
                            allProductModel.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                            allProductModel.setLatitude(jsonArray.getJSONObject(i).getString("Latitude"));
                            allProductModel.setLongitude(jsonArray.getJSONObject(i).getString("Longitude"));
                            allProductModel.setDistance(jsonArray.getJSONObject(i).getDouble("Distance"));
                            allProductModel.setStatus(jsonArray.getJSONObject(i).getString("Status"));
                            allProductModel.setBookedTill(jsonArray.getJSONObject(i).getString("BookedTill"));
                            allProductArrayList.add(allProductModel);
                        }
                        Log.d(TAG, "onResponse: " + allProductArrayList.size());
                        if (allProductArrayList.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new AllProductAdapter(AllProductActivity.this, allProductArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllProductActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                        }
                    }else {
                        showToast(AllProductActivity.this, "No Record Found");
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
                showToast(AllProductActivity.this, "Something went wrong");
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
