package com.example.e5322.thyrosoft.startscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by E5322 on 21-05-2018.
 */

public class Change_Passowrd extends Activity {
    EditText new_pass;
    EditText changed_pass;
    Button btn_new_pass ;String  EXISTS,MOBILE,NAME, PASSWORD, RESPONSE, RES_ID, SMS_SENT_ON, STATUS, USER_TYPE;
    SharedPreferences prefs;
    String numberOTP;
    String TAG=Change_Passowrd.class.getSimpleName().toString();
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
    public static com.android.volley.RequestQueue PostQueOtp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        new_pass = (EditText) findViewById(R.id.new_pass);
        changed_pass = (EditText) findViewById(R.id.changed_pass);
        btn_new_pass = (Button) findViewById(R.id.btn_new_pass);

        new_pass.setFilters(new InputFilter[] { EMOJI_FILTER });
        changed_pass.setFilters(new InputFilter[] { EMOJI_FILTER });

        prefs = getSharedPreferences("OTPnumber", MODE_PRIVATE);
        numberOTP = prefs.getString("PhoneNUmber", null);
        btn_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass = new_pass.getText().toString();
                String conpass = changed_pass.getText().toString();

                if (newpass.equals("") || conpass.equals("")) {
                    Toast.makeText(Change_Passowrd.this, ToastFile.passwd, Toast.LENGTH_SHORT).show();
                } else if (!newpass.equals(conpass)) {
                    changed_pass.setError("Password not matching");

                } else {
                    PostQueOtp = Volley.newRequestQueue(Change_Passowrd.this);
                    JSONObject jsonObjectOtp = new JSONObject();
                    try {
                        jsonObjectOtp.put("api_key", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
                        jsonObjectOtp.put("type", "FORGOT_PWD");

                        jsonObjectOtp.put("new_pass",conpass);
                        jsonObjectOtp.put("mobile", GlobalClass.saveMobileNUmber);

                        /*Global.Username = username.getText().toString();*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queueOtp = Volley.newRequestQueue(Change_Passowrd.this);
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.ForgotPass, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(TAG, "onResponse: "+response );
                            try {
                                String finalJson = response.toString();
                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                EXISTS       = parentObjectOtp.getString("EXISTS");
                                MOBILE        = parentObjectOtp.getString("MOBILE");
                                NAME           = parentObjectOtp.getString("NAME");
                                PASSWORD       = parentObjectOtp.getString("PASSWORD");
                                RESPONSE       = parentObjectOtp.getString("RESPONSE");
                                RES_ID         = parentObjectOtp.getString("RES_ID");
                                SMS_SENT_ON   = parentObjectOtp.getString("SMS_SENT_ON");
                                STATUS         = parentObjectOtp.getString("STATUS");
                                USER_TYPE      =      parentObjectOtp.getString("USER_TYPE");

                                TastyToast.makeText(getApplicationContext(),RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                Intent a = new Intent(Change_Passowrd.this, Login.class);
                                startActivity(a);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /* Toast.makeText(Login.this, "" + response, Toast.LENGTH_SHORT).show();*/
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                                // Toast.makeText(Evening.this, item + " Booking not done successfully", Toast.LENGTH_SHORT).show();
                            } else {

                                System.out.println(error);
                            }
                        }
                    });
                    queueOtp.add(jsonObjectRequest1);
                    Log.e(TAG, "onClick: url"+jsonObjectRequest1 );
                    Log.e(TAG, "onClick: json"+jsonObjectOtp );
                }
            }
        });
    }
}
