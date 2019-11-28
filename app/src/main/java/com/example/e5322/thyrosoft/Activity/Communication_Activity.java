package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Communication_Activity extends AppCompatActivity {

    FloatingActionButton addCommunication;
    TextView FromCPL, ToCPL;
    ImageView back, home;
    public static com.android.volley.RequestQueue PostQueOtp;
    ImageView enter_arrow_enter, enter_arrow_entered;
    LinearLayout unchecked_entered_ll, enter_ll_unselected;
    public static RequestQueue PostQue;
    ExpandableListCommunication adapter;
    ExpandableListCommunicationSents adapterSents;
    ExpandableListView expandlistcommunication;
    ProgressDialog barProgressDialog;
    CommunicationRepsponseModel communicationRepsponseModel;
    private Global globalClass;
    LinearLayout offline_img;
    private String TAG = getClass().getSimpleName();
    private SharedPreferences prefs;
    private String user, api_key;
    private String communicationMasterResponse, inboxesResponse, resId, sentsResponse, responseID;
    String comefrom;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        user = prefs.getString("Username", null);
        api_key = prefs.getString("API_KEY", null);
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
       /* FromCPL.setBackgroundColor(getResources().getColor(R.color.orange));
        FromCPL.setTextColor(getResources().getColor(R.color.colorWhite));
        ToCPL.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        ToCPL.setTextColor(getResources().getColor(R.color.colorBlack));*/

        FromCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        if (globalClass.checkForApi21()) {
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

        barProgressDialog = new ProgressDialog(Communication_Activity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        PostQue = Volley.newRequestQueue(Communication_Activity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences prefs = Communication_Activity.this.getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            jsonObject.put("apiKey", api_key);
            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put("type", "");
            jsonObject.put("communication", "");
            jsonObject.put("commId", "");
            jsonObject.put("forwardTo", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(Communication_Activity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.commGetLive, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: RESPONSE" + response);
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }

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
        Log.e(TAG, "Getdata: URL" + jsonObjectRequest);
        Log.e(TAG, "Getdata: json" + jsonObject);
    }

    private void setAdapter() {

        if (communicationRepsponseModel.getInboxes() != null && communicationRepsponseModel.getInboxes().length > 0) {
            expandlistcommunication.setVisibility(View.VISIBLE);
            adapter = new ExpandableListCommunication(Communication_Activity.this, communicationRepsponseModel.getInboxes(), new Interface_Pass_CommunicationValue() {
                @Override
                public void passCommIdAndMSg(Activity activity, String commiId, String message) {

                 /*   barProgressDialog = new ProgressDialog(activity, R.style.ProgressBarColor);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    barProgressDialog.show();*/

                    PostQueOtp = Volley.newRequestQueue(activity);
                    JSONObject jsonObjectOtp = new JSONObject();
                    try {
                        jsonObjectOtp.put("apiKey", api_key);
                        jsonObjectOtp.put("userCode", user);
                        jsonObjectOtp.put("type", "response");
                        jsonObjectOtp.put("communication", message);
                        jsonObjectOtp.put("commId", commiId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.postResponseToCommunication, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                          /*  if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }*/
                            try {
                                Log.e(TAG, "onResponse: " + response);
                                String finalJson = response.toString();
                                JSONObject parentObjectOtp = new JSONObject(finalJson);

                                communicationMasterResponse = parentObjectOtp.getString("communicationMaster");
                                inboxesResponse = parentObjectOtp.getString("inboxes");
                                resId = parentObjectOtp.getString("resId");
                                responseID = parentObjectOtp.getString("response");
                                sentsResponse = parentObjectOtp.getString("sents");

                                if (responseID.equalsIgnoreCase("Communication acknowledged Successfully")) {
                                    TastyToast.makeText(Communication_Activity.this, responseID, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    Getdata();
                                } else {
                                    TastyToast.makeText(Communication_Activity.this, responseID, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }

                            } catch (JSONException e) {
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
                    Log.e(TAG, "SendFeedbackToAPI: json" + jsonObjectOtp);
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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.comm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/

}
