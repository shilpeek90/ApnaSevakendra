package com.vibrant.asp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.vibrant.asp.adapter.BuyRangeAdapter;
import com.vibrant.asp.adapter.CancelOrderAdapter;
import com.vibrant.asp.adapter.ProductsForBuyAdapter;
import com.vibrant.asp.adapter.RangeAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.constants.Util;
import com.vibrant.asp.gps.GPSTracker1;
import com.vibrant.asp.model.BuyRangeModel;
import com.vibrant.asp.model.CancelOrderModel;
import com.vibrant.asp.model.ProductsForBuy;
import com.vibrant.asp.model.RangeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class BuyActivity extends AppCompatActivity {
    private static final String TAG = "BuyActivity";
    TextView tvHeader, tvNoRecord;
    ImageView ivBack;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ProgressDialog pd;
    Spinner spinnerRange;
    String selectedRange = "";
    String mFromKm = "";
    String mToKm = "";
    List<BuyRangeModel> rangeArray = new ArrayList<>();
    List<ProductsForBuy> buyList  = new ArrayList<>();
    BuyRangeAdapter rangeAdapter;
    RecyclerView recyclerView;
    ProductsForBuyAdapter mAdapter;
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

    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.buy_orders));
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
        spinnerRange = findViewById(R.id.spinnerRange);

        getRange();
        spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BuyRangeModel rangeModel = rangeAdapter.getItem(position);
                selectedRange = rangeModel.getRange();
                Log.d(TAG, "onItemSelected: " + rangeModel.getRange());
                String[] parts = selectedRange.split("to");
                mFromKm = parts[0];
                mToKm = parts[1];
                if (Util.checkRequestPermiss(getApplicationContext(), BuyActivity.this)) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    doPermissionGranted();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRange() {
        try {
            JSONObject jsonObject = new JSONObject(mResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("d");
            rangeArray.clear();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    BuyRangeModel rangeModel = new BuyRangeModel();
                    rangeModel.setRange(jsonArray.getJSONObject(i).getString("range"));
                    rangeArray.add(rangeModel);
                }
                if (rangeArray.size() > 0) {
                    rangeAdapter = new BuyRangeAdapter(getApplicationContext(), rangeArray);
                    spinnerRange.setAdapter(rangeAdapter);
                }
            } else {
                showToast(BuyActivity.this, "Data not found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBuyOrder() {
        String url = Cons.GET_FOR_BUY;
        pd = ProgressDialog.show(BuyActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenterId = getPreference(BuyActivity.this, "renterId");
            if (mRenterId != null) {
                jsonObject.put("UserId", mRenterId);
            }
            jsonObject.put("FromKm",mFromKm );
            jsonObject.put("ToKm", mToKm);
            jsonObject.put("Latitude", String.valueOf(latitude));
            jsonObject.put("Longitude", String.valueOf(longitude));
            Log.d(TAG, "getBuyOrder: "+jsonObject);
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
                    buyList.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProductsForBuy getOrdersForRenter = new ProductsForBuy(
                                    jsonArray.getJSONObject(i).getInt("SellerId"),
                                    jsonArray.getJSONObject(i).getString("Seller"),
                                    jsonArray.getJSONObject(i).getString("StateName"),
                                    jsonArray.getJSONObject(i).getString("DistrictName"),
                                    jsonArray.getJSONObject(i).getString("Address"),
                                    jsonArray.getJSONObject(i).getInt("ProdId"),
                                    jsonArray.getJSONObject(i).getInt("Rate"),
                                    jsonArray.getJSONObject(i).getString("ProductName"),
                                    jsonArray.getJSONObject(i).getInt("BalancedQty"),
                                    jsonArray.getJSONObject(i).getString("Image1"),
                                    jsonArray.getJSONObject(i).getString("Image2"));
                            buyList.add(getOrdersForRenter);
                        }
                        if (buyList.size() > 0) {
                            tvNoRecord.setVisibility(View.GONE);
                            mAdapter = new ProductsForBuyAdapter(BuyActivity.this, buyList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BuyActivity.this);
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
                showToast(BuyActivity.this, "Something went wrong");
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

    private void doPermissionGranted() {
        if (isInternetConnected(getApplicationContext())) {
                try {
                    GPSTracker1 gpsTracker = new GPSTracker1(BuyActivity.this);
                    // check if GPS enabled
                    if(gpsTracker.canGetLocation()){
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                        getBuyOrder();
                    }else{
                        gpsTracker.showSettingsAlert();    // Ask user to enable GPS/network in settings
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        } else {
            showToast(BuyActivity.this, getResources().getString(R.string.check_network));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for all permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "CAMERA & location services permission granted");
                        doPermissionGranted();
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Camera and Location Services Permission are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    Util.checkRequestPermiss(getApplicationContext(), BuyActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("Go to settings and enable permissions");
                            //  Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.vibrant.asp")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
