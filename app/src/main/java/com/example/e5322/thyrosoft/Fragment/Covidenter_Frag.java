package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.R;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class Covidenter_Frag extends Fragment implements View.OnClickListener {
    Activity activity;
    ConnectionDetector cd;
    LinearLayout mainlinear;
    //  Drawable imgotp;
    String b2b, b2c;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    PageIndicatorView pageIndicatorView;
    List<String> presclist = new ArrayList<>();
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> viallist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    CountDownTimer countDownTimer;
    public RadioButton by_missed, by_generate;
    LinearLayout lin_by_missed, lin_generate_verify, lin_pres_preview, lin_adhar_images, lin_trf_images, lin_vial_images, lin_other_images;
    RelativeLayout rel_mobno;
    Button btn_choosefile_presc, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_vial, btn_choosefile_other;
    Button btn_generate, btn_submit, btn_verify, btn_resend, btn_reset;
    TextView txt_nofilepresc, txt_nofileadhar, txt_nofiletrf, txt_nofilevial, txt_nofileother, tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob;
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

    TextView txt_presfileupload, txt_adharfileupload, txt_trffileupload, txt_vialrfileupload, txt_otherfileupload;

    boolean ispresciption, isadhar, istrf, isvial, isother;
    EditText edt_fname, edt_amt, edt_lname, edt_missed_mobile, edt_verifycc;
    SharedPreferences preferences;
    String usercode, apikey;
    boolean verifyotp = false;
    RelativeLayout rel_verify_mobile;


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
        if (cd.isConnectingToInternet()) {
            displayrate();
        }

    }


    private void displayrate() {
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);

        Call<Covidratemodel> covidratemodelCall = postAPIInteface.displayrates();
        covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
            @Override
            public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        b2b = response.body().getB2B();
                        b2c = response.body().getB2C();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidratemodel> call, Throwable t) {

            }
        });

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


        edt_amt.setFilters(new InputFilter[]{EMOJI_FILTER});

        //TODO Buttons
        btn_choosefile_presc = view.findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = view.findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = view.findViewById(R.id.btn_choosefile_trf);
        btn_choosefile_vial = view.findViewById(R.id.btn_choosefile_vial);
        btn_choosefile_other = view.findViewById(R.id.btn_choosefile_other);

        btn_generate = view.findViewById(R.id.btn_generate);
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_resend = view.findViewById(R.id.btn_resend);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_generate.setText(getResources().getString(R.string.enterccc));


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

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));

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
                        edt_fname.setText("");
                    } else {
                        edt_fname.setText(enteredString.substring(1));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_fname.getText().toString().length() == 0) {
                    Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
                    edt_fname.requestFocus();
                }

                if (edt_fname.getText().toString().length() < 2) {
                    Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
                    edt_fname.requestFocus();
                }

                if (Validation()) {
                    btn_submit.setBackground(getResources().getDrawable(R.color.maroon));
                    btn_submit.setTextColor(getResources().getColor(R.color.white));
                    btn_submit.setEnabled(true);
                    btn_submit.setClickable(true);
                } else {
                    btn_submit.setBackground(getResources().getDrawable(R.color.flouride));
                    btn_submit.setTextColor(getResources().getColor(R.color.black));
                    btn_submit.setEnabled(false);
                    btn_submit.setClickable(false);
                }
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
                        edt_lname.setText("");
                    } else {
                        edt_lname.setText(enteredString.substring(1));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_lname.getText().toString().length() == 0) {
                    Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
                    edt_lname.requestFocus();
                }
                if (edt_lname.getText().toString().length() < 1) {
                    Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
                    edt_lname.requestFocus();
                }
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
                    TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        edt_missed_mobile.setText(enteredString.substring(1));
                    } else {
                        edt_missed_mobile.setText("");
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

                try {
                    int amt = Integer.parseInt(charSequence.toString());

                    if (amt > Integer.parseInt(b2c)) {
                        edt_amt.setText("");
                       // TastyToast.makeText(getActivity(), "Please enter correct collected amount", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                         TastyToast.makeText(getActivity(), "Amount collected should be less than " + b2c, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }


                    if (amt < Integer.parseInt(b2b)) {
                        if (charSequence.length() > 3) {
                            edt_amt.setText("");
                            TastyToast.makeText(getActivity(), "Amount collected should be greater than " + b2b, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            //TastyToast.makeText(getActivity(), "Please enter correct collected amount", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                             TastyToast.makeText(getActivity(), "Amount collected should be greater than " + b2b, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    }
                    buttonval();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                // Get the input method manager
                GlobalClass.Hidekeyboard(view);
                break;

            case R.id.btn_reset:
                clearonreset();
                break;
            case R.id.btn_submit:
                if (Validation()) {
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
                            covidpostdata.setPRESCRIPTION(presc_file);
                            covidpostdata.setVIAIMAGE(vial_file);

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
                break;

            case R.id.btn_verify:
                if (!TextUtils.isEmpty(edt_missed_mobile.getText().toString())) {
                    if (!TextUtils.isEmpty(edt_verifycc.getText().toString())) {
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

                break;

            case R.id.btn_generate:
                genrateflow();
                break;

            case R.id.by_missed:
                btn_generate.setText(getResources().getString(R.string.enterccc));
                clearfields("1");
                break;

            case R.id.btn_resend:
                genrateflow();
                break;

            case R.id.by_generate:
                btn_generate.setText(getResources().getString(R.string.btngenerateccc));
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
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.btn_choosefile_adhar:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }

                break;
            case R.id.btn_choosefile_trf:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            istrf = true;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.btn_choosefile_vial:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.btn_choosefile_other:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = true;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;


            case R.id.txt_presfileupload:
                if (presclist != null && presclist.size() > 0) {
                    setviewpager(presclist, "pres");
                }
                break;

            case R.id.txt_adharfileupload:
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
                break;

            case R.id.txt_trffileupload:
                if (trflist != null && trflist.size() > 0) {
                    setviewpager(trflist, "trf");
                }
                break;

            case R.id.txt_vialrfileupload:
                if (viallist != null && viallist.size() > 0) {
                    setviewpager(viallist, "vial");
                }
                break;

            case R.id.txt_otherfileupload:
                if (otherlist != null && otherlist.size() > 0) {
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
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        lin_by_missed.setVisibility(View.VISIBLE);

        if (by_missed.isChecked()) {
            btn_generate.setText(getResources().getString(R.string.enterccc));
        } else {
            btn_generate.setText(getResources().getString(R.string.btngenerateccc));
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

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);


        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
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

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
        aadharlist.clear();
        trflist.clear();
        viallist.clear();
        otherlist.clear();

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
                        TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
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

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN);
                }
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
                    txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
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
                        buttonval();
                    }
                    lin_other_images.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(activity, "Failed to load image!", Toast.LENGTH_SHORT).show();
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
                    txt_presfileupload.setText("1 " + getResources().getString(R.string.imgupload));
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
                    txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
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
                        buttonval();
                    }

                    lin_other_images.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                Toast.makeText(activity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
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


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private boolean Validation() {
        if (edt_fname.getText().toString().length() == 0) {
            // Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            // edt_fname.requestFocus();
            return false;
        }

        if (edt_fname.getText().toString().length() < 2) {
            //  Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            // edt_fname.requestFocus();
            return false;
        }

        if (edt_lname.getText().toString().length() == 0) {
            // Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            //  edt_lname.requestFocus();
            return false;
        }
        if (edt_lname.getText().toString().length() < 1) {
            //  Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            //  edt_lname.requestFocus();
            return false;
        }

        if (edt_missed_mobile.getText().toString().length() == 0) {
            //   Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            // edt_missed_mobile.requestFocus();
            return false;
        }

        if (edt_missed_mobile.getText().toString().length() < 10) {
            //  Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            // edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            // Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            // edt_missed_mobile.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(edt_amt.getText().toString())) {
            //    Global.showCustomToast(getActivity(), ToastFile.AMTCOLL);
            //  edt_amt.requestFocus();
            return false;
        }

        if (Integer.parseInt(edt_amt.getText().toString()) < Integer.parseInt(b2b) || (Integer.parseInt(edt_amt.getText().toString()) > Integer.parseInt(b2c))) {
            //   Global.showCustomToast(getActivity(), "Amount collected should greater than "+b2b+" and less than "+b2c);
            // Global.showCustomToast(getActivity(), "please enter correct collected amount");

            //  edt_amt.requestFocus();
            return false;
        }

        if (presc_file == null) {
            //   Global.showCustomToast(getActivity(), ToastFile.SELECT_PIMAGE);
            return false;
        }

        if (aadhar_file == null && aadhar_file1 == null) {
            //     Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            //      Global.showCustomToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE);
            return false;
        }

        if (vial_file == null) {
            //    Global.showCustomToast(getActivity(), ToastFile.SELECT_VIALDHIMAGE);
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
        btn_resend.setVisibility(View.VISIBLE);
        lin_by_missed.setVisibility(View.VISIBLE);
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);

        if (by_missed.isChecked()) {
            btn_generate.setText(getResources().getString(R.string.enterccc));
        } else {
            btn_generate.setText(getResources().getString(R.string.btngenerateccc));
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
        trf_file1 = null;

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);


        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
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


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
        aadharlist.clear();
        trflist.clear();
        viallist.clear();
        otherlist.clear();
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        tv_verifiedmob.setText(edt_missed_mobile.getText().toString());
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

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);

                Intent i = new Intent(getActivity(), ManagingTabsActivity.class);
                startActivity(i);
                Constants.covidfrag_flag = "1";

                clearfields("3");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.imageslider_dialog);
        //maindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        Button btn_delete = maindialog.findViewById(R.id.btn_delete);
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
                            txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
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
                                buttonval();

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
                                    // viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    trf_file1 = null;
                                    if (trflist.size() == 2) {
                                        trflist.remove(trflist.get(1));
                                    } else {
                                        trflist.remove(trflist.get(0));
                                    }
                                    //viewPagerAdapter.notifyDataSetChanged();
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
                            buttonval();
                        } else if (type.equalsIgnoreCase("vial")) {
                            vial_file = null;
                            viallist.clear();
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
                            lin_vial_images.setVisibility(View.GONE);
                            btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            buttonval();
                        } else if (type.equalsIgnoreCase("other")) {
                            if (otherlist != null && otherlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (other_file != null) {
                                        other_file = null;
                                    } else {
                                        other_file1 = null;
                                    }

                                    otherlist.remove(otherlist.get(0));
                                    //   viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                    //  viewPagerAdapter.notifyDataSetChanged();
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
                            buttonval();
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

}
