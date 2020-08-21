package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.DeleteWoe_Controller;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.DeleteWOERequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.DeleteWOEResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SummaryActivity extends AppCompatActivity {
    public static com.android.volley.RequestQueue POstQue;
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    LinearLayout SGCLinearid, ll_patient_age, ll_patient_gender;
    TextView saverepeat, title, delete_woe, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    String getSelctedTests;
    SharedPreferences prefs;
    SharedPreferences preferences;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    ArrayList<BarcodelistModel> barcodelists;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    ImageView back, home;
    DatabaseHelper myDb;
    LinearLayout btech_layout;
    private String totalAnount;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String brandName, sampleCollectionDate, sampleCollectionTime;
    private String typeName, referenceBy, sampleCollectionPoint, sampleGivingClient, getFinalSrNO;
    private String RES_ID;
    private String barcode_patient_id;
    private String alertMsg;
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
    private ArrayList<Barcodelist> barcodelists1;
    Activity mActivity;

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        mActivity = SummaryActivity.this;
        initView();

        GlobalClass.SetText(pat_sgc,sampleGivingClient);
        GlobalClass.SetText(pat_scp,sampleCollectionPoint);
        GlobalClass.SetText(pat_type,brandName + "/" + typeName + "/" + user);

        if (GlobalClass.isNull(patientYear)) {
            GlobalClass.SetText(pat_name,patientName);
            ll_patient_age.setVisibility(View.GONE);
            ll_patient_gender.setVisibility(View.GONE);
        } else {
            GlobalClass.SetText(pat_name,patientName);
            if (!GlobalClass.isNull(patientYear)) {
                ll_patient_age.setVisibility(View.VISIBLE);
                GlobalClass.SetText(txt_pat_age,patientYear + " " + patientYearType);
            } else {
                ll_patient_age.setVisibility(View.GONE);
            }
            if (!GlobalClass.isNull(patientGender)) {
                ll_patient_gender.setVisibility(View.VISIBLE);
                if (patientGender.equalsIgnoreCase("M")) {
                    GlobalClass.SetText(txt_pat_gender,"Male");
                } else if (patientGender.equalsIgnoreCase("F")) {
                    GlobalClass.SetText(txt_pat_gender,"Female");
                }
            } else {
                ll_patient_gender.setVisibility(View.GONE);
            }
        }

        sampleCollectionTime = GlobalClass.changetimeformate(sampleCollectionTime);
        GlobalClass.SetText(pat_sct,sampleCollectionDate + " " + sampleCollectionTime);
        GlobalClass.SetText(pat_ref,referenceBy);
        alertMsg = "";
        //display the current version in a TextView
        if (!GlobalClass.isNull(btechID)) {
            GlobalClass.SetText(btech,btechID);
        } else {
            btech_layout.setVisibility(View.GONE);
        }

        GlobalClass.SetText(pat_amt_collected,amountPaid);
        GlobalClass.SetText(tests,testnames);
        barcodelists1 = new ArrayList<>();

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                barcodelistData = new Barcodelist();
                barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
                barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
                barcodelists1.add(barcodelistData);
            }

        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(SummaryActivity.this, barcodelists1, pass_to_api);
        sample_list.setAdapter(getPatientSampleDetails);

        initListner();

    }

    private void initListner() {
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
                alertDialogBuilder.setTitle(MessageConstants.CONFIRM_DT);
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton(MessageConstants.YES,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (!GlobalClass.isNetworkAvailable(SummaryActivity.this)) {
                                    boolean deletedRows = myDb.deleteData(getBarcodesOffline);
                                    if (deletedRows)
                                        GlobalClass.showTastyToast(SummaryActivity.this, ToastFile.woeDelete, 1);
                                    finish();
                                    Constants.covidwoe_flag = "1";
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
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

        versionNameTopass =GlobalClass.getversion(mActivity);
        versionCode =GlobalClass.getversioncode(mActivity);

        getSelctedTests = bundle.getString("selectedTest");
        amountPaid = bundle.getString("payment");
        totalAnount = bundle.getString("TotalAmt");
        barcode_patient_id = bundle.getString("patient_id");
        finalspecimenttypewiselist = bundle.getParcelableArrayList("sendArraylist");

        preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("age", "");
        patientYearType = preferences.getString("ageType", "");
        patientGender = preferences.getString("gender", "");
        brandName = preferences.getString("WOEbrand", "");
        typeName = preferences.getString("woetype", "");
        sampleCollectionDate = preferences.getString("date", "");
        sampleCollectionTime = preferences.getString("sct", "");
        sr_number = preferences.getString("SR_NO", "");
        pass_to_api = Integer.parseInt(sr_number);
        referenceBy = preferences.getString("refBy", "");
        ////////////////////////////////////////////////////////////////////////
        sampleCollectionPoint = preferences.getString("labAddress", "");
        sampleGivingClient = preferences.getString("labname", "");
        ////////////////////////////////////////////////////////////////////////

        refeID = preferences.getString("refId", "");
        labAddress = preferences.getString("labAddress", "");
        labID = preferences.getString("labIDaddress", "");
        labName = preferences.getString("labname", "");
        btechID = preferences.getString("btechIDToPass", "");
        campID = preferences.getString("getcampIDtoPass", "");
        homeaddress = preferences.getString("patientAddress", "");
        getFinalPhoneNumberToPost = preferences.getString("kycinfo", "");
        getFinalEmailIdToPost = "";
        getPincode = "";

        GlobalClass.SetText(title,"Summary");
        getBarcodesOffline = bundle.getString("Outlbbarcodes");

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


        deletePatienDetail = Volley.newRequestQueue(SummaryActivity.this);

        JSONObject jsonObject = null;
        try {
            DeleteWOERequestModel requestModel = new DeleteWOERequestModel();
            requestModel.setApi_key(api_key);
            requestModel.setPatient_id(barcode_patient_id);
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
            ControllersGlobalInitialiser.deleteWoe_controller = new DeleteWoe_Controller(mActivity, SummaryActivity.this);
            ControllersGlobalInitialiser.deleteWoe_controller.deletewoe(jsonObject, deletePatienDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchData() {
        Constants.covidwoe_flag = "1";
        GlobalClass.setflagToRefreshData = true;
        Intent i = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
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

                if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)){
                    for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                        barcodelist = new BarcodelistModel();
                        barcodelist.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
                        barcodelist.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
                        barcodelist.setTESTS(finalspecimenttypewiselist.get(i).getProducts());
                        barcodelists.add(barcodelist);
                    }
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
        Constants.covidwoe_flag = "1";
        Intent i = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
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
                    GlobalClass.showTastyToast(SummaryActivity.this, ToastFile.woe_dlt, 1);
                    Intent intent = new Intent(SummaryActivity.this, ManagingTabsActivity.class);
                    startActivity(intent);
                } else {
                    GlobalClass.showTastyToast(SummaryActivity.this, deleteWOEResponseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(SummaryActivity.this, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
