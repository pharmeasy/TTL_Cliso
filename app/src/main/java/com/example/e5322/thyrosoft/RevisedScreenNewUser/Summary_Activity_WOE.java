package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.DeleteWoe_Controller;
import com.example.e5322.thyrosoft.Controller.Getwomaster_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Summary_Activity_WOE extends AppCompatActivity {
    StringBuilder commaSepValueBuilder = new StringBuilder();
    Context mContext;
    SharedPreferences prefs;
    LinearLayout SGCLinearid, refbylinear, btechlinear, linear_scp, delete_patient_test_water, ll_patient_age, ll_patient_gender;
    String user, passwrd, genderId, access, api_key, error, pid, response1, barcodes, resID, saveAgeType;
    LinearLayout delete_patient_test;
    public static com.android.volley.RequestQueue deletePatienDetail;
    RecyclerView sample_list;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile, title, txt_pat_age, txt_pat_gender;
    LinearLayoutManager linearLayoutManager;
    ImageView back, home;
    private Global globalClass;
    int passvalue = 0;
    private String TAG = Summary_Activity_WOE.class.getSimpleName().toString();
    Activity mActivity;
    public ArrayList<Summary_model> summary_models = new ArrayList<>();
    public ArrayList<Barcodelist> barcodelists;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_summary);
        mActivity = Summary_Activity_WOE.this;

        if (getIntent().getExtras() != null) {
            summary_models = getIntent().getExtras().getParcelableArrayList("summary_models");
        }

        initViews();

        linearLayoutManager = new LinearLayoutManager(Summary_Activity_WOE.this);
        sample_list.setLayoutManager(linearLayoutManager);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        try {
            if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null &&
                    summary_models.get(0).getWoeditlist().getWoe() != null) {

                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getBRAND()) &&
                        summary_models.get(0).getWoeditlist().getWoe().getBRAND().equalsIgnoreCase("WHATERS")) {
                    delete_patient_test_water.setVisibility(View.VISIBLE);
                    delete_patient_test.setVisibility(View.GONE);
                } else {
                    delete_patient_test_water.setVisibility(View.GONE);
                    delete_patient_test.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        GlobalClass.SetText(title, "Summary");


        iniListner();

        try {
            if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null) {
                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("Y")) {
                    saveAgeType = "Year";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("M")) {
                    saveAgeType = "Month";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE()) && summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equalsIgnoreCase("D")) {
                    saveAgeType = "Days";
                }
                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getGENDER()) && summary_models.get(0).getWoeditlist().getWoe().getGENDER().equalsIgnoreCase("F")) {
                    genderId = "Female";
                } else if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getGENDER()) && summary_models.get(0).getWoeditlist().getWoe().getGENDER().equalsIgnoreCase("M")) {
                    genderId = "Male";
                }
            } else {
                Woe_fragment notificationFragment = new Woe_fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, notificationFragment, notificationFragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        getDataFromServer();

        barcodelists = new ArrayList<>();
        ArrayList<String> getNames = new ArrayList<>();


        if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null && summary_models.get(0).getWoeditlist().getWoe() != null) {
            if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getBRAND()) && summary_models.get(0).getWoeditlist().getWoe().getBRAND().equalsIgnoreCase("WHATERS")) {
                GlobalClass.SetText(pat_type, "EQNX" + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getTYPE() + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getSR_NO() + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
            } else {
                GlobalClass.SetText(pat_type, summary_models.get(0).getWoeditlist().getWoe().getBRAND().toString() + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getTYPE() + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getSR_NO() + "/" +
                        summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
            }
        }

        if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null && summary_models.get(0).getWoeditlist().getWoe() != null) {
            if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME())) {
                GlobalClass.SetText(pat_sct, summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
            } else {
                pat_sct.setVisibility(View.GONE);
            }
        }

        if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null && summary_models.get(0).getWoeditlist().getWoe() != null) {
            if (GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getGENDER())) {
                GlobalClass.SetText(pat_name, summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
                ll_patient_age.setVisibility(View.GONE);
                ll_patient_gender.setVisibility(View.GONE);
            } else {
                if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAGE())
                        && saveAgeType != null && !saveAgeType.equalsIgnoreCase("")) {
                    ll_patient_age.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(pat_name, summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
                    GlobalClass.SetText(txt_pat_age, summary_models.get(0).getWoeditlist().getWoe().getAGE() + " " + saveAgeType);
                } else {
                    ll_patient_age.setVisibility(View.GONE);
                }
                if (!GlobalClass.isNull(genderId)) {
                    ll_patient_gender.setVisibility(View.VISIBLE);
                    GlobalClass.SetText(txt_pat_gender, genderId);
                } else {
                    ll_patient_gender.setVisibility(View.GONE);
                }
            }
        }


        if (GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME())) {
            pat_ref.setVisibility(View.GONE);
            refbylinear.setVisibility(View.GONE);
        } else {
            GlobalClass.SetText(pat_ref, summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
        }
        if (GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME())) {
            SGCLinearid.setVisibility(View.GONE);
        } else {
            GlobalClass.SetText(pat_sgc, summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
        }
        if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS())) {
            GlobalClass.SetText(pat_scp, summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS());
        }
        if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS()) && summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equals("(),,,")) {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        } else {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        }
        if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED())) {
            GlobalClass.SetText(pat_amt_collected, summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED());
        } else {
            pat_amt_collected.setVisibility(View.GONE);
        }
        if (GlobalClass.CheckArrayList(summary_models) && summary_models.get(0).getWoeditlist() != null &&
                GlobalClass.checkArray(summary_models.get(0).getWoeditlist().getBarcodelist())) {

            for (int i = 0; i < summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                getNames.add(summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                barcodelists.add(summary_models.get(0).getWoeditlist().getBarcodelist()[i]);
                if (GlobalClass.CheckArrayList(getNames)){
                    for (int j = 0; j < getNames.size(); j++) {
                        commaSepValueBuilder.append(getNames.get(j));
                        if (i != getNames.size()) {
                            String setSelectedTest = TextUtils.join(",", getNames);
                            GlobalClass.SetText(tests, setSelectedTest);
                        }
                    }
                }

            }

        }
        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(mContext, barcodelists, passvalue);
        sample_list.setAdapter(getPatientSampleDetails);


    }

    private void iniListner() {


        delete_patient_test_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Summary_Activity_WOE.this);
                builder.setTitle(MessageConstants.CONFIRM_DT);
                builder.setMessage(ToastFile.wish_woe_dlt);
                builder.setPositiveButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePatientDetailsandTest();
                    }
                });
                builder.show();


            }
        });


        delete_patient_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deletePatientDetailsandTest();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Intent i = new Intent(Summary_Activity_WOE.this, Woe_Edt_Activity.class);
                                startActivity(i);
                                finish();
                                break;
                        }
                    }
                };


                try {
                    if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getORDER_NO())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Summary_Activity_WOE.this);
                        builder.setTitle(MessageConstants.CONFIRM_DT);
                        builder.setMessage(ToastFile.wish_woe_dlt).setPositiveButton(MessageConstants.YES, dialogClickListener).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Summary_Activity_WOE.this);
                        builder.setTitle(MessageConstants.CONFIRM_DT);
                        builder.setMessage(ToastFile.wish_woe_dlt).setPositiveButton(MessageConstants.YES, dialogClickListener)
                                .setNegativeButton(MessageConstants.EDIT, dialogClickListener).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Summary_Activity_WOE.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {

        pat_type = (TextView) findViewById(R.id.pat_type);
        pat_sct = (TextView) findViewById(R.id.pat_sct);
        pat_name = (TextView) findViewById(R.id.pat_name);
        pat_ref = (TextView) findViewById(R.id.pat_ref);
        pat_sgc = (TextView) findViewById(R.id.pat_sgc);
        pat_scp = (TextView) findViewById(R.id.pat_scp);
        pat_amt_collected = (TextView) findViewById(R.id.pat_amt_collected);
        tests = (TextView) findViewById(R.id.tests);
        btech = (TextView) findViewById(R.id.btech);
        btechtile = (TextView) findViewById(R.id.btechtile);
        title = (TextView) findViewById(R.id.title);
        sample_list = (RecyclerView) findViewById(R.id.sample_list);
        delete_patient_test = (LinearLayout) findViewById(R.id.delete_patient_test);
        delete_patient_test_water = (LinearLayout) findViewById(R.id.delete_patient_test_water);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        SGCLinearid = (LinearLayout) findViewById(R.id.SGCLinearid);
        refbylinear = (LinearLayout) findViewById(R.id.refbylinear);
        btechlinear = (LinearLayout) findViewById(R.id.btechlinear);
        linear_scp = (LinearLayout) findViewById(R.id.linear_scp);

        ll_patient_age = (LinearLayout) findViewById(R.id.ll_patient_age);
        ll_patient_gender = (LinearLayout) findViewById(R.id.ll_patient_gender);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);
    }

    private void getDataFromServer() {
        RequestQueue requestQueue = GlobalClass.setVolleyReq(Summary_Activity_WOE.this);

        try {
            if (ControllersGlobalInitialiser.getwomaster_controller != null) {
                ControllersGlobalInitialiser.getwomaster_controller = null;
            }
            ControllersGlobalInitialiser.getwomaster_controller = new Getwomaster_Controller(mActivity, Summary_Activity_WOE.this);
            ControllersGlobalInitialiser.getwomaster_controller.getwoeMaster_Controller(api_key, user, requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePatientDetailsandTest() {

        RequestQueue deletePatienDetail = GlobalClass.setVolleyReq(Summary_Activity_WOE.this);
        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", GlobalClass.getPatientIdforDeleteDetails);
            jsonObjectOtp.put("tsp", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Delete WOE -->" + jsonObjectOtp.toString());

        try {
            if (ControllersGlobalInitialiser.deleteWoe_controller != null) {
                ControllersGlobalInitialiser.deleteWoe_controller = null;
            }
            ControllersGlobalInitialiser.deleteWoe_controller = new DeleteWoe_Controller(mActivity, Summary_Activity_WOE.this);
            ControllersGlobalInitialiser.deleteWoe_controller.deletewoe(jsonObjectOtp, deletePatienDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getdeletewoe(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);

            error = parentObjectOtp.getString("error");
            pid = parentObjectOtp.getString("pid");
            response1 = parentObjectOtp.getString("response");
            barcodes = parentObjectOtp.getString("barcodes");
            resID = parentObjectOtp.getString("RES_ID");

            if (!GlobalClass.isNull(resID) && resID.equals(Constants.RES0000)) {
                GlobalClass.showTastyToast(mActivity, ToastFile.woe_dlt, 1);
                Constants.covidwoe_flag = "1";
                Intent intent = new Intent(Summary_Activity_WOE.this, ManagingTabsActivity.class);
                GlobalClass.setFlagBackToWoe = true;
                startActivity(intent);
                finish();
            } else {
                GlobalClass.showTastyToast(mActivity, response1, 2);
            }

        } catch (JSONException e) {
            GlobalClass.showTastyToast(mActivity, response1, 2);
            e.printStackTrace();
        }
    }

    public void getTSPResponse(JSONObject response) {
        Log.e(TAG, "onResponse: response" + response);
        Gson gson = new Gson();
        MyPojo myPojo = gson.fromJson(response.toString(), MyPojo.class);

        try {
            BCT_LIST[] bct_lists = myPojo.getMASTERS().getBCT_LIST();
            if (GlobalClass.checkArray(bct_lists)) {
                for (int i = 0; i < bct_lists.length; i++) {
                    if (!GlobalClass.isNull(summary_models.get(0).getWoeditlist().getWoe().getBCT_ID()) &&
                            !GlobalClass.isNull(bct_lists[i].getNED_NUMBER()) &&
                            summary_models.get(0).getWoeditlist().getWoe().getBCT_ID().equalsIgnoreCase(bct_lists[i].getNED_NUMBER())) {
                        String nameofbtech = bct_lists[i].getNAME();
                        GlobalClass.SetText(btech, nameofbtech);
                    } else {
                        btech.setVisibility(View.GONE);
                        btechtile.setVisibility(View.GONE);
                    }
                }
            } else {
                btech.setVisibility(View.GONE);
                btechtile.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
