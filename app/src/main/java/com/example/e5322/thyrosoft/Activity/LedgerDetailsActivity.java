package com.example.e5322.thyrosoft.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.Fragment1_ledgerDet_adapter;
import com.example.e5322.thyrosoft.Adapter.VIewPagerAdapter_ledgerDet;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.Ledger_details;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class LedgerDetailsActivity extends AppCompatActivity {
    ViewPager viewpager;
    private Ledger_details.OnFragmentInteractionListener mListener;
    Spinner year, month;
    TextView all, credit, debit;
    int monthSEND = 0;
    int thisYear = 0;
    LinearLayout containerlist;
    ArrayList<String> years;
    ArrayList<String> monthlist;
    int thismonth = 0;
    private int selectedMonthPosition = 0;
    int yearSpinner = 0;
    public static RequestQueue PostQue;
    ArrayAdapter monthadap;
    ProgressDialog barProgressDialog;
    Fragment1_ledgerDet_adapter adapter;
    private String TAG = Ledger_details.class.getSimpleName();
    DateFormatSymbols symbols = new DateFormatSymbols();
    String[] monthNames = symbols.getMonths();
    private boolean flagfor1sttime = false;
    private LinearLayout offline_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_details);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        month = (Spinner) findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.year);

        all = (TextView) findViewById(R.id.alltab);
        credit = (TextView) findViewById(R.id.credittab);
        debit = (TextView) findViewById(R.id.debit);

        containerlist = (LinearLayout) findViewById(R.id.containerlist);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);

        all.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
        all.setTextColor(getResources().getColor(R.color.colorWhite));
        years = new ArrayList<String>();
        monthlist = new ArrayList<String>();
        thismonth = Calendar.getInstance().get(Calendar.MONTH);
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();

        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear - 2; i--) {
            years.add(Integer.toString(i));

        }
        ArrayAdapter yearadap = new ArrayAdapter(LedgerDetailsActivity.this, R.layout.spinner_property_main, years);
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
                        monthadap = new ArrayAdapter(LedgerDetailsActivity.this, R.layout.spinner_property_main, monthlist);
                    }
                } else {
                    monthadap = new ArrayAdapter(LedgerDetailsActivity.this, R.layout.spinner_property_main, monthNames);

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

                if (!GlobalClass.isNetworkAvailable(LedgerDetailsActivity.this)) {
                    offline_img.setVisibility(View.VISIBLE);
                    containerlist.setVisibility(View.GONE);
                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                    containerlist.setVisibility(View.VISIBLE);
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


                if (!GlobalClass.isNetworkAvailable(LedgerDetailsActivity.this)) {
                    offline_img.setVisibility(View.VISIBLE);
                    containerlist.setVisibility(View.GONE);
                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                    containerlist.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        monthlist = new ArrayList<String>();
        symbols = new DateFormatSymbols();

        monthNames = symbols.getMonths();

        initDialog();
        if (barProgressDialog.isShowing()) {
            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                barProgressDialog.dismiss();
            }
        }
        //  TextView dateview = LedgerDetailsActivity.this.findViewById(R.id.show_date);

        //dateview.setVisibility(View.GONE);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    viewpager.setCurrentItem(0);
                    all.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                    all.setTextColor(getResources().getColor(R.color.colorWhite));
                    credit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    credit.setTextColor(getResources().getColor(R.color.colorBlack));
                    debit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    debit.setTextColor(getResources().getColor(R.color.colorBlack));

                } else if (position == 1) {
                    viewpager.setCurrentItem(1);

                    credit.setBackground(getResources().getDrawable(R.drawable.maroon_rect_box));
                    credit.setTextColor(getResources().getColor(R.color.colorWhite));
                    all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    all.setTextColor(getResources().getColor(R.color.colorBlack));
                    debit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    debit.setTextColor(getResources().getColor(R.color.colorBlack));

                } else {
                    viewpager.setCurrentItem(2);

                    debit.setBackground(getResources().getDrawable(R.drawable.maroon_rect_right_round));
                    debit.setTextColor(getResources().getColor(R.color.colorWhite));
                    all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    all.setTextColor(getResources().getColor(R.color.colorBlack));
                    credit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    credit.setTextColor(getResources().getColor(R.color.colorBlack));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewpager.getCurrentItem() != 0) {
                    viewpager.setCurrentItem(0);
                }
                all.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                all.setTextColor(getResources().getColor(R.color.colorWhite));
                credit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                credit.setTextColor(getResources().getColor(R.color.colorBlack));
                debit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                debit.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
                credit.setBackground(getResources().getDrawable(R.drawable.maroon_rect_box));
                credit.setTextColor(getResources().getColor(R.color.colorWhite));
                all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                all.setTextColor(getResources().getColor(R.color.colorBlack));
                debit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                debit.setTextColor(getResources().getColor(R.color.colorBlack));


            }
        });
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);
                debit.setBackground(getResources().getDrawable(R.drawable.maroon_rect_right_round));
                debit.setTextColor(getResources().getColor(R.color.colorWhite));
                all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                all.setTextColor(getResources().getColor(R.color.colorBlack));
                credit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                credit.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });
    }

    private void initDialog() {
        barProgressDialog = new ProgressDialog(LedgerDetailsActivity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
    }


    private void GetData() {
        Log.e(TAG, "GetData: ");

        barProgressDialog.show();


        PostQue = GlobalClass.setVolleyReq(LedgerDetailsActivity.this);


        JSONObject jsonObject = new JSONObject();
        try {
            // monthSEND= Integer.parseInt(month.getSelectedItem().toString());

            SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);

            String user = prefs.getString("Username", "");
            String passwrd = prefs.getString("password", "");
            String access = prefs.getString("ACCESS_TYPE", "");
            String api_key = prefs.getString("API_KEY", "");

           /* {
                "apiKey":"qpa6@YJ9XY@LP8Hzxn4PFz3M5WU4NaGo)bsELEn8lFY=",
                    "month":"04" ,
                    "userCode":"TC001" ,
                    "year":"2018"
            }*/


            jsonObject.put("apiKey", api_key);//api_key

            if (monthSEND < 10) {
                jsonObject.put(Constants.month, "0" + monthSEND);
            } else {
                jsonObject.put(Constants.month, monthSEND);
            }
            GlobalClass.MONTH=String.valueOf(monthSEND);
            GlobalClass.YEAR=String.valueOf(year.getSelectedItem());


            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put(Constants.year, year.getSelectedItem());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(LedgerDetailsActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.LedgerDetLive, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            JSONArray jsonArray = response.optJSONArray(Constants.ledgerListDetails);
                            JSONArray jsonArraycredit = response.optJSONArray(Constants.credits);
                            JSONArray jsonArraydebit = response.optJSONArray(Constants.debits);

                            GlobalClass.CREDITLIST = new ArrayList<Ledger_DetailsModel>();
                            GlobalClass.DEBIT = new ArrayList<Ledger_DetailsModel>();


                            for (int j = 0; j < jsonArray.length(); j++) {
                                try {

                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Ledger_DetailsModel ledgerModel = new Ledger_DetailsModel();
                                    ledgerModel.setAmount(jsonObject.optString(Constants.amount).toString());
                                    ledgerModel.setAmountType(jsonObject.optString(Constants.amountType).toString());
                                    ledgerModel.setDate(jsonObject.optString(Constants.date).toString());
                                    ledgerModel.setNarration(jsonObject.optString(Constants.narration).toString());
                                    ledgerModel.setTransactionType(jsonObject.optString(Constants.transactionType).toString());

                                    if (ledgerModel.getAmountType().equals("CREDIT")) {
                                        GlobalClass.CREDITLIST.add(ledgerModel);
                                    } else {
                                        GlobalClass.DEBIT.add(ledgerModel);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            GlobalClass.listdata = new ArrayList<String>();
                            JSONArray jArray = (JSONArray) jsonArraycredit;
                            if (jArray != null) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    GlobalClass.listdata.add(jArray.getString(i));
                                }

                            }

                            GlobalClass.debitlist = new ArrayList<String>();
                            JSONArray jArray1 = (JSONArray) jsonArraydebit;
                            if (jArray1 != null) {
                                for (int i = 0; i < jArray1.length(); i++) {
                                    GlobalClass.debitlist.add(jArray1.getString(i));
                                }

                            }
                          /*  GlobalClass.debitlist = new ArrayList<String>();
                            JSONArray jArray1 = (JSONArray)jsonArraydebit;
                            if (jArray != null) {
                                for (int i=0;i<jArray1.length();i++){
                                    GlobalClass.listdata .add(jArray.getString(i));
                                }

                            }
*/
                            viewpager.setAdapter(buildAdapter());

//                            viewpager.setSaveFromParentEnabled(false);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                try {
                    Log.v("error ala parat " ,""+ error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: json" + jsonObject);
    }

    private PagerAdapter buildAdapter() {

        return (new VIewPagerAdapter_ledgerDet(LedgerDetailsActivity.this.getSupportFragmentManager()));
    }

}
