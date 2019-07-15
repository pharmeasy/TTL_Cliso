package com.example.e5322.thyrosoft.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.ResultDtlAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BarcodeWiseReport_Download extends AppCompatActivity {
    public static RequestQueue PostQue;
    public String toDate = "";
    public String fromDate = "";
    public String Date = "";
    TextView txt_date, nodata;
    ProgressDialog barProgressDialog;
    ImageView back, home;
    Context mContext;
    Spinner spinnertype;
    ResultDtlAdapter adapter;
    Spinner filterBy;
    SearchView searchbarcode;
    ListView ListReportStatus;
    ArrayList<TrackDetModel> FilterReport = new ArrayList<TrackDetModel>();
    String send_Date = "";
    private SimpleDateFormat sdf;
    private String mParam1;
    private String mParam2;
    private com.example.e5322.thyrosoft.Fragment.FilterReport.OnFragmentInteractionListener mListener;
    private String showDate;
    private String passToAPI;
    private String halfTime;
    private String responsetoshow;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_wise_report__download);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        searchbarcode = (SearchView) findViewById(R.id.searchbarcode);
        nodata = (TextView) findViewById(R.id.nodata);

        ListReportStatus = (ListView) findViewById(R.id.ListReportStatus);
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        toDate = sdf.format(cl.getTime());
        Date = sdf.format(cl.getTime());

        fromDate = sdf.format(cl.getTime());

        String getDate = GlobalClass.getYesterdaysDate;

        halfTime = getDate.substring(11, getDate.length() - 0);
        GlobalClass.saveDtaeTopass = halfTime;

        txt_date.setText(GlobalClass.saveDtaeTopass);
        String[] spinner = {"Reported", "Pending", "Cancelled", "CHN"};
        String[] spinnerfilterby = {"All", "Name", "Barcode", "Mobile"};
        ArrayAdapter aa = new ArrayAdapter(BarcodeWiseReport_Download.this, android.R.layout.simple_spinner_item, spinner);
        ArrayAdapter filterby = new ArrayAdapter(BarcodeWiseReport_Download.this, android.R.layout.simple_spinner_item, spinnerfilterby);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterby.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(aa);
        filterBy.setAdapter(filterby);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(BarcodeWiseReport_Download.this);
            }
        });

        searchbarcode.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final List<TrackDetModel> filteredModelList = filter(FilterReport, query);
                adapter.setFilter(filteredModelList);
                ListReportStatus.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    GetData();
                }
                return false;
            }
        });

    }

    private List<TrackDetModel> filter(List<TrackDetModel> models, String query) {
        query = query.toLowerCase();
        final List<TrackDetModel> filteredModelList = new ArrayList<>();
        for (TrackDetModel model : models) {
            final String name = model.getName();
            final String barcode = model.getBarcode();
            final String type = model.getSample_type();
            if (model.getName().toString().contains(query.toUpperCase()) || model.getBarcode().contains(query.toUpperCase())) {
                filteredModelList.add(model);
                adapter.notifyDataSetChanged();
                break;
            }


        }
        return filteredModelList;
    }

    private void GetData() {


        String sendDtaetoAPI = txt_date.getText().toString();


        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd

        java.util.Date date = null;
        try {
            date = inputFormat.parse(sendDtaetoAPI);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);

        PostQue = Volley.newRequestQueue(BarcodeWiseReport_Download.this);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        final String user = prefs.getString("Username", null);
        String passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("API_Key", api_key);
            jsonObject.put("result_type", spinnertype.getSelectedItem().toString());
            jsonObject.put("tsp", user);
            jsonObject.put("tsp", user);
            jsonObject.put("date", GlobalClass.date);
            jsonObject.put("key", "");
            jsonObject.put("value", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(BarcodeWiseReport_Download.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.ResultLIVE + "/" + api_key + "/" + spinnertype.getSelectedItem().toString() + "/" + user + "/" + passToAPI + "/" + "key/value", jsonObject,//passToAPI
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            String reportStatus = response.optString("reportStatus");
                            responsetoshow = response.optString("response");

                            if (reportStatus.equals("ALLOW")) {
                                JSONArray jsonArray = response.optJSONArray(Constants.patients);
                                if (jsonArray != null) {
                                    nodata.setVisibility(View.GONE);
                                    FilterReport = new ArrayList<TrackDetModel>();

                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        TrackDetModel trackdetails = new TrackDetModel();

                                        trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                                        trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                                        trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                                        trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                        // trackdetails.setCancel_tests_with_remark(jsonObject.optString(Constants.cancel_tests_with_remark).toString());
                                        trackdetails.setChn_pending(jsonObject.optString(Constants.chn_pending).toString());
                                        trackdetails.setChn_test(jsonObject.optString(Constants.chn_test).toString());
                                        trackdetails.setConfirm_status(jsonObject.optString(Constants.confirm_status).toString());
                                        trackdetails.setDate(jsonObject.optString(Constants.date).toString());
                                        trackdetails.setEmail(jsonObject.optString(Constants.email).toString());
                                        trackdetails.setIsOrder(jsonObject.optString(Constants.isOrder).toString());
                                        trackdetails.setLabcode(jsonObject.optString(Constants.labcode).toString());
                                        trackdetails.setLeadId(jsonObject.optString(Constants.leadId).toString());
                                        trackdetails.setName(jsonObject.optString(Constants.name).toString());
                                        trackdetails.setPatient_id(jsonObject.optString(Constants.patient_id).toString());
                                        trackdetails.setPdflink(jsonObject.optString(Constants.pdflink).toString());
                                        trackdetails.setSample_type(jsonObject.optString(Constants.sample_type).toString());
                                        trackdetails.setScp(jsonObject.optString(Constants.scp).toString());
                                        trackdetails.setSct(jsonObject.optString(Constants.sct).toString());
                                        trackdetails.setSu_code2(jsonObject.optString(Constants.su_code2).toString());
                                        trackdetails.setWo_sl_no(jsonObject.optString(Constants.wo_sl_no).toString());

                                        FilterReport.add(trackdetails);
                                    }
                                    adapter = new ResultDtlAdapter(BarcodeWiseReport_Download.this, FilterReport);
                                    ListReportStatus.setAdapter(adapter);
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                    ListReportStatus.setVisibility(View.VISIBLE);
                                    searchbarcode.setVisibility(View.VISIBLE);
                                } else {
                                    ListReportStatus.setVisibility(View.GONE);
                                    searchbarcode.setVisibility(View.GONE);
                                    nodata.setVisibility(View.VISIBLE);
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                }
                            } else {

                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                TastyToast.makeText(mContext, responsetoshow, TastyToast.LENGTH_SHORT, TastyToast.ERROR);


                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        mContext).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Update Ledger !");

                                // Setting Dialog Message
                                alertDialog.setMessage(ToastFile.update_ledger);
                                // Setting OK Button
                                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(mContext, Payment_Activity.class);
                                        i.putExtra("COMEFROM", "BarcodeWiseReport_Download");
                                        mContext.startActivity(i);
                                       /* Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                        httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                        startActivity(httpIntent);*/
                                        // Write your code here to execute after dialog closed
                                    }
                                });
                                alertDialog.show();
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
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObject);

    }

}
