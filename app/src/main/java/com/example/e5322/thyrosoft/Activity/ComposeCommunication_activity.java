package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Cliso_BMC.BMC_MainActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class ComposeCommunication_activity extends AppCompatActivity {
    Spinner spinnercomm;
    EditText commuTXT;
    ImageView back, home;
    LinearLayout parent_ll, offline_img;
    public static RequestQueue PostQue;
    private SimpleDateFormat sdf;
    String Date = "";
    private Global globalClass;
    ProgressDialog barProgressDialog;
    Button sendcomm;
    String comefrom;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compose_communication);

        if (getIntent().getExtras() != null) {
            comefrom = getIntent().getExtras().getString("comefrom");
        }

        spinnercomm = (Spinner) findViewById(R.id.spinnercomm);
        sendcomm = (Button) findViewById(R.id.sendcomm);
        commuTXT = (EditText) findViewById(R.id.commuTXT);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) findViewById(R.id.parent_ll);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comefrom.equals("BMC"))
                    startActivity(new Intent(ComposeCommunication_activity.this, BMC_MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                else
                    GlobalClass.goToHome(ComposeCommunication_activity.this);
            }
        });

        if (!GlobalClass.isNetworkAvailable(ComposeCommunication_activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }

        String[] string = new String[GlobalClass.commSpinner.size()];
        for (int i = 0; i < GlobalClass.commSpinner.size(); i++) {
            string[i] = GlobalClass.commSpinner.get(i).getDisplayName();
        }

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        commuTXT.setFilters(new InputFilter[]{EMOJI_FILTER});
        commuTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    TastyToast.makeText(ComposeCommunication_activity.this, ToastFile.crt_txt, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        commuTXT.setText(enteredString.substring(1));
                    } else {
                        commuTXT.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date = sdf.format(cl.getTime());
        ArrayAdapter aa = new ArrayAdapter(ComposeCommunication_activity.this, android.R.layout.simple_spinner_item, string);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercomm.setAdapter(aa);
        sendcomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getCommunication = commuTXT.getText().toString();
                if (getCommunication.equals("")) {
                    TastyToast.makeText(ComposeCommunication_activity.this, "Compose your message", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {

                    if (!GlobalClass.isNetworkAvailable(ComposeCommunication_activity.this)) {
                        offline_img.setVisibility(View.VISIBLE);
                        parent_ll.setVisibility(View.GONE);
                    } else {
                        PostData();
                        offline_img.setVisibility(View.GONE);
                        parent_ll.setVisibility(View.VISIBLE);
                    }
                }
                commuTXT.setText("");
                spinnercomm.setSelection(0);
            }
        });
    }

    private void PostData() {
        barProgressDialog = new ProgressDialog(ComposeCommunication_activity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        String spinnerItem;

        PostQue = Volley.newRequestQueue(ComposeCommunication_activity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            // spinnerItem = spinnercomm.getSelectedItem().toString();
            if (spinnercomm.getSelectedItem().toString() != null)
                spinnerItem = spinnercomm.getSelectedItem().toString();

            if (spinnercomm.getSelectedItem().equals("WOE & REPORTS")) {
                spinnerItem = "WOE-REPORTS";
            } else {
                spinnerItem = spinnercomm.getSelectedItem().toString();
            }

            SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            jsonObject.put("apiKey", api_key);
            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put("type", "Request");
            jsonObject.put("communication", commuTXT.getText());
            jsonObject.put("commId", "");
            jsonObject.put("forwardTo", spinnerItem);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(ComposeCommunication_activity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.commPost, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            String responsetoshow = response.optString("response", "");

                            if (responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(ComposeCommunication_activity.this);
                            } else if (responsetoshow.equalsIgnoreCase("Communication Registered Successfully")) {
                                TastyToast.makeText(ComposeCommunication_activity.this, response.optString(Constants.response).toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                TastyToast.makeText(ComposeCommunication_activity.this, response.optString(Constants.response).toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        Log.e(TAG, "PostData: URL" + jsonObjectRequest);
        Log.e(TAG, "PostData: json" + jsonObject);

    }

}
