package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcode_New;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.ProductWithBarcode;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class Scan_Barcode_ILS_New extends AppCompatActivity implements RecyclerInterface {
    SharedPreferences prefs;
    String testName, productnames;
    EditText enterAmt;
    TextView title;
    Button next;
    ImageView back;
    LocationManager locationManager;
    GpsTracker gpsTracker;
    TextView lab_alert_spin;
    private MyPojo myPojo;
    public static ArrayList<String> labAlerts;
    ImageView home;
    LinearLayout sample_type_linear, amt_collected_and_total_amt_ll;
    ScannedBarcodeDetails scannedBarcodeDetails;
    private SpinnerDialog spinnerDialog;
    ArrayList<BaseModel> selctedTest;
    ArrayList<String> setSpecimenTypeCodes;
    ArrayList<String> getUniquespecimenttype;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    SharedPreferences preferences, prefe;
    String brandName, typeName;
    TextView show_selected_tests_data, setAmt;
    LinearLayoutManager linearLayoutManager;
    public IntentIntegrator scanIntegrator;
    BaseModel.Barcodes[] barcodes;
    private boolean GpsStatus;
    RecyclerView recycler_barcode;
    //    TextView companycost_test;
    private ArrayList<String> temparraylist;
    private ArrayList<ProductWithBarcode> getproductDetailswithBarcodes;
    public String specimenttype1;
    private AdapterBarcode_New adapterBarcode;
    public int position1 = 0;
    private String getAmount;

    private AlertDialog.Builder alertDialog;
    private int collectedAmt;
    private int totalAmount;
    private String getCollectedAmt;
    private String testsnames;
    private DatabaseHelper myDb;
    ;
    private Cursor cursor;

    private static String stringofconvertedTime;
    private static String cutString;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;

    LinearLayout SGCLinearid;
    TextView saverepeat, saveclose, ref_by_txt, serial_number, serial_number_re;
    RecyclerView sample_list;
    String getSelctedTest;
    int saveSrNumber;
    public static com.android.volley.RequestQueue sendGPSDetails;
    String getStateName, getCountryName, getCityName;


    SharedPreferences savepatientDetails;
    private String amt_collected;

    ArrayList<BarcodelistModel> barcodelists;
    ArrayList<String> getBarcodeArrList;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    Barcodelist barcodelistData;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    public static com.android.volley.RequestQueue POstQue;
    private String message;
    private String ageType;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = Scan_Barcode_ILS_New.this.getClass().getSimpleName();
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
    boolean flagcallonce = false;
    private String version;
    private int versionCode;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    com.google.android.gms.location.LocationListener listener;
    long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Global globalClass;

    String getIMEINUMBER;
    String mobileModel;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    private String referenceID;
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
    private String lab_alert_pass_toApi;
    private String testsCodesPassingToProduct;
    private String barcode_id;
    private String getRemark;


    @SuppressLint({"WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);

        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        setAmt = (TextView) findViewById(R.id.setAmt);
        title = (TextView) findViewById(R.id.title);
        sample_type_linear = (LinearLayout) findViewById(R.id.sample_type_linear);
        amt_collected_and_total_amt_ll = (LinearLayout) findViewById(R.id.amt_collected_and_total_amt_ll);
        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
//        companycost_test = (TextView) findViewById(R.id.companycost_test);

        enterAmt = (EditText) findViewById(R.id.enterAmt);
        next = (Button) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        prefe = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", null);
        typeName = prefe.getString("woetype", null);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        title.setText("Scan Barcode");
        GlobalClass.finalspecimenttypewiselist = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        selctedTest = bundle.getParcelableArrayList("key");
        myDb = new DatabaseHelper(getApplicationContext());
        CheckGpsStatus();
        if (GpsStatus == true) {
            gpsTracker = new GpsTracker(Scan_Barcode_ILS_New.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                latitudePassTOAPI = String.valueOf(latitude);
                longitudePassTOAPI = String.valueOf(longitude);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
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
            } else {
                gpsTracker.showSettingsAlert();
            }

        } else {
            showSettingsAlert();
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SharedPreferences getIMIE = getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);
        SharedPreferences getModelNumber = getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", null);

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
        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("year", null);
        patientYearType = preferences.getString("yearType", null);
        patientGender = preferences.getString("gender", null);
        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        barProgressDialog = new ProgressDialog(Scan_Barcode_ILS_New.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        pass_to_api = Integer.parseInt(sr_number);

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

        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.marronFaint));
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(Scan_Barcode_ILS_New.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();
        if (myPojo.getMASTERS().getLAB_ALERTS() != null) {
            for (int i = 0; i < myPojo.getMASTERS().getLAB_ALERTS().length; i++) {
                labAlerts.add(myPojo.getMASTERS().getLAB_ALERTS()[i]);
            }
        }


        lab_alert_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        spinnerDialog = new SpinnerDialog(Scan_Barcode_ILS_New.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                lab_alert_spin.setText(s);
            }
        });


        ArrayList<String> getTestNameToPAss = new ArrayList<>();
        getAmount = bundle.getString("payment");
        testsnames = bundle.getString("writeTestName");
        getTestNameToPAss = bundle.getStringArrayList("TestCodesPass");
        testsCodesPassingToProduct = TextUtils.join(",", getTestNameToPAss);

        setAmt.setText(getAmount);
        show_selected_tests_data.setText(testsnames);
        System.out.println("" + selctedTest.toString());
        prefs = getSharedPreferences("showSelectedTest", MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(Scan_Barcode_ILS_New.this);
        recycler_barcode.setLayoutManager(linearLayoutManager);
        sample_type_linear.setVisibility(View.GONE);
        temparraylist = new ArrayList<>();
        ArrayList<Integer> rateList = new ArrayList<>();
        int totalcount = 0;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Scan_Barcode_ILS_New.this);
            }
        });

        if (selctedTest.size() != 0) {
            for (int i = 0; i < selctedTest.size(); i++) {

                totalcount = totalcount + Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                Log.e(TAG, "onCreate: 11 " + totalcount);
            }
        }
        Log.e(TAG, "onCreate: total " + totalcount);


        if (selctedTest != null) {
            getproductDetailswithBarcodes = new ArrayList<>();
            for (int i = 0; i < selctedTest.size(); i++) {
                for (int j = 0; j < selctedTest.get(i).getBarcodes().length; j++) {
                    ProductWithBarcode productWithBarcode = new ProductWithBarcode();
                    temparraylist.add(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                    productWithBarcode.setBarcode(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                    productWithBarcode.setProduct(selctedTest.get(i).getBarcodes()[j].getCode());
                    getproductDetailswithBarcodes.add(productWithBarcode);
                }
            }

        } else {
            // Toast.makeText(this, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
        }

        setAmt.setText(String.valueOf(getAmount));

        Set<String> hs = new HashSet<>();
        hs.addAll(temparraylist);
        temparraylist.clear();
        temparraylist.addAll(hs);

        for (int i = 0; i < temparraylist.size(); i++) {

            ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
            setSpecimenTypeCodes = new ArrayList<>();
            for (int j = 0; j < getproductDetailswithBarcodes.size(); j++) {
                if (temparraylist.get(i).equalsIgnoreCase(getproductDetailswithBarcodes.get(j).getBarcode())) {
                    setSpecimenTypeCodes.add(getproductDetailswithBarcodes.get(j).getProduct());
                }
            }
            scannedBarcodeDetails.setSpecimen_type(temparraylist.get(i));
            for (int j = 0; j < setSpecimenTypeCodes.size(); j++) {


                HashSet<String> listToSet = new HashSet<String>(setSpecimenTypeCodes);

//Creating Arraylist without duplicate values
                List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                String setProducts = TextUtils.join(",", listWithoutDuplicates);
                HashSet<String> test = new HashSet<String>(Arrays.asList(setProducts.split(",")));
                String setFinalProducts = TextUtils.join(",", test);
                scannedBarcodeDetails.setProducts(setFinalProducts);
            }
            GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
        }
        if (typeName.equals("ILS")) {
            if (selctedTest.size() != 0) {
                totalcount = 0;
                for (int i = 0; i < selctedTest.size(); i++) {
                    int getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getBillrate());
                    totalcount = totalcount + getAmtBilledRate;
                    Log.e(TAG, "onCreate: 11 " + totalcount);
                    amt_collected_and_total_amt_ll.setVisibility(View.GONE);
                    setAmt.setText(String.valueOf(totalcount));
                    enterAmt.setText(String.valueOf(totalcount));
                }
            }

        } else {
            amt_collected_and_total_amt_ll.setVisibility(View.VISIBLE);
        }


        System.out.println("finallist" + GlobalClass.finalspecimenttypewiselist.toString());
        adapterBarcode = new AdapterBarcode_New(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String totalamt = getAmount;
                final String getTestSelection = show_selected_tests_data.getText().toString();
                final String passProdcucts = testsnames;
                String ampount = enterAmt.getText().toString();

                String getLab_alert = lab_alert_spin.getText().toString();

                if (getLab_alert.equals("SELECT LAB ALERTS")) {
                    lab_alert_pass_toApi = "";
                } else {
                    lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                }
                getCollectedAmt = enterAmt.getText().toString();
                if (!getCollectedAmt.equals("") && !totalamt.equals("")) {
                    collectedAmt = Integer.parseInt(getCollectedAmt);
                    totalAmount = Integer.parseInt(totalamt);
                } else {
                    Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.colAmt, Toast.LENGTH_SHORT).show();
                }

                if (getCollectedAmt.equals("")) {
                    Toast.makeText(Scan_Barcode_ILS_New.this, "Please enter collected Amount", Toast.LENGTH_SHORT).show();
                } else if (getTestSelection.equals("") || getTestSelection.equals(null)) {
                    Toast.makeText(Scan_Barcode_ILS_New.this, "Select test", Toast.LENGTH_SHORT).show();
                } else if (getCollectedAmt.equals("") || getCollectedAmt.equals(null) || getCollectedAmt.isEmpty()) {
                    Toast.makeText(Scan_Barcode_ILS_New.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                } else {
                    if (GlobalClass.finalspecimenttypewiselist != null) {
                        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() == null) {
                                Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.pls_scn_br + GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type(), Toast.LENGTH_SHORT).show();
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
                                getBrand_name = savepatientDetails.getString("WOEbrand", null);
                                sampleCollectionDate = getFinalDate;
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
                                GlobalClass.Req_Date_Req(getFinalTime, "hh:mm a", "HH:mm:ss");

                                MyPojoWoe myPojoWoe = new MyPojoWoe();
                                Woe woe = new Woe();
                                woe.setAADHAR_NO("");
                                woe.setADDRESS(patientAddress);
                                woe.setAGE(getFinalAge);
                                woe.setAGE_TYPE(ageType);
                                woe.setALERT_MESSAGE(lab_alert_pass_toApi);
                                woe.setAMOUNT_COLLECTED(getCollectedAmt);
                                woe.setAMOUNT_DUE(null);
                                woe.setAPP_ID(version);
                                woe.setBCT_ID(btechIDToPass);
                                woe.setBRAND(getBrand_name);
                                woe.setCAMP_ID(getcampIDtoPass);
                                woe.setCONT_PERSON("");
                                woe.setCONTACT_NO(kycinfo);
                                woe.setCUSTOMER_ID("");
                                woe.setDELIVERY_MODE(2);
                                woe.setEMAIL_ID("");
                                woe.setENTERED_BY(user);
                                woe.setGENDER(saveGenderId);
                                woe.setLAB_ADDRESS(labAddress);
                                woe.setLAB_ID(labIDaddress);
                                woe.setLAB_NAME(labname);
                                woe.setLEAD_ID("");
                                woe.setMAIN_SOURCE(user);
                                woe.setORDER_NO("");
                                woe.setOS("unknown");
                                woe.setPATIENT_NAME(nameString);
                                woe.setPINCODE("");
                                woe.setPRODUCT(testsCodesPassingToProduct);
                                woe.setPurpose("mobile application");
                                woe.setREF_DR_ID(referenceID);
                                woe.setREF_DR_NAME(referrredBy);
                                woe.setREMARKS("MOBILE - " + getRemark);
                                woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
                                woe.setSPECIMEN_SOURCE("");
                                woe.setSR_NO(pass_to_api);
                                woe.setSTATUS("N");
                                woe.setSUB_SOURCE_CODE(user);
                                woe.setTOTAL_AMOUNT(totalamt);
                                woe.setTYPE(typename);
                                woe.setWATER_SOURCE("");
                                woe.setWO_MODE("THYROSOFTLITE APP");
                                woe.setWO_STAGE(3);
                                woe.setULCcode("");
                                myPojoWoe.setWoe(woe);
                                barcodelists = new ArrayList<>();
                                getBarcodeArrList = new ArrayList<>();
                                for (int p = 0; p < GlobalClass.finalspecimenttypewiselist.size(); p++) {
                                    barcodelist = new BarcodelistModel();
                                    barcodelist.setSAMPLE_TYPE(GlobalClass.finalspecimenttypewiselist.get(p).getSpecimen_type());
                                    barcodelist.setBARCODE(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());
                                    getBarcodeArrList.add(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());
                                    barcodelist.setTESTS(GlobalClass.finalspecimenttypewiselist.get(p).getProducts());
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


                                if (!GlobalClass.isNetworkAvailable(Scan_Barcode_ILS_New.this)) {


                                    String barcodes = TextUtils.join(",", getBarcodeArrList);
                                    if (flagcallonce == false) {
                                        flagcallonce = true;
                                        boolean isInserted = myDb.insertData(barcodes, json);
                                        if (isInserted == true) {
                                            TastyToast.makeText(Scan_Barcode_ILS_New.this, ToastFile.woeSaved, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                            Intent intent = new Intent(Scan_Barcode_ILS_New.this, SummaryActivity_New.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("tetsts", getTestSelection);
                                            bundle.putString("passProdcucts", testsCodesPassingToProduct);
                                            bundle.putString("TotalAmt", totalamt);
                                            bundle.putString("CollectedAmt", getCollectedAmt);
                                            bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
                                            bundle.putString("patientId", "");
                                            bundle.putString("barcodes", barcodes);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        } else {
                                            TastyToast.makeText(Scan_Barcode_ILS_New.this, ToastFile.woenotSaved, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }
                                    }


                                } else {
                                    if (flagcallonce == false) {
                                        flagcallonce = true;
                                        barProgressDialog.show();

                                        POstQue = Volley.newRequestQueue(Scan_Barcode_ILS_New.this);
                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {

                                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                        barProgressDialog.dismiss();
                                                    }
                                                    Log.e(TAG, "onResponse: RESPONSE" + response);
                                                    String finalJson = response.toString();
                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                    RES_ID = parentObjectOtp.getString("RES_ID");
                                                    barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                                    message = parentObjectOtp.getString("message");
                                                    status = parentObjectOtp.getString("status");
                                                    barcode_id = parentObjectOtp.getString("barcode_id");

                                                    if (status.equalsIgnoreCase("SUCCESS")) {
                                                        SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                                                        editor.remove("testsSElected");
                                                        editor.remove("getProductNames");
                                                        editor.commit();
                                                        sendGPSDetails = Volley.newRequestQueue(Scan_Barcode_ILS_New.this);
                                                        JSONObject jsonObjectOtp = new JSONObject();
                                                        try {
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


                                                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                                        Intent intent = new Intent(Scan_Barcode_ILS_New.this, SummaryActivity_New.class);
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString("tetsts", getTestSelection);
                                                                        bundle.putString("passProdcucts", testsCodesPassingToProduct);
                                                                        bundle.putString("TotalAmt", totalamt);
                                                                        bundle.putString("CollectedAmt", getCollectedAmt);
                                                                        bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
                                                                        bundle.putString("patientId", barcode_patient_id);
                                                                        bundle.putString("draft", "");
                                                                        intent.putExtras(bundle);
                                                                        startActivity(intent);

                                                                    } else {

                                                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, "" + Response, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                                    }

                                                                } catch (JSONException e) {

                                                                    TastyToast.makeText(Scan_Barcode_ILS_New.this, "", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                                        Log.e(TAG, "onResponse: json" + jsonObjectOtp);

                                                    } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {


                                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                                                Scan_Barcode_ILS_New.this).create();

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
                                                            }
                                                        });
                                                        alertDialog.show();

                                                    }else if(status.equalsIgnoreCase(caps_invalidApikey)){
                                                        GlobalClass.redirectToLogin(Scan_Barcode_ILS_New.this);
                                                    } else {
                                                        flagcallonce = false;
                                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                    }

                                                } catch (JSONException e) {

                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new com.android.volley.Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                    barProgressDialog.dismiss();
                                                    flagcallonce = false;
                                                }
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
                                        Log.e(TAG, "fetchData: URL" + jsonObjectRequest1);
                                        Log.e(TAG, "fetchData: JSON" + jsonObj);
                                    }

                                }
                            }
                        }
                    } else {
                        Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.allw_scan, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(
                Scan_Barcode_ILS_New.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Scan_Barcode_ILS_New.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void CheckGpsStatus() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void putDataToscan(Activity context, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");
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
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().isEmpty()) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(s)) {
                    isbacodeduplicate = true;
                }
            } else {

            }
        }

        if (isbacodeduplicate == true) {
            Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {

                if (GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(s);
                    GlobalClass.finalspecimenttypewiselist.get(i).setRemark("Scan");
                    getRemark = "";
                    getRemark = GlobalClass.finalspecimenttypewiselist.get(i).getRemark();
                    Log.e(TAG, "passBarcodeData: show barcode" + s);
                }
            }
        }


        recycler_barcode.removeAllViews();
//        adapterBarcode.notifyDataSetChanged();
        adapterBarcode = new AdapterBarcode_New(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
