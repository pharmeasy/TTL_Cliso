package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.CovidMISAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVfiltermodel;
import com.example.e5322.thyrosoft.Models.CovidMIS_req;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Covidentered_frag extends Fragment {
    Calendar myCalendar;
    private String myFormat = "dd-MM-yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    Activity activity;
    String TAG = getClass().getSimpleName();
    ConnectionDetector cd;
    CovidMISAdapter covidMISAdapter;
    LinearLayout mainlinear;
    RecyclerView recy_mis;
    List<Covidmis_response.OutputBean> covidMISmodelList;
    private String putDate, showDate, from_formateDate;
    TextView tv_fromDate, txt_nodata;
    LinearLayout ll_fromDate;
    Date fromdate;
    private EditText edit_search;
    List<String> filterlist;
    List<COVfiltermodel> coVfiltermodelList;
    Spinner mode_filter;
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

        View viewMain = (View) inflater.inflate(R.layout.covid_entered, container, false);
        activity = getActivity();
        cd = new ConnectionDetector(activity);
        return viewMain;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        tv_fromDate.setText(putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);
        mode_filter.setSelection(0);
        if (GlobalClass.isNetworkAvailable((Activity) getContext())) {
            if (cd.isConnectingToInternet()) {
                getMIS(from_formateDate);
            } else {
                GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
            }
        } else {
            GlobalClass.toastySuccess(getContext(), ToastFile.intConnection, false);
        }

    }

    private void initView(View view) {
        edit_search = view.findViewById(R.id.edit_search);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        ll_fromDate = view.findViewById(R.id.ll_fromDate);
        txt_nodata = view.findViewById(R.id.txt_nodata);

        mainlinear=view.findViewById(R.id.mainlinear);
        mainlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.Hidekeyboard(view);
            }
        });

        mode_filter = view.findViewById(R.id.mode_filter);

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);
        tv_fromDate.setText(showDate);
        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String enteredString = charSequence.toString();
                if (enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("?")) {
                }
                if (enteredString.equals("")){
                    mode_filter.setSelection(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });

        if (cd.isConnectingToInternet()) {
            getfilterapilist();
        } else {
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }



        recy_mis = view.findViewById(R.id.recy_covidmis);

        ll_fromDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });
    }

    private void getfilterapilist() {
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        Call<COVfiltermodel> coVfiltermodelCall = postAPIInteface.getfilter();
        coVfiltermodelCall.enqueue(new Callback<COVfiltermodel>() {
            @Override
            public void onResponse(Call<COVfiltermodel> call, Response<COVfiltermodel> response) {
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000) && response.body().getStatus() != null) {


                        filterlist = new ArrayList<>();
                        filterlist.clear();
                        for (int i = 0; i < response.body().getStatus().size(); i++) {
                            filterlist.add(response.body().getStatus().get(i).getStatus());
                        }

                        ArrayAdapter<String> filteradapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterlist);
                        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mode_filter.setAdapter(filteradapter);

                        mode_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String type = mode_filter.getSelectedItem().toString();
                                if (type.equalsIgnoreCase("All")){
                                    if (GlobalClass.isNetworkAvailable((Activity) getContext())) {
                                        if (cd.isConnectingToInternet()) {
                                            getMIS(from_formateDate);
                                        } else {
                                            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
                                        }
                                    } else {
                                        GlobalClass.toastySuccess(getContext(), ToastFile.intConnection, false);
                                    }
                                }else {
                                    filterbytype(type);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<COVfiltermodel> call, Throwable t) {

            }
        });
    }

    private void getMIS(String from_formateDate) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        String usercode = preferences.getString("USER_CODE", null);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        CovidMIS_req covidMIS_req = new CovidMIS_req();
        covidMIS_req.setSdate(from_formateDate);
        covidMIS_req.setSourceCode(usercode);
        final Call<Covidmis_response> covidmis_responseCall = postAPIInteface.getcovidmis(covidMIS_req);
        covidmis_responseCall.enqueue(new Callback<Covidmis_response>() {
            @Override
            public void onResponse(Call<Covidmis_response> call, Response<Covidmis_response> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        recy_mis.setVisibility(View.VISIBLE);
                        txt_nodata.setVisibility(View.GONE);
                        covidMISmodelList = new ArrayList<>();
                        covidMISmodelList.addAll(response.body().getOutput());
                        recy_mis.setLayoutManager(new LinearLayoutManager(getContext()));
                        covidMISAdapter = new CovidMISAdapter(getContext(), covidMISmodelList, activity);
                        recy_mis.setAdapter(covidMISAdapter);
                    } else {
                        recy_mis.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                        txt_nodata.setText(MessageConstants.NODATA);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Covidmis_response> call, Throwable t) {

            }
        });
    }

    private Date returndate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private void filter(String text) {
        try {
            ArrayList<Covidmis_response.OutputBean> filterlist = new ArrayList<>();
            for (Covidmis_response.OutputBean var : covidMISmodelList) {
                if (var.getPatientName().toLowerCase().contains(text.toLowerCase()) ||var.getStatusName().toLowerCase().contains(text.toLowerCase()) || var.getMobile().toLowerCase().contains(text.toLowerCase())
                        || var.getCcc().toLowerCase().contains(text.toLowerCase())) {
                    filterlist.add(var);
                }
            }

            if (filterlist!=null &&filterlist.size()>0){
                recy_mis.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
            }else {
                recy_mis.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText(MessageConstants.NODATA);
            }
            covidMISAdapter.filterList(filterlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterbytype(String text) {
        try {
            ArrayList<Covidmis_response.OutputBean> filterlist = new ArrayList<>();
            for (Covidmis_response.OutputBean var : covidMISmodelList) {
                if (var.getStatusName().contains(text)) {
                    filterlist.add(var);
                }
            }
            if (filterlist!=null &&filterlist.size()>0){
                recy_mis.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
            }else {
                recy_mis.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText(MessageConstants.NODATA);
            }
            covidMISAdapter.filterList(filterlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
