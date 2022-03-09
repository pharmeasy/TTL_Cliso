package com.example.e5322.thyrosoft.Fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.SCollectionPAdapter;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.BuildConfig;
import com.example.e5322.thyrosoft.CommonItils.FilePath;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.GetTestCodeController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.getSCPSRFAPIController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.CovidRateReqModel;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.GetTestCodeResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.ResponseModels.getSCPTechnicianModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public RadioButton by_missed, by_generate, by_sendsms;
    LinearLayout lin_by_missed, lin_selfie, lin_generate_verify, lin_pres_preview, lin_adhar_images, lin_trf_images, lin_vial_images, lin_other_images;
    RelativeLayout rel_mobno;
    Button btn_choosefile_presc, btn_selfie, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_vial, btn_choosefile_other;
    Button btn_generate, btn_submit, btn_verify, btn_resend, btn_reset;
    TextView txt_barcode, txt_nofilepresc, txt_nofileadhar, txt_selfie, txt_nofiletrf, txt_nofilevial, txt_nofileother, tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob;
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    private String userChoosenTask;
    private Camera camera;
    Uri imageUri;
    ImageView img_scanbarcode;
    private boolean timerflag = false;
    String TAG = getClass().getSimpleName();
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
    EditText edt_fname, edt_amt, edt_lname, edt_missed_mobile, edt_verifycc, edt_srfid, edt_passport_number;
    SharedPreferences preferences;
    String usercode, apikey;
    boolean verifyotp = false;
    RelativeLayout rel_verify_mobile;
    private int CAMERA_PHOTO_REQUEST_CODE = 1001;
    private int selfie_flag = 0;
    RadioGroup radiogrp2;
    EditText edt_email;
    AppPreferenceManager appPreferenceManager;

    TextView tv_help;
    ImageView img_camera_pres, img_gallery_pres, img_camera_aadhar, img_gallery_aadhar, img_camera_trf, img_gallery_trf;
    ImageView img_camera_vial, img_gallery_vial, img_camera_other, img_gallery_other, img_camera_selfie;
    EditText edt_refer_by;
    Spinner spr_test;

    private int Select_PDFFILE = 2121;
    private Uri path;
    boolean OTPAccess;

    TextView samplecollectionponit;
    private getSCPTechnicianModel.getTechnicianlist selectedSCT;
    private ArrayList<getSCPTechnicianModel.getTechnicianlist> filterPatientsArrayList;

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
        appPreferenceManager = new AppPreferenceManager(activity);

        try {
            if (appPreferenceManager != null && appPreferenceManager.getCovidAccessResponseModel() != null) {
                OTPAccess = appPreferenceManager.getCovidAccessResponseModel().isCovidOtpAllow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewMain;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (cd.isConnectingToInternet()) {
            GetTesctCode();
        } else {
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }
    }

    private void GetTesctCode() {
        GetTestCodeController getTestCodeController = new GetTestCodeController(this);
        getTestCodeController.CallAPI(usercode);
    }


    private void displayrate(String Testcode) {
        CovidRateReqModel covidRateReqModel = new CovidRateReqModel();
        covidRateReqModel.setUsercode("" + usercode);
        covidRateReqModel.setAPIKEY("" + apikey);
        covidRateReqModel.setTestcode("" + Testcode);

        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Call<Covidratemodel> covidratemodelCall = postAPIInterface.displayrates(covidRateReqModel);
        covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
            @Override
            public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
                try {
                    if (!GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        if (!GlobalClass.isNull(response.body().getB2B())) {
                            b2b = response.body().getB2B();
                            Global.B2B = b2b;
                        }
                        if (!GlobalClass.isNull(response.body().getB2C())) {
                            b2c = response.body().getB2C();
                            Global.B2C = b2c;
                        }
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

    private void initView(final View view) {
        edt_refer_by = view.findViewById(R.id.edt_refer_by);
        preferences = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        edt_srfid = view.findViewById(R.id.edt_srfid);
        edt_fname = view.findViewById(R.id.edt_firstname);
        edt_amt = view.findViewById(R.id.edt_amt);
        img_scanbarcode = view.findViewById(R.id.img_scanbarcode);
        edt_lname = view.findViewById(R.id.edt_lastname);
        edt_missed_mobile = view.findViewById(R.id.edt_missed_mobile);
        edt_verifycc = view.findViewById(R.id.edt_verifycc);
        edt_passport_number = view.findViewById(R.id.edt_passport_number);

        tv_help = view.findViewById(R.id.tv_help);
        tv_help.setText(Html.fromHtml("<u> Help</u>"));

        if (!OTPAccess) {
            tv_help.setVisibility(View.VISIBLE);
        } else {
            tv_help.setVisibility(View.GONE);
        }


        edt_fname.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxLength = 16;
        InputFilter[] FilterArray = new InputFilter[2];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        FilterArray[1] = new InputFilter.AllCaps();
        edt_fname.setFilters(FilterArray);

        int maxLength1 = 16;
        edt_lname.setFilters(new InputFilter[]{EMOJI_FILTER});
        FilterArray[0] = new InputFilter.LengthFilter(maxLength1);
        FilterArray[1] = new InputFilter.AllCaps();
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
        radiogrp2 = view.findViewById(R.id.radiogrp2);
        spr_test = view.findViewById(R.id.spr_test);

        samplecollectionponit = view.findViewById(R.id.samplecollectionponit);
        samplecollectionponit.setText(Constants.setSCPmsg1);

        img_camera_pres = view.findViewById(R.id.img_camera_pres);
        img_gallery_pres = view.findViewById(R.id.img_gallery_pres);
        img_camera_aadhar = view.findViewById(R.id.img_camera_aadhar);
        img_gallery_aadhar = view.findViewById(R.id.img_gallery_aadhar);
        img_camera_trf = view.findViewById(R.id.img_camera_trf);
        img_gallery_trf = view.findViewById(R.id.img_gallery_trf);
        img_camera_vial = view.findViewById(R.id.img_camera_vial);
        img_gallery_vial = view.findViewById(R.id.img_gallery_vial);
        img_camera_other = view.findViewById(R.id.img_camera_other);
        img_gallery_other = view.findViewById(R.id.img_gallery_other);
        img_camera_selfie = view.findViewById(R.id.img_camera_selfie);


        //TODO Textviews
        txt_barcode = view.findViewById(R.id.txt_barcode);
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
        by_sendsms = (RadioButton) view.findViewById(R.id.by_sendsms);
        edt_email = view.findViewById(R.id.edt_email);
        by_missed.setOnClickListener(this);
        by_sendsms.setOnClickListener(this);
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

        img_camera_pres.setOnClickListener(this);
        img_gallery_pres.setOnClickListener(this);
        img_camera_aadhar.setOnClickListener(this);
        img_gallery_aadhar.setOnClickListener(this);
        img_camera_trf.setOnClickListener(this);
        img_gallery_trf.setOnClickListener(this);
        img_camera_vial.setOnClickListener(this);
        img_gallery_vial.setOnClickListener(this);
        img_camera_other.setOnClickListener(this);
        img_gallery_other.setOnClickListener(this);
        img_camera_selfie.setOnClickListener(this);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));

        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }

        if (Global.isKYC) {
            by_sendsms.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.GONE);
            by_missed.setVisibility(View.GONE);
        } else {
            by_missed.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.VISIBLE);
            by_sendsms.setVisibility(View.VISIBLE);
        }

        spr_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spr_test.getSelectedItem().toString().equalsIgnoreCase("Select Test*")) {
                    displayrate(spr_test.getSelectedItem().toString());
                    buttonval();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_refer_by.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonval();
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        btn_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyotp) {
                    if (selfie_flag == 0) {
                        boolean result = Utility.checkPermission(activity);
                        if (result)
                            openCamera();
                        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE);*/
                    } else {

                        Toast.makeText(activity, "Only 1 selfie can be Uploaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
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
                        edt_fname.setText("");
                    } else {
                        edt_fname.setText(enteredString.substring(1));
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
                        edt_lname.setText("");
                    } else {
                        edt_lname.setText(enteredString.substring(1));
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
                if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        edt_missed_mobile.setText(enteredString.substring(1));
                    } else {
                        edt_missed_mobile.setText("");
                    }
                }

                if (!OTPAccess) {
                    if (enteredString.length() == 10) {
                        mobileverify(edt_missed_mobile.getText().toString());
                    }
                }

                buttonval();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String checkNumber = s.toString();
                if (checkNumber.length() == 10) {
                    buttonval();
                }

            }
        });

        img_scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(Covidenter_Frag.this).initiateScan();
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
                if (BuildConfig.DEBUG) {
                    txt_barcode.setText("" + Global.randomBarcodeString(8));
                } else {
                    IntentIntegrator.forSupportFragment(Covidenter_Frag.this).initiateScan();
                }
            }
        });


        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.SetBottomSheet(activity);
            }
        });

        samplecollectionponit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallAPIForSCP();
                buttonval();
            }
        });
    }

    private void buttonval() {
        if (Validation()) {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            btn_submit.setTextColor(getResources().getColor(R.color.white));
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
                            if (!GlobalClass.isNull(edt_srfid.getText().toString()) && edt_srfid.getText().length() != 13) {
                                Global.showCustomToast(activity, "SRF-ID should be of 13 digits");
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
                                    covidpostdata.setTESTCODE("" + spr_test.getSelectedItem().toString());
                                    covidpostdata.setVIAIMAGE(vial_file);
                                    covidpostdata.setSELFIE(selfie_file);
                                    covidpostdata.setPPEBARCODE(txt_barcode.getText().toString().trim());
//                                    covidpostdata.setBARCODE(txt_barcode.getText().toString().trim());
                                    covidpostdata.setEMAIL("" + edt_email.getText().toString());
                                    covidpostdata.setREFBY("" + edt_refer_by.getText().toString().trim());
                                    covidpostdata.setPASSPORTNO(!GlobalClass.isNull(edt_passport_number.getText().toString()) ? edt_passport_number.getText().toString().toUpperCase() : "");

                                    if (selectedSCT != null) {
                                        covidpostdata.setTECHNICIAN(selectedSCT.getNED_NUMBER());
                                    }

                                    covidpostdata.setAPIKEY(apikey);

                                    if (edt_srfid.getText().toString() != null) {
                                        covidpostdata.setSRFID(edt_srfid.getText().toString());
                                    }
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
                }
                break;

            case R.id.btn_verify:
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

                break;

            case R.id.btn_generate:
                genrateflow();
                break;

            case R.id.by_missed:
                btn_generate.setText(getResources().getString(R.string.enterccc));
                clearfields("1");
                break;

            case R.id.by_sendsms:
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

            case R.id.img_camera_pres:
                if (verifyotp) {
                    if (checkPermission()) {

                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            openCamera();
                            GlobalClass.cropImageFullScreenFragment(this, 0);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;


            case R.id.img_gallery_pres:
                if (verifyotp) {
                    if (checkPermission()) {

                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            chooseFromGallery();
                            GlobalClass.cropImageFullScreenFragment(this, 2);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;


            case R.id.btn_choosefile_presc:
                if (verifyotp) {
                    if (checkPermission()) {

                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            selectImage();
                            GlobalClass.cropImageFullScreenFragment(this, 0);

                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.btn_choosefile_adhar:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            selectImage();
                            GlobalClass.cropImageFullScreenFragment(this, 0);
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

                break;

            case R.id.img_camera_aadhar:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            openCamera();
                            GlobalClass.cropImageFullScreenFragment(this, 0);
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

                break;

            case R.id.img_gallery_aadhar:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
//                            chooseFromGallery();
                            GlobalClass.cropImageFullScreenFragment(this, 2);
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

                break;

            case R.id.btn_choosefile_trf:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        CallBrowseFile();
                       /* if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            istrf = true;
                            selectImage();
                        }*/
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.img_camera_trf:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            istrf = true;
//                            openCamera();
                            GlobalClass.cropImageFullScreenFragment(this, 0);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.img_gallery_trf:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            istrf = true;
//                            chooseFromGallery();
                            GlobalClass.cropImageFullScreenFragment(this, 2);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.btn_choosefile_vial:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
//                            selectImage();
                            GlobalClass.cropImageFragment(this, 1);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.img_camera_vial:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
//                            openCamera();
                            GlobalClass.cropImageFragment(this, 0);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.img_gallery_vial:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
//                            chooseFromGallery();
                            GlobalClass.cropImageFragment(this, 2);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.btn_choosefile_other:

            case R.id.img_camera_other:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = true;
//                            openCamera();
                            GlobalClass.cropImageFullScreenFragment(this, 0);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
                break;

            case R.id.img_gallery_other:
                if (getVerifiedAllowed()) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = true;
//                            chooseFromGallery();
                            GlobalClass.cropImageFullScreenFragment(this, 2);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
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
                if (trf_file != null) {
                    setPdfToDialog(trf_file);
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
        edt_email.getText().clear();
        edt_amt.getText().clear();
        edt_missed_mobile.getText().clear();
        edt_passport_number.getText().clear();
        edt_verifycc.getText().clear();
        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setClickable(true);

        btn_verify.setClickable(true);
        btn_verify.setEnabled(true);
        btn_resend.setEnabled(true);
        btn_resend.setClickable(true);
        btn_selfie.setEnabled(true);
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        lin_by_missed.setVisibility(View.VISIBLE);


        if (by_missed.isChecked() || by_sendsms.isChecked()) {
            btn_generate.setText(getResources().getString(R.string.enterccc));
        } else {
            btn_generate.setText(getResources().getString(R.string.btngenerateccc));
        }
        samplecollectionponit.setText(Constants.setSCPmsg1);

        if (OTPAccess) {
            radiogrp2.setVisibility(View.GONE);
            verifyotp = true;
            btn_generate.setVisibility(View.GONE);
        } else {
            radiogrp2.setVisibility(View.VISIBLE);
            btn_generate.setVisibility(View.VISIBLE);
            btn_generate.setEnabled(true);
            btn_generate.setClickable(true);
            verifyotp = false;
        }

        if (Global.isKYC) {
            by_sendsms.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.GONE);
            by_missed.setVisibility(View.GONE);
        } else {
            by_missed.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.VISIBLE);
            by_sendsms.setVisibility(View.VISIBLE);
        }


        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }


        timerflag = false;

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

        txt_barcode.setText(getString(R.string.ppe));
        txt_barcode.setTextColor(getResources().getColor(R.color.default_text_color));

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

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setTextColor(getResources().getColor(R.color.black));
        txt_selfie.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);


        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));

        btn_selfie.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_selfie.setTextColor(getResources().getColor(R.color.white));

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
        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            Global.showCustomToast(getActivity(), ToastFile.AMTCOLL);
            edt_amt.requestFocus();
            return false;
        }
        try {
            if (!GlobalClass.isNull(b2c) && Integer.parseInt(edt_amt.getText().toString()) > Integer.parseInt(b2c)) {
                TastyToast.makeText(getActivity(), "You cannot enter collected amount more than " + b2c, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                return false;
            } else if (!GlobalClass.isNull(b2b) && Integer.parseInt(edt_amt.getText().toString()) < Integer.parseInt(b2b)) {
                TastyToast.makeText(getActivity(), "You cannot enter collected amount less than " + b2b, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        /*if (!GlobalClass.isNull(edt_passport_number.getText().toString())) {
            if (!Validator.passportValidation(edt_passport_number.getText().toString())) {
                Global.showCustomToast(getActivity(), "Enter valid passport number");
                edt_passport_number.requestFocus();
                return false;
            }
        }*/

        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            if (GlobalClass.isNull(edt_email.getText().toString())) {
                Global.showCustomToast(getActivity(), "Enter Email-ID");
                edt_email.requestFocus();
                return false;
            }

            if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                Global.showCustomToast(getActivity(), "Enter valid Email-ID");
                edt_email.requestFocus();
                return false;
            }
        } else {
            if (!TextUtils.isEmpty(edt_email.getText().toString())) {
                if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                    Global.showCustomToast(getActivity(), "Enter valid Email-ID");
                    edt_email.requestFocus();
                    return false;
                }
            }
        }

        if (spr_test.getSelectedItem().toString().equalsIgnoreCase("Select Test*")) {
            Global.showCustomToast(getActivity(), "Kindly Select Test");
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
                            radiogrp2.setVisibility(View.VISIBLE);
                            btn_generate.setVisibility(View.VISIBLE);
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

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN, 0);
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
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
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
                .setCompression(Constants.setcompression)
                .setImageHeight(Constants.setheight)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {

                if (ispresciption) {

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

                        CallAadharImageOCR(aadhar_file1);
                    } else {
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
                        CallAadharImageOCR(aadhar_file);
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
                } else {

                    String imageurl = camera.getCameraBitmapPath();
                    selfie_file = new File(imageurl);
                    txt_selfie.setVisibility(View.GONE);
                    lin_selfie.setVisibility(View.VISIBLE);
                    selfie_flag = 1;
                    txt_selfiefileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_selfiefileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                    btn_selfie.setTextColor(getResources().getColor(R.color.black));
                    btn_selfie.setEnabled(false);

                    // selfie_file = GlobalClass.getCompressedFile(activity, selfie_file);
                    selfielist.add(selfie_file.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (isvial) {
                    String imageurl = ImagePicker.Companion.getFile(data).toString();
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
                } else if (isadhar) {
                    if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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

                        CallAadharImageOCR(aadhar_file1);
                    } else {
                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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
                        CallAadharImageOCR(aadhar_file);
                        buttonval();
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

                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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

                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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
                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {

                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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

                        String imageurl = ImagePicker.Companion.getFile(data).toString();
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

                        aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file1.toString());
                        CallAadharImageOCR(aadhar_file1);
                    } else {
                        if (data.getData() != null) {
                            if (aadhar_file == null) {
                                aadhar_file = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();

                        aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file.toString());
                        CallAadharImageOCR(aadhar_file);
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
                    buttonval();

                    lin_adhar_images.setVisibility(View.VISIBLE);


                } else if (istrf) {
                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();

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
                    buttonval();

                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    if (data.getData() != null) {
                        vial_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();

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
        } else if (result != null) {
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    txt_barcode.setText("" + getBarcodeDetails.toUpperCase());
                    txt_barcode.setTextColor(getResources().getColor(android.R.color.black));
                    buttonval();
                } else {
                    Toast.makeText(getContext(), invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Select_PDFFILE) {
            try {
                if (data != null && data.getData() != null) {
                    path = data.getData();
                    if (path.toString().contains(".pdf")) {
                        String pt = FilePath.getPath(activity, path);
                        if (pt == null) {
                            Toast.makeText(activity, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_SHORT).show();
                        } else {
                            trf_file = new File(FilePath.getPath(activity, path));
                            lin_trf_images.setVisibility(View.VISIBLE);
                            txt_trffileupload.setVisibility(View.VISIBLE);
                            txt_trffileupload.setText("PDF Uploaded");
                            txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            txt_nofiletrf.setVisibility(View.GONE);
                            buttonval();
                        }
                    } else {
                        Toast.makeText(activity, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(activity, "Something wrong with file", Toast.LENGTH_SHORT).show();
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
        if (edt_refer_by.getText().toString().length() < 2) {
            return false;
        }

        if (edt_refer_by.getText().toString().length() == 0) {
            return false;
        }
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


        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            return false;
        }

        if (txt_barcode.getText().toString().equalsIgnoreCase(getResources().getString(R.string.ppe))) {
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

 /*       if (samplecollectionponit.getText().toString().trim().equalsIgnoreCase(Constants.setSCPmsg)) {
            Toast.makeText(activity, ToastFile.slt_technicain, Toast.LENGTH_SHORT).show();
            return false;
        }*/

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
        txt_barcode.setText(getString(R.string.ppe));
        txt_barcode.setTextColor(getResources().getColor(R.color.default_text_color));
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

        if (by_missed.isChecked() || by_sendsms.isChecked()) {
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

        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setTextColor(getResources().getColor(R.color.black));
        txt_selfie.setPaintFlags(0);

        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));

        btn_selfie.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_selfie.setTextColor(getResources().getColor(R.color.white));


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
        radiogrp2.setVisibility(View.GONE);
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
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        disablefields();
                    } else {
                        GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
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

    public void getUploadResponse(String response, String mobileNo) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                new LogUserActivityTagging(activity, "WOE-COVID", mobileNo);
                Intent i = new Intent(getActivity(), ManagingTabsActivity.class);
                startActivity(i);
                Constants.covidfrag_flag = "1";

                clearfields("3");


            } else {
                Global.showCustomToast(activity, RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPdfToDialog(File trf_file) {
        try {
            Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".imageprovider", trf_file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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

                        if (type.equalsIgnoreCase("pres")) {
                            presc_file = null;
                            presclist.clear();
                            txt_nofilepresc.setVisibility(View.VISIBLE);
                            txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
                            lin_pres_preview.setVisibility(View.GONE);
                            btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            btn_choosefile_presc.setTextColor(getResources().getColor(R.color.white));
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
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
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
                                } else if (1 == viewPager.getCurrentItem()) {
                                    trf_file1 = null;
                                    if (trflist.size() == 2) {
                                        trflist.remove(trflist.get(1));
                                    } else {
                                        trflist.remove(trflist.get(0));
                                    }
                                }

                                if (trflist.size() == 1) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
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
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
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
                            btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            btn_choosefile_vial.setTextColor(getResources().getColor(R.color.white));
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
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                }

                                if (otherlist.size() == 1) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
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
                            btn_selfie.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            btn_selfie.setTextColor(getResources().getColor(R.color.white));

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

    public void getTestCode(GetTestCodeResponseModel body) {
        if (!GlobalClass.isNull(body.getResID()) && body.getResID().equalsIgnoreCase("RES0000")) {
            if (GlobalClass.CheckArrayList(body.getTestCodeMappinpList())) {
                body.getTestCodeMappinpList().add(0, "Select Test*");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.name_age_spinner, body.getTestCodeMappinpList());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_test.setAdapter(adapter);

            } else {
                GlobalClass.showShortToast(activity, body.getResponse());
            }
        } else {
            GlobalClass.showShortToast(activity, body.getResponse());
        }
    }

    private void showDialogue(final ArrayList<getSCPTechnicianModel.getTechnicianlist> obj) {

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.custom_alert_scp, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        LinearLayoutManager linearLayoutManager = null;

        EditText search_view = (EditText) promptsView.findViewById(R.id.search_view);
        TextView title = (TextView) promptsView.findViewById(R.id.spinerTitle);
        ImageView close = (ImageView) promptsView.findViewById(R.id.close);
        final RecyclerView scp_name = (RecyclerView) promptsView.findViewById(R.id.scp_name);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scp_name.setLayoutManager(linearLayoutManager);


        title.setText(Constants.setSCPmsg);

        final ArrayList<getSCPTechnicianModel.getTechnicianlist> labDetailsArrayList = new ArrayList<>();

        for (int i = 0; i < obj.size(); i++) {
            labDetailsArrayList.add(obj.get(i));
        }

        final SCollectionPAdapter sampleCollectionAdapter = new SCollectionPAdapter(Covidenter_Frag.this, activity, labDetailsArrayList);
        sampleCollectionAdapter.setOnItemClickListener(new SCollectionPAdapter.OnItemClickListener() {
            @Override
            public void onPassSgcID(getSCPTechnicianModel.getTechnicianlist pos) {
                samplecollectionponit.setText(pos.getNAME() + " - " + pos.getNED_NUMBER());
                selectedSCT = pos;
                alertDialog.dismiss();
            }
        });
        scp_name.setAdapter(sampleCollectionAdapter);

        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().toLowerCase();

                filterPatientsArrayList = new ArrayList<>();
                String labname = "";
                String clientId = "";
                if (labDetailsArrayList != null) {
                    for (int i = 0; i < labDetailsArrayList.size(); i++) {

                        final String text1 = labDetailsArrayList.get(i).getNAME().toLowerCase();
                        final String text2 = labDetailsArrayList.get(i).getNED_NUMBER().toLowerCase();

                        if (labDetailsArrayList.get(i).getNED_NUMBER() != null || !labDetailsArrayList.get(i).getNED_NUMBER().equals("")) {
                            clientId = labDetailsArrayList.get(i).getNED_NUMBER().toLowerCase();
                        }
                        if (labDetailsArrayList.get(i).getNAME() != null || !labDetailsArrayList.get(i).getNAME().equals("")) {
                            labname = labDetailsArrayList.get(i).getNAME().toLowerCase();
                        }

                        if (text1.contains(s1) || (clientId != null && clientId.contains(s1)) ||
                                (labname != null && labname.contains(s1))) {
                            String testname = (labDetailsArrayList.get(i).getNAME());
                            filterPatientsArrayList.add(labDetailsArrayList.get(i));

                        } else {

                        }
                        sampleCollectionAdapter.filteredArraylist(filterPatientsArrayList);
                        sampleCollectionAdapter.notifyDataSetChanged();
                    }
                }
                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        if (GlobalClass.setFlagToClose) {
            alertDialog.dismiss();
        }
    }

    private void CallAPIForSCP() {
        SharedPreferences sdfsd_pref = activity.getSharedPreferences("SCPDATA", MODE_PRIVATE);
        if (GlobalClass.isNull(sdfsd_pref.getString("scplist", ""))) {
            CallAPItogetSCPData();
        } else {
            if (GlobalClass.checkSync(activity, "SCTTech")) {
                CallAPItogetSCPData();
            } else {
                setFinalSCPList(sdfsd_pref.getString("scplist", ""));
            }
        }


    }

    private void CallAPItogetSCPData() {
        if (GlobalClass.isNetworkAvailable(activity)) {
            try {
                ControllersGlobalInitialiser.getSCPSRFAPIController = new getSCPSRFAPIController(Covidenter_Frag.this, activity);
                ControllersGlobalInitialiser.getSCPSRFAPIController.getSCPtechnician(true, usercode, apikey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFinalSCPList(String response) {
        if (response != null) {
            if (!response.toString().trim().equalsIgnoreCase("")) {
                Gson gson = new Gson();
                getSCPTechnicianModel getSCP_model = gson.fromJson(response, getSCPTechnicianModel.class);
                if (getSCP_model != null) {
                    if (getSCP_model.getResId() != null) {
                        if (getSCP_model.getResId().toString().trim().equalsIgnoreCase(Constants.RES0000)) {
                            GlobalClass.storeCachingDate(activity, "SCTTech");
                            showDialogue(getSCP_model.getTechnicianlist());
                        }
                    }
                }
            }
        }
    }

    public void setSCPresData(String scpdata) {
        SharedPreferences.Editor saveProfileDetails = activity.getSharedPreferences("SCPDATA", 0).edit();
        saveProfileDetails.putString("scplist", scpdata);
        saveProfileDetails.apply();
    }


    private void CallAadharImageOCR(File aadhar_imgfile) {
        try {
            String filePath = aadhar_imgfile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            CalltextRecFromImage(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalltextRecFromImage(Bitmap bitmap) {
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            TextRecognizer recognizer = TextRecognition.getClient();

            // [START run_detector]
            Task<Text> result = recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text visionText) {
                            for (Text.TextBlock block : visionText.getTextBlocks()) {
                                Rect boundingBox = block.getBoundingBox();
                                Point[] cornerPoints = block.getCornerPoints();
                                String text = block.getText();

                                for (Text.Line line : block.getLines()) {
                                    // ...
                                    for (Text.Element element : line.getElements()) {
                                        // ...
                                    }
                                }
                            }
                            processTextBlock(visionText);
                        }
                    }).addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                    // Task failed with an exception
                                    // ...
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTextBlock(Text result) {
        try {
            String resultText = result.getText();
            if (result.getTextBlocks().size() != 0) {

                for (int j = 0; j < result.getTextBlocks().size(); j++) {
                    Text.TextBlock block = result.getTextBlocks().get(j);
                    String blockText = block.getText();
                    setNameFromList(blockText, j, result.getTextBlocks());
                    for (int i = 0; i < block.getLines().size(); i++) {
                        Text.Line line = block.getLines().get(i);
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();

                        setName(lineText, i, block.getLines());
                    }
                }

                getLastNameFromFinalLastString(result.getTextBlocks());
                getFirstNameFromFinalLastString(result.getTextBlocks());
                // [END mlkit_process_text_block]
            } else {
//                Toast.makeText(activity, "Please select proper image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNameFromList(String lineText, int i, List<Text.TextBlock> lines) {
        try {
            if (lines != null) {
                if (lineText.toString().trim().contains("D0B") || lineText.toString().trim().contains("DOB") || lineText.toString().trim().contains("Year Of Birth") || lineText.toString().trim().contains("Year of Birth") || lineText.toString().trim().contains("Birth")) {
                    if (isValidNameOnly(lines.get(i - 1).getText())) {
                        SetName(lines.get(i - 1).getText());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setName(String lineText, int i, List<Text.Line> lines) {
        try {
            if (lines != null) {
                if (lines.size() > i + 1) {
                    Text.Line line = lines.get(i + 1);
                    if (line.getText().toString().trim().contains("D0B") || line.getText().toString().trim().contains("DOB") || line.getText().toString().trim().contains("Year Of Birth") || line.getText().toString().trim().contains("Year of Birth") || line.getText().toString().trim().contains("Birth")) {
                        //                    txt_aadharname.setText("Name: " + lineText);
                        SetName(lineText);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetName(String lineText) {
        try {
            if (lineText != null) {
                if (!lineText.toString().trim().equalsIgnoreCase("")) {
                    String[] splited = lineText.split("\\s+");
                    if (splited.length != 0) {
                        if (splited.length == 1) {
                            if (edt_fname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_fname.setText(splited[0].toString().trim());
                                }

                            }
                            if (edt_lname.getText().toString().trim().equalsIgnoreCase("")) {
                                edt_lname.setText("");
                            }
                        } else if (splited.length == 2) {
                            if (edt_fname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_fname.setText(splited[0].toString().trim());
                                }
                            }
                            if (edt_lname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[1].toString().trim().contains("GOVERN") || splited[1].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_lname.setText(splited[1].toString().trim());
                                }
                            }

                        } else if (splited.length == 3) {
                            if (edt_fname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_fname.setText(splited[0].toString().trim());
                                }
                            }

                            if (edt_lname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[2].toString().trim().contains("GOVERN") || splited[2].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_lname.setText(splited[2].toString().trim());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String regxName = "([A-Z][a-z]*\\s*)+";

    public static boolean isValidNameOnly(String str) {
        String regex = regxName;
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void getFirstNameFromFinalLastString(List<Text.TextBlock> allText) {
        try {
            String st = "";
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.startsWith("P<") || stsd.startsWith("p<")) {
                            st = getFinalFirstname(stsd);
                        }
                    }
                }
            }

            if (edt_fname.getText().toString().trim().equalsIgnoreCase("")) {
                edt_fname.setText(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFinalFirstname(String fstr) {
        String st = "";
        try {
            String[] splited = fstr.split("<<");
            if (splited.length != 0 && splited.length > 1) {
                String stksh = splited[1].toString().trim();
                stksh = stksh.replaceAll("\\s+", "");
                stksh = stksh.replaceAll("<", "");
                System.out.println("Nitya --- " + stksh);
                st = stksh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    private void getLastNameFromFinalLastString(List<Text.TextBlock> allText) {
        try {
            String st = "";
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.startsWith("P<") || stsd.startsWith("p<")) {
                            System.out.println("Nitya =Name= " + stsd);
                            st = getFinalSurname(stsd);
                        }
                    }
                }
            }

            if (edt_lname.getText().toString().trim().equalsIgnoreCase("")) {
                edt_lname.setText(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFinalSurname(String fstr) {
        String st = "";
        try {
            String[] splited = fstr.split("<");
            if (splited.length != 0 && splited.length > 1) {
                String stksh = splited[1].toString().trim();
                stksh = stksh.substring(3, stksh.length());
                st = stksh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public static boolean isValidPassPortNumber(String str) {
//        String regex = "^[A-Z]{1}[0-9]{7}$";
        String regex = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getPassPortCode(String str) {
        String regex = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return "" + m.group(0);
        }
        return "";
    }

    private void CallBrowseFile() {
        try {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), Select_PDFFILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean getVerifiedAllowed() {
        if (OTPAccess) {
            return true;
        } else {
            if (verifyotp) {
                return true;
            } else {
                return false;
            }
        }
    }
}
