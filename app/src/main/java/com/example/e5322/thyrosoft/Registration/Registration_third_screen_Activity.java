package com.example.e5322.thyrosoft.Registration;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Models.SourcesCountry;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Registration_third_screen_Activity extends AppCompatActivity {

    Spinner source;
    EditText enquiry_remarks;
    Button save;
    TextView visitingcard;
    public static com.android.volley.RequestQueue PostQue;
    ProgressDialog barProgressDialog;
    private SourcesCountry sourcesCountry;
    private int CAMERA = 2;
    String regisStatus, resId, response2;
    private static final int CAMERA_REQUEST = 1888;
    String encodedImage,enquiryremark_data;
    String user_name,source_spinner_item,user_landline,user_profession,user_qulification,user_locatio,user_pincode,user_addr,user_city,user_state,user_country,user_email,user_mobile;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static com.android.volley.RequestQueue PostQueRegistration;
    private String TAG=Registration_third_screen_Activity.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_third_screen_);
        enquiry_remarks = (EditText)findViewById(R.id.enquiry_remarks);
        source = (Spinner)findViewById(R.id.source);
        save = (Button) findViewById(R.id.save);
        visitingcard = (TextView) findViewById(R.id.visitingcard);

        ArrayAdapter sourcespinner = ArrayAdapter.createFromResource(this, R.array.Sources, R.layout.spinner_item);
        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        source.setAdapter(sourcespinner);

        visitingcard.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               source_spinner_item= source.getSelectedItem().toString();
               enquiryremark_data = enquiry_remarks.getText().toString();
               registerUser();
           }
       });

        SharedPreferences prefsUserDetails = getSharedPreferences("Registration", MODE_PRIVATE);
        user_name = prefsUserDetails.getString("name_user", null);
        user_landline = prefsUserDetails.getString("landline", null);
        user_profession = prefsUserDetails.getString("profession", null);
        user_qulification = prefsUserDetails.getString("qualification", null);
        user_locatio = prefsUserDetails.getString("location", null);
        user_pincode = prefsUserDetails.getString("pincode", null);
        user_addr = prefsUserDetails.getString("adddress", null);
        user_city = prefsUserDetails.getString("city", null);
        user_state = prefsUserDetails.getString("state", null);
        user_country = prefsUserDetails.getString("country", null);

        SharedPreferences prefsUserDetails1 = getApplicationContext().getSharedPreferences("getEmailMobile", MODE_PRIVATE);
        user_mobile = prefsUserDetails1.getString("Mobile", null);
        user_email = prefsUserDetails1.getString("Email", null);

    }

    private void registerUser() {
        PostQueRegistration = Volley.newRequestQueue(Registration_third_screen_Activity.this);
        JSONObject jsonObjectRegister_User = new JSONObject();
        try {
            jsonObjectRegister_User.put("apiKey", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
            jsonObjectRegister_User.put("state",user_state );
            jsonObjectRegister_User.put("country",user_country );
            jsonObjectRegister_User.put("city", user_city);
            jsonObjectRegister_User.put("mobileNumber", user_mobile);
            jsonObjectRegister_User.put("emailId", user_email);
            jsonObjectRegister_User.put("name", user_name);
            jsonObjectRegister_User.put("landLine", user_landline);
            jsonObjectRegister_User.put("currentProfession", user_profession);
            jsonObjectRegister_User.put("qualification", user_qulification);
            jsonObjectRegister_User.put("intrestedLocation", user_locatio);
            jsonObjectRegister_User.put("source", source_spinner_item);
            jsonObjectRegister_User.put("address", user_addr);
            jsonObjectRegister_User.put("pinCode", user_pincode);
            jsonObjectRegister_User.put("inquiryRemarks", enquiryremark_data);
            jsonObjectRegister_User.put("visitingCardImage", encodedImage);
            jsonObjectRegister_User.put("userType", "");
            jsonObjectRegister_User.put("masterType", "MASTER");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequestEmailPhone = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.Register_User, jsonObjectRegister_User, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG, "onResponse: "+response );
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                          regisStatus      = parentObjectOtp.getString("regisStatus");
                          resId            = parentObjectOtp.getString("resId");
                          response2        = parentObjectOtp.getString("response");
                          if(resId.equals("RES0000")){
                              TastyToast.makeText(Registration_third_screen_Activity.this, ToastFile.reg_success, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                              Intent i = new Intent(Registration_third_screen_Activity.this, Login.class);
                              startActivity(i);
                          }else
                    {
                        TastyToast.makeText(Registration_third_screen_Activity.this,response2 , TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Toast.makeText(Registration_third_screen_Activity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

                } else {

                    System.out.println(error);

                }
            }
        });
        PostQueRegistration.add(jsonObjectRequestEmailPhone);
        Log.e(TAG, "registerUser: json"+jsonObjectRegister_User );
        Log.e(TAG, "registerUser: url"+jsonObjectRequestEmailPhone );
        jsonObjectRequestEmailPhone.setRetryPolicy(new DefaultRetryPolicy(300000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Bitmap converetdImage = getResizedBitmap(photo, 100);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            System.out.println(""+encodedImage);
//            String  input = encodedImage;
//            String input2 = input.replace("\n", "");


        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        System.out.println("height of image"+height);

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
