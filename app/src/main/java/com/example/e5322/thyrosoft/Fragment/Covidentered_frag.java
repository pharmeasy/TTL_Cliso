package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.CovidMISAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.CovidFilter_Controller;
import com.example.e5322.thyrosoft.Controller.CovidMIS_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVfiltermodel;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.R;

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
        initListner();

    }

    private void initListner() {

        mode_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = mode_filter.getSelectedItem().toString();
                if (type.equalsIgnoreCase("All")) {
                    getMIS(from_formateDate);
                } else {
                    filterbytype(type);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        mainlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.Hidekeyboard(view);
            }
        });

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
                if (enteredString.equals("")) {
                    mode_filter.setSelection(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        GlobalClass.SetText(tv_fromDate, putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);
        mode_filter.setSelection(0);

        getMIS(from_formateDate);

    }

    private void initView(View view) {
        edit_search = view.findViewById(R.id.edit_search);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        ll_fromDate = view.findViewById(R.id.ll_fromDate);
        txt_nodata = view.findViewById(R.id.txt_nodata);

        mainlinear = view.findViewById(R.id.mainlinear);
        mode_filter = view.findViewById(R.id.mode_filter);

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);

        GlobalClass.SetText(tv_fromDate, showDate);

        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);


        getfilterapilist();

        recy_mis = view.findViewById(R.id.recy_covidmis);

    }

    private void getfilterapilist() {

        try {
            if (ControllersGlobalInitialiser.covidFilter_controller != null) {
                ControllersGlobalInitialiser.covidFilter_controller = null;
            }
            ControllersGlobalInitialiser.covidFilter_controller = new CovidFilter_Controller(activity, Covidentered_frag.this);
            ControllersGlobalInitialiser.covidFilter_controller.getcovidfilter_controller();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMIS(String from_formateDate) {

        try {
            if (ControllersGlobalInitialiser.covidMIS_controller != null) {
                ControllersGlobalInitialiser.covidMIS_controller = null;
            }
            ControllersGlobalInitialiser.covidMIS_controller = new CovidMIS_Controller(activity, Covidentered_frag.this);
            ControllersGlobalInitialiser.covidMIS_controller.getcovidMIScontroller(from_formateDate);
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

    private void filter(String text) {
        try {
            ArrayList<Covidmis_response.OutputBean> filterlist = new ArrayList<>();
            for (Covidmis_response.OutputBean var : covidMISmodelList) {
                if (var.getPatientName().toLowerCase().contains(text.toLowerCase()) || var.getStatusName().toLowerCase().contains(text.toLowerCase()) || var.getMobile().toLowerCase().contains(text.toLowerCase())
                        || var.getCcc().toLowerCase().contains(text.toLowerCase())) {
                    filterlist.add(var);
                }
            }

            if (GlobalClass.CheckArrayList(filterlist)) {
                recy_mis.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
            } else {
                recy_mis.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                GlobalClass.SetText(txt_nodata, MessageConstants.NODATA);
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

            if (GlobalClass.CheckArrayList(filterlist)) {
                recy_mis.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
            } else {
                recy_mis.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                GlobalClass.SetText(txt_nodata, MessageConstants.NODATA);
            }
            covidMISAdapter.filterList(filterlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getcovidMISResponse(Response<Covidmis_response> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) &&
                    response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                recy_mis.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
                covidMISmodelList = new ArrayList<>();
                covidMISmodelList.addAll(response.body().getOutput());
                recy_mis.setLayoutManager(new LinearLayoutManager(getContext()));
                covidMISAdapter = new CovidMISAdapter(getContext(), covidMISmodelList, activity);
                recy_mis.setAdapter(covidMISAdapter);
            } else {
                covidMISmodelList = null;
                recy_mis.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                GlobalClass.SetText(txt_nodata, MessageConstants.NODATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getcovidfilterResponse(Response<COVfiltermodel> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId())
                    && response.body().getResId().equalsIgnoreCase(Constants.RES0000)
                    &&  GlobalClass.CheckArrayList(response.body().getStatus())) {

                filterlist = new ArrayList<>();
                filterlist.clear();

                if (GlobalClass.CheckArrayList(response.body().getStatus())) {
                    for (int i = 0; i < response.body().getStatus().size(); i++) {
                        filterlist.add(response.body().getStatus().get(i).getStatus());
                    }
                }

                if (GlobalClass.CheckArrayList(filterlist)) {
                    ArrayAdapter<String> filteradapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterlist);
                    filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mode_filter.setAdapter(filteradapter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
