package com.vibrant.asp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.vibrant.asp.R;
import com.vibrant.asp.fragment.ImgViewFragment;

import java.util.HashMap;
import java.util.Map;

public class WalletRechargeActivity extends AppCompatActivity {
    private static final String TAG = "WalletRechargeActivity";
    TextView tvHeader;
    ImageView ivBack;
    Button btn_checkout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_recharge);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.wallet_recharge));
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar =findViewById(R.id.progress_bar);
        btn_checkout =findViewById(R.id.btn_checkout);


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleProgress(true);
             //   startActivity(new Intent(WalletRechargeActivity.this, PayTMActivity.class));
               /* ImgViewFragment fragment = new ImgViewFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());*/
            }
        });

   /* //For Paytm
        PaytmPGService Service = PaytmPGService.getStagingService();

        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "rxazcv89315285244163");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , "order1");
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "username@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "100.12");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        //PaytmOrder Order = new PaytmOrder(paramMap);*/
    }
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }
    public void toggleProgress(boolean isLoading) {
        if (isLoading)
            showProgress();
        else
            hideProgress();
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
