package com.example.e5322.thyrosoft.SpecialOffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcode_New;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.WoeController;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class OfferScan_Activity extends AppCompatActivity implements RecyclerInterface, View.OnClickListener {
    public static ArrayList<String> labAlerts;
    public static com.android.volley.RequestQueue POstQue;
    public String TAG = getClass().getSimpleName();
    public IntentIntegrator scanIntegrator;
    public String specimenttype1;
    public int position1 = 0;
    ArrayList<BaseModel.Barcodes> barcodesArrayList;
    AdapterBarcode_New adapterBarcode;
    ArrayList<BaseModel> selctedTest = new ArrayList<>();
    RecyclerView recycler_barcode;
    TextView lab_alert_spin;
    int payment;
    TextView setAmt;
    String setLocation = "RPL";
    LinearLayout lin_labalert;
    ArrayList<BarcodelistModel> barcodelists;
    ArrayList<String> getBarcodeArrList;
    BarcodelistModel barcodelist;
    boolean flagcallonce = false;
    SharedPreferences savepatientDetails, preferences;
    SharedPreferences prefs;
    String labname, tspaddress, pincode;
    String labAddress;
    Button btn_next;
    TextView show_selected_tests_data;
    String getTestSelection, totalamt, type;
    RadioButton cpl_rdo, rpl_rdo;
    private MyPojo myPojo;
    private SpinnerDialog spinnerDialog;
    private String referenceID, referrredBy;
    private int collectedAmt;
    private int totalAmount, versionCode;
    private String nameString, barcode_patient_id;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
    private String testsnames;
    private String genderType;
    private String getFinalDate, user, passwrd, access, api_key, status, message;
    private boolean barcodeExistsFlag = false;
    private String getRemark;
    private String getBrand_name;
    private String sampleCollectionDate;
    private String getPincode, ac_code;
    private String outputDateStr;
    private String ageType;
    private String patientAddress;
    private EditText enterAmt;
    private RadioGroup location_radio_grp;
    private String version;
    private String btechIDToPass, contactno, btechNameToPass;
    public ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist = new ArrayList<>();

    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);
        mActivity = OfferScan_Activity.this;

        if (getIntent().getExtras() != null) {
            finalspecimenttypewiselist.clear();
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
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);

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
        nameString = savepatientDetails.getString("name", "");
        getFinalAge = savepatientDetails.getString("age", "");
        saveGenderId = savepatientDetails.getString("gender", "");
        getFinalTime = savepatientDetails.getString("sct", "");
        getageType = savepatientDetails.getString("ageType", "");
        getFinalDate = savepatientDetails.getString("date", "");
        patientAddress = savepatientDetails.getString("patientAddress", "");
        referenceID = savepatientDetails.getString("refId", "");
        referrredBy = savepatientDetails.getString("refBy", "");
        labname = savepatientDetails.getString("labname", "");
        labAddress = savepatientDetails.getString("labAddress", "");
        btechNameToPass = savepatientDetails.getString("btechNameToPass", "");

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
        setAmt = findViewById(R.id.setAmt);
        setAmt.setText("" + payment);

        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
        lab_alert_spin.setVisibility(View.GONE);
        lin_labalert.setVisibility(View.GONE);


        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(OfferScan_Activity.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();

        try {
            if (!GlobalClass.checkArray(myPojo.getMASTERS().getLAB_ALERTS()) && myPojo.getMASTERS() != null) {
                for (int i = 0; i < myPojo.getMASTERS().getLAB_ALERTS().length; i++) {
                    labAlerts.add(myPojo.getMASTERS().getLAB_ALERTS()[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (GlobalClass.CheckArrayList(barcodesArrayList)) {
            for (int i = 0; i < barcodesArrayList.size(); i++) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                scannedBarcodeDetails.setBarcode(barcodesArrayList.get(0).getCode());
                scannedBarcodeDetails.setSpecimen_type(barcodesArrayList.get(0).getSpecimen_type());
                finalspecimenttypewiselist.add(scannedBarcodeDetails);
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
        adapterBarcode = new AdapterBarcode_New(OfferScan_Activity.this, selctedTest, finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (!GlobalClass.isNull(GlobalClass.specimenttype)) {
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
        GlobalClass.showTastyToast(OfferScan_Activity.this, message, 1);
        Intent intent = new Intent(OfferScan_Activity.this, SummaryActivity_New.class);
        Bundle bundle = new Bundle();
        bundle.putString("tetsts", getTestSelection);
        bundle.putString("location", setLocation);
        bundle.putString("passProdcucts", testsnames);
        bundle.putString("TotalAmt", totalamt);
        bundle.putString("CollectedAmt", enterAmt.getText().toString());
        bundle.putParcelableArrayList("sendArraylist", finalspecimenttypewiselist);
        bundle.putString("patientId", barcode_patient_id);
        bundle.putString("draft", "");
        bundle.putString("fromcome", "offerscreen");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void doFinalWoe() {

        if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
            for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                if (!GlobalClass.isNull(finalspecimenttypewiselist.get(i).getBarcode())) {
                    barcodeExistsFlag = true;
                }
            }

            if (barcodeExistsFlag) {
                barcodeExistsFlag = false;
                GlobalClass.showTastyToast(OfferScan_Activity.this, ToastFile.scan_barcode_all, 2);
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

                Log.e(TAG, "fetchData: " + outputDateStr);
                if (!GlobalClass.isNull(getageType) && getageType.equalsIgnoreCase("Years")) {
                    ageType = "Y";
                } else if (!GlobalClass.isNull(getageType) && getageType.equalsIgnoreCase("Months")) {
                    ageType = "M";
                } else if (!GlobalClass.isNull(getageType) && getageType.equalsIgnoreCase("Days")) {
                    ageType = "D";
                }


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

                if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
                    for (int p = 0; p < finalspecimenttypewiselist.size(); p++) {
                        barcodelist = new BarcodelistModel();
                        barcodelist.setSAMPLE_TYPE(finalspecimenttypewiselist.get(p).getSpecimen_type());
                        barcodelist.setBARCODE(finalspecimenttypewiselist.get(p).getBarcode());
                        getBarcodeArrList.add(finalspecimenttypewiselist.get(p).getBarcode());
                        barcodelist.setTESTS(testsnames);
                        barcodelists.add(barcodelist);
                    }
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


                if (GlobalClass.isNetworkAvailable(OfferScan_Activity.this)) {
                    if (!flagcallonce) {
                        flagcallonce = true;

                        if (POstQue == null) {
                            POstQue = GlobalClass.setVolleyReq(OfferScan_Activity.this);
                        }

                        Log.e(TAG, "Post WOE Entry ---->" + jsonObj.toString());


                        try {
                            if (ControllersGlobalInitialiser.woeController != null) {
                                ControllersGlobalInitialiser.woeController = null;
                            }
                            ControllersGlobalInitialiser.woeController = new WoeController(mActivity, OfferScan_Activity.this);
                            ControllersGlobalInitialiser.woeController.woeDoneController(jsonObj, POstQue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            GlobalClass.showTastyToast(OfferScan_Activity.this, ToastFile.allw_scan, 2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.home:
                startActivity(new Intent(OfferScan_Activity.this, ManagingTabsActivity.class));
                break;

            case R.id.next:
                getTestSelection = show_selected_tests_data.getText().toString();

                if (GlobalClass.isNull(enterAmt.getText().toString())) {
                    GlobalClass.showTastyToast(OfferScan_Activity.this, MessageConstants.COLLECT_AMT, 2);
                } else if (GlobalClass.isNull(getTestSelection)) {
                    GlobalClass.showTastyToast(OfferScan_Activity.this, MessageConstants.SL_TEST, 2);
                } else if (GlobalClass.isNull(setLocation)) {
                    GlobalClass.showTastyToast(OfferScan_Activity.this, MessageConstants.SL_LOC, 2);
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
                    GlobalClass.showTastyToast(this, invalid_brcd, 2);
                }
            }
        }
    }

    private void passBarcodeData(String getBarcodeDetails) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            if (finalspecimenttypewiselist.get(i).getBarcode() != null && !finalspecimenttypewiselist.get(i).getBarcode().isEmpty()) {
                if (finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(getBarcodeDetails)) {
                    isbacodeduplicate = true;
                }
            }
        }

        if (isbacodeduplicate) {
            GlobalClass.showTastyToast(OfferScan_Activity.this, ToastFile.duplicate_barcd, 2);
        } else {

            if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
                for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                    if (!GlobalClass.isNull(finalspecimenttypewiselist.get(i).getSpecimen_type()) &&
                            !GlobalClass.isNull(GlobalClass.specimenttype) && finalspecimenttypewiselist.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                        finalspecimenttypewiselist.get(i).setBarcode(getBarcodeDetails);
                        finalspecimenttypewiselist.get(i).setRemark("Scan");
                        getRemark = "";
                        getRemark = finalspecimenttypewiselist.get(i).getRemark();
                        Log.e(TAG, "passBarcodeData: show barcode" + getBarcodeDetails);
                    }
                }
            }

        }


        recycler_barcode.removeAllViews();
        adapterBarcode = new AdapterBarcode_New(OfferScan_Activity.this, selctedTest, finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (!GlobalClass.isNull(GlobalClass.specimenttype)) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_barcode.setAdapter(adapterBarcode);
    }

    public void getWoeResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);

            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
            status = parentObjectOtp.getString("status");
            message = parentObjectOtp.getString("message");
            // barcode_id = parentObjectOtp.getString("barcode_id");
            Log.e(TAG, "onResponse: " + status + " " + " message---->" + message);

            if (!GlobalClass.isNull(status) && status.equalsIgnoreCase(Constants.SUCCESS)) {

                SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                editor.remove("testsSElected");
                editor.remove("getProductNames");
                editor.commit();

                getUploadFileResponse();
                JSONObject jsonObjectOtp = new JSONObject();


            } else if (!GlobalClass.isNull(message) && message.equals(MessageConstants.CRDIT_LIMIT)) {

                flagcallonce = false;
                GlobalClass.showTastyToast(OfferScan_Activity.this, message, 2);

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        OfferScan_Activity.this).create();

                alertDialog.setTitle(ToastFile.updateledger);
                alertDialog.setMessage(ToastFile.update_ledger);
                alertDialog.setButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                        httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                        startActivity(httpIntent);

                        Intent i = new Intent(OfferScan_Activity.this, Payment_Activity.class);
                        i.putExtra("COMEFROM", "OfferScan_Activity");
                        startActivity(i);
                    }
                });
                alertDialog.show();

            } else if (status.equalsIgnoreCase(caps_invalidApikey)) {
                GlobalClass.redirectToLogin(OfferScan_Activity.this);

            } else {
                flagcallonce = false;
                GlobalClass.showTastyToast(OfferScan_Activity.this, message, 2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
