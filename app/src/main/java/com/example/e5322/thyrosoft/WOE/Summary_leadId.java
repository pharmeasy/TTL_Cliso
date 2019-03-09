package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Summary_leadId extends AppCompatActivity {
    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    LinearLayoutManager linearLayoutManager;
    TextView saverepeat, saveclose, id_number, title;
    RecyclerView sample_list;
    ImageView home,back;

    SharedPreferences prefs;
    private String totalAnount;
    SharedPreferences preferences, prefe;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    String getDateTopass,getDate_and_time,get_time;
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
    ArrayList<String> getReferenceNmae;
    ArrayList<String> getLabNmae;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    private String patientYearType, user, passwrd, getFinalSrNO, api_key, status, access;
    private String patientGender;
    private String sampleCollectionTime;
    private String typeName;
    public static com.android.volley.RequestQueue POstQue;
    private ProgressDialog progressDialog;
    private String message, RES_ID, barcode_patient_id;
    private Global globalClass;
    int passvalue=0;
    private String TAG = Summary_leadId.this.getClass().getSimpleName();
    private String outputDateStr;
    String leadAddress, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE, leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData;
    private String samplCollectiondate, referenceBy, leadREF_ID, labName, leadLABAddress;
    SourceILSMainModel sourceILSMainModel;
    REF_DR[] ref_drs;
    LABS[] labs;
    int convertSrno;
    private String version;
    private int versionCode;
    private String sr_number;
    private int sr_number_pass_to_api;
    private String fulldate;

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

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        SharedPreferences prefe = getSharedPreferences("getBrandTypeandName", MODE_PRIVATE);

        typeName = prefe.getString("typeName", null);
        sr_number = prefe.getString("SR_NO",null);
        if(sr_number==null){
            sr_number_pass_to_api=Integer.parseInt("0");
        }else{
            sr_number_pass_to_api=Integer.parseInt(sr_number);
        }

        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}

//        if (typeName.equals("")) {
            title.setText("Summary");
//        } else {
//            title.setText("Summary(" + typeName + ")");
//        }

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

        SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);

        leadAddress = sharedPreferences.getString("ADDRESS", null);
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

        samplCollectiondate = leadSCT.substring(0, leadSCT.length() - 6);
        sampleCollectionTime = leadSCT.substring(11, leadSCT.length());

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.SOURCEils + "" + api_key + "/" + "" + user + "/B2BAPP/getclients", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: RESPONSE"+response);
                Gson gson = new Gson();
                getReferenceNmae = new ArrayList<>();
                getLabNmae = new ArrayList<>();
                sourceILSMainModel = gson.fromJson(response.toString(), SourceILSMainModel.class);

                if (sourceILSMainModel.getMASTERS().getLABS().length != 0) {
                    for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                        getLabNmae.add(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName());
                        labs = sourceILSMainModel.getMASTERS().getLABS();
                    }

                } else {

//                    Toast.makeText(Summary_leadId.this, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                }

                if (sourceILSMainModel.getMASTERS().getREF_DR().length != 0) {
                    for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                        getReferenceNmae.add(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName());
                        ref_drs = sourceILSMainModel.getMASTERS().getREF_DR();
                    }


                } else {

//                    Toast.makeText(Summary_leadId.this, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
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

        Log.e(TAG, "onCreate: URL"+jsonObjectRequestfetchData);

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

        pat_type.setText("TTL/" + leadTYPE);
        pat_name.setText(leadNAME + "(" + leadAGE + " " + patientYearType + "/" + patientGender + ")");
        pat_sct.setText(leadSCT);
        pat_ref.setText(leadREF_BY);
        btech.setText(leadBCT);
        pat_scp.setText(leadAddress);
        tests.setText(leadTESTS);
        id_number.setText(leadORDER_NO);

        GlobalClass.barcodelists = new ArrayList<>();

        finalspecimenttypewiselist = new ArrayList<>();
        Summary_leadId.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();
        finalspecimenttypewiselist = bundle.getParcelableArrayList("leadArraylist");
        getDateTopass =bundle.getString("Date");
        get_time = bundle.getString("Time");
        getDate_and_time = bundle.getString("DateTime");


        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelistData = new Barcodelist();
            barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            GlobalClass.barcodelists.add(barcodelistData);
        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(Summary_leadId.this, GlobalClass.barcodelists,passvalue);
        sample_list.setAdapter(getPatientSampleDetails);

        saveclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandClose();
            }
        });
    }

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = null;
        try {
            date = inputFormat.parse(time);
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11,stringofconvertedTime.length()-0);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringofconvertedTime;
    }


    private void saveandClose() {

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = inputFormat.parse(samplCollectiondate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDateStr = outputFormat.format(date);

        //sampleCollectionTime


        GlobalClass.Req_Date_Req(getDate_and_time, "hh:mm a", "HH:mm:ss");

        Log.e(TAG, "fetchData: " + outputDateStr);

        //  sendFinalWoe();
        labName = leadLAB_NAME;
        if (labName.equals("")) {
            leadLABAddress = "";
        } else {
            for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                if (labName.equals(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName())) {
                    leadLABAddress = sourceILSMainModel.getMASTERS().getLABS()[i].getLabAddress();
                }
            }
        }


        referenceBy = leadREF_BY;

        if (referenceBy != null) {

            if(sourceILSMainModel!=null ){
                if (sourceILSMainModel.getMASTERS().getREF_DR().length != 0 && sourceILSMainModel.getMASTERS().getREF_DR()!=null ){
                    for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                        if (referenceBy.equalsIgnoreCase(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName())) {
                            leadREF_ID = sourceILSMainModel.getMASTERS().getREF_DR()[j].getId().toString();
                        }else{
                            leadREF_ID="";
                        }
                    }
                }else{
                    leadREF_ID="";
                }
            }else{
                leadREF_ID="";
            }


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
        POstQue = Volley.newRequestQueue(Summary_leadId.this);

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
        woe.setBCT_ID(leadBCT);
        woe.setBRAND("TTL");

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
        woe.setSPECIMEN_COLLECTION_TIME(getDateTopass + " " + GlobalClass.cutString + ".00");
        woe.setSPECIMEN_SOURCE("");
        woe.setSR_NO(sr_number_pass_to_api);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(user);
        woe.setTOTAL_AMOUNT("");
        woe.setTYPE(leadTYPE);
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
                    Log.e(TAG, "onResponse: RESPONSE"+response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    RES_ID = parentObjectOtp.getString("RES_ID");
                    barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                    message = parentObjectOtp.getString("message");
                    status = parentObjectOtp.getString("status");
                    progressDialog.dismiss();
                    if (status.equalsIgnoreCase("SUCCESS")) {

                        TastyToast.makeText(Summary_leadId.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent i = new Intent(Summary_leadId.this, ManagingTabsActivity.class);
                        i.putExtra("passToWoefragment", "frgamnebt");
                        startActivity(i);

                    }else if(status.equalsIgnoreCase(caps_invalidApikey)){
                        GlobalClass.redirectToLogin(Summary_leadId.this);
                    } else {
                        TastyToast.makeText(Summary_leadId.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {

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
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        POstQue.add(jsonObjectRequest1);
        Log.e(TAG, "saveandClose: URL"+jsonObjectRequest1);
        Log.e(TAG, "saveandClose: URL"+jsonObj);


    }

}
