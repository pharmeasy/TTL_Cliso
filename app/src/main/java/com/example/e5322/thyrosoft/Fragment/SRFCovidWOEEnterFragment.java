package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.SRFCovidWOEMainActivity;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Adapter.SCollectionPAdapter;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetTestCodeController;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.SRFCovidWOEmultipart_controller;
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
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.ResponseModels.VerifyBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.getSCPTechnicianModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class SRFCovidWOEEnterFragment extends Fragment {


    private Activity activity;
    private ConnectionDetector cd;
    private List<String> presclist = new ArrayList<>();
    private List<String> aadharlist = new ArrayList<>();
    private List<String> viallist = new ArrayList<>();
    private List<String> otherlist = new ArrayList<>();
    private List<String> selfielist = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private RadioButton by_missed, by_generate, by_sendsms;
    private LinearLayout mainlinear, consignment_name_layout, lineareditbarcode, lin_by_missed, lin_selfie, lin_generate_verify, lin_pres_preview, lin_adhar_images, lin_vial_images, lin_other_images;
    private RelativeLayout rel_mobno, rel_verify_mobile;
    private Button btn_choosefile_presc, btn_barcd, btn_selfie, btn_choosefile_adhar, btn_choosefile_vial, btn_choosefile_other, btn_generate, btn_submit, btn_verify, btn_resend, btn_reset;
    private TextView txt_nofilepresc, txt_nofileadhar, txt_selfie, txt_nofilevial, txt_nofileother, tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob, txt_presfileupload, txt_selfiefileupload, txt_adharfileupload, txt_vialrfileupload, txt_otherfileupload, tv_coll_date, tv_coll_time;
    private final int PICK_PHOTO_FROM_GALLERY = 202;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    private Camera camera;
    private String TAG = SRFCovidWOEEnterFragment.class.getSimpleName();
    private File presc_file = null, aadhar_file = null, aadhar_file1 = null, vial_file = null, other_file = null, other_file1 = null, selfie_file = null;
    private boolean ispresciption, isadhar, isvial, isother, timerflag = false, genderId = false, flag = true, verifyotp = false, iscamera = false;
    private EditText edt_fname, edt_amt, edt_lname, edt_missed_mobile, edt_verifycc, edt_srfid, edt_age, edt_patient_address, edt_patient_pincode, edt_coll_address, edt_coll_pincode, edt_barcode, edt_re_enter_barcode;
    private String usercode, apikey, gender = "", currentText, selDate = "", selTime = "", userChoosenTask, b2b, b2c;
    private int selfie_flag = 0, agesinteger;
    private Spinner spn_age, spn_scp;
    private ImageView male, female, male_red, female_red, img_scan__barcode;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Date minDate;
    EditText edt_email, enter_barcode, reenter;
    AppPreferenceManager appPreferenceManager;
    private ImageView setback;
    IntentIntegrator scanIntegrator;
    TextView tv_help;

    ImageView img_camera_prescription, img_gallery_prescription, img_camera_aadhar, img_gallery_aadhar;
    ImageView img_camera_vial, img_gallery_vial, img_camera_other, img_gallery_other, img_camera_selfie;

    Spinner spr_test;
    TextView samplecollectionponit;
    static SRFCovidWOEEnterFragment fragment;
    private getSCPTechnicianModel.getTechnicianlist selectedSCT;
    private ArrayList<getSCPTechnicianModel.getTechnicianlist> filterPatientsArrayList;
    private boolean isgetDOB = false;
    private boolean isCapturedAddress = false;
    ArrayList<String> getAddressList;
    ArrayList<String> l_pincode = new ArrayList<>();

    public SRFCovidWOEEnterFragment() {
        // Required empty public constructor
    }

    public static SRFCovidWOEEnterFragment newInstance() {
        fragment = new SRFCovidWOEEnterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_srf_covid_woe_enter, container, false);

        activity = getActivity();
        fragment = this;
        cd = new ConnectionDetector(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");
        scanIntegrator = IntentIntegrator.forSupportFragment(this);
        initUI(view);
        initListeners();
        GetTestCode();

        setAgeSpinner(R.array.Patientsagespinner);
        spn_age.setSelection(0);

        setSCPSpinner();

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, mDay - 2);
        minDate = c1.getTime();


        return view;
    }

    private void GetTestCode() {
        if (cd.isConnectingToInternet()) {
            GetTestCodeController getTestCodeController = new GetTestCodeController(this);
            getTestCodeController.CallAPI(usercode);
        } else {
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }

    }

    private void initUI(View view) {
        edt_fname = view.findViewById(R.id.edt_firstname);
        edt_amt = view.findViewById(R.id.edt_amt);
        edt_lname = view.findViewById(R.id.edt_lastname);
        edt_missed_mobile = view.findViewById(R.id.edt_missed_mobile);
        edt_verifycc = view.findViewById(R.id.edt_verifycc);
        edt_srfid = view.findViewById(R.id.edt_srfid);
        edt_patient_address = view.findViewById(R.id.edt_patient_address);
        edt_patient_pincode = view.findViewById(R.id.edt_patient_pincode);
        edt_coll_address = view.findViewById(R.id.edt_coll_address);
        edt_coll_pincode = view.findViewById(R.id.edt_coll_pincode);
        edt_barcode = view.findViewById(R.id.edt_barcode);
        edt_re_enter_barcode = view.findViewById(R.id.edt_re_enter_barcode);
        tv_coll_date = view.findViewById(R.id.tv_coll_date);
        tv_coll_time = view.findViewById(R.id.tv_coll_time);
        edt_age = view.findViewById(R.id.edt_age);
        spn_age = view.findViewById(R.id.spn_age);
        spn_scp = view.findViewById(R.id.spn_scp);
        male = view.findViewById(R.id.male);
        male_red = view.findViewById(R.id.male_red);
        female = view.findViewById(R.id.female);
        female_red = view.findViewById(R.id.female_red);
        spr_test = view.findViewById(R.id.spr_test);
        btn_choosefile_presc = view.findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = view.findViewById(R.id.btn_choosefile_adhar);
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

        tv_help = view.findViewById(R.id.tv_help);
        tv_help.setText(Html.fromHtml("<u> Help</u>"));

        setback = view.findViewById(R.id.setback);
        enter_barcode = view.findViewById(R.id.enter_barcode);
        reenter = view.findViewById(R.id.reenter);
        lineareditbarcode = view.findViewById(R.id.lineareditbarcode);
        img_scan__barcode = view.findViewById(R.id.img_scan_barcode);
        btn_barcd = view.findViewById(R.id.btn_barcd);
        consignment_name_layout = view.findViewById(R.id.consignment_name_layout);

        rel_verify_mobile = view.findViewById(R.id.rel_verify_mobile);
        tv_verifiedmob = view.findViewById(R.id.tv_verifiedmob);
        lin_by_missed = view.findViewById(R.id.lin_missed_verify);
        lin_generate_verify = view.findViewById(R.id.lin_generate_verify);
        lin_pres_preview = view.findViewById(R.id.lin_pres_preview);
        lin_adhar_images = view.findViewById(R.id.lin_adhar_images);
        lin_vial_images = view.findViewById(R.id.lin_vial_images);
        lin_other_images = view.findViewById(R.id.lin_other_images);
        mainlinear = view.findViewById(R.id.mainlinear);
        rel_mobno = view.findViewById(R.id.rel_mobno);
        txt_presfileupload = view.findViewById(R.id.txt_presfileupload);
        txt_adharfileupload = view.findViewById(R.id.txt_adharfileupload);
        txt_vialrfileupload = view.findViewById(R.id.txt_vialrfileupload);
        txt_otherfileupload = view.findViewById(R.id.txt_otherfileupload);
        txt_nofilepresc = view.findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = view.findViewById(R.id.txt_nofileadhar);
        txt_nofilevial = view.findViewById(R.id.txt_nofilevial);
        txt_nofileother = view.findViewById(R.id.txt_nofileother);
        btn_selfie = view.findViewById(R.id.btn_selfie);
        tv_timer = view.findViewById(R.id.tv_timer);
        tv_resetno = view.findViewById(R.id.tv_resetno);
        tv_mobileno = view.findViewById(R.id.tv_mobileno);
        by_missed = view.findViewById(R.id.by_missed);
        by_generate = view.findViewById(R.id.by_generate);
        by_sendsms = view.findViewById(R.id.by_sendsms);
        edt_email = view.findViewById(R.id.edt_email);

        samplecollectionponit = view.findViewById(R.id.samplecollectionponit);
        samplecollectionponit.setText(Constants.setSCPmsg);

        img_camera_prescription = view.findViewById(R.id.img_camera_prescription);
        img_gallery_prescription = view.findViewById(R.id.img_gallery_prescription);
        img_camera_aadhar = view.findViewById(R.id.img_camera_aadhar);
        img_gallery_aadhar = view.findViewById(R.id.img_gallery_aadhar);
        img_camera_vial = view.findViewById(R.id.img_camera_vial);
        img_gallery_vial = view.findViewById(R.id.img_gallery_vial);
        img_camera_other = view.findViewById(R.id.img_camera_other);
        img_gallery_other = view.findViewById(R.id.img_gallery_other);
        img_camera_selfie = view.findViewById(R.id.img_camera_selfie);


        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));

        edt_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edt_re_enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());

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
    }

    private void initListeners() {


        spr_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spr_test.getSelectedItem().toString().equalsIgnoreCase("Select Test*")) {
                    displayrate(spr_test.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_camera_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (selfie_flag == 0) {
                        boolean result = Utility.checkPermission(activity);
                        if (result)
                            ispresciption = false;
                        isadhar = false;
                        isvial = false;
                        isother = false;
                        iscamera = true;
                        openCamera();
                    } else {
                        GlobalClass.showCustomToast(activity, "Only 1 selfie can be Uploaded", 0);
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });


        img_gallery_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = true;
                            chooseFromGallery();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        img_camera_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = true;
                            iscamera = false;
                            openCamera();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        img_gallery_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = true;
                            isother = false;
//                            chooseFromGallery();
                            GlobalClass.cropImageFragment(SRFCovidWOEEnterFragment.this, 2);

                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        img_camera_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = true;
                            isother = false;
//                            openCamera();
                            GlobalClass.cropImageFragment(SRFCovidWOEEnterFragment.this, 0);
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        img_camera_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            isvial = false;
                            isother = false;
                            iscamera = false;
                            openCamera();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

            }
        });
        img_gallery_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            isvial = false;
                            isother = false;
                            chooseFromGallery();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

            }
        });

        img_camera_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            iscamera = false;
                            openCamera();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });
        img_gallery_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verifyotp) {
                    if (checkPermission()) {
                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            chooseFromGallery();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }

            }
        });


        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.SetBottomSheet(activity);
            }
        });

        btn_barcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consignment_name_layout.setVisibility(View.GONE);
                lineareditbarcode.setVisibility(View.VISIBLE);
            }
        });


        img_scan__barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanIntegrator.initiateScan();
            }
        });

        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.GONE);
                consignment_name_layout.setVisibility(View.VISIBLE);
            }
        });


        reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (enter_barcode.getText().length() != 8) {
                    Toast.makeText(activity, "Enter Valid Enter Barcode", Toast.LENGTH_SHORT).show();
                } else {
                    if (s.toString().equalsIgnoreCase(enter_barcode.getText().toString())) {
                        verifyBarcode(s.toString());
                    } else {
                        if (s.length() == 8) {
                            Toast.makeText(activity, "Barcode not Matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleBtnClick();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleBtnClick();
            }
        });

        edt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    if (enteredString.length() > 1) {
                        edt_age.setText(enteredString.substring(1));
                    } else {
                        edt_age.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 0) {
                    int result = Integer.parseInt(enteredString);
                    if (result > 120) {
                        edt_age.setText(enteredString.substring(0, 2));
                        edt_age.setText("");
                    }
                }
                if (s.length() > 0) {
                    agesinteger = Integer.parseInt(s.toString());
                }
                if (!GlobalClass.isNull(edt_age.getText().toString())) {
                    if (agesinteger < 12) {
                        setAgeSpinner(R.array.Patientsagespinner);
                    }
                    if (agesinteger > 12) {
                        setAgeSpinner(R.array.Patientspinyrday);
                    }
                    if (agesinteger > 29) {
                        setAgeSpinner(R.array.Patientspinyr);
                    }
                }
                buttonval();
            }
        });

        edt_patient_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(getActivity(), ToastFile.patient_crt_addr, 0);
                    if (enteredString.length() > 0) {
                        edt_patient_address.setText(enteredString.substring(1));
                    } else {
                        edt_patient_address.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_patient_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0")) {
                    GlobalClass.showCustomToast(getActivity(), ToastFile.patient_crt_pincode, 0);
                    if (enteredString.length() > 0) {
                        edt_patient_pincode.setText(enteredString.substring(1));
                    } else {
                        edt_patient_pincode.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        edt_coll_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(getActivity(), ToastFile.coll_crt_addr, 0);
                    if (enteredString.length() > 0) {
                        edt_coll_address.setText(enteredString.substring(1));
                    } else {
                        edt_coll_address.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_coll_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0")) {
                    GlobalClass.showCustomToast(getActivity(), ToastFile.coll_crt_pincode, 0);
                    if (enteredString.length() > 0) {
                        edt_coll_pincode.setText(enteredString.substring(1));
                    } else {
                        edt_coll_pincode.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

        edt_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.entr_brcd, 0);
                    if (enteredString.length() > 0) {
                        edt_barcode.setText(enteredString.substring(1));
                    } else {
                        edt_barcode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (s.length() < 8) {
                    flag = true;
                }
                if (s.length() > 8) {
                    edt_barcode.setText(enteredString.substring(1));
                    GlobalClass.showCustomToast(activity, ToastFile.crt_brcd, 0);
                }
                if (s.length() == 8) {
                    if (flag) {
                        flag = false;
                        if (!cd.isConnectingToInternet()) {
                            edt_barcode.setText(s);
                        } else {
                            verifyBarcode(s.toString());
                        }
                    }
                }
                buttonval();
            }
        });

        edt_re_enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.entr_brcd, 0);
                    if (enteredString.length() > 0) {
                        edt_re_enter_barcode.setText(enteredString.substring(1));
                    } else {
                        edt_re_enter_barcode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() == 8) {
                    String getPreviouseText = edt_barcode.getText().toString();
                    currentText = edt_re_enter_barcode.getText().toString();
                    if (getPreviouseText.equals(currentText)) {
                        GlobalClass.showCustomToast(activity, ToastFile.mtch_brcd, 0);
                    } else {
                        edt_re_enter_barcode.setText("");
                        GlobalClass.showCustomToast(activity, ToastFile.crt_brcd, 0);
                    }
                }
                buttonval();
            }
        });

        by_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_generate.setText(getResources().getString(R.string.enterccc));
                clearfields("1");
            }
        });

        by_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_generate.setText(getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
                clearfields("2");
            }
        });

        btn_choosefile_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        btn_choosefile_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = true;
                            isother = false;
//                            selectImage();
                            GlobalClass.cropImageFragment(SRFCovidWOEEnterFragment.this, 2);

                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            isvial = false;
                            isother = true;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
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
                    } else {
                        GlobalClass.showCustomToast(activity, "Only 1 selfie can be Uploaded", 0);
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY, 0);
                }
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genrateflow();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearonreset();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        tv_resetno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    if (amtvalidation()) {
                        callSRFCovidWOEAPI();
                    }
                }
            }
        });

        txt_presfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presclist != null && presclist.size() > 0) {
                    setviewpager(presclist, "pres");
                }
            }
        });

        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
            }
        });

        txt_vialrfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viallist != null && viallist.size() > 0) {
                    setviewpager(viallist, "vial");
                }
            }
        });

        txt_otherfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherlist != null && otherlist.size() > 0) {
                    setviewpager(otherlist, "other");
                }
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genrateflow();
            }
        });

        mainlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.Hidekeyboard(view);
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

        txt_selfiefileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(selfielist, "selfie");
            }
        });

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
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                ) {
                    GlobalClass.showCustomToast(activity, ToastFile.crt_mob_num, 0);
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

        tv_coll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        selDate = GlobalClass.convertDate(selDate, "dd-MM-yyyy");
                        GlobalClass.SetText(tv_coll_date, selDate);
                        if (!GlobalClass.isNull(selTime)) {
                            validateSelectedDateTime();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        tv_coll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selTime = hourOfDay + ":" + minute;
                                selTime = GlobalClass.convertDate(selTime, "HH:mm");
                                GlobalClass.SetText(tv_coll_time, selTime);
                                if (!GlobalClass.isNull(selDate)) {
                                    validateSelectedDateTime();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        spn_scp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buttonval();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        samplecollectionponit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallAPIForSCP();
            }
        });
    }

    private void callSRFCovidWOEAPI() {
        if (!cd.isConnectingToInternet()) {
            GlobalClass.showAlertDialog(getActivity());
        } else {
            try {
                String age_type;
                if (spn_age.getSelectedItem().toString().equalsIgnoreCase("Years")) {
                    age_type = "Y";
                } else if (spn_age.getSelectedItem().toString().equalsIgnoreCase("Months")) {
                    age_type = "M";
                } else {
                    age_type = "D";
                }
                String fname = edt_fname.getText().toString().trim();
                String lname = edt_lname.getText().toString().trim();
                String fullname = fname + " " + lname;

                Covidpostdata covidpostdata = new Covidpostdata();
                covidpostdata.setSOURCECODE(usercode);
                covidpostdata.setNAME(fullname);
                covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                covidpostdata.setAMOUNTCOLLECTED(edt_amt.getText().toString().trim());
                covidpostdata.setSRFID(edt_srfid.getText().toString().trim());
                covidpostdata.setAGE(edt_age.getText().toString().trim());
                covidpostdata.setAGETYPE(age_type);
                covidpostdata.setTESTCODE("" + spr_test.getSelectedItem().toString());
                covidpostdata.setGENDER(gender);
                covidpostdata.setPATIENTADDRESS(edt_patient_address.getText().toString().trim());
                covidpostdata.setPATIENTPINCODE(edt_patient_pincode.getText().toString().trim());
                covidpostdata.setSCP(spn_scp.getSelectedItem().toString());
                covidpostdata.setCOLLECTIONADDRESS(edt_coll_address.getText().toString().trim());
                covidpostdata.setCOLLECTIONPINCODE(edt_coll_pincode.getText().toString().trim());
                covidpostdata.setSPECIMENDATE(selDate);
                covidpostdata.setSPECIMENTIME(selTime);
                covidpostdata.setBARCODE(btn_barcd.getText().toString().trim());
                covidpostdata.setVIAIMAGE(vial_file);

                covidpostdata.setTECHNICIAN(selectedSCT.getNED_NUMBER());
                covidpostdata.setAPIKEY(apikey);

                covidpostdata.setEMAIL("" + edt_email.getText().toString());
                if (selfie_file != null) {
                    covidpostdata.setSELFIE(selfie_file);
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
                if (other_file != null && other_file1 != null) {
                    covidpostdata.setOTHER(other_file);
                    covidpostdata.setOTHER1(other_file1);
                } else if (other_file != null && other_file1 == null) {
                    covidpostdata.setOTHER(other_file);
                } else {
                    covidpostdata.setOTHER(other_file1);
                }
                new SRFCovidWOEmultipart_controller(SRFCovidWOEEnterFragment.this, covidpostdata).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void validateSelectedDateTime() {
        String selDateTime = selDate + " " + selTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = GlobalClass.dateFromString(selDateTime, simpleDateFormat);
        if (date.after(new Date())) {
            GlobalClass.showCustomToast(activity, ToastFile.SELECT_VALID_COLL_TIME, 0);
            tv_coll_time.setText("");
        }
    }

    private void displayrate(String Testcode) {
        CovidRateReqModel covidRateReqModel = new CovidRateReqModel();
        covidRateReqModel.setUsercode("" + usercode);
        covidRateReqModel.setAPIKEY("" + apikey);
        covidRateReqModel.setTestcode("" + Testcode);

        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);
        Call<Covidratemodel> covidratemodelCall = postAPIInteface.displayrates(covidRateReqModel);
        covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
            @Override
            public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
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

    private void verifyBarcode(final String s) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        RequestQueue requestQueue = GlobalClass.setVolleyReq(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + apikey + "/" + s + "/getcheckbarcode", new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    GlobalClass.printLog(Constants.ERROR, TAG, "Barcode response: ", "" + response);
                    VerifyBarcodeResponseModel responseModel = new Gson().fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);
                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                            btn_barcd.setText(s);
                            lineareditbarcode.setVisibility(View.GONE);
                            consignment_name_layout.setVisibility(View.VISIBLE);
                        } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(activity);
                        } else {
                            edt_barcode.setText("");
                            edt_re_enter_barcode.setText("");
                            GlobalClass.showCustomToast(activity, "" + responseModel.getResponse(), 0);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest);
        GlobalClass.printLog(Constants.ERROR, TAG, "Barcode response: ", "" + jsonObjectRequest);
    }

    private void setAgeSpinner(int array) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, array, R.layout.name_age_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_age.setAdapter(adapter);
    }

    private void setSCPSpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, R.array.scp_entries, R.layout.name_age_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_scp.setAdapter(adapter);
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

    private void clearonreset() {
        rel_verify_mobile.setVisibility(View.GONE);
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_email.getText().clear();
        edt_amt.getText().clear();
        btn_barcd.setText("BARCODE*");
        enter_barcode.getText().clear();
        reenter.getText().clear();
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
        edt_srfid.getText().clear();
        edt_age.getText().clear();
        male_red.setVisibility(View.GONE);
        female.setVisibility(View.VISIBLE);
        female_red.setVisibility(View.GONE);
        male.setVisibility(View.VISIBLE);
        edt_patient_address.getText().clear();
        edt_patient_pincode.getText().clear();
        spn_scp.setSelection(0);
        edt_coll_address.getText().clear();
        edt_coll_pincode.getText().clear();
        tv_coll_date.setText("");
        tv_coll_time.setText("");
        edt_barcode.getText().clear();
        edt_re_enter_barcode.getText().clear();
        samplecollectionponit.setText(Constants.setSCPmsg);

        isgetDOB = false;

        l_pincode.clear();
        getAddressList = null;

        if (by_missed.isChecked() || by_sendsms.isChecked()) {
            btn_generate.setText(getResources().getString(R.string.enterccc));
        } else {
            btn_generate.setText(getResources().getString(R.string.btngenerateccc));
        }

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

        timerflag = false;
        verifyotp = false;
        presc_file = null;
        aadhar_file = null;
        aadhar_file1 = null;
        vial_file = null;
        other_file = null;
        other_file1 = null;
        selfie_file = null;
        selfie_flag = 0;

        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        lin_selfie.setVisibility(View.GONE);


        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
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
        viallist.clear();
        otherlist.clear();
        selfielist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
    }

    private boolean amtvalidation() {
        try {
            if (GlobalClass.isNull(edt_amt.getText().toString())) {
                return false;
            }
            if (GlobalClass.isNull(edt_amt.getText().toString())) {
                return false;
            }
            if (!GlobalClass.isNull(b2c) && Integer.parseInt(edt_amt.getText().toString()) > Integer.parseInt(b2c)) {
                GlobalClass.showCustomToast(activity, "You cannot enter collected amount more than " + b2c, 0);
                return false;
            } else if (!GlobalClass.isNull(b2b) && Integer.parseInt(edt_amt.getText().toString()) < Integer.parseInt(b2b)) {
                GlobalClass.showCustomToast(activity, "You cannot enter collected amount less than " + b2b, 0);
                return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

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

        if (btn_barcd.getText().toString().equalsIgnoreCase("BARCODE*")) {
            Global.showCustomToast(getActivity(), "Enter Barcode");
            return false;
        }

        if (spr_test.getSelectedItem().toString().equalsIgnoreCase("Select Test*")) {
            Global.showCustomToast(getActivity(), "Kindly Select Test");
            return false;
        }
        return true;
    }

    private void mobileverify(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);
        CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
        coVerifyMobReq.setApi_key(apikey);
        coVerifyMobReq.setMobile(mobileno);
        coVerifyMobReq.setScode(usercode);
        final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInteface.covmobileVerification(coVerifyMobReq);
        covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
            @Override
            public void onResponse(Call<COVerifyMobileResponse> call, Response<COVerifyMobileResponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
                        disablefields();
                    } else if (response.body().getResId().equalsIgnoreCase(Constants.RES0082)) {
                        GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
                    } else {
                        if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                            generateOtP(edt_missed_mobile.getText().toString());
                        } else {
                            GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
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
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
                if (items[item].equals("Choose from Library")) {
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
        Uri imageUri = activity.getContentResolver().insert(
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
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                if (result.getContents().length() == 8) {
                    verifyBarcode(result.getContents());
                } else {
                    Global.showCustomToast(getActivity(), "Invalid Barcode");
                }
            } else {
                Global.showCustomToast(getActivity(), "Something Went Wrong");
            }
        } else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                if (iscamera) {
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
                    selfielist.add(selfie_file.toString());
                } else if (ispresciption) {
                    presc_file = new File(camera.getCameraBitmapPath());
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
                        aadhar_file1 = new File(camera.getCameraBitmapPath());
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file1.toString());
                        CallAadharImageOCR(aadhar_file1);
                    } else {
                        aadhar_file = new File(camera.getCameraBitmapPath());
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
                } else if (isvial) {
                    vial_file = new File(camera.getCameraBitmapPath());
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
                        other_file1 = new File(camera.getCameraBitmapPath());

                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file1.toString());
                    } else {
                        other_file = new File(camera.getCameraBitmapPath());
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
                    buttonval();
                    lin_other_images.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (isvial) {
                    if (data.getData() != null) {
                        vial_file = ImagePicker.Companion.getFile(data);
//                        vial_file = FileUtil.from(activity, data.getData());
                    }
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showCustomToast(activity, "Failed to load image!", 0);
                return;
            }
            try {
                if (ispresciption) {
                    if (data.getData() != null) {
                        presc_file = FileUtil.from(activity, data.getData());
                    }
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
                } else if (isvial) {
                    if (data.getData() != null) {
                        vial_file = FileUtil.from(activity, data.getData());
                    }
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
                GlobalClass.showCustomToast(activity, "Failed to read image data!", 0);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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
        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            return false;
        }
        if (GlobalClass.isNull(edt_srfid.getText().toString())) {
            return false;
        }

        if (edt_srfid.getText().length() < 10) {
            return false;
        }

        if (GlobalClass.isNull(edt_age.getText().toString())) {
            return false;
        }
        if (GlobalClass.isNull(gender)) {
            return false;
        }
        if (GlobalClass.isNull(edt_patient_address.getText().toString())) {
            return false;
        }
        if (edt_patient_address.getText().toString().length() < 25) {
            return false;
        }
        if (GlobalClass.isNull(edt_patient_pincode.getText().toString())) {
            return false;
        }
        if (edt_patient_pincode.getText().toString().length() < 6) {
            return false;
        }
        if (edt_patient_pincode.getText().toString().startsWith("0") || edt_patient_pincode.getText().toString().startsWith("9")) {
            return false;
        }
        if (spn_scp.getSelectedItemPosition() == 0) {
            return false;
        }
        if (GlobalClass.isNull(edt_coll_address.getText().toString())) {
            return false;
        }
        if (edt_coll_address.getText().toString().length() < 25) {
            return false;
        }
        if (GlobalClass.isNull(edt_coll_pincode.getText().toString())) {
            return false;
        }
        if (edt_coll_pincode.getText().toString().length() < 6) {
            return false;
        }
        if (edt_coll_pincode.getText().toString().startsWith("0") || edt_coll_pincode.getText().toString().startsWith("9")) {
            return false;
        }

        if (samplecollectionponit.getText().toString().trim().equalsIgnoreCase(Constants.setSCPmsg)) {
            Toast.makeText(activity, ToastFile.slt_technicain, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (GlobalClass.isNull(selDate)) {
            return false;
        }
        if (GlobalClass.isNull(selTime)) {
            return false;
        }
       /* if (GlobalClass.isNull(edt_barcode.getText().toString())) {
            return false;
        }
        if (edt_barcode.getText().toString().length() < 8) {
            return false;
        }
        if (GlobalClass.isNull(edt_re_enter_barcode.getText().toString())) {
            return false;
        }
        if (edt_re_enter_barcode.getText().toString().length() < 8) {
            return false;
        }
        if (!edt_barcode.getText().toString().trim().equalsIgnoreCase(edt_re_enter_barcode.getText().toString().trim())) {
            return false;
        }*/
        if (vial_file == null) {
            return false;
        }

        return true;
    }

    private void setCountDownTimer() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            final NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (!timerflag) {
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
        btn_selfie.setEnabled(true);
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
        selfie_file = null;
        selfie_flag = 0;
        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);
        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        lin_selfie.setVisibility(View.GONE);
        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);
        txt_selfie.setVisibility(View.VISIBLE);
        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

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
        viallist.clear();
        otherlist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
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
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);

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
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
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
                    GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
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
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);
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
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
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

    public void getUploadResponse(String response,String mobile) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");
            if (!GlobalClass.isNull(RESPONSEID) && RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                new LogUserActivityTagging(activity,"WOE-SRF(COVID)",mobile);
                Global.showCustomToast(activity, RESPONSE);
                Constants.SRFcovidWOEfrag_flag = "1";
                clearonreset();
                Intent i = new Intent(getActivity(), SRFCovidWOEMainActivity.class);
                startActivity(i);
            } else {
                Global.showCustomToast(activity, RESPONSE);
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
        //maindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = maindialog.findViewById(R.id.viewPager);
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

        ImageView ic_close = maindialog.findViewById(R.id.img_close);
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

        final SCollectionPAdapter sampleCollectionAdapter = new SCollectionPAdapter(fragment, activity, labDetailsArrayList);
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
                ControllersGlobalInitialiser.getSCPSRFAPIController = new getSCPSRFAPIController(fragment, activity);
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
                        setGender(lineText);
                        setDOB(lineText);

                        setAddressToList(lineText);
                        setPinCode(lineText);

                        setPassPortCode(lineText);
                        setPassportGender(lineText);

                    }
                }

                if (edt_patient_address.getText().toString().trim().equalsIgnoreCase("")) {
                    setAddresstoView(edt_patient_address, getAddressList);
                }
                if (edt_patient_pincode.getText().toString().trim().equalsIgnoreCase("")) {
                    edt_patient_pincode.setText(getPincodeFromList());
                }

                getLastNameFromFinalLastString(result.getTextBlocks());
                getFirstNameFromFinalLastString(result.getTextBlocks());
                getPassPortDOBDate(result.getTextBlocks(), getPassPortFromList());
                // [END mlkit_process_text_block]
            } else {
//                Toast.makeText(activity, "Please select proper image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPincodeFromList() {
        String st = "";
        if (l_pincode != null) {
            for (int i = 0; i < l_pincode.size(); i++) {
                st = st + l_pincode.get(0).toString().trim();
                return st;
            }
        }
        return st;
    }

    private void setAddresstoView(TextView txt_address, ArrayList<String> getAddressList) {
        String st = "";
        if (getAddressList != null) {
            if (getAddressList.size() != 0) {
                for (int i = 0; i < getAddressList.size(); i++) {
                    st = st + getAddressList.get(i).toString().trim() + ",";
                }
            }
        }

        st = Constants.removelastchar(st, 1);
        st = st.replace("Address", "");
        txt_address.setText(st);
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

    private void setGender(String lineText) {
        try {
            if (lineText.contains("MALE") || lineText.contains("Male")) {
                maleBtnClick();
            } else if (lineText.contains("FEMALE") || lineText.contains("Female")) {
                femaleBtnClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDOB(String lineText) {
        if (lineText.contains("DOB") || lineText.contains("D0B")) {
            if (getBirthdate(lineText).toString().trim().length() != 0) {
                setDate(getBirthdate(lineText).toString().trim());
            }
        } else if (lineText.contains("Year Of Birth") || lineText.contains("Year of Birth")) {
            if (getBirthYear(lineText).toString().trim().length() != 0) {
                setDateFromYear(getBirthYear(lineText).toString().trim());
            }
        }
    }

    private void setDate(String datetext) {
        try {
            if (isValidDateFormat(datetext)) {
                if (!isgetDOB) {
                    isgetDOB = true;
                    CalCulateAgeFromDate(datetext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDateFromYear(String datetext) {
        try {
            if (!isgetDOB) {
                isgetDOB = true;
                CalCulateAgeFromYear(datetext);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalCulateAgeFromDate(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "yyyy")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "MM")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "dd")));
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CalCulateAgeFromYear(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyyy", "yyyy")), 1, 1);
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String mAadhardateFormat = "dd/MM/yyyy";

    private boolean isValidDateFormat(String datetext) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(mAadhardateFormat);
        try {
            Date javaDate = sdfrmt.parse(datetext);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static String getBirthdate(String str) {
        String st = "";
//        Pattern p = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
        Pattern p = Pattern.compile("(^1[01]|[12][0-9]|0[1-9])/(3[0-2]|0[1-9])/[0-9]{4}$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            st = "" + m.group(0);
        }
        return st;
    }

    public static String getBirthYear(String str) {
        Pattern p = Pattern.compile("[0-9]{4}$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return "" + m.group(0);
        }
        return "";
    }

    private void setAddressToList(String lineText) {
        System.out.println("Nitya >> Add >> " + lineText);
        if (lineText.toString().trim().contains("Address")) {
            isCapturedAddress = true;
            if (isCapturedAddress) {
                if (getAddressList != null) {
                    getAddressList = null;
                }
                getAddressList = new ArrayList<>();
                if (!getAddressList.contains(lineText))
                    getAddressList.add(lineText);
            }
        }

        if (isCapturedAddress) {
            if (!getAddressList.contains(lineText))
                getAddressList.add(lineText);
            if (getPincode(lineText).toString().trim().length() == 6) {
                isCapturedAddress = false;
            }
        }
    }

    private void setPinCode(String lineText) {
        if (getPincode(lineText).toString().trim().length() == 6) {
            if (!l_pincode.contains(getPincode(lineText)))
                l_pincode.add(getPincode(lineText));
        }
    }

    public static String getPincode(String str) {
        try {
            String[] splited = str.split("\\s+");
            if (splited.length != 0) {
                for (int i = 0; i < splited.length; i++) {
                    if (splited[i].toString().trim().length() == 6) {
                        Pattern p = Pattern.compile("(|^)\\d{6}");
                        Matcher m = p.matcher(splited[i].toString().trim());
                        if (m.find()) {
                            return "" + m.group(0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
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

    ArrayList<String> l_PassPortcard = new ArrayList<>();

    private void setPassPortCode(String lineText) {
        try {
            if (isValidPassPortNumber(lineText)) {
                if (!l_PassPortcard.contains(getPassPortCode(lineText)))
                    l_PassPortcard.add(getPassPortCode(lineText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String getPassPortFromList() {
        String st = "";
        try {
            if (l_PassPortcard != null) {
                for (int i = 0; i < l_PassPortcard.size(); i++) {
                    st = st + l_PassPortcard.get(0).toString().trim();
                    return st;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    private void getPassPortDOBDate(List<Text.TextBlock> allText, String passPorttext) {
        try {
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.contains(passPorttext.toString().trim())) {
                            if (stsd.length() > 9) {
                                System.out.println("Nitya == " + stsd);
                                getDateDateFromString(stsd.replaceAll("\\s+", ""));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDateDateFromString(String dkgdgd) {
        try {
            int speciallchar = 0;
            if (dkgdgd != null) {
                for (int i = 0; i < dkgdgd.length(); i++) {
                    if (dkgdgd.charAt(i) == '<') {
                        System.out.println(i);
                        speciallchar = i;
                        break;
                    }
                }
                if (speciallchar != 0) {
                    String sdgsdg = dkgdgd.substring(speciallchar + 5, speciallchar + 5 + 6);
                    System.out.println(">>" + sdgsdg);
                    setDOBFinalDate(sdgsdg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDOBFinalDate(String str) {
        try {
            if (str != null) {
                CalCulateAgeFromPassDate(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPassportGender(String lineText) {
        try {
            if (lineText.toString().trim().equals("F") || lineText.toString().trim().equals("f")) {
                femaleBtnClick();
            } else if (lineText.toString().trim().equals("M") || lineText.toString().trim().equals("m")) {
                maleBtnClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalCulateAgeFromPassDate(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "yyyy")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "MM")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "dd")));
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void femaleBtnClick() {
        genderId = !genderId;
        gender = "F";
        female_red.setVisibility(View.VISIBLE);
        male.setVisibility(View.VISIBLE);
        male_red.setVisibility(View.GONE);
        female.setVisibility(View.GONE);
        buttonval();
    }

    private void maleBtnClick() {
        genderId = !genderId;
        gender = "M";
        male_red.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
        female_red.setVisibility(View.GONE);
        male.setVisibility(View.GONE);
        buttonval();
    }

}