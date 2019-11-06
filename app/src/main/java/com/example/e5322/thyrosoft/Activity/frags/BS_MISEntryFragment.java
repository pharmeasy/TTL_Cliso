package com.example.e5322.thyrosoft.Activity.frags;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.BS_MISAdapter;
import com.example.e5322.thyrosoft.Controller.BloodSugarMISController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Models.BSMISModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class BS_MISEntryFragment extends Fragment {

    private View viewMain;
    final Calendar myCalendar = Calendar.getInstance();
    LinearLayout bs_calendar_ll;
    TextView txt_bs_cal,tv_noResult;
    private RecyclerView recycler_view;
    private SharedPreferences prefs;
    Activity activity;
    private String user,passwrd,access,api_key,USER_CODE;
    private String currentDate;

    public BS_MISEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = (View) inflater.inflate(R.layout.bs_mis_fragment, container, false);

        activity = getActivity();

        initUI();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(linearLayoutManager);

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        USER_CODE = prefs.getString("USER_CODE", null);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(d);
        txt_bs_cal.setText(currentDate);

        callMISDataAPI(currentDate);

        bs_calendar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return viewMain;
    }

    private void initUI() {
        recycler_view = (RecyclerView)viewMain.findViewById(R.id.recycler_view);
        bs_calendar_ll= (LinearLayout) viewMain.findViewById(R.id.bs_calendar_ll);
        txt_bs_cal= (TextView) viewMain.findViewById(R.id.txt_bs_cal);
        tv_noResult = (TextView) viewMain.findViewById(R.id.tv_noResult);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        txt_bs_cal.setText(sdf.format(myCalendar.getTime()));
    }

    private void callMISDataAPI(String date) {
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", user);
                jsonObject.put("Date", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (ControllersGlobalInitialiser.bloodSugarMISController != null) {
                    ControllersGlobalInitialiser.bloodSugarMISController = null;
                }
                ControllersGlobalInitialiser.bloodSugarMISController = new BloodSugarMISController(BS_MISEntryFragment.this,activity);
                ControllersGlobalInitialiser.bloodSugarMISController.getMISData(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getMISResponse(JSONObject response) {
        Gson gson =new Gson();
      BSMISModel misModel = gson.fromJson(String.valueOf(response), BSMISModel.class);
        if (misModel != null) {
            if (misModel.getResId().equalsIgnoreCase(Constants.RES0000)) {
                if (misModel.getBloodSugarEntries().size() > 0) {
                    recycler_view.setVisibility(View.VISIBLE);
                    tv_noResult.setVisibility(View.GONE);
                    BS_MISAdapter bs_misAdapter = new BS_MISAdapter(activity, misModel.getBloodSugarEntries());
                    recycler_view.setAdapter(bs_misAdapter);
                } else {
                    recycler_view.setVisibility(View.GONE);
                    tv_noResult.setVisibility(View.VISIBLE);
                }
            } else
                Global.showCustomToast(activity, misModel.getResponse());
        } else
            Global.showCustomToast(activity, ToastFile.something_went_wrong);
    }
}
