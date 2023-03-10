package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ExpandableListAdapter_FAQ;
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQ_Model;
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQandANSArray;
import com.example.e5322.thyrosoft.Fragment.FAQ_Fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.FAQTypesResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Faq_activity extends AppCompatActivity {
    public static ArrayList<String> type_spinner_value;
    ImageView add;
    ImageView back, home;
    View viewfab;
    FAQ_Model faq_model;
    ExpandableListAdapter_FAQ expandableListAdapter;
    View viewMain;
    View view;
    ExpandableListView expandable_list_faq;
    Spinner category_spinner;
    String user, passwrd, access, api_key, client_type;
    ProgressDialog barProgressDialog;
    private RequestQueue requestQueueSpinner_value, requestQueue_FAQ;
    private SharedPreferences prefs;
    private FAQ_Fragment.OnFragmentInteractionListener mListener;
    private String TAG = getClass().getSimpleName();
    private Global globalClass;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_);

        expandable_list_faq = (ExpandableListView) findViewById(R.id.faq_list_expandable);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Faq_activity.this);
            }
        });

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        client_type = prefs.getString("CLIENT_TYPE", "");

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Faq_activity.this);

        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, "");
        java.lang.reflect.Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> arrayList = gson.fromJson(json, type);
        type_spinner_value = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expandable_list_faq.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));


        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                type_spinner_value.add(arrayList.get(i).toString());
            }
            ArrayAdapter type_value_spinner = new ArrayAdapter(Faq_activity.this, R.layout.spinnerproperty, type_spinner_value);
            category_spinner.setAdapter(type_value_spinner);
            getAll_FAQ_data();
        } else {
            getSpinnerdata();
        }

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getAll_FAQ_data();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void getSpinnerdata() {

        barProgressDialog = new ProgressDialog(Faq_activity.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        requestQueueSpinner_value = GlobalClass.setVolleyReq(Faq_activity.this);

        Log.e(TAG, "getSpinnerdata URL : " + Api.static_pages_link + client_type + "/Get_Faq_Type_Spinner");

        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + client_type + "/Get_Faq_Type_Spinner", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response" + response);
                try {
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }

                    FAQTypesResponseModel responseModel = new Gson().fromJson(String.valueOf(response), FAQTypesResponseModel.class);

                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {

                            if (responseModel.getFAQ_type_spinnner_names() != null && responseModel.getFAQ_type_spinnner_names().size() > 0) {
                                type_spinner_value = new ArrayList<>();
                                for (int i = 0; i < responseModel.getFAQ_type_spinnner_names().size(); i++) {
                                    type_spinner_value.add(responseModel.getFAQ_type_spinnner_names().get(i));
                                }
                                ArrayAdapter type_value_spinner = new ArrayAdapter(Faq_activity.this, R.layout.spinnerproperty, type_spinner_value);
                                category_spinner.setAdapter(type_value_spinner);

                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Faq_activity.this);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(type_spinner_value);
                                editor.putString(TAG, json);
                                editor.apply();

                                getAll_FAQ_data();
                            } else {
                                Toast.makeText(Faq_activity.this, responseModel.getResponse(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Faq_activity.this, responseModel.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Faq_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(Faq_activity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequestProfile);
        requestQueueSpinner_value.add(jsonObjectRequestProfile);
        Log.e(TAG, "getSpinnerdata: URL" + jsonObjectRequestProfile);
    }

    private void getAll_FAQ_data() {

        String getSpinner_value = category_spinner.getSelectedItem().toString();
        requestQueue_FAQ = GlobalClass.setVolleyReq(Faq_activity.this);
        JsonObjectRequest jsonObjectRequestFAQ = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + client_type + "/" + getSpinner_value + "/GetAllFAQ", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response" + response);
                Gson gson = new Gson();
                faq_model = gson.fromJson(response.toString(), FAQ_Model.class);

                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(Faq_activity.this);
                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                Gson gson22 = new Gson();
                String json22 = gson22.toJson(faq_model);
                prefsEditor1.putString("FAQ_DATA", json22);
                prefsEditor1.apply();

                if (faq_model != null) {
                    if (faq_model.getResponse().equals("Success")) {
                        if (faq_model.getFAQandANSArray() != null) {
                            ArrayList<FAQandANSArray> faq_list = new ArrayList<>();
                            for (int i = 0; i < faq_model.getFAQandANSArray().length; i++) {
                                faq_list.add(faq_model.getFAQandANSArray()[i]);
                            }
                            expandableListAdapter = new ExpandableListAdapter_FAQ(faq_list, Faq_activity.this);
                            expandable_list_faq.setAdapter(expandableListAdapter);
                        }
                    }
                } else {
                    Toast.makeText(Faq_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(Faq_activity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequestFAQ);
        requestQueue_FAQ.add(jsonObjectRequestFAQ);
        Log.e(TAG, "getAll_FAQ_data: URL" + jsonObjectRequestFAQ);
    }
}