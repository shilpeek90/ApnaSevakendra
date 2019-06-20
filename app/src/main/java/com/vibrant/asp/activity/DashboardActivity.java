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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.gps.GPSTracker;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.showToast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardActivity";
    private boolean doubleBackToExitPressedOnce = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private double latitude;
    private double longitude;
    TextView tvName, tvWallet;
    LinearLayout rlayLend, rlRent, llrow1, llrow2, llrow3, llConfirmedOrder, llayMutureOrder, llayPendingOrder;
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

        tvWallet.setText(getResources().getString(R.string.wallet) + " " + "0");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        init();
    }

    private void init() {
        rlayLend = findViewById(R.id.rlayLend);
        rlRent = findViewById(R.id.rlRent);
        llrow1 = findViewById(R.id.llrow1);
        llrow2 = findViewById(R.id.llrow2);
        llrow3 = findViewById(R.id.llrow3);
        llayMutureOrder = findViewById(R.id.llayMutureOrder);
        llConfirmedOrder = findViewById(R.id.llConfirmedOrder);
        llayPendingOrder = findViewById(R.id.llayPendingOrder);
        //for animation
        llrow1.startAnimation(rightSide);
        llrow2.startAnimation(leftSide);
        llrow3.startAnimation(rightSide);

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

        llayMutureOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llConfirmedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llayPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            startActivity(new Intent(DashboardActivity.this, GetOrdersForRenteeActivity.class));
        } else if (id == R.id.nav_logout) {

        } /*else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
