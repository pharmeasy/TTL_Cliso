package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.BillingSummaryAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.BillingSummary;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BillingSummaryMOdel;
import com.example.e5322.thyrosoft.Models.RequestModels.BillingSummaryRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.BillingSummaryResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BillingActivity extends AppCompatActivity {

    ImageView back;
    public static RequestQueue PostQue;
    public String toDateTxt = "";
    public String fromDateTxt = "";
    public String Date = "", tempMonth = "";
    TextView txtFromDate, txt_to_date;
    // ProgressDialog barProgressDialog;
    SharedPreferences sharedpreferences;
    ListView list_billingSummary;
    BillingSummaryAdapter adapter;
    String user;
    String passwrd;
    LinearLayout parent_ll;
    LinearLayout offline_img;
    String access;
    java.util.Date daysBeforeDate;
    String api_key;
    int fromday, frommonth, fromyear;
    SharedPreferences prefsBilling;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private SimpleDateFormat sdf;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BillingSummary.OnFragmentInteractionListener mListener;
    private int mYear, mMonth, mDay;
    private DatePicker dpAppointmentDate;
    private java.util.Date result, fromDate, toDate;
    private SimpleDateFormat format;
    TextView txt_nodata, tv_total_billing, tv_total_wl;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();
        calenderView();


        txtFromDate.setText(fromDateTxt);
        txt_to_date.setText(toDateTxt);

        prefsBilling = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefsBilling.getString("Username", null);
        passwrd = prefsBilling.getString("password", null);
        access = prefsBilling.getString("ACCESS_TYPE", null);
        api_key = prefsBilling.getString("API_KEY", null);

        if (!GlobalClass.isNetworkAvailable(this)) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
            GetData();
        }

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == txtFromDate) {
                    final Calendar c = Calendar.getInstance();
                    c.setTime(fromDate);
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(BillingActivity.this, R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                    try {
                                        fromDate = sdf.parse(getDateSecond);
                                        fromDateTxt = sdf.format(fromDate);
                                        txtFromDate.setText(fromDateTxt);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (!GlobalClass.isNetworkAvailable(BillingActivity.this)) {
                                        offline_img.setVisibility(View.VISIBLE);
                                        parent_ll.setVisibility(View.GONE);
                                    } else {
                                        offline_img.setVisibility(View.GONE);
                                        parent_ll.setVisibility(View.VISIBLE);
                                        GetData();
                                    }
                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.getDatePicker().setMaxDate(toDate.getTime() - 2);
                    datePickerDialog.show();
                }
            }
        });


        txt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txt_to_date) {
                    final Calendar c = Calendar.getInstance();
                    c.setTime(toDate);
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    long time3 = c.getTimeInMillis();

                    DatePickerDialog datePickerDialog = new DatePickerDialog(BillingActivity.this, R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                                    try {
                                        toDate = sdf.parse(getDateSecond);
                                        toDateTxt = sdf.format(toDate);
                                        txt_to_date.setText(toDateTxt);
                                        System.out.println(txt_to_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (!GlobalClass.isNetworkAvailable(BillingActivity.this)) {
                                        offline_img.setVisibility(View.VISIBLE);
                                        parent_ll.setVisibility(View.GONE);
                                    } else {
                                        offline_img.setVisibility(View.GONE);
                                        parent_ll.setVisibility(View.VISIBLE);
                                        GetData();
                                    }

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(fromDate.getTime());
                    datePickerDialog.getDatePicker().setMaxDate(result.getTime() - 2);
                    datePickerDialog.show();
                }
            }
        });

    }

    private void GetData() {
        final ProgressDialog barProgressDialog = GlobalClass.ShowprogressDialog(BillingActivity.this);

        PostQue = GlobalClass.setVolleyReq(BillingActivity.this);

        JSONObject jsonObject = null;
        try {
            String inputDate = txtFromDate.getText().toString();
            String inputDate1 = txt_to_date.getText().toString();


            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = null;
            try {
                date = inputFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date);


            Date date1 = null;
            try {
                date1 = inputFormat.parse(inputDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr1 = outputFormat.format(date1);

            BillingSummaryRequestModel requestModel = new BillingSummaryRequestModel();
            requestModel.setApiKey(api_key);
            requestModel.setDate(Date);
            requestModel.setUserCode(user);
            requestModel.setFromDate(outputDateStr);
            requestModel.setToDate(outputDateStr1);

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = GlobalClass.setVolleyReq(BillingActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.billingSUMLIVE, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            GlobalClass.hideProgress(BillingActivity.this, barProgressDialog);

                            BillingSummaryResponseModel responseModel = new Gson().fromJson(String.valueOf(response), BillingSummaryResponseModel.class);
                            if (responseModel != null) {
                                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(Constants.caps_invalidApikey)) {
                                    GlobalClass.redirectToLogin(BillingActivity.this);
                                } else {
                                    if (responseModel.getBillingList() != null && responseModel.getBillingList().size() > 0) {
                                        list_billingSummary.setVisibility(View.VISIBLE);
                                        txt_nodata.setVisibility(View.GONE);
                                        adapter = new BillingSummaryAdapter(BillingActivity.this, responseModel.getBillingList());
                                        list_billingSummary.setAdapter(adapter);
                                        showTotalWlCount(responseModel.getBillingList());
                                        showTotalBillingCount(responseModel.getBillingList());
                                    }else {
                                        list_billingSummary.setVisibility(View.GONE);
                                        txt_nodata.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Toast.makeText(BillingActivity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: json" + jsonObject);
    }

    private void calenderView() {
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        toDateTxt = sdf.format(cl.getTime());
        try {
            toDate = sdf.parse(toDateTxt);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calbackdate = Calendar.getInstance();
        calbackdate.setTime(new Date());
        calbackdate.add(Calendar.DAY_OF_YEAR, -10);
        daysBeforeDate = calbackdate.getTime();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateTxt = sdf.format(daysBeforeDate);
        try {
            fromDate = sdf.parse(fromDateTxt);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        result = cal.getTime();
    }

    private void initView() {
        txtFromDate = (TextView) findViewById(R.id.txt_from_date);
        txt_to_date = (TextView) findViewById(R.id.txt_to_date);
        txt_nodata = findViewById(R.id.txt_nodata);
        list_billingSummary = (ListView) findViewById(R.id.list_billingSummary);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) findViewById(R.id.parent_ll);


        tv_total_wl = findViewById(R.id.tv_total_wl);
        tv_total_billing = findViewById(R.id.tv_total_billing);
    }

    private void showTotalBillingCount(ArrayList<BillingSummaryMOdel> billingList) {
        if (billingList != null) {
            int total_bill_amount = 0;
            for (int i = 0; i < billingList.size(); i++) {
                if (!GlobalClass.isNull(billingList.get(i).getBilledAmount())) {
                    int amount  = Integer.parseInt(billingList.get(i).getBilledAmount());
                    total_bill_amount += amount;
                }
            }
            tv_total_billing.setText(total_bill_amount+"");
        }
    }

    private void showTotalWlCount(ArrayList<BillingSummaryMOdel> billingList) {
        if (billingList != null) {
            int total_wl_count = 0;
            for (int i = 0; i < billingList.size(); i++) {
                if (!GlobalClass.isNull(billingList.get(i).getWorkLoad())) {
                    int amount  = Integer.parseInt(billingList.get(i).getWorkLoad());
                    total_wl_count += amount;
                }
            }
            tv_total_wl.setText(total_wl_count+"");
        }

    }

}