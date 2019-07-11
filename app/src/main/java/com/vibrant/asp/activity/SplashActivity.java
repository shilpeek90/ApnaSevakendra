package com.vibrant.asp.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.vibrant.asp.BuildConfig;
import com.vibrant.asp.R;
import org.jsoup.Jsoup;
import java.io.IOException;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class SplashActivity extends AppCompatActivity {
    public final int SPLASH_DISPLAY_LENGTH = 1500;
    TextView tvVersion;
    String newVersion = "";
    String versionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = findViewById(R.id.tvVersion);
        checkForNetwork();
    }

    private void checkForNetwork() {
        try {
            if (isInternetConnected(getApplicationContext())) {
                versionName = BuildConfig.VERSION_NAME;
                int versionCode = BuildConfig.VERSION_CODE;
                tvVersion.setText(getResources().getString(R.string.version) + " " + versionCode + " ( " + versionName + " )");
                versionChecker VersionChecker = new versionChecker();
                newVersion = VersionChecker.execute().get().toString();
                if (!versionName.equals(newVersion)) {
                    getUpdateApp();
                } else if (versionName.equals(newVersion)) {
                    if (isInternetConnected(getApplicationContext())) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getPreference(SplashActivity.this, "status") != null &&
                                        !getPreference(SplashActivity.this, "status").isEmpty()) {
                                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
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
            } else {
                showToast(this, getResources().getString(R.string.check_network));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class versionChecker extends AsyncTask<String, String, String> {
        String newVersion;
        @Override
        protected String doInBackground(String... params) {
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.vibrant.asp&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
    }

    private void getUpdateApp() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Please Update Your App")
                .setMessage("A new version of this app is available. Please update it")
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vibrant.asp&hl=en"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }
}
