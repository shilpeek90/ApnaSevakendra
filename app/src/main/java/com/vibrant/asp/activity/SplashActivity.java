package com.vibrant.asp.activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.vibrant.asp.BuildConfig;
import com.vibrant.asp.R;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    public final int SPLASH_DISPLAY_LENGTH = 1500;
    TextView tvVersion, tvMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvMess = findViewById(R.id.tvMess);
        tvVersion = findViewById(R.id.tvVersion);
        checkForNetwork();
    }

    private void checkForNetwork() {
        try {
            String versionName = BuildConfig.VERSION_NAME;
            int versionCode = BuildConfig.VERSION_CODE;
            tvVersion.setText(getResources().getString(R.string.version) + " " + versionCode + " ( " + versionName + " )");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isInternetConnected(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getPreference(SplashActivity.this, "status") != null && !getPreference(SplashActivity.this, "status").isEmpty()) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            showToast(this, getResources().getString(R.string.check_network));
        }
    }
}
