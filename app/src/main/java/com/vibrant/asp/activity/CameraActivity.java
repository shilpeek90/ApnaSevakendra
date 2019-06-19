package com.vibrant.asp.activity;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.vibrant.asp.constants.ImageFilePath;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.gps.GPSTracker;
import com.vibrant.asp.model.SubscriptionModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
   // private boolean doubleBackToExitPressedOnce = false;
    ProgressDialog pd;
    TextView tvHeader;
    EditText editResName, editRent,editDiscription;
    Button btnChoose1, btnChoose2, btnSubmit, btnCancel, btnCancel2;
    ImageView ivImage1, ivImage2;
    LinearLayout lLayRentCatgry, lLayGallery, lLayCamera, lLayRemove, lLayGallery2, lLayCamera2, lLayRemove2;
    Spinner spinnerSub;
    List<SubscriptionModel> subArrayList = new ArrayList<>();
    List<SubscriptionModel> subArrayList1 = new ArrayList<>();
    SubscriptionAdapter subAdapter;
    String selectedSubId = "";
    String error_message = "";
    String mConvertedImg1;
    String mConvertedImg2;
    String imgExtension;
    public static final int PICK_IMAGE_GALLERY_1 = 1;
    public static final int PICK_IMAGE_GALLERY_2 = 2;
    private static final int PERMISSION_REQUEST_CODE_1 = 100;
    private static final int PERMISSION_REQUEST_CODE_2 = 200;
    BottomSheetDialog dialog;
    BottomSheetDialog dialog2;
    private double latitude;
    private double longitude;
    GPSTracker gpsTracker;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        hideKeyboard(CameraActivity.this);
        gpsTracker = new GPSTracker(CameraActivity.this);

        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.camera));
        ivBack =findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editResName = findViewById(R.id.editResName);
        editRent = findViewById(R.id.editRent);
        editDiscription = findViewById(R.id.editDiscription);
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
                showToast(CameraActivity.this, "Category Clicked");
            }
        });

        btnChoose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet1();
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
                            getUploadProduct();
                        }
                    } else {
                        showToast(CameraActivity.this, error_message);
                    }
                } else {
                    showToast(CameraActivity.this, getResources().getString(R.string.check_network));
                }
            }
        });

        //For Subscription
        getSubscription();
        //For Spinner Click Listener
        spinnerSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SubscriptionModel subModel = subAdapter.getItem(position);
                    selectedSubId = subModel.getSubID();
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
        }
        else if (TextUtils.isEmpty(editDiscription.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_dis);
            return false;
        }else {
            return true;
        }
    }

    private void getUploadProduct() {
        String url = Cons.GET_UPLOAD_PRODUCT;
        pd = ProgressDialog.show(CameraActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", "1");
            jsonObject.put("ProductName", editResName.getText().toString().trim());
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
            jsonObject.put("ImageBase64String1", mConvertedImg1);
            jsonObject.put("ImageBase64String2", mConvertedImg2);
            jsonObject.put("Extension", imgExtension);
            jsonObject.put("SubscriptionId", selectedSubId);
            jsonObject.put("Rate", editRent.getText().toString().trim());
            jsonObject.put("Description", editDiscription.getText().toString().trim());
            Log.d(TAG, "getUploadProduct: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("d");
                    if (status.equals("true")) {
                        editResName.setText("");
                        editRent.setText("");
                        editDiscription.setText("");
                        showToast(CameraActivity.this, "Successfully Product Uploaded");
                        startActivity(new Intent(CameraActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showToast(CameraActivity.this, "Something went wrong");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                pd.dismiss();
                showToast(CameraActivity.this, "Something went wrong");
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
                        showToast(CameraActivity.this, "Data not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(CameraActivity.this, "Something went wrong");
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
                showToast(CameraActivity.this, "Successfully Removed photo");
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
            requestCameraPermission2();
        } else {
            // CAMERA permission is already been granted.
            doPermissionGrantedCamera2();
        }
    }

    private void getBottomSheet1() {
        View view = getLayoutInflater().inflate(R.layout.camera_bottom_sheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        lLayGallery = dialog.findViewById(R.id.lLayGallery);
        lLayCamera = dialog.findViewById(R.id.lLayCamera);
        lLayRemove = dialog.findViewById(R.id.lLayRemove);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        //Click for gallery Image
        lLayGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });
        //click for camera Image 1
        lLayCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionCamera1();   //For permission
            }
        });

        lLayRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage1.setImageDrawable(getResources().getDrawable(R.drawable.file));
                showToast(CameraActivity.this, "Successfully Removed photo");
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_1);
    }

    private void choosePhotoFromGallary2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_2);
    }

    private void getPermissionCamera1() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // CAMERA permission has not been granted.
            requestCameraPermission1();
        } else {
            // CAMERA permission is already been granted.
            doPermissionGrantedCamera1();
        }
    }

    private void requestCameraPermission1() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(CameraActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_need_allowed))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_1);
                        }
                    })
                    .show();
        } else {
            // CAMERA permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_1);
        }
    }

    private void requestCameraPermission2() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(CameraActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_need_allowed))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_2);
                        }
                    })
                    .show();
        } else {
            // CAMERA permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_2);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //For Camera Image 1
        if (requestCode == PERMISSION_REQUEST_CODE_1) {
            if (grantResults.length > 0) {
                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted
                    alertAlert(getString(R.string.permissions_granted));
                    doPermissionGrantedCamera1();
                } else {
                    alertAlert(getString(R.string.permissions_not_granted));
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_2) {
            //For Camera Image 2
            if (grantResults.length > 0) {
                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted
                    alertAlert(getString(R.string.permissions_granted));
                    doPermissionGrantedCamera2();
                } else {
                    alertAlert(getString(R.string.permissions_not_granted));
                }
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(CameraActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do something here
                    }
                })
                .show();
    }

    //For Camera Image 1
    public void doPermissionGrantedCamera1() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_1);
        }
    }

    //For Camera Image 2
    public void doPermissionGrantedCamera2() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for gallery Image 1
        if (requestCode == PICK_IMAGE_GALLERY_1 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                final Uri imageUri = data.getData();
                String path = ImageFilePath.getPath(CameraActivity.this, imageUri);
                Log.d(TAG, "onActivityResult: "+path);
                imgExtension = path.substring(path.lastIndexOf("."));
                if (imgExtension.equalsIgnoreCase(".jpg")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    mConvertedImg1 = convertToBase64(resizedBitmap);
                    ivImage1.setImageBitmap(bitmap);
                } else {
                    showToast(CameraActivity.this, "Please select jpg image only");
                }
                dialog.dismiss();
            } else {
                showToast(CameraActivity.this, "You haven't picked Image");
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_1 && resultCode == RESULT_OK) {
            //for camera Image 1
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUriImg1 = getImageUri(getApplicationContext(), imageBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFileImg = new File(getRealPathFromURI(tempUriImg1));
                String fileEx = String.valueOf(finalFileImg);
                imgExtension = fileEx.substring(fileEx.lastIndexOf("."));
                Bitmap bitmap = BitmapFactory.decodeFile(fileEx);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                mConvertedImg1 = convertToBase64(resizedBitmap);

                Log.d(TAG, "onActivityResult:" + finalFileImg);
                Log.d(TAG, "onActivityResult:" + imgExtension);
                Log.d(TAG, "onActivityResult:" + tempUriImg1);
                Log.d(TAG, "onActivityResult:" + mConvertedImg1);

                ivImage1.setImageBitmap(imageBitmap);
                dialog.dismiss();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_2 && resultCode == RESULT_OK) {
            //For Gallery Image 2
            if (data != null && data.getData() != null) {
                final Uri imageUri1 = data.getData();
                String imagePath = ImageFilePath.getPath(CameraActivity.this, imageUri1);
                imgExtension = imagePath.substring(imagePath.lastIndexOf("."));
                if (imgExtension.equalsIgnoreCase(".jpg")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    mConvertedImg2 = convertToBase64(resizedBitmap);
                    ivImage2.setImageBitmap(bitmap);
                } else {
                    showToast(CameraActivity.this, "Please select jpg image only");
                }
                dialog2.dismiss();
            } else {
                showToast(CameraActivity.this, "You haven't picked Image");
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            //For Camera Image 2
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUriImg2 = getImageUri(getApplicationContext(), imageBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFileImg2 = new File(getRealPathFromURI(tempUriImg2));
                String fileEx = String.valueOf(finalFileImg2);
                imgExtension = fileEx.substring(fileEx.lastIndexOf("."));
                Bitmap bitmap = BitmapFactory.decodeFile(fileEx);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                mConvertedImg2 = convertToBase64(resizedBitmap);

                Log.d(TAG, "onActivityResult:" + finalFileImg2);
                Log.d(TAG, "onActivityResult:" + imgExtension);
                Log.d(TAG, "onActivityResult:" + tempUriImg2);
                Log.d(TAG, "onActivityResult:" + mConvertedImg1);

                ivImage2.setImageBitmap(imageBitmap);
                dialog2.dismiss();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // For Image path
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    //For Converting Image into Base64
    private String convertToBase64(Bitmap bitmap) {
        String encodedStr = "";
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        String encodedImage = Base64.encodeToString(ba, Base64.NO_WRAP);

        byte[] utf8Bytes = new byte[1024];
        try {
            utf8Bytes = encodedImage.getBytes("UTF-8");
            encodedStr = new String(utf8Bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedStr;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    /* @Override
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
    }*/
}
