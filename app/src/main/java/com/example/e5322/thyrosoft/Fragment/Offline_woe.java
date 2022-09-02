package com.example.e5322.thyrosoft.Fragment;

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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.HomeMenuActivity;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.Offline_Woe_Adapter;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.Interface.RefreshOfflineWoe;
import com.example.e5322.thyrosoft.Models.RequestModels.GeoLocationRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Offline_woe extends AppCompatActivity {
    RequestQueue POstQue;
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
    static Offline_woe fragment;
    private static HomeMenuActivity mContext;
    LocationManager locationManager;
    Activity activity;
    ArrayList<String> errorList;
    DatabaseHelper myDb;
    LinearLayout sendwoe_ll, parent_ll;
    ArrayList<BarcodelistModel> barcodelists;
    Offline_Woe_Adapter offline_woe_adapter;
    ArrayList<MyPojoWoe> allWoe;
    MyPojoWoe myPojoWoe;
    ProgressDialog barProgressDialog;
    ArrayList<String> getBarcodeArrList;
    BarcodelistModel barcodelist;
    String RES_ID, barcode_patient_id, getCityName, getStateName, getCountryName;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    SharedPreferences prefs;
    GpsTracker gpsTracker;
    ArrayList<MyPojoWoe> resultList;
    ArrayList<TRFModel> trflist;
    String blockCharacterSet = "~#^|$%&*!+:`";
    TextView tv_total;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View viewMain;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private LinearLayout app_bar_layout;
    private LinearLayoutManager linearLayoutManager;
    private TextView sync_woe, tvNoDataFound;
    private String version;
    private String message;
    private String MY_DEBUG_TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private int versionCode;
    private boolean GpsStatus;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private ArrayList<MyPojoWoe> filterWoeList;
    private String text;
    private String barcode_id;
    private String barcodeCompare;
    RequestQueue sendGPSDetails;
    ImageView back, home;

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public Offline_woe() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_offline_workorder);

        activity = Offline_woe.this;
        //  mContext = (HomeMenuActivity) ;
        sync_woe = findViewById(R.id.sync_woe);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        edtSearch = findViewById(R.id.edtSearch);
//        ImageView  add = (ImageView)viewMain.findViewById(R.id.add);
        recyclerView = findViewById(R.id.recycler_view);
        app_bar_layout = findViewById(R.id.enter_entered_layout_consign);
        sendwoe_ll = findViewById(R.id.sendwoe_ll);
        parent_ll = findViewById(R.id.parent_ll);
        tv_total = findViewById(R.id.tv_total);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(activity);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(activity);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDb = new DatabaseHelper(activity);
        progressDialog();

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        edtSearch.setFilters(new InputFilter[]{filter});
        edtSearch.setFilters(new InputFilter[]{EMOJI_FILTER});


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(activity,
                            ToastFile.edt_search,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edtSearch.setText(enteredString.substring(1));
                    } else {
                        edtSearch.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                String s1 = "";
                if (s != null) {
                    s1 = s.toString().toLowerCase();
                    filterWoeList = new ArrayList<>();
                    String barcode = "";
                    String name = "";
                    if (resultList != null) {
                        for (int i = 0; i < resultList.size(); i++) {

                            for (int j = 0; j < resultList.get(i).getBarcodelist().size(); j++) {
                                if (resultList.get(i).getBarcodelist().get(j).getBARCODE() != null)
                                    text = resultList.get(i).getBarcodelist().get(j).getBARCODE().toLowerCase();

                                // Added !GlobalClass.isNull() at Line no 268 and 272
                                if (!GlobalClass.isNull(resultList.get(i).getBarcodelist().get(j).getBARCODE())) {
                                    barcode = resultList.get(i).getBarcodelist().get(j).getBARCODE().toLowerCase();
                                }
                            }
                            if (!GlobalClass.isNull(resultList.get(i).getWoe().getPATIENT_NAME())) {
                                name = resultList.get(i).getWoe().getPATIENT_NAME().toLowerCase();
                            }

                            if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                    (name != null && name.contains(s1))) {
                                String testname = resultList.get(i).getWoe().getPATIENT_NAME();
                                filterWoeList.add(resultList.get(i));

                            }

                            offline_woe_adapter = new Offline_Woe_Adapter(filterWoeList, Offline_woe.this, errorList);
                            recyclerView.setAdapter(offline_woe_adapter);
                            offline_woe_adapter.notifyDataSetChanged();
                        }
                    }
                }


                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        CheckGpsStatus();

        if (GpsStatus == true) {
            gpsTracker = new GpsTracker(activity);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                latitudePassTOAPI = String.valueOf(latitude);
                longitudePassTOAPI = String.valueOf(longitude);
                Geocoder gcd = new Geocoder(activity, Locale.getDefault());
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

        }
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView

        resultList = getResults();


        if (resultList.size() == 0) {
            tvNoDataFound.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
            sendwoe_ll.setVisibility(View.GONE);
        } else {
            parent_ll.setVisibility(View.VISIBLE);
            sendwoe_ll.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
        }
        tv_total.setText("Total Count : " + resultList.size());
        offline_woe_adapter = new Offline_Woe_Adapter(resultList, Offline_woe.this, errorList);
        offline_woe_adapter.onClickDeleteOffWoe(new RefreshOfflineWoe() {
            @Override
            public void onClickDeleteOffWoe(String barcodes) {
                boolean deletedRows = myDb.deleteData(barcodes);
                if (deletedRows == true) {
                    TastyToast.makeText(activity, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    resultList = getResults();
                } else {
                    TastyToast.makeText(activity, ToastFile.woeDeleteUnsuccess, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });

        recyclerView.setAdapter(offline_woe_adapter);


        sendwoe_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultList != null && !resultList.isEmpty()) {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        // Set the Alert Dialog Message
                        builder.setMessage(ToastFile.intConnection)
                                .setCancelable(false)
                                .setPositiveButton("Retry",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                               /* Offline_woe fragment1 = new Offline_woe();
                                                try {
                                                    activity.getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                } catch (IllegalStateException ignored) {
                                                    // There's no way to avoid getting this if saveInstanceState has already been called.
                                                }*/
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(ToastFile.internet_avail);
                        builder.setMessage(ToastFile.setTitle_submitall_woe);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                barProgressDialog.show();

                                for (int i = 0; i < resultList.size(); i++) {

                                    MyPojoWoe myPojoWoe = new MyPojoWoe();
                                    Woe woe = new Woe();
                                    woe.setAADHAR_NO("");
                                    woe.setADDRESS(resultList.get(i).getWoe().getADDRESS());
                                    woe.setAGE(resultList.get(i).getWoe().getAGE());
                                    woe.setAGE_TYPE(resultList.get(i).getWoe().getAGE_TYPE());
                                    woe.setALERT_MESSAGE(resultList.get(i).getWoe().getALERT_MESSAGE());
                                    woe.setAMOUNT_COLLECTED(resultList.get(i).getWoe().getAMOUNT_COLLECTED());
                                    woe.setAMOUNT_DUE(null);
                                    woe.setAPP_ID(version);
                                    woe.setADDITIONAL1(resultList.get(i).getWoe().getADDITIONAL1());
                                    woe.setBCT_ID(resultList.get(i).getWoe().getBCT_ID());
                                    woe.setBRAND(resultList.get(i).getWoe().getBRAND());
                                    woe.setCAMP_ID(resultList.get(i).getWoe().getCAMP_ID());
                                    woe.setCONT_PERSON("");
                                    woe.setCONTACT_NO(resultList.get(i).getWoe().getCONTACT_NO());
                                    woe.setCUSTOMER_ID("");
                                    woe.setDELIVERY_MODE(2);
                                    woe.setEMAIL_ID("");
                                    woe.setENTERED_BY(resultList.get(i).getWoe().getENTERED_BY());
                                    woe.setGENDER(resultList.get(i).getWoe().getGENDER());
                                    woe.setLAB_ADDRESS(resultList.get(i).getWoe().getLAB_ADDRESS());
                                    woe.setLAB_ID(resultList.get(i).getWoe().getLAB_ID());
                                    woe.setLAB_NAME(resultList.get(i).getWoe().getLAB_NAME());
                                    woe.setLEAD_ID("");
                                    woe.setMAIN_SOURCE(resultList.get(i).getWoe().getMAIN_SOURCE());
                                    woe.setORDER_NO("");
                                    woe.setOS("unknown");
                                    woe.setPATIENT_NAME(resultList.get(i).getWoe().getPATIENT_NAME());
                                    woe.setPINCODE(resultList.get(i).getWoe().getPINCODE());
                                    woe.setPRODUCT(resultList.get(i).getWoe().getPRODUCT());
                                    woe.setPurpose("mobile application");
                                    woe.setREF_DR_ID(resultList.get(i).getWoe().getREF_DR_ID());
                                    woe.setREF_DR_NAME(resultList.get(i).getWoe().getREF_DR_NAME());
                                    woe.setREMARKS("MOBILE-OFFLINE");
                                    woe.setSPECIMEN_COLLECTION_TIME(resultList.get(i).getWoe().getSPECIMEN_COLLECTION_TIME());
                                    woe.setSPECIMEN_SOURCE("");
                                    woe.setSR_NO(resultList.get(i).getWoe().getSR_NO());
                                    woe.setSTATUS("N");
                                    woe.setSUB_SOURCE_CODE(resultList.get(i).getWoe().getSUB_SOURCE_CODE());
                                    woe.setTOTAL_AMOUNT(resultList.get(i).getWoe().getTOTAL_AMOUNT());
                                    woe.setTYPE(resultList.get(i).getWoe().getTYPE());
                                    woe.setWATER_SOURCE("");
                                    woe.setWO_MODE("CLISO APP");
                                    woe.setWO_STAGE(3);
                                    woe.setULCcode("");
                                    myPojoWoe.setWoe(woe);

                                    barcodelists = new ArrayList<>();
                                    getBarcodeArrList = new ArrayList<>();
                                    for (int p = 0; p < resultList.get(i).getBarcodelist().size(); p++) {
                                        barcodelist = new BarcodelistModel();
                                        barcodelist.setSAMPLE_TYPE(resultList.get(i).getBarcodelist().get(p).getSAMPLE_TYPE());
                                        barcodelist.setBARCODE(resultList.get(i).getBarcodelist().get(p).getBARCODE());
                                        getBarcodeArrList.add(resultList.get(i).getBarcodelist().get(p).getBARCODE());
                                        barcodelist.setTESTS(resultList.get(i).getBarcodelist().get(p).getTESTS());
                                        barcodelists.add(barcodelist);
                                    }
                                    myPojoWoe.setBarcodelistModel(barcodelists);
                                    myPojoWoe.setWoe_type("WOE");
                                    myPojoWoe.setApi_key(resultList.get(i).getApi_key());//api_key

                                    Gson gson = new GsonBuilder().create();
                                    String json = gson.toJson(myPojoWoe);
                                    JSONObject jsonObj = null;
                                    try {
                                        jsonObj = new JSONObject(json);
                                        POstQue = GlobalClass.setVolleyReq(activity);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Error e) {
                                        e.printStackTrace();
                                    }


                                    final int finalI = i;
                                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                                barProgressDialog.dismiss();
                                                Gson woeGson = new Gson();
                                                WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);

                                                if (woeResponseModel != null) {
                                                    barcode_patient_id = woeResponseModel.getBarcode_patient_id();
                                                    message = woeResponseModel.getMessage();
                                                    status = woeResponseModel.getStatus();
                                                    barcode_id = woeResponseModel.getBarcode_id();

                                                    if (barcode_id.endsWith(",")) {
                                                        barcode_id = barcode_id.substring(0, barcode_id.length() - 1);
                                                    }

                                                    if (!GlobalClass.isNull(message) && message.equalsIgnoreCase("WORK ORDER ENTRY DONE SUCCESSFULLY")) {
                                                        sendGeolocation();
                                                        sendTrf(finalI);
                                                    } else if (!GlobalClass.isNull(message) && message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {
                                                        TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                                        alertDialog.setTitle("Update Ledger !");
                                                        alertDialog.setMessage(ToastFile.update_ledger);
                                                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent i = new Intent(activity, Payment_Activity.class);
                                                                i.putExtra("COMEFROM", "Offline_woe");
                                                                activity.startActivity(i);
                                                            }
                                                        });
                                                        alertDialog.show();
                                                    } else {
                                                        boolean isUpdated = myDb.updateDataeRROR(barcode_id, message);
                                                        TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                        refreshList();
                                                       /* Offline_woe fragment1 = new Offline_woe();
                                                        try {
                                                            activity.getSupportFragmentManager().beginTransaction()
                                                                    .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                        } catch (IllegalStateException ignored) {
                                                            // There's no way to avoid getting this if saveInstanceState has already been called.
                                                        }*/
                                                    }
                                                } else {
                                                    TastyToast.makeText(activity, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    refreshList();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            barProgressDialog.dismiss();
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
                        });

                        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                } else {
                    TastyToast.makeText(activity, "No data found", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }

        });
    }
    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_offline_workorder);

        activity = Offline_woe.this;
     //  mContext = (HomeMenuActivity) ;
        sync_woe = findViewById(R.id.sync_woe);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        edtSearch = findViewById(R.id.edtSearch);
//        ImageView  add = (ImageView)viewMain.findViewById(R.id.add);
        recyclerView = findViewById(R.id.recycler_view);
        app_bar_layout = findViewById(R.id.enter_entered_layout_consign);
        sendwoe_ll = findViewById(R.id.sendwoe_ll);
        parent_ll = findViewById(R.id.parent_ll);
        tv_total = findViewById(R.id.tv_total);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(activity);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(activity);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDb = new DatabaseHelper(activity);
        progressDialog();

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        edtSearch.setFilters(new InputFilter[]{filter});
        edtSearch.setFilters(new InputFilter[]{EMOJI_FILTER});


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(activity,
                            ToastFile.edt_search,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edtSearch.setText(enteredString.substring(1));
                    } else {
                        edtSearch.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                String s1 = "";
                if (s != null) {
                    s1 = s.toString().toLowerCase();
                    filterWoeList = new ArrayList<>();
                    String barcode = "";
                    String name = "";
                    if (resultList != null) {
                        for (int i = 0; i < resultList.size(); i++) {

                            for (int j = 0; j < resultList.get(i).getBarcodelist().size(); j++) {
                                if (resultList.get(i).getBarcodelist().get(j).getBARCODE() != null)
                                    text = resultList.get(i).getBarcodelist().get(j).getBARCODE().toLowerCase();

                                // Added !GlobalClass.isNull() at Line no 268 and 272
                                if (!GlobalClass.isNull(resultList.get(i).getBarcodelist().get(j).getBARCODE())) {
                                    barcode = resultList.get(i).getBarcodelist().get(j).getBARCODE().toLowerCase();
                                }
                            }
                            if (!GlobalClass.isNull(resultList.get(i).getWoe().getPATIENT_NAME())) {
                                name = resultList.get(i).getWoe().getPATIENT_NAME().toLowerCase();
                            }

                            if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                    (name != null && name.contains(s1))) {
                                String testname = resultList.get(i).getWoe().getPATIENT_NAME();
                                filterWoeList.add(resultList.get(i));

                            }

                            offline_woe_adapter = new Offline_Woe_Adapter(filterWoeList, Offline_woe.this, errorList);
                            recyclerView.setAdapter(offline_woe_adapter);
                            offline_woe_adapter.notifyDataSetChanged();
                        }
                    }
                }


                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        CheckGpsStatus();

        if (GpsStatus == true) {
            gpsTracker = new GpsTracker(activity);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                latitudePassTOAPI = String.valueOf(latitude);
                longitudePassTOAPI = String.valueOf(longitude);
                Geocoder gcd = new Geocoder(activity, Locale.getDefault());
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

        }
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView

        resultList = getResults();


        if (resultList.size() == 0) {
            tvNoDataFound.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
            sendwoe_ll.setVisibility(View.GONE);
        } else {
            parent_ll.setVisibility(View.VISIBLE);
            sendwoe_ll.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
        }
        tv_total.setText("Total Count : " + resultList.size());
        offline_woe_adapter = new Offline_Woe_Adapter(resultList, Offline_woe.this, errorList);
        offline_woe_adapter.onClickDeleteOffWoe(new RefreshOfflineWoe() {
            @Override
            public void onClickDeleteOffWoe(String barcodes) {
                boolean deletedRows = myDb.deleteData(barcodes);
                if (deletedRows == true) {
                    TastyToast.makeText(activity, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    resultList = getResults();
                } else {
                    TastyToast.makeText(activity, ToastFile.woeDeleteUnsuccess, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });

        recyclerView.setAdapter(offline_woe_adapter);


        sendwoe_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultList != null && !resultList.isEmpty()) {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        // Set the Alert Dialog Message
                        builder.setMessage(ToastFile.intConnection)
                                .setCancelable(false)
                                .setPositiveButton("Retry",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                               *//* Offline_woe fragment1 = new Offline_woe();
                                                try {
                                                    activity.getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                } catch (IllegalStateException ignored) {
                                                    // There's no way to avoid getting this if saveInstanceState has already been called.
                                                }*//*
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(ToastFile.internet_avail);
                        builder.setMessage(ToastFile.setTitle_submitall_woe);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                barProgressDialog.show();

                                for (int i = 0; i < resultList.size(); i++) {

                                    MyPojoWoe myPojoWoe = new MyPojoWoe();
                                    Woe woe = new Woe();
                                    woe.setAADHAR_NO("");
                                    woe.setADDRESS(resultList.get(i).getWoe().getADDRESS());
                                    woe.setAGE(resultList.get(i).getWoe().getAGE());
                                    woe.setAGE_TYPE(resultList.get(i).getWoe().getAGE_TYPE());
                                    woe.setALERT_MESSAGE(resultList.get(i).getWoe().getALERT_MESSAGE());
                                    woe.setAMOUNT_COLLECTED(resultList.get(i).getWoe().getAMOUNT_COLLECTED());
                                    woe.setAMOUNT_DUE(null);
                                    woe.setAPP_ID(version);
                                    woe.setADDITIONAL1(resultList.get(i).getWoe().getADDITIONAL1());
                                    woe.setBCT_ID(resultList.get(i).getWoe().getBCT_ID());
                                    woe.setBRAND(resultList.get(i).getWoe().getBRAND());
                                    woe.setCAMP_ID(resultList.get(i).getWoe().getCAMP_ID());
                                    woe.setCONT_PERSON("");
                                    woe.setCONTACT_NO(resultList.get(i).getWoe().getCONTACT_NO());
                                    woe.setCUSTOMER_ID("");
                                    woe.setDELIVERY_MODE(2);
                                    woe.setEMAIL_ID("");
                                    woe.setENTERED_BY(resultList.get(i).getWoe().getENTERED_BY());
                                    woe.setGENDER(resultList.get(i).getWoe().getGENDER());
                                    woe.setLAB_ADDRESS(resultList.get(i).getWoe().getLAB_ADDRESS());
                                    woe.setLAB_ID(resultList.get(i).getWoe().getLAB_ID());
                                    woe.setLAB_NAME(resultList.get(i).getWoe().getLAB_NAME());
                                    woe.setLEAD_ID("");
                                    woe.setMAIN_SOURCE(resultList.get(i).getWoe().getMAIN_SOURCE());
                                    woe.setORDER_NO("");
                                    woe.setOS("unknown");
                                    woe.setPATIENT_NAME(resultList.get(i).getWoe().getPATIENT_NAME());
                                    woe.setPINCODE(resultList.get(i).getWoe().getPINCODE());
                                    woe.setPRODUCT(resultList.get(i).getWoe().getPRODUCT());
                                    woe.setPurpose("mobile application");
                                    woe.setREF_DR_ID(resultList.get(i).getWoe().getREF_DR_ID());
                                    woe.setREF_DR_NAME(resultList.get(i).getWoe().getREF_DR_NAME());
                                    woe.setREMARKS("MOBILE-OFFLINE");
                                    woe.setSPECIMEN_COLLECTION_TIME(resultList.get(i).getWoe().getSPECIMEN_COLLECTION_TIME());
                                    woe.setSPECIMEN_SOURCE("");
                                    woe.setSR_NO(resultList.get(i).getWoe().getSR_NO());
                                    woe.setSTATUS("N");
                                    woe.setSUB_SOURCE_CODE(resultList.get(i).getWoe().getSUB_SOURCE_CODE());
                                    woe.setTOTAL_AMOUNT(resultList.get(i).getWoe().getTOTAL_AMOUNT());
                                    woe.setTYPE(resultList.get(i).getWoe().getTYPE());
                                    woe.setWATER_SOURCE("");
                                    woe.setWO_MODE("CLISO APP");
                                    woe.setWO_STAGE(3);
                                    woe.setULCcode("");
                                    myPojoWoe.setWoe(woe);

                                    barcodelists = new ArrayList<>();
                                    getBarcodeArrList = new ArrayList<>();
                                    for (int p = 0; p < resultList.get(i).getBarcodelist().size(); p++) {
                                        barcodelist = new BarcodelistModel();
                                        barcodelist.setSAMPLE_TYPE(resultList.get(i).getBarcodelist().get(p).getSAMPLE_TYPE());
                                        barcodelist.setBARCODE(resultList.get(i).getBarcodelist().get(p).getBARCODE());
                                        getBarcodeArrList.add(resultList.get(i).getBarcodelist().get(p).getBARCODE());
                                        barcodelist.setTESTS(resultList.get(i).getBarcodelist().get(p).getTESTS());
                                        barcodelists.add(barcodelist);
                                    }
                                    myPojoWoe.setBarcodelistModel(barcodelists);
                                    myPojoWoe.setWoe_type("WOE");
                                    myPojoWoe.setApi_key(resultList.get(i).getApi_key());//api_key

                                    Gson gson = new GsonBuilder().create();
                                    String json = gson.toJson(myPojoWoe);
                                    JSONObject jsonObj = null;
                                    try {
                                        jsonObj = new JSONObject(json);
                                        POstQue = GlobalClass.setVolleyReq(activity);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Error e) {
                                        e.printStackTrace();
                                    }


                                    final int finalI = i;
                                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                                barProgressDialog.dismiss();
                                                Gson woeGson = new Gson();
                                                WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);

                                                if (woeResponseModel != null) {
                                                    barcode_patient_id = woeResponseModel.getBarcode_patient_id();
                                                    message = woeResponseModel.getMessage();
                                                    status = woeResponseModel.getStatus();
                                                    barcode_id = woeResponseModel.getBarcode_id();

                                                    if (barcode_id.endsWith(",")) {
                                                        barcode_id = barcode_id.substring(0, barcode_id.length() - 1);
                                                    }

                                                    if (!GlobalClass.isNull(message) && message.equalsIgnoreCase("WORK ORDER ENTRY DONE SUCCESSFULLY")) {
                                                        sendGeolocation();
                                                        sendTrf(finalI);
                                                    } else if (!GlobalClass.isNull(message) && message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {
                                                        TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                                        alertDialog.setTitle("Update Ledger !");
                                                        alertDialog.setMessage(ToastFile.update_ledger);
                                                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent i = new Intent(activity, Payment_Activity.class);
                                                                i.putExtra("COMEFROM", "Offline_woe");
                                                                activity.startActivity(i);
                                                            }
                                                        });
                                                        alertDialog.show();
                                                    } else {
                                                        boolean isUpdated = myDb.updateDataeRROR(barcode_id, message);
                                                        TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                        refreshList();
                                                       *//* Offline_woe fragment1 = new Offline_woe();
                                                        try {
                                                            activity.getSupportFragmentManager().beginTransaction()
                                                                    .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                        } catch (IllegalStateException ignored) {
                                                            // There's no way to avoid getting this if saveInstanceState has already been called.
                                                        }*//*
                                                    }
                                                } else {
                                                    TastyToast.makeText(activity, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    refreshList();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            barProgressDialog.dismiss();
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
                        });

                        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                } else {
                    TastyToast.makeText(activity, "No data found", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }

        });
        // Inflate the layout for this fragment
        return viewMain;
    }*/

    private void uploadTRf(ArrayList<TRFModel> trflist, File vialimage) {
        if (trflist.size() > 0)
            new AsyncTaskPost_uploadfile(this, activity, api_key, user, barcode_patient_id, trflist, vialimage).execute();
    }

    private void progressDialog() {
        barProgressDialog = new ProgressDialog(activity);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        resultList = getResults();
        //trflist = getTRFlist();
        offline_woe_adapter = new Offline_Woe_Adapter(resultList,  Offline_woe.this, errorList);
        recyclerView.setAdapter(offline_woe_adapter);
    }

    private void CheckGpsStatus() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private ArrayList<MyPojoWoe> getResults() {

        DatabaseHelper db = new DatabaseHelper(activity); //my database helper file
        db.open();

        ArrayList<MyPojoWoe> resultList = new ArrayList<MyPojoWoe>();
        errorList = new ArrayList<String>();

        Cursor c = db.getAllData(); //function to retrieve all values from a table- written in MyDb.java file
        while (c.moveToNext()) {
            String woejson = c.getString(c.getColumnIndex("WOEJSON"));
            String error = c.getString(c.getColumnIndex("ERROR"));

            try {
                Gson gson = new Gson();
                myPojoWoe = new MyPojoWoe();
                myPojoWoe = gson.fromJson(woejson, MyPojoWoe.class);
                resultList.add(myPojoWoe);
                errorList.add(error);
            } catch (Exception e) {
                Log.e(MY_DEBUG_TAG, "Error " + e.toString());
            }
        }
        c.close();
        db.close();
        return resultList;

    }

    private void sendGeolocation() {
        //Delete woe from Db
        boolean deletedRows = myDb.deleteData(barcode_id);
        SharedPreferences.Editor editor = activity.getSharedPreferences("showSelectedTest", 0).edit();
        editor.remove("testsSElected");
        editor.remove("getProductNames");
        editor.apply();

        if (sendGPSDetails == null) {
            sendGPSDetails = GlobalClass.setVolleyReq(activity);
        }


        JSONObject jsonObject = null;
        try {
            GeoLocationRequestModel requestModel = new GeoLocationRequestModel();
            requestModel.setUsername(user);
            requestModel.setIMEI(GlobalClass.getIMEINo(activity));
            requestModel.setCity(getCityName);
            requestModel.setState(getStateName);
            requestModel.setCountry(getCountryName);
            requestModel.setLongitude(longitudePassTOAPI);
            requestModel.setLatitude(latitudePassTOAPI);
            requestModel.setDeviceName(GlobalClass.getDeviceName());

            Gson geoGson = new Gson();
            String json = geoGson.toJson(requestModel);
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.sendGeoLocation, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: " + response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                    String resId = parentObjectOtp.getString("resId");
                    if (GlobalClass.checkEqualIgnoreCase(resId, "RES0000")) {
                        //after sending the woe succrssfully
                        refreshList();
                        TastyToast.makeText(activity, "" + message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                      //  activity.getSupportFragmentManager().beginTransaction().remove(new Offline_woe()).commitAllowingStateLoss();
                    } else {
                        refreshList();
                    }
                } catch (JSONException e) {
                    TastyToast.makeText(activity, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshList();
                System.out.println("" + error);
            }
        });
        sendGPSDetails.add(jsonObjectRequest1);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
        Log.e(TAG, "onResponse: url" + jsonObjectRequest1);
        Log.e(TAG, "onResponse: json" + jsonObject);
    }

    private void sendTrf(int finalI) {
        try {
            trflist = new ArrayList<TRFModel>();
            JSONArray jsonArray = new JSONArray(resultList.get(finalI).getTrfjson());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TRFModel trfModel = new TRFModel();
                trfModel.setProduct(jsonObject.getString("Product"));
                String path = jsonObject.getString("trf_image");
                JSONObject jsonObject1 = new JSONObject(path);
                trfModel.setTrf_image(new File(jsonObject1.getString("path")));
                trflist.add(trfModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uploadTRf(trflist, resultList.get(finalI).getVialimage());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setNewFragment() {
        Intent intent = new Intent(activity, ManagingTabsActivity.class);
        GlobalClass.setFlagBackToWoe = true;
        activity.startActivity(intent);
    }

    public void getUploadFileResponse() {
        refreshList();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
