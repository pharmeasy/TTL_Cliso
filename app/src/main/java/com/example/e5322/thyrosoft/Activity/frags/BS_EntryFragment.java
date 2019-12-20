package com.example.e5322.thyrosoft.Activity.frags;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.e5322.thyrosoft.CommonItils.GPSTracker;
import com.example.e5322.thyrosoft.Controller.AsyncTaskPost_Multipartfile;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GETValidateBSOTPController;
import com.example.e5322.thyrosoft.Controller.GetOTPController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.Models.BS_POSTDataModel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.RequestModels.GenerateOTPRequestModel;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BS_EntryFragment extends Fragment {
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
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private String userChoosenTask;
    private GPSTracker gpsTracker;
    private int LOCATION_PERMISSION_REQUEST_CODE = 199;
    private SharedPreferences prefs;
    private CountDownTimer countDownTimer;
    private String user,passwrd,access,api_key,USER_CODE;

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
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        USER_CODE = prefs.getString("USER_CODE", null);

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
                    mobile_edt.setText(enteredString.substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*validMobileNumber = false;
                String enteredString = s.toString();
                if (enteredString.length() == 10) {
                    if (cd.isConnectingToInternet()) {
                        verifyMobileNumber(enteredString);
                    } else {
                        Global.showCustomToast(activity, ToastFile.intConnection);
                    }
                }*/
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
                    edt_pincode.setText(str.substring(1));
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
                if (btnText.equalsIgnoreCase(getString(R.string.send_otp))) {
                    mobile_number = mobile_edt.getText().toString();
                    if ((mobile_number.length() > 0) && (mobile_number.length() < 10)) {
                        Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
                        validMobileNumber = false;
                    } else if (mobile_number.length() == 0) {
                        Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
                        validMobileNumber = false;
                    } else {
                        if (cd.isConnectingToInternet()) {
                            callGenerateOTP(mobile_number);
                        } else {
                            Global.showCustomToast(activity, ToastFile.intConnection);
                        }
                    }
                } else if (btnText.equalsIgnoreCase(getString(R.string.reset))) {
                    mobile_edt.setEnabled(true);
                    mobile_edt.requestFocus();
                    btn_send_otp.setText(activity.getString(R.string.send_otp));
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
                        edt_name.setText("");
                    } else {
                        edt_name.setText(enteredString.substring(1));
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
                    if (result > 200) {
                        edt_age.setText(enteredString.substring(0, 2));
                        edt_age.setText("");
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
                        edt_val.setText(enteredString.substring(1));
                    } else {
                        edt_val.setText("");
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
                    txt_ref_msg.setText("");
                    edt_val.setVisibility(View.INVISIBLE);
                    ll_refRange.setVisibility(View.GONE);
                } else {
                    edt_val.setVisibility(View.VISIBLE);
                    ll_refRange.setVisibility(View.VISIBLE);
                    minValue = GlobalClass.getTestList().get(position).getMinVal();
                    maxValue = GlobalClass.getTestList().get(position).getMaxVal();
                    txt_ref_msg.setText("Ref. Range: " + GlobalClass.getTestList().get(position).getRangeVal());
                    range = GlobalClass.getTestList().get(position).getRangeVal();
                    edt_val.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage();
                } else {
                    requestPermission();
                }
            }
        });

        tvUploadImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile != null && imageFile.exists())
                    GlobalClass.showImageDialog(activity, imageFile, "", 1);
                else
                    Global.showCustomToast(activity, "Image not found");
            }
        });

        btn_submit_bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
//                    if (global.checkMultipleValidation(Integer.parseInt(edt_collamount.getText().toString()))) {
                    if (checkLocationPerm())
                        callPOSTAPI();
                    else
                        requestLocationPerm();
                    /*} else
                        Global.showCustomToast(activity, ToastFile.ENTER_VALID_AMOUNT);*/
                }
            }
        });

        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null)
                    countDownTimer.cancel();
                edt_otp.setText("");
                mobile_number = mobile_edt.getText().toString();
                if (mobile_number.length() < 10) {
                    Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
                } else {
                    if (cd.isConnectingToInternet()) {
                        callGenerateOTP(mobile_number);
                    } else {
                        Global.showCustomToast(activity, ToastFile.intConnection);
                    }
                }
            }
        });

        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number = mobile_edt.getText().toString().trim();
                String otp = edt_otp.getText().toString().trim();
                if (otp.length() < 4) {
                    Global.showCustomToast(getActivity(), ToastFile.ENTER_VALID_OTP);
                } else {
                    callValidateOTP(mobile_number, otp);
                }
            }
        });
    }

    private void callGenerateOTP(String mobile_number) {
        JSONObject jsonObject = null;
        try {
            GenerateOTPRequestModel requestModel = new GenerateOTPRequestModel();
            requestModel.setApi_key(Constants.GENRATE_OTP_API_KEY);
            requestModel.setMobile(mobile_number);
            requestModel.setType("SENDOTPALL");

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
            Global.showCustomToast(activity, ToastFile.intConnection);
        }
    }

    public void getSendOTPResponse(JSONObject response) {
        String RESPONSE = response.optString("RESPONSE", "");
        String RES_ID = response.optString("RES_ID", "");

        if (RES_ID.equalsIgnoreCase(Constants.RES0000)) {
            mobile_edt.setEnabled(false);
            Global.showCustomToast(activity, ToastFile.OTP_SENT_SUCCESS);
            ll_OTP.setVisibility(View.VISIBLE);
            btn_verify_otp.setText(activity.getString(R.string.verify));
            btn_send_otp.setEnabled(false);
            setCountDownTimer();
        } else
            Global.showCustomToast(activity, RESPONSE);
    }

    private void setCountDownTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                tv_timer.setVisibility(View.VISIBLE);
                tv_resendotp.setVisibility(View.INVISIBLE);
                tv_timer.setText("Please wait 00:" + numberFormat.format(time));
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
            Global.showCustomToast(activity, ToastFile.intConnection);
        }
    }

    public void getValidateOTPResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response", "");
            String RES_ID = jsonObject.optString("ResId", "");
            if (RES_ID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                validMobileNumber = true;
                countDownTimer.cancel();
                tv_timer.setVisibility(View.GONE);
                tv_resendotp.setVisibility(View.GONE);
                edt_otp.setEnabled(false);
                btn_verify_otp.setEnabled(false);
                btn_verify_otp.setText(activity.getString(R.string.verified));
                btn_send_otp.setEnabled(true);
                btn_send_otp.setText(activity.getString(R.string.reset));
                ll_detailsView.setVisibility(View.VISIBLE);
            } else {
                Global.showCustomToast(activity, RESPONSE);
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
                    Global.showCustomToast(activity, ToastFile.intConnection);
            } else
                Global.showCustomToast(activity, ToastFile.LOCATION_NOT_FOUND);
        } else
            Global.showCustomToast(activity, ToastFile.LOCATION_NOT_FOUND);
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
                Log.e(TAG,   "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(imageFile.length())));
                if (imageFile != null && imageFile.exists()) {
                    correct_img.setVisibility(View.VISIBLE);
                    btn_choose_file.setText(getString(R.string.re_upload));
                    tvUploadImageText.setHint(getString(R.string.image_uploaded));
                    tvUploadImageText.setPaintFlags(tvUploadImageText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    correct_img.setVisibility(View.GONE);
                    btn_choose_file.setText(getString(R.string.choose_file));
                    tvUploadImageText.setHint(getString(R.string.img_upload));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                Global.showCustomToast(activity, "Failed to load image!");
                return;
            }
            try {
                imageFile = FileUtil.from(activity, data.getData());
                Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(imageFile.length())) );
                imageFile = GlobalClass.getCompressedFile(activity, imageFile);
                Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(imageFile.length())));
                if (imageFile != null && imageFile.exists()) {
                    correct_img.setVisibility(View.VISIBLE);
                    btn_choose_file.setText(getString(R.string.re_upload));
                    tvUploadImageText.setHint(getString(R.string.image_uploaded));
                    tvUploadImageText.setPaintFlags(tvUploadImageText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    correct_img.setVisibility(View.GONE);
                    btn_choose_file.setText(getString(R.string.choose_file));
                    tvUploadImageText.setHint(getString(R.string.img_upload));
                }
            } catch (IOException e) {
                Global.showCustomToast(activity, "Failed to read image data!");
                e.printStackTrace();
            }
        }
    }

    private boolean Validation() {
        if (mobile_edt.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            mobile_edt.requestFocus();
            return false;
        }
        if (mobile_edt.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            mobile_edt.requestFocus();
            return false;
        }
        if (mobile_edt.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            mobile_edt.requestFocus();
            return false;
        }
        if (!validMobileNumber) {
            Global.showCustomToast(getActivity(), ToastFile.MOB_NOT_VERIFIED);
            mobile_edt.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_NAME);
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() < 2) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_NAME);
            edt_name.requestFocus();
            return false;
        }
        if (edt_age.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_AGE);
            edt_age.requestFocus();
            return false;
        }
        /*if (edt_collamount.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_COLL_AMOUNT);
            edt_collamount.requestFocus();
            return false;
        }
        if (Integer.parseInt(edt_collamount.getText().toString()) > 25) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_VALID_AMOUNT);
            edt_collamount.requestFocus();
            return false;
        }*/
        if (GlobalClass.isNull(gender)) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_GENDER);
            return false;
        }
        if (spin_bs_test.getSelectedItem().toString().equals("Select")) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_TEST_TYPE);
            return false;
        }
        if (edt_val.getText().toString().equals("")) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_VALUE);
            edt_val.requestFocus();
            return false;
        }
        if (edt_systolic.getText().toString().length() > 0) {
            if (edt_diastolic.getText().toString().isEmpty()) {
                Global.showCustomToast(getActivity(), ToastFile.ENTER_DIASTOLIC_VALUE);
                edt_diastolic.requestFocus();
                return false;
            }
        }
        if (edt_diastolic.getText().toString().length() > 0) {
            if (edt_systolic.getText().toString().isEmpty()) {
                Global.showCustomToast(getActivity(), ToastFile.ENTER_SYSTOLIC_VALUE);
                edt_systolic.requestFocus();
                return false;
            }
        }
        if (correct_img.getVisibility() == View.GONE) {
            Global.showCustomToast(getActivity(), ToastFile.UPLOAD_IMAGE);
            return false;
        }  /*else if ((edt_val.getText().toString().length() > 0)) {
            enteredVal = Integer.parseInt(edt_val.getText().toString());
            if (((enteredVal >= minValue) && (enteredVal <= maxValue))) {
                return true;
            } else {
                Global.showCustomToast(getActivity(), "Enter valid value !");
                edt_val.requestFocus();
                edt_val.setText("");
                return false;
            }
        }*/
        if (imageFile == null && !imageFile.exists()) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_IMAGE);
            return false;
        }
        if (spin_coll_amt.getSelectedItem().toString().equals("Select collected amount")) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_COLL_AMT);
            return false;
        }
        if (edt_pincode.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_PINCODE);
            edt_pincode.requestFocus();
            return false;
        }
        if (edt_pincode.getText().toString().length() < 6) {
            Global.showCustomToast(getActivity(), ToastFile.PINCODE_6_DIGITS);
            edt_pincode.requestFocus();
            return false;
        }
        if (edt_email.getText().toString().length() > 0) {
            if (edt_email.getText().toString().length() == 0 || edt_email.getText().toString().isEmpty()) {
                Global.showCustomToast(getActivity(), ToastFile.ENTER_EMAIL);
                edt_email.requestFocus();
                return false;
            } else if (!emailValidation(edt_email.getText().toString())) {
                Global.showCustomToast(getActivity(), ToastFile.VALID_EMAIL);
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

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
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
        btn_verify_otp.setText(activity.getString(R.string.verify));
        edt_otp.setEnabled(true);
        btn_verify_otp.setEnabled(true);
        mobile_edt.setText("");
        edt_otp.setText("");
        edt_name.setText("");
        edt_age.setText("");
//        edt_collamount.setText("");
        spin_bs_test.setSelection(0);
        spin_coll_amt.setSelection(0);
        edt_val.setText("");
        edt_systolic.setText("");
        edt_diastolic.setText("");
        imageFile = null;
        edt_email.setText("");
        edt_pincode.setText("");
        gender = "";
        male.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
        male_red.setVisibility(View.GONE);
        female_red.setVisibility(View.GONE);
        correct_img.setVisibility(View.GONE);
        btn_choose_file.setText(getString(R.string.choose_file));
        tvUploadImageText.setHint(getString(R.string.img_upload));
    }

    private boolean checkLocationPerm() {
        int result = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPerm() {
        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
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

}