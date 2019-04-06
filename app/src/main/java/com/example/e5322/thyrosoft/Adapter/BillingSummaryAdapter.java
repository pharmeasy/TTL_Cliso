package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.BillingDetailsActivity;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BillingSummaryMOdel;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e5426@thyrocare.com on 16/5/18.
 */

public class BillingSummaryAdapter extends BaseAdapter {

    Context mContext;
    SharedPreferences prefs;
    ArrayList<BillingSummaryMOdel> billdata = new ArrayList<>();
    public static RequestQueue PostQue;
    SharedPreferences sharedpreferences;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    ProgressDialog barProgressDialog;

    public BillingSummaryAdapter(Context ManagingTabsActivity, ArrayList<BillingSummaryMOdel> arrayList) {

        sharedpreferences = ManagingTabsActivity.getSharedPreferences(Constants.MyPREFERENCES, MODE_PRIVATE);
        prefs = ManagingTabsActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        mContext = ManagingTabsActivity;
        billdata = arrayList;

    }

    @Override
    public int getCount() {
        return billdata.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.billing_sum_row, parent, false);
        }

        barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        TableRow tr_itlcr=(TableRow) convertView.findViewById(R.id.tr_itlcr);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView wl = (TextView) convertView.findViewById(R.id.WL);
        TextView billling = (TextView) convertView.findViewById(R.id.BILLING);


        date.setText(billdata.get(position).getBillingDate());

        SpannableString content = new SpannableString((billdata.get(position).getWorkLoad()));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        wl.setText(content);

        billling.setText(billdata.get(position).getBilledAmount() + "");


        tr_itlcr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                {
                    String DateSTR = billdata.get(position).getBillingDate(); //14/05/2018

                    try {
                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = inputFormat.parse(DateSTR);
                        String outputDateStr = outputFormat.format(date);


                        GetData(outputDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return convertView;

    }

    private void GetData(String Datestr) {

        barProgressDialog.show();
        PostQue = Volley.newRequestQueue(mContext);

        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put(Constants.API_KEY_billing, prefs.getString(Constants.API_KEY, ""));
            jsonObject.put(Constants.date, Datestr);
            jsonObject.put(Constants.UserCode_billing, prefs.getString("Username", ""));
            jsonObject.put(Constants.fromDate, "");
            jsonObject.put(Constants.toDate, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.billingDetLIVE, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            JSONArray jsonArray = response.optJSONArray(Constants.billingDetails);

                            if (jsonArray != null) {

                                GlobalClass.billingDETArray = new ArrayList<billingDetailsModel>();
                                GlobalClass.billingDETheaderArray = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    try {

                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        billingDetailsModel billDET = new billingDetailsModel();

                                        billDET.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                        billDET.setBilledAmount(jsonObject.optString(Constants.billedAmount).toString());
                                        billDET.setCollectedAmount(jsonObject.optString(Constants.collectedAmount).toString());
                                        billDET.setPatient(jsonObject.optString(Constants.patient).toString());
                                        billDET.setTests(jsonObject.optString(Constants.tests).toString());
                                        billDET.setWoetype(jsonObject.optString(Constants.woetype).toString());
                                        billDET.setRefId(jsonObject.optString(Constants.refId).toString());


                                        GlobalClass.billingDETArray.add(billDET);
                                        GlobalClass.billingDETheaderArray.add(jsonObject.optString(Constants.patient).toString());
                                    } catch (Exception e) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                /*BillingDetails a2Fragment = new BillingDetails();
                                FragmentTransaction transaction =a2Fragment.getChildFragmentManager().beginTransaction();
                                transaction.addToBackStack(null);
                                transaction.replace(R.id.fragment_mainLayout,a2Fragment);
                                transaction.commit();*/


                               /* BillingDetails BILLDET = new BillingDetails();
                                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_mainLayout,BILLDET);
                                fragmentTransaction.commit();*/

                                Intent i = new Intent(mContext, BillingDetailsActivity.class);
                                mContext.startActivity(i);


                            }

                        } catch (Exception e) {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObject);

    }
}