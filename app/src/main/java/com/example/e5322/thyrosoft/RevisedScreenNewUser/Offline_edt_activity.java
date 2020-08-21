package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.AttachBarcodeAdpter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class Offline_edt_activity extends AppCompatActivity implements RecyclerInterface {

    TextView brand_name, selectType_txt, samplecollectionponit, referedby, sct_txt, sub_source_code, barcode_number, test_names_txt, title;
    EditText name_edt, age_edt;
    Spinner spinyr;
    Activity mActivity;
    RadioButton male, female;
    Button next_btn_patient;
    ArrayList<BarcodelistModel> barcodelists;
    BarcodelistModel barcodelist;
    public boolean genderId = false;
    SharedPreferences prefs;
    private String user, passwrd, access, api_key;
    private String saveGenderId;
    private String age_type;
    private String getName;
    private String getAge;
    private String getAgeType;
    int agesinteger;
    public IntentIntegrator scanIntegrator;
    private int deliveryMode;
    private int Woe_mode;
    RecyclerView recycler_barcode;
    private int sr_no;
    ImageView back, home;
    ScannedBarcodeDetails scannedBarcodeDetails;
    ArrayList<ScannedBarcodeDetails> setAllTestWithBArcodeList;
    private Global globalClass;
    private String versionNameTopass;
    private int versionCode;
    private String TAG = Offline_edt_activity.class.getSimpleName().toString();
    private String getWoeJson;
    private MyPojoWoe myPojoWoe;

    private String MY_DEBUG_TAG = Offline_edt_activity.class.getSimpleName().toString();
    private DatabaseHelper myDb;
    LinearLayoutManager linearLayoutManager;
    private String specimenttype1;
    private int position1;
    private String displayBarcodes;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_edt_ll);
        mActivity = Offline_edt_activity.this;
        initViews();

        initListner();


        linearLayoutManager = new LinearLayoutManager(Offline_edt_activity.this);
        recycler_barcode.setLayoutManager(linearLayoutManager);
        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionNameTopass = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;

        getWoeJson = getIntent().getStringExtra("WoeJson");
        try {
            Gson gson = new Gson();
            myPojoWoe = new MyPojoWoe();
            myPojoWoe = gson.fromJson(getWoeJson, MyPojoWoe.class);

        } catch (Exception e) {
            Log.e(MY_DEBUG_TAG, "Error " + e.toString());
        }


        if (myPojoWoe != null) {
            GlobalClass.SetText(brand_name, myPojoWoe.getWoe().getBRAND());
            GlobalClass.SetText(brand_name, myPojoWoe.getWoe().getBRAND());
            GlobalClass.SetText(selectType_txt, myPojoWoe.getWoe().getTYPE());
            GlobalClass.SetText(name_edt, myPojoWoe.getWoe().getPATIENT_NAME());
            GlobalClass.SetText(sub_source_code, myPojoWoe.getWoe().getMAIN_SOURCE());

            GlobalClass.SetEditText(age_edt, myPojoWoe.getWoe().getAGE());
            GlobalClass.SetText(samplecollectionponit, "Sample collection point : " + myPojoWoe.getWoe().getADDRESS());
            GlobalClass.SetText(referedby, "Ref By: " + myPojoWoe.getWoe().getREF_DR_NAME());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            Date date = null;
            String output = null;
            try {
                //Converting the input String to Date
                date = sdf.parse(myPojoWoe.getWoe().getSPECIMEN_COLLECTION_TIME());
                //Changing the format of date and storing it in String
                output = sdfOutput.format(date);
                //Displaying the date
                Log.v("TAG", output);
                GlobalClass.SetText(sct_txt, output);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }


            setAllTestWithBArcodeList = new ArrayList<>();

            ArrayList<String> getOnlyBarcodes = new ArrayList<>();
            if (myPojoWoe.getBarcodelist().size() != 0) {
                for (int i = 0; i < myPojoWoe.getBarcodelist().size(); i++) {
                    getOnlyBarcodes.add(myPojoWoe.getBarcodelist().get(i).getBARCODE());
                    displayBarcodes = TextUtils.join(",", getOnlyBarcodes);
                    scannedBarcodeDetails = new ScannedBarcodeDetails();
                    scannedBarcodeDetails.setBarcode(myPojoWoe.getBarcodelist().get(i).getBARCODE());
                    scannedBarcodeDetails.setProducts(myPojoWoe.getBarcodelist().get(i).getTESTS());
                    scannedBarcodeDetails.setSpecimen_type(myPojoWoe.getBarcodelist().get(i).getSAMPLE_TYPE());
                    setAllTestWithBArcodeList.add(scannedBarcodeDetails);
                }
            }


            AttachBarcodeAdpter attachBarcodeAdpter = new AttachBarcodeAdpter(Offline_edt_activity.this, setAllTestWithBArcodeList);
            recycler_barcode.setAdapter(attachBarcodeAdpter);
            attachBarcodeAdpter.setOnItemClickListener(new AttachBarcodeAdpter.OnItemClickListener() {
                @Override
                public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                    scanIntegrator = new IntentIntegrator(activity);
                    if (GlobalClass.specimenttype != null) {
                        GlobalClass.specimenttype = "";
                    }
                    GlobalClass.specimenttype = Specimenttype;
                    scanIntegrator.initiateScan();
                }
            });


            ArrayList<String> getOnlyTestNames = new ArrayList<>();
            if (myPojoWoe.getBarcodelist().size() != 0)
                for (int i = 0; i < myPojoWoe.getBarcodelist().size(); i++) {
                    getOnlyTestNames.add(myPojoWoe.getBarcodelist().get(i).getTESTS());
                    String displayslectedtest = TextUtils.join(",", getOnlyTestNames);
                    GlobalClass.SetText(test_names_txt, displayslectedtest);
                }

            if (myPojoWoe.getWoe().getAGE_TYPE().equals("Y")) {
                spinyr.setSelection(0);
                age_type = "Y";
            } else if (myPojoWoe.getWoe().getAGE_TYPE().equals("M")) {
                spinyr.setSelection(1);
                age_type = "M";
            } else if (myPojoWoe.getWoe().getAGE_TYPE().equals("D")) {
                spinyr.setSelection(2);
                age_type = "D";
            }
            if (myPojoWoe.getWoe().getGENDER().equals("M")) {
                male.setChecked(true);
                female.setChecked(false);
                saveGenderId = "M";
            } else if (myPojoWoe.getWoe().getGENDER().equals("F")) {
                female.setChecked(true);
                male.setChecked(false);
                saveGenderId = "F";
            }
        }

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
                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "M";
                } else if (genderId == true) {
                    genderId = false;
                    saveGenderId = "M";
                }

            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "F";
                } else if (genderId == true) {
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
                if (getName.equals("") || getName.equals(null) || getName.length() <= 1) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_name, 2);
                } else if (getAge.equals("") || getAge.equals(null)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_age, 2);
                } else if (getAgeType.equals("") || getAgeType.equals(null)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_age_type, 2);
                } else if (saveGenderId.equals("") || saveGenderId.equals(null)) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_gender, 2);
                } else {
                    if (getAgeType.equals("Years")) {
                        age_type = "Y";
                    } else if (getAgeType.equals("Months")) {
                        age_type = "M";
                    } else if (getAgeType.equals("Days")) {
                        age_type = "D";
                    }
                    doWOE_edit();
                }
            }
        });
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
                GlobalClass.goToHome(Offline_edt_activity.this);
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
                } else {

                }


                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_age, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(age_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(age_edt, "");
                    }
                }
                if (age_edt.getText().toString().equals("")) {

                } else {
                    if (agesinteger < 12) {
                        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(Offline_edt_activity.this, R.array.Patientsagespinner,
                                R.layout.spinner_item);
                        spinyr.setAdapter(PatientsagespinnerAdapter);
                    }
                    if (agesinteger > 12) {
                        ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(Offline_edt_activity.this, R.array.Patientspinyrday,
                                R.layout.spinner_item);
                        spinyr.setAdapter(Patientsagespinner);
                    }
                    if (agesinteger > 29) {
                        ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(Offline_edt_activity.this, R.array.Patientspinyr,
                                R.layout.spinner_item);
                        spinyr.setAdapter(Patientsagesyr);
                    }
                }
            }
        });


    }

    private void initViews() {
        brand_name = (TextView) findViewById(R.id.brand_name);
        selectType_txt = (TextView) findViewById(R.id.selectType_txt);
        samplecollectionponit = (TextView) findViewById(R.id.samplecollectionponit);
        referedby = (TextView) findViewById(R.id.referedby);
        sct_txt = (TextView) findViewById(R.id.sct_txt);
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
        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        myDb = new DatabaseHelper(Offline_edt_activity.this);
        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(Offline_edt_activity.this, R.array.Patientsagespinner,
                R.layout.spinner_item);
        spinyr.setAdapter(PatientsagespinnerAdapter);

        prefs = Offline_edt_activity.this.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        GlobalClass.SetText(title, "Offline WOE Edit");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    passBarcodeData(getBarcodeDetails);
                } else {
                    GlobalClass.showTastyToast(mActivity, invalid_brcd, 2);
                }

            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;

        if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
            for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                if (!GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getBarcode())) {
                    if (!GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getBarcode()) && !GlobalClass.isNull(s) &&
                            setAllTestWithBArcodeList.get(i).getBarcode().equalsIgnoreCase(s)) {
                        isbacodeduplicate = true;
                    }
                }
            }
        }


        if (isbacodeduplicate == true) {
            GlobalClass.showTastyToast(mActivity, ToastFile.duplicate_barcd, 2);
        } else {

            if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
                for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                    if (!GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getSpecimen_type()) &&
                            !GlobalClass.isNull(GlobalClass.specimenttype) && setAllTestWithBArcodeList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                        setAllTestWithBArcodeList.get(i).setBarcode("");
                        setAllTestWithBArcodeList.get(i).setBarcode(s);
                        Log.e(TAG, "passBarcodeData: show barcode" + s);
                    }
                }
            }

        }
        recycler_barcode.removeAllViews();
        AttachBarcodeAdpter attachBarcodeAdpter = new AttachBarcodeAdpter(Offline_edt_activity.this, setAllTestWithBArcodeList);

        attachBarcodeAdpter.setOnItemClickListener(new AttachBarcodeAdpter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (GlobalClass.specimenttype != null) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_barcode.setAdapter(attachBarcodeAdpter);
    }


    private void doWOE_edit() {

        Woe woe = new Woe();
        woe.setAADHAR_NO(myPojoWoe.getWoe().getAADHAR_NO());
        woe.setADDRESS(myPojoWoe.getWoe().getADDRESS());
        woe.setAGE(getAge);
        woe.setAGE_TYPE(age_type);
        woe.setALERT_MESSAGE(myPojoWoe.getWoe().getALERT_MESSAGE());
        woe.setAMOUNT_COLLECTED(myPojoWoe.getWoe().getAMOUNT_COLLECTED());
        woe.setAMOUNT_DUE(myPojoWoe.getWoe().getAMOUNT_DUE());
        woe.setAPP_ID(myPojoWoe.getWoe().getAPP_ID());
        woe.setBCT_ID(myPojoWoe.getWoe().getBCT_ID());
        woe.setBRAND(myPojoWoe.getWoe().getBRAND());
        woe.setCAMP_ID(myPojoWoe.getWoe().getCAMP_ID());
        woe.setCONT_PERSON(myPojoWoe.getWoe().getCONT_PERSON());
        woe.setCONTACT_NO(myPojoWoe.getWoe().getCONTACT_NO());
        woe.setCUSTOMER_ID(myPojoWoe.getWoe().getCUSTOMER_ID());
        woe.setDELIVERY_MODE(myPojoWoe.getWoe().getDELIVERY_MODE());

        woe.setEMAIL_ID(myPojoWoe.getWoe().getEMAIL_ID());
        woe.setENTERED_BY(myPojoWoe.getWoe().getENTERED_BY());
        woe.setGENDER(myPojoWoe.getWoe().getGENDER());
        woe.setLAB_ADDRESS(myPojoWoe.getWoe().getLAB_ADDRESS());
        woe.setLAB_ID(myPojoWoe.getWoe().getLAB_ID());
        woe.setLAB_NAME(myPojoWoe.getWoe().getLAB_NAME());
        woe.setLEAD_ID(myPojoWoe.getWoe().getLEAD_ID());
        woe.setMAIN_SOURCE(myPojoWoe.getWoe().getMAIN_SOURCE());
        woe.setORDER_NO(myPojoWoe.getWoe().getORDER_NO());
        woe.setOS(myPojoWoe.getWoe().getOS());
        woe.setPATIENT_NAME(getName);
        woe.setPINCODE(myPojoWoe.getWoe().getPINCODE());
        woe.setPRODUCT(myPojoWoe.getWoe().getPRODUCT());
        woe.setPurpose(myPojoWoe.getWoe().getPurpose());
        woe.setREF_DR_ID(myPojoWoe.getWoe().getREF_DR_ID());
        woe.setREF_DR_NAME(myPojoWoe.getWoe().getREF_DR_NAME());
        woe.setREMARKS(myPojoWoe.getWoe().getREMARKS());
        woe.setSPECIMEN_COLLECTION_TIME(myPojoWoe.getWoe().getSPECIMEN_COLLECTION_TIME());
        woe.setSPECIMEN_SOURCE(myPojoWoe.getWoe().getSPECIMEN_SOURCE());
        woe.setSR_NO(myPojoWoe.getWoe().getSR_NO());
        woe.setSTATUS(myPojoWoe.getWoe().getSTATUS());
        woe.setSUB_SOURCE_CODE(myPojoWoe.getWoe().getSUB_SOURCE_CODE());
        woe.setTOTAL_AMOUNT(myPojoWoe.getWoe().getTOTAL_AMOUNT());
        woe.setTYPE(myPojoWoe.getWoe().getTYPE());
        woe.setWATER_SOURCE(myPojoWoe.getWoe().getWATER_SOURCE());
        woe.setWO_MODE(myPojoWoe.getWoe().getWO_MODE());
        woe.setWO_STAGE(myPojoWoe.getWoe().getWO_STAGE());
        myPojoWoe.setWoe(woe);

        barcodelists = new ArrayList<>();

        ArrayList<String> setBarcodes = new ArrayList<>();

        if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
            for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                barcodelist = new BarcodelistModel();
                barcodelist.setSAMPLE_TYPE(setAllTestWithBArcodeList.get(i).getSpecimen_type());
                barcodelist.setBARCODE(setAllTestWithBArcodeList.get(i).getBarcode());
                setBarcodes.add(setAllTestWithBArcodeList.get(i).getBarcode());
                barcodelist.setTESTS((setAllTestWithBArcodeList.get(i).getProducts()));
                barcodelists.add(barcodelist);
            }
        }

        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WOE");
        myPojoWoe.setApi_key(api_key);//api_key

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);

        boolean isUpdated = myDb.updateData(displayBarcodes, json);
        if (isUpdated) {
            try {
                Gson gsonData = new Gson();
                myPojoWoe = new MyPojoWoe();
                myPojoWoe = gsonData.fromJson(json, MyPojoWoe.class);

            } catch (Exception e) {
                Log.e(MY_DEBUG_TAG, "Error " + e.toString());
            }

            ArrayList<String> setBarcodesinDb = new ArrayList<>();

            if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.getBarcodelist())) {
                for (int i = 0; i < myPojoWoe.getBarcodelist().size(); i++) {
                    setBarcodesinDb.add(myPojoWoe.getBarcodelist().get(i).getBARCODE());
                }
            }

            String passbrcd = TextUtils.join(",", setBarcodesinDb);
            boolean isUpdatedData = myDb.updateBArcodeColumn(passbrcd, json);
            if (isUpdatedData) {
                GlobalClass.showTastyToast(mActivity, ToastFile.woeEdt, 1);
                finish();
            }

        } else {
            GlobalClass.showTastyToast(mActivity, ToastFile.woenotEdt, 2);
        }

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public void putDataToscan(Activity context, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();
    }


}
