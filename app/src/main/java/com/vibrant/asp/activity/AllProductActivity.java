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
    List<RangeModel> rangeMainArray = new ArrayList<>();
    AllProductAdapter mAdapter;
    private double mLatCurrent;
    private double mLngCurrent;
    String mResponse = "{\"d\":[       \n" +
            "    {\"range\":1},    \n" +
            "    {\"range\":2},  \n" +
            "    {\"range\":3},    \n" +
            "    {\"range\":4},    \n" +
            "    {\"range\":5},    \n" +
            "    {\"range\":6},    \n" +
            "    {\"range\":7},    \n" +
            "    {\"range\":8},    \n" +
            "    {\"range\":9},    \n" +
            "    {\"range\":10},    \n" +
            "    {\"range\":11},    \n" +
            "    {\"range\":12},    \n" +
            "    {\"range\":13},    \n" +
            "    {\"range\":14},    \n" +
            "    {\"range\":15},    \n" +
            "    {\"range\":16},    \n" +
            "    {\"range\":17},    \n" +
            "    {\"range\":18},    \n" +
            "    {\"range\":19},    \n" +
            "    {\"range\":20},   \n" +
            "    {\"range\":21},   \n" +
            "    {\"range\":22},   \n" +
            "    {\"range\":23},   \n" +
            "    {\"range\":24},   \n" +
            "    {\"range\":25},   \n" +
            "    {\"range\":26},   \n" +
            "    {\"range\":27},   \n" +
            "    {\"range\":28},   \n" +
            "    {\"range\":29},   \n" +
            "    {\"range\":30},   \n" +
            "    {\"range\":31},   \n" +
            "    {\"range\":32},   \n" +
            "    {\"range\":33},   \n" +
            "    {\"range\":34},   \n" +
            "    {\"range\":35},   \n" +
            "    {\"range\":36},   \n" +
            "    {\"range\":37},   \n" +
            "    {\"range\":38},   \n" +
            "    {\"range\":39},   \n" +
            "    {\"range\":40},   \n" +
            "    {\"range\":41},   \n" +
            "    {\"range\":42},   \n" +
            "    {\"range\":43},   \n" +
            "    {\"range\":44},   \n" +
            "    {\"range\":45},   \n" +
            "    {\"range\":46},   \n" +
            "    {\"range\":47},   \n" +
            "    {\"range\":48},   \n" +
            "    {\"range\":49},   \n" +
            "    {\"range\":50}   \n" +
            "]}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        hideKeyboard(AllProductActivity.this);
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle!=null) {
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

        editSearch =findViewById(R.id.editSearch);

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
                Log.d(TAG, "onTextChanged: "+s);
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
                    Log.d(TAG, "onItemSelected: "+rangeModel.getRange());
                    getNearByProduct();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

   public void filter(String text){
        List<AllProductModel> filteredArray = new ArrayList();
        for(AllProductModel d: allProductArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
           // if(d.getName().contains(text)){
            if(d.getName().contains(text)){
                tvNoRecord.setVisibility(View.GONE);
                filteredArray.add(d);
            }else {
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
            jsonObject.put("Range", selectedRange);
            jsonObject.put("Latitude", String.valueOf(mLatCurrent));
            jsonObject.put("Longitude", String.valueOf(mLngCurrent));
            //jsonObject.put("Latitude", "27.8975");
           // jsonObject.put("Longitude", "81.8555");
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AllProductModel allProductModel = new AllProductModel();
                        allProductModel.setName(jsonArray.getJSONObject(i).getString("name"));
                        allProductModel.setMobile(jsonArray.getJSONObject(i).getString("mobno"));
                        allProductModel.setStateName(jsonArray.getJSONObject(i).getString("StateName"));
                        allProductModel.setDistrictName(jsonArray.getJSONObject(i).getString("DistrictName"));
                        allProductModel.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                        allProductModel.setSubName(jsonArray.getJSONObject(i).getString("SubName"));
                        allProductModel.setRate(jsonArray.getJSONObject(i).getInt("Rate"));
                        allProductModel.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                        allProductModel.setImage1(jsonArray.getJSONObject(i).getString("Iamge1"));
                        allProductModel.setImage2(jsonArray.getJSONObject(i).getString("Image2"));
                        allProductModel.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                        allProductModel.setLatitude(jsonArray.getJSONObject(i).getString("Latitude"));
                        allProductModel.setLongitude(jsonArray.getJSONObject(i).getString("Longitude"));
                        allProductModel.setDistance(jsonArray.getJSONObject(i).getDouble("Distance"));

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
