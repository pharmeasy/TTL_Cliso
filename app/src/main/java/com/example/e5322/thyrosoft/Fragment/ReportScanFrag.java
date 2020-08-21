package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.InsertScandetail_Controller;
import com.example.e5322.thyrosoft.Controller.Insertreason_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InsertScandetailRes;
import com.example.e5322.thyrosoft.Models.InsertreasonResponse;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class ReportScanFrag extends Fragment {

    ImageView element1_iv;
    EditText edt_wastecnt, edt_reason;
    TextView txt_barcode;
    public IntentIntegrator scanIntegrator;
    String TAG = getClass().getSimpleName(), usercode;
    Button btn_scansubmit, btn_reason_submit;
    LinearLayout lin_reason;
    Activity activity;


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_scanbarcode, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        scanIntegrator = IntentIntegrator.forSupportFragment(this);
        initView(view);

        initListner();

    }


    private void initView(View view) {

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        usercode = prefs.getString("USER_CODE", "");

        edt_wastecnt = view.findViewById(R.id.edt_wastecnt);
        edt_reason = view.findViewById(R.id.edt_reason);

        element1_iv = view.findViewById(R.id.element1_iv);
        btn_scansubmit = view.findViewById(R.id.btn_scansubmit);
        btn_reason_submit = view.findViewById(R.id.btn_reason_submit);
        lin_reason = view.findViewById(R.id.lin_reason);


        txt_barcode = view.findViewById(R.id.txt_barcode);


        edt_reason.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxreason = 500;
        edt_reason.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxreason)});
    }

    private void initListner() {

        edt_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    GlobalClass.showTastyToast(activity, MessageConstants.ENTER_REASON, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_reason, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_reason, "");
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


        btn_scansubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.isNull(txt_barcode.getText().toString())) {
                    GlobalClass.showTastyToast(activity, ToastFile.scan_brcd, 2);
                } else {
                    insertscandetailData();
                }
            }
        });

        btn_reason_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalClass.isNull(edt_wastecnt.getText().toString())) {
                    GlobalClass.showTastyToast(activity, MessageConstants.ENTER_WSTCNT, 2);
                } else if (GlobalClass.isNull(edt_reason.getText().toString())) {
                    GlobalClass.showTastyToast(activity, MessageConstants.ENTER_REASON, 2);
                } else {
                    insertreason();
                }
            }
        });


        edt_wastecnt.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxLength = 4;

        edt_wastecnt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        edt_wastecnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {

                    GlobalClass.showTastyToast(activity, MessageConstants.ENTER_WSTCNT, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_wastecnt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_wastecnt, "");
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

        lin_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_reason.requestFocus();
                edt_reason.setFocusable(true);
                edt_reason.setCursorVisible(true);

                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        lin_reason.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);

            }
        });

        element1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanIntegrator.initiateScan();
            }
        });
    }

    private void insertreason() {

        Log.e(TAG, "Reason lenght ------>" + edt_reason.getText().toString().length());
        Log.e(TAG, "  ------>" + edt_reason.getText().toString().length());
        btn_reason_submit.setClickable(false);
        btn_reason_submit.setEnabled(false);

        try {
            if (ControllersGlobalInitialiser.insertreason_controller != null) {
                ControllersGlobalInitialiser.insertreason_controller = null;
            }
            ControllersGlobalInitialiser.insertreason_controller = new Insertreason_Controller(getActivity(), ReportScanFrag.this);
            ControllersGlobalInitialiser.insertreason_controller.getinsertreasoncontroller(usercode, edt_reason, edt_wastecnt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void insertscandetailData() {
        btn_scansubmit.setClickable(false);
        btn_scansubmit.setEnabled(false);

        try {
            if (ControllersGlobalInitialiser.insertScandetail_controller != null) {
                ControllersGlobalInitialiser.insertScandetail_controller = null;
            }
            ControllersGlobalInitialiser.insertScandetail_controller = new InsertScandetail_Controller(getActivity(), ReportScanFrag.this);
            ControllersGlobalInitialiser.insertScandetail_controller.insertScandetail(usercode, txt_barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearField() {
        GlobalClass.SetText(txt_barcode, "");
        edt_reason.getText().clear();
        edt_wastecnt.getText().clear();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                Log.e(TAG, "getBarcodeDetails---->" + getBarcodeDetails.length());
                if (!GlobalClass.isNull(getBarcodeDetails) || getBarcodeDetails.length() != 0 ) {
                    GlobalClass.SetText(txt_barcode, getBarcodeDetails);
                } else {
                    GlobalClass.showTastyToast(activity, invalid_brcd, 2);

                }
            }
        }
    }

    public void getscanResponse(Response<InsertScandetailRes> response) {

        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getRed_id()) && response.body().getRed_id().equalsIgnoreCase(Constants.RES0000)) {
                btn_scansubmit.setClickable(true);
                btn_scansubmit.setEnabled(true);
                GlobalClass.SetText(txt_barcode, "");
                GlobalClass.showTastyToast(activity, MessageConstants.BARCODESUCC, 1);
                clearField();
            } else {
                btn_scansubmit.setClickable(true);
                btn_scansubmit.setEnabled(true);

                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getinsertreasonResponse(Response<InsertreasonResponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getRed_id()) && response.body().getRed_id().equalsIgnoreCase(Constants.RES0000)) {
                btn_reason_submit.setClickable(true);
                btn_reason_submit.setEnabled(true);
                GlobalClass.showTastyToast(activity, MessageConstants.DATASUCCUSS, 1);
                GlobalClass.SetText(txt_barcode, null);
                clearField();
            } else {
                btn_reason_submit.setClickable(true);
                btn_reason_submit.setEnabled(true);
                GlobalClass.showTastyToast(activity, response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
