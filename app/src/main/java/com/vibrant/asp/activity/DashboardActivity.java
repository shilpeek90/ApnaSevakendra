package com.vibrant.asp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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
import com.vibrant.asp.constants.Util;
import com.vibrant.asp.gps.GPSTracker1;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardActivity";
    private boolean doubleBackToExitPressedOnce = false;
    private double latitude;
    private double longitude;
    TextView tvName, tvWallet;
    ProgressDialog pd;
    LinearLayout rlayLend, rlRent, llrow1, llrow2, llrow3, llrow4, llaySale, llConfirmedOrder, llayBuyOrder,
            llayPendingOrder, rlWalletRecharge, rlHelp;
    Animation leftSide, rightSide;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        leftSide = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.left_side);
        rightSide = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.right_side);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvName);
        tvWallet = headerView.findViewById(R.id.tvWallet);
        String mName = getPreference(DashboardActivity.this, "name");
        if (mName != null && !mName.isEmpty()) {
            tvName.setText(mName);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (isInternetConnected(getApplicationContext())) {
            GetRenterWalletBalance();
        } else {
            showToast(DashboardActivity.this, getResources().getString(R.string.check_network));
        }
        init();
    }

    private void init() {
        tvWallet = findViewById(R.id.tvWallet);
        rlayLend = findViewById(R.id.rlayLend);
        rlRent = findViewById(R.id.rlRent);
        llrow1 = findViewById(R.id.llrow1);
        llrow2 = findViewById(R.id.llrow2);
        llrow3 = findViewById(R.id.llrow3);
        llrow4 = findViewById(R.id.llrow4);
        llayBuyOrder = findViewById(R.id.llayBuyOrder);
        llConfirmedOrder = findViewById(R.id.llConfirmedOrder);
        llayPendingOrder = findViewById(R.id.llayPendingOrder);
        rlWalletRecharge = findViewById(R.id.rlWalletRecharge);
        llaySale = findViewById(R.id.llaySale);
        rlHelp = findViewById(R.id.rlHelp);

        //for animation
        llrow1.startAnimation(rightSide);
        llrow2.startAnimation(leftSide);
        llrow3.startAnimation(rightSide);
        llrow4.startAnimation(leftSide);

        rlRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkRequestPermiss(getApplicationContext(), DashboardActivity.this)) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    doPermissionGranted();
                }
            }
        });

        rlayLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, UploadProductActivity.class));
            }
        });

        llayBuyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, BuyActivity.class));
            }
        });

        llaySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this, "Coming soon");
                // startActivity(new Intent(DashboardActivity.this, SaleActivity.class));
            }
        });
        rlWalletRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this, "Coming Soon");
            }
        });

        rlHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HelpActivity.class));
            }
        });
    }

    private void doPermissionGranted() {
        try {
            GPSTracker1 gpsTracker = new GPSTracker1(DashboardActivity.this);
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                Intent intent = new Intent(DashboardActivity.this, AllProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mLatCurrent", String.valueOf(latitude));
                bundle.putString("mLngCurrent", String.valueOf(longitude));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else {
                gpsTracker.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "CAMERA & location services permission granted");
                        doPermissionGranted();
                    } else {
                        Log.d(TAG, "Camera and Location Services Permission are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    Util.checkRequestPermiss(getApplicationContext(), DashboardActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            explain("Go to settings and enable permissions");
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

    private void GetRenterWalletBalance() {
        String url = Cons.GET_RENTER_WALLET_BALANCE;
        pd = ProgressDialog.show(DashboardActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteerId = getPreference(DashboardActivity.this, "renterId");
            if (mRenteerId != null) {
                jsonObject.put("RenterId", mRenteerId);
            }
            Log.d(TAG, "getOrderForRentee: " + jsonObject);
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
                    String mWalletBalance = jsonObject.getString("d");
                    if (mWalletBalance != null && !mWalletBalance.isEmpty()) {
                        tvWallet.setText(" " + mWalletBalance);
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
                showToast(DashboardActivity.this, "Something went wrong");
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_to_cart) {
            startActivity(new Intent(DashboardActivity.this, ShowCartDetailsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_OrdersForRenter) {
            startActivity(new Intent(DashboardActivity.this, OrdersForRenterActivity.class));
        } else if (id == R.id.nav_GetOrdersForRentee) {
            startActivity(new Intent(DashboardActivity.this, OrdersForRenteeActivity.class));
        } else if (id == R.id.nav_AllProductsForRenter) {
            startActivity(new Intent(DashboardActivity.this, AllProductsForRenterActivity.class));
        } else if (id == R.id.nav_cancel_order) {
            startActivity(new Intent(DashboardActivity.this, CancelOrderActivity.class));
        } else if (id == R.id.nav_confirmed_order) {
            startActivity(new Intent(DashboardActivity.this, ConfirmedOrdersActivity.class));
        } else if (id == R.id.nav_pending_orders) {
            startActivity(new Intent(DashboardActivity.this, PendingOrdersActivity.class));
        } else if (id == R.id.nav_logout) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
