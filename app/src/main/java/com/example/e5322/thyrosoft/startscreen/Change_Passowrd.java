package com.example.e5322.thyrosoft.startscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.ForgotPassController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

/**
 * Created by E5322 on 21-05-2018.
 */

public class Change_Passowrd extends Activity {
    EditText new_pass;
    EditText changed_pass;
    Button btn_new_pass;
    String EXISTS, MOBILE, NAME, PASSWORD, RESPONSE, RES_ID, SMS_SENT_ON, STATUS, USER_TYPE;
    SharedPreferences prefs;
    String numberOTP;
    String TAG = Change_Passowrd.class.getSimpleName().toString();
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
    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mActivity = Change_Passowrd.this;

        initViews();
        initListneer();

    }

    private void initListneer() {
        new_pass.setFilters(new InputFilter[]{EMOJI_FILTER});
        changed_pass.setFilters(new InputFilter[]{EMOJI_FILTER});
        btn_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpass = new_pass.getText().toString();
                String conpass = changed_pass.getText().toString();

                if (GlobalClass.isNull(newpass)) {
                    GlobalClass.showTastyToast(Change_Passowrd.this, ToastFile.passwd, 2);
                } else if (!newpass.equalsIgnoreCase(conpass)) {
                    changed_pass.setError(MessageConstants.PASSWORD_NOT_MATCHES);

                } else {
                    PostQueOtp = Volley.newRequestQueue(Change_Passowrd.this);
                    JSONObject jsonObjectOtp = new JSONObject();
                    try {
                        jsonObjectOtp.put("api_key", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
                        jsonObjectOtp.put("type", "FORGOT_PWD");

                        jsonObjectOtp.put("new_pass", conpass);
                        jsonObjectOtp.put("mobile", GlobalClass.saveMobileNUmber);

                        /*Global.Username = username.getText().toString();*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (ControllersGlobalInitialiser.forgotPassController != null) {
                            ControllersGlobalInitialiser.forgotPassController = null;
                        }
                        ControllersGlobalInitialiser.forgotPassController = new ForgotPassController(mActivity, Change_Passowrd.this);
                        ControllersGlobalInitialiser.forgotPassController.change_passowrd(jsonObjectOtp, PostQueOtp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void initViews() {
        new_pass = (EditText) findViewById(R.id.new_pass);
        changed_pass = (EditText) findViewById(R.id.changed_pass);
        btn_new_pass = (Button) findViewById(R.id.btn_new_pass);
        prefs = getSharedPreferences("OTPnumber", MODE_PRIVATE);
        numberOTP = prefs.getString("PhoneNUmber", "");

    }

    public void getResponse(JSONObject response) {
        try {
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            EXISTS = parentObjectOtp.getString("EXISTS");
            MOBILE = parentObjectOtp.getString("MOBILE");
            NAME = parentObjectOtp.getString("NAME");
            PASSWORD = parentObjectOtp.getString("PASSWORD");
            RESPONSE = parentObjectOtp.getString("RESPONSE");
            RES_ID = parentObjectOtp.getString("RES_ID");
            SMS_SENT_ON = parentObjectOtp.getString("SMS_SENT_ON");
            STATUS = parentObjectOtp.getString("STATUS");
            USER_TYPE = parentObjectOtp.getString("USER_TYPE");

            GlobalClass.showTastyToast(mActivity, RESPONSE, 1);
            Intent a = new Intent(mActivity, Login.class);
            startActivity(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
