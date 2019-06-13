package com.vibrant.asp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.SubscriptionAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.gps.GPSTracker;
import com.vibrant.asp.model.StateModel;
import com.vibrant.asp.model.SubscriptionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.setPreference;
import static com.vibrant.asp.constants.Util.showToast;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";
    private boolean doubleBackToExitPressedOnce = false;
    TextView tvHeader;
    EditText editResName, editRent;
    Button btnChoose1, btnChoose2, btnSubmit, btnCancel, btnCancel2;
    ImageView ivImage1, ivImage2;
    LinearLayout lLayRentCatgry, lLayGallery, lLayCamera, lLayRemove, lLayGallery2, lLayCamera2, lLayRemove2;
    Spinner spinnerSub;
    List<SubscriptionModel> subArrayList = new ArrayList<>();
    List<SubscriptionModel> subArrayList1 = new ArrayList<>();
    SubscriptionAdapter subAdapter;
    String selectedSubId = "";
    String error_message = "";

    public static final int PICK_IMAGE_GALLERY = 1;
    public static final int PICK_IMAGE_GALLERY_2 = 2;
    //  private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE_2 = 200;
    BottomSheetDialog dialog;
    BottomSheetDialog dialog2;
    private static final int CAMERA_PIC_REQUEST = 100;

    private final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA};
    private Uri uri;
    private double latitude;
    private double longitude;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        hideKeyboard(DashboardActivity.this);
        gpsTracker = new GPSTracker(DashboardActivity.this);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.dashboard));
        editResName = findViewById(R.id.editResName);
        editRent = findViewById(R.id.editRent);
        ivImage1 = findViewById(R.id.ivImage1);
        ivImage2 = findViewById(R.id.ivImage2);
        btnChoose1 = findViewById(R.id.btnChoose1);
        btnChoose2 = findViewById(R.id.btnChoose2);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinnerSub = findViewById(R.id.spinnerSub);

        lLayRentCatgry = findViewById(R.id.lLayRentCatgry);

        lLayRentCatgry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(DashboardActivity.this, "Category Clicked");
            }
        });

        btnChoose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet();
            }
        });

        btnChoose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet2();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    if (Validation()) {
                        Location location = gpsTracker.getLocation();
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("Current-----", "Dash " + latitude + ">>>>>>>>>>" + longitude);
                            getUploadProduct();
                        }
                    } else {
                        showToast(DashboardActivity.this, error_message);
                    }
                } else {
                    showToast(DashboardActivity.this, getResources().getString(R.string.check_network));
                }

            }
        });

        getSubscription();
        spinnerSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SubscriptionModel subModel = subAdapter.getItem(position);
                    selectedSubId = subModel.getSubID();
                    Log.d(TAG, "onItemSelected: " + subModel.getSubID());
                    Log.d(TAG, "onItemSelected: " + subModel.getSubName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean Validation() {
        if (TextUtils.isEmpty(editResName.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_re_name);
            return false;
        } else if (TextUtils.isEmpty(editRent.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_rate);
            return false;
        } else {
            return true;
        }
    }


    private void getUploadProduct() {
        String url = Cons.GET_UPLOAD_PRODUCT;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("UserId", "1");
            jsonObject.put("ProductName", editResName.getText().toString().trim());
            jsonObject.put("Description", "");
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
            jsonObject.put("ImageBase64String1", "");
            jsonObject.put("Extension", "");
            jsonObject.put("SubscriptionId", selectedSubId);
            jsonObject.put("Rate", editRent.getText().toString().trim());


            Log.d(TAG, "getState: " + jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equals("true")) {
                        editResName.setText("");
                        editRent.setText("");
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showToast(DashboardActivity.this, "Please Check Details");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(DashboardActivity.this, "Something went wrong");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }

    private void getSubscription() {
        String url = Cons.GET_SUBSCRIPTION;
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                subArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            SubscriptionModel stateModel = new SubscriptionModel();
                            stateModel.setSubID(jObj.getString("SubId"));
                            stateModel.setSubName(jObj.getString("SubName"));
                            subArrayList.add(stateModel);
                        }

                        if (jsonArray.length() > 0) {
                            SubscriptionModel model = new SubscriptionModel();
                            model.setSubName("Please Select");
                            subArrayList1.add(0, model);
                            subArrayList1.addAll(subArrayList);
                            subAdapter = new SubscriptionAdapter(getApplicationContext(), subArrayList1);
                            spinnerSub.setAdapter(subAdapter);
                        }
                    } else {
                        showToast(DashboardActivity.this, "Data not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(DashboardActivity.this, "Something went wrong");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);


    }

    private void getBottomSheet2() {
        View view = getLayoutInflater().inflate(R.layout.camera_bottom_sheet2, null);
        dialog2 = new BottomSheetDialog(this);
        dialog2.setContentView(view);
        lLayGallery2 = dialog2.findViewById(R.id.lLayGallery2);
        lLayCamera2 = dialog2.findViewById(R.id.lLayCamera2);
        lLayRemove2 = dialog2.findViewById(R.id.lLayRemove2);
        btnCancel2 = dialog2.findViewById(R.id.btnCancel2);

        lLayGallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary2();
            }
        });

        lLayCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission2();
            }
        });

        lLayRemove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage2.setImageDrawable(getResources().getDrawable(R.drawable.file));
                Toast.makeText(DashboardActivity.this, "Suceessfully Removed photo", Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
            }
        });
        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void getPermission2() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // CAMERA permission has not been granted.
            requestReadPhoneStatePermission2();
        } else {
            // CAMERA permission is already been granted.
            doPermissionGrantedStuffs2();
        }
    }


    private void getBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.camera_bottom_sheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        lLayGallery = dialog.findViewById(R.id.lLayGallery);
        lLayCamera = dialog.findViewById(R.id.lLayCamera);
        lLayRemove = dialog.findViewById(R.id.lLayRemove);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        lLayGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

        lLayCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });


        /* if(checkAndRequestPermissions()){
                    takePhotoFromCamera();
                }*/


        lLayRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage1.setImageDrawable(getResources().getDrawable(R.drawable.file));
                Toast.makeText(DashboardActivity.this, "Suceessfully Removed photo", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
    }


    private void choosePhotoFromGallary2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_2);
    }


   /* private void takePhotoFromCamera() {
        File file = new File( Environment.getExternalStorageDirectory()+ "/profileimg.jpg");
        Log.d ( TAG , "takePhotoFromCamera: "+file );
        ///Package
        //uri = FileProvider.getUriForFile(this, "com.demoproject.Custprovider", file );
        uri = FileProvider.getUriForFile(this, "com.vibrant.asp.Custprovider", file );
        Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri );
        cameraIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

*/

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // CAMERA permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // CAMERA permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }


    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_need_allowed))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }


    private void requestReadPhoneStatePermission2() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_need_allowed))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_2);
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                    alertAlert(getString(R.string.permissions_granted));
                    doPermissionGrantedStuffs();
                } else {
                    alertAlert(getString(R.string.permissions_not_granted));
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_2) {
            Log.d(TAG, "onRequestPermissionsResult: " + "second----->>>>>>");
            if (grantResults.length > 0) {
                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                    alertAlert(getString(R.string.permissions_granted));
                    doPermissionGrantedStuffs2();
                } else {
                    alertAlert(getString(R.string.permissions_not_granted));
                }
            }

        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(DashboardActivity.this)
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
        Log.d(TAG, "doPermissionGrantedStuffs: " + "clicled");
        /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PERMISSION_REQUEST_CODE);
        */
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE);
        }

    }

    public void doPermissionGrantedStuffs2() {
        Log.d(TAG, "doPermissionGrantedStuffs2: " + "clicled");
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_2);
        }
    }


  /*  private boolean checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String prem : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, prem) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(prem);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ALL);
            return false;
        }
        return true;
    }

    public AlertDialog showDialog(String title, String this_app_needs_permission, String s1, DialogInterface.OnClickListener onClickListener, String s2, DialogInterface.OnClickListener onClickListener1, boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(b);
        builder.setMessage(this_app_needs_permission);
        builder.setPositiveButton(s1, onClickListener);
        builder.setNegativeButton(s2, onClickListener1);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ivImage1.setImageBitmap(bitmap);
                    dialog.dismiss();
                    Log.d(TAG, "onActivityResult: " + bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(DashboardActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Log.d(TAG, "onActivityResult: camera" + imageBitmap);
                ivImage1.setImageBitmap(imageBitmap);
                dialog.dismiss();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_2 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ivImage2.setImageBitmap(bitmap);
                    dialog2.dismiss();
                    Log.d(TAG, "onActivityResult: " + bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(DashboardActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Log.d(TAG, "onActivityResult: camera" + imageBitmap);
                ivImage2.setImageBitmap(imageBitmap);
                dialog2.dismiss();
            }
        }
//        if (requestCode == CAMERA_PIC_REQUEST ) {
//            try {
//              Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                ivImage1.setImageBitmap(bitmap);
//                Log.d ( TAG , "onActivityResult: "+bitmap );
//                dialog.dismiss ();
//            } catch ( IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    /*  Uri imageUri = data.getData();
            Log.d(TAG, "onActivityResult: " + imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivImage1.setImageBitmap(bitmap);
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
}
