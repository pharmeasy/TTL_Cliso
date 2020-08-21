package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.DeleteWoe_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.DeleteWOERequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.DeleteWOEResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SummaryActivity_New extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public static com.android.volley.RequestQueue sendGPSDetails;
    public static com.android.volley.RequestQueue POstQue;
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid, ll_patient_age, ll_patient_gender;
    TextView saverepeat, title, delete_woe, ref_by_txt, serial_number, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    String getSelctedTests, passProdcucts, getbrandname;
    String getStateName, getCountryName, getCityName;
    SharedPreferences prefs;
    SharedPreferences savepatientDetails;
    SharedPreferences preferences;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    Barcodelist barcodelistData;
    RequestQueue deletePatienDetail;
    DatabaseHelper myDb;
    LinearLayout btech_layout, refbylinear;
    Activity mActivity;
    ImageView back, home;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    com.google.android.gms.location.LocationListener listener;
    long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 2000; /* 2 sec */
    String getIMEINUMBER;
    String mobileModel;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    LinearLayout linamt_collected;
    TextView amtcollected;
    private String totalAmount, amt_collected, patient_id_to_delete_tests;
    private String patientName, patientYearType, user, passwrd, access, api_key;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    private String message;
    private String ageType;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = mActivity.getClass().getSimpleName();
    private String outputDateStr;
    private String nameString;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
    private String genderType, CollectedAmt;
    private String getFinalDate;
    private String version;
    private int versionCode;
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
    private String getBarcodesOffline;
    private boolean flagforOnce = false;
    private String location, fromcome;
    private LinearLayout ll_location;
    private TextView txt_setLocation;
    public ArrayList<Barcodelist> barcodelists;

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

    @SuppressLint({"WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity_new);
        mActivity = SummaryActivity_New.this;

        initViews();

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


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version = pInfo.versionName;
        versionCode = pInfo.versionCode;

        if (!GlobalClass.isNull(referrredBy)) {
            refbylinear.setVisibility(View.VISIBLE);
            GlobalClass.SetText(ref_by_txt, referrredBy);
        } else {
            refbylinear.setVisibility(View.GONE);
        }

        initListner();

        if (!GlobalClass.isNull(saveGenderId)) {
            if (saveGenderId.equalsIgnoreCase("F")) {
                genderType = "Female";
            } else if (saveGenderId.equalsIgnoreCase("M")) {
                genderType = "Male";
            }
        }

        if (!GlobalClass.isNull(getageType)) {
            if (getageType.equalsIgnoreCase("Years")) {
                ageType = "Y";
            } else if (getageType.equalsIgnoreCase("Months")) {
                ageType = "M";
            } else if (getageType.equalsIgnoreCase("Days")) {
                ageType = "D";
            }
        }

        finalspecimenttypewiselist = new ArrayList<>();
        mActivity.setTitle("Summary");

        Bundle bundle = getIntent().getExtras();
        getSelctedTests = bundle.getString("tetsts");
        getbrandname = bundle.getString("brandname");
        fromcome = bundle.getString("fromcome", "");
        location = bundle.getString("location");
        passProdcucts = bundle.getString("passProdcucts");
        totalAmount = bundle.getString("TotalAmt");
        amt_collected = bundle.getString("CollectedAmt");
        patient_id_to_delete_tests = bundle.getString("patientId");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");
        getBarcodesOffline = bundle.getString("barcodes");
        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);

        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("year", "");
        patientYearType = preferences.getString("yearType", "");
        patientGender = preferences.getString("gender", "");

        getFinalTime = GlobalClass.changetimeformate(getFinalTime);

        Log.e(TAG, "getFinalDate---->" + getFinalDate);
        Log.e(TAG, "getFinalTime---->" + getFinalTime);


        GlobalClass.SetText(pat_type, getbrandname + "/" + typename + "/" + user);
        GlobalClass.SetText(pat_sct, getFinalDate + " " + getFinalTime);
        GlobalClass.SetText(pat_name, nameString);

        if (GlobalClass.isNull(fromcome)) {
            linamt_collected.setVisibility(View.GONE);
        } else {
            linamt_collected.setVisibility(View.VISIBLE);
            GlobalClass.SetText(amtcollected, amt_collected);
        }

        try {
            if (!GlobalClass.isNull(typename) && typename.equalsIgnoreCase("WHATERS")) {
                ll_patient_age.setVisibility(View.GONE);
                ll_patient_gender.setVisibility(View.GONE);
            } else {
                if (!GlobalClass.isNull(getFinalAge)) {
                    ll_patient_age.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_pat_age, getFinalAge + " " + getageType);
                } else {
                    ll_patient_age.setVisibility(View.GONE);
                }
                if (!GlobalClass.isNull(genderType)) {
                    ll_patient_gender.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_pat_gender, genderType);
                } else {
                    ll_patient_gender.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        GlobalClass.SetText(pat_amt_collected, amt_collected);
        GlobalClass.SetText(serial_number, sr_number);

        if (!GlobalClass.isNull(location)) {
            ll_location.setVisibility(View.VISIBLE);
            GlobalClass.SetText(txt_setLocation, location);
        } else {
            ll_location.setVisibility(View.GONE);
        }

        GlobalClass.SetText(title, "Summary");

        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        GlobalClass.SetText(tests, passProdcucts);

        barcodelists = new ArrayList<>();

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                barcodelistData = new Barcodelist();
                barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
                barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
                barcodelists.add(barcodelistData);
            }
        }


        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(mActivity, barcodelists, pass_to_api);
        sample_list.setAdapter(getPatientSampleDetails);


    }

    private void initListner() {
        saverepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchData();
            }
        });

        delete_woe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setTitle(MessageConstants.CONFIRM_DT);
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton(MessageConstants.YES,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (!GlobalClass.isNetworkAvailable(mActivity)) {
                                    boolean deletedRows = myDb.deleteData(getBarcodesOffline);
                                    if (deletedRows)
                                        GlobalClass.showTastyToast(mActivity, ToastFile.woeDelete, 1);
                                    finish();
                                    Constants.covidwoe_flag = "1";
                                    Intent i = new Intent(mActivity, ManagingTabsActivity.class);
                                    startActivity(i);
                                } else {
                                    deleteWoe();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton(MessageConstants.NO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(mActivity);
            }
        });

    }

    private void initViews() {
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
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_patient_age = (LinearLayout) findViewById(R.id.ll_patient_age);
        ll_patient_gender = (LinearLayout) findViewById(R.id.ll_patient_gender);
        txt_setLocation = (TextView) findViewById(R.id.txt_setLocation);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);

        amtcollected = findViewById(R.id.txt_amtcollect);
        linamt_collected = findViewById(R.id.ll_amtcollect);

        linearLayoutManager = new LinearLayoutManager(mActivity);
        sample_list.setLayoutManager(linearLayoutManager);

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


        if (!flagforOnce) {
            flagforOnce = true;
            deletePatienDetail = GlobalClass.setVolleyReq(mActivity);

            JSONObject jsonObject = null;
            try {
                DeleteWOERequestModel requestModel = new DeleteWOERequestModel();
                requestModel.setApi_key(api_key);
                requestModel.setPatient_id(patient_id_to_delete_tests);
                requestModel.setTsp(user);

                Gson deleteGson = new Gson();
                String deleteJSON = deleteGson.toJson(requestModel);
                jsonObject = new JSONObject(deleteJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (ControllersGlobalInitialiser.deleteWoe_controller != null) {
                    ControllersGlobalInitialiser.deleteWoe_controller = null;
                }
                ControllersGlobalInitialiser.deleteWoe_controller = new DeleteWoe_Controller(mActivity, SummaryActivity_New.this);
                ControllersGlobalInitialiser.deleteWoe_controller.deletewoe(jsonObject, deletePatienDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.Sample_not_delete, 2);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitudePassTOAPI = String.valueOf(location.getLatitude());
        longitudePassTOAPI = String.valueOf(location.getLongitude());

        Double getlat = Double.parseDouble(latitudePassTOAPI);
        Double getlog = Double.parseDouble(longitudePassTOAPI);

        Geocoder gcd = new Geocoder(mActivity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(getlat, getlog, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (GlobalClass.CheckArrayList(addresses)) {
            if (addresses.size() != 0) {
                getStateName = addresses.get(0).getAdminArea();
                getCountryName = addresses.get(0).getCountryName();
                getCityName = addresses.get(0).getLocality();

                Log.v("TAG", addresses.get(0).getAdminArea());
                Log.v("TAG", addresses.get(0).getCountryName());
                Log.v("TAG", addresses.get(0).getLocality());
            }
        } else {
            getStateName = "";
            getCountryName = "";
            getCityName = "";
        }
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
        Intent intent = null;

        if (GlobalClass.isNull(fromcome)) {
            Constants.covidwoe_flag = "1";
            intent = new Intent(mActivity, ManagingTabsActivity.class);
            intent.putExtra("passToWoefragment", "frgamnebt");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            intent = new Intent(mActivity, SpecialOffer_Activity.class);
            intent.putExtra("passToWoefragment", "frgamnebt");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


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
            return;
        }
        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation != null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.e("reque", "--->>>>");
    }


    @Override
    public void onBackPressed() {
        finish();
        Constants.covidwoe_flag = "1";
        Intent i = new Intent(mActivity, ManagingTabsActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    public void getdeletewoe(JSONObject response) {
        try {
            Gson gson = new Gson();
            DeleteWOEResponseModel deleteWOEResponseModel = gson.fromJson(String.valueOf(response), DeleteWOEResponseModel.class);
            if (deleteWOEResponseModel != null) {
                if (!GlobalClass.isNull(deleteWOEResponseModel.getRES_ID()) && deleteWOEResponseModel.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                    Constants.covidwoe_flag = "1";
                    GlobalClass.showTastyToast(mActivity, ToastFile.woe_dlt, 1);
                    Intent intent = new Intent(mActivity, ManagingTabsActivity.class);
                    startActivity(intent);
                } else {
                    GlobalClass.showTastyToast(mActivity, deleteWOEResponseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
