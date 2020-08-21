package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.CovidMobverification_Controller;
import com.example.e5322.thyrosoft.Controller.CovidRates_Controller;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.GenerateOTP_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ValidateOTP_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class Covidenter_Frag extends Fragment implements View.OnClickListener {
    Activity activity;
    ConnectionDetector cd;
    LinearLayout mainlinear;
    String b2b, b2c;
    List<String> presclist = new ArrayList<>();
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> viallist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    List<String> selfielist = new ArrayList<>();
    CountDownTimer countDownTimer;
    public RadioButton by_missed, by_generate;
    LinearLayout lin_by_missed, lin_selfie, lin_generate_verify, lin_pres_preview, lin_adhar_images, lin_trf_images, lin_vial_images, lin_other_images;
    RelativeLayout rel_mobno;
    Button btn_choosefile_presc, btn_selfie, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_vial, btn_choosefile_other;
    Button btn_generate, btn_submit, btn_verify, btn_resend, btn_reset;
    TextView txt_nofilepresc, txt_nofileadhar, txt_selfie, txt_nofiletrf, txt_nofilevial, txt_nofileother, tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob;
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    private String userChoosenTask;
    private Camera camera;
    Uri imageUri;
    private boolean timerflag = false;
    String TAG = getClass().getSimpleName();
    Bitmap bitmapimage;
    File presc_file = null;
    File aadhar_file = null;
    File aadhar_file1 = null;
    File trf_file = null;
    File trf_file1 = null;
    File vial_file = null;
    File other_file = null;
    File other_file1 = null;
    File selfie_file = null;
    TextView txt_presfileupload, txt_selfiefileupload, txt_adharfileupload, txt_trffileupload, txt_vialrfileupload, txt_otherfileupload;
    boolean ispresciption, isadhar, istrf, isvial, isother;
    EditText edt_fname, edt_amt, edt_lname, edt_missed_mobile, edt_verifycc;
    SharedPreferences preferences;
    String usercode, apikey;
    boolean verifyotp = false;
    RelativeLayout rel_verify_mobile;
    private int CAMERA_PHOTO_REQUEST_CODE = 1001;
    private int selfie_flag = 0;
    Activity mActivity;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View viewMain = (View) inflater.inflate(R.layout.covid_enter, container, false);
        activity = getActivity();
        cd = new ConnectionDetector(activity);
        return viewMain;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        displayrate();
        initListener();

    }

    private void initListener() {
        btn_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyotp) {
                    if (selfie_flag == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE);
                    } else {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.Upload_selfie, 2);
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }


            }
        });

        txt_selfiefileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(selfielist, "selfie");
            }
        });

        btn_generate.setOnClickListener(this);
        mainlinear.setOnClickListener(this);

        edt_fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        GlobalClass.SetEditText(edt_fname, "");
                    } else {
                        GlobalClass.SetEditText(edt_fname, enteredString.substring(1));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });


        edt_lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        GlobalClass.SetEditText(edt_lname, "");
                    } else {
                        GlobalClass.SetEditText(edt_lname, enteredString.substring(1));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_missed_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                ) {
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_mob_num, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_missed_mobile, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_missed_mobile, "");
                    }
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String checkNumber = s.toString();
                if (checkNumber.length() == 10) {
                    buttonval();
                }

            }
        });

        edt_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonval();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void displayrate() {

        try {
            if (ControllersGlobalInitialiser.covidRates_controller != null) {
                ControllersGlobalInitialiser.covidRates_controller = null;
            }
            ControllersGlobalInitialiser.covidRates_controller = new CovidRates_Controller(mActivity, Covidenter_Frag.this);
            ControllersGlobalInitialiser.covidRates_controller.getCovidRatescontroller();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView(View view) {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", null);
        apikey = preferences.getString("API_KEY", null);

        edt_fname = view.findViewById(R.id.edt_firstname);
        edt_amt = view.findViewById(R.id.edt_amt);
        edt_lname = view.findViewById(R.id.edt_lastname);
        edt_missed_mobile = view.findViewById(R.id.edt_missed_mobile);
        edt_verifycc = view.findViewById(R.id.edt_verifycc);

        edt_fname.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxLength = 16;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edt_fname.setFilters(FilterArray);

        int maxLength1 = 16;
        edt_lname.setFilters(new InputFilter[]{EMOJI_FILTER});
        FilterArray[0] = new InputFilter.LengthFilter(maxLength1);
        edt_lname.setFilters(FilterArray);


        //TODO Buttons
        btn_choosefile_presc = view.findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = view.findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = view.findViewById(R.id.btn_choosefile_trf);
        btn_choosefile_vial = view.findViewById(R.id.btn_choosefile_vial);
        btn_choosefile_other = view.findViewById(R.id.btn_choosefile_other);
        txt_selfiefileupload = view.findViewById(R.id.txt_selfiefileupload);

        btn_generate = view.findViewById(R.id.btn_generate);
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_resend = view.findViewById(R.id.btn_resend);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_submit = view.findViewById(R.id.btn_submit);
        txt_selfie = view.findViewById(R.id.txt_selfie);
        lin_selfie = view.findViewById(R.id.lin_selfie);

        GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));


        rel_verify_mobile = view.findViewById(R.id.rel_verify_mobile);
        tv_verifiedmob = view.findViewById(R.id.tv_verifiedmob);

        //TODO LinearLaypouts
        lin_by_missed = view.findViewById(R.id.lin_missed_verify);
        lin_generate_verify = view.findViewById(R.id.lin_generate_verify);
        lin_pres_preview = view.findViewById(R.id.lin_pres_preview);
        lin_adhar_images = view.findViewById(R.id.lin_adhar_images);
        lin_trf_images = view.findViewById(R.id.lin_trf_images);
        lin_vial_images = view.findViewById(R.id.lin_vial_images);
        lin_other_images = view.findViewById(R.id.lin_other_images);
        mainlinear = view.findViewById(R.id.mainlinear);

        //TODO Relativelayout
        rel_mobno = view.findViewById(R.id.rel_mobno);


        //TODO Textviews
        txt_presfileupload = view.findViewById(R.id.txt_presfileupload);
        txt_adharfileupload = view.findViewById(R.id.txt_adharfileupload);
        txt_trffileupload = view.findViewById(R.id.txt_trffileupload);
        txt_vialrfileupload = view.findViewById(R.id.txt_vialrfileupload);
        txt_otherfileupload = view.findViewById(R.id.txt_otherfileupload);

        txt_nofilepresc = view.findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = view.findViewById(R.id.txt_nofileadhar);
        txt_nofiletrf = view.findViewById(R.id.txt_nofiletrf);
        txt_nofilevial = view.findViewById(R.id.txt_nofilevial);
        txt_nofileother = view.findViewById(R.id.txt_nofileother);
        btn_selfie = view.findViewById(R.id.btn_selfie);
        tv_timer = view.findViewById(R.id.tv_timer);
        tv_resetno = view.findViewById(R.id.tv_resetno);
        tv_mobileno = view.findViewById(R.id.tv_mobileno);
        by_missed = (RadioButton) view.findViewById(R.id.by_missed);
        by_generate = (RadioButton) view.findViewById(R.id.by_generate);

        by_missed.setOnClickListener(this);
        by_generate.setOnClickListener(this);

        btn_choosefile_presc.setOnClickListener(this);
        btn_choosefile_adhar.setOnClickListener(this);
        btn_choosefile_trf.setOnClickListener(this);
        btn_choosefile_vial.setOnClickListener(this);
        btn_choosefile_other.setOnClickListener(this);

        btn_resend.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        tv_resetno.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        GlobalClass.SetText(txt_nofilepresc, getResources().getString(R.string.nofilechoosen));
        GlobalClass.SetText(txt_nofileadhar, getResources().getString(R.string.nofilechoosen));
        GlobalClass.SetText(txt_nofiletrf, getResources().getString(R.string.nofilechoosen));
        GlobalClass.SetText(txt_nofilevial, getResources().getString(R.string.nofilechoosen));
        GlobalClass.SetText(txt_nofileother, getResources().getString(R.string.nofilechoosen));
        GlobalClass.SetText(txt_selfie, getResources().getString(R.string.nofilechoosen));


        txt_nofilepresc.setOnClickListener(this);
        txt_nofileadhar.setOnClickListener(this);
        txt_nofiletrf.setOnClickListener(this);
        txt_nofilevial.setOnClickListener(this);
        txt_nofileother.setOnClickListener(this);

        txt_presfileupload.setOnClickListener(this);
        txt_adharfileupload.setOnClickListener(this);
        txt_trffileupload.setOnClickListener(this);
        txt_vialrfileupload.setOnClickListener(this);
        txt_otherfileupload.setOnClickListener(this);


    }

    private void buttonval() {
        if (Validation()) {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.covidbtn));
            btn_submit.setTextColor(getResources().getColor(R.color.maroon));
            btn_submit.setEnabled(true);
            btn_submit.setClickable(true);
        } else {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
            btn_submit.setTextColor(getResources().getColor(R.color.black));
            btn_submit.setEnabled(false);
            btn_submit.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.mainlinear:
                GlobalClass.Hidekeyboard(view);
                break;

            case R.id.btn_reset:
                clearonreset();
                break;
            case R.id.btn_submit:
                if (Validation()) {
                    if (amtvalidation()) {
                        if (!GlobalClass.isNetworkAvailable(getActivity())) {
                            GlobalClass.showAlertDialog(getActivity());
                        } else {

                            try {
                                if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                                    ControllersGlobalInitialiser.covidmultipart_controller = null;
                                }
                                String fname = edt_fname.getText().toString().trim();
                                String lname = edt_lname.getText().toString().trim();

                                String fullname = fname + " " + lname;
                                Log.e("NAME LENGHTH---->", "" + fullname.length());

                                Covidpostdata covidpostdata = new Covidpostdata();
                                covidpostdata.setSOURCECODE(usercode);
                                covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                                covidpostdata.setNAME(fullname);
                                covidpostdata.setAMOUNTCOLLECTED(edt_amt.getText().toString().trim());
                                covidpostdata.setTESTCODE("COVID-19");
                                covidpostdata.setVIAIMAGE(vial_file);
                                covidpostdata.setSELFIE(selfie_file);

                                if (presc_file != null) {
                                    covidpostdata.setPRESCRIPTION(presc_file);
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
                                new Covidmultipart_controller(Covidenter_Frag.this, activity, covidpostdata).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                break;

            case R.id.btn_verify:
                if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                    if (!GlobalClass.isNull(edt_verifycc.getText().toString())) {
                        validateotp();

                    } else {
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_otp, 2);
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.MOBNO, 2);
                }

                break;

            case R.id.btn_generate:
                genrateflow();
                break;

            case R.id.by_missed:
                GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));
                clearfields("1");
                break;

            case R.id.btn_resend:
                genrateflow();
                break;

            case R.id.by_generate:
                GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
                clearfields("2");
                break;

            case R.id.tv_resetno:
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

                break;

            case R.id.btn_choosefile_presc:
                if (verifyotp) {
                    if (checkPermission()) {

                        if (presc_file != null) {
                            GlobalClass.showTastyToast(activity, MessageConstants.UPLOAD_ONEFILE, 2);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            checkpremissionproceed();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }
                break;

            case R.id.btn_choosefile_adhar:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showTastyToast(activity, MessageConstants.UPLOADTWOFILES, 2);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            checkpremissionproceed();
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }

                break;
            case R.id.btn_choosefile_trf:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showTastyToast(activity, MessageConstants.UPLOADTWOFILES, 2);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            istrf = true;
                            checkpremissionproceed();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }
                break;

            case R.id.btn_choosefile_vial:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showTastyToast(activity, MessageConstants.UPLOAD_ONEFILE, 2);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
                            checkpremissionproceed();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }
                break;

            case R.id.btn_choosefile_other:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showTastyToast(activity, MessageConstants.UPLOADTWOFILES, 2);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = true;
                            checkpremissionproceed();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showTastyToast(activity, MessageConstants.VERIFY, 2);
                }
                break;


            case R.id.txt_presfileupload:
                if (GlobalClass.CheckArrayList(presclist)) {
                    setviewpager(presclist, "pres");
                }
                break;

            case R.id.txt_adharfileupload:
                if (GlobalClass.CheckArrayList(aadharlist)) {
                    setviewpager(aadharlist, "adhar");
                }
                break;

            case R.id.txt_trffileupload:
                if (GlobalClass.CheckArrayList(trflist)) {
                    setviewpager(trflist, "trf");
                }
                break;

            case R.id.txt_vialrfileupload:
                if (GlobalClass.CheckArrayList(viallist)) {
                    setviewpager(viallist, "vial");
                }
                break;

            case R.id.txt_otherfileupload:
                if (GlobalClass.CheckArrayList(otherlist)) {
                    setviewpager(otherlist, "other");
                }
                break;


        }
    }

    private void clearonreset() {
        rel_verify_mobile.setVisibility(View.GONE);
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_amt.getText().clear();
        edt_missed_mobile.getText().clear();
        edt_verifycc.getText().clear();
        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setClickable(true);
        btn_generate.setVisibility(View.VISIBLE);
        btn_generate.setEnabled(true);
        btn_generate.setClickable(true);
        btn_verify.setClickable(true);
        btn_verify.setEnabled(true);
        btn_resend.setEnabled(true);
        btn_resend.setClickable(true);
        btn_selfie.setEnabled(true);
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        lin_by_missed.setVisibility(View.VISIBLE);

        if (by_missed.isChecked()) {
            GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));
        } else {
            GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.btngenerateccc));
        }

        timerflag = false;
        verifyotp = false;
        presc_file = null;
        aadhar_file = null;
        aadhar_file1 = null;

        trf_file = null;
        trf_file1 = null;

        vial_file = null;
        other_file = null;
        other_file1 = null;
        selfie_file = null;
        selfie_flag = 0;

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        lin_selfie.setVisibility(View.GONE);


        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);
        txt_selfie.setVisibility(View.VISIBLE);

        GlobalClass.SetText(txt_nofilepresc, getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        GlobalClass.SetText(txt_nofileadhar, getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        GlobalClass.SetText(txt_selfie, getResources().getString(R.string.nofilechoosen));
        txt_selfie.setTextColor(getResources().getColor(R.color.black));
        txt_selfie.setPaintFlags(0);

        GlobalClass.SetText(txt_nofiletrf, getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        GlobalClass.SetText(txt_nofilevial, getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        GlobalClass.SetText(txt_nofileother, getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);


        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));

        btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_selfie.setTextColor(getResources().getColor(R.color.maroon));

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
        aadharlist.clear();
        trflist.clear();
        viallist.clear();
        otherlist.clear();
        selfielist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
    }

    private boolean amtvalidation() {
        if (TextUtils.isEmpty(edt_amt.getText().toString())) {
            return false;
        }

        if (TextUtils.isEmpty(edt_amt.getText().toString())) {
            return false;
        }

        try {
            if (!GlobalClass.isNull(b2c) && Integer.parseInt(edt_amt.getText().toString()) > Integer.parseInt(b2c)) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Amt_collected_more + b2c, 2);
                return false;
            } else if (!GlobalClass.isNull(b2b) && Integer.parseInt(edt_amt.getText().toString()) < Integer.parseInt(b2b)) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Amt_collected_less + b2b, 2);
                return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void mobileverify(String mobileno) {
        try {
            if (ControllersGlobalInitialiser.covidMobverification_controller != null) {
                ControllersGlobalInitialiser.covidMobverification_controller = null;
            }
            ControllersGlobalInitialiser.covidMobverification_controller = new CovidMobverification_Controller(activity, Covidenter_Frag.this);
            ControllersGlobalInitialiser.covidMobverification_controller.getcovidmobverifycontroller(apikey, mobileno, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void checkpremissionproceed() {
        if (AccessRuntimePermissions.checkcameraPermission(getActivity())) {
            selectImage();
        } else {
            AccessRuntimePermissions.requestCamerapermission(getActivity());
        }
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

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {

                if (ispresciption) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    presc_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + presc_file;
                    presc_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), presc_file);
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_presfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    GlobalClass.SetText(txt_presfileupload, "1 " + getResources().getString(R.string.imgupload));
                    txt_presfileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilepresc.setVisibility(View.GONE);
                    presclist = new ArrayList<>();
                    presclist.clear();
                    presclist.add(destFile);

                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }

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
                        GlobalClass.SetText(txt_adharfileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_adharfileupload, "1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(imageurl);
                    }

                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(txt_adharfileupload, "2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                    lin_adhar_images.setVisibility(View.VISIBLE);

                } else if (istrf) {

                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file1;
                        trf_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file1);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(txt_trffileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_trffileupload, "1 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(imageurl);
                    }

                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(txt_trffileupload, "2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    vial_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + vial_file;
                    vial_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_vialrfileupload, "1 " + getResources().getString(R.string.imgupload));
                    txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilevial.setVisibility(View.GONE);
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                    }
                    viallist.add(imageurl);
                    buttonval();

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
                        GlobalClass.SetText(txt_otherfileupload, "2 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_otherfileupload, "1 " + getResources().getString(R.string.imgupload));
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
                        GlobalClass.SetText(txt_otherfileupload, "2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        buttonval();
                    }
                    lin_other_images.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Failedto_load_image, 2);
                return;
            }

            try {
                if (ispresciption) {
                    if (data.getData() != null) {
                        presc_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    presc_file = GlobalClass.getCompressedFile(activity, presc_file);
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_presfileupload.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_presfileupload, "1 " + getResources().getString(R.string.imgupload));
                    txt_presfileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilepresc.setVisibility(View.GONE);
                    presclist = new ArrayList<>();
                    presclist.clear();
                    presclist.add(presc_file.toString());
                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                } else if (isadhar) {
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
                        buttonval();
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
                        buttonval();
                    }

                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    if (data.getData() != null) {
                        vial_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    vial_file = GlobalClass.getCompressedFile(activity, vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_vialrfileupload, "1 " + getResources().getString(R.string.imgupload));
                    txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilevial.setVisibility(View.GONE);
                    viallist.add(vial_file.toString());
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
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
                        buttonval();
                    }

                    lin_other_images.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Failed_to_read_img, 2);
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getActivity(), photo);
            selfie_file = new File(getRealPathFromURI(tempUri));
            txt_selfie.setVisibility(View.GONE);
            lin_selfie.setVisibility(View.VISIBLE);
            selfie_flag = 1;
            GlobalClass.SetText(txt_selfiefileupload, "1 " + getResources().getString(R.string.imgupload));
            txt_selfiefileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
            btn_selfie.setTextColor(getResources().getColor(R.color.black));
            btn_selfie.setEnabled(false);

            selfielist.add(selfie_file.toString());


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private boolean Validation() {
        if (edt_fname.getText().toString().length() == 0) {
            return false;
        }

        if (edt_fname.getText().toString().length() < 2) {
            return false;
        }

        if (edt_lname.getText().toString().length() == 0) {
            return false;
        }
        if (edt_lname.getText().toString().length() < 1) {
            return false;
        }

        if (edt_missed_mobile.getText().toString().length() == 0) {
            return false;
        }

        if (edt_missed_mobile.getText().toString().length() < 10) {
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            return false;
        }


        if (TextUtils.isEmpty(edt_amt.getText().toString())) {
            return false;
        }


        if (aadhar_file == null && aadhar_file1 == null) {
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            return false;
        }

        if (vial_file == null) {
            return false;
        }

        return true;
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

    private void clearfields(String flag) {
        rel_verify_mobile.setVisibility(View.GONE);
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        if (flag.equalsIgnoreCase("3")) {
            edt_fname.getText().clear();
            edt_lname.getText().clear();
            edt_missed_mobile.getText().clear();
        }

        edt_verifycc.getText().clear();
        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setClickable(true);
        btn_generate.setVisibility(View.VISIBLE);
        btn_generate.setEnabled(true);
        btn_generate.setClickable(true);
        btn_verify.setClickable(true);
        btn_verify.setEnabled(true);
        btn_resend.setEnabled(true);
        btn_resend.setClickable(true);
        btn_selfie.setEnabled(true);
        btn_resend.setVisibility(View.VISIBLE);
        lin_by_missed.setVisibility(View.VISIBLE);
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);

        if (by_missed.isChecked()) {
            GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.enterccc));
        } else {
            GlobalClass.SetButtonText(btn_generate, getResources().getString(R.string.btngenerateccc));
        }

        timerflag = false;
        verifyotp = false;
        presc_file = null;
        aadhar_file = null;
        aadhar_file1 = null;
        vial_file = null;
        other_file = null;
        other_file1 = null;
        trf_file = null;
        selfie_file = null;
        trf_file1 = null;
        selfie_flag = 0;

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        lin_selfie.setVisibility(View.GONE);


        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);
        txt_selfie.setVisibility(View.VISIBLE);

        GlobalClass.SetText(txt_nofilepresc, getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);


        GlobalClass.SetText(txt_nofileadhar, getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        GlobalClass.SetText(txt_nofiletrf, getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        GlobalClass.SetText(txt_nofilevial, getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        GlobalClass.SetText(txt_nofileother, getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);

        GlobalClass.SetText(txt_selfie, getResources().getString(R.string.nofilechoosen));
        txt_selfie.setTextColor(getResources().getColor(R.color.black));
        txt_selfie.setPaintFlags(0);

        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));

        btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_selfie.setTextColor(getResources().getColor(R.color.maroon));


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
        aadharlist.clear();
        trflist.clear();
        viallist.clear();
        otherlist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        GlobalClass.SetText(tv_verifiedmob, edt_missed_mobile.getText().toString());
        lin_by_missed.setVisibility(View.GONE);

        tv_mobileno.setVisibility(View.GONE);

        edt_missed_mobile.setEnabled(false);
        btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);
        btn_resend.setVisibility(View.GONE);

        edt_missed_mobile.setClickable(false);
        btn_generate.setClickable(false);
        btn_generate.setVisibility(View.GONE);
        btn_resend.setClickable(false);

        edt_missed_mobile.setClickable(false);
        btn_generate.setClickable(false);
        btn_resend.setClickable(false);

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        timerflag = true;
        verifyotp = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }


    private void generateOtP(String mobileno) {

        try {
            if (ControllersGlobalInitialiser.generateOTP_controller != null) {
                ControllersGlobalInitialiser.generateOTP_controller = null;
            }
            ControllersGlobalInitialiser.generateOTP_controller = new GenerateOTP_Controller(activity, Covidenter_Frag.this);
            ControllersGlobalInitialiser.generateOTP_controller.generteotpController(apikey, mobileno, usercode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateotp() {
        try {
            if (ControllersGlobalInitialiser.validateOTP_controller != null) {
                ControllersGlobalInitialiser.validateOTP_controller = null;
            }
            ControllersGlobalInitialiser.validateOTP_controller = new ValidateOTP_Controller(activity, Covidenter_Frag.this);
            ControllersGlobalInitialiser.validateOTP_controller.getvalidateotp_Controller(apikey,edt_missed_mobile,edt_verifycc,usercode);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                GlobalClass.showTastyToast(activity, RESPONSE, 1);

                Intent i = new Intent(getActivity(), ManagingTabsActivity.class);
                startActivity(i);
                Constants.covidfrag_flag = "1";

                clearfields("3");


            } else {
                GlobalClass.showTastyToast(activity, RESPONSE, 2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

                        if (type.equalsIgnoreCase("pres")) {
                            presc_file = null;
                            presclist.clear();
                            txt_nofilepresc.setVisibility(View.VISIBLE);
                            GlobalClass.SetText(txt_nofilepresc, getResources().getString(R.string.nofilechoosen));

                            lin_pres_preview.setVisibility(View.GONE);
                            btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_choosefile_presc.setTextColor(getResources().getColor(R.color.maroon));
                            txt_nofilepresc.setVisibility(View.VISIBLE);
                            buttonval();
                        } else if (type.equalsIgnoreCase("adhar")) {

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

                                buttonval();

                            } else {
                                txt_nofileadhar.setVisibility(View.VISIBLE);
                                lin_adhar_images.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("trf")) {
                            if (GlobalClass.CheckArrayList(trflist)) {
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
                            buttonval();
                        } else if (type.equalsIgnoreCase("vial")) {
                            vial_file = null;
                            viallist.clear();
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            GlobalClass.SetText(txt_nofilevial, getResources().getString(R.string.nofilechoosen));
                            lin_vial_images.setVisibility(View.GONE);
                            btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            buttonval();
                        } else if (type.equalsIgnoreCase("other")) {
                            if (GlobalClass.CheckArrayList(otherlist)) {
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
                            buttonval();
                        } else if (type.equalsIgnoreCase("selfie")) {

                            selfie_file = null;
                            selfie_flag = 0;
                            selfielist.clear();
                            lin_selfie.setVisibility(View.GONE);
                            txt_selfie.setVisibility(View.VISIBLE);
                            btn_selfie.setEnabled(true);
                            btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_selfie.setTextColor(getResources().getColor(R.color.maroon));

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

    public void getcovidratesResponse(Response<Covidratemodel> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                if (!GlobalClass.isNull(response.body().getB2B())) {
                    b2b = response.body().getB2B();
                    Global.B2B = b2b;
                }

                if (!GlobalClass.isNull(response.body().getB2C())) {
                    b2c = response.body().getB2C();
                    Global.B2C = b2c;
                }

            } else {
                GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
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
                GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
            } else {
                if (btn_generate.getText().toString().equalsIgnoreCase(MessageConstants.Generate_CCC)) {
                    generateOtP(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
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
                GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 1);
            } else {
                GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getvalidateOTPResponse(Response<Covid_validateotp_res> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                disablefields();
            } else {
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
