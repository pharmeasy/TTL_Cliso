package com.example.e5322.thyrosoft.Activity;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.LedgerFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.LedgerSummaryRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.LedgerSummaryResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LedgerActivity extends AppCompatActivity {

    public static RequestQueue PostQue;
    TextView open_bal, closing__bal, credit, debit, billed_amt, cash_cheque, credit_limit, txt_unbillwoe, txt_unbill_woe, txt_unbill_material;
    Spinner year, month;
    Button ledgerdetails;
    ArrayAdapter monthadap;
    LinearLayout parent_ll;
    int monthSEND = 0;
    int thisYear = 0;
    LinearLayout offline_img;
    ArrayList<String> years;
    ArrayList<String> monthlist;
    int thismonth = 0;
    int MonthSEND = 0;
    ProgressDialog barProgressDialog;
    private LedgerFragment.OnFragmentInteractionListener mListener;
    private boolean flagfor1sttime = false;
    private int selectedMonthPosition = 0;
    private String TAG = getClass().getSimpleName();
    ImageView back;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        String CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);
        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        initView();

        try {
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                txt_unbillwoe.setText("Unbilled Scan");
            } else {
                txt_unbillwoe.setText("Unbilled WOE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txt_unbill_material = findViewById(R.id.unbilled_material);

        // TextView dateview = getActivity().findViewById(R.id.show_date);
        // dateview.setVisibility(View.GONE);


        years = new ArrayList<String>();
        monthlist = new ArrayList<String>();
        thismonth = Calendar.getInstance().get(Calendar.MONTH);

        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();

        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear - 2; i--) {
            years.add(Integer.toString(i));

        }

        if (!GlobalClass.isNetworkAvailable(this)) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }


        ArrayAdapter yearadap = new ArrayAdapter(this, R.layout.spinner_property_main, years);
        yearadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearadap);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthlist = new ArrayList<String>();
                years = new ArrayList<String>();
                DateFormatSymbols symbols = new DateFormatSymbols();
                String[] monthNames = symbols.getMonths();

                if (Integer.parseInt(year.getSelectedItem().toString()) == thisYear) {
                    for (int i = 0; i <= thismonth; i++) {
                        monthlist.add(monthNames[i].toString());
                        monthadap = new ArrayAdapter(LedgerActivity.this, R.layout.spinner_property_main, monthlist);
                    }
                } else {
                    monthadap = new ArrayAdapter(LedgerActivity.this, R.layout.spinner_property_main, monthNames);

                }
                monthadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month.setAdapter(monthadap);

                if (Integer.parseInt(year.getSelectedItem().toString()) == thisYear) {
                    if (!flagfor1sttime) {

                        flagfor1sttime = true;
                        month.setSelection(thismonth);
                    } else {
                        if (selectedMonthPosition > thismonth) {
                            month.setSelection(thismonth);
                        } else {
                            month.setSelection(selectedMonthPosition);
                        }

                    }

                } else {
                    month.setSelection(selectedMonthPosition);
                }

                for (int i = 0; i < monthNames.length; i++) {
                    if (month.getSelectedItem().equals(monthNames[i])) {
                        monthSEND = i + 1;
                        break;
                    }
                }

                ledgerdetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LedgerActivity.this, LedgerDetailsActivityNew.class);
                        startActivity(intent);
//                        Ledger_details a2Fragment = new Ledger_details();
//                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                        transaction.addToBackStack(null);
//                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
//                        Intent i = new Intent(LedgerActivity.this, LedgerDetailsActivity.class);
//                        startActivity(i);

                    }
                });


                if (!GlobalClass.isNetworkAvailable(LedgerActivity.this)) {
                    offline_img.setVisibility(View.VISIBLE);
                    parent_ll.setVisibility(View.GONE);
                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                    parent_ll.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateFormatSymbols symbols = new DateFormatSymbols();
                String[] monthNames = symbols.getMonths();
                selectedMonthPosition = position;
                for (int i = 0; i < monthNames.length; i++) {
                    if (month.getSelectedItem().equals(monthNames[i])) {
                        monthSEND = i + 1;
                        break;
                    }
                }

                if (!GlobalClass.isNetworkAvailable(LedgerActivity.this)) {
                    offline_img.setVisibility(View.VISIBLE);

                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void GetData() {
        PostQue = GlobalClass.setVolleyReq(this);

        JSONObject jsonObject = null;
        try {
            SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);

            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            LedgerSummaryRequestModel requestModel = new LedgerSummaryRequestModel();
            requestModel.setApiKey(api_key);
            if (monthSEND < 10) {
                requestModel.setMonth("0" + monthSEND);
            } else {
                requestModel.setMonth("" + monthSEND);
            }
            requestModel.setYear(year.getSelectedItem().toString());
            requestModel.setUserCode(user);

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);

            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = GlobalClass.setVolleyReq(this);
        Log.e(TAG, "GetData Request: " + jsonObject.toString());
        Log.e(TAG, "GetData API: " + Api.LedgerSummLive);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.LedgerSummLive, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            Gson gson = new Gson();
                            LedgerSummaryResponseModel responseModel = gson.fromJson(String.valueOf(response),LedgerSummaryResponseModel.class);

                            if (responseModel!=null){
                                if (!GlobalClass.isNull(responseModel.getResponse())&&responseModel.getResponse().equalsIgnoreCase(caps_invalidApikey)){
                                    GlobalClass.redirectToLogin(LedgerActivity.this);
                                }else {
                                    SharedPreferences prefs = getSharedPreferences("profile", MODE_PRIVATE);

                                    String credit_limit_value = prefs.getString(Constants.credit_limit, null);

                                    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("en", "in"));
                                    String opening_bal = responseModel.getOpeningBalance();
                                    String closing_bal = responseModel.getClosingBalance();
                                    String credit_bal = responseModel.getCreditNotes();
                                    String debit_bal = responseModel.getDebitNotes();
                                    String billed_bal = responseModel.getBillAmount();
                                    String cheque_bal = responseModel.getCashCheque();
                                    String unbill_woe = responseModel.getUnbilledWOE();
                                    String unbill_mat = responseModel.getUnbilledMaterial();


                                    double openingbal = Double.parseDouble(opening_bal);
                                    double closingbal = Double.parseDouble(closing_bal);
                                    double creditbal = Double.parseDouble(credit_bal);
                                    double debitbal = Double.parseDouble(debit_bal);
                                    double billedbal = Double.parseDouble(billed_bal);
                                    double chequebal = Double.parseDouble(cheque_bal);
                                    double credit_bal_ltd = Double.parseDouble(credit_limit_value);
                                    double unbillwoe = Double.parseDouble(unbill_woe);
                                    double unbillmat = Double.parseDouble(unbill_mat);

                                    String ope_bal_str = numberFormat.format(openingbal);
                                    String clos_bal_str = numberFormat.format(closingbal);
                                    String credit_bal_str = numberFormat.format(creditbal);
                                    String debit_bal_str = numberFormat.format(debitbal);
                                    String billed_bal_str = numberFormat.format(billedbal);
                                    String cheque_bal_str = numberFormat.format(chequebal);
                                    String str_unbillwoe = numberFormat.format(unbillwoe);
                                    String str_unbillmat = numberFormat.format(unbillmat);
                                    String credit_bal_str_value = numberFormat.format(credit_bal_ltd);

                                    open_bal.setText(ope_bal_str);
                                    closing__bal.setText(clos_bal_str);
                                    credit.setText(credit_bal_str);
                                    debit.setText(debit_bal_str);
                                    billed_amt.setText(billed_bal_str);
                                    cash_cheque.setText(cheque_bal_str);
                                    txt_unbill_woe.setText(str_unbillwoe);
                                    txt_unbill_material.setText(str_unbillmat);

                                    credit_limit.setText("Credit Limit :" + credit_bal_str_value);
                                }
                            }else {
                                Toast.makeText(LedgerActivity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            Log.e(TAG, "on error-->" + e.getLocalizedMessage());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(jsonObjectRequest);
        /*TODO below line is added for handle out of memory for Volly*/
        jsonObjectRequest.setShouldCache(false);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: json" + jsonObject);
    }

    private void initView() {

        open_bal = findViewById(R.id.open_bal);
        credit_limit = findViewById(R.id.credit_limit);
        closing__bal = findViewById(R.id.closing__bal);
        credit = findViewById(R.id.credit);
        debit = findViewById(R.id.debit);
        billed_amt = findViewById(R.id.billed_amt);
        cash_cheque = findViewById(R.id.cash_cheque);
        offline_img = findViewById(R.id.offline_img);
        parent_ll = findViewById(R.id.parent_ll);

        txt_unbill_woe = findViewById(R.id.unbilled_woe);
        txt_unbillwoe = findViewById(R.id.txt_unbillwoe);

        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        ledgerdetails = findViewById(R.id.ledgerdetails);
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LedgerActivity.this, MyProfile_activity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}