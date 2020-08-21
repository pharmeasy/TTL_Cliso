package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.BillingDetailsActivity;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.Billingdetail_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BillingSummaryMOdel;
import com.example.e5322.thyrosoft.Models.RequestModels.BillingSummaryRequestModel;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

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
    private String TAG = ManagingTabsActivity.class.getSimpleName();
    ArrayList<String> billingDETheaderArray;
    public ArrayList<billingDetailsModel> billingDETArray;

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


        TableRow tr_itlcr = convertView.findViewById(R.id.tr_itlcr);
        TextView date = convertView.findViewById(R.id.date);
        TextView wl = convertView.findViewById(R.id.WL);
        TextView billling = convertView.findViewById(R.id.BILLING);


        GlobalClass.SetText(date, billdata.get(position).getBillingDate());


        GlobalClass.SetSpannable(wl, billdata.get(position).getWorkLoad());


        GlobalClass.SetText(billling, billdata.get(position).getBilledAmount() + "");

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
        JSONObject jsonObject = null;
        try {

            BillingSummaryRequestModel requestModel = new BillingSummaryRequestModel();
            requestModel.setApiKey(prefs.getString(Constants.API_KEY, ""));
            requestModel.setDate(Datestr);
            requestModel.setUserCode(prefs.getString("Username", ""));
            requestModel.setFromDate("");
            requestModel.setToDate("");

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = GlobalClass.setVolleyReq(mContext);

        try {
            if (ControllersGlobalInitialiser.billingdetail_controller != null) {
                ControllersGlobalInitialiser.billingdetail_controller = null;
            }
            ControllersGlobalInitialiser.billingdetail_controller = new Billingdetail_Controller((Activity)mContext, BillingSummaryAdapter.this);
            ControllersGlobalInitialiser.billingdetail_controller.billingcontroller(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getbillresp(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            JSONArray jsonArray = response.optJSONArray(Constants.billingDetails);

            if (jsonArray != null) {
                billingDETheaderArray = new ArrayList<>();
                billingDETArray = new ArrayList<>();

                for (int j = 0; j < jsonArray.length(); j++) {
                    try {

                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        billingDetailsModel billDET = new billingDetailsModel();

                        billDET.setBarcode(jsonObject.optString(Constants.barcode));
                        billDET.setBilledAmount(jsonObject.optString(Constants.billedAmount));
                        billDET.setCollectedAmount(jsonObject.optString(Constants.collectedAmount));
                        billDET.setPatient(jsonObject.optString(Constants.patient));
                        billDET.setTests(jsonObject.optString(Constants.tests));
                        billDET.setWoetype(jsonObject.optString(Constants.woetype));
                        billDET.setRefId(jsonObject.optString(Constants.refId));

                        billingDETArray.add(billDET);
                        billingDETheaderArray.add(jsonObject.optString(Constants.patient));


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                Intent i = new Intent(mContext, BillingDetailsActivity.class);
                i.putStringArrayListExtra("billheaderlist", billingDETheaderArray);
                i.putParcelableArrayListExtra("billingDETArray", billingDETArray);
                mContext.startActivity(i);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}