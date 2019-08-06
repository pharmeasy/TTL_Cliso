package com.example.e5322.thyrosoft.SpecialOffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcode_New;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.SummaryActivity_New;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class OfferScan_Activity extends AppCompatActivity implements RecyclerInterface, View.OnClickListener {
    ArrayList<BaseModel.Barcodes> barcodesArrayList;
    public String TAG = getClass().getSimpleName();
    AdapterBarcode_New adapterBarcode;
    ArrayList<BaseModel> selctedTest = new ArrayList<>();
    RecyclerView recycler_barcode;
    public static ArrayList<String> labAlerts;
    private MyPojo myPojo;
    public IntentIntegrator scanIntegrator;
    TextView lab_alert_spin;
    public String specimenttype1;
    public int position1 = 0;
    private SpinnerDialog spinnerDialog;
    int payment;
    private String referenceID, referrredBy;
    TextView setAmt;
    ArrayList<String> saveLocation;
    private int collectedAmt;
    private int totalAmount, versionCode;
    String setLocation = "RPL";
    LinearLayout lin_labalert;

    private String nameString, barcode_patient_id;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    ArrayList<BarcodelistModel> barcodelists;
    ArrayList<String> getBarcodeArrList;
    BarcodelistModel barcodelist;
    boolean flagcallonce = false;
    private String getageType;
    private String testsnames;
    private String genderType;
    private String getFinalDate, user, passwrd, access, api_key, status, message;
    SharedPreferences savepatientDetails, preferences;
    SharedPreferences prefs;
    String labname, tspaddress, pincode;
    ProgressDialog progressDialog;
    String labAddress;
    private boolean barcodeExistsFlag = false;
    private String getRemark;
    private String getBrand_name;
    private String sampleCollectionDate;
    private String getPincode, ac_code;
    private String outputDateStr;
    private String ageType;
    private String patientAddress;
    private String lab_alert_pass_toApi;
    Button btn_next;
    TextView show_selected_tests_data;
    String getTestSelection, totalamt, type;
    private String getCollectedAmt = "";
    private EditText enterAmt;
    private RadioGroup location_radio_grp;
    public static com.android.volley.RequestQueue POstQue;
    RadioButton cpl_rdo, rpl_rdo;
    private String version;
    private String btechIDToPass, contactno, btechNameToPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);

        if (getIntent().getExtras() != null) {
            GlobalClass.finalspecimenttypewiselist.clear();
            barcodesArrayList = getIntent().getParcelableArrayListExtra("barcodelist");
            testsnames = getIntent().getExtras().getString("ts_name");
            payment = getIntent().getExtras().getInt("payment");
            type = getIntent().getExtras().getString("type");
            contactno = getIntent().getExtras().getString("contactno");
            Log.e(TAG, " --- " + barcodesArrayList.get(0).getSpecimen_type() + " " + type);
        }

        BaseModel baseModel = new BaseModel();
        baseModel.setName(testsnames);
        selctedTest.add(baseModel);

        initView();

    }

    private void initView() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;

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
        patientAddress = savepatientDetails.getString("patientAddress", null);
        referenceID = savepatientDetails.getString("refId", null);
        referrredBy = savepatientDetails.getString("refBy", null);
        labname = savepatientDetails.getString("labname", null);
        labAddress = savepatientDetails.getString("labAddress", null);
        btechNameToPass = savepatientDetails.getString("btechNameToPass", null);

        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        show_selected_tests_data.setText(testsnames);
        btn_next = findViewById(R.id.next);
        btn_next.setOnClickListener(this);

        enterAmt = (EditText) findViewById(R.id.enterAmt);

        cpl_rdo = (RadioButton) findViewById(R.id.cpl_rdo);
        cpl_rdo.setChecked(true);
        rpl_rdo = (RadioButton) findViewById(R.id.rpl_rdo);

        location_radio_grp = (RadioGroup) findViewById(R.id.location_radio_grp);
        location_radio_grp.setVisibility(View.GONE);

        lin_labalert = findViewById(R.id.lin_labalert);

      /*  if (saveLocation != null && !saveLocation.isEmpty()) {
            for (int i = 0; i < saveLocation.size(); i++) {
                if (saveLocation.contains("CPL")) {
                    location_radio_grp.setVisibility(View.GONE);
                    setLocation = "CPL";
                } else {
                    location_radio_grp.setVisibility(View.VISIBLE);
                    setLocation = null;
                }
            }
        }*/

        setAmt = findViewById(R.id.setAmt);
        setAmt.setText("" + payment);

        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
        lab_alert_spin.setVisibility(View.GONE);
        lin_labalert.setVisibility(View.GONE);

       /* lab_alert_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });*/

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(OfferScan_Activity.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();

        try {
            if (myPojo.getMASTERS().getLAB_ALERTS() != null && myPojo.getMASTERS() != null) {
                for (int i = 0; i < myPojo.getMASTERS().getLAB_ALERTS().length; i++) {
                    labAlerts.add(myPojo.getMASTERS().getLAB_ALERTS()[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        spinnerDialog = new SpinnerDialog(OfferScan_Activity.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                lab_alert_spin.setText(s);
            }
        });*/


        if (barcodesArrayList != null) {
            for (int i = 0; i < barcodesArrayList.size(); i++) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                scannedBarcodeDetails.setBarcode(barcodesArrayList.get(0).getCode());
                scannedBarcodeDetails.setSpecimen_type(barcodesArrayList.get(0).getSpecimen_type());
                GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
            }
        }


        rpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation = "RPL";
            }
        });

        cpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation = "CPL";
            }
        });

        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        recycler_barcode.setLayoutManager(new LinearLayoutManager(OfferScan_Activity.this));
        adapterBarcode = new AdapterBarcode_New(OfferScan_Activity.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (GlobalClass.specimenttype != null) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_barcode.setAdapter(adapterBarcode);
        adapterBarcode.notifyDataSetChanged();

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public void putDataToscan(Activity activity, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();
    }

    public void getUploadFileResponse() {
        TastyToast.makeText(OfferScan_Activity.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        Intent intent = new Intent(OfferScan_Activity.this, SummaryActivity_New.class);
        Bundle bundle = new Bundle();
        bundle.putString("tetsts", getTestSelection);
        bundle.putString("location", setLocation);
        bundle.putString("passProdcucts", testsnames);
        bundle.putString("TotalAmt", totalamt);
        bundle.putString("CollectedAmt", enterAmt.getText().toString());
        bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
        bundle.putString("patientId", barcode_patient_id);
        bundle.putString("draft", "");
        bundle.putString("fromcome", "offerscreen");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void doFinalWoe() {

        if (GlobalClass.finalspecimenttypewiselist != null) {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() == null) {
                    barcodeExistsFlag = true;
                }
            }

            if (barcodeExistsFlag) {
                barcodeExistsFlag = false;
                Toast.makeText(OfferScan_Activity.this, ToastFile.scan_barcode_all, Toast.LENGTH_SHORT).show();
            } else {
                if (getRemark == null) {
                    getRemark = "Manual";
                }

                savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
                nameString = savepatientDetails.getString("name", null);
                getFinalAge = savepatientDetails.getString("age", null);
                saveGenderId = savepatientDetails.getString("gender", null);
                getFinalTime = savepatientDetails.getString("sct", null);
                getageType = savepatientDetails.getString("ageType", null);
                getFinalDate = savepatientDetails.getString("date", null);
                btechIDToPass = savepatientDetails.getString("btechIDToPass", null);
                getBrand_name = savepatientDetails.getString("WOEbrand", null);
                getPincode = savepatientDetails.getString("pincode", null);
                sampleCollectionDate = getFinalDate;

                preferences = getSharedPreferences("profile", 0);
                tspaddress = preferences.getString("address", "");
                pincode = preferences.getString("pincode", "");
                ac_code = preferences.getString("ac_code", null);

                String getOnlyTime = getFinalTime.substring(0, getFinalTime.length() - 3);
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;

                try {
                    date = inputFormat.parse(sampleCollectionDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                outputDateStr = outputFormat.format(date);

                //sampleCollectionTime
                Log.e(TAG, "fetchData: " + outputDateStr);
                if (getageType.equalsIgnoreCase("Years")) {
                    ageType = "Y";
                } else if (getageType.equalsIgnoreCase("Months")) {
                    ageType = "M";
                } else if (getageType.equalsIgnoreCase("Days")) {
                    ageType = "D";
                }

                progressDialog = GlobalClass.ShowprogressDialog(OfferScan_Activity.this);

                String getFulltime = sampleCollectionDate + " " + getFinalTime;
                GlobalClass.Req_Date_Req(getFulltime, "hh:mm a", "HH:mm:ss");
                Log.e(TAG, " getFinalTime---->" + getFinalTime + "  getOnlyTime---> " + getOnlyTime + " ---- cutString ---->" + GlobalClass.cutString);


                MyPojoWoe myPojoWoe = new MyPojoWoe();
                Woe woe = new Woe();
                woe.setADDITIONAL1(setLocation);
                woe.setMAIN_SOURCE(user);
                woe.setSUB_SOURCE_CODE(user);
                woe.setTYPE(type);
                woe.setREF_DR_ID(referenceID);
                woe.setREF_DR_NAME(referrredBy);
                woe.setPATIENT_NAME(nameString);
                woe.setCONTACT_NO(contactno);
                woe.setADDRESS(tspaddress);
                woe.setPINCODE(pincode);
                woe.setAGE(getFinalAge);
                woe.setAGE_TYPE(ageType);
                woe.setGENDER(saveGenderId);
                woe.setEMAIL_ID("");
                woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
                woe.setBRAND("TTL");
                woe.setAMOUNT_COLLECTED(enterAmt.getText().toString());
                woe.setREMARKS("MOBILE - " + getRemark);
                woe.setSR_NO(0);
                woe.setPRODUCT(testsnames);
                woe.setLAB_ADDRESS(tspaddress);
                woe.setBCT_ID(ac_code);
                woe.setENTERED_BY(user);
                woe.setWO_MODE("CLISO OFFER APP");
                woe.setWO_STAGE(3);
                woe.setAADHAR_NO("");


                barcodelists = new ArrayList<>();
                getBarcodeArrList = new ArrayList<>();

                for (int p = 0; p < GlobalClass.finalspecimenttypewiselist.size(); p++) {
                    barcodelist = new BarcodelistModel();
                    barcodelist.setSAMPLE_TYPE(GlobalClass.finalspecimenttypewiselist.get(p).getSpecimen_type());
                    barcodelist.setBARCODE(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());


                    getBarcodeArrList.add(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());
                    barcodelist.setTESTS(testsnames);
                    barcodelists.add(barcodelist);
                }

                myPojoWoe.setBarcodelistModel(barcodelists);
                myPojoWoe.setWoe_type("WOE");
                myPojoWoe.setApi_key(api_key);//api_key
                myPojoWoe.setWoe(woe);


                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myPojoWoe);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (!GlobalClass.isNetworkAvailable(OfferScan_Activity.this)) {


                } else {
                    if (flagcallonce == false) {

                        flagcallonce = true;

                        if (POstQue == null) {
                            POstQue = Volley.newRequestQueue(OfferScan_Activity.this);
                        }

                        Log.e(TAG, "Post WOE Entry ---->" + jsonObj.toString());

                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.e(TAG, "onResponse: RESPONSE" + response);

                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                    status = parentObjectOtp.getString("status");
                                    message = parentObjectOtp.getString("message");
                                    // barcode_id = parentObjectOtp.getString("barcode_id");
                                    Log.e(TAG, "onResponse: " + status + " " + " message---->" + message);

                                    if (status.equalsIgnoreCase("SUCCESS")) {

                                        GlobalClass.hideProgress(OfferScan_Activity.this, progressDialog);

                                        SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                                        editor.remove("testsSElected");
                                        editor.remove("getProductNames");
                                        editor.commit();

                                        getUploadFileResponse();
                                        //  sendGPSDetails = Volley.newRequestQueue(OfferScan_Activity.this);
                                        JSONObject jsonObjectOtp = new JSONObject();

                                 /*       try {
                                            jsonObjectOtp.put("Username", user);
                                            jsonObjectOtp.put("IMEI", getIMEINUMBER);
                                            jsonObjectOtp.put("city", getCityName);
                                            jsonObjectOtp.put("state", getStateName);
                                            jsonObjectOtp.put("country", getCountryName);
                                            jsonObjectOtp.put("longitude", longitudePassTOAPI);
                                            jsonObjectOtp.put("latitude", latitudePassTOAPI);
                                            jsonObjectOtp.put("DeviceName", mobileModel);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.sendGeoLocation, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {
                                                    Log.e(TAG, "onResponse: " + response);
                                                    String finalJson = response.toString();
                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                                                    String Response = parentObjectOtp.getString("Response");
                                                    String resId = parentObjectOtp.getString("resId");


                                                    if (resId.equals("RES0000")) {
                                                        //after sending the woe succrssfully
                                                    } else {
                                                        TastyToast.makeText(OfferScan_Activity.this, "" + Response, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    }



                                                } catch (JSONException e) {

                                                    TastyToast.makeText(OfferScan_Activity.this, "", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                        sendGPSDetails.add(jsonObjectRequest1);
                                        Log.e(TAG, "onResponse: url" + jsonObjectRequest1);
                                        Log.e(TAG, "onResponse: json" + jsonObjectOtp);*/

                                    } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {
                                        GlobalClass.hideProgress(OfferScan_Activity.this, progressDialog);
                                        flagcallonce = false;
                                        TastyToast.makeText(OfferScan_Activity.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                                OfferScan_Activity.this).create();

                                        // Setting Dialog Title
                                        alertDialog.setTitle("Update Ledger !");

                                        // Setting Dialog Message
                                        alertDialog.setMessage(ToastFile.update_ledger);
                                        // Setting OK Button
                                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                                httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                                startActivity(httpIntent);

                                                // Write your code here to execute after dialog closed
                                                Intent i = new Intent(OfferScan_Activity.this, Payment_Activity.class);
                                                i.putExtra("COMEFROM", "OfferScan_Activity");
                                                startActivity(i);
                                            }
                                        });
                                        alertDialog.show();

                                    } else if (status.equalsIgnoreCase(caps_invalidApikey)) {
                                        GlobalClass.redirectToLogin(OfferScan_Activity.this);
                                        GlobalClass.hideProgress(OfferScan_Activity.this, progressDialog);
                                    } else {
                                        GlobalClass.hideProgress(OfferScan_Activity.this, progressDialog);
                                        flagcallonce = false;
                                        TastyToast.makeText(OfferScan_Activity.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                              /*  if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                    flagcallonce = false;
                                }
                                if (OfferScan_Activity.this instanceof Activity) {
                                    if (!((Activity) OfferScan_Activity.this).isFinishing())
                                        barProgressDialog.dismiss();
                                }
                                flagcallonce = false;
                                if (error != null) {
                                } else {
                                    System.out.println(error);
                                }*/
                            }
                        });
                        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                                150000,
                                3,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        POstQue.add(jsonObjectRequest1);
                        Log.e(TAG, "fetchData: URL" + jsonObjectRequest1);
                        Log.e(TAG, "fetchData: JSON" + jsonObj);
                    }

                }
            }
        } else {
            Toast.makeText(OfferScan_Activity.this, ToastFile.allw_scan, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                getTestSelection = show_selected_tests_data.getText().toString();
                final String passProdcucts = testsnames;


                String getLab_alert = lab_alert_spin.getText().toString();

               /* if (getLab_alert.equals("SELECT LAB ALERTS")) {
                    lab_alert_pass_toApi = "";
                } else {
                    lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                }*/


                if (enterAmt.getText().toString().equals("")) {
                    Toast.makeText(OfferScan_Activity.this, "Please enter collected amount", Toast.LENGTH_SHORT).show();
                } else if (getTestSelection.equals("") || getTestSelection.equals(null)) {
                    Toast.makeText(OfferScan_Activity.this, "Select test", Toast.LENGTH_SHORT).show();
                } else if (setLocation == null) {
                    TastyToast.makeText(OfferScan_Activity.this, "Please select location", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {
                    doFinalWoe();
                }


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    passBarcodeData(getBarcodeDetails);
                } else {
                    Toast.makeText(this, invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void passBarcodeData(String getBarcodeDetails) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().isEmpty()) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(getBarcodeDetails)) {
                    isbacodeduplicate = true;
                }
            }
        }

        if (isbacodeduplicate == true) {
            Toast.makeText(OfferScan_Activity.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(getBarcodeDetails);
                    GlobalClass.finalspecimenttypewiselist.get(i).setRemark("Scan");
                    getRemark = "";
                    getRemark = GlobalClass.finalspecimenttypewiselist.get(i).getRemark();
                    Log.e(TAG, "passBarcodeData: show barcode" + getBarcodeDetails);
                }
            }
        }


        recycler_barcode.removeAllViews();
//        adapterBarcode.notifyDataSetChanged();
        adapterBarcode = new AdapterBarcode_New(OfferScan_Activity.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (GlobalClass.specimenttype != null) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_barcode.setAdapter(adapterBarcode);
    }

}
