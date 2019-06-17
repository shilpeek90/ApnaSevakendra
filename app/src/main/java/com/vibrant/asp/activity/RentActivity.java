package com.vibrant.asp.activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.vibrant.asp.R;
import static com.vibrant.asp.constants.Util.showToast;

public class RentActivity extends AppCompatActivity {
    private static final String TAG = "RentActivity";
    private boolean doubleBackToExitPressedOnce = false;
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
                startActivity(new Intent(RentActivity.this,AllProductActivity.class));
            }
        });

        btnLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentActivity.this,CameraActivity.class));
            }
        });
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
