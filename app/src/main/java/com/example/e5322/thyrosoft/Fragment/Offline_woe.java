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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.Offline_Woe_Adapter;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.Interface.RefreshOfflineWoe;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Offline_woe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Offline_woe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offline_woe extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LocationManager locationManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    public static com.android.volley.RequestQueue sendGPSDetails;
    private String mParam2;
    private static ManagingTabsActivity mContext;
    Activity activity;
    private OnFragmentInteractionListener mListener;
    private View viewMain;
    ArrayList<String> errorList;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private LinearLayout app_bar_layout;
    private LinearLayoutManager linearLayoutManager;
    private TextView sync_woe, tvNoDataFound;
    DatabaseHelper myDb;
    static Offline_woe fragment;
    LinearLayout sendwoe_ll, parent_ll;
    ArrayList<BarcodelistModel> barcodelists;
    Offline_Woe_Adapter offline_woe_adapter;
    ArrayList<MyPojoWoe> allWoe;
    private String version;
    private String message;
    MyPojoWoe myPojoWoe;
    ProgressDialog barProgressDialog;
    public static com.android.volley.RequestQueue POstQue;
    ArrayList<String> getBarcodeArrList;
    BarcodelistModel barcodelist;
    String getIMEINUMBER;
    String mobileModel, RES_ID, barcode_patient_id, getCityName, getStateName, getCountryName;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    SharedPreferences prefs;
    private String MY_DEBUG_TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private int versionCode;
    private boolean GpsStatus;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    GpsTracker gpsTracker;
    ArrayList<MyPojoWoe> resultList;

    private ArrayList<MyPojoWoe> filterWoeList;
    private String text;
    private String barcode_id;
    private String barcodeCompare;
    String blockCharacterSet = "~#^|$%&*!+:`";

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offline_woe.
     */
    // TODO: Rename and change types and number of parameters
    public static Offline_woe newInstance(String param1, String param2) {
        Offline_woe fragment = new Offline_woe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        mContext = (ManagingTabsActivity) getActivity();

        viewMain = (View) inflater.inflate(R.layout.fragment_offline_workorder, container, false);
        sync_woe = (TextView) viewMain.findViewById(R.id.sync_woe);
        tvNoDataFound = (TextView) viewMain.findViewById(R.id.tvNoDataFound);
        edtSearch = (EditText) viewMain.findViewById(R.id.edtSearch);
//        ImageView  add = (ImageView)viewMain.findViewById(R.id.add);
        recyclerView = (RecyclerView) viewMain.findViewById(R.id.recycler_view);
        app_bar_layout = (LinearLayout) viewMain.findViewById(R.id.enter_entered_layout_consign);
        sendwoe_ll = (LinearLayout) viewMain.findViewById(R.id.sendwoe_ll);
        parent_ll = (LinearLayout) viewMain.findViewById(R.id.parent_ll);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        myDb = new DatabaseHelper(mContext);
        progressDialog();

        final SharedPreferences getIMIE = mContext.getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);
        SharedPreferences getModelNumber = mContext.getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", null);

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
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
                    Toast.makeText(getActivity(),
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

                                if (resultList.get(i).getBarcodelist().get(j).getBARCODE() != null || !resultList.get(i).getBarcodelist().get(j).getBARCODE().equals("")) {
                                    barcode = resultList.get(i).getBarcodelist().get(j).getBARCODE().toLowerCase();
                                }
                            }
                            if (resultList.get(i).getWoe().getPATIENT_NAME() != null || !resultList.get(i).getWoe().getPATIENT_NAME().equals("")) {
                                name = resultList.get(i).getWoe().getPATIENT_NAME().toLowerCase();
                            }

                            if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                    (name != null && name.contains(s1))) {
                                String testname = resultList.get(i).getWoe().getPATIENT_NAME();
                                filterWoeList.add(resultList.get(i));

                            } else {

                            }
                            offline_woe_adapter = new Offline_Woe_Adapter(mContext, filterWoeList, fragment, errorList);
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
            gpsTracker = new GpsTracker(mContext);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                latitudePassTOAPI = String.valueOf(latitude);
                longitudePassTOAPI = String.valueOf(longitude);
                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
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
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
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

        offline_woe_adapter = new Offline_Woe_Adapter(mContext, resultList, fragment, errorList);
        offline_woe_adapter.onClickDeleteOffWoe(new RefreshOfflineWoe() {
            @Override
            public void onClickDeleteOffWoe(String barcodes) {
                boolean deletedRows = myDb.deleteData(barcodes);
                if (deletedRows == true) {
                    TastyToast.makeText(mContext, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    resultList = getResults();
                } else {
                    TastyToast.makeText(mContext, ToastFile.woeDeleteUnsuccess, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
        recyclerView.setAdapter(offline_woe_adapter);


        sendwoe_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultList != null && !resultList.isEmpty()) {
                    if (!GlobalClass.isNetworkAvailable(mContext)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        // Set the Alert Dialog Message
                        builder.setMessage(ToastFile.intConnection)
                                .setCancelable(false)
                                .setPositiveButton("Retry",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                                Offline_woe fragment1 = new Offline_woe();

                                                try {
                                                    mContext.getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                } catch (IllegalStateException ignored) {
                                                    // There's no way to avoid getting this if saveInstanceState has already been called.
                                                }


                                                // Restart the Activity
//                                    Intent intent = getIntent();
//                                    finish();
//                                    startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                                    woe.setPINCODE("");
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
                                    woe.setWO_MODE("THYROSOFTLITE APP");
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
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    catch (Error e) {
                                        e.printStackTrace();
                                    }

                                    POstQue = Volley.newRequestQueue(mContext);
                                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                                String finalJson = response.toString();
                                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                RES_ID = parentObjectOtp.getString("RES_ID");
                                                barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                                message = parentObjectOtp.getString("message");
                                                status = parentObjectOtp.getString("status");
                                                barcode_id = parentObjectOtp.getString("barcode_id");
                                                if (barcode_id.endsWith(",")) {
                                                    barcode_id = barcode_id.substring(0, barcode_id.length() - 1);
                                                }
                                                if (message.equalsIgnoreCase("WORK ORDER ENTRY DONE SUCCESSFULLY")) {
                                                    barProgressDialog.dismiss();
                                                    //Delete woe from Db
                                                    boolean deletedRows = myDb.deleteData(barcode_id);
                                                    SharedPreferences.Editor editor = mContext.getSharedPreferences("showSelectedTest", 0).edit();
                                                    editor.remove("testsSElected");
                                                    editor.remove("getProductNames");
                                                    editor.commit();
                                                    sendGPSDetails = Volley.newRequestQueue(mContext);
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
                                                                    resultList = getResults();
                                                                    offline_woe_adapter = new Offline_Woe_Adapter(mContext, resultList, fragment, errorList);
                                                                    recyclerView.setAdapter(offline_woe_adapter);
                                                                    TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                                } else {
                                                                    resultList = getResults();
                                                                    offline_woe_adapter = new Offline_Woe_Adapter(mContext, resultList, fragment, errorList);
                                                                    recyclerView.setAdapter(offline_woe_adapter);
                                                                }

                                                            } catch (JSONException e) {

                                                                TastyToast.makeText(mContext, "", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

                                                    barProgressDialog.dismiss();
                                                    TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                    final AlertDialog alertDialog = new AlertDialog.Builder(
                                                            mContext).create();

                                                    // Setting Dialog Title
                                                    alertDialog.setTitle("Update Ledger !");

                                                    // Setting Dialog Message
                                                    alertDialog.setMessage(ToastFile.update_ledger);
                                                    // Setting OK Button
                                                    alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent i = new Intent(mContext, Payment_Activity.class);
                                                            mContext.startActivity(i);
                                                          /*  Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                                            httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                                            startActivity(httpIntent);*/
                                                            // Write your code here to execute after dialog closed
                                                        }
                                                    });
                                                    alertDialog.show();

                                                } else {
                                                    barProgressDialog.dismiss();
                                                    boolean isUpdated = myDb.updateDataeRROR(barcode_id, message);
                                                    TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    Offline_woe fragment1 = new Offline_woe();

                                                    try {
                                                        mContext.getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                                                    } catch (IllegalStateException ignored) {
                                                        // There's no way to avoid getting this if saveInstanceState has already been called.
                                                    }
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
                    TastyToast.makeText(mContext, "No data found", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }

        });
        // Inflate the layout for this fragment
        return viewMain;
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
        resultList = getResults();
        offline_woe_adapter = new Offline_Woe_Adapter(mContext, resultList, fragment, errorList);
        recyclerView.setAdapter(offline_woe_adapter);
    }

    private void CheckGpsStatus() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private ArrayList<MyPojoWoe> getResults() {

        DatabaseHelper db = new DatabaseHelper(mContext); //my database helper file
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void setNewFragment() {
        try {
            getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).attach(Offline_woe.this).commitAllowingStateLoss();
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
//        adapter.notifyDataSetChanged();
    }
}
