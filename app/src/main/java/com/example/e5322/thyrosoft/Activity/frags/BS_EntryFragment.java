package com.example.e5322.thyrosoft.Activity.frags;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.Blood_sugar_entry_activity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.CommonItils.GPSTracker;
import com.example.e5322.thyrosoft.Controller.AsyncTaskPost_Multipartfile;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.EmailValidationController;
import com.example.e5322.thyrosoft.Controller.GETValidateBSOTPController;
import com.example.e5322.thyrosoft.Controller.GetOTPController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.OTPtoken_controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.Models.BS_POSTDataModel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.RequestModels.GenerateOTPRequestModel;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BS_EntryFragment extends Fragment {
    public static String OTPAPPID = "9";
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
    Spinner spin_bs_test, spin_coll_amt;
    EditText mobile_edt, edt_otp, edt_name, edt_val, edt_age, edt_email, edt_collamount, edt_pincode, edt_systolic, edt_diastolic;
    Button btn_send_otp, btn_verify_otp, btn_choose_file, btn_submit_bs;
    TextView txt_ref_msg, tvUploadImageText, tv_timer, tv_resendotp;
    ImageView male, male_red, female, female_red, correct_img;
    LinearLayout choose_file_ll, ll_detailsView, ll_refRange, ll_OTP;
    int minValue = 0, maxValue = 0;
    Activity activity;
    double latitude = 0.0, longitude = 0.0;
    Camera camera;
    private Global global;
    private String mobile_number;
    private String TAG = BS_EntryFragment.class.getSimpleName();
    private boolean validMobileNumber;
    private String gender = "";
    private ConnectionDetector cd;
    private File imageFile = null;
    private String range;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private String userChoosenTask;
    private GPSTracker gpsTracker;
    private SharedPreferences prefs;
    private CountDownTimer countDownTimer;
     String user, passwrd, access, api_key, USER_CODE;

    public BS_EntryFragment() {
        // Required empty public constructor
    }

    public static boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewMain = (View) inflater.inflate(R.layout.bs_entry_fragment, container, false);

        activity = getActivity();
        cd = new ConnectionDetector(activity);

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        USER_CODE = prefs.getString("USER_CODE", "");

        initUI(viewMain);

        ArrayAdapter<BSTestDataModel> adap = new ArrayAdapter<>(activity, R.layout.name_age_spinner, GlobalClass.getTestList());
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_bs_test.setAdapter(adap);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(activity, R.layout.name_age_spinner, GlobalClass.getCollAmount());
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_coll_amt.setAdapter(stringArrayAdapter);

        edt_val.setVisibility(View.INVISIBLE);
        ll_refRange.setVisibility(View.GONE);

        int maxLength = 10;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        mobile_edt.setFilters(FilterArray);

        initListeners();

        ll_detailsView.setVisibility(View.GONE);

        return viewMain;
    }

    private void initUI(View view) {
        spin_bs_test = (Spinner) view.findViewById(R.id.spin_bs_test);
        spin_coll_amt = (Spinner) view.findViewById(R.id.spin_coll_amt);
        mobile_edt = (EditText) view.findViewById(R.id.edt_mobile);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_val = (EditText) view.findViewById(R.id.edt_val);
        edt_age = (EditText) view.findViewById(R.id.edt_age);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_collamount = (EditText) view.findViewById(R.id.edt_collamount);
        edt_pincode = (EditText) view.findViewById(R.id.edt_pincode);
        edt_systolic = (EditText) view.findViewById(R.id.edt_systolic);
        edt_diastolic = (EditText) view.findViewById(R.id.edt_diastolic);
        btn_send_otp = (Button) view.findViewById(R.id.btn_send_otp);
        btn_verify_otp = (Button) view.findViewById(R.id.btn_verify_otp);
        btn_choose_file = (Button) view.findViewById(R.id.btn_choose_file);
        btn_submit_bs = (Button) view.findViewById(R.id.btn_submit_bs);
        txt_ref_msg = (TextView) view.findViewById(R.id.txt_ref_msg);
        tvUploadImageText = (TextView) view.findViewById(R.id.tvUploadImageText);
        male = (ImageView) view.findViewById(R.id.male);
        male_red = (ImageView) view.findViewById(R.id.male_red);
        female = (ImageView) view.findViewById(R.id.female);
        female_red = (ImageView) view.findViewById(R.id.female_red);
        correct_img = (ImageView) view.findViewById(R.id.correct_img);
        choose_file_ll = (LinearLayout) view.findViewById(R.id.choose_file_ll);
        ll_detailsView = (LinearLayout) view.findViewById(R.id.ll_detailsView);
        ll_refRange = (LinearLayout) view.findViewById(R.id.ll_refRange);
        ll_OTP = (LinearLayout) view.findViewById(R.id.ll_OTP);
        tv_timer = (TextView) view.findViewById(R.id.tv_timer);
        tv_resendotp = (TextView) view.findViewById(R.id.tv_resendotp);
        edt_otp = (EditText) view.findViewById(R.id.edt_otp);
    }

    private void initListeners() {
        mobile_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    GlobalClass.SetEditText(mobile_edt, enteredString.substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        edt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.startsWith("0") || str.startsWith("9")) {
                    GlobalClass.SetEditText(edt_pincode, str.substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = btn_send_otp.getText().toString();
                if (!GlobalClass.isNull(btnText) && btnText.equalsIgnoreCase(getString(R.string.send_otp))) {
                    mobile_number = mobile_edt.getText().toString();
                    if ((mobile_number.length() > 0) && (mobile_number.length() < 10)) {
                        GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
                        validMobileNumber = false;
                    } else if (mobile_number.length() == 0) {
                        GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_MOBILE, 2);
                        validMobileNumber = false;
                    } else {
                        generateToken();

                    }
                } else if (!GlobalClass.isNull(btnText) &&  btnText.equalsIgnoreCase(getString(R.string.reset))) {
                    mobile_edt.setEnabled(true);
                    mobile_edt.requestFocus();
                    GlobalClass.SetButtonText(btn_send_otp, activity.getString(R.string.send_otp));
                    ll_OTP.setVisibility(View.GONE);
                    tv_timer.setVisibility(View.GONE);
                    tv_resendotp.setVisibility(View.GONE);
                    ll_detailsView.setVisibility(View.GONE);
                    resetAll();
                }
            }
        });

        edt_name.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxLength = 50;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edt_name.setFilters(FilterArray);

        edt_name.addTextChangedListener(new TextWatcher() {
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
                        GlobalClass.SetEditText(edt_name, "");
                    } else {
                        GlobalClass.SetEditText(edt_name, enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                        GlobalClass.SetEditText(edt_age, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_age, "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 0) {
                    int result = Integer.parseInt(enteredString);
                    if (result > 200) {
                        GlobalClass.SetEditText(edt_age, enteredString.substring(0, 2));
                        GlobalClass.SetEditText(edt_age, "");
                    }
                }
            }
        });

        edt_val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_val, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_val, "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setVisibility(View.GONE);
                male_red.setVisibility(View.VISIBLE);
                female.setVisibility(View.VISIBLE);
                female_red.setVisibility(View.GONE);
                gender = "M";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setVisibility(View.GONE);
                female_red.setVisibility(View.VISIBLE);
                male.setVisibility(View.VISIBLE);
                male_red.setVisibility(View.GONE);
                gender = "F";
            }
        });

        spin_bs_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    GlobalClass.SetText(txt_ref_msg, "");
                    edt_val.setVisibility(View.INVISIBLE);
                    ll_refRange.setVisibility(View.GONE);
                } else {
                    edt_val.setVisibility(View.VISIBLE);
                    ll_refRange.setVisibility(View.VISIBLE);
                    minValue = GlobalClass.getTestList().get(position).getMinVal();
                    maxValue = GlobalClass.getTestList().get(position).getMaxVal();
                    GlobalClass.SetText(txt_ref_msg, "Ref. Range: " + GlobalClass.getTestList().get(position).getRangeVal());
                    range = GlobalClass.getTestList().get(position).getRangeVal();
                    GlobalClass.SetEditText(edt_val, "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessRuntimePermissions.checkcameraPermission(getActivity())
                        && AccessRuntimePermissions.checkExternalPerm(getActivity())) {
                    selectImage();
                } else {
                    AccessRuntimePermissions.requestCamerapermission(getActivity());
                    AccessRuntimePermissions.requestExternalpermission(getActivity());
                }
            }
        });

        tvUploadImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile != null && imageFile.exists())
                    GlobalClass.showImageDialog(activity, imageFile, "", 1);
                else
                    GlobalClass.showTastyToast(activity, MessageConstants.Image_not_found, 2);
            }
        });

        btn_submit_bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (AccessRuntimePermissions.checkLocationPerm(activity)) {
                        if (edt_email.getText().length() > 0) {
                            try {
                                if (ControllersGlobalInitialiser.emailValidationController != null) {
                                    ControllersGlobalInitialiser.emailValidationController = null;
                                }
                                ControllersGlobalInitialiser.emailValidationController = new EmailValidationController(BS_EntryFragment.this, activity);
                                ControllersGlobalInitialiser.emailValidationController.emailvalidationapi(edt_email.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (cd.isConnectingToInternet()) {
                                callPOSTAPI();
                            } else {
                                GlobalClass.showTastyToast(activity, MessageConstants.CHECK_INTERNET_CONN, 2);
                            }
                        }
                    } else {
                        AccessRuntimePermissions.requestLocationPerm(activity);
                    }
                }
            }
        });

        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null)
                    countDownTimer.cancel();
                GlobalClass.SetEditText(edt_otp, "");
                mobile_number = mobile_edt.getText().toString();
                if (mobile_number.length() < 10) {
                    GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
                } else {
                    generateToken();
                }
            }
        });

        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number = mobile_edt.getText().toString().trim();
                String otp = edt_otp.getText().toString().trim();
                if (otp.length() < 4) {
                    GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_VALID_OTP, 2);
                } else {
                    callValidateOTP(mobile_number, otp);
                }
            }
        });
    }

    private void callGenerateOTP(String mobile_number, String token, String requestId) {
        JSONObject jsonObject = null;
        try {
            GenerateOTPRequestModel requestModel = new GenerateOTPRequestModel();
            requestModel.setApi_key(Constants.GENRATE_OTP_API_KEY);
            requestModel.setMobile(mobile_number);
            requestModel.setType("WOEROUTINE");
            requestModel.setAccessToken(token);
            requestModel.setReqId(requestId);

            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (cd.isConnectingToInternet()) {
            try {
                if (ControllersGlobalInitialiser.getOTPController != null) {
                    ControllersGlobalInitialiser.getOTPController = null;
                }
                ControllersGlobalInitialiser.getOTPController = new GetOTPController(activity, BS_EntryFragment.this);
                ControllersGlobalInitialiser.getOTPController.GetOTP(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlobalClass.showTastyToast(activity, ToastFile.intConnection, 2);
        }
    }

    public void getSendOTPResponse(JSONObject response) {
        String RESPONSE = response.optString("RESPONSE", "");
        String RES_ID = response.optString("RES_ID", "");

        if (!GlobalClass.isNull(RES_ID) && RES_ID.equalsIgnoreCase(Constants.RES0000)) {
            mobile_edt.setEnabled(false);
            GlobalClass.showTastyToast(activity, ToastFile.OTP_SENT_SUCCESS, 1);
            ll_OTP.setVisibility(View.VISIBLE);
            GlobalClass.SetButtonText(btn_verify_otp, activity.getString(R.string.verify));
            btn_send_otp.setEnabled(false);
            setCountDownTimer();
        } else
            GlobalClass.showTastyToast(activity, RESPONSE, 2);
    }

    private void setCountDownTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                tv_timer.setVisibility(View.VISIBLE);
                tv_resendotp.setVisibility(View.INVISIBLE);
                GlobalClass.SetText(tv_timer, "Please wait 00:" + numberFormat.format(time));
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                tv_resendotp.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void callValidateOTP(String mobile_number, String otp) {
        if (cd.isConnectingToInternet()) {
            try {
                if (ControllersGlobalInitialiser.getValidateBSOTPController != null) {
                    ControllersGlobalInitialiser.getValidateBSOTPController = null;
                }
                ControllersGlobalInitialiser.getValidateBSOTPController = new GETValidateBSOTPController(activity, BS_EntryFragment.this);
                ControllersGlobalInitialiser.getValidateBSOTPController.validateOTP(mobile_number, otp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlobalClass.showTastyToast(activity, ToastFile.intConnection, 2);
        }
    }

    public void getValidateOTPResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response", "");
            String RES_ID = jsonObject.optString("ResId", "");
            if (!GlobalClass.isNull(RES_ID) && RES_ID.equalsIgnoreCase(Constants.RES0000)) {
                GlobalClass.showTastyToast(activity, RESPONSE, 1);
                validMobileNumber = true;
                countDownTimer.cancel();
                tv_timer.setVisibility(View.GONE);
                tv_resendotp.setVisibility(View.GONE);
                edt_otp.setEnabled(false);
                btn_verify_otp.setEnabled(false);
                GlobalClass.SetButtonText(btn_verify_otp, activity.getString(R.string.verified));
                btn_send_otp.setEnabled(true);
                GlobalClass.SetButtonText(btn_send_otp, activity.getString(R.string.reset));
                ll_detailsView.setVisibility(View.VISIBLE);
            } else {
                GlobalClass.showTastyToast(activity, RESPONSE, 2);
                ll_detailsView.setVisibility(View.GONE);
                validMobileNumber = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callPOSTAPI() {
        String sbpValue = edt_systolic.getText().toString().trim();
        String dbpValue = edt_diastolic.getText().toString().trim();

        if (GlobalClass.isNull(sbpValue) || GlobalClass.isNull(dbpValue)) {
            sbpValue = "";
            dbpValue = "";
        }

        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e(TAG, "Lat: " + latitude + " Long: " + longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }

        BS_POSTDataModel bs_postDataModel = new BS_POSTDataModel();
        bs_postDataModel.setUser(user);
        bs_postDataModel.setMobile(mobile_edt.getText().toString().trim());
        bs_postDataModel.setLandLine("");
        bs_postDataModel.setName(edt_name.getText().toString().trim());
        bs_postDataModel.setAge(edt_age.getText().toString().trim());
        bs_postDataModel.setValue(edt_val.getText().toString().trim());
        bs_postDataModel.setTest(spin_bs_test.getSelectedItem().toString().trim());
        bs_postDataModel.setEmail_id(edt_email.getText().toString().trim());
        bs_postDataModel.setGender(gender);
        bs_postDataModel.setRange(range);
        bs_postDataModel.setFile(imageFile);
        bs_postDataModel.setLatitude(String.valueOf(new DecimalFormat("##.##########").format(latitude)));
        bs_postDataModel.setLongitude(String.valueOf(new DecimalFormat("##.##########").format(longitude)));
        bs_postDataModel.setCollAmount(spin_coll_amt.getSelectedItem().toString().trim());
        bs_postDataModel.setPincode(edt_pincode.getText().toString().trim());
        bs_postDataModel.setSBP(sbpValue);
        bs_postDataModel.setDBP(dbpValue);

        if (!GlobalClass.isNull(bs_postDataModel.getLatitude()) && !GlobalClass.isNull(bs_postDataModel.getLongitude())) {
            if (!bs_postDataModel.getLatitude().equalsIgnoreCase("0.0") && !bs_postDataModel.getLongitude().equalsIgnoreCase("0.0")) {
                if (cd.isConnectingToInternet())
                    new AsyncTaskPost_Multipartfile(BS_EntryFragment.this, activity, bs_postDataModel).execute();
                else
                    GlobalClass.showTastyToast(activity, ToastFile.intConnection, 2);
            } else
                GlobalClass.showTastyToast(activity, ToastFile.LOCATION_NOT_FOUND, 2);
        } else
            GlobalClass.showTastyToast(activity, ToastFile.LOCATION_NOT_FOUND, 2);
    }


    private void selectImage() {
        CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Upload Image !");
        final CharSequence[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (finalItems[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    openCamera();
                } else if (finalItems[item].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
                    chooseFromGallery();
                } else if (finalItems[item].equals("Cancel")) {
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
                .setDirectory("/Uploads/Images")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                String imageurl = camera.getCameraBitmapPath();
                imageFile = new File(imageurl);
                Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(imageFile.length())));
                if (imageFile != null && imageFile.exists()) {
                    correct_img.setVisibility(View.VISIBLE);
                    GlobalClass.SetButtonText(btn_choose_file, getString(R.string.re_upload));
                    tvUploadImageText.setHint(getString(R.string.image_uploaded));
                    tvUploadImageText.setPaintFlags(tvUploadImageText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    correct_img.setVisibility(View.GONE);
                    GlobalClass.SetButtonText(btn_choose_file, getString(R.string.choose_file));
                    tvUploadImageText.setHint(getString(R.string.img_upload));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showTastyToast(activity, MessageConstants.Failedto_load_image, 2);
                return;
            }
            try {
                imageFile = FileUtil.from(activity, data.getData());
                Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(imageFile.length())));
                imageFile = GlobalClass.getCompressedFile(activity, imageFile);
                Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(imageFile.length())));
                if (imageFile != null && imageFile.exists()) {
                    correct_img.setVisibility(View.VISIBLE);
                    GlobalClass.SetButtonText(btn_choose_file, getString(R.string.re_upload));
                    tvUploadImageText.setHint(getString(R.string.image_uploaded));
                    tvUploadImageText.setPaintFlags(tvUploadImageText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    correct_img.setVisibility(View.GONE);
                    GlobalClass.SetButtonText(btn_choose_file, getString(R.string.choose_file));
                    tvUploadImageText.setHint(getString(R.string.img_upload));
                }
            } catch (IOException e) {
                GlobalClass.showTastyToast(activity, MessageConstants.Failed_to_read_img, 2);
                e.printStackTrace();
            }
        }
    }

    private boolean Validation() {
        if (mobile_edt.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_MOBILE, 2);
            mobile_edt.requestFocus();
            return false;
        }
        if (mobile_edt.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
            mobile_edt.requestFocus();
            return false;
        }
        if (mobile_edt.getText().toString().length() > 10) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.MOBILE_10_DIGITS, 2);
            mobile_edt.requestFocus();
            return false;
        }
        if (!validMobileNumber) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.MOB_NOT_VERIFIED, 2);
            mobile_edt.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_NAME, 2);
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_NAME, 2);
            edt_name.requestFocus();
            return false;
        }
        if (edt_age.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_AGE, 2);
            edt_age.requestFocus();
            return false;
        }

        if (GlobalClass.isNull(gender)) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_GENDER, 2);
            return false;
        }
        if (spin_bs_test.getSelectedItem().toString().equals("Select")) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_TEST_TYPE, 2);
            return false;
        }
        if (edt_val.getText().toString().equals("")) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_VALUE, 2);
            edt_val.requestFocus();
            return false;
        }
        if (edt_systolic.getText().toString().length() > 0) {
            if (edt_diastolic.getText().toString().isEmpty()) {
                GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_DIASTOLIC_VALUE, 2);
                edt_diastolic.requestFocus();
                return false;
            }
        }
        if (edt_diastolic.getText().toString().length() > 0) {
            if (edt_systolic.getText().toString().isEmpty()) {
                GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_SYSTOLIC_VALUE, 2);
                edt_systolic.requestFocus();
                return false;
            }
        }
        if (correct_img.getVisibility() == View.GONE) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.UPLOAD_IMAGE, 2);
            return false;
        }

        if (imageFile == null && !imageFile.exists()) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_IMAGE, 2);
            return false;
        }
        if (spin_coll_amt.getSelectedItem().toString().equals("Select collected amount")) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.SELECT_COLL_AMT, 2);
            return false;
        }
        if (edt_pincode.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_PINCODE, 2);
            edt_pincode.requestFocus();
            return false;
        }
        if (edt_pincode.getText().toString().length() < 6) {
            GlobalClass.showTastyToast(getActivity(), ToastFile.PINCODE_6_DIGITS, 2);
            edt_pincode.requestFocus();
            return false;
        }
        if (edt_email.getText().toString().length() > 0) {
            if (edt_email.getText().toString().length() == 0 || edt_email.getText().toString().isEmpty()) {
                GlobalClass.showTastyToast(getActivity(), ToastFile.ENTER_EMAIL, 2);
                edt_email.requestFocus();
                return false;
            } else if (!emailValidation(edt_email.getText().toString())) {
                GlobalClass.showTastyToast(getActivity(), ToastFile.VALID_EMAIL, 2);
                edt_email.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void showOKDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("RESPONSE");
            String RESPONSEID = jsonObject.optString("RESPONSEID");

            if (!GlobalClass.isNull(RESPONSEID) && RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                GlobalClass.showTastyToast(activity, RESPONSE, 1);
                Intent intent = new Intent(activity, Blood_sugar_entry_activity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                showOKDialog(RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resetAll() {
        GlobalClass.SetButtonText(btn_verify_otp, activity.getString(R.string.verify));
        edt_otp.setEnabled(true);
        btn_verify_otp.setEnabled(true);
        GlobalClass.SetEditText(mobile_edt, "");

        GlobalClass.SetEditText(edt_otp, "");
        GlobalClass.SetEditText(edt_name, "");
        GlobalClass.SetEditText(edt_age, "");
        spin_bs_test.setSelection(0);
        spin_coll_amt.setSelection(0);

        GlobalClass.SetEditText(edt_val, "");
        GlobalClass.SetEditText(edt_systolic, "");
        GlobalClass.SetEditText(edt_diastolic, "");

        imageFile = null;

        GlobalClass.SetEditText(edt_email, "");
        GlobalClass.SetEditText(edt_pincode, "");

        gender = "";
        male.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
        male_red.setVisibility(View.GONE);
        female_red.setVisibility(View.GONE);
        correct_img.setVisibility(View.GONE);
        GlobalClass.SetButtonText(btn_choose_file, getString(R.string.choose_file));
        tvUploadImageText.setHint(getString(R.string.img_upload));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (camera != null)
                camera.deleteImage();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageFile != null)
                imageFile = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateToken() {
        try {
            if (ControllersGlobalInitialiser.otPtoken_controller != null) {
                ControllersGlobalInitialiser.otPtoken_controller = null;
            }
            ControllersGlobalInitialiser.otPtoken_controller = new OTPtoken_controller(activity, BS_EntryFragment.this);
            ControllersGlobalInitialiser.otPtoken_controller.getotptokencontroller();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getemailresponse() {
        callPOSTAPI();
    }

    public void getotptokenResponse(Response<Tokenresponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getRespId()) && response.body().getRespId().equalsIgnoreCase(Constants.RES0000)) {
                if (!GlobalClass.isNull(response.body().getToken())) {
                    Log.e(TAG, "TOKEN--->" + response.body().getToken());
                    callGenerateOTP(mobile_number, response.body().getToken(), response.body().getRequestId());
                }
            } else {
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}