package com.vibrant.asp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import android.webkit.WebView;
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
import com.vibrant.asp.adapter.GetOrdersForRenteeAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.gps.GPSTracker;
import com.vibrant.asp.model.GetOrdersForRentee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardActivity";
    private boolean doubleBackToExitPressedOnce = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private double latitude;
    private double longitude;
    TextView tvName, tvWallet;
    ProgressDialog pd;
    LinearLayout rlayLend, rlRent, llrow1, llrow2, llrow3, llrow4,llaySale,llConfirmedOrder, llayCancelOrder, llayPendingOrder, rlWalletRecharge, rlHelp;
    Animation leftSide, rightSide;

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

        //tvWallet.setText(getResources().getString(R.string.wallet) + " " + "0");

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
        llayCancelOrder = findViewById(R.id.llayCancelOrder);
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

        //For Click Listener
        rlRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionGPS();
            }
        });

        rlayLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CameraActivity.class));
            }
        });

        llayCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CancelOrderActivity.class));
            }
        });
        llConfirmedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this,"Coming Soon");

            }
        });

        llayPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this,"Coming Soon");
            }
        });

        llaySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this,"Coming Soon");
            }
        });

        rlWalletRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this,"Coming Soon");
            }
        });
        rlHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,HelpActivity.class));
               // showToast(DashboardActivity.this,"Coming Soon");
            }
        });
    }

    private void getPermissionGPS() {
        // Check if the ACCESS_FINE_LOCATION permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ACCESS_FINE_LOCATION permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_read_phone_state_rationale1))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

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
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do something here
                    }
                })
                .show();
    }

    public void doPermissionGrantedStuffs() {
        try {
            GPSTracker gpsTracker = new GPSTracker(DashboardActivity.this);
            String msg = gpsTracker.getGPS_Location();
            Log.d(TAG, "doPermissionGrantedStuffs:---" + msg);
            if (msg == null) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Location not found,Please move to open sky and try again")) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Your GPS module is disabled. Would you like to enable it ?")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DashboardActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
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
                    Intent intent = new Intent(DashboardActivity.this, AllProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mLatCurrent", String.valueOf(latitude));
                    bundle.putString("mLngCurrent", String.valueOf(longitude));
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DashboardActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
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


    private void GetRenterWalletBalance() {
        String url = Cons.GET_RENTER_WALLET_BALANCE;
        pd = ProgressDialog.show(DashboardActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteeId = getPreference(DashboardActivity.this, "Id");
            if (mRenteeId != null) {
                jsonObject.put("RenterId", mRenteeId);
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
            // super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_OrdersForRenter) {
            startActivity(new Intent(DashboardActivity.this, OrdersForRenterActivity.class));
        } else if (id == R.id.nav_GetOrdersForRentee) {
            startActivity(new Intent(DashboardActivity.this, OrdersForRenteeActivity.class));
        } else if (id == R.id.nav_AllProductsForRenter) {
            startActivity(new Intent(DashboardActivity.this, AllProductsForRenterActivity.class));
        } else if (id == R.id.nav_logout) {

        } /* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
