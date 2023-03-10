package com.example.e5322.thyrosoft.Activity;

import static com.example.e5322.thyrosoft.API.Constants.tab_flag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetLeadgerDetailsController;
import com.example.e5322.thyrosoft.Controller.POSTBookLeadController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BookLeadPOSTModel;
import com.example.e5322.thyrosoft.Models.GetLeadgerBalnce;
import com.example.e5322.thyrosoft.Models.LeadBookingResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.BundleConstants;
import com.example.e5322.thyrosoft.Models.PostInclisionMaterial;
import com.example.e5322.thyrosoft.Models.getInclusionMasterModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmbookDetail extends AppCompatActivity implements View.OnClickListener {
    public TextView tv_toolname, tv_pname, tv_gender, tv_diabetics, tv_dob, tv_mobileno, tv_email, tv_city, tv_remarks, tv_servicetype, tv_avaibal, txt_inclusionmnt, tv_paidamt, txt_finalpaidamt, txt_appoint_dt, txt_age;
    public String pname, gender, diabetics, dob, mobileno, emailId, city, remarks, sercicetype, availbal, scanAmount, finalTotalAmount, amt_coll, header, appoint_dt;
    public LinearLayout lin_email, lin_remarks, lin_age, lin_dob;
    public Button btn_confirm, btn_reset;
    public ProgressDialog progressDialog;
    public String TAG = getClass().getSimpleName();
    public ConnectionDetector cd;
    public RequestQueue PostQueOtp;
    public String RESPONSE;
    public String api_key, fname, mname, lname;
    public SharedPreferences prefs;
    public String name_tsp, user, passwrd, access, email_id, usercode;
    public PaymentParams mPaymentParams;
    public PayuConfig payuConfig;
    String centerId, serviceId, calage, str_slot;
    private Gson gson;
    private int fdupl_incl_Amnt = 0;
    LinearLayout ll_inclusionamnt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_confbook);
        initView();
        setData();
        GetLeadgerbal();

    }

    private void GetLeadgerbal() {

        GetLeadgerDetailsController getLeadgerDetailsController = new GetLeadgerDetailsController(this, ConfirmbookDetail.this);
        getLeadgerDetailsController.getLedgerBal();

    }

    private void initView() {

        Bundle bundle = getIntent().getExtras();
        cd = new ConnectionDetector(ConfirmbookDetail.this);

        Payu.setInstance(this);
        int environment = PayuConstants.STAGING_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        mPaymentParams = new PaymentParams();

        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
        name_tsp = getProfileName.getString("name", "");
        usercode = getProfileName.getString("user_code", "");
        email_id = getProfileName.getString("email", "");

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        api_key = prefs.getString("API_KEY", "");

        if (bundle != null) {

            pname = bundle.getString(Constants.PNAME, "");
            fname = bundle.getString(Constants.FNAME, "");
            mname = bundle.getString(Constants.MNAME, "");
            lname = bundle.getString(Constants.LNAME, "");
            header = bundle.getString(Constants.HEADER, "");
            centerId = bundle.getString(Constants.CENTERID, "");
            serviceId = bundle.getString(Constants.SERVICEID, "");
            calage = bundle.getString(Constants.CALAGE, "");
            str_slot = bundle.getString(Constants.SLOT, "");


            gender = bundle.getString(Constants.PGENDER, "");
            diabetics = bundle.getString(Constants.PDIEB, "");
            dob = bundle.getString(Constants.PDOB, "");
            appoint_dt = bundle.getString(Constants.appointdate, "");
            mobileno = bundle.getString(Constants.PMOB, "");
            emailId = bundle.getString(Constants.PEMAIL, "");
            city = bundle.getString(Constants.PCITY, "");
            remarks = bundle.getString(Constants.PREMARK, "");
            sercicetype = bundle.getString(Constants.SERVICETYPE, "");
            availbal = bundle.getString(Constants.AVAILBAL, "");
            scanAmount = bundle.getString(Constants.PAID_BAL, "");
            finalTotalAmount = bundle.getString(Constants.PAID_FINALBAL, "");
            amt_coll = bundle.getString(Constants.AMT_Coll, "");
        }

        tv_toolname = findViewById(R.id.txt_name);
        tv_toolname.setText("Booking Detail");

        tv_pname = findViewById(R.id.txt_pname);
        tv_gender = findViewById(R.id.txt_gender);
        tv_diabetics = findViewById(R.id.txt_diabetics);
        tv_dob = findViewById(R.id.txt_dob);
        tv_mobileno = findViewById(R.id.txt_mobileno);
        tv_email = findViewById(R.id.txt_emailid);
        tv_city = findViewById(R.id.txt_city);
        tv_remarks = findViewById(R.id.txt_remarks);
        tv_servicetype = findViewById(R.id.txt_servicetype);
        tv_avaibal = findViewById(R.id.txt_avaibal);
        tv_paidamt = findViewById(R.id.txt_paidamt);
        txt_inclusionmnt = findViewById(R.id.txt_inclusionmnt);
        txt_finalpaidamt = findViewById(R.id.txt_finalpaidamt);
        txt_appoint_dt = findViewById(R.id.txt_appoint_dt);
        txt_age = findViewById(R.id.txt_age);

        lin_dob = findViewById(R.id.lin_dob);
        lin_age = findViewById(R.id.lin_age);
        ll_inclusionamnt = findViewById(R.id.ll_inclusionamnt);

        lin_email = findViewById(R.id.lin_email);
        lin_remarks = findViewById(R.id.lin_remarks);

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

    }

    private void setData() {

        try {
            tv_pname.setText(fname + " " + mname + " " + lname);
            tv_gender.setText(gender);
            tv_diabetics.setText(diabetics);

            tv_mobileno.setText(mobileno);

            if (GlobalClass.isNull(dob)) {
                lin_dob.setVisibility(View.GONE);
            } else {
                lin_dob.setVisibility(View.VISIBLE);
                tv_dob.setText(dob);
            }


            if (GlobalClass.isNull(calage)) {
                lin_age.setVisibility(View.GONE);
            } else {
                lin_age.setVisibility(View.VISIBLE);
                GlobalClass.SetText(txt_age, calage);
            }


            if (!GlobalClass.isNull(emailId)) {
                lin_email.setVisibility(View.VISIBLE);
                tv_email.setText(emailId);
            } else {
                lin_email.setVisibility(View.GONE);
            }


            if (!GlobalClass.isNull(remarks)) {
                lin_remarks.setVisibility(View.VISIBLE);
                tv_remarks.setText(remarks);
            } else {
                lin_remarks.setVisibility(View.GONE);
            }

            tv_city.setText(city);
            tv_servicetype.setText(sercicetype);
            txt_appoint_dt.setText(appoint_dt);


            GlobalClass.SetText(tv_paidamt, "Rs. " + GlobalClass.currencyFormat(scanAmount));
            GlobalClass.SetText(txt_finalpaidamt, "Rs. " + GlobalClass.currencyFormat(finalTotalAmount));
            setinClusionAmount();
            //  GlobalClass.SetText(tv_avaibal, GlobalClass.currencyFormat(availbal));


            if (Integer.parseInt(availbal) >= Integer.parseInt(scanAmount)) {
                //Booking Flow
                btn_confirm.setVisibility(View.VISIBLE);
            } else {
                btn_confirm.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void setinClusionAmount() {
        int sl_itemmnt = setTotalDuplInclusionAmount();
        if (sl_itemmnt == 0) {
            GlobalClass.SetText(txt_inclusionmnt, "");
            ll_inclusionamnt.setVisibility(View.GONE);
        } else {
            ll_inclusionamnt.setVisibility(View.VISIBLE);
            GlobalClass.SetText(txt_inclusionmnt, "Rs. " + GlobalClass.currencyFormat(String.valueOf(sl_itemmnt)));
        }
    }

    private int setTotalDuplInclusionAmount() {
        try {
            fdupl_incl_Amnt = 0;
            if (BundleConstants.lists_incl_dupl != null) {
                if (BundleConstants.lists_incl_dupl.size() != 0) {
                    for (int i = 0; i < BundleConstants.lists_incl_dupl.size(); i++) {
                        if (BundleConstants.lists_incl_dupl.get(i).isSelected()) {
                            fdupl_incl_Amnt = fdupl_incl_Amnt + (GlobalClass.getIntVal(BundleConstants.lists_incl_dupl.get(i).getUnitCost()) * BundleConstants.lists_incl_dupl.get(i).getSelectedqty());
                        } else {

                        }
                    }
                }
            }
            return fdupl_incl_Amnt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fdupl_incl_Amnt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_reset:
                onBackPressed();
                break;


            case R.id.btn_confirm:
                if (cd.isConnectingToInternet()) {
                    bookLeadAPICall();
                } else {
                    GlobalClass.toastyError(ConfirmbookDetail.this, MessageConstants.CHECK_INTERNET_CONN, false);
                }
                break;

        }
    }


    private void bookLeadAPICall() {
        BookLeadPOSTModel BookLeadPOSTModel = new BookLeadPOSTModel();
        BookLeadPOSTModel.setFirstName(fname);
        BookLeadPOSTModel.setMiddleName(mname);
        BookLeadPOSTModel.setLastName(lname);
        BookLeadPOSTModel.setAddress("");
        BookLeadPOSTModel.setPincode("");
        BookLeadPOSTModel.setMobile(mobileno);
        BookLeadPOSTModel.setEmailId(emailId);
        if (gender.equalsIgnoreCase("Male")) {
            BookLeadPOSTModel.setGender("1");
        } else {
            BookLeadPOSTModel.setGender("2");
        }


        BookLeadPOSTModel.setDOB(dob);
        BookLeadPOSTModel.setAge(calage);
        BookLeadPOSTModel.setAadharNo("");
        BookLeadPOSTModel.setAdditionalMob("");
        BookLeadPOSTModel.setRemarks(remarks + " Booked From Cliso App");
        BookLeadPOSTModel.setApptDate(GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, appoint_dt) + " " + str_slot);
        BookLeadPOSTModel.setDoctorId("");
        BookLeadPOSTModel.setCenterId(centerId);
        BookLeadPOSTModel.setSourceId("");
        BookLeadPOSTModel.setReference_Id(user);
        BookLeadPOSTModel.setRelativeName("");
        BookLeadPOSTModel.setRelativeMobNo("");
        BookLeadPOSTModel.setLeadMode("");
        BookLeadPOSTModel.setLeadSource("NHF");
        BookLeadPOSTModel.setRelation("");

        if (diabetics.equalsIgnoreCase("Yes")) {
            BookLeadPOSTModel.setDiabetes("true");
        } else {
            BookLeadPOSTModel.setDiabetes("false");
        }

        BookLeadPOSTModel.setReportDispAddress("");
        BookLeadPOSTModel.setReportDispPincode("");
        BookLeadPOSTModel.setServiceId(serviceId);
        BookLeadPOSTModel.setOldCD("");
        BookLeadPOSTModel.setReport("");
        BookLeadPOSTModel.setReferralSheet("");
        BookLeadPOSTModel.setThyrocare_Api_key(api_key);

        JSONObject jsonObject = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        try {
            jsonObject = new JSONObject(gson.toJson(BookLeadPOSTModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ControllersGlobalInitialiser.POSTBookLeadController != null) {
            ControllersGlobalInitialiser.POSTBookLeadController = null;
        }
        ControllersGlobalInitialiser.POSTBookLeadController = new POSTBookLeadController(ConfirmbookDetail.this, ConfirmbookDetail.this, header);
        ControllersGlobalInitialiser.POSTBookLeadController.postBookLead(true, jsonObject);

    }

    public void getPOSTBookLeadResponse(JSONObject response) {
        Gson gson = new Gson();
        LeadBookingResponseModel leadBookingResponseModel = gson.fromJson(response.toString(), LeadBookingResponseModel.class);
        if (leadBookingResponseModel.getRespId() != null) {
            if (leadBookingResponseModel.getRespId().equalsIgnoreCase(Constants.RES0000)) {
                CallInclusionAPI(leadBookingResponseModel.getLeadId());
                GlobalClass.toastySuccess(ConfirmbookDetail.this, leadBookingResponseModel.getResponseMessage(), false);
                startActivity(new Intent(ConfirmbookDetail.this, ManagingTabsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                tab_flag = "1";
            } else {
                GlobalClass.toastyError(ConfirmbookDetail.this, leadBookingResponseModel.getResponseMessage(), false);
            }
        }
    }

    private void CallInclusionAPI(String leadId) {
        if (getSelectedinClusionMaterialWithOther().size() != 0) {
            CallgenerateInclusionModel(leadId);
        }
    }

    private void CallgenerateInclusionModel(String leadId) {
        try {
            PostInclisionMaterial entity = new PostInclisionMaterial();
            entity.setLeadId(leadId);
            entity.setLstMaterial(getSelectedinClusionMaterialWithOther());


            entity.setHasOtherCharges(false);
            entity.setOtherRemarks("");
            entity.setOtherAmount(0);

            entity.setRefNo("");
            entity.setAmount(setTotalDuplInclusionAmount());
            entity.setRemarks("");
            entity.setMode("" + 11);
            entity.setThyrocare_Api_key(api_key);

            CallAPIForSubmitInclusion(entity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void CallAPIForSubmitInclusion(PostInclisionMaterial entity) {
        try {
            if (ControllersGlobalInitialiser.POSTBookLeadController != null) {
                ControllersGlobalInitialiser.POSTBookLeadController = null;
            }

            if (cd.isConnectingToInternet()) {
                ControllersGlobalInitialiser.POSTBookLeadController = new POSTBookLeadController(ConfirmbookDetail.this, ConfirmbookDetail.this, header);
                ControllersGlobalInitialiser.POSTBookLeadController.CallSubMitInClusiondataAPI(entity);
            } else {
                GlobalClass.toastyError(ConfirmbookDetail.this, MessageConstants.CHECK_INTERNET_CONN, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<getInclusionMasterModel> getSelectedinClusionMaterialWithOther() {
        ArrayList<getInclusionMasterModel> entity = new ArrayList<>();
        try {
            getInclusionMasterModel ent = null;

            if (BundleConstants.lists_incl_dupl != null) {
                if (BundleConstants.lists_incl_dupl.size() != 0) {
                    for (int i = 0; i < BundleConstants.lists_incl_dupl.size(); i++) {
                        if (BundleConstants.lists_incl_dupl.get(i).isSelected()) {
                            if (!BundleConstants.lists_incl_dupl.get(i).getName().equalsIgnoreCase("OTHERS")) {
                                ent = new getInclusionMasterModel();
                                ent.setMCode(BundleConstants.lists_incl_dupl.get(i).getMCode());
                                ent.setQty("" + BundleConstants.lists_incl_dupl.get(i).getSelectedqty());
                                ent.setMaterial_name(BundleConstants.lists_incl_dupl.get(i).getName());

                                entity.add(ent);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }


    public void getPOSTBookLeadErrorResponse(String error) {
        onAPIResponseError(error);
    }

    private void onAPIResponseError(String error) {
        try {
            JSONObject jsonObject = new JSONObject(error);
            String message = jsonObject.optString("Message", "");
            if (message.contains(MessageConstants.AUTH_FAILED)) {
                //  GlobalClass.logout(ConfirmbookDetail.this);
                startActivity(new Intent(ConfirmbookDetail.this, Login.class));
            }
            GlobalClass.toastyError(ConfirmbookDetail.this, message, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getLeaderDetails(GetLeadgerBalnce body) {
        tv_avaibal.setText("Rs. " + GlobalClass.currencyFormat(String.valueOf(body.getBalance())));
    }
}