package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.e5322.thyrosoft.Activity.CovidReg_Activity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class Covidenter_Frag extends Fragment implements View.OnClickListener {
    Activity activity;
    ConnectionDetector cd;
    Drawable imgotp;
    CountDownTimer countDownTimer;
    public RadioButton by_missed, by_generate;
    LinearLayout lin_by_missed, lin_generate_verify, lin_pres_preview, lin_adhar_images, lin_trf_images;
    RelativeLayout rel_adhar1, rel_adhar2, rel_trf1, rel_trf2, rel_mobno;
    Button btn_choosefile_presc, btn_choosefile_adhar, btn_choosefile_trf;
    Button btn_generate, btn_submit, btn_verify, btn_resend, btn_reset;
    TextView txt_nofilepresc, txt_nofileadhar, txt_nofiletrf, tv_timer, tv_resetno, tv_mobileno;
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
    boolean ispresciption, isadhar, istrf;
    EditText edt_fname, edt_lname, edt_missed_mobile, edt_verifycc;
    SharedPreferences preferences;
    String usercode, apikey;
    boolean verifyotp = false;
    ImageView img_pres_close, img_pres_preview, img_close_adhar1, img_close_adhar2, img_adhar_preview1, img_adhar_preview2, img_close_trf1, img_trf_preview1, img_close_trf2, img_trf_preview2;

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
    }

    private void initView(View view) {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", null);
        apikey = preferences.getString("API_KEY", null);

        edt_fname = view.findViewById(R.id.edt_firstname);
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
        btn_generate = view.findViewById(R.id.btn_generate);
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_resend = view.findViewById(R.id.btn_resend);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_generate.setText(getResources().getString(R.string.enterccc));

        //TODO IMAGEVIEW

        img_pres_close = view.findViewById(R.id.img_pres_close);
        img_pres_preview = view.findViewById(R.id.img_pres_preview);

        img_close_adhar1 = view.findViewById(R.id.img_close_adhar1);
        img_close_adhar2 = view.findViewById(R.id.img_close_adhar2);

        img_close_trf1 = view.findViewById(R.id.img_close_trf1);
        img_close_trf2 = view.findViewById(R.id.img_close_trf2);

        img_adhar_preview1 = view.findViewById(R.id.img_adhar_preview1);
        img_adhar_preview2 = view.findViewById(R.id.img_adhar_preview2);

        img_trf_preview1 = view.findViewById(R.id.img_trf_preview1);
        img_trf_preview2 = view.findViewById(R.id.img_trf_preview2);


        //TODO LinearLaypouts
        lin_by_missed = view.findViewById(R.id.lin_missed_verify);
        lin_generate_verify = view.findViewById(R.id.lin_generate_verify);
        lin_pres_preview = view.findViewById(R.id.lin_pres_preview);
        lin_adhar_images = view.findViewById(R.id.lin_adhar_images);
        lin_trf_images = view.findViewById(R.id.lin_trf_images);

        //TODO Relativelayout
        rel_mobno = view.findViewById(R.id.rel_mobno);

        rel_adhar1 = view.findViewById(R.id.rel_adhar1);
        rel_adhar2 = view.findViewById(R.id.rel_adhar2);

        rel_trf1 = view.findViewById(R.id.rel_trf1);
        rel_trf2 = view.findViewById(R.id.rel_trf2);

        //TODO Textviews
        txt_nofilepresc = view.findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = view.findViewById(R.id.txt_nofileadhar);
        txt_nofiletrf = view.findViewById(R.id.txt_nofiletrf);
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
        btn_resend.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        tv_resetno.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));

        txt_nofilepresc.setOnClickListener(this);
        txt_nofileadhar.setOnClickListener(this);
        txt_nofiletrf.setOnClickListener(this);

        btn_generate.setOnClickListener(this);

        img_pres_preview.setOnClickListener(this);
        img_pres_close.setOnClickListener(this);

        img_close_adhar1.setOnClickListener(this);
        img_close_adhar2.setOnClickListener(this);

        img_adhar_preview1.setOnClickListener(this);
        img_adhar_preview2.setOnClickListener(this);

        img_close_trf1.setOnClickListener(this);
        img_close_trf2.setOnClickListener(this);

        img_trf_preview1.setOnClickListener(this);
        img_trf_preview2.setOnClickListener(this);


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
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_reset:
                clearonreset();
                break;
            case R.id.btn_submit:
                if (Validation()) {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        GlobalClass.showAlertDialog(getActivity());
                    } else {
                        if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                            ControllersGlobalInitialiser.covidmultipart_controller = null;
                        }
                        String fullname = edt_fname.getText().toString().trim() + " " + edt_lname.getText().toString().trim();
                        Log.e("NAME LENGHTH---->", "" + fullname.length());
                        Covidpostdata covidpostdata = new Covidpostdata();
                        covidpostdata.setSOURCECODE(usercode);
                        covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                        covidpostdata.setNAME(fullname);
                        covidpostdata.setPRESCRIPTION(presc_file);

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
                        new Covidmultipart_controller(Covidenter_Frag.this, activity, covidpostdata).execute();

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
                clearfields();
                break;

            case R.id.btn_resend:
                genrateflow();
                break;

            case R.id.by_generate:
                btn_generate.setText(getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
                clearfields();
                break;

            case R.id.tv_resetno:
                lin_by_missed.setVisibility(View.VISIBLE);
                lin_generate_verify.setVisibility(View.GONE);
                edt_missed_mobile.setEnabled(true);
                edt_missed_mobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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


            case R.id.img_pres_close:
                presc_file = null;
                txt_nofilepresc.setVisibility(View.VISIBLE);
                txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
                lin_pres_preview.setVisibility(View.GONE);
                break;

            case R.id.img_pres_preview:
                GlobalClass.showImageDialog(activity, presc_file, "", 1);
                break;

            case R.id.img_close_adhar1:
                aadhar_file = null;
                rel_adhar1.setVisibility(View.GONE);
                if (rel_adhar2.getVisibility() == View.VISIBLE) {
                    txt_nofileadhar.setVisibility(View.GONE);
                } else {
                    txt_nofileadhar.setVisibility(View.VISIBLE);
                    lin_adhar_images.setVisibility(View.GONE);
                }
                break;

            case R.id.img_close_adhar2:
                aadhar_file1 = null;
                rel_adhar2.setVisibility(View.GONE);
                if (rel_adhar1.getVisibility() == View.VISIBLE) {
                    txt_nofileadhar.setVisibility(View.GONE);
                } else {
                    txt_nofileadhar.setVisibility(View.VISIBLE);
                    lin_adhar_images.setVisibility(View.GONE);
                }
                break;

            case R.id.img_adhar_preview1:
                GlobalClass.showImageDialog(activity, aadhar_file, "", 1);
                break;
            case R.id.img_adhar_preview2:
                GlobalClass.showImageDialog(activity, aadhar_file1, "", 1);
                break;

            case R.id.img_close_trf1:
                trf_file = null;
                rel_trf1.setVisibility(View.GONE);
                if (rel_trf2.getVisibility() == View.VISIBLE) {
                    txt_nofiletrf.setVisibility(View.GONE);
                } else {
                    txt_nofiletrf.setVisibility(View.VISIBLE);
                    lin_trf_images.setVisibility(View.GONE);
                }
                break;

            case R.id.img_close_trf2:
                trf_file1 = null;
                rel_trf2.setVisibility(View.GONE);
                if (rel_trf1.getVisibility() == View.VISIBLE) {
                    txt_nofiletrf.setVisibility(View.GONE);
                } else {
                    txt_nofiletrf.setVisibility(View.VISIBLE);
                    lin_trf_images.setVisibility(View.GONE);
                }
                break;

            case R.id.img_trf_preview1:
                GlobalClass.showImageDialog(activity, trf_file, "", 1);
                break;
            case R.id.img_trf_preview2:
                GlobalClass.showImageDialog(activity, trf_file1, "", 1);
                break;

        }
    }

    private void clearonreset() {

        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_missed_mobile.getText().clear();
        edt_verifycc.getText().clear();
        edt_missed_mobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);

        rel_adhar1.setVisibility(View.GONE);
        rel_adhar2.setVisibility(View.GONE);

        rel_trf1.setVisibility(View.GONE);
        rel_trf2.setVisibility(View.GONE);

        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
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
                        disablefields();
                    } else {
                        if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                            generateOtP(edt_missed_mobile.getText().toString());
                        } else {
                            lin_by_missed.setVisibility(View.GONE);
                            tv_mobileno.setVisibility(View.VISIBLE);
                            tv_mobileno.setText(edt_missed_mobile.getText().toString());
                            rel_mobno.setVisibility(View.VISIBLE);
                            lin_generate_verify.setVisibility(View.VISIBLE);
                            tv_resetno.setVisibility(View.VISIBLE);
                            btn_resend.setVisibility(View.GONE);
                            tv_resetno.setText(getResources().getString(R.string.reset_mob));
                            tv_resetno.setPaintFlags(tv_resetno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                    txt_nofilepresc.setVisibility(View.GONE);
                    if (presc_file != null) {
                        ispresciption = false;
                    }

                } else if (isadhar) {
                    if (rel_adhar1.getVisibility() == View.VISIBLE) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        aadhar_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file1;
                        aadhar_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), aadhar_file1);
                        rel_adhar2.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        aadhar_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file;
                        aadhar_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), aadhar_file);
                        rel_adhar1.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    }

                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                    }
                    lin_adhar_images.setVisibility(View.VISIBLE);

                } else {

                    if (rel_trf1.getVisibility() == View.VISIBLE) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file1;
                        trf_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file1);
                        rel_trf2.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file;
                        trf_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file);
                        rel_trf1.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    }

                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                    }
                    lin_trf_images.setVisibility(View.VISIBLE);
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
                    txt_nofilepresc.setVisibility(View.GONE);
                    if (presc_file != null) {
                        ispresciption = false;
                    }
                } else if (isadhar) {
                    if (rel_adhar1.getVisibility() == View.VISIBLE) {
                        if (data.getData() != null) {
                            if (aadhar_file1 == null) {
                                aadhar_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                        rel_adhar2.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    } else {
                        if (data.getData() != null) {
                            if (aadhar_file == null) {
                                aadhar_file = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                        rel_adhar1.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    }

                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                    }

                    lin_adhar_images.setVisibility(View.VISIBLE);


                } else if (istrf) {
                    if (rel_trf1.getVisibility() == View.VISIBLE) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file1 = GlobalClass.getCompressedFile(activity, trf_file1);
                        rel_trf2.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);

                    } else {
                        if (data.getData() != null) {
                            trf_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                        rel_trf1.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    }

                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                    }

                    lin_trf_images.setVisibility(View.VISIBLE);
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
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_fname.requestFocus();
            return false;
        }
        if (edt_fname.getText().toString().length() < 2) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_fname.requestFocus();
            return false;
        }

        if (edt_lname.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lname.requestFocus();
            return false;
        }
        if (edt_lname.getText().toString().length() < 1) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lname.requestFocus();
            return false;
        }

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

        if (presc_file == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_PIMAGE);
            return false;
        }

        if (aadhar_file == null && aadhar_file1 == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE);
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

    private void clearfields() {
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_missed_mobile.getText().clear();
        edt_verifycc.getText().clear();
        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setClickable(true);
        edt_missed_mobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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

        trf_file = null;
        trf_file1 = null;

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);

        rel_adhar1.setVisibility(View.GONE);
        rel_adhar2.setVisibility(View.GONE);

        rel_trf1.setVisibility(View.GONE);
        rel_trf2.setVisibility(View.GONE);

        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    private void disablefields() {

        imgotp = getContext().getResources().getDrawable(R.drawable.otpverify);
        imgotp.setBounds(40, 40, 40, 40);

        edt_missed_mobile.setCompoundDrawablesWithIntrinsicBounds(null, null, imgotp, null);
        edt_missed_mobile.setEnabled(false);
        btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);
        btn_resend.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        edt_missed_mobile.setClickable(false);
        btn_generate.setClickable(false);
        btn_generate.setVisibility(View.GONE);
        btn_resend.setClickable(false);
        lin_by_missed.setVisibility(View.VISIBLE);


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
                Intent intent = new Intent(activity, CovidReg_Activity.class);
                activity.startActivity(intent);
                activity.finish();
                clearfields();
            } else {
                //showOKDialog(RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
