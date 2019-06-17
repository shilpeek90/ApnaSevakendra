package com.vibrant.asp.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.vibrant.asp.R;

public class ViewImageActivity extends AppCompatActivity {
    private static final String TAG = "ViewImageActivity";
    String mImg1 = "";
    String mImg2 = "";
    TextView tvHeader;
    ImageView ivBack, image1, image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                mImg1 = bundle.getString("image1");
                mImg2 = bundle.getString("image2");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.vi_imag));
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(getApplicationContext()).load(mImg1)
                .apply(options)
                .into(image1);

        RequestOptions options2 = new RequestOptions()
                .centerCrop()
                .error(R.drawable.file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(getApplicationContext()).load(mImg2)
                .apply(options2)
                .into(image2);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}