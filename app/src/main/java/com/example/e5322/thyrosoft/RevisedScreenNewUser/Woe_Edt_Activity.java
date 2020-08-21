package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.WoeController;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Woe_Edt_Activity extends AppCompatActivity {
    public boolean genderId = false;
    TextView brand_name, selectType_txt, samplecollectionponit, referedby, sct_txt, sub_source_code, barcode_number, test_names_txt, title;
    EditText name_edt, age_edt;
    Spinner spinyr;
    RadioButton male, female;
    Button next_btn_patient;
    ArrayList<BarcodelistModel> barcodelists;
    BarcodelistModel barcodelist;
    SharedPreferences prefs;
    int agesinteger;
    ImageView back, home;
    LinearLayout labname_linear;
    private String RES_ID;
    private String barcode_patient_id;
    private String message;
    private String status;
    private String user, passwrd, access, api_key;
    private String saveGenderId;
    private String age_type;
    private String getName;
    private String getAge;
    private String getAgeType;
    private int deliveryMode;
    private int Woe_mode;
    private Global globalClass;
    private int sr_no;
    private String parsableDate;
    private String versionNameTopass;
    private int versionCode;
    private String TAG = Woe_Edt_Activity.class.getSimpleName().toString();
    Activity mActivity;
    ConnectionDetector cd;
    public ArrayList<Summary_model> summary_models = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit__woe);
        mActivity = Woe_Edt_Activity.this;
        cd = new ConnectionDetector(mActivity);

        if (getIntent().getExtras() != null) {
            summary_models = getIntent().getExtras().getParcelableArrayList("summary_models");
        }

        initViews();
        initListner();

    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Woe_Edt_Activity.this);
            }
        });

        age_edt.addTextChangedListener(new TextWatcher() {
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

                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_age, 1);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(age_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(age_edt, "");
                    }
                }
                if (age_edt.getText().toString().equals("")) {

                } else {
                    try {
                        if (agesinteger < 12) {
                            ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(Woe_Edt_Activity.this, R.array.Patientsagespinner,
                                    R.layout.spinner_item);
                            spinyr.setAdapter(PatientsagespinnerAdapter);
                        }
                        if (agesinteger > 12) {
                            ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(Woe_Edt_Activity.this, R.array.Patientspinyrday,
                                    R.layout.spinner_item);
                            spinyr.setAdapter(Patientsagespinner);
                        }

                        if (agesinteger > 29) {
                            ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(Woe_Edt_Activity.this, R.array.Patientspinyr,
                                    R.layout.spinner_item);
                            spinyr.setAdapter(Patientsagesyr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_name, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(name_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(name_edt, "");
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

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!genderId) {
                    genderId = true;
                    saveGenderId = "M";
                } else if (genderId) {
                    genderId = false;
                    saveGenderId = "M";
                }

            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!genderId) {
                    genderId = true;
                    saveGenderId = "F";
                } else if (genderId) {
                    genderId = false;
                    saveGenderId = "F";
                }

            }
        });

        next_btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName = name_edt.getText().toString();
                getName = getName.replaceAll("\\s+", " ");
                getAge = age_edt.getText().toString();
                getAgeType = spinyr.getSelectedItem().toString();
                if (GlobalClass.isNull(getName)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_name, 2);
                } else if (GlobalClass.isNull(getAge)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_age, 2);
                } else if (GlobalClass.isNull(getAgeType)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_age_type, 2);
                } else if (GlobalClass.isNull(saveGenderId)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_gender, 2);
                } else {
                    if (!GlobalClass.isNull(getAgeType) && getAgeType.equals("Years")) {
                        age_type = "Y";
                    } else if (!GlobalClass.isNull(getAgeType) && getAgeType.equals("Months")) {
                        age_type = "M";
                    } else if (!GlobalClass.isNull(getAgeType) && getAgeType.equals("Days")) {
                        age_type = "D";
                    }

                    doWOE_edit();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        brand_name = (TextView) findViewById(R.id.brand_name);
        selectType_txt = (TextView) findViewById(R.id.selectType_txt);
        samplecollectionponit = (TextView) findViewById(R.id.samplecollectionponit);
        referedby = (TextView) findViewById(R.id.referedby);
        sct_txt = (TextView) findViewById(R.id.sct_txt);
        barcode_number = (TextView) findViewById(R.id.barcode_number);
        sub_source_code = (TextView) findViewById(R.id.sub_source_code);
        title = (TextView) findViewById(R.id.title);
        test_names_txt = (TextView) findViewById(R.id.test_names_txt);
        name_edt = (EditText) findViewById(R.id.name_edt);
        age_edt = (EditText) findViewById(R.id.age_edt);
        spinyr = (Spinner) findViewById(R.id.spinyr);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        next_btn_patient = (Button) findViewById(R.id.next_btn_patient);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        labname_linear = (LinearLayout) findViewById(R.id.labname_linear);

        try {
            ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(Woe_Edt_Activity.this, R.array.Patientsagespinner,
                    R.layout.spinner_item);
            spinyr.setAdapter(PatientsagespinnerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        prefs = Woe_Edt_Activity.this.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        GlobalClass.SetText(title, "WOE Edit");


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionNameTopass = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;


        if (GlobalClass.CheckArrayList(summary_models)) {

            try {
                GlobalClass.SetText(brand_name, summary_models.get(0).getWoeditlist().getWoe().getBRAND());
                GlobalClass.SetText(selectType_txt, summary_models.get(0).getWoeditlist().getWoe().getTYPE());
                GlobalClass.SetText(name_edt, summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
                GlobalClass.SetText(sub_source_code, summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
                GlobalClass.SetText(age_edt, summary_models.get(0).getWoeditlist().getWoe().getAGE());

                if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null
                        && summary_models.get(0).getWoeditlist().getWoe() != null &&
                        !GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME())) {

                    if (!GlobalClass.isNull(selectType_txt.getText().toString()) && selectType_txt.getText().toString().equalsIgnoreCase("HOME") ||
                            selectType_txt.getText().toString().equalsIgnoreCase("DPS")) {
                        labname_linear.setVisibility(View.GONE);
                    } else {
                        labname_linear.setVisibility(View.VISIBLE);
                    }

                    GlobalClass.SetText(samplecollectionponit, summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
                } else {
                    labname_linear.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null && summary_models.get(0).getWoeditlist().getWoe() != null &&
                        !GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME())) {
                    GlobalClass.SetText(referedby, summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

            Date date = null;
            String output = null;

            try {
                //Converting the input String to Date
                if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null) {
                    date = sdf.parse(summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
                    //Changing the format of date and storing it in String
                    output = sdfOutput.format(date);
                    //Displaying the date
                    Log.v("TAG", output);
                    GlobalClass.SetText(sct_txt, summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
                }

            } catch (ParseException pe) {
                pe.printStackTrace();
            }


            ArrayList<String> getOnlyBarcodes = new ArrayList<>();
            try {
                if (GlobalClass.CheckArrayList(summary_models) && GlobalClass.checkArray(summary_models.get(0).getWoeditlist().getBarcodelist()))
                    for (int i = 0; i < summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                        getOnlyBarcodes.add(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getBARCODE());
                        String displayBarcodes = TextUtils.join(",", getOnlyBarcodes);
                        GlobalClass.SetText(barcode_number, displayBarcodes);
                    }

                ArrayList<String> getOnlyTestNames = new ArrayList<>();
                if (GlobalClass.CheckArrayList(summary_models) && GlobalClass.checkArray(summary_models.get(0).getWoeditlist().getBarcodelist()))
                    for (int i = 0; i < summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                        getOnlyTestNames.add(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                        String displayslectedtest = TextUtils.join(",", getOnlyTestNames);
                        GlobalClass.SetText(test_names_txt, "Tests : " + displayslectedtest);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("Y")) {
                    age_type = "Y";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("M")) {
                    age_type = "M";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("D")) {
                    age_type = "D";
                }

                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getGENDER()) &&
                        summary_models.get(0).getWoeditlist().getWoe().getGENDER().equalsIgnoreCase("M")) {
                    male.setChecked(true);
                    female.setChecked(false);
                    saveGenderId = "M";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getGENDER())
                        && summary_models.get(0).getWoeditlist().getWoe().getGENDER().equalsIgnoreCase("F")) {
                    female.setChecked(true);
                    male.setChecked(false);
                    saveGenderId = "F";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void doWOE_edit() {
        String brand_type_to_send = null;
        String type_to_send = null;
        try {
            summary_models.get(0).getWoeditlist().getWoe().setPATIENT_NAME(getName);
            summary_models.get(0).getWoeditlist().getWoe().setAGE(getAge);
            summary_models.get(0).getWoeditlist().getWoe().setAGE_TYPE(age_type);
            summary_models.get(0).getWoeditlist().getWoe().setGENDER(saveGenderId);
            brand_type_to_send = brand_name.getText().toString();
            type_to_send = selectType_txt.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String getDateTopass = summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            Date d = sdf.parse(getDateTopass);
            parsableDate = sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RequestQueue POstQue = GlobalClass.setVolleyReq(Woe_Edt_Activity.this);
        MyPojoWoe myPojoWoe = new MyPojoWoe();

        if (GlobalClass.CheckArrayList(summary_models)
                && summary_models.get(0).getWoeditlist() != null &&
                summary_models.get(0).getWoeditlist().getWoe() != null &&
                summary_models.get(0).getWoeditlist().getWoe().getDELIVERY_MODE() != null) {

            deliveryMode = Integer.valueOf(summary_models.get(0).getWoeditlist().getWoe().getDELIVERY_MODE());
        } else {
            Woe_mode = Integer.parseInt(summary_models.get(0).getWoeditlist().getWoe().getWO_STAGE());
        }
        if (GlobalClass.CheckArrayList(summary_models)
                && summary_models.get(0).getWoeditlist() != null &&
                summary_models.get(0).getWoeditlist().getWoe() != null &&
                 !GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getWO_MODE())) {

            sr_no = Integer.parseInt(summary_models.get(0).getWoeditlist().getWoe().getSR_NO());
        }

        Woe woe = new Woe();
        woe.setAADHAR_NO(summary_models.get(0).getWoeditlist().getWoe().getAADHAR_NO());
        woe.setADDRESS(summary_models.get(0).getWoeditlist().getWoe().getADDRESS());
        woe.setAGE(summary_models.get(0).getWoeditlist().getWoe().getAGE());
        woe.setAGE_TYPE(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE());
        woe.setALERT_MESSAGE(summary_models.get(0).getWoeditlist().getWoe().getALERT_MESSAGE());
        woe.setAMOUNT_COLLECTED(summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED());
        woe.setAMOUNT_DUE(summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_DUE());
        woe.setAPP_ID(versionNameTopass);
        woe.setBCT_ID(summary_models.get(0).getWoeditlist().getWoe().getBCT_ID());
        woe.setBRAND(brand_type_to_send);
        woe.setCAMP_ID(summary_models.get(0).getWoeditlist().getWoe().getCAMP_ID());
        woe.setCONT_PERSON(summary_models.get(0).getWoeditlist().getWoe().getCONT_PERSON());
        woe.setCONTACT_NO(summary_models.get(0).getWoeditlist().getWoe().getCONTACT_NO());
        woe.setCUSTOMER_ID(summary_models.get(0).getWoeditlist().getWoe().getCUSTOMER_ID());
        woe.setDELIVERY_MODE(deliveryMode);

        woe.setEMAIL_ID(summary_models.get(0).getWoeditlist().getWoe().getEMAIL_ID());
        woe.setENTERED_BY(summary_models.get(0).getWoeditlist().getWoe().getENTERED_BY());
        woe.setGENDER(summary_models.get(0).getWoeditlist().getWoe().getGENDER());
        woe.setLAB_ADDRESS(summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS());
        woe.setLAB_ID(summary_models.get(0).getWoeditlist().getWoe().getLAB_ID());
        woe.setLAB_NAME(summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
        woe.setLEAD_ID(summary_models.get(0).getWoeditlist().getWoe().getLEAD_ID());
        woe.setMAIN_SOURCE(summary_models.get(0).getWoeditlist().getWoe().getMAIN_SOURCE());
        woe.setORDER_NO(summary_models.get(0).getWoeditlist().getWoe().getORDER_NO());
        woe.setOS(summary_models.get(0).getWoeditlist().getWoe().getOS());
        woe.setPATIENT_NAME(summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
        woe.setPINCODE(summary_models.get(0).getWoeditlist().getWoe().getPINCODE());
        woe.setPRODUCT(summary_models.get(0).getWoeditlist().getWoe().getPRODUCT());
        woe.setPurpose("mobile application");
        woe.setREF_DR_ID(summary_models.get(0).getWoeditlist().getWoe().getREF_DR_ID());
        woe.setREF_DR_NAME(summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
        woe.setREMARKS("MOBILE");
        woe.setSPECIMEN_COLLECTION_TIME(parsableDate);
        woe.setSPECIMEN_SOURCE(summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_SOURCE());
        woe.setSR_NO(sr_no);
        woe.setSTATUS(summary_models.get(0).getWoeditlist().getWoe().getSTATUS());
        woe.setSUB_SOURCE_CODE(summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
        woe.setTOTAL_AMOUNT(summary_models.get(0).getWoeditlist().getWoe().getTOTAL_AMOUNT());
        woe.setTYPE(type_to_send);
        woe.setWATER_SOURCE(summary_models.get(0).getWoeditlist().getWoe().getWATER_SOURCE());
        woe.setWO_MODE(Constants.WOEMODE);
        woe.setWO_STAGE(Woe_mode);
        myPojoWoe.setWoe(woe);

        barcodelists = new ArrayList<>();

        if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null && GlobalClass.checkArray(summary_models.get(0).getWoeditlist().getBarcodelist())) {

            for (int i = 0; i < summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                barcodelist = new BarcodelistModel();
                barcodelist.setSAMPLE_TYPE(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getSAMPLE_TYPE());
                barcodelist.setBARCODE(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getBARCODE());
                barcodelist.setTESTS(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                barcodelists.add(barcodelist);
            }

        }


        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WO_EDIT");
        myPojoWoe.setApi_key(api_key);//api_key


        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (ControllersGlobalInitialiser.woeController != null) {
                ControllersGlobalInitialiser.woeController = null;
            }
            ControllersGlobalInitialiser.woeController = new WoeController(mActivity, Woe_Edt_Activity.this);
            ControllersGlobalInitialiser.woeController.woeDoneController(jsonObj, POstQue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getWoeResponse(JSONObject response) {
        try {

            Log.e(TAG, "onResponse: " + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            RES_ID = parentObjectOtp.getString("RES_ID");
            barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
            message = parentObjectOtp.getString("message");
            status = parentObjectOtp.getString("status");

            if (!GlobalClass.isNull(status) && status.equalsIgnoreCase("SUCCESS")) {

                GlobalClass.showTastyToast(mActivity, message, 1);
                GlobalClass.setFlag_back_toWOE = true;
                Constants.covidwoe_flag = "1";
                Intent i = new Intent(Woe_Edt_Activity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();

            } else if (!GlobalClass.isNull(message) && message.equals(MessageConstants.CRDIT_LIMIT)) {

                GlobalClass.showTastyToast(mActivity, message, 2);


                final AlertDialog alertDialog = new AlertDialog.Builder(
                        Woe_Edt_Activity.this).create();
                alertDialog.setTitle(ToastFile.updateledger);
                alertDialog.setMessage(ToastFile.update_ledger);
                alertDialog.setButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Woe_Edt_Activity.this, Payment_Activity.class);
                        i.putExtra("COMEFROM", "Woe_Edt_Activity");
                        startActivity(i);
                    }
                });
                alertDialog.show();

            } else {
                GlobalClass.showTastyToast(mActivity, message, 2);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}
