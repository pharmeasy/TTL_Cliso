package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.ClientController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.LeadWoeController;
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

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

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
    LinearLayout ll_patient_age, ll_patient_gender;
    private String patientYearType, user, passwrd, api_key, access;
    private String patientGender;
    private String sampleCollectionTime;
    private String typeName;
    private Global globalClass;
    private String TAG = Summary_leadId.this.getClass().getSimpleName();
    private String samplCollectiondate, referenceBy, leadREF_ID, labName, leadLABAddress;
    private String version;
    private int versionCode;
    private String sr_number;
    private int sr_number_pass_to_api;
    public String fromcome;
    private ArrayList<Barcodelist> barcodelists1;
    Activity mActivity;

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
        mActivity = Summary_leadId.this;
        initView();
        getClient();
        initListner();


    }

    private void getClient() {
        String url = Api.SOURCEils + "" + api_key + "/" + "" + user + "/B2BAPP/getclients";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        try {
            if (ControllersGlobalInitialiser.clientController != null) {
                ControllersGlobalInitialiser.clientController = null;
            }
            ControllersGlobalInitialiser.clientController = new ClientController(mActivity, Summary_leadId.this);
            ControllersGlobalInitialiser.clientController.getclientController(url, requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!GlobalClass.isNull(leadAGE_TYPE)) {
            if (leadAGE_TYPE.equals("Y")) {
                patientYearType = "Year";
            } else if (leadAGE_TYPE.equals("M")) {
                patientYearType = "Month";
            } else if (leadAGE_TYPE.equals("D")) {
                patientYearType = "Days";
            }
        }
        if (!GlobalClass.isNull(leadGENDER)) {
            if (leadGENDER.equals("F")) {
                patientGender = "Female";
            } else if (leadGENDER.equals("M")) {
                patientGender = "Male";

            }
        }


        GlobalClass.SetText(pat_type, brandtype + "/" + leadTYPE);
        GlobalClass.SetText(pat_name, leadNAME);
        GlobalClass.SetText(pat_sct, leadSCT);
        GlobalClass.SetText(pat_ref, leadREF_BY);
        GlobalClass.SetText(btech, leadBCT);
        GlobalClass.SetText(pat_scp, leadAddress);
        GlobalClass.SetText(id_number, leadORDER_NO);

        if (!GlobalClass.isNull(leadAGE)) {
            ll_patient_age.setVisibility(View.VISIBLE);
            GlobalClass.SetText(txt_pat_age, leadAGE + " " + patientYearType);
        } else {
            ll_patient_age.setVisibility(View.GONE);
        }
        if (!GlobalClass.isNull(patientGender)) {
            ll_patient_gender.setVisibility(View.VISIBLE);
            GlobalClass.SetText(txt_pat_gender, patientGender);
        } else {
            ll_patient_gender.setVisibility(View.GONE);
        }


        Summary_leadId.this.setTitle("Summary");
        Bundle bundle = getIntent().getExtras();

        finalspecimenttypewiselist = bundle.getParcelableArrayList("leadArraylist");
        leadTESTS = "";

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                leadTESTS += finalspecimenttypewiselist.get(i).getProducts() + ",";
                Log.e(TAG, "leadTESTS---" + leadTESTS);
            }
            try {
                if (!GlobalClass.isNull(leadTESTS)&& leadTESTS.endsWith(",")) {
                    leadTESTS = leadTESTS.substring(0, leadTESTS.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            GlobalClass.SetText(tests, leadTESTS);
        }
        getDateTopass = bundle.getString("Date");
        get_time = bundle.getString("Time");
        getDate_and_time = bundle.getString("DateTime");

        if (!GlobalClass.isNull(get_time)){
            if (get_time.contains("AM")) {
                finalgettime = get_time.replaceAll("AM", "");
            } else {
                finalgettime = get_time.replaceAll("PM", "");
            }
        }


        barcodelists1 = new ArrayList<>();

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                barcodelistData = new Barcodelist();
                barcodelistData.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
                barcodelistData.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
                barcodelists1.add(barcodelistData);
            }

        }

        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(Summary_leadId.this, barcodelists1, passvalue);
        sample_list.setAdapter(getPatientSampleDetails);

    }

    private void initListner() {
        saveclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandClose();
            }
        });
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

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
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        SharedPreferences prefe = getSharedPreferences("getBrandTypeandName", MODE_PRIVATE);

        typeName = prefe.getString("typeName", "");
        sr_number = prefe.getString("SR_NO", "");
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

        GlobalClass.SetText(title, "Summary");

        fromcome = getIntent().getExtras().getString("fromcome");
        leadLEAD_ID = getIntent().getExtras().getString("LeadID");
        leadOrderIdMainModel = (LeadOrderIdMainModel) getIntent().getParcelableExtra("MyClass");
        finalspecimenttypewiselist = new ArrayList<>();

        if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("adapter")) {
            if (leadOrderIdMainModel != null && GlobalClass.checkArray(leadOrderIdMainModel.getLeads())) {

                for (int i = 0; i < leadOrderIdMainModel.getLeads().length; i++) {

                    if (!GlobalClass.isNull(leadOrderIdMainModel.getLeads()[i].getLEAD_ID()) &&
                            !GlobalClass.isNull(leadLEAD_ID) &&
                            leadOrderIdMainModel.getLeads()[i].getLEAD_ID().equalsIgnoreCase(leadLEAD_ID)) {

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
                        leadAddress = sharedPreferences.getString("ADDRESS", "");
                        brandtype = sharedPreferences.getString("brandtype", "");
                        leadAGE = sharedPreferences.getString("AGE", "");
                        leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", "");
                        leadBCT = sharedPreferences.getString("BCT", "");
                        leadEDTA = sharedPreferences.getString("EDTA", "");
                        leadEMAIL = sharedPreferences.getString("EMAIL", "");
                        leadERROR = sharedPreferences.getString("ERROR", "");
                        leadFLUORIDE = sharedPreferences.getString("FLUORIDE", "");
                        leadGENDER = sharedPreferences.getString("GENDER", "");
                        leadHEPARIN = sharedPreferences.getString("HEPARIN", "");

                        leadLAB_ID = sharedPreferences.getString("LAB_ID", "");
                        leadLAB_NAME = sharedPreferences.getString("LAB_NAME", "");
                        leadLEAD_ID = sharedPreferences.getString("LEAD_ID", "");
                        leadMOBILE = sharedPreferences.getString("MOBILE", "");
                        leadNAME = sharedPreferences.getString("NAME", "");
                        leadORDER_NO = sharedPreferences.getString("ORDER_NO", "");
                        leadPACKAGE = sharedPreferences.getString("PACKAGE", "");
                        leadPINCODE = sharedPreferences.getString("PINCODE", "");
                        leadPRODUCT = sharedPreferences.getString("PRODUCT", "");
                        leadRATE = sharedPreferences.getString("RATE", "");

                        leadREF_BY = sharedPreferences.getString("REF_BY", "");
                        leadRESPONSE = sharedPreferences.getString("RESPONSE", "");
                        leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", "");
                        leadSCT = sharedPreferences.getString("SCT", "");
                        leadSERUM = sharedPreferences.getString("SERUM", "");
                        leadTESTS = sharedPreferences.getString("TESTS", "");
                        leadTYPE = sharedPreferences.getString("TYPE", "");
                        leadURINE = sharedPreferences.getString("URINE", "");
                        leadWATER = sharedPreferences.getString("WATER", "");
                        leadleadData = sharedPreferences.getString("leadData", null);
                    }
                }
            }

        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);
            leadAddress = sharedPreferences.getString("ADDRESS", "");
            brandtype = sharedPreferences.getString("brandtype", "");
            leadAGE = sharedPreferences.getString("AGE", "");
            leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", "");
            leadBCT = sharedPreferences.getString("BCT", "");
            leadEDTA = sharedPreferences.getString("EDTA", "");
            leadEMAIL = sharedPreferences.getString("EMAIL", "");
            leadERROR = sharedPreferences.getString("ERROR", "");
            leadFLUORIDE = sharedPreferences.getString("FLUORIDE", "");
            leadGENDER = sharedPreferences.getString("GENDER", "");
            leadHEPARIN = sharedPreferences.getString("HEPARIN", "");

            leadLAB_ID = sharedPreferences.getString("LAB_ID", "");
            leadLAB_NAME = sharedPreferences.getString("LAB_NAME", "");
            leadLEAD_ID = sharedPreferences.getString("LEAD_ID", "");
            leadMOBILE = sharedPreferences.getString("MOBILE", "");
            leadNAME = sharedPreferences.getString("NAME", "");
            leadORDER_NO = sharedPreferences.getString("ORDER_NO", "");
            leadPACKAGE = sharedPreferences.getString("PACKAGE", "");
            leadPINCODE = sharedPreferences.getString("PINCODE", "");
            leadPRODUCT = sharedPreferences.getString("PRODUCT", "");
            leadRATE = sharedPreferences.getString("RATE", "");

            leadREF_BY = sharedPreferences.getString("REF_BY", "");
            leadRESPONSE = sharedPreferences.getString("RESPONSE", "");
            leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", "");
            leadSCT = sharedPreferences.getString("SCT", "");
            leadSERUM = sharedPreferences.getString("SERUM", "");
            leadTESTS = sharedPreferences.getString("TESTS", "");
            leadTYPE = sharedPreferences.getString("TYPE", "");
            leadURINE = sharedPreferences.getString("URINE", "");
            leadWATER = sharedPreferences.getString("WATER", "");
            leadleadData = sharedPreferences.getString("leadData", "");
        }

        if (!GlobalClass.isNull(leadSCT)) {
            samplCollectiondate = leadSCT.substring(0, leadSCT.length() - 6);
            sampleCollectionTime = leadSCT.substring(11, leadSCT.length());
        }


        version =GlobalClass.getversion(mActivity);
        versionCode = GlobalClass.getversioncode(mActivity);


    }

    private void saveandClose() {

        labName = leadLAB_NAME;
        if (GlobalClass.isNull(labName)) {
            leadLABAddress = "";
        } else {
            if (GlobalClass.checkArray(sourceILSMainModel.getMASTERS().getLABS())) {
                for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                    if (!GlobalClass.isNull(labName) &&
                            !GlobalClass.isNull(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName()) &&
                            labName.equals(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName())) {
                        leadLABAddress = sourceILSMainModel.getMASTERS().getLABS()[i].getLabAddress();
                    }
                }
            }
        }

        referenceBy = leadREF_BY;

        if (!GlobalClass.isNull(referenceBy)) {
            if (sourceILSMainModel != null) {
                if (GlobalClass.checkArray(sourceILSMainModel.getMASTERS().getREF_DR())) {
                    for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                        if (!GlobalClass.isNull(referenceBy) && !GlobalClass.isNull(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName()) &&
                                referenceBy.equalsIgnoreCase(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName())) {
                            leadREF_ID = sourceILSMainModel.getMASTERS().getREF_DR()[j].getId();
                        } else {
                            leadREF_ID = "";
                        }
                    }
                } else {
                    leadREF_ID = "";
                }
            } else {
                leadREF_ID = "";
            }
        }


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
        woe.setWO_MODE("THYROSOFTLITE APP");
        woe.setWO_STAGE(3);
        woe.setULCcode("");
        myPojoWoe.setWoe(woe);
        barcodelists = new ArrayList<>();

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
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
        myPojoWoe.setApi_key(api_key);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (GlobalClass.isNetworkAvailable(mActivity)) {
            try {
                if (ControllersGlobalInitialiser.leadWoeController != null) {
                    ControllersGlobalInitialiser.leadWoeController = null;
                }
                ControllersGlobalInitialiser.leadWoeController = new LeadWoeController(mActivity, Summary_leadId.this);
                ControllersGlobalInitialiser.leadWoeController.getleadwoeController(jsonObj, POstQue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

    public void doleadwoe(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            Gson woeGson = new Gson();
            WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);

            if (woeResponseModel != null) {
                if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                    GlobalClass.showTastyToast(Summary_leadId.this, woeResponseModel.getMessage(), 1);
                    Intent i = new Intent(Summary_leadId.this, ManagingTabsActivity.class);
                    i.putExtra("passToWoefragment", "frgamnebt");
                    startActivity(i);
                } else if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Summary_leadId.this);
                } else {
                    GlobalClass.showTastyToast(Summary_leadId.this, woeResponseModel.getMessage(), 2);
                }
            } else {
                GlobalClass.showTastyToast(Summary_leadId.this, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getclientResponse(JSONObject response) {

        Log.e(TAG, "onResponse: RESPONSE" + response);
        Gson gson = new Gson();
        getReferenceNmae = new ArrayList<>();
        getLabNmae = new ArrayList<>();
        sourceILSMainModel = gson.fromJson(response.toString(), SourceILSMainModel.class);

        if (sourceILSMainModel != null && sourceILSMainModel.getMASTERS() != null &&
                GlobalClass.checkArray(sourceILSMainModel.getMASTERS().getLABS())) {
            for (int i = 0; i < sourceILSMainModel.getMASTERS().getLABS().length; i++) {
                getLabNmae.add(sourceILSMainModel.getMASTERS().getLABS()[i].getLabName());
                labs = sourceILSMainModel.getMASTERS().getLABS();
            }
        }

        if (sourceILSMainModel != null && sourceILSMainModel.getMASTERS() != null && GlobalClass.checkArray(sourceILSMainModel.getMASTERS().getREF_DR())) {
            for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                getReferenceNmae.add(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName());
                ref_drs = sourceILSMainModel.getMASTERS().getREF_DR();
            }
        }
    }
}
