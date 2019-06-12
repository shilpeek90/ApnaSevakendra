package com.vibrant.asp.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.AutoSuggestDistrictAdapter;
import com.vibrant.asp.adapter.AutoSuggestStateAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.DistrictModel;
import com.vibrant.asp.model.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    TextView tvHeader;
    EditText editGrowerName, editMobile;
    AppCompatAutoCompleteTextView autoCompletState, autoCompletDistrict;
    private AutoSuggestStateAdapter autoSuggestAdapter;
    private AutoSuggestDistrictAdapter autoSuggestDistrictAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;

    private static final int TRIGGER_AUTO_COMPLETE1 = 100;
    private static final long AUTO_COMPLETE_DELAY1 = 300;
    private Handler handler1;

    Button btnSubmit;
    private boolean doubleBackToExitPressedOnce = false;
    ProgressDialog pd;
    ArrayList<StateModel> stateArray = new ArrayList<>();
    ArrayList<DistrictModel> districtArrayList = new ArrayList<>();

    String mSelectedState = "";
    String mSelectedDistrict = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        hideKeyboard(RegistrationActivity.this);

        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.registration));
        //  editState = findViewById(R.id.editState);
        autoCompletDistrict = findViewById(R.id.autoCompletDistrict);
        editGrowerName = findViewById(R.id.editGrowerName);
        editMobile = findViewById(R.id.editMobile);

        autoCompletState = findViewById(R.id.autoCompletState);

        autoSuggestAdapter = new AutoSuggestStateAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompletState.setThreshold(1);
        autoCompletState.setAdapter(autoSuggestAdapter);
        autoCompletState.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: " + autoSuggestAdapter.getObject(position).getStateId());
                        Log.d(TAG, "onItemClick: " + autoSuggestAdapter.getObject(position).getStateName());

                        mSelectedState = autoSuggestAdapter.getObject(position).getStateId();
                    }
                });

        autoCompletState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompletState.getText().toString())) {
                        getState(autoCompletState.getText().toString());
                    }
                }
                return false;
            }
        });

        //For  District

        autoSuggestDistrictAdapter = new AutoSuggestDistrictAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompletDistrict.setThreshold(1);
        autoCompletDistrict.setAdapter(autoSuggestDistrictAdapter);
        autoCompletDistrict.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: " + autoSuggestDistrictAdapter.getObject(position).getDistrictId());
                        Log.d(TAG, "onItemClick: " + autoSuggestDistrictAdapter.getObject(position).getDistrictName());

                        mSelectedDistrict = autoSuggestDistrictAdapter.getObject(position).getDistrictId();
                    }
                });

        autoCompletDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler1.removeMessages(TRIGGER_AUTO_COMPLETE1);
                handler1.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE1, AUTO_COMPLETE_DELAY1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler1 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE1) {
                    if (!TextUtils.isEmpty(autoCompletDistrict.getText().toString())) {
                        getDistrict(autoCompletDistrict.getText().toString());
                    }
                }
                return false;
            }
        });


        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getState(String text) {
        String url = Cons.GET_STATE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StateName", text);
            Log.d(TAG, "getState: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                stateArray.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            StateModel stateModel = new StateModel();
                            stateModel.setStateId(jObj.getString("StateId"));
                            stateModel.setStateName(jObj.getString("StateName"));
                            stateArray.add(stateModel);
                        }
                        if (stateArray.size() > 0) {
                            autoSuggestAdapter.setData(stateArray);
                            autoSuggestAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void getDistrict(String text) {
        String url = Cons.GET_DISTRICT;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StateCode", mSelectedState);
            jsonObject.put("District", text);
            Log.d(TAG, "getDistrict: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                districtArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            DistrictModel districtModel = new DistrictModel(jObj.getString("DistrictId"), jObj.getString("DistrictName"));
                            districtArrayList.add(districtModel);
                        }
                        if (districtArrayList.size() > 0) {
                            autoSuggestDistrictAdapter.setData(districtArrayList);
                            autoSuggestDistrictAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
