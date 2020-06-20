package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InsertReasonsReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailRes;
import com.example.e5322.thyrosoft.Models.InsertreasonResponse;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
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
        activity=getActivity();
        scanIntegrator = IntentIntegrator.forSupportFragment(this);
        initView(view);

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

        txt_barcode = view.findViewById(R.id.txt_barcode);

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
                    Toast.makeText(getActivity(), "Please enter valid Waste count", Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        edt_wastecnt.setText(enteredString.substring(1));
                    } else {
                        edt_wastecnt.setText("");
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

        edt_reason.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxreason = 500;
        edt_reason.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxreason)});

        edt_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    Toast.makeText(getActivity(), "Please enter valid reason", Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        edt_reason.setText(enteredString.substring(1));
                    } else {
                        edt_reason.setText("");
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
                if (GlobalClass.isNetworkAvailable(getActivity())) {
                    if (TextUtils.isEmpty(txt_barcode.getText().toString())) {
                        GlobalClass.toastyError(getActivity(), "Please Scan barcode", false);
                    } else {
                        insertscandetailData();
                    }

                } else {
                    GlobalClass.toastyError(getActivity(), MessageConstants.CHECK_INTERNET_CONN, false);
                }
            }
        });

        btn_reason_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.isNetworkAvailable(getActivity())) {
                    if (TextUtils.isEmpty(edt_wastecnt.getText().toString())) {
                        GlobalClass.toastyError(getActivity(), "Please enter Waste count", false);
                    } else if (TextUtils.isEmpty(edt_reason.getText().toString())) {
                        GlobalClass.toastyError(getActivity(), "Please enter reason", false);
                    } else {
                        insertreason();
                    }
                } else {
                    GlobalClass.toastyError(getActivity(), MessageConstants.CHECK_INTERNET_CONN, false);
                }
            }
        });


    }


    private void insertreason() {

        Log.e(TAG, "Reason lenght ------>" + edt_reason.getText().toString().length());
        Log.e(TAG, "  ------>" + edt_reason.getText().toString().length());
        btn_reason_submit.setClickable(false);
        btn_reason_submit.setEnabled(false);
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
        InsertReasonsReq insertScandetailReq = new InsertReasonsReq();
        insertScandetailReq.setSource(usercode);
        insertScandetailReq.setRemarks(edt_reason.getText().toString());
        insertScandetailReq.setRowaste(edt_wastecnt.getText().toString());

        APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.insertscandetail).create(APIInteface.class);
        Call<InsertreasonResponse> insertScandetailResCall = apiInteface.insertreason(insertScandetailReq);

       // Log.e(TAG, "URL ---->" + insertScandetailResCall.request().url());
       // Log.e(TAG, "REQUEST BODY ---->" + new GsonBuilder().create().toJson(insertScandetailReq));

        insertScandetailResCall.enqueue(new Callback<InsertreasonResponse>() {
            @Override
            public void onResponse(Call<InsertreasonResponse> call, Response<InsertreasonResponse> response) {

                try {
                    if (response.body().getRed_id().equalsIgnoreCase(Constants.RES0000)) {
                        btn_reason_submit.setClickable(true);
                        btn_reason_submit.setEnabled(true);
                        GlobalClass.hideProgress(getContext(), progressDialog);
                       // Log.e(TAG, "DATA SUBMITEED SUCCESSS ");
                        GlobalClass.toastySuccess(getContext(), "Data submitted Successfully", false);
                        txt_barcode.setText(null);
                        clearField();
                    } else {
                        btn_reason_submit.setClickable(true);
                        btn_reason_submit.setEnabled(true);
                        GlobalClass.toastyError(getContext(), response.body().getResponse(), false);
                        GlobalClass.hideProgress(getContext(), progressDialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<InsertreasonResponse> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
            }
        });
    }

    private void insertscandetailData() {
        btn_scansubmit.setClickable(false);
        btn_scansubmit.setEnabled(false);
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
        InsertScandetailReq insertScandetailReq = new InsertScandetailReq();
        insertScandetailReq.setSource(usercode);
        insertScandetailReq.setBarcode(txt_barcode.getText().toString());

        APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.insertscandetail).create(APIInteface.class);
        Call<InsertScandetailRes> insertScandetailResCall = apiInteface.insertScandetail(insertScandetailReq);

       // Log.e(TAG, "URL ---->" + insertScandetailResCall.request().url());
        //Log.e(TAG, "REQUEST BODY ---->" + new GsonBuilder().create().toJson(insertScandetailReq));

        insertScandetailResCall.enqueue(new Callback<InsertScandetailRes>() {
            @Override
            public void onResponse(Call<InsertScandetailRes> call, Response<InsertScandetailRes> response) {

                try {
                    if (response.body().getRed_id().equalsIgnoreCase(Constants.RES0000)) {
                        GlobalClass.hideProgress(getContext(), progressDialog);
                        btn_scansubmit.setClickable(true);
                        btn_scansubmit.setEnabled(true);
                        txt_barcode.setText("");
                        GlobalClass.toastySuccess(getContext(), "Barcode submitted successfully", false);
                        clearField();
                    } else {
                        btn_scansubmit.setClickable(true);
                        btn_scansubmit.setEnabled(true);
                        GlobalClass.hideProgress(getContext(), progressDialog);
                        GlobalClass.toastyError(getContext(), response.body().getResponse(), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<InsertScandetailRes> call, Throwable t) {
                GlobalClass.hideProgress(getContext(), progressDialog);
            }
        });

    }

    private void clearField() {
        txt_barcode.setText("");
        edt_reason.getText().clear();
        edt_wastecnt.getText().clear();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* super.onActivityResult(requestCode, resultCode, data);*/
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                Log.e(TAG, "getBarcodeDetails---->" + getBarcodeDetails.length());
                if (getBarcodeDetails.length() != 0 || !TextUtils.isEmpty(getBarcodeDetails)) {
                    txt_barcode.setText(getBarcodeDetails);
                } else {
                    Toast.makeText(getContext(), invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
