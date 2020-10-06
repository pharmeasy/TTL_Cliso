package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetOTPCreditMISController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.OTPCreditMISRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.OTPCreditResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OTPCreditMISActivity extends AppCompatActivity {

    private ImageView back, home;
    private Activity mActivity;
    private ConnectionDetector cd;
    private String user, passwrd, access, api_key;
    private LinearLayout ll_fromDate, ll_toDate;
    private TextView tv_fromDate, tv_toDate, tv_noResult;
    private Calendar to_cal, from_cal;
    private String putDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private Date fromDate, toDate;
    private String From_formatDate, To_formatDate;
    private TableLayout tl_misData;
    private int mYear, mMonth, mDay;

    private DatePickerDialog.OnDateSetListener from_date_listnr = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            from_cal.set(Calendar.YEAR, year);
            from_cal.set(Calendar.MONTH, monthOfYear);
            from_cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            putDate = sdf.format(from_cal.getTime());
            fromDate = returnDate(fromDate, putDate);
            From_formatDate = GlobalClass.formatDate("dd-MM-yyyy", "yyyy-MM-dd", putDate);


            Date currDate = new Date();
            if (fromDate.after(currDate)) {
                GlobalClass.showShortToast(mActivity, "Kindly choose correct Date");
            } else {
                tv_fromDate.setText(putDate);
                if (!GlobalClass.isNull(To_formatDate)) {
                    if (toDate.before(fromDate)) {
                        GlobalClass.showShortToast(mActivity, "Kindly choose correct Date");
                        tv_fromDate.setText("");
                        From_formatDate = "";
                        tl_misData.removeAllViews();
                    } else {
                        callFetchOTPCreditMISAPI();
                    }
                } else {
                    GlobalClass.showShortToast(mActivity, "Kindly select to date");
                }
            }
        }
    };
    private DatePickerDialog.OnDateSetListener to_date_listnr = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            to_cal.set(Calendar.YEAR, year);
            to_cal.set(Calendar.MONTH, monthOfYear);
            to_cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            putDate = sdf.format(to_cal.getTime());
            toDate = returnDate(toDate, putDate);
            To_formatDate = GlobalClass.formatDate("dd-MM-yyyy", "yyyy-MM-dd", putDate);

            Date currDate = new Date();
            if (toDate.after(currDate)) {
                GlobalClass.showShortToast(mActivity, "Kindly choose correct Date");
            } else {
                tv_toDate.setText(putDate);
                if (!GlobalClass.isNull(From_formatDate)) {
                    if (toDate.before(fromDate)) {
                        GlobalClass.showShortToast(mActivity, "Kindly choose correct Date");
                        tv_toDate.setText("");
                        To_formatDate = "";
                        tl_misData.removeAllViews();
                    } else {
                        callFetchOTPCreditMISAPI();
                    }
                } else {
                    GlobalClass.showShortToast(mActivity, "Kindly select from date");
                }
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcredit_mis);

        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        mActivity = OTPCreditMISActivity.this;
        cd = new ConnectionDetector(mActivity);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        from_cal = Calendar.getInstance();
        to_cal = Calendar.getInstance();

        initUI();
        initListeners();

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        putDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
        From_formatDate = GlobalClass.formatDate("dd-MM-yyyy", "yyyy-MM-dd", putDate);
        To_formatDate = GlobalClass.formatDate("dd-MM-yyyy", "yyyy-MM-dd", putDate);

        tv_fromDate.setText(putDate);
        tv_toDate.setText(putDate);

        fromDate = returnDate(fromDate, putDate);
        toDate = returnDate(toDate, putDate);

        callFetchOTPCreditMISAPI();
    }

    private void initUI() {
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        ll_fromDate = findViewById(R.id.ll_fromDate);
        ll_toDate = findViewById(R.id.ll_toDate);
        tv_fromDate = findViewById(R.id.tv_fromDate);
        tv_toDate = findViewById(R.id.tv_toDate);
        tv_noResult = findViewById(R.id.tv_noResult);
        tl_misData = findViewById(R.id.tl_misData);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(OTPCreditMISActivity.this);
            }
        });

        ll_fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, from_date_listnr, from_cal
                        .get(Calendar.YEAR), from_cal.get(Calendar.MONTH),
                        from_cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        ll_toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, to_date_listnr, to_cal
                        .get(Calendar.YEAR), to_cal.get(Calendar.MONTH),
                        to_cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


    }

    private Date returnDate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void callFetchOTPCreditMISAPI() {
        OTPCreditMISRequestModel requestModel = new OTPCreditMISRequestModel();
        requestModel.setUserid(user);
        requestModel.setFromdate(From_formatDate);
        requestModel.setTodate(To_formatDate);

        if (cd.isConnectingToInternet()) {
            try {
                if (ControllersGlobalInitialiser.getOTPCreditMISController != null) {
                    ControllersGlobalInitialiser.getOTPCreditMISController = null;
                }
                ControllersGlobalInitialiser.getOTPCreditMISController = new GetOTPCreditMISController(OTPCreditMISActivity.this, mActivity);
                ControllersGlobalInitialiser.getOTPCreditMISController.getOTPCreditMISResponse(requestModel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlobalClass.showShortToast(mActivity, ToastFile.intConnection);
        }
    }

    public void getOTPCreditMISResponse(OTPCreditResponseModel responseModel) {
        if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000)) {
            if (responseModel.getOutput() != null && responseModel.getOutput().size() > 0) {
                displayData(responseModel.getOutput());
            } else {
                tv_noResult.setVisibility(View.VISIBLE);
                tl_misData.removeAllViews();
            }
        } else {
            GlobalClass.showShortToast(mActivity, responseModel.getResponse());
            tv_noResult.setVisibility(View.VISIBLE);
            tl_misData.removeAllViews();
        }
    }

    private void displayData(ArrayList<OTPCreditResponseModel.OutputBean> arrayList) {
        tl_misData.removeAllViews();
        if (arrayList.size() > 0) {
            tv_noResult.setVisibility(View.GONE);
            int sr_no;
            for (int i = 0; i < arrayList.size(); i++) {
                TableRow tableRow = (TableRow) LayoutInflater.from(mActivity).inflate(R.layout.otp_credit_mis_item_view, null);

                TextView tv_srno = tableRow.findViewById(R.id.tv_srno);
                TextView tv_mob = tableRow.findViewById(R.id.tv_mob);
                TextView tv_reqTime = tableRow.findViewById(R.id.tv_reqTime);
                TextView tv_recvTime = tableRow.findViewById(R.id.tv_recvTime);
                TextView tv_verified = tableRow.findViewById(R.id.tv_verified);
                TextView tv_credit = tableRow.findViewById(R.id.tv_credit);

                String reqTime = GlobalClass.formatDate("dd/MM/yyyy hh:mm:ss aa", "hh:mm:ss aa", arrayList.get(i).getCREATETIME());
                String recvTime = GlobalClass.formatDate("dd/MM/yyyy hh:mm:ss aa", "hh:mm:ss aa", arrayList.get(i).getSENTTIME());

                sr_no = i + 1;
                GlobalClass.SetText(tv_srno, "" + sr_no);
                GlobalClass.SetText(tv_mob, arrayList.get(i).getMOBILE());
                GlobalClass.SetText(tv_reqTime, reqTime);
                GlobalClass.SetText(tv_recvTime, recvTime);
                GlobalClass.SetText(tv_verified, arrayList.get(i).getVERIFIED());
                GlobalClass.SetText(tv_credit, arrayList.get(i).getCREDIT());

                tl_misData.addView(tableRow);
            }
        } else {
            tv_noResult.setVisibility(View.VISIBLE);
        }
    }
}
