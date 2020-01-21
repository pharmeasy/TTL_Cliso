package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid, ll_patient_age, ll_patient_gender;
    TextView saverepeat, title, delete_woe, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    String getSelctedTests;
    SharedPreferences prefs;
    private String totalAnount;
    SharedPreferences preferences, prefe;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String brandName, sampleCollectionDate, sampleCollectionTime;
    private String typeName, referenceBy, sampleCollectionPoint, sampleGivingClient, getFinalSrNO;
    public static com.android.volley.RequestQueue POstQue;
    private String RES_ID;
    private String barcode_patient_id, message;
    private String alertMsg;
    ImageView back, home;
    DatabaseHelper myDb;
    private String refeID;
    private String ageType;
    private String labAddress;
    private String labID;
    private String labName;
    private String amountPaid;
    private String btechID;
    private String campID;
    private String homeaddress;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String getPincode;
    private String TAG = SummaryActivity.this.getClass().getSimpleName();
    private String outputDateStr;
    LinearLayout btech_layout;
    int convertSrno;
    private String version;
    private int versionCode;
    private String testnames;
    private String sr_number;
    private String error;
    private String pid;
    private String response1;
    private String barcodes;
    private String resID;
    private int pass_to_api;
    private Global globalClass;
    private String versionNameTopass;
    private RequestQueue deletePatienDetail;
    private String getBarcodesOffline;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

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
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        SGCLinearid = (LinearLayout) findViewById(R.id.SGCLinearid);
        btech_layout = (LinearLayout) findViewById(R.id.btech_layout);
        ll_patient_age = (LinearLayout) findViewById(R.id.ll_patient_age);
        ll_patient_gender = (LinearLayout) findViewById(R.id.ll_patient_gender);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);
        linearLayoutManager = new LinearLayoutManager(SummaryActivity.this);
        sample_list.setLayoutManager(linearLayoutManager);
//        setHasOptionsMenu(true);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        finalspecimenttypewiselist = new ArrayList<>();
        SummaryActivity.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();
        testnames = bundle.getString("tetsts");
        myDb = new DatabaseHelper(this);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        versionNameTopass = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;

        getSelctedTests = bundle.getString("selectedTest");
        amountPaid = bundle.getString("payment");
        totalAnount = bundle.getString("TotalAmt");
        barcode_patient_id = bundle.getString("patient_id");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");

        //   finalspecimenttypewiselist= bundle.getParcelableArrayList("getOutlablist");

        preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("age", null);
        patientYearType = preferences.getString("ageType", null);
        patientGender = preferences.getString("gender", null);
        brandName = preferences.getString("WOEbrand", null);
        typeName = preferences.getString("woetype", null);
        sampleCollectionDate = preferences.getString("date", null);
        sampleCollectionTime = preferences.getString("sct", null);
        sr_number = preferences.getString("SR_NO", null);
        pass_to_api = Integer.parseInt(sr_number);
        referenceBy = preferences.getString("refBy", null);
        ////////////////////////////////////////////////////////////////////////
        sampleCollectionPoint = preferences.getString("labAddress", null);
        sampleGivingClient = preferences.getString("labname", null);
        ////////////////////////////////////////////////////////////////////////

        refeID = preferences.getString("refId", null);
        labAddress = preferences.getString("labAddress", null);
        labID = preferences.getString("labIDaddress", null);
        labName = preferences.getString("labname", null);
        btechID = preferences.getString("btechIDToPass", null);
        campID = preferences.getString("getcampIDtoPass", null);
        homeaddress = preferences.getString("patientAddress", null);
        getFinalPhoneNumberToPost = preferences.getString("kycinfo", null);
        getFinalEmailIdToPost = "";
        getPincode = "";

        title.setText("Summary");

        getBarcodesOffline = bundle.getString("Outlbbarcodes");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(SummaryActivity.this);
            }
        });

        pat_sgc.setText(sampleGivingClient);
        pat_scp.setText(sampleCollectionPoint);
        pat_type.setText(brandName + "/" + typeName + "/" + user);

        if (patientYear.equalsIgnoreCase("")) {
            pat_name.setText(patientName);
            ll_patient_age.setVisibility(View.GONE);
            ll_patient_gender.setVisibility(View.GONE);
        } else {
            pat_name.setText(patientName);
            if (patientYear != null && !patientYear.equalsIgnoreCase("") && patientYearType != null && !patientYearType.equalsIgnoreCase("")) {
                ll_patient_age.setVisibility(View.VISIBLE);
                txt_pat_age.setText(patientYear + " " + patientYearType);
            } else {
                ll_patient_age.setVisibility(View.GONE);
            }
            if (patientGender != null && !patientGender.equalsIgnoreCase("")) {
                ll_patient_gender.setVisibility(View.VISIBLE);
                if (patientGender.equalsIgnoreCase("M")) {
                    txt_pat_gender.setText("Male");
                } else if(patientGender.equalsIgnoreCase("F")){
                    txt_pat_gender.setText("Female");
                }
            } else {
                ll_patient_gender.setVisibility(View.GONE);
            }
        }

        pat_sct.setText(sampleCollectionDate + " " + sampleCollectionTime);
        pat_ref.setText(referenceBy);
        alertMsg = "";
        //display the current version in a TextView
        if (btechID != null) {
            btech.setText(btechID);
        } else {
            btech_layout.setVisibility(View.GONE);
        }
        pat_amt_collected.setText(amountPaid);
        tests.setText(testnames);
        GlobalClass.barcodelists = new ArrayList<>();
        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelistData = new Barcodelist();
            barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            GlobalClass.barcodelists.add(barcodelistData);
        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(SummaryActivity.this, GlobalClass.barcodelists, pass_to_api);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SummaryActivity.this);
                alertDialogBuilder.setTitle("Confirm delete !");
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (!GlobalClass.isNetworkAvailable(SummaryActivity.this)) {
                                    boolean deletedRows = myDb.deleteData(getBarcodesOffline);
                                    if (deletedRows == true)
                                        TastyToast.makeText(SummaryActivity.this, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    finish();
                                    Intent i = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
                                    startActivity(i);
                                } else {
                                    deleteWoe();
                                }

                                deleteWoe();
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

        barProgressDialog = new ProgressDialog(SummaryActivity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        deletePatienDetail = Volley.newRequestQueue(SummaryActivity.this);
        JSONObject jsonObjectOtp = new JSONObject();
        try {

            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", barcode_patient_id);
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
                        TastyToast.makeText(SummaryActivity.this, ToastFile.woe_dlt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent intent = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
                        startActivity(intent);
                    } else {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        TastyToast.makeText(SummaryActivity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(SummaryActivity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
    }


    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
            String convertedDate = sdf_format.format(date);
            date = inputFormat.parse(time);
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

        GlobalClass.setflagToRefreshData = true;
        Intent i = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
        i.putExtra("passToWoefragment", "frgamnebt");
        startActivity(i);

    }

    private void sendFinalWoe() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more_tab_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterReport:
                MyPojoWoe myPojoWoe = new MyPojoWoe();

                Woe woe = new Woe();
                woe.setAADHAR_NO("");
                woe.setADDRESS(getFinalAddressToPost);
                woe.setAGE(patientYear);
                woe.setAGE_TYPE(ageType);
                woe.setALERT_MESSAGE(alertMsg);
                woe.setAMOUNT_COLLECTED(amountPaid);
                woe.setAMOUNT_DUE("");
                woe.setAPP_ID(versionNameTopass);
                woe.setBCT_ID(btechID);
                woe.setBRAND(brandName);
                woe.setCAMP_ID(campID);
                woe.setCONT_PERSON("");
                woe.setCONTACT_NO(getFinalPhoneNumberToPost);
                woe.setCUSTOMER_ID("");
                woe.setDELIVERY_MODE(2);
                woe.setEMAIL_ID(getFinalEmailIdToPost);
                woe.setENTERED_BY(user);
                woe.setGENDER(patientGender);
                woe.setLAB_ADDRESS(labAddress);
                woe.setLAB_ID(labID);
                woe.setLAB_NAME(labName);
                woe.setLEAD_ID("");
                woe.setMAIN_SOURCE(user);
                woe.setORDER_NO("");
                woe.setOS("unknown");
                woe.setPATIENT_NAME(patientName);
                woe.setPINCODE(getPincode);
                woe.setPRODUCT(testnames);
                woe.setPurpose("mobile application");
                woe.setREF_DR_ID(refeID);
                woe.setREF_DR_NAME(referenceBy);
                woe.setREMARKS("MOBILE");
                woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
                woe.setSPECIMEN_SOURCE("");
                woe.setSR_NO(pass_to_api);
                woe.setSTATUS("N");
                woe.setSUB_SOURCE_CODE(GlobalClass.subSourceCodeItem);
                woe.setTOTAL_AMOUNT(totalAnount);
                woe.setTYPE(typeName);
                woe.setWATER_SOURCE("");
                woe.setWO_MODE("THYROSOFTLITE APP");
                woe.setWO_STAGE(3);
                woe.setULCcode("");
                myPojoWoe.setWoe(woe);

                barcodelists = new ArrayList<>();
                for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                    barcodelist = new BarcodelistModel();
                    barcodelist.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
                    barcodelist.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
                    barcodelist.setTESTS(finalspecimenttypewiselist.get(i).getProducts());

                    barcodelists.add(barcodelist);
                }
                myPojoWoe.setBarcodelistModel(barcodelists);
                myPojoWoe.setWoe_type("WOE");
                myPojoWoe.setApi_key(api_key);//api_key

                SharedPreferences.Editor prefsEditor = getSharedPreferences("saveWOEinDraft", 0).edit();
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myPojoWoe);
                prefsEditor.putString("DraftWOE", json);
                prefsEditor.commit();
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
