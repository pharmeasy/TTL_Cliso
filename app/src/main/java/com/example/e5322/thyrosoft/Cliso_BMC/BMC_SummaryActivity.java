package com.example.e5322.thyrosoft.Cliso_BMC;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BMC_SummaryActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static RequestQueue POstQue;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid, ll_patient_age, ll_patient_gender;
    TextView tv_enter_new_woe, title, delete_woe, ref_by_txt, serial_number, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    String getSelctedTests, passProdcucts;
    String getStateName, getCountryName, getCityName;
    SharedPreferences prefs;
    SharedPreferences savepatientDetails;
    SharedPreferences preferences;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    ProgressDialog barProgressDialog;
    Barcodelist barcodelistData;
    RequestQueue deletePatienDetail;
    DatabaseHelper myDb;
    LinearLayout btech_layout, refbylinear;
    Context context1;
    ImageView back, home;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 2000; /* 2 sec */
    String getIMEINUMBER;
    String mobileModel;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    private String totalAmount, amt_collected, patient_id_to_delete_tests;
    private String patientName, patientYearType, user, passwrd, access, api_key;
    private String patientYear, patientGender;
    private String ageType;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = BMC_SummaryActivity.this.getClass().getSimpleName();
    private String nameString;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
    private String genderType;
    private String getFinalDate;
    private String version;
    private int versionCode;
    private String referenceID;
    private Global globalClass;
    private String patientAddress;
    private String referrredBy;
    private String labIDaddress;
    private String btechIDToPass;
    private String btechNameToPass;
    private String getcampIDtoPass;
    private String kycinfo;
    private String typename;
    private String labAddress;
    private String labname;
    private String sr_number;
    private int pass_to_api;
    private String error, pid, barcodes, response1, resID;
    private String getBarcodesOffline;
    private boolean flagforOnce = false;
    private String location;
    private LinearLayout ll_location;
    private TextView txt_setLocation, tv_view_TRF, tv_view_receipt;
    private ArrayList<String> locationlist;
    private Dialog dialog;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmc_summary);

        context1 = BMC_SummaryActivity.this;
        initUI();

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        SharedPreferences getIMIE = getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);
        myDb = new DatabaseHelper(this);
        SharedPreferences getModelNumber = getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", null);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        nameString = savepatientDetails.getString("name", null);
        getFinalAge = savepatientDetails.getString("age", null);
        saveGenderId = savepatientDetails.getString("gender", null);
        getFinalTime = savepatientDetails.getString("sct", null);
        getageType = savepatientDetails.getString("ageType", null);
        getFinalDate = savepatientDetails.getString("date", null);

        labname = savepatientDetails.getString("labname", null);
        labAddress = savepatientDetails.getString("labAddress", null);

        patientAddress = savepatientDetails.getString("patientAddress", null);
        referrredBy = savepatientDetails.getString("refBy", null);
        referenceID = savepatientDetails.getString("refId", null);
        labIDaddress = savepatientDetails.getString("labIDaddress", null);
        btechIDToPass = savepatientDetails.getString("btechIDToPass", null);
        btechNameToPass = savepatientDetails.getString("btechNameToPass", null);
        getcampIDtoPass = savepatientDetails.getString("getcampIDtoPass", null);
        kycinfo = savepatientDetails.getString("kycinfo", null);
        typename = savepatientDetails.getString("woetype", null);
        sr_number = savepatientDetails.getString("SR_NO", null);

        if (sr_number != null)
            pass_to_api = Integer.parseInt(sr_number);

        pass_to_api = Integer.parseInt(sr_number);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView

        if (referrredBy != null) {
            refbylinear.setVisibility(View.VISIBLE);
            ref_by_txt.setText(referrredBy);
        } else {
            refbylinear.setVisibility(View.GONE);
        }

        initListeners();

        if (saveGenderId.equalsIgnoreCase("F")) {
            genderType = "Female";
        } else if (saveGenderId.equalsIgnoreCase("M")) {
            genderType = "Male";
        }

        if (getageType.equalsIgnoreCase("Years")) {
            ageType = "Y";
        } else if (getageType.equalsIgnoreCase("Months")) {
            ageType = "M";
        } else if (getageType.equalsIgnoreCase("Days")) {
            ageType = "D";
        }

        finalspecimenttypewiselist = new ArrayList<>();
        BMC_SummaryActivity.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();
        getSelctedTests = bundle.getString("tetsts");
        location = bundle.getString("location");
        passProdcucts = bundle.getString("passProdcucts");
        totalAmount = bundle.getString("TotalAmt");
        amt_collected = bundle.getString("CollectedAmt");
        patient_id_to_delete_tests = bundle.getString("patientId");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");
        getBarcodesOffline = bundle.getString("barcodes");

        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);

        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("year", null);
        patientYearType = preferences.getString("yearType", null);
        patientGender = preferences.getString("gender", null);

        pat_type.setText("TTL/" + typename);
        pat_sct.setText(getFinalDate + " " + getFinalTime);
        pat_name.setText(nameString);

        if (typename.equalsIgnoreCase("WHATERS")) {
            ll_patient_age.setVisibility(View.GONE);
            ll_patient_gender.setVisibility(View.GONE);
        } else {
            if (getFinalAge != null && !getFinalAge.equalsIgnoreCase("") && getageType != null && !getageType.equalsIgnoreCase("")) {
                ll_patient_age.setVisibility(View.VISIBLE);
                txt_pat_age.setText(getFinalAge + " " + getageType);
            } else {
                ll_patient_age.setVisibility(View.GONE);
            }
            if (genderType != null && !genderType.equalsIgnoreCase("")) {
                ll_patient_gender.setVisibility(View.VISIBLE);
                txt_pat_gender.setText(genderType);
            } else {
                ll_patient_gender.setVisibility(View.GONE);
            }
        }

        pat_amt_collected.setText(amt_collected);
        serial_number.setText(sr_number);

        locationlist = new ArrayList<>();
        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            locationlist.add(finalspecimenttypewiselist.get(i).getLocation());
        }

        if (locationlist.contains("TTL-OTHERS")) {
            ll_location.setVisibility(View.GONE);
        } else {
            if (location != null && !location.equalsIgnoreCase("")) {
                ll_location.setVisibility(View.VISIBLE);
                txt_setLocation.setText(location);
            } else {
                ll_location.setVisibility(View.GONE);
            }
        }

        title.setText("Summary");

        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        tests.setText(passProdcucts);

        GlobalClass.BMC_barcodelists = new ArrayList<>();

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelistData = new Barcodelist();
            barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            GlobalClass.BMC_barcodelists.add(barcodelistData);
        }

        BMC_PatientSampleDetailsAdapter bmc_patientSampleDetailsAdapter = new BMC_PatientSampleDetailsAdapter(BMC_SummaryActivity.this, GlobalClass.BMC_barcodelists, pass_to_api);
        sample_list.setAdapter(bmc_patientSampleDetailsAdapter);
    }

    private void initUI() {
        pat_type = (TextView) findViewById(R.id.pat_type);
        pat_sct = (TextView) findViewById(R.id.pat_sct);
        pat_name = (TextView) findViewById(R.id.pat_name);
        pat_ref = (TextView) findViewById(R.id.pat_ref);
        pat_sgc = (TextView) findViewById(R.id.pat_sgc);
        pat_scp = (TextView) findViewById(R.id.pat_scp);
        pat_amt_collected = (TextView) findViewById(R.id.pat_amt_collected);
        tests = (TextView) findViewById(R.id.tests);
        btech = (TextView) findViewById(R.id.btech);
        btechtile = (TextView) findViewById(R.id.btechtile);
        sample_list = (RecyclerView) findViewById(R.id.sample_list);
        tv_enter_new_woe = (TextView) findViewById(R.id.tv_enter_new_woe);
        delete_woe = (TextView) findViewById(R.id.delete_woe);
        title = (TextView) findViewById(R.id.title);
        ref_by_txt = (TextView) findViewById(R.id.ref_by_txt);
        serial_number = (TextView) findViewById(R.id.serial_number);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        SGCLinearid = (LinearLayout) findViewById(R.id.SGCLinearid);
        btech_layout = (LinearLayout) findViewById(R.id.btech_layout);
        refbylinear = (LinearLayout) findViewById(R.id.refbylinear);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_patient_age = (LinearLayout) findViewById(R.id.ll_patient_age);
        ll_patient_gender = (LinearLayout) findViewById(R.id.ll_patient_gender);
        txt_setLocation = (TextView) findViewById(R.id.txt_setLocation);
        tv_view_TRF = (TextView) findViewById(R.id.tv_view_TRF);
        tv_view_receipt = (TextView) findViewById(R.id.tv_view_receipt);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);
        linearLayoutManager = new LinearLayoutManager(BMC_SummaryActivity.this);
        sample_list.setLayoutManager(linearLayoutManager);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_SummaryActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        tv_enter_new_woe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });

        delete_woe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
                alertDialogBuilder.setTitle("Confirm delete !");
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (!GlobalClass.isNetworkAvailable(BMC_SummaryActivity.this)) {
                                    Toast.makeText(BMC_SummaryActivity.this, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
                                } else {
                                    deleteWoe();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        tv_view_TRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.TRF_BITMAP != null) {
                    showImageDialog(GlobalClass.TRF_BITMAP);
                }
                else
                    Toast.makeText(BMC_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
            }
        });

        tv_view_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.RECEIPT_BITMAP != null) {
                    showImageDialog(GlobalClass.RECEIPT_BITMAP);
                }
                else
                    Toast.makeText(BMC_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteWoe() {
        SharedPreferences.Editor editor = getSharedPreferences("savePatientDetails", 0).edit();
        editor.remove("name");
        editor.remove("age");
        editor.remove("gender");
        editor.remove("sct");
        editor.remove("date");
        editor.remove("ageType");
        editor.remove("labname");
        editor.remove("labAddress");
        editor.remove("patientAddress");
        editor.remove("refBy");
        editor.remove("refId");
        editor.remove("labIDaddress");
        editor.remove("btechIDToPass");
        editor.remove("btechNameToPass");
        editor.remove("getcampIDtoPass");
        editor.remove("kycinfo");
        editor.remove("woetype");
        editor.remove("WOEbrand");
        editor.commit();

        barProgressDialog = new ProgressDialog(context1);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        if (flagforOnce == false) {
            flagforOnce = true;
            deletePatienDetail = Volley.newRequestQueue(context1);
            JSONObject jsonObjectOtp = new JSONObject();
            try {
                jsonObjectOtp.put("api_key", api_key);
                jsonObjectOtp.put("Patient_id", patient_id_to_delete_tests);
                jsonObjectOtp.put("tsp", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        String finalJson = response.toString();
                        JSONObject parentObjectOtp = new JSONObject(finalJson);

                        error = parentObjectOtp.getString("error");
                        pid = parentObjectOtp.getString("pid");
                        response1 = parentObjectOtp.getString("response");
                        barcodes = parentObjectOtp.getString("barcodes");
                        resID = parentObjectOtp.getString("RES_ID");

                        if (resID.equals("RES0000")) {
                            TastyToast.makeText(context1, ToastFile.woe_dlt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Intent intent = new Intent(context1, ManagingTabsActivity.class);
                            startActivity(intent);
                        } else {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            TastyToast.makeText(context1, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } catch (JSONException e) {
                        TastyToast.makeText(context1, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                    } else {
                        System.out.println(error);
                    }
                }
            });
            deletePatienDetail.add(jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);
        } else {
            TastyToast.makeText(context1, "Sample not deleted successfully !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        latitudePassTOAPI = String.valueOf(location.getLatitude());
        longitudePassTOAPI = String.valueOf(location.getLongitude());

        Double getlat = Double.parseDouble(latitudePassTOAPI);
        Double getlog = Double.parseDouble(longitudePassTOAPI);

        Geocoder gcd = new Geocoder(BMC_SummaryActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(getlat, getlog, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null) {
            if (addresses.size() != 0) {
                getStateName = addresses.get(0).getAdminArea();
                getCountryName = addresses.get(0).getCountryName();
                getCityName = addresses.get(0).getLocality();
            }
        } else {
            getStateName = "";
            getCountryName = "";
            getCityName = "";
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void fetchData() {
        SharedPreferences.Editor editor = getSharedPreferences("savePatientDetails", 0).edit();
        editor.remove("name");
        editor.remove("age");
        editor.remove("gender");
        editor.remove("sct");
        editor.remove("date");
        editor.remove("ageType");
        editor.remove("labname");
        editor.remove("labAddress");
        editor.remove("patientAddress");
        editor.remove("refBy");
        editor.remove("refId");
        editor.remove("labIDaddress");
        editor.remove("btechIDToPass");
        editor.remove("btechNameToPass");
        editor.remove("getcampIDtoPass");
        editor.remove("kycinfo");
        editor.remove("woetype");
        editor.remove("WOEbrand");
        editor.commit();
        GlobalClass.setflagToRefreshData = true;

        Intent i = new Intent(BMC_SummaryActivity.this, ManagingTabsActivity.class);
        i.putExtra("passToWoefragment", "frgamnebt");
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more_tab_menu, menu);
        return true;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
//            startLocationUpdates();
        }
        if (mLocation != null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.e("reque", "--->>>>");
//        showSettingsAlert();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BMC_SummaryActivity.this, ManagingTabsActivity.class);
        startActivity(i);
        finish();
    }

    private void showImageDialog(Bitmap bitmap) {
        dialog = new Dialog(BMC_SummaryActivity.this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        imgView.setImageBitmap(bitmap);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}