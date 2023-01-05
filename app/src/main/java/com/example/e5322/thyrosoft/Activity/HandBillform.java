package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.CommonItils.Validator;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.EmailValidationController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.EmailModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandBillform extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_mobile, edt_name, edt_email, edt_address, edt_pincode;
    private TextView toolbar_title;
    private ImageView back_img;
    Button btn_preview;
    String img_url;
    String TAG = getClass().getSimpleName();
    private SharedPreferences pref_user;
    HandBillform mActivity;
    static GlobalClass globalClass;
    ImageView back, home;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalClass = new GlobalClass(HandBillform.this);
        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.maroon));
        }
        setContentView(R.layout.lay_handbillform);
        initilazation();
        initUI();
        initListeners();
    }

    private void initilazation() {
        mActivity = HandBillform.this;
        if (getIntent().getExtras() != null) {
            img_url = getIntent().getExtras().getString(Constants.IMAG_URL);
            Log.e(TAG, "onCreate image ----> " + img_url);
        }

        pref_user = mActivity.getSharedPreferences("Userdetails", 0);
    }

    private void initUI() {
        String usermob = pref_user.getString("mobile_user", "");
        String name = pref_user.getString("NAME", "");
        String email = pref_user.getString("email", "");

        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        btn_preview = findViewById(R.id.btn_preview);
        btn_preview.setOnClickListener(this);


        TextView txtname = findViewById(R.id.txt_name);
        GlobalClass.SetText(txtname, mActivity.getString(R.string.handbill));

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(this);
        home.setOnClickListener(this);


        if (!GlobalClass.isNull(usermob)) {
            GlobalClass.EditSetText(edt_mobile,usermob.trim());
        }

        if (!GlobalClass.isNull(name)) {
            GlobalClass.EditSetText(edt_name,name.trim());
        }

        if (!GlobalClass.isNull(email)) {
            GlobalClass.EditSetText(edt_email,email.trim().toLowerCase());
        }
    }

    private void initListeners() {
        edt_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    GlobalClass.EditSetText(edt_mobile,enteredString.substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        GlobalClass.EditSetText(edt_name,"");
                    } else {
                        GlobalClass.EditSetText(edt_name,enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("9")) {
                    Toast.makeText(HandBillform.this, "Please enter valid pincode",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(edt_pincode,enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(edt_pincode,"");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private boolean validation() {

        if (edt_name.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_NAME, 2);
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_NAME, 2);
            edt_name.requestFocus();
            return false;
        }

        if (edt_mobile.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_MOBILE, 2);
            edt_mobile.requestFocus();
            return false;
        }
        if (edt_mobile.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(HandBillform.this, MessageConstants.MOBILE_10_DIGITS, 2);
            edt_mobile.requestFocus();
            return false;
        }

        if (edt_email.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_EMAIL, 2);
            return false;
        } else {
            if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                GlobalClass.showTastyToast(HandBillform.this, MessageConstants.VALID_EMAIL, 2);
                edt_email.requestFocus();
                return false;
            }
        }

        if (!edt_address.getText().toString().isEmpty()) {
            if (edt_address.getText().toString().length() == 0) {
                GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_ADDRESS, 2);
                edt_address.requestFocus();
                return false;
            }
            if (edt_address.getText().toString().length() < 25) {
                GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ADDRESS_MIN_25, 2);
                edt_address.requestFocus();
                return false;
            }
        }

        if (!edt_pincode.getText().toString().isEmpty()) {
            if (edt_pincode.getText().toString().length() == 0) {
                GlobalClass.showTastyToast(HandBillform.this, MessageConstants.ENTER_PINCODE, 2);
                edt_pincode.requestFocus();
                return false;
            }
            if (edt_pincode.getText().toString().length() < 6) {
                GlobalClass.showTastyToast(HandBillform.this, MessageConstants.PINCODE_6_DIGITS, 2);
                edt_pincode.requestFocus();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.home:
                GlobalClass.redirectToHome(mActivity);
                break;

            case R.id.btn_preview:
                if (validation()) {
                    if (edt_email.getText().length() > 0) {
                        try {
                            emailvalidationapi(edt_email.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        gotoNext();
                    }
                }
                break;


        }
    }

    private void gotoNext() {
        Intent intent = new Intent(HandBillform.this, Previewhandbill_Activity.class);
        intent.putExtra(Constants.IMAG_URL, img_url);
        intent.putExtra(Constants.B_NAME, edt_name.getText().toString());
        intent.putExtra(Constants.B_MOB, edt_mobile.getText().toString());
        intent.putExtra(Constants.B_EMAIL, edt_email.getText().toString());
        intent.putExtra(Constants.B_ADDR, edt_address.getText().toString());
        intent.putExtra(Constants.B_PINCODE, edt_pincode.getText().toString());
        intent.putExtra(Constants.FROMCOME, "form");
        startActivity(intent);
        finish();
    }

    public void emailvalidationapi(String email) {
        try {
            if (ControllersGlobalInitialiser.emailValidationController != null) {
                ControllersGlobalInitialiser.emailValidationController = null;
            }
            ControllersGlobalInitialiser.emailValidationController = new EmailValidationController(HandBillform.this, mActivity);
            ControllersGlobalInitialiser.emailValidationController.emailvalidationapi(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getemailresponse() {
        gotoNext();
    }
}
