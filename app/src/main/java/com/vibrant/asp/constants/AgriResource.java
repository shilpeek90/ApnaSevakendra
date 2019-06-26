package com.vibrant.asp.constants;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class AgriResource extends Application {
    private static final String TAG = "AgriResource";
     FirebaseRemoteConfig firebaseRemoteConfig =null;
    private static Context context;
    private static AgriResource mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        /* Try catch to handle any runtime exception thrown by firebase API */
        try {
            /* Initialize firebase app : Done to avoid crashes due to IllegalStateException - Default FirebaseApp is not initialized */
            FirebaseApp.initializeApp(this);
            /* Start to fetch Remote config parameters */
            startConfigFetch();
        } catch (Exception e){
            e.printStackTrace();
        }

/*
        if(!FirebaseApp.getApps(this).isEmpty()) {
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        }*/
        // set in-app defaults
       /* Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.1");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.vibrant.asp&hl=en");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });*/
    }

    private void startConfigFetch() {
        try {
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        /* If we don't get an instance of Firebase remote config, then do nothing */
        if (firebaseRemoteConfig == null){
            return;
        }

        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.1");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.vibrant.asp&hl=en");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }
    public static Context getInstance() {
        return mInstance;
    }

}
