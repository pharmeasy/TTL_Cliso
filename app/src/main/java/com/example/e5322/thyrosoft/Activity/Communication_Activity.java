package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ExpandableListCommunication;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.Interface_Pass_CommunicationValue;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationRepsponseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.GetCommunicationsRequestModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Communication_Activity extends AppCompatActivity {

    public static com.android.volley.RequestQueue PostQueOtp;
    public static RequestQueue PostQue;
    FloatingActionButton addCommunication;
    TextView FromCPL, ToCPL;
    ImageView back, home;
    ImageView enter_arrow_enter, enter_arrow_entered;
    LinearLayout unchecked_entered_ll, enter_ll_unselected;
    ExpandableListCommunication adapter;
    ExpandableListCommunicationSents adapterSents;
    ExpandableListView expandlistcommunication;
    ProgressDialog barProgressDialog;
    CommunicationRepsponseModel communicationRepsponseModel;
    LinearLayout offline_img;
    String comefrom;
    private Global globalClass;
    private String TAG = getClass().getSimpleName();
    private SharedPreferences prefs;
    private String user, api_key, CLIENT_TYPE;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.fragment_communication);

        /*if (getIntent().getExtras() != null) {
            comefrom = getIntent().getExtras().getString("comefrom");
        }*/

        FromCPL = (TextView) findViewById(R.id.FromCPL);
        ToCPL = (TextView) findViewById(R.id.ToCPL);
        enter_arrow_enter = (ImageView) findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) findViewById(R.id.enter_arrow_entered);
        unchecked_entered_ll = (LinearLayout) findViewById(R.id.unchecked_entered_ll);
        enter_ll_unselected = (LinearLayout) findViewById(R.id.enter_ll_unselected);
        addCommunication = (FloatingActionButton) findViewById(R.id.addCommunication);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        api_key = prefs.getString("API_KEY", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");
        expandlistcommunication = (ExpandableListView) findViewById(R.id.expandlistcommunication);
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
                /*if (comefrom.equals("BMC"))
                    startActivity(new Intent(Communication_Activity.this, BMC_MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                else*/
                GlobalClass.goToHome(Communication_Activity.this);
            }
        });

        Log.e(TAG, "");


        FromCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromCPL.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                ToCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                if (!GlobalClass.isNetworkAvailable(Communication_Activity.this)) {
                    offline_img.setVisibility(View.VISIBLE);
                    expandlistcommunication.setVisibility(View.GONE);
                } else {
                    try {

                        if (communicationRepsponseModel.getSents() != null && communicationRepsponseModel.getSents().length > 0) {
                            adapterSents = new ExpandableListCommunicationSents(Communication_Activity.this, communicationRepsponseModel.getSents());
                            expandlistcommunication.setAdapter(adapterSents);
                            offline_img.setVisibility(View.GONE);
                            expandlistcommunication.setVisibility(View.VISIBLE);
                        } else {
                            expandlistcommunication.setVisibility(View.GONE);
                            adapterSents.notifyDataSetChanged();
                            Toast.makeText(Communication_Activity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                ToCPL.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);

                if (!GlobalClass.isNetworkAvailable(Communication_Activity.this)) {
                    offline_img.setVisibility(View.VISIBLE);
                    expandlistcommunication.setVisibility(View.GONE);
                } else {
                    offline_img.setVisibility(View.GONE);
                    setAdapter();
                }

            }
        });

        addCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Communication_Activity.this, ComposeCommunication_activity.class);
//                i.putExtra("comefrom", comefrom);
                startActivity(i);
            }
        });


        /*if (!GlobalClass.isNetworkAvailable(Communication_Activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            expandlistcommunication.setVisibility(View.GONE);
        } else {
            Getdata();
            offline_img.setVisibility(View.GONE);
            expandlistcommunication.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!GlobalClass.isNetworkAvailable(Communication_Activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            expandlistcommunication.setVisibility(View.GONE);
        } else {
            Getdata();
            offline_img.setVisibility(View.GONE);
            expandlistcommunication.setVisibility(View.VISIBLE);
        }
    }

    private void Getdata() {

    /*    barProgressDialog = new ProgressDialog(Communication_Activity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        PostQue = GlobalClass.setVolleyReq(Communication_Activity.this);
*/

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(Communication_Activity.this);

        JSONObject jsonObject = null;
        try {
            SharedPreferences prefs = Communication_Activity.this.getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", "");
            String passwrd = prefs.getString("password", "");
            String access = prefs.getString("ACCESS_TYPE", "");
            String api_key = prefs.getString("API_KEY", "");

            GetCommunicationsRequestModel requestModel = new GetCommunicationsRequestModel();
            requestModel.setApiKey(api_key);
            requestModel.setUserCode(user);
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                requestModel.setType(Constants.NHF);
            } else {
                requestModel.setType("Cliso");
            }
            requestModel.setCommunication("");
            requestModel.setCommId("");
            requestModel.setForwardTo("");

            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG,"COMMAPI-->"+Api.commGetLive);
        Log.e(TAG,"COMMAPI BODY-->"+jsonObject);
        RequestQueue queue = GlobalClass.setVolleyReq(Communication_Activity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.commGetLive, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.e(TAG, "onResponse: RESPONSE" + response);
                    GlobalClass.hideProgress(Communication_Activity.this, progressDialog);

                    String getResponse = response.optString("response", "");

                    if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                        GlobalClass.redirectToLogin(Communication_Activity.this);
                    } else {
                        Gson gson = new Gson();
                        communicationRepsponseModel = new CommunicationRepsponseModel();
                        communicationRepsponseModel = gson.fromJson(response.toString(), CommunicationRepsponseModel.class);
                        GlobalClass.commSpinner = new ArrayList<CommunicationMaster>();

                        for (int i = 0; i < communicationRepsponseModel.getCommunicationMaster().length; i++) {
                            GlobalClass.commSpinner.add(communicationRepsponseModel.getCommunicationMaster()[i]);
                        }

                        setAdapter();
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
        queue.add(jsonObjectRequest);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        Log.e(TAG, "Getdata: URL" + jsonObjectRequest);
        Log.e(TAG, "Getdata: json" + jsonObject);
    }

    private void setAdapter() {

        if (communicationRepsponseModel.getInboxes() != null && communicationRepsponseModel.getInboxes().length > 0) {
            expandlistcommunication.setVisibility(View.VISIBLE);
            adapter = new ExpandableListCommunication(Communication_Activity.this, communicationRepsponseModel.getInboxes(), new Interface_Pass_CommunicationValue() {
                @Override
                public void passCommIdAndMSg(final Activity activity, String commiId, String message) {
                    PostQueOtp = GlobalClass.setVolleyReq(activity);
                    JSONObject jsonObject = null;
                    try {
                        GetCommunicationsRequestModel requestModel = new GetCommunicationsRequestModel();
                        requestModel.setApiKey(api_key);
                        requestModel.setUserCode(user);
                        requestModel.setType("response");
                        requestModel.setCommunication(message);
                        requestModel.setCommId(commiId);

                        String json = new Gson().toJson(requestModel);
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.postResponseToCommunication, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(TAG, "onResponse: " + response);
                                CommunicationRepsponseModel responseModel = new Gson().fromJson(String.valueOf(response), CommunicationRepsponseModel.class);
                                if (responseModel != null) {
                                    if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Communication acknowledged Successfully")) {
                                        TastyToast.makeText(Communication_Activity.this, responseModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        Getdata();
                                    } else {
                                        TastyToast.makeText(Communication_Activity.this, responseModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                } else {
                                    Toast.makeText(activity, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                TastyToast.makeText(Communication_Activity.this, "Feedback not sent successfully", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

                    PostQueOtp.add(jsonObjectRequest1);
                    GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
                    Log.e(TAG, "SendFeedbackToAPI: json" + jsonObject);
                    Log.e(TAG, "SendFeedbackToAPI: url" + jsonObjectRequest1);
                }
            });

            expandlistcommunication.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            expandlistcommunication.setVisibility(View.GONE);
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
}