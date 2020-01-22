package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.ScansummAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GenerateTokenController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.Models.TokenModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ScanSummaryActivity extends Fragment {
    Calendar myCalendar;
    private String myFormat = "dd-MM-yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private TextView from_date, to_date, txt_nodata, txt_frm_dialog, txt_to_dialog;
    private String putDate;
    private String showDate;
    Date fromdate, To_date;
    private String getFormatDate;
    private long minDate;
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
                Toast.makeText(getContext(), "Please choose correct Date", Toast.LENGTH_SHORT).show();
            } else {
                to_date.setText(putDate);
                if (GlobalClass.isNetworkAvailable(((Activity) getActivity()))) {
                    if (cd.isConnectingToInternet()) {
                        setData();
                    } else {
                        GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
                    }
                } else {
                    GlobalClass.toastySuccess(getContext(), ToastFile.intConnection, false);
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
        initView(view);

        preferences = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = preferences.getString("Username", null);

        cd = new ConnectionDetector(getContext());
        generateToken();


    }


    private void  updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        fromdate = returndate(fromdate, putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);

        if (To_date.before(fromdate)) {
            Toast.makeText(getContext(), "Please choose correct Date", Toast.LENGTH_SHORT).show();
        } else {
            from_date.setText(putDate);
            if (GlobalClass.isNetworkAvailable((Activity) getContext())) {
                if (cd.isConnectingToInternet()) {
                    setData();
                } else {
                    GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
                }
            } else {
                GlobalClass.toastySuccess(getContext(), ToastFile.intConnection, false);
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
        from_date.setText(showDate);
        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today1 = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today1);
        showDate = sdf1.format(today1);
        to_date.setText(showDate);
        To_date = returndate(To_date, showDate);
        To_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

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

    private void generateToken() {
        if (cd.isConnectingToInternet()) {
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
        } else {
            //  GlobalClass.hideProgress(context, progressDialog);
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }

    }

    public void getGenerateTokenResponse(JSONObject response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson;
        gson = gsonBuilder.create();

        try {
            TokenModel tokenResponseModel = gson.fromJson(response.toString(), TokenModel.class);
            if (tokenResponseModel != null) {
                if (tokenResponseModel.getRespId().equalsIgnoreCase(Constants.RES0000)) {
                    if (!GlobalClass.isNull(tokenResponseModel.getAccess_token()) && !GlobalClass.isNull(tokenResponseModel.getToken_type())) {
                        if (cd.isConnectingToInternet()) {
                            if (tokenResponseModel != null) {
                                header = tokenResponseModel.getToken_type() + " " + tokenResponseModel.getAccess_token();
                                if (cd.isConnectingToInternet()) {
                                    setData();
                                } else {
                                    GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
                                }
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

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getActivity());
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.SCANSOAPI).create(APIInteface.class);
        Call<List<ScansummaryModel>> responseCall = apiInterface.getsummarydetail(header, str_frmdt, str_todt, user);

        Log.e("TAG", "S U M M A R Y  U R L --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<List<ScansummaryModel>>() {
            @Override
            public void onResponse(Call<List<ScansummaryModel>> call, Response<List<ScansummaryModel>> response) {
                try {
                    if (!response.body().isEmpty()) {
                        GlobalClass.hideProgress(getActivity(), progressDialog);

                        recy_mis.setVisibility(View.VISIBLE);
                        lin_border.setVisibility(View.VISIBLE);
                        txt_nodata.setVisibility(View.GONE);

                        if (scansummaryModelArrayList == null) {
                            scansummaryModelArrayList = new ArrayList<>();
                        }

                        scansummaryModelArrayList.clear();
                        scansummaryModelArrayList.addAll(response.body());
                        modelList = new ArrayList<>();

                        if (scansummaryModelArrayList != null) {
                            for (int i = 0; i < scansummaryModelArrayList.size(); i++) {
                                String[] bkdt = scansummaryModelArrayList.get(i).getBOOKEDAT().split("T");
                                String strbkt = bkdt[0];

                                String[] appodt = scansummaryModelArrayList.get(i).getAPPOINTMENT_DATE().split("T");
                                String strappo = appodt[0];

                                ScansummaryModel scansummaryModel = new ScansummaryModel();
                                scansummaryModel.setBOOKEDAT(GlobalClass.formatDate(Constants.YEARFORMATE, Constants.DATEFORMATE, strbkt));
                                scansummaryModel.setADDRESS(scansummaryModelArrayList.get(i).getADDRESS());
                                scansummaryModel.setAPPOINTMENT_DATE(GlobalClass.formatDate(Constants.YEARFORMATE, Constants.DATEFORMATE, strappo));
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

                        if (modelList != null) {
                            recy_mis.setLayoutManager(new LinearLayoutManager(getContext()));
                            scansummAdapter = new ScansummAdapter(getContext(), modelList);
                            recy_mis.setAdapter(scansummAdapter);
                        }

                    } else {
                        txt_nodata.setVisibility(View.VISIBLE);
                        lin_border.setVisibility(View.GONE);
                        GlobalClass.hideProgress(getActivity(), progressDialog);
                        recy_mis.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    GlobalClass.hideProgress(getActivity(), progressDialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ScansummaryModel>> call, Throwable t) {
                GlobalClass.hideProgress(getActivity(), progressDialog);
            }
        });
    }
}
