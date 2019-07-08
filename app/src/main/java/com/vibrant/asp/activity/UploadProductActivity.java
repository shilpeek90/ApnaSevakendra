package com.vibrant.asp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import com.vibrant.asp.adapter.CameraQuantityAdapter;
import com.vibrant.asp.adapter.SubscriptionAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ImageFilePath;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.constants.Util;
import com.vibrant.asp.gps.GPSTracker1;
import com.vibrant.asp.model.CameraQuantityModel;
import com.vibrant.asp.model.SubscriptionModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.vibrant.asp.constants.Util.getPreference;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;
import static com.vibrant.asp.constants.Util.showToast;

public class UploadProductActivity extends AppCompatActivity {
    private static final String TAG = "UploadProductActivity";
    // private boolean doubleBackToExitPressedOnce = false;
    ProgressDialog pd;
    TextView tvHeader;
    EditText editResName, editRent, editDiscription;
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
    Spinner spinnerQuantity;
    List<CameraQuantityModel> quantityArray = new ArrayList<>();
    String selectedQuantity = "";
    CameraQuantityAdapter adapter;
    ImageView ivBack;
    String mResponse = "{\"d\":[  \n" +
            "    {\"quantity\":\"1\"},  \n" +
            "    {\"quantity\":\"2\"},  \n" +
            "    {\"quantity\":\"3\"},  \n" +
            "    {\"quantity\":\"4\"},  \n" +
            "    {\"quantity\":\"5\"},  \n" +
            "    {\"quantity\":\"6\"},  \n" +
            "    {\"quantity\":\"7\"},  \n" +
            "    {\"quantity\":\"8\"},  \n" +
            "    {\"quantity\":\"9\"},  \n" +
            "    {\"quantity\":\"10\"},  \n" +
            "    {\"quantity\":\"11\"},  \n" +
            "    {\"quantity\":\"12\"},  \n" +
            "    {\"quantity\":\"13\"},  \n" +
            "    {\"quantity\":\"14\"},  \n" +
            "    {\"quantity\":\"15\"},  \n" +
            "    {\"quantity\":\"16\"},  \n" +
            "    {\"quantity\":\"17\"},  \n" +
            "    {\"quantity\":\"18\"},  \n" +
            "    {\"quantity\":\"19\"},  \n" +
            "    {\"quantity\":\"20\"},  \n" +
            "    {\"quantity\":\"21\"},  \n" +
            "    {\"quantity\":\"22\"},  \n" +
            "    {\"quantity\":\"23\"},  \n" +
            "    {\"quantity\":\"24\"},  \n" +
            "    {\"quantity\":\"25\"},  \n" +
            "    {\"quantity\":\"26\"},  \n" +
            "    {\"quantity\":\"27\"},  \n" +
            "    {\"quantity\":\"28\"},  \n" +
            "    {\"quantity\":\"29\"},  \n" +
            "    {\"quantity\":\"30\"},  \n" +
            "    {\"quantity\":\"31\"},  \n" +
            "    {\"quantity\":\"32\"},  \n" +
            "    {\"quantity\":\"33\"},  \n" +
            "    {\"quantity\":\"34\"},  \n" +
            "    {\"quantity\":\"35\"},  \n" +
            "    {\"quantity\":\"36\"},  \n" +
            "    {\"quantity\":\"37\"},  \n" +
            "    {\"quantity\":\"38\"},  \n" +
            "    {\"quantity\":\"39\"},  \n" +
            "    {\"quantity\":\"40\"},  \n" +
            "    {\"quantity\":\"41\"},  \n" +
            "    {\"quantity\":\"42\"},  \n" +
            "    {\"quantity\":\"43\"},  \n" +
            "    {\"quantity\":\"44\"},  \n" +
            "    {\"quantity\":\"45\"},  \n" +
            "    {\"quantity\":\"46\"},  \n" +
            "    {\"quantity\":\"47\"},  \n" +
            "    {\"quantity\":\"48\"},  \n" +
            "    {\"quantity\":\"49\"},  \n" +
            "    {\"quantity\":\"50\"},  \n" +
            "    {\"quantity\":\"51\"},  \n" +
            "    {\"quantity\":\"52\"},  \n" +
            "    {\"quantity\":\"53\"},  \n" +
            "    {\"quantity\":\"54\"},  \n" +
            "    {\"quantity\":\"55\"},  \n" +
            "    {\"quantity\":\"56\"},  \n" +
            "    {\"quantity\":\"57\"},  \n" +
            "    {\"quantity\":\"58\"},  \n" +
            "    {\"quantity\":\"59\"},  \n" +
            "    {\"quantity\":\"60\"},  \n" +
            "    {\"quantity\":\"61\"},  \n" +
            "    {\"quantity\":\"62\"},  \n" +
            "    {\"quantity\":\"63\"},  \n" +
            "    {\"quantity\":\"64\"},  \n" +
            "    {\"quantity\":\"65\"},  \n" +
            "    {\"quantity\":\"66\"},  \n" +
            "    {\"quantity\":\"67\"},  \n" +
            "    {\"quantity\":\"68\"},  \n" +
            "    {\"quantity\":\"69\"},  \n" +
            "    {\"quantity\":\"70\"},  \n" +
            "    {\"quantity\":\"71\"},  \n" +
            "    {\"quantity\":\"72\"},  \n" +
            "    {\"quantity\":\"73\"},  \n" +
            "    {\"quantity\":\"74\"},  \n" +
            "    {\"quantity\":\"75\"},  \n" +
            "    {\"quantity\":\"76\"},  \n" +
            "    {\"quantity\":\"78\"},  \n" +
            "    {\"quantity\":\"79\"},  \n" +
            "    {\"quantity\":\"80\"},  \n" +
            "    {\"quantity\":\"81\"},  \n" +
            "    {\"quantity\":\"82\"},  \n" +
            "    {\"quantity\":\"83\"},  \n" +
            "    {\"quantity\":\"84\"},  \n" +
            "    {\"quantity\":\"85\"},  \n" +
            "    {\"quantity\":\"86\"},  \n" +
            "    {\"quantity\":\"87\"},  \n" +
            "    {\"quantity\":\"88\"},  \n" +
            "    {\"quantity\":\"89\"},  \n" +
            "    {\"quantity\":\"90\"},  \n" +
            "    {\"quantity\":\"91\"},  \n" +
            "    {\"quantity\":\"92\"},  \n" +
            "    {\"quantity\":\"93\"},  \n" +
            "    {\"quantity\":\"94\"},  \n" +
            "    {\"quantity\":\"95\"},  \n" +
            "    {\"quantity\":\"96\"},  \n" +
            "    {\"quantity\":\"97\"},  \n" +
            "    {\"quantity\":\"98\"},  \n" +
            "    {\"quantity\":\"99\"},  \n" +
            "    {\"quantity\":\"100\"} \n" +
            "]}  ";

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String isClicked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_updated);
        hideKeyboard(UploadProductActivity.this);
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.camera));
        ivBack = findViewById(R.id.ivBack);
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
        spinnerQuantity = findViewById(R.id.spinnerQuantity);
        lLayRentCatgry = findViewById(R.id.lLayRentCatgry);
        lLayRentCatgry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(UploadProductActivity.this, "Category Clicked");
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

        getQuantity();
        spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CameraQuantityModel cameraQuantityModel = adapter.getItem(position);
                selectedQuantity = cameraQuantityModel.getQuantity();
                Log.d(TAG, "onItemSelected: " + cameraQuantityModel.getQuantity());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    if (Validation()) {
                        try {
                            GPSTracker1 gpsTracker = new GPSTracker1(UploadProductActivity.this);
                            // check if GPS enabled
                            if (gpsTracker.canGetLocation()) {
                                latitude = gpsTracker.getLatitude();
                                longitude = gpsTracker.getLongitude();
                                if (mConvertedImg1 != null && !mConvertedImg1.isEmpty()) {
                                    if (mConvertedImg2 != null &&!mConvertedImg2.isEmpty()) {
                                        getUploadProduct();
                                    } else {
                                        showToast(UploadProductActivity.this, "Please select image2");
                                    }
                                } else {
                                    showToast(UploadProductActivity.this, "Please select image1");
                                }
                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gpsTracker.showSettingsAlert();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast(UploadProductActivity.this, error_message);
                    }
                } else {
                    showToast(UploadProductActivity.this, getResources().getString(R.string.check_network));
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

    private void getQuantity() {
        try {
            JSONObject jsonObject = new JSONObject(mResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("d");
            quantityArray.clear();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CameraQuantityModel cameraQuantityModel = new CameraQuantityModel();
                    cameraQuantityModel.setQuantity(jsonArray.getJSONObject(i).getString("quantity"));
                    quantityArray.add(cameraQuantityModel);
                }
                if (quantityArray.size() > 0) {
                    adapter = new CameraQuantityAdapter(getApplicationContext(), quantityArray);
                    spinnerQuantity.setAdapter(adapter);
                    // rangeAdapter.notifyDataSetChanged();
                }
            } else {
                showToast(UploadProductActivity.this, "Data not found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean Validation() {
        if (TextUtils.isEmpty(editResName.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_re_name);
            return false;
        } else if (TextUtils.isEmpty(editRent.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_rate);
            return false;
        } else if (Patterns.PHONE.matcher(editRent.getText().toString().trim()).matches()) {
            error_message = getString(R.string.please_enter_num);
            return false;
        } else if (TextUtils.isEmpty(editDiscription.getText().toString().trim())) {
            error_message = getString(R.string.please_enter_dis);
            return false;
        } else {
            return true;
        }
    }

    private void getUploadProduct() {
        String url = Cons.GET_UPLOAD_PRODUCT;
        pd = ProgressDialog.show(UploadProductActivity.this, "Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            String mRenteerId = getPreference(UploadProductActivity.this, "renterId");
            if (mRenteerId != null) {
                jsonObject.put("UserId", mRenteerId);
            }
            //jsonObject.put("UserId", "1");
            jsonObject.put("ProductName", editResName.getText().toString().trim());
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
            jsonObject.put("ImageBase64String1", mConvertedImg1);
            jsonObject.put("ImageBase64String2", mConvertedImg2);
            jsonObject.put("Extension", imgExtension);
            jsonObject.put("SubscriptionId", selectedSubId);
            jsonObject.put("Rate", editRent.getText().toString().trim());
            jsonObject.put("Description", editDiscription.getText().toString().trim());
            jsonObject.put("Quantity", selectedQuantity);
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
                        showToast(UploadProductActivity.this, "Successfully Product Uploaded");
                        startActivity(new Intent(UploadProductActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        showToast(UploadProductActivity.this, "Something went wrong");
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
                showToast(UploadProductActivity.this, "Something went wrong");
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
                            model.setSubName("Please select subscription");
                            subArrayList1.add(0, model);
                            subArrayList1.addAll(subArrayList);
                            subAdapter = new SubscriptionAdapter(getApplicationContext(), subArrayList1);
                            spinnerSub.setAdapter(subAdapter);
                        }
                    } else {
                        showToast(UploadProductActivity.this, "Data not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                showToast(UploadProductActivity.this, "Something went wrong");
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
                if (Util.checkRequestPermiss(getApplicationContext(), UploadProductActivity.this)) {
                    isClicked = "camera2";
                    doPermissionGranted();
                }
            }
        });

        lLayRemove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage2.setImageDrawable(getResources().getDrawable(R.drawable.file));
                mConvertedImg2 ="";
                showToast(UploadProductActivity.this, "Successfully Removed photo");
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
                if (Util.checkRequestPermiss(getApplicationContext(), UploadProductActivity.this)) {
                    Log.d(TAG, "onClick: " + "permission already granted");
                    isClicked = "camera1";
                    doPermissionGranted();
                }
            }
        });

        lLayRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage1.setImageDrawable(getResources().getDrawable(R.drawable.file));
                mConvertedImg1="";
                showToast(UploadProductActivity.this, "Successfully Removed photo");
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

    private void doPermissionGranted() {
        if (isClicked.equals("camera1")) {
            Log.d(TAG, "doPermissionGranted: " + "1 clicked");
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_1);
            }
        } else if (isClicked.equals("camera2")) {
            Log.d(TAG, "doPermissionGranted: " + "2 clicked");
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_2);
            }
        }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for gallery Image 1
        if (requestCode == PICK_IMAGE_GALLERY_1 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                final Uri imageUri = data.getData();
                String path = ImageFilePath.getPath(UploadProductActivity.this, imageUri);
                Log.d(TAG, "onActivityResult: " + path);
                imgExtension = path.substring(path.lastIndexOf("."));
                if (imgExtension.equalsIgnoreCase(".jpg")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    mConvertedImg1 = convertToBase64(resizedBitmap);
                    ivImage1.setImageBitmap(bitmap);
                } else {
                    showToast(UploadProductActivity.this, "Please select jpg image only");
                }
                dialog.dismiss();
            } else {
                showToast(UploadProductActivity.this, "You haven't picked Image");
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
                String imagePath = ImageFilePath.getPath(UploadProductActivity.this, imageUri1);
                imgExtension = imagePath.substring(imagePath.lastIndexOf("."));
                if (imgExtension.equalsIgnoreCase(".jpg")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    mConvertedImg2 = convertToBase64(resizedBitmap);
                    ivImage2.setImageBitmap(bitmap);
                } else {
                    showToast(UploadProductActivity.this, "Please select jpg image only");
                }
                dialog2.dismiss();
            } else {
                showToast(UploadProductActivity.this, "You haven't picked Image");
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
                Log.d(TAG, "onActivityResult:" + ">>>>>>2>" + tempUriImg2);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Log.d(TAG, "onRequestPermissionsResult: " + "case1");
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for all permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "CAMERA & location services permission granted");
                        doPermissionGranted();
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Camera and Location Services Permission are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    Util.checkRequestPermiss(getApplicationContext(), UploadProductActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("Go to settings and enable permissions");
                            //  Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.vibrant.asp")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

}
