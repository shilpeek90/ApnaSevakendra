package com.vibrant.asp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.AutoSuggestDistrictAdapter;
import com.vibrant.asp.adapter.AutoSuggestStateAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.gps.GPSTracker;
import com.vibrant.asp.model.DistrictModel;
import com.vibrant.asp.model.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.setPreference;
import static com.vibrant.asp.constants.Util.showToast;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    TextView tvHeader;
    EditText editGrowerName, editMobile, editAddress, editPass;
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
    String error_message = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private double latitude;
    private double longitude;

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
        autoCompletDistrict = findViewById(R.id.autoCompletDistrict);
        editGrowerName = findViewById(R.id.editGrowerName);
        editMobile = findViewById(R.id.editMobile);
        editAddress = findViewById(R.id.editAddress);
        editPass = findViewById(R.id.editPass);
        autoCompletState = findViewById(R.id.autoCompletState);

        autoSuggestAdapter = new AutoSuggestStateAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompletState.setThreshold(1);
        autoCompletState.setAdapter(autoSuggestAdapter);
        autoCompletState.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        editGrowerName = findViewById(R.id.editGrowerName);
        editMobile = findViewById(R.id.editMobile);
        editAddress = findViewById(R.id.editAddress);


        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    if (Validation()) {
                        getPermissionGPS();
                    } else {
                        showToast(RegistrationActivity.this, error_message);
                    }
                } else {
                    showToast(RegistrationActivity.this, getResources().getString(R.string.check_network));
                }
            }
        });
    }


    private void getPermissionGPS() {
        // Check if the ACCESS_FINE_LOCATION permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ACCESS_FINE_LOCATION permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // ACCESS_FINE_LOCATION permission is already been granted.
            doPermissionGrantedStuffs();
        }

    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(RegistrationActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_read_phone_state_rationale1))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

                            //ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Received permission result for  permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission has been granted,
                alertAlert(getString(R.string.permision_available_read_phone_state1));
                doPermissionGrantedStuffs();
            } else {
                alertAlert(getString(R.string.permissions_not_granted_read_phone_state1));
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(RegistrationActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do something here
                    }
                })
                //	.setIcon(R.drawable.onlinlinew_warning_sign)
                .show();
    }

    public void doPermissionGrantedStuffs() {
        try {
            GPSTracker gpsTracker = new GPSTracker(RegistrationActivity.this);
            String msg = gpsTracker.getGPS_Location();
            Log.d(TAG, "doPermissionGrantedStuffs:---" + msg);
            if (msg == null) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Location not found,Please move to open sky and try again")) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Your GPS module is disabled. Would you like to enable it ?")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegistrationActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
                builder.setMessage("Your GPS module is disabled. Would you like to enable it ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Sent user to GPS settings screen
                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle(getResources().getString(R.string.app_name));
                alert.show();
            } else if (msg.equals("Location Found")) {
                Location location = gpsTracker.getLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    getRegister();
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getRegister() {
        String url = Cons.GET_REGISTER;
        pd = ProgressDialog.show(RegistrationActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StateId", mSelectedState);
            jsonObject.put("DistrictId", mSelectedDistrict);
            jsonObject.put("Name", editGrowerName.getText().toString().trim());
            jsonObject.put("Mobno", editMobile.getText().toString().trim());
            jsonObject.put("Password", editPass.getText().toString().trim());
            jsonObject.put("Address", editAddress.getText().toString().trim());
            jsonObject.put("Latitude", String.valueOf(latitude));
            jsonObject.put("Longitude", String.valueOf(longitude));

            Log.d(TAG, "getRegister: " + jsonObject);
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
                        setPreference(RegistrationActivity.this, "status", status);
                        String mob = editMobile.getText().toString().trim();
                        String pass = editPass.getText().toString().trim();
                        getGrowerProfile(mob, pass);
                    } else {
                        showToast(RegistrationActivity.this, "This Mobile Number Already Registered");
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
                showToast(RegistrationActivity.this, "Something went wrong");
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

    private void getGrowerProfile(String mob, String pass) {
        String url = Cons.GET_GROWER_PROFILE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Mobno", mob);
            jsonObject.put("Password", pass);
            Log.d(TAG, "getGrowerProfile: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(0);
                            String Id = jobj.getString("Id");
                            String name = jobj.getString("Name");
                            // String mobno = jobj.getString("Mobno");
                            //String address = jobj.getString("Address");
                            // String latitude = jobj.getString("Latitude");
                            // String longitude = jobj.getString("Longitude");
                            //  String districtId = jobj.getString("DistrictId");
                            // String stateId = jobj.getString("StateId");
                            // String tstamp = jobj.getString("Tstamp");

                            setPreference(RegistrationActivity.this, "Id", Id);
                            setPreference(RegistrationActivity.this, "name", name);
                        }
                        editGrowerName.setText("");
                        editMobile.setText("");
                        editAddress.setText("");
                        startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        showToast(RegistrationActivity.this, "No Record Found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(RegistrationActivity.this, "Something went wrong");
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


    private boolean Validation() {
        if (TextUtils.isEmpty(editGrowerName.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_name);
            return false;
        } else if (TextUtils.isEmpty(editMobile.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_mobile_number);
            return false;
        } else if (TextUtils.isEmpty(editPass.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_pass);
            return false;
        } else if (TextUtils.isEmpty(editAddress.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_address);
            return false;
        } else {
            return true;
        }
    }

    public void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegistrationActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //alert.setIcon(R.drawable.logodlg);
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.show();
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
                    // if (jsonArray.length() > 0) {
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
                    /*} else {
                        showToast(RegistrationActivity.this, "Data not found");
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(RegistrationActivity.this, "Something went wrong");
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        showToast(RegistrationActivity.this, "Something went wrong");
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
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
                        showToast(RegistrationActivity.this, "Data not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(RegistrationActivity.this, "Something went wrong");
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
        showToast(this, "Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
