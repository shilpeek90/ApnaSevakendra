package com.vibrant.asp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibrant.asp.R;

public class HelpActivity extends AppCompatActivity {
    private static final String TAG = "HelpActivity";
    TextView tvHeader;
    ImageView ivBack;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.help));
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView =findViewById(R.id.webView);
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/help.html");

    }
    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading: "+">>>>>>>>>>>>>>>.");
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
