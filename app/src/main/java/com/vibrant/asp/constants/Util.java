package com.vibrant.asp.constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vibrant.asp.R;

import static android.content.Context.MODE_PRIVATE;

public class Util {


    private static final String MY_PREFS_NAME = "EPICKDROP";

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public static void setPreference(Context mContext, String key,String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }



    public static String getPreference(Context mContext,String key){
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE );
        View f = activity.getCurrentFocus();
        if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
            imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
        else
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }

    public static void showToast(Activity activity,String message){
        LayoutInflater li = activity.getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.custom_toast_layout,(ViewGroup)activity.findViewById(R.id.custom_toast_layout));
        TextView textView =layout.findViewById(R.id.custom_toast_message);
        textView.setText(message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,50, 50);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();

    }


}

