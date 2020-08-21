package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.Controller.Checkbarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.CovidMobverification_Controller;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.GenerateOTP_Controller;
import com.example.e5322.thyrosoft.Controller.GetHospitalController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ValidateOTP_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.Hospital_model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;


public class RATEnterFrag extends Fragment {

    Button btn_generate, btn_resend, btn_verify;
    View root;
    String hospt_ID;
    EditText edt_missed_mobile, edt_verifycc;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    SharedPreferences preferences;
    CountDownTimer countDownTimer;
    String TAG = getClass().getSimpleName();
    TextView tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob, txt_barcode;
    private boolean timerflag = false;
    RelativeLayout rel_mobno;
    LinearLayout lin_generate_verify, lin_by_missed, ll_enterView;
    RadioButton by_missed, by_generate;
    ImageView img_scanbarcode;
    RequestQueue requestQueue;
    private Camera camera;
    String ERROR, RES_ID, barcode, response1;
    RelativeLayout rel_verify_mobile;
    Button btn_submit, btn_reset, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_other;
    Spinner spr_gender;
    SearchableSpinner spr_hospital;
    private String userChoosenTask;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    EditText edt_firstname, edt_lastname, edt_age, edt_amtcollected;
    RadioButton rd_home, rd_dps;
    File aadhar_file = null;
    Bitmap bitmapimage;
    File aadhar_file1 = null;
    private int PERMISSION_REQUEST_CODE = 200;
    boolean isadhar, istrf, isother;
    File trf_file = null;
    File trf_file1 = null;
    File other_file = null;
    File other_file1 = null;
    LinearLayout lin_adhar_images, lin_trf_images, lin_other_images;
    TextView txt_adharfileupload, txt_nofileadhar, txt_trffileupload, txt_nofiletrf, txt_otherfileupload, txt_nofileother;
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    List<String> patientsagespinner = Arrays.asList("Gender", "Male", "Female");
    List<Hospital_model.HospitalDETAILSBean> hospitalDETAILSBeanList = new ArrayList<>();
    List<String> hospitalname = new ArrayList<>();
    private int agesinteger;
    Activity mActivity;

    public RATEnterFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_r_a_t_enter, container, false);
        cd = new ConnectionDetector(getContext());
        initui(root);
        initlistner();
        GetHospitalList();

        return root;
    }

    private void GetHospitalList() {
        try {
            if (ControllersGlobalInitialiser.getHospitalController != null) {
                ControllersGlobalInitialiser.getHospitalController = null;
            }
            ControllersGlobalInitialiser.getHospitalController = new GetHospitalController(mActivity, RATEnterFrag.this);
            ControllersGlobalInitialiser.getHospitalController.GetHosptitalController(apikey, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkpermProceed() {
        if (AccessRuntimePermissions.checkcameraPermission(mActivity) && AccessRuntimePermissions.checkExternalPerm(mActivity)) {
            selectImage();
        } else {
            AccessRuntimePermissions.requestCamerapermission(mActivity);
            AccessRuntimePermissions.requestExternalpermission(mActivity);
        }
    }


    private void initlistner() {

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

                        Log.e(TAG, "Hospial type ---> " + hospital);
                        Log.e(TAG, "hospt_ID  ---> " + hospt_ID);

                    }
                } else {
                    hospt_ID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        txt_trffileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trflist != null && trflist.size() > 0) {
                    setviewpager(trflist, "trf");
                }
            }
        });

        btn_choosefile_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (trf_file != null && trf_file1 != null) {
                    GlobalClass.showTastyToast(activity, "You can upload only two images", 0);
                } else {
                    isadhar = false;
                    isother = false;
                    istrf = true;
                    checkpermProceed();
                }

            }
        });


        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (other_file != null && other_file1 != null) {
                    GlobalClass.showTastyToast(activity, "You can upload only two images", 0);
                } else {
                    isadhar = false;
                    istrf = false;
                    isother = true;
                    checkpermProceed();
                }

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
                            String fname = edt_firstname.getText().toString().trim();
                            String lname = edt_lastname.getText().toString().trim();

                            String fullname = fname + " " + lname;
                            Log.e("NAME LENGHTH---->", "" + fullname.length());

                            Covidpostdata covidpostdata = new Covidpostdata();
                            covidpostdata.setSOURCECODE(usercode);
                            covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                            covidpostdata.setNAME(fullname);
                            covidpostdata.setAMOUNTCOLLECTED(edt_amtcollected.getText().toString());
                            covidpostdata.setTESTCODE("CRAT");
                            covidpostdata.setAGE(edt_age.getText().toString());
                            covidpostdata.setBARCODE(txt_barcode.getText().toString());


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
                            new Covidmultipart_controller(RATEnterFrag.this, activity, covidpostdata).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();

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

        img_scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(RATEnterFrag.this).initiateScan();


            }
        });

        txt_barcode.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_barcode.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_AUTO_CORRECT);
                } else {
                    txt_barcode.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                }
            }

        });

        txt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(RATEnterFrag.this).initiateScan();
            }
        });

        by_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));
            }
        });
        by_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                    if (!GlobalClass.isNull(edt_verifycc.getText().toString())) {
                        validateotp();
                    } else {
                        GlobalClass.showTastyToast(activity, "Kindly enter otp", 0);
                    }
                } else {
                    GlobalClass.showTastyToast(activity, "Kindly enter mobile number", 0);
                }
            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aadhar_file != null && aadhar_file1 != null) {
                    GlobalClass.showTastyToast(activity, MessageConstants.UPLOADTWOFILES, 0);
                } else {
                    checkpermProceed();
                    isadhar = true;
                    istrf = false;
                    isother = false;
                }


            }
        });
    }

    private void ClearFields() {

        GlobalClass.SetEditText(edt_firstname, "");
        GlobalClass.SetEditText(edt_lastname, "");
        GlobalClass.SetEditText(edt_age, "");
        GlobalClass.SetEditText(edt_amtcollected, "");


        spr_gender.setSelection(0);
        GlobalClass.SetText(txt_barcode, "Barcode*");
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

        GlobalClass.SetEditText(edt_missed_mobile, "");

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }


    }

    private boolean Validate() {

        if (edt_firstname.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, ToastFile.ENTER_FNAME, 2);
            edt_firstname.requestFocus();
            return false;
        }

        if (edt_firstname.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_FNAME, 2);
            edt_firstname.requestFocus();
            return false;
        }


        if (edt_lastname.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_LNAME, 2);
            edt_lastname.requestFocus();
            return false;
        }
        if (edt_lastname.getText().toString().length() < 1) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_LNAME, 2);
            edt_lastname.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(edt_age.getText().toString())) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_AGE, 2);
            edt_lastname.requestFocus();
            return false;
        }
        if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Gender")) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_GENDER, 2);
            return false;
        }

        if (txt_barcode.getText().toString().equalsIgnoreCase("Barcode*")) {
            GlobalClass.showTastyToast(getActivity(), MessageConstants.KDY_SCAN_BARCODE, 2);
            return false;
        }

        if (TextUtils.isEmpty(edt_amtcollected.getText().toString())) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.AMTCOLL, 2);
            edt_lastname.requestFocus();
            return false;
        }


        try {
            int amt = Integer.parseInt(edt_amtcollected.getText().toString());
            if (!GlobalClass.isNull(Global.B2C)) {
                if (amt > Integer.parseInt(Global.B2C)) {
                    GlobalClass.showTastyToast(mActivity, "You cannot enter amount collected more than " + Global.B2C, 2);
                    return false;
                } else if (amt < Integer.parseInt(Global.B2B)) {
                    GlobalClass.showTastyToast(mActivity, "You cannot enter amount collected less than " + Global.B2B, 2);
                    return false;
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }

        if (aadhar_file == null && aadhar_file1 == null) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_ADHIMAGE, 5);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE, 5);
            return false;
        }


        return true;
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
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
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }


    private void initui(View root) {

        activity = getActivity();
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        btn_generate = root.findViewById(R.id.btn_generate);
        GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        txt_otherfileupload = root.findViewById(R.id.txt_otherfileupload);
        btn_resend = root.findViewById(R.id.btn_resend);
        tv_timer = root.findViewById(R.id.tv_timer);
        edt_verifycc = root.findViewById(R.id.edt_verifycc);
        tv_resetno = root.findViewById(R.id.tv_resetno);
        btn_choosefile_other = root.findViewById(R.id.btn_choosefile_other);
        rel_mobno = root.findViewById(R.id.rel_mobno);
        lin_other_images = root.findViewById(R.id.lin_other_images);
        lin_generate_verify = root.findViewById(R.id.lin_generate_verify);
        tv_mobileno = root.findViewById(R.id.tv_mobileno);
        lin_trf_images = root.findViewById(R.id.lin_trf_images);
        lin_by_missed = root.findViewById(R.id.lin_missed_verify);
        by_missed = root.findViewById(R.id.by_missed);
        by_generate = root.findViewById(R.id.by_generate);
        btn_verify = root.findViewById(R.id.btn_verify);
        spr_gender = root.findViewById(R.id.spr_gender);

        spr_hospital = root.findViewById(R.id.spr_hospital);
        spr_hospital.setTitle("");

        txt_nofileadhar = root.findViewById(R.id.txt_nofileadhar);
        txt_adharfileupload = root.findViewById(R.id.txt_adharfileupload);
        lin_adhar_images = root.findViewById(R.id.lin_adhar_images);
        btn_choosefile_trf = root.findViewById(R.id.btn_choosefile_trf);
        txt_trffileupload = root.findViewById(R.id.txt_trffileupload);
        txt_nofiletrf = root.findViewById(R.id.txt_nofiletrf);
        txt_nofileother = root.findViewById(R.id.txt_nofileother);

        btn_choosefile_adhar = root.findViewById(R.id.btn_choosefile_adhar);
        if (patientsagespinner != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    getContext(), R.layout.name_age_spinner, patientsagespinner);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_gender.setAdapter(adap);
            spr_gender.setSelection(0);
        }
        ll_enterView = root.findViewById(R.id.ll_enterView);
        img_scanbarcode = root.findViewById(R.id.img_scanbarcode);
        rel_verify_mobile = root.findViewById(R.id.rel_verify_mobile);
        tv_verifiedmob = root.findViewById(R.id.tv_verifiedmob);
        btn_submit = root.findViewById(R.id.btn_submit);
        btn_reset = root.findViewById(R.id.btn_reset);
        edt_firstname = root.findViewById(R.id.edt_firstname);
        edt_lastname = root.findViewById(R.id.edt_lastname);
        edt_age = root.findViewById(R.id.edt_age);
        edt_amtcollected = root.findViewById(R.id.edt_amtcollected);


        txt_barcode = root.findViewById(R.id.txt_barcode);
        rd_home = root.findViewById(R.id.rd_home);
        rd_dps = root.findViewById(R.id.rd_dps);


        edt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    agesinteger = Integer.parseInt(s.toString());
                }

                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    GlobalClass.showTastyToast(activity,
                            ToastFile.crt_age,
                            2);
                    if (enteredString.length() > 0) {

                        GlobalClass.SetEditText(edt_age, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_age, "");
                    }
                }

            }

        });
    }


    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase(MessageConstants.Generate_CCC)) {
                mobileverify(edt_missed_mobile.getText().toString());
            } else {
                mobileverify(edt_missed_mobile.getText().toString());
            }
        }
    }

    private boolean mobilenovalidation() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_MOBILE, 2);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
            edt_missed_mobile.requestFocus();
            return false;
        }
        return true;
    }

    private void mobileverify(String mobileno) {
        try {
            if (ControllersGlobalInitialiser.covidMobverification_controller != null) {
                ControllersGlobalInitialiser.covidMobverification_controller = null;
            }
            ControllersGlobalInitialiser.covidMobverification_controller = new CovidMobverification_Controller(activity, RATEnterFrag.this);
            ControllersGlobalInitialiser.covidMobverification_controller.getcovidmobverifycontroller(apikey, mobileno, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateOtP(String mobileno) {
        try {
            if (ControllersGlobalInitialiser.generateOTP_controller != null) {
                ControllersGlobalInitialiser.generateOTP_controller = null;
            }
            ControllersGlobalInitialiser.generateOTP_controller = new GenerateOTP_Controller(activity, RATEnterFrag.this);
            ControllersGlobalInitialiser.generateOTP_controller.generteotpController(apikey, mobileno, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                GlobalClass.SetText(tv_timer, "Please wait 00:" + numberFormat.format(time));
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

    private void validateotp() {
        try {
            if (ControllersGlobalInitialiser.validateOTP_controller != null) {
                ControllersGlobalInitialiser.validateOTP_controller = null;
            }
            ControllersGlobalInitialiser.validateOTP_controller = new ValidateOTP_Controller(activity, RATEnterFrag.this);
            ControllersGlobalInitialiser.validateOTP_controller.getvalidateotp_Controller(apikey, edt_missed_mobile, edt_verifycc, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    GlobalClass.showTastyToast(activity, MessageConstants.Failedto_load_image, 2);
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
                        GlobalClass.SetText(txt_adharfileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_adharfileupload, "1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file.toString());

                    }

                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);

                        GlobalClass.SetText(txt_adharfileupload, "2 " + getResources().getString(R.string.imgupload));

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
                        GlobalClass.SetText(txt_trffileupload, "2 " + getResources().getString(R.string.imgupload));
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

                        GlobalClass.SetText(txt_trffileupload, "1 " + getResources().getString(R.string.imgupload));

                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file.toString());
                    }


                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);

                        GlobalClass.SetText(txt_trffileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_otherfileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_otherfileupload, "1 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_otherfileupload, "2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    }

                    lin_other_images.setVisibility(View.VISIBLE);
                }
            } else if (result != null) {
                Log.e(TAG, "onActivityResult: " + result);
                if (result.getContents() != null) {
                    String getBarcodeDetails = result.getContents();
                    if (getBarcodeDetails.length() == 8) {
                        passBarcodeData(getBarcodeDetails);
                    } else {
                        GlobalClass.showTastyToast(activity, invalid_brcd, 2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void passBarcodeData(final String getBarcodeDetails) {
        requestQueue = GlobalClass.setVolleyReq(getContext());

        String strurl = Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode";

        Log.v(TAG, "barcode url  --> " + strurl);

        try {
            if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                ControllersGlobalInitialiser.checkbarcode_controller = null;
            }
            ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller(activity, RATEnterFrag.this, getBarcodeDetails);
            ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(strurl, requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        GlobalClass.SetText(tv_verifiedmob, edt_missed_mobile.getText().toString());
        ll_enterView.setVisibility(View.VISIBLE);
        lin_by_missed.setVisibility(View.GONE);

        tv_mobileno.setVisibility(View.GONE);

        edt_missed_mobile.setEnabled(false);
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


    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, imagelist);
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
                                    GlobalClass.SetText(txt_adharfileupload, "1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    GlobalClass.SetText(txt_adharfileupload, "2 " + getResources().getString(R.string.imgupload));
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

                                    GlobalClass.SetText(txt_trffileupload, "1 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (trflist.size() == 2) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    GlobalClass.SetText(txt_trffileupload, "2 " + getResources().getString(R.string.imgupload));
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

                                    GlobalClass.SetText(txt_otherfileupload, "1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    GlobalClass.SetText(txt_otherfileupload, "2 " + getResources().getString(R.string.imgupload));

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

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (!GlobalClass.isNull(RESPONSEID) && RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                GlobalClass.showTastyToast(activity, RESPONSE, 1);
                ClearFields();
                Constants.universal = 1;
                Intent i = new Intent(activity, ManagingTabsActivity.class);
                startActivity(i);
                Constants.ratfrag_flag = "1";
                getActivity().finish();

            } else {
                GlobalClass.showTastyToast(activity, RESPONSE, 2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void gethospitalResponse(Response<Hospital_model> response) {

        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResID())
                    && response.body().getResID().equalsIgnoreCase(Constants.RES0000)) {
                if (response.body().getHospitalDETAILS() != null && response.body().getHospitalDETAILS().size() > 0) {
                    hospitalname.add("Select Hospital Name");
                    hospitalDETAILSBeanList.addAll(response.body().getHospitalDETAILS());

                    if (GlobalClass.CheckArrayList(hospitalDETAILSBeanList)) {
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
                    }

                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.NODATA, 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getcovidmobverifyResponse(Response<COVerifyMobileResponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                if (!GlobalClass.isNull(response.body().getResponse()) && response.body().getResponse().equalsIgnoreCase("NOT VERIFIED")) {
                    GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
                } else {
                    GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 1);
                }
                disablefields();
            } else if (!GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0082)) {
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            } else {
                if (btn_generate.getText().toString().equalsIgnoreCase(MessageConstants.Generate_CCC)) {
                    generateOtP(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateOTPResponse(Response<Covidotpresponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                setCountDownTimer();
                lin_by_missed.setVisibility(View.GONE);
                tv_mobileno.setVisibility(View.VISIBLE);
                GlobalClass.SetText(tv_mobileno, edt_missed_mobile.getText().toString());
                rel_mobno.setVisibility(View.VISIBLE);
                lin_generate_verify.setVisibility(View.VISIBLE);
                tv_resetno.setVisibility(View.VISIBLE);
                GlobalClass.SetText(tv_resetno, getResources().getString(R.string.reset_mob));
                tv_resetno.setPaintFlags(tv_resetno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 1);

            } else {
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getvalidateOTPResponse(Response<Covid_validateotp_res> response) {

        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                disablefields();
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 1);
            } else {
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getpassbarcode(JSONObject response, String barcodeDetails) {
        Log.v("barcode respponse", response.toString());
        Log.e(TAG, "onResponse: " + response);
        String finalJson = response.toString();
        JSONObject parentObjectOtp = null;
        try {
            parentObjectOtp = new JSONObject(finalJson);
            ERROR = parentObjectOtp.getString("ERROR");
            RES_ID = parentObjectOtp.getString("RES_ID");
            barcode = parentObjectOtp.getString("barcode");
            response1 = parentObjectOtp.getString("response");

            if (!GlobalClass.isNull(response1) && response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                GlobalClass.SetText(txt_barcode, barcodeDetails.toUpperCase());
            } else {
                GlobalClass.showTastyToast(mActivity, response1, 2);
                GlobalClass.SetText(txt_barcode, "Barcode*");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}