package com.example.e5322.thyrosoft.WOE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.AddrecheckWOE_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.AddRecheckResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class SummaryAddWoe extends AppCompatActivity {
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
    TextView pat_name, pat_test, pat_patient_info, pat_sct, old_tests;
    EditText add_remark;
    RequestQueue POstQue;
    SharedPreferences prefs, getBarcodeDetails;
    Button addWoeDone;
    String user, passwrd, access, api_key, RES_ID, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS, getTEst;
    String version;
    int versionCode;
    TextView title;
    ImageView home, back;
    Activity mActivity;
    String blockCharacterSetdata = "~#^|$%&*!+:`";
    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private String sr_number;
    private String getTestCodes;
    private String TAG = SummaryAddWoe.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_add_woe);
        mActivity = SummaryAddWoe.this;
        initViews();
        initListner();
    }

    private void initListner() {
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        getBarcodeDetails = getSharedPreferences("AddTestType", MODE_PRIVATE);
        ALERT = getBarcodeDetails.getString("ALERT", "");
        BARCODE = getBarcodeDetails.getString("BARCODE", "");
        BVT_HRS = getBarcodeDetails.getString("BVT_HRS", "");
        LABCODE = getBarcodeDetails.getString("LABCODE", "");
        PATIENT = getBarcodeDetails.getString("PATIENT", "");
        REF_DR = getBarcodeDetails.getString("REF_DR", "");
        REQUESTED_ADDITIONAL_TESTS = getBarcodeDetails.getString("REQUESTED_ADDITIONAL_TESTS", "");
        REQUESTED_ON = getBarcodeDetails.getString("REQUESTED_ON", "");
        RES_ID = getBarcodeDetails.getString("RES_ID", "");
        SDATE = getBarcodeDetails.getString("SDATE", "");
        SL_NO = getBarcodeDetails.getString("SL_NO", "");
        STATUS = getBarcodeDetails.getString("STATUS", "");
        SU_CODE1 = getBarcodeDetails.getString("SU_CODE1", "");
        SU_CODE2 = getBarcodeDetails.getString("SU_CODE2", "");
        TESTS = getBarcodeDetails.getString("TESTS", "");
        getTEst = getIntent().getStringExtra("extraAddedTest");
        getTestCodes = getIntent().getStringExtra("passtoAPI");


        GlobalClass.SetText(pat_name,PATIENT);
        GlobalClass.SetText(pat_test,TESTS);
        GlobalClass.SetText(pat_patient_info,REF_DR);
        GlobalClass.SetText(pat_sct,SDATE);
        GlobalClass.SetText(old_tests,getTEst);


        version = GlobalClass.getversion(mActivity);

        versionCode = GlobalClass.getversioncode(mActivity);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(SummaryAddWoe.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        add_remark.setFilters(new InputFilter[]{filter1});
        add_remark.setFilters(new InputFilter[]{EMOJI_FILTER});

        add_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("0") || enteredString.startsWith("1") ||
                        enteredString.startsWith("2") || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5") || enteredString.startsWith("6") ||
                        enteredString.startsWith("7") || enteredString.startsWith("8") || enteredString.startsWith("9")) {
                    GlobalClass.showTastyToast(SummaryAddWoe.this, ToastFile.remark, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(add_remark,enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(add_remark,"");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        addWoeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String purpose = add_remark.getText().toString();
                if (purpose.equals("")) {
                    GlobalClass.showTastyToast(SummaryAddWoe.this, ToastFile.remark, 2);
                } else {

                    POstQue = GlobalClass.setVolleyReq(SummaryAddWoe.this);
                    JSONObject jsonObjectOtp = new JSONObject();
                    JSONObject addrecheck = new JSONObject();
                    try {
                        jsonObjectOtp.put("api_key", api_key);
                        addrecheck.put("LABCODE", LABCODE);
                        addrecheck.put("BARCODE", BARCODE);
                        addrecheck.put("TSP", user);
                        addrecheck.put("REQ_TESTS", getTestCodes);
                        addrecheck.put("REQ_TYPE", "ADD");
                        addrecheck.put("PURPOSE", purpose);
                        addrecheck.put("ORDERED_TESTS", TESTS);
                        jsonObjectOtp.put("addrecheck", addrecheck);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (ControllersGlobalInitialiser.addrecheckWOE_controller != null) {
                            ControllersGlobalInitialiser.addrecheckWOE_controller = null;
                        }
                        ControllersGlobalInitialiser.addrecheckWOE_controller = new AddrecheckWOE_Controller(mActivity, SummaryAddWoe.this);
                        ControllersGlobalInitialiser.addrecheckWOE_controller.recheckwoeController(jsonObjectOtp,POstQue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void initViews() {
        pat_name = (TextView) findViewById(R.id.pat_name);
        pat_test = (TextView) findViewById(R.id.pat_test);
        pat_patient_info = (TextView) findViewById(R.id.pat_patient_info);
        pat_sct = (TextView) findViewById(R.id.pat_sct);
        old_tests = (TextView) findViewById(R.id.old_tests);
        title = (TextView) findViewById(R.id.title);
        home = (ImageView) findViewById(R.id.home);
        back = (ImageView) findViewById(R.id.back);
        add_remark = (EditText) findViewById(R.id.add_remark);
        addWoeDone = (Button) findViewById(R.id.addWoeDone);
        GlobalClass.SetText(title,"Summary");

    }

    public void getrecheckWoeResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            Gson gson = new Gson();
            AddRecheckResponseModel responseModel = gson.fromJson(String.valueOf(response), AddRecheckResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("DATA INSERTED SUCCESSFULLY")) {
                    Intent i = new Intent(SummaryAddWoe.this, ManagingTabsActivity.class);
                    i.putExtra("passToWoefragment", "frgamnebt");
                    startActivity(i);
                    GlobalClass.showTastyToast(SummaryAddWoe.this, responseModel.getResponse(), 1);
                } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(SummaryAddWoe.this);
                } else {
                    GlobalClass.showTastyToast(SummaryAddWoe.this, responseModel.getResponse(), 1);
                }
            } else {
                GlobalClass.showTastyToast(SummaryAddWoe.this, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
