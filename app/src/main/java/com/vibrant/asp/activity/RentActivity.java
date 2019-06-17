package com.vibrant.asp.activity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.gps.GPSTracker;

import static com.vibrant.asp.constants.Util.showToast;

public class RentActivity extends AppCompatActivity {
    private static final String TAG = "RentActivity";
    private boolean doubleBackToExitPressedOnce = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private double latitude;
    private double longitude;

    TextView tvHeader;
    Button btnLend,btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        init();
    }

    private void init() {
        tvHeader =findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.rent));

        btnLend =findViewById(R.id.btnLend);
        btnRent =findViewById(R.id.btnRent);

        //For click Listner
        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionGPS();
            }
        });

        btnLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentActivity.this,CameraActivity.class));
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
            new AlertDialog.Builder(RentActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_read_phone_state_rationale1))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(RentActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

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
        new AlertDialog.Builder(RentActivity.this)
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
            GPSTracker gpsTracker = new GPSTracker(RentActivity.this);
            String msg = gpsTracker.getGPS_Location();
            Log.d(TAG, "doPermissionGrantedStuffs:---" + msg);
            if (msg == null) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Location not found,Please move to open sky and try again")) {
                showDialog("GPS Location not found,Please move to open sky and try again");
            } else if (msg.equals("Your GPS module is disabled. Would you like to enable it ?")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RentActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
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
                    Intent intent = new Intent(RentActivity.this,AllProductActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RentActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
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
