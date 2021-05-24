package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.RequestModels.DeleteWOERequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.DeleteWOEResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SummaryActivity_New extends AppCompatActivity {
    public static com.android.volley.RequestQueue sendGPSDetails;
    public static com.android.volley.RequestQueue POstQue;
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid, ll_patient_age, ll_patient_gender;
    TextView saverepeat, title, delete_woe, ref_by_txt, serial_number, serial_number_re, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    String getSelctedTests, passProdcucts, getbrandname;
    int saveSrNumber;
    AlertDialog.Builder alertDialog;
    SharedPreferences prefs;
    SharedPreferences savepatientDetails;
    SharedPreferences preferences;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    ArrayList<BarcodelistModel> barcodelists;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    RequestQueue deletePatienDetail;
    DatabaseHelper myDb;
    LinearLayout btech_layout, refbylinear;
    int convertSrno;
    Date date;
    Context context1;
    ImageView back, home;


    com.google.android.gms.location.LocationListener listener;
    long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 2000; /* 2 sec */
    LocationManager locationManager;
    LinearLayout linamt_collected;
    TextView amtcollected;
    private String totalAmount, amt_collected, patient_id_to_delete_tests;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    private String message;
    private String ageType;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = SummaryActivity_New.this.getClass().getSimpleName();
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

        linearLayoutManager = new LinearLayoutManager(SummaryActivity_New.this);
        sample_list.setLayoutManager(linearLayoutManager);


//        setHasOptionsMenu(true);

        context1 = SummaryActivity_New.this;

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        myDb = new DatabaseHelper(this);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


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

   /*     if (sr_number != null)
            pass_to_api = Integer.parseInt(sr_number);

        pass_to_api = Integer.parseInt(sr_number);
        int edta_sr_num = pass_to_api + 1;*/


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

   /*     if (saveGenderId.equalsIgnoreCase("F")) {
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
        }*/


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
        SummaryActivity_New.this.setTitle("Summary");

        Bundle bundle = getIntent().getExtras();
        getSelctedTests = bundle.getString("tetsts");
        getbrandname = bundle.getString("brandname");
        fromcome = bundle.getString("fromcome", "");
        location = bundle.getString("location");
        passProdcucts = bundle.getString("passProdcucts");
//        amountPaid = bundle.getString("payment");
        totalAmount = bundle.getString("TotalAmt");
        amt_collected = bundle.getString("CollectedAmt");
        patient_id_to_delete_tests = bundle.getString("patientId");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");
        getBarcodesOffline = bundle.getString("barcodes");

        //   finalspecimenttypewiselist= bundle.getParcelableArrayList("getOutlablist");
        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);

        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("year", "");
        patientYearType = preferences.getString("yearType", "");
        patientGender = preferences.getString("gender", "");

//        getFinalTime = GlobalClass.changetimeformate(getFinalTime);
        getFinalTime= DateUtils.Req_Date_Req(getFinalTime, "hh:mm a", "HH:mm");
        Log.e(TAG, "getFinalDate---->" + getFinalDate);
        Log.e(TAG, "getFinalTime---->" + getFinalTime);

        pat_type.setText(getbrandname + "/" + typename + "/" + user);
        pat_sct.setText(getFinalDate + " " + getFinalTime);
        pat_name.setText(nameString);

        if (fromcome.equals("")) {
            linamt_collected.setVisibility(View.GONE);
        } else {
            linamt_collected.setVisibility(View.VISIBLE);
            amtcollected.setText(amt_collected);
        }

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        pat_amt_collected.setText(amt_collected);
        serial_number.setText(sr_number);
        if (location != null && !location.equalsIgnoreCase("")) {
            ll_location.setVisibility(View.VISIBLE);
            txt_setLocation.setText(location);
        } else {
            ll_location.setVisibility(View.GONE);
        }
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
                                if (Global.isoffline || !GlobalClass.isNetworkAvailable(SummaryActivity_New.this)) {
                                    boolean deletedRows = myDb.deleteData(getBarcodesOffline);
                                    if (deletedRows == true)
                                        TastyToast.makeText(SummaryActivity_New.this, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    finish();
                                    Constants.covidwoe_flag = "1";
                                    Intent i = new Intent(SummaryActivity_New.this, ManagingTabsActivity.class);
                                    startActivity(i);
                                } else {
                                    deleteWoe();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();

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


        if (!flagforOnce) {
            flagforOnce = true;
            deletePatienDetail = GlobalClass.setVolleyReq(context1);

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
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        Gson gson = new Gson();
                        DeleteWOEResponseModel deleteWOEResponseModel = gson.fromJson(String.valueOf(response), DeleteWOEResponseModel.class);
                        if (deleteWOEResponseModel != null) {
                            if (!GlobalClass.isNull(deleteWOEResponseModel.getRES_ID()) && deleteWOEResponseModel.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                                Constants.covidwoe_flag = "1";
                                TastyToast.makeText(context1, ToastFile.woe_dlt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                Intent intent = new Intent(context1, ManagingTabsActivity.class);
                                startActivity(intent);
                            } else {
                                TastyToast.makeText(context1, deleteWOEResponseModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        } else {
                            TastyToast.makeText(context1, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }

                }
            });
            deletePatienDetail.add(jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObject);
        } else {
            TastyToast.makeText(context1, "Sample not deleted successfully !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

        if (fromcome.equals("")) {
            Constants.covidwoe_flag = "1";
            intent = new Intent(SummaryActivity_New.this, ManagingTabsActivity.class);
            intent.putExtra("passToWoefragment", "frgamnebt");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            intent = new Intent(SummaryActivity_New.this, SpecialOffer_Activity.class);
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
    public void onBackPressed() {
        finish();
        Constants.covidwoe_flag = "1";
        Intent i = new Intent(SummaryActivity_New.this, ManagingTabsActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
