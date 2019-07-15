package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BMC_WOE_SummaryActivity extends AppCompatActivity {

    public static RequestQueue deletePatienDetail;
    StringBuilder commaSepValueBuilder = new StringBuilder();
    Context mContext;
    SharedPreferences prefs;
    LinearLayout SGCLinearid, refbylinear, btechlinear, linear_scp, delete_patient_test_water, ll_patient_age, ll_patient_gender, ll_view_images;
    String user, passwrd, genderId, access, api_key, error, pid, response1, barcodes, resID, saveAgeType;
    LinearLayout delete_patient_test;
    RecyclerView sample_list;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile, title, txt_pat_age, txt_pat_gender;
    LinearLayoutManager linearLayoutManager;
    ImageView back, home;
    int passvalue = 0;
    private String TAG = BMC_WOE_SummaryActivity.class.getSimpleName();
    private Global globalClass;
    private TextView tv_view_TRF, tv_view_receipt;
    private Dialog dialog;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmc_woe_summary);

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
        ll_view_images = (LinearLayout) findViewById(R.id.ll_view_images);
        txt_pat_gender = (TextView) findViewById(R.id.txt_pat_gender);
        txt_pat_age = (TextView) findViewById(R.id.txt_pat_age);

        tv_view_TRF = (TextView) findViewById(R.id.tv_view_TRF);
        tv_view_receipt = (TextView) findViewById(R.id.tv_view_receipt);

        linearLayoutManager = new LinearLayoutManager(BMC_WOE_SummaryActivity.this);
        sample_list.setLayoutManager(linearLayoutManager);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBRAND().equalsIgnoreCase("WHATERS")) {
            delete_patient_test_water.setVisibility(View.VISIBLE);
            delete_patient_test.setVisibility(View.GONE);
        } else {
            delete_patient_test_water.setVisibility(View.GONE);
            delete_patient_test.setVisibility(View.VISIBLE);
        }

        title.setText("Summary");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_WOE_SummaryActivity.this, BMC_MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!GlobalClass.summary_models.get(0).getWoeditlist().equals(null)) {
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("Y")) {
                saveAgeType = "Year";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("M")) {
                saveAgeType = "Month";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("D")) {
                saveAgeType = "Days";
            }
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("F")) {
                genderId = "Female";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("M")) {
                genderId = "Male";
            }
        } else {
            BMC_WOEFragment notificationFragment = new BMC_WOEFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mainLayout, notificationFragment, notificationFragment.getClass().getSimpleName()).addToBackStack(null).commit();
        }

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        getDataFromServer();
        GlobalClass.BMC_barcodelists = new ArrayList<>();
        ArrayList<String> getNames = new ArrayList<>();

        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBRAND().equalsIgnoreCase("WHATERS")) {
            pat_type.setText("EQNX" + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getTYPE() + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSR_NO() + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
        } else {
            pat_type.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBRAND() + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getTYPE() + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSR_NO() + "/" +
                    GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());//getMAIN_SOURCE
        }

        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME().equalsIgnoreCase("")) {
            pat_sct.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
        } else {
            pat_sct.setVisibility(View.GONE);
        }

        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("")) {
            pat_name.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
            ll_patient_age.setVisibility(View.GONE);
            ll_patient_gender.setVisibility(View.GONE);
        } else {
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE() != null && !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE().equalsIgnoreCase("")
                    && saveAgeType != null && !saveAgeType.equalsIgnoreCase("")) {
                ll_patient_age.setVisibility(View.VISIBLE);
                pat_name.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
                txt_pat_age.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE() + " " + saveAgeType);
            } else {
                ll_patient_age.setVisibility(View.GONE);
            }
            if (genderId != null && !genderId.equalsIgnoreCase("")) {
                ll_patient_gender.setVisibility(View.VISIBLE);
                txt_pat_gender.setText(genderId);
            } else {
                ll_patient_gender.setVisibility(View.GONE);
            }
        }

        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME().equals(null) || GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME().equalsIgnoreCase("")) {
            pat_ref.setVisibility(View.GONE);
            refbylinear.setVisibility(View.GONE);
        } else {
            pat_ref.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME() == "" || GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME().equals(null)) {
            SGCLinearid.setVisibility(View.GONE);
        } else {
            pat_sgc.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
        }
        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equalsIgnoreCase("")) {
            pat_scp.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS());
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equals("(),,,")) {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        } else {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        }
        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equalsIgnoreCase("") || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equals("null")) {
            pat_amt_collected.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED());
        } else {
            pat_amt_collected.setVisibility(View.GONE);
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length != 0) {
            for (int i = 0; i < GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                getNames.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                GlobalClass.BMC_barcodelists.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i]);
                for (int j = 0; j < getNames.size(); j++) {
                    commaSepValueBuilder.append(getNames.get(j));
                    if (i != getNames.size()) {
                        String setSelectedTest = TextUtils.join(",", getNames);
                        tests.setText(setSelectedTest);
                    }
                }
            }
        }
        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(mContext, GlobalClass.BMC_barcodelists, passvalue);
        sample_list.setAdapter(getPatientSampleDetails);

        delete_patient_test_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BMC_WOE_SummaryActivity.this);
                builder.setTitle("Confirm delete !");
                builder.setMessage(ToastFile.wish_woe_dlt);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                                Intent i = new Intent(BMC_WOE_SummaryActivity.this, BMC_WOE_EditActivity.class);
                                startActivity(i);
                                finish();
                                break;
                        }
                    }
                };


                if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getORDER_NO() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BMC_WOE_SummaryActivity.this);
                    builder.setTitle("Confirm delete !");
                    builder.setMessage(ToastFile.wish_woe_dlt).setPositiveButton("Yes", dialogClickListener).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BMC_WOE_SummaryActivity.this);
                    builder.setTitle("Confirm delete !");
                    builder.setMessage(ToastFile.wish_woe_dlt).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("Edit", dialogClickListener).show();
                }
            }
        });

        if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls() != null) {
            if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getTRF() != null && GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getReceipt() != null)
                ll_view_images.setVisibility(View.VISIBLE);
            else
                ll_view_images.setVisibility(View.GONE);
        } else
            ll_view_images.setVisibility(View.GONE);

        tv_view_TRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls() != null) {
                    if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getTRF() != null) {
                        String url = GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getTRF().replaceAll("\\\\", "//");
                        showImage(url);
                    } else
                        Toast.makeText(BMC_WOE_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(BMC_WOE_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
            }
        });

        tv_view_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls() != null) {
                    if (GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getReceipt() != null) {
                        String url = GlobalClass.summary_models.get(0).getWoeditlist().getUrls().getReceipt().replaceAll("\\\\", "//");
                        showImage(url);
                    } else
                        Toast.makeText(BMC_WOE_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(BMC_WOE_SummaryActivity.this, "Image not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(BMC_WOE_SummaryActivity.this);

        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.getBCTforSummary + api_key + "/" + user + "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response" + response);
                Gson gson = new Gson();
                MyPojo myPojo = gson.fromJson(response.toString(), MyPojo.class);
                BCT_LIST[] bct_lists = myPojo.getMASTERS().getBCT_LIST();

                if (bct_lists.length != 0) {
                    for (int i = 0; i < bct_lists.length; i++) {
                        //if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBCT_ID().equals(bct_lists[i].getNED_NUMBER())) {
                        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBCT_ID().equals(bct_lists[i].getNED_NUMBER()) && bct_lists[i].getNED_NUMBER() != null) {
                            String nameofbtech = bct_lists[i].getNAME();
                            btech.setText(nameofbtech);
                            btechtile.setVisibility(View.VISIBLE);
                            btechlinear.setVisibility(View.VISIBLE);
                        } else {
                            btech.setVisibility(View.GONE);
                            btechtile.setVisibility(View.GONE);
                        }
                    }
                } else {
                    btech.setVisibility(View.GONE);
                    btechtile.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequestfetchData.setRetryPolicy(new DefaultRetryPolicy(500000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequestfetchData);
        Log.e(TAG, "getDataFromServer: URL" + jsonObjectRequestfetchData);
    }

    private void deletePatientDetailsandTest() {

        deletePatienDetail = Volley.newRequestQueue(BMC_WOE_SummaryActivity.this);
        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", GlobalClass.getPatientIdforDeleteDetails);
            jsonObjectOtp.put("tsp", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG, "onResponse: " + response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    error = parentObjectOtp.getString("error");
                    pid = parentObjectOtp.getString("pid");
                    response1 = parentObjectOtp.getString("response");
                    barcodes = parentObjectOtp.getString("barcodes");
                    resID = parentObjectOtp.getString("RES_ID");

                    if (resID.equals("RES0000")) {
                        TastyToast.makeText(BMC_WOE_SummaryActivity.this, ToastFile.woe_dlt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Intent intent = new Intent(BMC_WOE_SummaryActivity.this, BMC_MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        GlobalClass.setFlagBackToWoe = true;
                        startActivity(intent);
                        finish();
                    } else {

                        TastyToast.makeText(BMC_WOE_SummaryActivity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(BMC_WOE_SummaryActivity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        deletePatienDetail.add(jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);
    }

    private void showImage(String url) {
        dialog = new Dialog(BMC_WOE_SummaryActivity.this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        Glide.with(BMC_WOE_SummaryActivity.this)
                .load(url)
                .placeholder(getResources().getDrawable(R.drawable.img_no_img_aval))
                .into(imgView);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
