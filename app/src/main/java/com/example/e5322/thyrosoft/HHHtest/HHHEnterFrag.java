package com.example.e5322.thyrosoft.HHHtest;

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
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.Controller.HHHController;
import com.example.e5322.thyrosoft.HHHtest.Model.CampDetailsResponseModel;
import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.Hospital_model;
import com.example.e5322.thyrosoft.Models.Hospital_req;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.MultiSelectSpinner;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;


public class HHHEnterFrag extends Fragment implements MultiSelectSpinner.OnMultipleItemsSelectedListener {

    Button btn_generate, btn_resend, btn_verify, btn_reset, btn_submit, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_other;
    View root;
    EditText edt_missed_mobile, edt_verifycc, edt_email, edt_amtcollected, edt_firstname, edt_lastname, edt_age, edt_address, edt_pincode;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    SharedPreferences preferences;
    CountDownTimer countDownTimer;
    String hospt_ID, refbyname, campid;
    private Camera camera;
    private boolean timerflag = false;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private int PERMISSION_REQUEST_CODE = 200;
    ImageView img_scanbarcode;
    TextView tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob, txt_barcode;
    LinearLayout lin_generate_verify, lin_missed_verify, ll_submitview, ll_hospitalview;
    RadioButton by_missed, by_generate;
    RelativeLayout rel_verify_mobile;
    RelativeLayout rel_mobno;
    List<String> testlist = new ArrayList<>();
    List<CampDetailsResponseModel.CampBean> Campid = new ArrayList<>();
    GetTestResponseModel getTestResponseModel;
    Spinner spr_test, spr_gender, spr_occu, spr_specimen, spr_camp;
    SearchableSpinner spr_hospital;
    String ERROR, RES_ID, barcode, response1;
    List<Hospital_model.HospitalDETAILSBean> hospitalDETAILSBeanList = new ArrayList<>();
    List<String> hospitalname = new ArrayList<>();
    RequestQueue requestQueue;
    MultiSelectSpinner spn_symptoms;
    List<String> agespinner = Arrays.asList("Gender", "Male", "Female");
    LinearLayout ll_verify, ll_first, ll_diclaimer, ll_age, ll_barcode, ll_aadharview, ll_trfview, ll_otherview, ll_specimen_view, ll_email_view, ll_address_view, ll_whole_view;
    boolean isadhar, istrf, isother;
    File trf_file = null;
    File trf_file1 = null;
    File other_file = null;
    File other_file1 = null;
    File aadhar_file = null;
    File aadhar_file1 = null;
    RadioGroup rg_view;
    CheckBox cb_disclaimer;
    List<String> Occupation = Arrays.asList("Service", "Private Job", "Business", "Student", "Housewife", "Others");
    List<String> arrayList = Arrays.asList(/*"Select", */"Fever", "Shortness of Breath", "Cough", "Sore Throat", "NA");
    LinearLayout lin_adhar_images, lin_trf_images, ll_camp_id, lin_other_images, ll_last30, ll_occupation_view, ll_pincode_view, ll_spinnerlayout;
    TextView txt_adharfileupload, txt_nofileadhar, txt_trffileupload, txt_nofiletrf, txt_otherfileupload, txt_nofileother, txt_other;
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    Bitmap bitmapimage;
    private String Dicsclaimertext = "";
    AppPreferenceManager appPreferenceManager;


    public HHHEnterFrag() {
    }


    public static HHHEnterFrag newInstance() {
        return new HHHEnterFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_h_h_h_enter, container, false);

        initui(root);
        initlistner();
        CallGetTests();
        return root;
    }

    private void CallGetTests() {
        try {
            final ArrayList<String> values = new ArrayList<String>();
            final HashSet<String> radiohash = new HashSet<String>();
            final HashSet<String> hashSet1 = new HashSet<String>();
                getTestResponseModel = appPreferenceManager.getTestResponseModel();
                if (getTestResponseModel.getPoctHHHData() != null && getTestResponseModel.getPoctHHHData().size() > 0) {
                    for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                        if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                            values.add(getTestResponseModel.getPoctHHHData().get(i).getType());
                        }
                        radiohash.addAll(values);
                        values.clear();
                        values.addAll(radiohash);

                    }
                    for (int i = 0; i < values.size(); i++) {
                        final RadioButton rb_type = new RadioButton(getContext());
                        rb_type.setText(values.get(i));
                        rg_view.addView(rb_type);

                        if (values.size() == 1) {
                            rb_type.setChecked(true);
                            SetSpinner(hashSet1, rb_type);
                            rb_type.setVisibility(View.GONE);
                        } else {
                            rb_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        SetSpinner(hashSet1, rb_type);
                                    }
                                }
                            });
                        }
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
        }
    }


    private void ClearFields() {

        try {
            edt_firstname.setText("");
            edt_lastname.setText("");
            edt_age.setText("");
            edt_email.setText("");
            edt_address.setText("");
            edt_amtcollected.setText("");
            spr_test.setEnabled(true);
            rg_view.setEnabled(true);
            aadhar_file = null;
            aadhar_file1 = null;
            trf_file = null;
            trf_file1 = null;
            other_file = null;
            other_file1 = null;
            otherlist.clear();
            trflist.clear();
            timerflag = false;
            aadharlist.clear();
            spr_gender.setSelection(0);
            txt_barcode.setText("Barcode*");
            cb_disclaimer.setChecked(false);
            lin_missed_verify.setVisibility(View.VISIBLE);
            lin_generate_verify.setVisibility(View.GONE);
            ll_whole_view.setVisibility(View.GONE);
            edt_missed_mobile.setEnabled(true);
            btn_generate.setVisibility(View.VISIBLE);
            btn_generate.setEnabled(true);
            rel_verify_mobile.setVisibility(View.GONE);
            ll_submitview.setVisibility(View.GONE);
            tv_resetno.setVisibility(View.GONE);
            edt_verifycc.getText().clear();
            tv_mobileno.setVisibility(View.GONE);
            rel_mobno.setVisibility(View.GONE);
            edt_missed_mobile.setText("");

            lin_adhar_images.setVisibility(View.GONE);
            lin_trf_images.setVisibility(View.GONE);
            lin_other_images.setVisibility(View.GONE);

            txt_nofileadhar.setVisibility(View.VISIBLE);
            txt_nofiletrf.setVisibility(View.VISIBLE);
            txt_nofileother.setVisibility(View.VISIBLE);

            btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
            btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));

            btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidbtn));
            btn_choosefile_trf.setTextColor(getResources().getColor(R.color.maroon));

            btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
            btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));


            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
                tv_timer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initlistner() {


        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
            }
        });
        txt_otherfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherlist != null && otherlist.size() > 0) {
                    setviewpager(otherlist, "other");
                }
            }
        });

        cb_disclaimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("" + Dicsclaimertext);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });


        txt_trffileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trflist != null && trflist.size() > 0) {
                    setviewpager(trflist, "trf");
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
                                edt_amtcollected.setText(Campid.get(i).getAmountCollected());
                                edt_address.setText(Campid.get(i).getCampAddress());
                                edt_pincode.setText(Campid.get(i).getCampPincode());
                                edt_email.setText(Campid.get(i).getRefDrEmail());
                                refbyname = Campid.get(i).getRefDrname();
                                campid = Campid.get(i).getCampID();
                            }
                        }
                        if (spr_camp.getSelectedItem().toString().equalsIgnoreCase("Select Camp-ID")) {
                            edt_amtcollected.setText("");
                            edt_address.setText("");
                            edt_pincode.setText("");
                            edt_email.setText("");
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
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        GlobalClass.showAlertDialog(getActivity());
                    } else {
                        try {
                            if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                                ControllersGlobalInitialiser.covidmultipart_controller = null;
                            }
                            String fname = edt_firstname.getText().toString().trim().toUpperCase();
                            String lname = edt_lastname.getText().toString().trim().toUpperCase();
                            String fullname = fname + " " + lname;
                            Covidpostdata covidpostdata = new Covidpostdata();
                            covidpostdata.setSOURCECODE(usercode);
                            covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                            covidpostdata.setNAME(fullname);
                            covidpostdata.setAMOUNTCOLLECTED(edt_amtcollected.getText().toString());
                            for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                                if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                                    for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                                        if (spr_test.getSelectedItem().toString().equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName())) {
                                            covidpostdata.setTESTCODE(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestCodeId());
                                        }
                                    }
                                }
                            }

                            covidpostdata.setAGE(edt_age.getText().toString());
                            covidpostdata.setBARCODE(txt_barcode.getText().toString());
                            covidpostdata.setEMAIL(edt_email.getText().toString());
                            covidpostdata.setADDRESS(edt_address.getText().toString().toUpperCase());
                            try {
                                for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                                    if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                                        for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                                            if (getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getSampleType().size() == 1) {
                                                covidpostdata.setSPECIMENTYPE(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getSampleType().get(0));
                                            } else {
                                                covidpostdata.setSPECIMENTYPE(spr_specimen.getSelectedItem().toString());
                                            }

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!GlobalClass.isNull(hospt_ID)) {
                                covidpostdata.setHOSPITAL(hospt_ID);
                            }
                            covidpostdata.setENTERBY(usercode);
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

                            if (trf_file != null && trf_file1 != null) {
                                covidpostdata.setTRF(trf_file);
                                covidpostdata.setTRF1(trf_file1);
                            } else if (trf_file != null && trf_file1 == null) {
                                covidpostdata.setTRF(trf_file);
                            } else {
                                covidpostdata.setTRF(trf_file1);
                            }

                            if (other_file != null && other_file1 != null) {
                                covidpostdata.setOTHER(other_file);
                                covidpostdata.setOTHER1(other_file1);
                            } else if (other_file != null && other_file1 == null) {
                                covidpostdata.setOTHER(other_file);
                            } else {
                                covidpostdata.setOTHER(other_file1);
                            }
                            new HHHController(HHHEnterFrag.this, activity, covidpostdata).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
        });


        spr_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String hospital = spr_hospital.getSelectedItem().toString().trim();
                if (!hospital.equalsIgnoreCase("Select Hospital Name")) {
                    if (hospitalDETAILSBeanList != null) {
                        for (int i = 0; i < hospitalDETAILSBeanList.size(); i++) {
                            if (hospitalDETAILSBeanList.get(i).getName().toUpperCase().equalsIgnoreCase(hospital)) {
                                hospt_ID = hospitalDETAILSBeanList.get(i).getId();
                                hospital = hospitalDETAILSBeanList.get(i).getName();
                            }
                        }
                    }
                } else {
                    hospt_ID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img_scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(HHHEnterFrag.this).initiateScan();
            }
        });
        txt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(HHHEnterFrag.this).initiateScan();
            }
        });

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
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                    if (!GlobalClass.isNull(edt_verifycc.getText().toString())) {
                        if (cd.isConnectingToInternet()) {
                            validateotp();
                        } else {
                            GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN, 0);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, "Kindly enter otp", 0);
                    }
                } else {
                    GlobalClass.showCustomToast(activity, "Kindly enter mobile number", 0);
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
                lin_missed_verify.setVisibility(View.VISIBLE);
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
        spr_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ViewGone();
                for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                    for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                        if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                            LoadDynamic(i);
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {

                        isadhar = true;
                        istrf = false;
                        isother = false;
                        selectImage();
                    }

                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (trf_file != null && trf_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        isother = false;
                        istrf = true;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        istrf = false;
                        isother = true;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });
    }


    private boolean Validate() {

        for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
            if (getTestResponseModel.getPoctHHHData().get(i).getDisplay().equalsIgnoreCase(Global.HHHTest)) {
                for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getFileds().size(); j++) {


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("First Name")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
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
                        }
                    }

                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Age")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {

                            if (GlobalClass.isNull(edt_age.getText().toString())) {
                                Global.showCustomToast(getActivity(), ToastFile.ENTER_AGE);
                                edt_age.requestFocus();
                                return false;
                            }

                            if (edt_age.getText().toString().startsWith("0")) {
                                Global.showCustomToast(getActivity(), "Age cannot start with zero");
                                edt_age.requestFocus();
                                return false;
                            }
                            int age = Integer.parseInt(edt_age.getText().toString());
                            if (120 < age) {
                                Global.showCustomToast(getActivity(), "Age cannot enter more than 120");
                                edt_age.requestFocus();
                                return false;
                            }

                        }
                    }


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Gender")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Gender")) {
                                Global.showCustomToast(getActivity(), ToastFile.SELECT_GENDER);
                                return false;
                            }
                        }
                    }

                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Barcode")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (txt_barcode.getText().toString().equalsIgnoreCase("Barcode*")) {
                                Global.showCustomToast(getActivity(), "Kindly Scan the Barcode");
                                return false;
                            }
                        }
                    }


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Amount Collected")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (GlobalClass.isNull(edt_amtcollected.getText().toString())) {
                                Global.showCustomToast(getActivity(), ToastFile.AMTCOLL);
                                edt_amtcollected.requestFocus();
                                return false;
                            }
                            int amt = Integer.parseInt(edt_amtcollected.getText().toString());
                            try {
                                for (int k = 0; k < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); k++) {
                                    String B2c = getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(k).getB2C();
                                    String B2b = getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(k).getB2B();
                                    if (!GlobalClass.isNull(B2c)) {
                                        if (amt > Integer.parseInt(B2c)) {
                                            TastyToast.makeText(getActivity(), "You cannot enter amount collected more than " + B2c, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                            return false;
                                        } else if (amt < Integer.parseInt(B2b)) {
                                            TastyToast.makeText(getActivity(), "You cannot enter amount collected less than " + B2b, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                            return false;
                                        }

                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Aadhaar/Passport")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (aadhar_file == null && aadhar_file1 == null) {
                                Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
                                return false;
                            }
                        }
                    }

                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("TRF")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (trf_file == null && trf_file1 == null) {
                                Global.showCustomToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE);
                                return false;
                            }
                        }
                    }

                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Upload Pre-counselling form")) {
                        for (int k = 0; k < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); k++) {
                            if (spr_test.getSelectedItem().toString().equalsIgnoreCase("HIV (RAPID TEST)")) {
                                if (other_file == null && other_file1 == null) {
                                    Global.showCustomToast(getActivity(), "Kindly Upload Pre-counseling form");
                                    return false;
                                }
                            }
                        }
                    }


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Disclaimer")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (!cb_disclaimer.isChecked()) {
                                Global.showCustomToast(getActivity(), "Kindly check the Disclaimer");
                                return false;
                            }
                        }
                    }


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Email Id")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (edt_email.getText().toString().length() == 0 || edt_email.getText().toString().isEmpty()) {
                                Global.showCustomToast(getActivity(), ToastFile.ENTER_EMAIL);
                                edt_email.requestFocus();
                                return false;
                            }
                            if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                Global.showCustomToast(getActivity(), "Enter valid Email-id");
                                edt_email.requestFocus();
                                return false;
                            }
                        }
                    }


                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Address")) {
                        if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equals("1")) {
                            if (edt_address.getText().toString().length() == 0 || edt_address.getText().toString().isEmpty()) {
                                Global.showCustomToast(getActivity(), "Please Enter Address");
                                edt_address.requestFocus();
                                return false;
                            }
                            if (edt_address.getText().toString().length() < 25) {
                                Global.showCustomToast(getActivity(), "Address minimum length should be 25");
                                edt_address.requestFocus();
                                return false;
                            }

                            if (edt_address.getText().toString().startsWith(" ")) {
                                Global.showCustomToast(getActivity(), "Address cannot start with space");
                                edt_address.requestFocus();
                                return false;
                            }

                        }
                    }

                }
            }
        }


        return true;
    }

    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, imagelist, 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
        if (imagelist != null && imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size());
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
                                } else if (1 == viewPager.getCurrentItem()) {
                                    aadhar_file1 = null;
                                    if (aadharlist.size() == 2) {
                                        aadharlist.remove(aadharlist.get(1));
                                    } else {
                                        aadharlist.remove(aadharlist.get(0));
                                    }
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
                        } else if (type.equalsIgnoreCase("trf")) {
                            if (trflist != null && trflist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (trf_file != null) {
                                        trf_file = null;
                                    } else {
                                        trf_file1 = null;
                                    }
                                    trflist.remove(trflist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    trf_file1 = null;
                                    if (trflist.size() == 2) {
                                        trflist.remove(trflist.get(1));
                                    } else {
                                        trflist.remove(trflist.get(0));
                                    }
                                }

                                if (trflist.size() == 1) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (trflist.size() == 2) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_nofiletrf.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.GONE);
                                }

                            } else {
                                txt_nofiletrf.setVisibility(View.VISIBLE);
                                lin_trf_images.setVisibility(View.GONE);
                            }

                        } else if (type.equalsIgnoreCase("other")) {
                            if (otherlist != null && otherlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (other_file != null) {
                                        other_file = null;
                                    } else {
                                        other_file1 = null;
                                    }

                                    otherlist.remove(otherlist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                }

                                if (otherlist.size() == 1) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_nofileother.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.GONE);
                                }

                            } else {
                                txt_nofileother.setVisibility(View.VISIBLE);
                                lin_other_images.setVisibility(View.GONE);
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

    private void ViewGone() {
        ll_first.setVisibility(View.GONE);
        ll_age.setVisibility(View.GONE);
        ll_email_view.setVisibility(View.GONE);
        ll_address_view.setVisibility(View.GONE);
        ll_hospitalview.setVisibility(View.GONE);
        ll_barcode.setVisibility(View.GONE);
        edt_amtcollected.setVisibility(View.GONE);
        ll_aadharview.setVisibility(View.GONE);
        ll_trfview.setVisibility(View.GONE);
        ll_otherview.setVisibility(View.GONE);
        ll_pincode_view.setVisibility(View.GONE);
        ll_occupation_view.setVisibility(View.GONE);
        ll_last30.setVisibility(View.GONE);
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
        final String[] userChoosenTask = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask[0] = "Take Photo";
                    if (result)
                        openCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask[0] = "Choose from Library";
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
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(activity, "Failed to load image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isadhar) {
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


                } else if (istrf) {

                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file1 = GlobalClass.getCompressedFile(activity, trf_file1);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            trf_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file.toString());
                    }


                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));

                    }

                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {
                        if (data.getData() != null) {
                            if (other_file1 == null) {
                                other_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file1 = GlobalClass.getCompressedFile(activity, other_file1);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file1.toString());

                    } else {
                        if (data.getData() != null) {
                            other_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file = GlobalClass.getCompressedFile(activity, other_file);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file.toString());
                    }


                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    }

                    lin_other_images.setVisibility(View.VISIBLE);
                }
            } else if (result != null) {
                if (result.getContents() != null) {
                    String getBarcodeDetails = result.getContents();
                    if (getBarcodeDetails.length() == 8) {
                        passBarcodeData(getBarcodeDetails);
                    } else {
                        Toast.makeText(getContext(), invalid_brcd, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                if (istrf) {

                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file1;
                        trf_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file1);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(imageurl);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file;
                        trf_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(imageurl);
                    }

                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));

                    }
                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isadhar) {

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

                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        other_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + other_file1;
                        other_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), other_file1);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(imageurl);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        other_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + other_file;
                        other_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), other_file);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(imageurl);
                    }


                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    }
                    lin_other_images.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void LoadDynamic(int i) {
        for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getFileds().size(); j++) {
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("By using missed call CCC")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_verify.setVisibility(View.VISIBLE);

                } else {
                    ll_verify.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("First Name")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_first.setVisibility(View.VISIBLE);

                } else {
                    ll_first.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Camp id")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_camp_id.setVisibility(View.VISIBLE);
                    GetCampId();
                } else {
                    ll_camp_id.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Age")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_age.setVisibility(View.VISIBLE);
                } else {
                    ll_age.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Barcode")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_barcode.setVisibility(View.VISIBLE);
                } else {
                    ll_barcode.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Address")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_address_view.setVisibility(View.VISIBLE);
                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        edt_address.setHint("Address*");
                    }
                } else {
                    ll_address_view.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Amount Collected")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    edt_amtcollected.setVisibility(View.VISIBLE);
                } else {
                    edt_amtcollected.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Aadhaar/Passport")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_aadharview.setVisibility(View.VISIBLE);
                } else {
                    ll_aadharview.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Disclaimer")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_diclaimer.setVisibility(View.VISIBLE);
                    SetUpDiscalaimer(getTestResponseModel.getPoctHHHData().get(i));
                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        cb_disclaimer.setText("Disclaimer*");
                    }
                } else {
                    ll_diclaimer.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("TRF")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_trfview.setVisibility(View.VISIBLE);
                } else {
                    ll_trfview.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Select Hospital Name")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_hospitalview.setVisibility(View.VISIBLE);
                    GetHospitalList();
                } else {
                    ll_hospitalview.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Email ID")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_email_view.setVisibility(View.VISIBLE);
                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        edt_email.setHint("Email Address*");
                    }
                } else {
                    ll_email_view.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("SpecimenType")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_specimen_view.setVisibility(View.VISIBLE);
                    SetSpecimenSpinner();
                } else {
                    ll_specimen_view.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Pincode")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_pincode_view.setVisibility(View.VISIBLE);
                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        edt_pincode.setHint("Pincode*");
                    }
                } else {
                    ll_pincode_view.setVisibility(View.GONE);
                }
            }
            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("In Last 30 days,do you have:")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_last30.setVisibility(View.VISIBLE);
                } else {
                    ll_last30.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Address Proof")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_otherview.setVisibility(View.VISIBLE);
                    txt_other.setText("Address Proof");
                    if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        txt_other.setText("Address Proof*");
                    }
                } else {
                    ll_otherview.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Upload PCF")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_otherview.setVisibility(View.VISIBLE);
                    //  txt_other.setText("Upload Pre-counselling form");

                    for (int k = 0; k < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); k++) {
                        if (spr_test.getSelectedItem().toString().equalsIgnoreCase("HIV (RAPID TEST)")) {
                            txt_other.setText("Upload PCF*");
                        } else {
                            txt_other.setText("Upload PCF");
                        }
                    }
                  /*  if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsCompulsary().equalsIgnoreCase("1")) {
                        txt_other.setText("Upload Pre-counselling form*");
                    }*/
                } else {
                    ll_otherview.setVisibility(View.GONE);
                }
            }


            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Occupation")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_occupation_view.setVisibility(View.VISIBLE);
                } else {
                    ll_occupation_view.setVisibility(View.GONE);
                }
            }

            if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getName().equalsIgnoreCase("Other")) {
                if (getTestResponseModel.getPoctHHHData().get(i).getFileds().get(j).getIsActive().equals("1")) {
                    ll_otherview.setVisibility(View.VISIBLE);
                    txt_other.setText("Other");
                } else {
                    ll_otherview.setVisibility(View.GONE);
                }
            }
        }
    }

    private void SetUpDiscalaimer(GetTestResponseModel.PoctHHHDataBean poctHHHDataBean) {

        for (int i = 0; i < poctHHHDataBean.getTestDetails().size(); i++) {
            if (spr_test.getSelectedItem().toString().equalsIgnoreCase(poctHHHDataBean.getTestDetails().get(i).getTestName())) {
                Dicsclaimertext = poctHHHDataBean.getTestDetails().get(i).getDisclaimer();
            }
        }
    }

    private void SetSpecimenSpinner() {
        try {
            ArrayList<String> abc = new ArrayList<>();
            HashSet<String> hashSet = new HashSet<>();
            for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                    if (getTestResponseModel.getPoctHHHData().get(i).getDisplay().equalsIgnoreCase(Global.HHHTest)) {
                        abc.addAll(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getSampleType());
                    }
                }
            }
            hashSet.addAll(abc);
            abc.clear();
            abc.addAll(hashSet);
            if (abc != null) {
                ArrayAdapter<String> adap = new ArrayAdapter<String>(
                        getContext(), R.layout.name_age_spinner, abc);
                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_specimen.setAdapter(adap);
                spr_specimen.setSelection(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initui(View root) {
        cd = new ConnectionDetector(getContext());
        activity = getActivity();
        appPreferenceManager=new AppPreferenceManager(activity);
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        btn_generate = root.findViewById(R.id.btn_generate);
        btn_generate.setText(getResources().getString(R.string.enterccc));
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        rg_view = root.findViewById(R.id.rg_view);
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        img_scanbarcode = root.findViewById(R.id.img_scanbarcode);
        ll_camp_id = root.findViewById(R.id.ll_camp_id);
        edt_firstname = root.findViewById(R.id.edt_firstname);
        spr_camp = root.findViewById(R.id.spr_camp);
        edt_lastname = root.findViewById(R.id.edt_lastname);
        edt_age = root.findViewById(R.id.edt_age);
        cb_disclaimer = root.findViewById(R.id.cb_disclaimer);
        edt_address = root.findViewById(R.id.edt_address);
        edt_pincode = root.findViewById(R.id.edt_pincode);
        lin_trf_images = root.findViewById(R.id.lin_trf_images);
        edt_email = root.findViewById(R.id.edt_email);
        GlobalClass.setEmailFilter(edt_email);
        txt_barcode = root.findViewById(R.id.txt_barcode);
        lin_other_images = root.findViewById(R.id.lin_other_images);
        btn_resend = root.findViewById(R.id.btn_resend);
        tv_timer = root.findViewById(R.id.tv_timer);
        rel_verify_mobile = root.findViewById(R.id.rel_verify_mobile);
        by_missed = root.findViewById(R.id.by_missed);
        spn_symptoms = root.findViewById(R.id.spn_symptoms);
        ll_diclaimer = root.findViewById(R.id.ll_diclaimer);
        tv_verifiedmob = root.findViewById(R.id.tv_verifiedmob);
        txt_otherfileupload = root.findViewById(R.id.txt_otherfileupload);
        by_generate = root.findViewById(R.id.by_generate);
        btn_reset = root.findViewById(R.id.btn_reset);
        btn_submit = root.findViewById(R.id.btn_submit);
        lin_generate_verify = root.findViewById(R.id.lin_generate_verify);
        rel_mobno = root.findViewById(R.id.rel_mobno);
        spr_test = root.findViewById(R.id.spr_test);
        ll_verify = root.findViewById(R.id.ll_verify);
        ll_first = root.findViewById(R.id.ll_first);
        ll_age = root.findViewById(R.id.ll_age);
        ll_barcode = root.findViewById(R.id.ll_barcode);
        ll_last30 = root.findViewById(R.id.ll_last30);
        ll_occupation_view = root.findViewById(R.id.ll_occupation_view);
        ll_pincode_view = root.findViewById(R.id.ll_pincode_view);
        ll_spinnerlayout = root.findViewById(R.id.ll_spinnerlayout);
        btn_verify = root.findViewById(R.id.btn_verify);
        tv_resetno = root.findViewById(R.id.tv_resetno);
        tv_mobileno = root.findViewById(R.id.tv_mobileno);
        ll_whole_view = root.findViewById(R.id.ll_whole_view);
        edt_amtcollected = root.findViewById(R.id.edt_amtcollected);
        edt_verifycc = root.findViewById(R.id.edt_verifycc);
        ll_address_view = root.findViewById(R.id.ll_address_view);
        ll_aadharview = root.findViewById(R.id.ll_aadharview);
        txt_other = root.findViewById(R.id.txt_other);
        ll_trfview = root.findViewById(R.id.ll_trfview);
        ll_email_view = root.findViewById(R.id.ll_email_view);
        spr_hospital = root.findViewById(R.id.spr_hospital);
        spr_hospital.setTitle("");

        txt_nofileadhar = root.findViewById(R.id.txt_nofileadhar);
        txt_adharfileupload = root.findViewById(R.id.txt_adharfileupload);
        lin_adhar_images = root.findViewById(R.id.lin_adhar_images);
        txt_trffileupload = root.findViewById(R.id.txt_trffileupload);
        txt_nofiletrf = root.findViewById(R.id.txt_nofiletrf);
        txt_nofileother = root.findViewById(R.id.txt_nofileother);

        lin_missed_verify = root.findViewById(R.id.lin_missed_verify);
        btn_choosefile_adhar = root.findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = root.findViewById(R.id.btn_choosefile_trf);
        btn_choosefile_other = root.findViewById(R.id.btn_choosefile_other);
        ll_submitview = root.findViewById(R.id.ll_submitview);
        ll_hospitalview = root.findViewById(R.id.ll_hospitalview);
        spr_occu = root.findViewById(R.id.spr_occu);
        spr_specimen = root.findViewById(R.id.spr_specimen);
        ll_otherview = root.findViewById(R.id.ll_otherview);
        ll_specimen_view = root.findViewById(R.id.ll_specimen_view);
        spr_gender = root.findViewById(R.id.spr_gender);

        if (agespinner != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    getContext(), R.layout.name_age_spinner, agespinner);
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
        spn_symptoms.setSelection(new int[]{naPos});
        spn_symptoms.setListener(this);
    }


    private void GetCampId() {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), Api.LIVEAPI).create(APIInteface.class);


        CampIdRequestModel campIdRequestModel = new CampIdRequestModel();
        campIdRequestModel.setSourceCode(usercode);
        campIdRequestModel.setTestCode("CRAB");

        Call<CampDetailsResponseModel> responseCall = apiInterface.GetCampDetails(campIdRequestModel);

        responseCall.enqueue(new Callback<CampDetailsResponseModel>() {
            @Override
            public void onResponse(Call<CampDetailsResponseModel> call, Response<CampDetailsResponseModel> response) {
                try {
                    GlobalClass.hideProgress(getContext(), progressDialog);
                    if (response.body().getResponseID().equalsIgnoreCase("RES000")) {

                        ArrayList<String> spinnerlist = new ArrayList<>();
                        Campid = response.body().getCamp();
                        spinnerlist.add("Select Camp-ID");
                        for (int i = 0; i < Campid.size(); i++) {
                            spinnerlist.add(Campid.get(i).getCampID());
                        }
                        if (spinnerlist != null) {
                            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                                    getContext(), R.layout.name_age_spinner, spinnerlist);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_camp.setAdapter(adap);
                            spr_camp.setSelection(0);
                        }

                    } else {
                        ll_verify.setVisibility(View.GONE);
                        GlobalClass.ShowError(getActivity(), "Oops..", response.body().getResponse(), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CampDetailsResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
            }
        });
    }

    private void validateotp() {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Covid_validateotp_req covid_validateotp_req = new Covid_validateotp_req();
        covid_validateotp_req.setApi_key(apikey);
        covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
        covid_validateotp_req.setOtp(edt_verifycc.getText().toString());
        covid_validateotp_req.setScode(usercode);

        Call<Covid_validateotp_res> covidotpresponseCall = postAPIInterface.validateotp(covid_validateotp_req);
        covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
            @Override
            public void onResponse(Call<Covid_validateotp_res> call, Response<Covid_validateotp_res> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                            disablefields();
                            Toast.makeText(activity, "" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                        } else {
                            GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covid_validateotp_res> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });
    }

    private void disablefields() {


        rel_verify_mobile.setVisibility(View.VISIBLE);
        ll_whole_view.setVisibility(View.VISIBLE);
        tv_verifiedmob.setText(edt_missed_mobile.getText().toString());
        lin_missed_verify.setVisibility(View.GONE);
        tv_mobileno.setVisibility(View.GONE);
        edt_missed_mobile.setEnabled(false);
        ll_submitview.setVisibility(View.VISIBLE);

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

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 0);
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
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
        coVerifyMobReq.setApi_key(apikey);
        coVerifyMobReq.setMobile(mobileno);
        coVerifyMobReq.setScode(usercode);

        final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInterface.covmobileVerification(coVerifyMobReq);
        covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
            @Override
            public void onResponse(Call<COVerifyMobileResponse> call, Response<COVerifyMobileResponse> response) {
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
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });

    }

    private void generateOtP(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);

        COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
        coviDgetotp_req.setApi_key(apikey);
        coviDgetotp_req.setMobile(mobileno);
        coviDgetotp_req.setScode(usercode);

        Call<Covidotpresponse> covidotpresponseCall = postAPIInterface.generateotp(coviDgetotp_req);
        covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
            @Override
            public void onResponse(Call<Covidotpresponse> call, Response<Covidotpresponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        setCountDownTimer();
                        lin_missed_verify.setVisibility(View.GONE);
                        tv_mobileno.setVisibility(View.VISIBLE);
                        tv_mobileno.setText(edt_missed_mobile.getText().toString());
                        rel_mobno.setVisibility(View.VISIBLE);
                        lin_generate_verify.setVisibility(View.VISIBLE);
                        tv_resetno.setVisibility(View.VISIBLE);
                        tv_resetno.setText(getResources().getString(R.string.reset_mob));
                        tv_resetno.setPaintFlags(tv_resetno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                    Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidotpresponse> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });
    }

    private void GetHospitalList() {
        Hospital_req hospital_req = new Hospital_req();
        hospital_req.setApiKey(apikey);
        hospital_req.setUserCode(usercode);

        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Call<Hospital_model> covidratemodelCall = postAPIInterface.GetWOEHospital(hospital_req);

        covidratemodelCall.enqueue(new Callback<Hospital_model>() {
            @Override
            public void onResponse(Call<Hospital_model> call, Response<Hospital_model> response) {
                try {
                    if (response.body().getResID().equalsIgnoreCase(Constants.RES0000)) {
                        if (response.body().getHospitalDETAILS() != null && response.body().getHospitalDETAILS().size() > 0) {
                            hospitalname.add("Select Hospital Name");
                            hospitalDETAILSBeanList.addAll(response.body().getHospitalDETAILS());

                            for (int i = 0; i < hospitalDETAILSBeanList.size(); i++) {
                                if (!GlobalClass.isNull(hospitalDETAILSBeanList.get(i).getName())) {
                                    hospitalname.add(hospitalDETAILSBeanList.get(i).getName());
                                }
                            }

                            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                                    activity, R.layout.name_age_spinner, hospitalname);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_hospital.setAdapter(adap);
                            spr_hospital.setSelection(0);

                        } else {
                            Toast.makeText(activity, MessageConstants.NODATA, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Hospital_model> call, Throwable t) {

            }
        });

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
        System.out.println("barcode url  --> " + Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode"
                , new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("barcode respponse" + response);
                GlobalClass.hideProgress(getContext(), progressDialog);
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
    }

    private void setCountDownTimer() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (timerflag == false) {
                    btn_resend.setEnabled(false);
                    btn_resend.setVisibility(View.GONE);
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    tv_timer.setVisibility(View.GONE);
                    btn_resend.setVisibility(View.VISIBLE);
                    btn_resend.setEnabled(true);
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



    private void SetSpinner(HashSet<String> hashSet1, RadioButton rb_type) {
        ll_spinnerlayout.setVisibility(View.VISIBLE);
        testlist.clear();
        hashSet1.clear();
        for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
            if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                if (getTestResponseModel.getPoctHHHData().get(i).getType().equalsIgnoreCase(rb_type.getText().toString())) {
                    for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                        testlist.add(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName().trim());
                        hashSet1.addAll(testlist);
                        testlist.clear();
                        testlist.addAll(hashSet1);
                    }

                }
            }
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(getContext(), R.layout.name_age_spinner, testlist);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_test.setAdapter(adap);
        spr_test.setSelection(0);
    }


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings, String value) {

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
               /* Constants.universal = 1;
                Intent i = new Intent(activity, ManagingTabsActivity.class);
                startActivity(i);
                Constants.ratfrag_flag = "1";

                getActivity().finish();*/

            } else {
                Global.showCustomToast(activity, RESPONSE);
            }
        } catch (JSONException e) {
            GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
            e.printStackTrace();
        }
    }
}