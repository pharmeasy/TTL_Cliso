package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.ScansummAdapter;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GenerateTokenController;
import com.example.e5322.thyrosoft.Controller.PETCTScansummary_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.Models.TokenModel;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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

import static android.content.Context.MODE_PRIVATE;

public class ScanSummaryActivity extends Fragment {
    Calendar myCalendar;
    private String myFormat = "dd-MM-yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private TextView from_date, to_date, txt_nodata, txt_frm_dialog, txt_to_dialog;
    private String putDate;
    private String showDate;
    private Date fromdate, To_date;
    private Activity mActivity;
    private String getFormatDate;
    private String from_formateDate, To_formateDate;
    RecyclerView recy_mis;
    ConnectionDetector cd;
    LinearLayout lin_border;
    String header;
    SharedPreferences preferences;
    private EditText edit_search;
    ArrayList<ScansummaryModel> scansummaryModelArrayList;
    ScansummAdapter scansummAdapter;
    List<ScansummaryModel> modelList;
    public static String CURR_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String OUTPUT_FORMAT = "dd-MM-yyyy HH:mm";

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


    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            putDate = sdf.format(myCalendar.getTime());
            getFormatDate = sdf.format(myCalendar.getTime());
            To_date = returndate(To_date, putDate);
            To_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);

            if (To_date.before(fromdate)) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Kindly_choose_correct_date, 2);
            } else {

                GlobalClass.SetText(to_date, putDate);

                if (cd.isConnectingToInternet()) {
                    setData();
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                }

            }
        }

    };


    private String user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_scansummary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mActivity = getActivity();
        preferences = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = preferences.getString("Username", null);
        cd = new ConnectionDetector(getContext());

        initView(view);
        intListner();

        if (cd.isConnectingToInternet()) {
            generateToken();
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

    private void intListner() {
        txt_frm_dialog.setOnClickListener(new View.OnClickListener() {
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

        txt_to_dialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String enteredString = charSequence.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("?")) {
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
        getFormatDate = sdf.format(myCalendar.getTime());

        fromdate = returndate(fromdate, putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);

        if (To_date.before(fromdate)) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.Kindly_choose_correct_date, 2);
        } else {
            GlobalClass.SetText(from_date, putDate);
            if (cd.isConnectingToInternet()) {
                setData();
            } else {
                GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        }
    }


    private void initView(View view) {


        from_date = view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);

        lin_border = view.findViewById(R.id.lin_border);


        txt_frm_dialog = view.findViewById(R.id.txt_frm_dialog);
        txt_to_dialog = view.findViewById(R.id.txt_to_dialog);
        txt_nodata = view.findViewById(R.id.txt_nodata);
        recy_mis = view.findViewById(R.id.recy_summary);
        edit_search = view.findViewById(R.id.edit_search);

        /*TODO BYDEFAULT CURRENT DATE SELELCTION*/
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);

        GlobalClass.SetText(from_date, showDate);
        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today1 = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today1);
        showDate = sdf1.format(today1);

        GlobalClass.SetText(to_date, showDate);

        To_date = returndate(To_date, showDate);
        To_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

    }

    private void generateToken() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", Constants.NHF_EMAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ControllersGlobalInitialiser.generateTokenController != null) {
            ControllersGlobalInitialiser.generateTokenController = null;
        }
        ControllersGlobalInitialiser.generateTokenController = new GenerateTokenController((Activity) getActivity(), ScanSummaryActivity.this);
        ControllersGlobalInitialiser.generateTokenController.generateToken(true, jsonObject);


    }

    public void getGenerateTokenResponse(JSONObject response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson;
        gson = gsonBuilder.create();

        try {
            TokenModel tokenResponseModel = gson.fromJson(response.toString(), TokenModel.class);
            if (tokenResponseModel != null) {
                if (!GlobalClass.isNull(tokenResponseModel.getRespId()) && tokenResponseModel.getRespId().equalsIgnoreCase(Constants.RES0000)) {
                    if (!GlobalClass.isNull(tokenResponseModel.getAccess_token()) && !GlobalClass.isNull(tokenResponseModel.getToken_type())) {
                        if (cd.isConnectingToInternet()) {
                            header = tokenResponseModel.getToken_type() + " " + tokenResponseModel.getAccess_token();
                            if (cd.isConnectingToInternet()) {
                                setData();
                            } else {
                                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filter(String text) {
        try {
            ArrayList<ScansummaryModel> filterlist = new ArrayList<>();
            for (ScansummaryModel var : modelList) {
                if (var.getLEAD_ID().toLowerCase().contains(text.toLowerCase()) || var.getBOOKEDAT().toLowerCase().contains(text.toLowerCase())
                        || var.getMOBILE().toLowerCase().contains(text.toLowerCase()) || var.getNAME().toLowerCase().contains(text.toLowerCase())) {
                    filterlist.add(var);
                }
            }
            scansummAdapter.filterList(filterlist);

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

    private void setData() {

        String str_frmdt = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, from_date.getText().toString());
        String str_todt = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, to_date.getText().toString());

        try {
            if (ControllersGlobalInitialiser.petctScansummaryController != null) {
                ControllersGlobalInitialiser.petctScansummaryController = null;
            }
            ControllersGlobalInitialiser.petctScansummaryController = new PETCTScansummary_Controller(mActivity, ScanSummaryActivity.this, header);
            ControllersGlobalInitialiser.petctScansummaryController.getpetctSummarycontroller(header, str_frmdt, str_todt, user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getScansummaryResponse(List<ScansummaryModel> body) {
        if (body != null) {
            recy_mis.setVisibility(View.VISIBLE);
            lin_border.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);

            if (!GlobalClass.CheckArrayList(scansummaryModelArrayList)) {
                scansummaryModelArrayList = new ArrayList<>();
            }

            scansummaryModelArrayList.clear();
            scansummaryModelArrayList.addAll(body);
            modelList = new ArrayList<>();

            if (GlobalClass.CheckArrayList(scansummaryModelArrayList)) {
                for (int i = 0; i < scansummaryModelArrayList.size(); i++) {

                    ScansummaryModel scansummaryModel = new ScansummaryModel();
                    scansummaryModel.setBOOKEDAT(GlobalClass.formatDate(CURR_FORMAT, OUTPUT_FORMAT, scansummaryModelArrayList.get(i).getBOOKEDAT()));
                    scansummaryModel.setADDRESS(scansummaryModelArrayList.get(i).getADDRESS());
                    scansummaryModel.setAPPOINTMENT_DATE(GlobalClass.formatDate(CURR_FORMAT, OUTPUT_FORMAT, scansummaryModelArrayList.get(i).getAPPOINTMENT_DATE()));
                    scansummaryModel.setMOBILE(scansummaryModelArrayList.get(i).getMOBILE());
                    scansummaryModel.setCENTERID(scansummaryModelArrayList.get(i).getCENTERID());
                    scansummaryModel.setLEAD_ID(scansummaryModelArrayList.get(i).getLEAD_ID());
                    scansummaryModel.setSTATUS(scansummaryModelArrayList.get(i).getSTATUS());
                    scansummaryModel.setBOOKED_BY(scansummaryModelArrayList.get(i).getBOOKED_BY());
                    scansummaryModel.setNAME(scansummaryModelArrayList.get(i).getNAME());
                    scansummaryModel.setCurrDate(scansummaryModelArrayList.get(i).getCurrDate());
                    scansummaryModel.setREMARKS(scansummaryModelArrayList.get(i).getREMARKS());
                    scansummaryModel.setBilling_Rate(scansummaryModelArrayList.get(i).getBilling_Rate());
                    modelList.add(scansummaryModel);

                }
            }

            if (GlobalClass.CheckArrayList(modelList)) {
                recy_mis.setLayoutManager(new LinearLayoutManager(getContext()));
                scansummAdapter = new ScansummAdapter(getContext(), modelList);
                recy_mis.setAdapter(scansummAdapter);
            }

        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            lin_border.setVisibility(View.GONE);
            recy_mis.setVisibility(View.GONE);
            GlobalClass.showTastyToast(getActivity(), MessageConstants.SOMETHING_WENT_WRONG, 2);
        }
    }
}
