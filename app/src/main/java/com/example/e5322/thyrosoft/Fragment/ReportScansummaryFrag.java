package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.MISAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Displayrodeails_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetScanResponse;
import com.example.e5322.thyrosoft.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReportScansummaryFrag extends Fragment implements View.OnClickListener {

    RecyclerView recy_report;
    RelativeLayout rel_dt;
    TextView to_date, txt_nodata;
    Calendar myCalendar;
    private String showDate, To_formateDate, usercode;
    String TAG = getClass().getSimpleName();
    Date fromdate;
    Activity activity;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE, Locale.US);
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_scansumm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        initView(view);

    }

    private void updateLabel() {
        String putDate = sdf.format(myCalendar.getTime());
        GlobalClass.SetText(to_date, putDate);

        if (!GlobalClass.isNull(to_date.getText().toString())) {
            GetMIS();
        }
    }

    private void initView(View view) {

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        usercode = prefs.getString("USER_CODE", "");

        recy_report = view.findViewById(R.id.recy_mis);
        rel_dt = view.findViewById(R.id.rel_dt);
        to_date = view.findViewById(R.id.to_date);
        txt_nodata = view.findViewById(R.id.txt_nodata);

        to_date.setOnClickListener(this);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);
        GlobalClass.SetText(to_date, showDate);
        fromdate = returndate(fromdate, showDate);
        To_formateDate = GlobalClass.formatDate("dd-MM-yyyy", "yyyy-MM-dd", showDate);


        if (!GlobalClass.isNull(to_date.getText().toString())) {
            GetMIS();
        }
    }

    private void GetMIS() {
        try {
            if (ControllersGlobalInitialiser.displayrodeails_controller != null) {
                ControllersGlobalInitialiser.displayrodeails_controller = null;
            }
            ControllersGlobalInitialiser.displayrodeails_controller = new Displayrodeails_Controller(getActivity(), ReportScansummaryFrag.this);
            ControllersGlobalInitialiser.displayrodeails_controller.getdisplayrodetails(usercode, to_date);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Date returndate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.to_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

        }
    }

    public void getscansummResponse(Response<GetScanResponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getRed_id()) && response.body().getRed_id().equalsIgnoreCase(Constants.RES0000)) {
                if (!response.body().getRODETAILS().isEmpty()) {
                    recy_report.setVisibility(View.VISIBLE);
                    recy_report.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recy_report.setAdapter(new MISAdapter(getContext(), response.body().getRODETAILS()));
                    txt_nodata.setVisibility(View.GONE);
                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    recy_report.setVisibility(View.GONE);
                    recy_report.setVisibility(View.GONE);
                }
            } else {
                recy_report.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
