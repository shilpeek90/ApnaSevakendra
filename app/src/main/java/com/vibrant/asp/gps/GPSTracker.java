package com.vibrant.asp.gps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.vibrant.asp.R;


public final class GPSTracker implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	public boolean isGPSEnabled = false;

	// flag for network status
	private boolean isNetworkEnabled = false;

	// flag for GPS status
	private boolean canGetLocation = false;

	private Location location; // location
	private double latitude; // latitude
	private double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	private Activity activity;

	private Location loc;

	public GPSTracker(Context context, Activity activity) {
		this.mContext = context;
		this.activity = activity;

	}

	public GPSTracker(Context mContext) {
		this.mContext = mContext;
	}

	public String getGPS_Location() {
		String exp=null;
		try {
			locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
			// showDialog("GPS Enabled.");
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			showDialog("GPS Disabled." + ex.getMessage());
			exp = ex.getMessage();
		}
		if (isGPSEnabled) {

			try {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, this);
				locationManager.addGpsStatusListener(new Listener() {

					@Override
					public void onGpsStatusChanged(int event) {
						// TODO Auto-generated method stub
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}


			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			// Get Current Location
			Location myLocation = null;
			try {
				myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (myLocation == null) {
					myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			setLoc(myLocation);
			if (myLocation == null) {
				return "Location not found,Please move to open sky and try again";
			}else{
				return "Location Found";
			}
		} else {
			setLoc(null);

			return "Your GPS module is disabled. Would you like to enable it ?";

		}
	}



	public void showDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(mContext,
						android.R.style.Theme_DeviceDefault_Dialog));
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});
		AlertDialog alert = builder.create();
		//alert.setIcon(R.drawable.logodlg);
		alert.setTitle(mContext.getResources().getString(R.string.app_name));
		alert.show();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					try {
						location = null;
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("Network", "Network");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					try {
						location = null;
						if (location == null) {
							locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							Log.d("GPS Enabled", "GPS Enabled");
							if (locationManager != null) {
								location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								if (location != null) {
									latitude = location.getLatitude();
									longitude = location.getLongitude();

									Log.d("GPSSS", "getLocation: "+latitude+longitude);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}


}
