package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SummaryActivity_New extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid;
    TextView saverepeat, title, delete_woe, ref_by_txt, serial_number, serial_number_re;
    RecyclerView sample_list;
    String getSelctedTests, passProdcucts;
    int saveSrNumber;
    public static com.android.volley.RequestQueue sendGPSDetails;
    String getStateName, getCountryName, getCityName;
    AlertDialog.Builder alertDialog;
    SharedPreferences prefs;
    SharedPreferences savepatientDetails;
    private String totalAmount, amt_collected, patient_id_to_delete_tests;
    SharedPreferences preferences;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    ArrayList<BarcodelistModel> barcodelists;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    public static com.android.volley.RequestQueue POstQue;
    RequestQueue deletePatienDetail;
    private String message;
    DatabaseHelper myDb;
    private String ageType;

    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = SummaryActivity_New.this.getClass().getSimpleName();
    private String outputDateStr;
    LinearLayout btech_layout, refbylinear;
    int convertSrno;
    private String nameString;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
    private String genderType;
    private String getFinalDate;
    Date date;
    Context context1;
    ImageView back, home;
    private String version;
    private int versionCode;

    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationManager mLocationManager;

    LocationRequest mLocationRequest;
    com.google.android.gms.location.LocationListener listener;
    long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 2000; /* 2 sec */

    LocationManager locationManager;
    String getIMEINUMBER;
    String mobileModel;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    private boolean GpsStatus;
    private String referenceID;
    private Global globalClass;
    private String patientAddress;
    private String referrredBy;
    private String labIDaddress;
    private String RES_ID;
    private String barcode_patient_id;
    private String btechIDToPass;
    private String btechNameToPass;
    private String getcampIDtoPass;
    private String kycinfo;
    private String typename;
    private String labAddress;
    private String labname;
    private String getBrand_name;
    private String sr_number;
    private int pass_to_api;
    private String error, pid, barcodes, response1, resID;
    private String getBarcodesOffline;
    private boolean flagforOnce=false;

    @SuppressLint({"WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity_new);
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
        saverepeat = (TextView) findViewById(R.id.saverepeat);
        delete_woe = (TextView) findViewById(R.id.delete_woe);
        title = (TextView) findViewById(R.id.title);
        ref_by_txt = (TextView) findViewById(R.id.ref_by_txt);
        serial_number = (TextView) findViewById(R.id.serial_number);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        SGCLinearid = (LinearLayout) findViewById(R.id.SGCLinearid);
        btech_layout = (LinearLayout) findViewById(R.id.btech_layout);
        refbylinear = (LinearLayout) findViewById(R.id.refbylinear);
        linearLayoutManager = new LinearLayoutManager(SummaryActivity_New.this);
        sample_list.setLayoutManager(linearLayoutManager);


//        setHasOptionsMenu(true);

        context1 = SummaryActivity_New.this;

        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}

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
        pass_to_api = Integer.parseInt(sr_number);
        int edta_sr_num = pass_to_api + 1;


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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent i = new Intent(SummaryActivity_New.this, ProductLisitngActivityNew.class);
//                startActivity(i);

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(SummaryActivity_New.this);
            }
        });

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
        SummaryActivity_New.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();
        getSelctedTests = bundle.getString("tetsts");
        passProdcucts = bundle.getString("passProdcucts");
//        amountPaid = bundle.getString("payment");
        totalAmount = bundle.getString("TotalAmt");
        amt_collected = bundle.getString("CollectedAmt");
        patient_id_to_delete_tests = bundle.getString("patientId");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");
        getBarcodesOffline = bundle.getString("barcodes");

        //   finalspecimenttypewiselist= bundle.getParcelableArrayList("getOutlablist");
        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);

        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("year", null);
        patientYearType = preferences.getString("yearType", null);
        patientGender = preferences.getString("gender", null);

        pat_type.setText("TTL/" + typename);
        pat_sct.setText(getFinalDate + " " + getFinalTime);
        pat_name.setText(nameString + "/" + getFinalAge + " " + getageType + "/" + genderType);
        pat_amt_collected.setText(amt_collected);
        serial_number.setText(sr_number);
        title.setText("Summary");

        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        tests.setText(passProdcucts);

        GlobalClass.barcodelists = new ArrayList<>();

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelistData = new Barcodelist();
            barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            GlobalClass.barcodelists.add(barcodelistData);
        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(SummaryActivity_New.this, GlobalClass.barcodelists, pass_to_api);
        sample_list.setAdapter(getPatientSampleDetails);

        saverepeat.setOnClickListener(new View.OnClickListener() {
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
                                if (!GlobalClass.isNetworkAvailable(SummaryActivity_New.this)) {
                                    boolean deletedRows = myDb.deleteData(getBarcodesOffline);
                                    if(deletedRows ==true)
                                        TastyToast.makeText(SummaryActivity_New.this, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    finish();
                                   Intent i = new Intent(SummaryActivity_New.this,ManagingTabsActivity.class);
                                   startActivity(i);
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


        if(flagforOnce==false){
            flagforOnce=true;
            deletePatienDetail = Volley.newRequestQueue(context1);
            JSONObject jsonObjectOtp = new JSONObject();
            try {

                jsonObjectOtp.put("api_key", api_key);
                jsonObjectOtp.put("Patient_id", patient_id_to_delete_tests);
                jsonObjectOtp.put("tsp", user);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
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
            }, new com.android.volley.Response.ErrorListener() {
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
        }else{
            TastyToast.makeText(context1, "Sample not deleted successfully !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        latitudePassTOAPI = String.valueOf(location.getLatitude());
        longitudePassTOAPI = String.valueOf(location.getLongitude());

        Double getlat = Double.parseDouble(latitudePassTOAPI);
        Double getlog = Double.parseDouble(longitudePassTOAPI);

        Geocoder gcd = new Geocoder(SummaryActivity_New.this, Locale.getDefault());
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

                System.out.println(addresses.get(0).getAdminArea());
                System.out.println(addresses.get(0).getCountryName());
                System.out.println(addresses.get(0).getLocality());
            }
        } else {
            getStateName = "";
            getCountryName = "";
            getCityName = "";
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
            String convertedDate = sdf_format.format(date);
            date = inputFormat.parse(convertedDate + time);
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringofconvertedTime;
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

        Intent i = new Intent(SummaryActivity_New.this, ManagingTabsActivity.class);
        i.putExtra("passToWoefragment", "frgamnebt");
        startActivity(i);
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

        } else {

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
        finish();
        Intent i = new Intent(SummaryActivity_New.this, ManagingTabsActivity.class);
        startActivity(i);
//        GlobalClass.exportDB(SummaryActivity_New.this);
        super.onBackPressed();
    }




}
