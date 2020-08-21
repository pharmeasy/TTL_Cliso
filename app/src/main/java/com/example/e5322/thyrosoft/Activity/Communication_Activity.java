package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ExpandableListCommunication;
import com.example.e5322.thyrosoft.Adapter.ExpandableListCommunicationSents;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.CommGetLive_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.PostCommunicationController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.Interface_Pass_CommunicationValue;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationRepsponseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.GetCommunicationsRequestModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Communication_Activity extends AppCompatActivity {

    public static com.android.volley.RequestQueue PostQueOtp;
    public static RequestQueue PostQue;
    private FloatingActionButton addCommunication;
    private TextView FromCPL, ToCPL;
    private ImageView back, home;
    ImageView enter_arrow_enter, enter_arrow_entered;
    LinearLayout unchecked_entered_ll, enter_ll_unselected;
    ExpandableListCommunication adapter;
    ExpandableListCommunicationSents adapterSents;
    ExpandableListView expandlistcommunication;
    CommunicationRepsponseModel communicationRepsponseModel;
    LinearLayout offline_img;
    private String TAG = getClass().getSimpleName();
    private SharedPreferences prefs;
    private String user, api_key, CLIENT_TYPE;
    Activity mActivity;
    ConnectionDetector cd;
    ArrayList<CommunicationMaster> commSpinner;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_communication);
        mActivity = Communication_Activity.this;
        cd = new ConnectionDetector(mActivity);

        initViews();
        initListner();
    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(mActivity);
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromCPL.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                ToCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                if (!GlobalClass.isNetworkAvailable(mActivity)) {
                    offline_img.setVisibility(View.VISIBLE);
                    expandlistcommunication.setVisibility(View.GONE);
                } else {
                    try {

                        if (communicationRepsponseModel != null && GlobalClass.checkArray(communicationRepsponseModel.getSents())) {
                            adapterSents = new ExpandableListCommunicationSents(Communication_Activity.this, communicationRepsponseModel.getSents());
                            expandlistcommunication.setAdapter(adapterSents);
                            offline_img.setVisibility(View.GONE);
                            expandlistcommunication.setVisibility(View.VISIBLE);
                        } else {
                            expandlistcommunication.setVisibility(View.GONE);
                            adapterSents.notifyDataSetChanged();
                            GlobalClass.showTastyToast(mActivity, MessageConstants.NODATA, 2);
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

                if (!GlobalClass.isNetworkAvailable(mActivity)) {
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
                Intent i = new Intent(mActivity, ComposeCommunication_activity.class);
                i.putParcelableArrayListExtra("commSpinner", commSpinner);
                startActivity(i);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
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

        FromCPL.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!GlobalClass.isNetworkAvailable(mActivity)) {
            offline_img.setVisibility(View.VISIBLE);
            expandlistcommunication.setVisibility(View.GONE);
        } else {

            offline_img.setVisibility(View.GONE);
            expandlistcommunication.setVisibility(View.VISIBLE);
        }
        Getdata();
    }

    private void Getdata() {
        JSONObject jsonObject = null;
        try {
            SharedPreferences prefs = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            GetCommunicationsRequestModel requestModel = new GetCommunicationsRequestModel();
            requestModel.setApiKey(api_key);
            requestModel.setUserCode(user);
            if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(Constants.NHF)&&
                    CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
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


        RequestQueue queue = Volley.newRequestQueue(mActivity);

        try {
            if (ControllersGlobalInitialiser.commGetLive_controller != null) {
                ControllersGlobalInitialiser.commGetLive_controller = null;
            }
            ControllersGlobalInitialiser.commGetLive_controller = new CommGetLive_Controller(mActivity, Communication_Activity.this);
            ControllersGlobalInitialiser.commGetLive_controller.commgetliveController(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {

        if (communicationRepsponseModel != null && GlobalClass.checkArray(communicationRepsponseModel.getInboxes())) {
            expandlistcommunication.setVisibility(View.VISIBLE);
            adapter = new ExpandableListCommunication(Communication_Activity.this, communicationRepsponseModel.getInboxes(), new Interface_Pass_CommunicationValue() {
                @Override
                public void passCommIdAndMSg(final Activity activity, String commiId, String message) {
                    PostQueOtp = Volley.newRequestQueue(activity);

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


                    try {
                        if (ControllersGlobalInitialiser.postCommunicationController != null) {
                            ControllersGlobalInitialiser.postCommunicationController = null;
                        }
                        ControllersGlobalInitialiser.postCommunicationController = new PostCommunicationController(mActivity, Communication_Activity.this);
                        ControllersGlobalInitialiser.postCommunicationController.postcommunucationController(jsonObject, PostQueOtp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            expandlistcommunication.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            expandlistcommunication.setVisibility(View.GONE);
            GlobalClass.showTastyToast(mActivity, MessageConstants.NODATA, 2);
        }
    }

    public void getpostcommResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            CommunicationRepsponseModel responseModel = new Gson().fromJson(String.valueOf(response), CommunicationRepsponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(MessageConstants.COMM_AKCK_SUCC)) {
                    GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 1);
                    Getdata();
                } else {
                    GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.FEEDBACKMSG, 2);
            e.printStackTrace();
        }
    }

    public void getcommliveResponse(JSONObject response) {
        try {

            Log.e(TAG, "onResponse: RESPONSE" + response);

            String getResponse = response.optString("response", "");

            if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                GlobalClass.redirectToLogin(mActivity);
            } else {
                Gson gson = new Gson();
                communicationRepsponseModel = new CommunicationRepsponseModel();
                communicationRepsponseModel = gson.fromJson(response.toString(), CommunicationRepsponseModel.class);
                commSpinner = new ArrayList<>();

                if (communicationRepsponseModel != null &&  GlobalClass.checkArray(communicationRepsponseModel.getCommunicationMaster())) {
                    for (int i = 0; i < communicationRepsponseModel.getCommunicationMaster().length; i++) {
                        commSpinner.add(communicationRepsponseModel.getCommunicationMaster()[i]);
                    }
                }

                setAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}