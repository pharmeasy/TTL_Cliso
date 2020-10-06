package com.example.e5322.thyrosoft.Activity.frags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.CampIdResponseModel;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.PaitientDataResponseModel;
import com.example.e5322.thyrosoft.Models.PaitientdataRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.WOERequestModel;
import com.example.e5322.thyrosoft.MultiSelectSpinner;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AntiBodyEnterFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AntiBodyEnterFrag extends Fragment implements MultiSelectSpinner.OnMultipleItemsSelectedListener {

    Button btn_generate, btn_resend, btn_verify;
    View root;
    EditText edt_missed_mobile, edt_verifycc;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    SharedPreferences preferences;
    CountDownTimer countDownTimer;
    private Camera camera;
    ProgressDialog progressDialog;
    MultiSelectSpinner spn_symptoms;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    String TAG = getClass().getSimpleName();
    TextView tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob, txt_barcode, txt_adharfileupload, txt_nofileadhar;
    private boolean timerflag = false;
    RelativeLayout rel_mobno;
    ArrayList<CampIdResponseModel.OutputBean> Campid = new ArrayList<>();
    private String outputDateStr;
    LinearLayout lin_generate_verify, lin_by_missed, ll_enterView, lin_adhar_images;
    RadioButton by_missed, by_generate;
    RelativeLayout rel_verify_mobile;
    ImageView img_scanbarcode;
    RequestQueue requestQueue;
    Bitmap bitmapimage;
    EditText edt_firstname, edt_lastname, edt_amtcollected, edt_age;
    boolean isadhar;
    List<String> aadharlist = new ArrayList<>();
    private int PERMISSION_REQUEST_CODE = 200;
    String ERROR, RES_ID, barcode, response1;
    List<String> patientsagespinner = Arrays.asList("Gender*", "Male", "Female");
    List<String> Occupation = Arrays.asList("Service", "Private Job", "Business", "Student", "Housewife", "Others");
    Spinner spr_gender, spr_occu, spr_camp;
    private String userChoosenTask;
    File other_file = null;
    Button btn_choosefile_adhar;
    File aadhar_file = null;
    File aadhar_file1 = null;
    Button btn_submit, btn_reset;
    EditText edt_email, edt_address, edt_pincode;
    RadioGroup radiogrp2;


    List<String> arrayList = Arrays.asList("Select", "Fever", "Shortness of Breath", "Cough", "Sore Throat", "NA");
    private String refbyname, campid;
    private String Patientid="";

    public AntiBodyEnterFrag() {
        // Required empty public constructor
    }


    public static AntiBodyEnterFrag newInstance(String param1, String param2) {
        AntiBodyEnterFrag fragment = new AntiBodyEnterFrag();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_anti_body_enter, container, false);


        initui(root);
        initlistner();
        GetCampId();
        return root;
    }


    private void initlistner() {

        by_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_generate.setText(getResources().getString(R.string.enterccc));
            }
        });
        by_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_generate.setText(getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
            }
        });
        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
            }
        });
        img_scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(AntiBodyEnterFrag.this).initiateScan();
            }
        });
        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (other_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images");
                    } else {
                        selectImage();
                    }

                } else {
                    requestPermission();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();

            }
        });

        spr_camp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (Campid != null) {
                        for (int i = 0; i < Campid.size(); i++) {
                            if (spr_camp.getSelectedItem().toString().equalsIgnoreCase(Campid.get(i).getCampID())) {
                                edt_amtcollected.setText(Campid.get(i).getAmountToBeCollected());
                                edt_amtcollected.setEnabled(false);
                                edt_amtcollected.setTextColor(Color.BLACK);
                                edt_address.setText(Campid.get(i).getCampLocationAddress());
                                edt_address.setEnabled(false);
                                edt_address.setTextColor(Color.BLACK);
                                edt_pincode.setText(Campid.get(i).getPincode());
                                edt_pincode.setEnabled(false);
                                edt_pincode.setTextColor(Color.BLACK);
                               // edt_email.setText(Campid.get(i).get);

                                refbyname = Campid.get(i).getReferringDoctorName();
                                campid = Campid.get(i).getCampID();
                            }
                        }
                        if (spr_camp.getSelectedItem().toString().equalsIgnoreCase("Select Camp-ID")) {
                            edt_amtcollected.setText("");
                            edt_amtcollected.setEnabled(true);
                            edt_address.setText("");
                            edt_address.setEnabled(true);
                            edt_pincode.setText("");
                            edt_pincode.setEnabled(true
                            );
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    WOERequestModel submitCampWoeRequestModel = setWoedata();
                    CallSubmitWoeDetailsAPI(submitCampWoeRequestModel);


                }
            }
        });

        txt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(AntiBodyEnterFrag.this).initiateScan();
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                    if (!GlobalClass.isNull(edt_verifycc.getText().toString())) {
                        if (cd.isConnectingToInternet()) {
                            validateotp();
                        } else {
                            GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, "Kindly enter otp");
                    }
                } else {
                    GlobalClass.showCustomToast(activity, "Kindly enter mobile number");
                }
            }
        });
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateflow();
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateflow();
            }
        });


        tv_resetno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_by_missed.setVisibility(View.VISIBLE);
                lin_generate_verify.setVisibility(View.GONE);
                edt_missed_mobile.setEnabled(true);
                btn_generate.setVisibility(View.VISIBLE);
                btn_generate.setEnabled(true);
                tv_resetno.setVisibility(View.GONE);
                edt_verifycc.getText().clear();
                tv_mobileno.setVisibility(View.GONE);
                rel_mobno.setVisibility(View.GONE);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    tv_timer.setVisibility(View.GONE);
                }
            }
        });
    }

    private WOERequestModel setWoedata() {
        WOERequestModel submitCampWoeRequestModel = new WOERequestModel();
        WOERequestModel.WoeBean woe = new WOERequestModel.WoeBean();
        woe.setAADHAR_NO("");
        woe.setADDRESS(edt_address.getText().toString().trim());
        woe.setAGE(edt_age.getText().toString());
        woe.setAGE_TYPE("Y");
        woe.setALERT_MESSAGE("");
        woe.setAMOUNT_COLLECTED(edt_amtcollected.getText().toString());
        woe.setAMOUNT_DUE("0");
        woe.setAPP_ID("2.0");
        woe.setBCT_ID(usercode);
        woe.setBRAND("TTL");
        woe.setCAMP_ID(spr_camp.getSelectedItem().toString());
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(edt_missed_mobile.getText().toString());
        woe.setCUSTOMER_ID("");
        woe.setDELIVERY_MODE(2);
        woe.setEMAIL_ID(edt_email.getText().toString());
        woe.setENTERED_BY(usercode);
        if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
            woe.setGENDER("M");
        } else {
            woe.setGENDER("F");
        }
        woe.setLAB_ADDRESS(edt_address.getText().toString().trim());
        woe.setLAB_ID("");
        woe.setLAB_NAME("");
        woe.setLEAD_ID("");
        woe.setMAIN_SOURCE(usercode);
        woe.setORDER_NO("");
        woe.setOS("");
        String fname = edt_firstname.getText().toString().trim();
        String lname = edt_lastname.getText().toString().trim();
        String fullname = fname + " " + lname;
        woe.setPATIENT_NAME(fullname);
        woe.setPINCODE(edt_pincode.getText().toString());
        woe.setPRODUCT(Global.test);
        woe.setPurpose("");
        woe.setREF_DR_ID("");
        woe.setREF_DR_NAME(refbyname);
        woe.setREMARKS("MOBILE");

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        woe.setSPECIMEN_COLLECTION_TIME(currentDate);
        woe.setSPECIMEN_SOURCE("");
        woe.setSR_NO(1);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(usercode);
        woe.setTOTAL_AMOUNT(edt_amtcollected.getText().toString());
        woe.setTYPE("CAMP");
        woe.setWATER_SOURCE("");
        woe.setWO_MODE("THYROSOFTLITE APP");
        woe.setWO_STAGE(3);

        ArrayList<WOERequestModel.BarcodelistBean> barcodelistBeans = new ArrayList<>();
        WOERequestModel.BarcodelistBean barcodelistBean = new WOERequestModel.BarcodelistBean();
        barcodelistBean.setBARCODE(txt_barcode.getText().toString());
        barcodelistBean.setTESTS(Global.test);
        barcodelistBean.setSAMPLE_TYPE(Global.sampletype);
        barcodelistBeans.add(barcodelistBean);


        submitCampWoeRequestModel.setWoe(woe);
        submitCampWoeRequestModel.setBarcodelist(barcodelistBeans);
        submitCampWoeRequestModel.setWoe_type("WOE");
        submitCampWoeRequestModel.setApi_key(apikey);
        return submitCampWoeRequestModel;
    }


    private void CallSubmitWoeDetailsAPI(WOERequestModel campWoeRequestModel) {


        progressDialog = GlobalClass.ShowprogressDialog(getContext());
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), Api.LIVEAPI).create(APIInteface.class);
        CampIdRequestModel campIdRequestModel = new CampIdRequestModel();
        campIdRequestModel.setSourceCode(usercode);
        campIdRequestModel.setTestCode("");
        Call<WOEResponseModel> responseCall = apiInterface.GetResponse(campWoeRequestModel);

        responseCall.enqueue(new Callback<WOEResponseModel>() {
            @Override
            public void onResponse(Call<WOEResponseModel> call, Response<WOEResponseModel> response) {
                try {

                    if (response.body().getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                        CallPatientData(response.body().getBarcode_patient_id());
                        Patientid=response.body().getBarcode_patient_id();
                    } else {
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle(response.body().getStatus())
                                .setCancelable(false)
                                .setMessage(response.body().getMessage())
                                .addButton("OK", -1, activity.getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        WOERequestModel submitCampWoeRequestModel = setWoedata();
                                        CallSubmitWoeDetailsAPI(submitCampWoeRequestModel);
                                    }
                                });

                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WOEResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
                Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());
            }
        });
    }

    private void CallPatientData(final String barcode_patient_id) {

        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), Api.LIVEAPI).create(APIInteface.class);
        PaitientdataRequestModel paitientdataRequestModel = new PaitientdataRequestModel();
        paitientdataRequestModel.setPatientid(barcode_patient_id);
        paitientdataRequestModel.setSourceCode(usercode);
        paitientdataRequestModel.setOccupation(spr_occu.getSelectedItem().toString());
        paitientdataRequestModel.setDisease(spn_symptoms.getSelectedItemsAsString());

        Call<PaitientDataResponseModel> responseCall = apiInterface.PostPatientdetails(paitientdataRequestModel);

        responseCall.enqueue(new Callback<PaitientDataResponseModel>() {
            @Override
            public void onResponse(Call<PaitientDataResponseModel> call, Response<PaitientDataResponseModel> response) {
                try {
                    if (response.body().getResponseID().equalsIgnoreCase(Constants.RES0000)) {
                        Covidpostdata covidpostdata = getCovidpostdata();
                        new Covidmultipart_controller(AntiBodyEnterFrag.this, activity, covidpostdata).execute();
                    } else {
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle(response.body().getResponse())
                                .setCancelable(false)
                                .addButton("OK", -1, activity.getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                        PaitientdataRequestModel paitientdataRequestModel = new PaitientdataRequestModel();
                                        paitientdataRequestModel.setPatientid(barcode_patient_id);
                                        paitientdataRequestModel.setOccupation(spr_occu.getSelectedItem().toString());
                                        paitientdataRequestModel.setDisease(spn_symptoms.getSelectedItemsAsString());
                                        CallPatientData(barcode_patient_id);
                                    }
                                });

                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PaitientDataResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
                Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());
            }
        });
    }

    private Covidpostdata getCovidpostdata() {
        Covidpostdata covidpostdata = new Covidpostdata();
        covidpostdata.setSOURCECODE(usercode);
        covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
        String fname = edt_firstname.getText().toString().trim();
        String lname = edt_lastname.getText().toString().trim();
        String fullname = fname + " " + lname;
        covidpostdata.setNAME(fullname);

        covidpostdata.setAMOUNTCOLLECTED(edt_amtcollected.getText().toString());
        covidpostdata.setTESTCODE(Global.test);
        covidpostdata.setAGE(edt_age.getText().toString());
        covidpostdata.setBARCODE(txt_barcode.getText().toString());
        covidpostdata.setENTERBY(usercode);
        covidpostdata.setHOSPITAL(edt_address.getText().toString());
        covidpostdata.setWOEID(Patientid);
        if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
            covidpostdata.setGENDER("M");
        } else {
            covidpostdata.setGENDER("F");
        }

        if (aadhar_file != null && aadhar_file1 != null) {
            covidpostdata.setADHAR(aadhar_file);
            covidpostdata.setADHAR1(aadhar_file1);
        } else if (aadhar_file != null && aadhar_file1 == null) {
            covidpostdata.setADHAR(aadhar_file);
        } else {
            covidpostdata.setADHAR(aadhar_file1);
        }
        return covidpostdata;
    }


    private boolean Validate() {


        if (spr_camp.getSelectedItem().toString().equalsIgnoreCase("Select Camp-ID")){
            Toast.makeText(activity, "Select Camp-ID", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edt_firstname.getText().toString().length() == 0) {
            Toast.makeText(activity, "" + ToastFile.ENTER_FNAME, Toast.LENGTH_SHORT).show();
            Global.showCustomToast(activity, ToastFile.ENTER_FNAME);
            edt_firstname.requestFocus();
            return false;
        }

        if (edt_firstname.getText().toString().length() < 2) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_firstname.requestFocus();
            return false;
        }


        if (edt_lastname.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lastname.requestFocus();
            return false;
        }
        if (edt_lastname.getText().toString().length() < 1) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lastname.requestFocus();
            return false;
        }

        if (GlobalClass.isNull(edt_age.getText().toString())) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_AGE);
            edt_lastname.requestFocus();
            return false;
        }
        if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Gender*")) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_GENDER);
            return false;
        }
        if (txt_barcode.getText().toString().equalsIgnoreCase("Barcode*")) {
            Global.showCustomToast(getActivity(), "Kindly Scan the Barcode");
            return false;
        }
        if (GlobalClass.isNull(edt_amtcollected.getText().toString())) {
            Global.showCustomToast(getActivity(), ToastFile.AMTCOLL);
            edt_lastname.requestFocus();
            return false;
        }

       /* try {
            int amt = Integer.parseInt(edt_amtcollected.getText().toString());
            if (!GlobalClass.isNull(Global.B2C)) {
                if (amt > Integer.parseInt(Global.B2C)) {
                    TastyToast.makeText(getActivity(), "You cannot enter amount collected more than " + Global.B2C, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    return false;
                } else if (amt < Integer.parseInt(Global.B2B)) {
                    TastyToast.makeText(getActivity(), "You cannot enter amount collected less than " + Global.B2B, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    return false;
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }*/

        /*if (aadhar_file == null && aadhar_file1 == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
            return false;
        }*/

        return true;
    }

    private void initui(View root) {
        cd = new ConnectionDetector(getContext());
        activity = getActivity();
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        btn_generate = root.findViewById(R.id.btn_generate);
        btn_generate.setText(getResources().getString(R.string.enterccc));
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        btn_resend = root.findViewById(R.id.btn_resend);
        tv_timer = root.findViewById(R.id.tv_timer);
        btn_submit = root.findViewById(R.id.btn_submit);
        btn_reset = root.findViewById(R.id.btn_reset);
        edt_verifycc = root.findViewById(R.id.edt_verifycc);
        txt_adharfileupload = root.findViewById(R.id.txt_adharfileupload);
        txt_nofileadhar = root.findViewById(R.id.txt_nofileadhar);
        tv_resetno = root.findViewById(R.id.tv_resetno);
        rel_mobno = root.findViewById(R.id.rel_mobno);
        radiogrp2 = root.findViewById(R.id.radiogrp2);
        btn_choosefile_adhar = root.findViewById(R.id.btn_choosefile_adhar);
        txt_barcode = root.findViewById(R.id.txt_barcode);
        rel_verify_mobile = root.findViewById(R.id.rel_verify_mobile);
        by_missed = root.findViewById(R.id.by_missed);
        tv_verifiedmob = root.findViewById(R.id.tv_verifiedmob);
        spn_symptoms = root.findViewById(R.id.spn_symptoms);
        by_generate = root.findViewById(R.id.by_generate);
        ll_enterView = root.findViewById(R.id.ll_enterView);
        lin_adhar_images = root.findViewById(R.id.lin_adhar_images);
        lin_generate_verify = root.findViewById(R.id.lin_generate_verify);
        tv_mobileno = root.findViewById(R.id.tv_mobileno);
        lin_by_missed = root.findViewById(R.id.lin_missed_verify);
        btn_verify = root.findViewById(R.id.btn_verify);
        img_scanbarcode = root.findViewById(R.id.img_scanbarcode);
        spr_gender = root.findViewById(R.id.spr_gender);
        spr_occu = root.findViewById(R.id.spr_occu);
        spr_camp = root.findViewById(R.id.spr_camp);
        edt_firstname = root.findViewById(R.id.edt_firstname);
        edt_lastname = root.findViewById(R.id.edt_lastname);
        edt_amtcollected = root.findViewById(R.id.edt_amtcollected);
        edt_age = root.findViewById(R.id.edt_age);
        edt_email = root.findViewById(R.id.edt_email);
        edt_address = root.findViewById(R.id.edt_address);
        edt_pincode = root.findViewById(R.id.edt_pincode);
        if (patientsagespinner != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    getContext(), R.layout.name_age_spinner, patientsagespinner);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_gender.setAdapter(adap);
            spr_gender.setSelection(0);
        }
        if (Occupation != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    getContext(), R.layout.name_age_spinner, Occupation);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_occu.setAdapter(adap);
            spr_occu.setSelection(0);
        }


        int naPos = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equalsIgnoreCase("NA")) {
                naPos = i;
            }
        }
        spn_symptoms.setItems(arrayList);
        spn_symptoms.hasNoneOption(true, naPos);
        spn_symptoms.setSelection(new int[]{0});
        spn_symptoms.setListener(this);

    }

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN);
                }
            } else {
                mobileverify(edt_missed_mobile.getText().toString());
            }
        }
    }

    private boolean mobilenovalidation() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        return true;
    }

    private void mobileverify(String mobileno) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
        coVerifyMobReq.setApi_key(apikey);
        coVerifyMobReq.setMobile(mobileno);
        coVerifyMobReq.setScode(usercode);

        final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInteface.covmobileVerification(coVerifyMobReq);
        Log.e(TAG, "MOB VERIFY URL--->" + covidmis_responseCall.request().url());
        Log.e(TAG, "MOB VERIFY BODY--->" + new GsonBuilder().create().toJson(coVerifyMobReq));
        covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
            @Override
            public void onResponse(Call<COVerifyMobileResponse> call, Response<COVerifyMobileResponse> response) {
                Log.e(TAG, "on Response-->" + response.body().getResponse());
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        if (response.body().getResponse().equalsIgnoreCase("NOT VERIFIED")) {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        }
                        disablefields();
                    } else if (response.body().getResId().equalsIgnoreCase(Constants.RES0082)) {
                        TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                            generateOtP(edt_missed_mobile.getText().toString());
                        } else {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<COVerifyMobileResponse> call, Throwable t) {

            }
        });

    }

    private void generateOtP(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);

        COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
        coviDgetotp_req.setApi_key(apikey);
        coviDgetotp_req.setMobile(mobileno);
        coviDgetotp_req.setScode(usercode);

        Call<Covidotpresponse> covidotpresponseCall = postAPIInteface.generateotp(coviDgetotp_req);
        covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
            @Override
            public void onResponse(Call<Covidotpresponse> call, Response<Covidotpresponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        setCountDownTimer();
                        lin_by_missed.setVisibility(View.GONE);
                        tv_mobileno.setVisibility(View.VISIBLE);
                        tv_mobileno.setText(edt_missed_mobile.getText().toString());
                        rel_mobno.setVisibility(View.VISIBLE);
                        lin_generate_verify.setVisibility(View.VISIBLE);
                        tv_resetno.setVisibility(View.VISIBLE);
                        tv_resetno.setText(getResources().getString(R.string.reset_mob));
                        tv_resetno.setPaintFlags(tv_resetno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidotpresponse> call, Throwable t) {

            }
        });
    }

    private void setCountDownTimer() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (timerflag == false) {
                    btn_resend.setEnabled(false);
                    btn_resend.setClickable(false);
                    btn_resend.setVisibility(View.GONE);
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    tv_timer.setVisibility(View.GONE);
                    btn_resend.setVisibility(View.VISIBLE);
                }

                long time = millisUntilFinished / 1000;
                tv_timer.setText("Please wait 00:" + numberFormat.format(time));
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_resend.setEnabled(true);
                btn_resend.setClickable(true);
                btn_resend.setVisibility(View.VISIBLE);

                edt_missed_mobile.setEnabled(true);
                edt_missed_mobile.setClickable(true);

                edt_verifycc.setEnabled(true);
                edt_verifycc.setClickable(true);

            }
        }.start();
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        tv_verifiedmob.setText(edt_missed_mobile.getText().toString());
        ll_enterView.setVisibility(View.VISIBLE);
        lin_by_missed.setVisibility(View.GONE);

        tv_mobileno.setVisibility(View.GONE);

        edt_missed_mobile.setEnabled(false);
        // btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);
        btn_resend.setVisibility(View.GONE);
        btn_generate.setVisibility(View.GONE);
        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        timerflag = true;


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    private void validateotp() {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        Covid_validateotp_req covid_validateotp_req = new Covid_validateotp_req();
        covid_validateotp_req.setApi_key(apikey);
        covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
        covid_validateotp_req.setOtp(edt_verifycc.getText().toString());
        covid_validateotp_req.setScode(usercode);

        Call<Covid_validateotp_res> covidotpresponseCall = postAPIInteface.validateotp(covid_validateotp_req);
        covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
            @Override
            public void onResponse(Call<Covid_validateotp_res> call, Response<Covid_validateotp_res> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        disablefields();

                        Toast.makeText(activity, "" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        GlobalClass.showCustomToast(activity, response.body().getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covid_validateotp_res> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(activity, "Failed to load image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                    if (data.getData() != null) {
                        if (aadhar_file1 == null) {
                            aadhar_file1 = FileUtil.from(activity, data.getData());
                        }
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofileadhar.setVisibility(View.GONE);
                    aadharlist.add(aadhar_file1.toString());
                } else {
                    if (data.getData() != null) {
                        if (aadhar_file == null) {
                            aadhar_file = FileUtil.from(activity, data.getData());
                        }
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofileadhar.setVisibility(View.GONE);
                    aadharlist.add(aadhar_file.toString());

                }

                if (aadhar_file != null && aadhar_file1 != null) {
                    isadhar = false;
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));

                }

                lin_adhar_images.setVisibility(View.VISIBLE);


            } else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    aadhar_file1 = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file1;
                    aadhar_file1 = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), aadhar_file1);
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofileadhar.setVisibility(View.GONE);
                    aadharlist.add(imageurl);
                } else {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    aadhar_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file;
                    aadhar_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), aadhar_file);
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofileadhar.setVisibility(View.GONE);
                    aadharlist.add(imageurl);
                }

                if (aadhar_file != null && aadhar_file1 != null) {
                    isadhar = false;
                    lin_adhar_images.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setVisibility(View.VISIBLE);
                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));

                }
                lin_adhar_images.setVisibility(View.VISIBLE);

            } else if (result != null) {
                Log.e(TAG, "onActivityResult: " + result);
                if (result.getContents() != null) {
                    String getBarcodeDetails = result.getContents();
                    if (getBarcodeDetails.length() == 8) {
                        passBarcodeData(getBarcodeDetails);
                    } else {
                        Toast.makeText(getContext(), invalid_brcd, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void passBarcodeData(final String getBarcodeDetails) {
        requestQueue = GlobalClass.setVolleyReq(getContext());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kindly wait...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(context);

        System.out.println("barcode url  --> " + Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode"
                , new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("barcode respponse" + response);
                GlobalClass.hideProgress(getContext(), progressDialog);
                Log.e(TAG, "onResponse: " + response);
                String finalJson = response.toString();
                JSONObject parentObjectOtp = null;
                try {
                    parentObjectOtp = new JSONObject(finalJson);
                    ERROR = parentObjectOtp.getString("ERROR");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    barcode = parentObjectOtp.getString("barcode");
                    response1 = parentObjectOtp.getString("response");

                    if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                        txt_barcode.setText(getBarcodeDetails.toUpperCase());

                    } else {
                        Toast.makeText(getContext(), "" + response1, Toast.LENGTH_SHORT).show();
                        txt_barcode.setText("Barcode*");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(getContext(), progressDialog);
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });


        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequestPop);
        Log.e(TAG, "onBindViewHolder: url" + jsonObjectRequestPop);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        openCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        chooseFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCamera() {
        camera = new com.mindorks.paracamera.Camera.Builder()
                .resetToCorrectOrientation(false)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(Constants.setcompression)
                .setImageHeight(Constants.setheight)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings, String value) {

    }


    private void ClearFields() {
        edt_firstname.setText("");
        edt_lastname.setText("");

        edt_age.setText("");
        edt_amtcollected.setText("");

        spr_gender.setSelection(0);
        txt_barcode.setText("Barcode*");
        ll_enterView.setVisibility(View.GONE);
        lin_by_missed.setVisibility(View.VISIBLE);
        lin_generate_verify.setVisibility(View.GONE);
        edt_missed_mobile.setEnabled(true);
        btn_generate.setVisibility(View.VISIBLE);
        btn_generate.setEnabled(true);
        rel_verify_mobile.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);
        edt_verifycc.getText().clear();
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        edt_missed_mobile.setText("");

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        //maindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, imagelist, 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
        if (imagelist != null && imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size()); // specify total count of indicators
            pageIndicatorView.setSelection(0);
            pageIndicatorView.setSelectedColor(activity.getResources().getColor(R.color.maroon));
        } else {
            pageIndicatorView.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
                pageIndicatorView.setSelectedColor(getResources().getColor(R.color.maroon));
                pageIndicatorView.setUnselectedColor(getResources().getColor(R.color.grey));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final Button btn_delete = maindialog.findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.VISIBLE);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Set the Alert Dialog Message
                builder.setMessage(ToastFile.deletefile);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (type.equalsIgnoreCase("adhar")) {

                            if (aadharlist != null && aadharlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (aadhar_file != null) {
                                        aadhar_file = null;
                                    } else {
                                        aadhar_file1 = null;
                                    }

                                    aadharlist.remove(aadharlist.get(0));
                                    // viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    aadhar_file1 = null;
                                    if (aadharlist.size() == 2) {
                                        aadharlist.remove(aadharlist.get(1));
                                    } else {
                                        aadharlist.remove(aadharlist.get(0));
                                    }
                                    // viewPagerAdapter.notifyDataSetChanged();
                                }

                                if (aadharlist.size() == 1) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_nofileadhar.setVisibility(View.VISIBLE);
                                    txt_adharfileupload.setVisibility(View.GONE);

                                }


                            } else {
                                txt_nofileadhar.setVisibility(View.VISIBLE);
                                lin_adhar_images.setVisibility(View.GONE);
                            }
                        }

                        dialog.dismiss();
                        maindialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ImageView ic_close = (ImageView) maindialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maindialog.dismiss();
            }
        });


        maindialog.setCancelable(true);
        maindialog.show();
    }

    private void GetCampId() {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), Api.LIVEAPI).create(APIInteface.class);

        CampIdRequestModel campIdRequestModel = new CampIdRequestModel();
        campIdRequestModel.setSourceCode(usercode);
        campIdRequestModel.setTestCode("COVID RAPID ANTIBODIES");

        Call<CampIdResponseModel> responseCall = apiInterface.GetCampID(campIdRequestModel);

        responseCall.enqueue(new Callback<CampIdResponseModel>() {
            @Override
            public void onResponse(Call<CampIdResponseModel> call, Response<CampIdResponseModel> response) {
                try {
                    GlobalClass.hideProgress(getContext(), progressDialog);
                    if (response.body().getResponseID().equalsIgnoreCase("RES0000")) {

                        ArrayList<String> spinnerlist = new ArrayList<>();
                        Campid = response.body().getOutput();
                        Global.sampletype = response.body().getSampleType();
                        Global.test = response.body().getTest();
                        spinnerlist.add("Select Camp-ID");
                        for (int i = 0; i < Campid.size(); i++) {
                            spinnerlist.add(Campid.get(i).getCampID());
                        }

                        if (spinnerlist != null) {
                            radiogrp2.setVisibility(View.VISIBLE);
                            lin_by_missed.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                                    getContext(), R.layout.name_age_spinner, spinnerlist);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_camp.setAdapter(adap);
                            spr_camp.setSelection(0);
                        }

                    } else {
                        radiogrp2.setVisibility(View.GONE);
                        lin_by_missed.setVisibility(View.GONE);
                        GlobalClass.ShowError(getActivity(), "Oops..", response.body().getResponse(), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CampIdResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
                Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());
            }
        });
    }

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                ClearFields();
                progressDialog.dismiss();
            } else {
                // Global.showCustomToast(activity, RESPONSE);
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle(RESPONSE)
                        .setCancelable(false)
                        .addButton("OK", -1, activity.getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                Covidpostdata covidpostdata = getCovidpostdata();
                                new Covidmultipart_controller(AntiBodyEnterFrag.this, activity, covidpostdata).execute();
                            }
                        });

                builder.show();
                GlobalClass.hideProgress(getContext(), progressDialog);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            GlobalClass.hideProgress(getContext(), progressDialog);
        }
    }
}