package com.example.e5322.thyrosoft.WOE;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SourceILSModel.LABS;
import com.example.e5322.thyrosoft.SourceILSModel.REF_DR;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
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

public class Summary_leadId extends AppCompatActivity {
    public static com.android.volley.RequestQueue POstQue;
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    TextView saverepeat, saveclose, id_number, title, txt_pat_age, txt_pat_gender;
    RecyclerView sample_list;
    ImageView home, back;
    SharedPreferences prefs;
    SharedPreferences preferences, prefe;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    String getDateTopass, getDate_and_time, get_time, finalgettime;
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
    ArrayList<String> getReferenceNmae;
    ArrayList<String> getLabNmae;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    LeadOrderIdMainModel leadOrderIdMainModel;
    int passvalue = 0;
    String leadAddress, brandtype, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE, leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData;
    SourceILSMainModel sourceILSMainModel;
    REF_DR[] ref_drs;
    LABS[] labs;
    int convertSrno;
    LinearLayout ll_patient_age, ll_patient_gender;
    private String totalAnount;
    private String patientYearType, user, passwrd, getFinalSrNO, api_key, access;
    private String patientGender;
    private String sampleCollectionTime;
    private String typeName;
    private ProgressDialog progressDialog;
    private Global globalClass;
    private String TAG = Summary_leadId.this.getClass().getSimpleName();
    private String outputDateStr;
    private String samplCollectiondate, referenceBy, leadREF_ID, labName, leadLABAddress;
    private String version;
    private int versionCode;
    private String sr_number;
    private int sr_number_pass_to_api;
    private String fulldate;
    public String fromcome;

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = null;
        try {
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
        setContentView(R.layout.activity_summary_lead_id);

        pat_type = (TextView) findViewById(R.id.pat_type);
        pat_sct = (TextView) findViewById(R.id.pat_sct);
        pat_name = (TextView) findViewById(R.id.pat_name);
        pat_ref = (TextView) findViewById(R.id.pat_ref);
        pat_sgc = (TextView) findViewById(R.id.pat_sgc);
        pat_scp = (TextView) findViewById(R.id.pat_scp);
        id_number = (TextView) findViewById(R.id.id_number);
        home = (ImageView) findViewById(R.id.home);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        pat_amt_collected = (TextView) findViewById(R.id.pat_amt_collected);
        tests = (TextView) findViewById(R.id.tests);
        btech = (TextView) findViewById(R.id.btech);
        btechtile = (TextView) findViewById(R.id.btechtile);
        sample_list = (RecyclerView) findViewById(R.id.sample_list);
        saverepeat = (TextView) findViewById(R.id.saverepeat);
        saveclose = (TextView) findViewById(R.id.saveclose);
        linearLayoutManager = new LinearLayoutManager(Summary_leadId.this);
        sample_list.setLayoutManager(linearLayoutManager);
        ll_patient_age = (LinearLayout) findViewById(R.id.ll_patient_age);
        ll_patient_gender = (LinearLayout) findViewById(R.id.ll_patient_gender);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        SharedPreferences prefe = getSharedPreferences("getBrandTypeandName", MODE_PRIVATE);

        typeName = prefe.getString("typeName", null);
        sr_number = prefe.getString("SR_NO", null);
        if (sr_number == null) {
            sr_number_pass_to_api = Integer.parseInt("0");
        } else {
            sr_number_pass_to_api = Integer.parseInt(sr_number);
        }

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        title.setText("Summary");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Summary_leadId.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fromcome = getIntent().getExtras().getString("fromcome");
        leadLEAD_ID = getIntent().getExtras().getString("LeadID");
        leadOrderIdMainModel = (LeadOrderIdMainModel) getIntent().getParcelableExtra("MyClass");
        finalspecimenttypewiselist = new ArrayList<>();
        if (fromcome.equalsIgnoreCase("adapter")) {
            if (leadOrderIdMainModel != null && leadOrderIdMainModel.getLeads() != null) {
                for (int i = 0; i < leadOrderIdMainModel.getLeads().length; i++) {
                    if (leadOrderIdMainModel.getLeads()[i].getLEAD_ID().equalsIgnoreCase(leadLEAD_ID)) {

                        SharedPreferences.Editor editor = getSharedPreferences("LeadOrderID", 0).edit();
                        editor.putString("ADDRESS", leadOrderIdMainModel.getLeads()[i].getADDRESS());
                        editor.putString("AGE", leadOrderIdMainModel.getLeads()[i].getAGE());
                        editor.putString("AGE_TYPE", leadOrderIdMainModel.getLeads()[i].getAGE_TYPE());
                        editor.putString("BCT", leadOrderIdMainModel.getLeads()[i].getBCT());
                        editor.putString("EDTA", leadOrderIdMainModel.getLeads()[i].getEDTA());
                        editor.putString("EMAIL", leadOrderIdMainModel.getLeads()[i].getEMAIL());
                        editor.putString("ERROR", leadOrderIdMainModel.getLeads()[i].getERROR());
                        editor.putString("FLUORIDE", leadOrderIdMainModel.getLeads()[i].getFLUORIDE());
                        editor.putString("GENDER", leadOrderIdMainModel.getLeads()[i].getGENDER());
                        editor.putString("HEPARIN", leadOrderIdMainModel.getLeads()[i].getHEPARIN());

                        editor.putString("LAB_ID", leadOrderIdMainModel.getLeads()[i].getLAB_ID());
                        editor.putString("LAB_NAME", leadOrderIdMainModel.getLeads()[i].getLAB_NAME());
                        editor.putString("LEAD_ID", leadOrderIdMainModel.getLeads()[i].getLEAD_ID());
                        editor.putString("MOBILE", leadOrderIdMainModel.getLeads()[i].getMOBILE());
                        editor.putString("NAME", leadOrderIdMainModel.getLeads()[i].getNAME());
                        editor.putString("ORDER_NO", leadOrderIdMainModel.getLeads()[i].getORDER_NO());
                        editor.putString("PACKAGE", leadOrderIdMainModel.getLeads()[i].getPACKAGE());
                        editor.putString("PINCODE", leadOrderIdMainModel.getLeads()[i].getPINCODE());
                        editor.putString("PRODUCT", leadOrderIdMainModel.getLeads()[i].getPRODUCT());
                        editor.putString("RATE", leadOrderIdMainModel.getLeads()[i].getRATE());

                        editor.putString("REF_BY", leadOrderIdMainModel.getLeads()[i].getREF_BY());
                        editor.putString("RESPONSE", leadOrderIdMainModel.getLeads()[i].getRESPONSE());
                        editor.putString("SAMPLE_TYPE", leadOrderIdMainModel.getLeads()[i].getSAMPLE_TYPE());
                        editor.putString("SCT", leadOrderIdMainModel.getLeads()[i].getSCT());
                        editor.putString("SERUM", leadOrderIdMainModel.getLeads()[i].getSERUM());
                        editor.putString("TESTS", leadOrderIdMainModel.getLeads()[i].getTESTS());
                        editor.putString("TYPE", leadOrderIdMainModel.getLeads()[i].getTYPE());
                        editor.putString("URINE", leadOrderIdMainModel.getLeads()[i].getURINE());
                        editor.putString("WATER", leadOrderIdMainModel.getLeads()[i].getWATER());
                        String json = new Gson().toJson(leadOrderIdMainModel.getLeads()[i].getLeadData());
                        editor.putString("leadData", json);
                        editor.commit();

                        SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);
                        leadAddress = sharedPreferences.getString("ADDRESS", null);
                        brandtype = sharedPreferences.getString("brandtype", null);
                        leadAGE = sharedPreferences.getString("AGE", null);
                        leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", null);
                        leadBCT = sharedPreferences.getString("BCT", null);
                        leadEDTA = sharedPreferences.getString("EDTA", null);
                        leadEMAIL = sharedPreferences.getString("EMAIL", null);
                        leadERROR = sharedPreferences.getString("ERROR", null);
                        leadFLUORIDE = sharedPreferences.getString("FLUORIDE", null);
                        leadGENDER = sharedPreferences.getString("GENDER", null);
                        leadHEPARIN = sharedPreferences.getString("HEPARIN", null);

                        leadLAB_ID = sharedPreferences.getString("LAB_ID", null);
                        leadLAB_NAME = sharedPreferences.getString("LAB_NAME", null);
                        leadLEAD_ID = sharedPreferences.getString("LEAD_ID", null);
                        leadMOBILE = sharedPreferences.getString("MOBILE", null);
                        leadNAME = sharedPreferences.getString("NAME", null);
                        leadORDER_NO = sharedPreferences.getString("ORDER_NO", null);
                        leadPACKAGE = sharedPreferences.getString("PACKAGE", null);
                        leadPINCODE = sharedPreferences.getString("PINCODE", null);
                        leadPRODUCT = sharedPreferences.getString("PRODUCT", null);
                        leadRATE = sharedPreferences.getString("RATE", null);

                        leadREF_BY = sharedPreferences.getString("REF_BY", null);
                        leadRESPONSE = sharedPreferences.getString("RESPONSE", null);
                        leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", null);
                        leadSCT = sharedPreferences.getString("SCT", null);
                        leadSERUM = sharedPreferences.getString("SERUM", null);
                        leadTESTS = sharedPreferences.getString("TESTS", null);
                        leadTYPE = sharedPreferences.getString("TYPE", null);
                        leadURINE = sharedPreferences.getString("URINE", null);
                        leadWATER = sharedPreferences.getString("WATER", null);
                        leadleadData = sharedPreferences.getString("leadData", null);
                    }
                }
            }

        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);
            leadAddress = sharedPreferences.getString("ADDRESS", null);
            brandtype = sharedPreferences.getString("brandtype", null);
            leadAGE = sharedPreferences.getString("AGE", null);
            leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", null);
            leadBCT = sharedPreferences.getString("BCT", null);
            leadEDTA = sharedPreferences.getString("EDTA", null);
            leadEMAIL = sharedPreferences.getString("EMAIL", null);
            leadERROR = sharedPreferences.getString("ERROR", null);
            leadFLUORIDE = sharedPreferences.getString("FLUORIDE", null);
            leadGENDER = sharedPreferences.getString("GENDER", null);
            leadHEPARIN = sharedPreferences.getString("HEPARIN", null);

            leadLAB_ID = sharedPreferences.getString("LAB_ID", null);
            leadLAB_NAME = sharedPreferences.getString("LAB_NAME", null);
            leadLEAD_ID = sharedPreferences.getString("LEAD_ID", null);
            leadMOBILE = sharedPreferences.getString("MOBILE", null);
            leadNAME = sharedPreferences.getString("NAME", null);
            leadORDER_NO = sharedPreferences.getString("ORDER_NO", null);
            leadPACKAGE = sharedPreferences.getString("PACKAGE", null);
            leadPINCODE = sharedPreferences.getString("PINCODE", null);
            leadPRODUCT = sharedPreferences.getString("PRODUCT", null);
            leadRATE = sharedPreferences.getString("RATE", null);

            leadREF_BY = sharedPreferences.getString("REF_BY", null);
            leadRESPONSE = sharedPreferences.getString("RESPONSE", null);
            leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", null);
            leadSCT = sharedPreferences.getString("SCT", null);
            leadSERUM = sharedPreferences.getString("SERUM", null);
            leadTESTS = sharedPreferences.getString("TESTS", null);
            leadTYPE = sharedPreferences.getString("TYPE", null);
            leadURINE = sharedPreferences.getString("URINE", null);
            leadWATER = sharedPreferences.getString("WATER", null);
            leadleadData = sharedPreferences.getString("leadData", null);
        }


        // Added leadSCT ! = null &&
        if (leadSCT != null && leadSCT.length() != 0) {
            samplCollectiondate = leadSCT.substring(0, leadSCT.length() - 6);
            sampleCollectionTime = leadSCT.substring(11, leadSCT.length());
        }


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
        RequestQueue requestQueue = GlobalClass.setVolleyReq(this);

        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + api_key + "/" + "" + user + "/B2BAPP/getclients", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: RESPONSE" + response);
                Gson gson = new Gson();
                getReferenceNmae = new ArrayList<>();
                getLabNmae = new ArrayList<>();
                sourceILSMainModel = gson.fromJson(response.toString(), SourceILSMainModel.class);

                if (sourceILSMainModel != null) {
                    if (GlobalClass.checkEqualIgnoreCase(sourceILSMainModel.getRES_ID(), Constants.RES0000)) {
                        if (sourceILSMainModel.getMASTERS() != null) {
                            if (GlobalClass.isArrayNotNull(sourceILSMainModel.getMASTERS().getLABS())) {
                                for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                                    getLabNmae.add(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName());
                                    labs = sourceILSMainModel.getMASTERS().getLABS();
                                }
                            }
                            if (GlobalClass.isArrayNotNull(sourceILSMainModel.getMASTERS().getREF_DR())) {
                                for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                                    getReferenceNmae.add(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName());
                                    ref_drs = sourceILSMainModel.getMASTERS().getREF_DR();
                                }
                            }
                        } else {
                            GlobalClass.showTastyToast(Summary_leadId.this, sourceILSMainModel.getRESPONSE(), 2);
                        }
                    } else if (GlobalClass.checkEqualIgnoreCase(sourceILSMainModel.getRESPONSE(), caps_invalidApikey)) {
                        GlobalClass.showTastyToast(Summary_leadId.this, sourceILSMainModel.getRESPONSE(), 2);
                        GlobalClass.redirectToLogin(Summary_leadId.this);
                    } else {
                        GlobalClass.showTastyToast(Summary_leadId.this, sourceILSMainModel.getRESPONSE(), 2);
                    }
                } else {
                    GlobalClass.showTastyToast(Summary_leadId.this, MessageConstants.SOMETHING_WENT_WRONG, 2);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        requestQueue.add(jsonObjectRequestfetchData);

        Log.e(TAG, "onCreate: URL" + jsonObjectRequestfetchData);

        if (leadAGE_TYPE != null) {
            if (leadAGE_TYPE.equals("Y")) {
                patientYearType = "Year";
            } else if (leadAGE_TYPE.equals("M")) {
                patientYearType = "Month";
            } else if (leadAGE_TYPE.equals("D")) {
                patientYearType = "Days";
            }
        }
        if (leadGENDER != null) {
            if (leadGENDER.equals("F")) {
                patientGender = "Female";
            } else if (leadGENDER.equals("M")) {
                patientGender = "Male";

            }
        }

        pat_type.setText(brandtype + "/" + leadTYPE);
        pat_name.setText(leadNAME);
        pat_sct.setText(leadSCT);
        pat_ref.setText(leadREF_BY);
        btech.setText(leadBCT);
        pat_scp.setText(leadAddress);

        id_number.setText(leadORDER_NO);

        if (leadAGE != null && !leadAGE.equalsIgnoreCase("") && patientYearType != null && !patientYearType.equalsIgnoreCase("")) {
            ll_patient_age.setVisibility(View.VISIBLE);
            txt_pat_age.setText(leadAGE + " " + patientYearType);
        } else {
            ll_patient_age.setVisibility(View.GONE);
        }
        if (patientGender != null && !patientGender.equalsIgnoreCase("")) {
            ll_patient_gender.setVisibility(View.VISIBLE);
            txt_pat_gender.setText(patientGender);
        } else {
            ll_patient_gender.setVisibility(View.GONE);
        }

        GlobalClass.barcodelists = new ArrayList<>();

        Summary_leadId.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();

        finalspecimenttypewiselist = bundle.getParcelableArrayList("leadArraylist");
        leadTESTS = "";

        if (finalspecimenttypewiselist != null && finalspecimenttypewiselist.size() > 0) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                leadTESTS += finalspecimenttypewiselist.get(i).getProducts() + ",";
                Log.e(TAG, "leadTESTS---" + leadTESTS);
            }
            try {
                if (leadTESTS.endsWith(",")) {
                    leadTESTS = leadTESTS.substring(0, leadTESTS.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tests.setText(leadTESTS);
        }
        getDateTopass = bundle.getString("Date");
        get_time = bundle.getString("Time");
        getDate_and_time = bundle.getString("DateTime");
        if (get_time.contains("AM")) {
            finalgettime = get_time.replaceAll("AM", "");
        } else {
            finalgettime = get_time.replaceAll("PM", "");
        }

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelistData = new Barcodelist();
            barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            GlobalClass.barcodelists.add(barcodelistData);
        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(Summary_leadId.this, GlobalClass.barcodelists, passvalue);
        sample_list.setAdapter(getPatientSampleDetails);

        saveclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandClose();
            }
        });

    }

    private void saveandClose() {
        try {
            labName = leadLAB_NAME;
            if (labName.equals("")) {
                leadLABAddress = "";
            } else {
                if (sourceILSMainModel != null) {
                    if (sourceILSMainModel.getMASTERS() != null) {
                        if (sourceILSMainModel.getMASTERS().getLABS() != null) {
                            for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                                if (labName.equals(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName())) {
                                    leadLABAddress = sourceILSMainModel.getMASTERS().getLABS()[i].getLabAddress();
                                }
                            }
                        }
                    }
                }


            }

            referenceBy = leadREF_BY;

            if (referenceBy != null) {
                if (sourceILSMainModel != null) {
                    if (sourceILSMainModel.getMASTERS() != null) {
                        if (sourceILSMainModel.getMASTERS().getREF_DR().length != 0 && sourceILSMainModel.getMASTERS().getREF_DR() != null) {
                            for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                                if (referenceBy.equalsIgnoreCase(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName())) {
                                    leadREF_ID = sourceILSMainModel.getMASTERS().getREF_DR()[j].getId().toString();
                                } else {
                                    leadREF_ID = "";
                                }
                            }
                        } else {
                            leadREF_ID = "";
                        }
                    }

                } else {
                    leadREF_ID = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(Summary_leadId.this);
        progressDialog.setTitle("Kindly wait ...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        POstQue = GlobalClass.setVolleyReq(Summary_leadId.this);

        MyPojoWoe myPojoWoe = new MyPojoWoe();
        Woe woe = new Woe();
        woe.setAADHAR_NO("");
        woe.setADDRESS(leadAddress);
        woe.setAGE(leadAGE);
        woe.setAGE_TYPE(leadAGE_TYPE);
        woe.setALERT_MESSAGE("");
        woe.setAMOUNT_COLLECTED(leadRATE);
        woe.setAMOUNT_DUE("");
        woe.setAPP_ID(version);
        woe.setADDITIONAL1("CPL");
        woe.setBCT_ID(leadBCT);
        woe.setBRAND(brandtype);
        woe.setCAMP_ID("");
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(leadMOBILE);
        woe.setCUSTOMER_ID("");
        woe.setDELIVERY_MODE(2);
        woe.setEMAIL_ID(leadEMAIL);
        woe.setENTERED_BY(user);
        woe.setGENDER(leadGENDER);
        woe.setLAB_ADDRESS(leadAddress);
        woe.setLAB_ID(leadLAB_ID);
        woe.setLAB_NAME(leadLAB_NAME);
        woe.setLEAD_ID(leadLEAD_ID);
        woe.setMAIN_SOURCE(user);
        woe.setORDER_NO(leadORDER_NO);
        woe.setOS("unknown");
        woe.setPATIENT_NAME(leadNAME);
        woe.setPINCODE(leadPINCODE);
        woe.setPRODUCT(leadTESTS);
        woe.setPurpose("mobile application");
        woe.setREF_DR_ID(leadREF_ID);
        woe.setREF_DR_NAME(leadREF_BY);
        woe.setREMARKS("MOBILE");
        woe.setSPECIMEN_COLLECTION_TIME(getDateTopass + " " + finalgettime.trim() + ":00.000");
        woe.setSPECIMEN_SOURCE("");
        woe.setSR_NO(sr_number_pass_to_api);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(user);
        woe.setTOTAL_AMOUNT("");
        woe.setTYPE(leadTYPE);
        woe.setWATER_SOURCE("");
        woe.setWO_MODE("CLISO APP");
        woe.setWO_STAGE(3);
        woe.setULCcode("");
        myPojoWoe.setWoe(woe);
        barcodelists = new ArrayList<>();

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelist = new BarcodelistModel();
            if (GlobalClass.checkEqualIgnoreCase(finalspecimenttypewiselist.get(i).getSpecimen_type(), "(PPBS)FLUORIDE") || GlobalClass.checkEqualIgnoreCase(finalspecimenttypewiselist.get(i).getSpecimen_type(), "(FBS)FLUORIDE")) {
                barcodelist.setSAMPLE_TYPE("FLUORIDE");
            } else {
                barcodelist.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            }
            barcodelist.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            barcodelist.setTESTS(finalspecimenttypewiselist.get(i).getProducts());
            barcodelists.add(barcodelist);
        }

        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WOE");
        myPojoWoe.setApi_key(api_key);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntry, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: RESPONSE" + response);
                    Gson woeGson = new Gson();
                    WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);
                    progressDialog.dismiss();
                    if (woeResponseModel != null) {
                        if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                            TastyToast.makeText(Summary_leadId.this, woeResponseModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Intent i = new Intent(Summary_leadId.this, ManagingTabsActivity.class);
                            i.putExtra("passToWoefragment", "frgamnebt");
                            startActivity(i);
                        } else if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(Summary_leadId.this);
                        } else {
                            TastyToast.makeText(Summary_leadId.this, woeResponseModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(Summary_leadId.this, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {
                    //Log.v(TAG,error);
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
        POstQue.add(jsonObjectRequest1);
        Log.e(TAG, "saveandClose: URL" + jsonObjectRequest1);
        Log.e(TAG, "saveandClose: URL" + jsonObj);
    }


}
