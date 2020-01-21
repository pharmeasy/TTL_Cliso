package com.example.e5322.thyrosoft.WOE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class SummaryAddWoe extends AppCompatActivity {
    ProgressDialog barProgressDialog;
    TextView pat_name,pat_test,pat_patient_info,pat_sct,old_tests;
    EditText add_remark;
    RequestQueue POstQue;
    SharedPreferences prefs,getBarcodeDetails;
    Button addWoeDone;
    String message,status;
    String user,passwrd,access,api_key,RES_ID, barcode_patient_id,ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2,TESTS,getTEst;
     String version;
     int versionCode;
    private String ERROR;
    TextView title;
    ImageView home,back;
    private String responseAdd;

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
    private String sr_number;
    private String getTestCodes;
    private String TAG=SummaryAddWoe.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_add_woe);

        pat_name=(TextView)findViewById(R.id.pat_name);
        pat_test=(TextView)findViewById(R.id.pat_test);
        pat_patient_info=(TextView)findViewById(R.id.pat_patient_info);
        pat_sct=(TextView)findViewById(R.id.pat_sct);
        old_tests=(TextView)findViewById(R.id.old_tests);
        title=(TextView)findViewById(R.id.title);
        home=(ImageView) findViewById(R.id.home);
        back=(ImageView)findViewById(R.id.back);
        add_remark=(EditText) findViewById(R.id.add_remark);
        addWoeDone=(Button) findViewById(R.id.addWoeDone);

        title.setText("Summary");

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
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        getBarcodeDetails = getSharedPreferences("AddTestType", MODE_PRIVATE);
        ALERT = getBarcodeDetails.getString("ALERT", null);
        BARCODE = getBarcodeDetails.getString("BARCODE", null);
        BVT_HRS = getBarcodeDetails.getString("BVT_HRS", null);
        LABCODE = getBarcodeDetails.getString("LABCODE", null);
        PATIENT = getBarcodeDetails.getString("PATIENT", null);
        REF_DR = getBarcodeDetails.getString("REF_DR", null);
        REQUESTED_ADDITIONAL_TESTS = getBarcodeDetails.getString("REQUESTED_ADDITIONAL_TESTS", null);
        REQUESTED_ON = getBarcodeDetails.getString("REQUESTED_ON", null);
        RES_ID = getBarcodeDetails.getString("RES_ID", null);
        SDATE = getBarcodeDetails.getString("SDATE", null);
        SL_NO = getBarcodeDetails.getString("SL_NO", null);
        STATUS = getBarcodeDetails.getString("STATUS", null);
        SU_CODE1 = getBarcodeDetails.getString("SU_CODE1", null);
        SU_CODE2 = getBarcodeDetails.getString("SU_CODE2", null);
        TESTS = getBarcodeDetails.getString("TESTS", null);
        getTEst = getIntent().getStringExtra("extraAddedTest");
        getTestCodes=getIntent().getStringExtra("passtoAPI");

        pat_name.setText(PATIENT);
        pat_test.setText(TESTS);
        pat_patient_info.setText(REF_DR);
        pat_sct.setText(SDATE);
        old_tests.setText(getTEst);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView
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
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")||  enteredString.startsWith("0")|| enteredString.startsWith("1")||
                        enteredString.startsWith("2")|| enteredString.startsWith("3")|| enteredString.startsWith("4")|| enteredString.startsWith("5")|| enteredString.startsWith("6")||
                        enteredString.startsWith("7")|| enteredString.startsWith("8")|| enteredString.startsWith("9")) {
                    Toast.makeText(SummaryAddWoe.this,
                            ToastFile.remark,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        add_remark.setText(enteredString.substring(1));
                    } else {
                        add_remark.setText("");
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
                if(purpose.equals("")){
                    Toast.makeText(SummaryAddWoe.this,  ToastFile.remark, Toast.LENGTH_SHORT).show();
                }else{
                    barProgressDialog = new ProgressDialog(SummaryAddWoe.this);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    POstQue = Volley.newRequestQueue(SummaryAddWoe.this);
                    JSONObject jsonObjectOtp = new JSONObject();
                    JSONObject addrecheck = new JSONObject();
                    try {
                        jsonObjectOtp.put("api_key", api_key);
                        addrecheck.put("LABCODE",LABCODE);
                        addrecheck.put("BARCODE",BARCODE);
                        addrecheck.put("TSP",user);
                        addrecheck.put("REQ_TESTS",getTestCodes);
                        addrecheck.put("REQ_TYPE","ADD");
                        addrecheck.put("PURPOSE",purpose);
                        addrecheck.put("ORDERED_TESTS",TESTS);
                        jsonObjectOtp.put("addrecheck", addrecheck);

                        /*Global.Username = username.getText().toString();*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.addrecheckWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(TAG, "onResponse: "+response );
                                String finalJson = response.toString();
                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                ERROR = parentObjectOtp.getString("ERROR");
                                RES_ID = parentObjectOtp.getString("RES_ID");
                                responseAdd = parentObjectOtp.getString("response");
                                if (responseAdd.equalsIgnoreCase("DATA INSERTED SUCCESSFULLY")) {
                                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                    Intent i = new Intent(SummaryAddWoe.this, ManagingTabsActivity.class);
                                    i.putExtra("passToWoefragment", "frgamnebt");
                                    startActivity(i);
                                    //SummaryActivity_New.this, Response, TastyToast.LENGTH_SHORT, TastyToast.ERROR
                                    TastyToast.makeText(SummaryAddWoe.this,responseAdd,TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }else if(ERROR.equalsIgnoreCase(caps_invalidApikey)){
                                    GlobalClass.redirectToLogin(SummaryAddWoe.this);
                                } else {
                                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                    TastyToast.makeText(SummaryAddWoe.this,responseAdd,TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                            } else {

                                System.out.println(error);
                            }
                        }
                    });
                    jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                            150000,
                            3,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    POstQue.add(jsonObjectRequest1);
                    Log.e(TAG, "onClick: json"+jsonObjectOtp );
                    Log.e(TAG, "onClick: url"+jsonObjectRequest1 );

                }

            }
        });
    }
}
