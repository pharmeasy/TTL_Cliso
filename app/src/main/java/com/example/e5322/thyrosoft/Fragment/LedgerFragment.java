package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.LedgerSumm_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.LedgerSummaryRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.LedgerSummaryResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LedgerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedgerFragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RequestQueue PostQue;
    TextView open_bal, closing__bal, credit, debit, billed_amt, cash_cheque, credit_limit, txt_unbillwoe, txt_unbill_woe, txt_unbill_material;
    Spinner year, month;
    Button ledgerdetails;
    ArrayAdapter monthadap;
    LinearLayout parent_ll;
    int monthSEND = 0;
    int thisYear = 0;
    LinearLayout offline_img;
    ArrayList<String> years;
    ArrayList<String> monthlist;
    int thismonth = 0;
    int MonthSEND = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private boolean flagfor1sttime = false;
    private int selectedMonthPosition = 0;
    private String TAG = getClass().getSimpleName();
    Activity mActivity;

    public LedgerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LedgerFragment newInstance(String param1, String param2) {
        LedgerFragment fragment = new LedgerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_ledger, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);

        String CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);

        initViews(view);

        try {
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                GlobalClass.SetText(txt_unbill_woe, "Unbilled Scan");
            } else {
                GlobalClass.SetText(txt_unbill_woe, "Unbilled WOE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        years = new ArrayList<String>();
        monthlist = new ArrayList<String>();
        thismonth = Calendar.getInstance().get(Calendar.MONTH);

        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();

        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear - 2; i--) {
            years.add(Integer.toString(i));

        }

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }


        ArrayAdapter yearadap = new ArrayAdapter(getContext(), R.layout.spinner_property_main, years);
        yearadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearadap);

        initListner();
        return view;
    }

    private void initListner() {
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthlist = new ArrayList<String>();
                years = new ArrayList<String>();
                DateFormatSymbols symbols = new DateFormatSymbols();
                String[] monthNames = symbols.getMonths();

                if (Integer.parseInt(year.getSelectedItem().toString()) == thisYear) {
                    for (int i = 0; i <= thismonth; i++) {
                        monthlist.add(monthNames[i].toString());
                        monthadap = new ArrayAdapter(getContext(), R.layout.spinner_property_main, monthlist);
                    }
                } else {
                    monthadap = new ArrayAdapter(getContext(), R.layout.spinner_property_main, monthNames);

                }
                monthadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month.setAdapter(monthadap);

                if (Integer.parseInt(year.getSelectedItem().toString()) == thisYear) {
                    if (!flagfor1sttime) {

                        flagfor1sttime = true;
                        month.setSelection(thismonth);
                    } else {
                        if (selectedMonthPosition > thismonth) {
                            month.setSelection(thismonth);
                        } else {
                            month.setSelection(selectedMonthPosition);
                        }

                    }

                } else {
                    month.setSelection(selectedMonthPosition);
                }

                if (monthNames != null && monthNames.length != 0) {
                    for (int i = 0; i < monthNames.length; i++) {
                        if (month.getSelectedItem().equals(monthNames[i])) {
                            monthSEND = i + 1;
                            break;
                        }
                    }
                }


                ledgerdetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ledger_details a2Fragment = new Ledger_details();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();

                    }
                });


                if (!GlobalClass.isNetworkAvailable(getActivity())) {
                    offline_img.setVisibility(View.VISIBLE);
                    parent_ll.setVisibility(View.GONE);
                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                    parent_ll.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateFormatSymbols symbols = new DateFormatSymbols();
                String[] monthNames = symbols.getMonths();
                selectedMonthPosition = position;
                for (int i = 0; i < monthNames.length; i++) {
                    if (month.getSelectedItem().equals(monthNames[i])) {
                        monthSEND = i + 1;
                        break;
                    }
                }

                if (!GlobalClass.isNetworkAvailable(getActivity())) {
                    offline_img.setVisibility(View.VISIBLE);
                } else {
                    GetData();
                    offline_img.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initViews(View view) {
        open_bal = (TextView) view.findViewById(R.id.open_bal);
        credit_limit = (TextView) view.findViewById(R.id.credit_limit);
        closing__bal = (TextView) view.findViewById(R.id.closing__bal);
        credit = (TextView) view.findViewById(R.id.credit);
        debit = (TextView) view.findViewById(R.id.debit);
        billed_amt = (TextView) view.findViewById(R.id.billed_amt);
        cash_cheque = (TextView) view.findViewById(R.id.cash_cheque);
        offline_img = (LinearLayout) view.findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) view.findViewById(R.id.parent_ll);

        txt_unbill_woe = view.findViewById(R.id.unbilled_woe);
        txt_unbillwoe = view.findViewById(R.id.txt_unbillwoe);

        txt_unbill_material = view.findViewById(R.id.unbilled_material);

        month = (Spinner) view.findViewById(R.id.month);
        year = (Spinner) view.findViewById(R.id.year);
        ledgerdetails = (Button) view.findViewById(R.id.ledgerdetails);

    }


    private void GetData() {

        JSONObject jsonObject = null;
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String api_key = prefs.getString("API_KEY", null);

            LedgerSummaryRequestModel requestModel = new LedgerSummaryRequestModel();
            requestModel.setApiKey(api_key);
            if (monthSEND < 10) {
                requestModel.setMonth("0" + monthSEND);
            } else {
                requestModel.setMonth("" + monthSEND);
            }
            requestModel.setYear(year.getSelectedItem().toString());
            requestModel.setUserCode(user);

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);

            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());

        try {
            if (ControllersGlobalInitialiser.ledgerSumm_controller != null) {
                ControllersGlobalInitialiser.ledgerSumm_controller = null;
            }
            ControllersGlobalInitialiser.ledgerSumm_controller = new LedgerSumm_Controller(mActivity, LedgerFragment.this);
            ControllersGlobalInitialiser.ledgerSumm_controller.ledgersummcontroller(jsonObject, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getledgerResp(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            Gson gson = new Gson();
            LedgerSummaryResponseModel responseModel = gson.fromJson(String.valueOf(response), LedgerSummaryResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(getActivity());
                } else {
                    SharedPreferences prefs = getActivity().getSharedPreferences("profile", MODE_PRIVATE);

                    String credit_limit_value = prefs.getString(Constants.credit_limit, null);

                    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("en", "in"));
                    String opening_bal = responseModel.getOpeningBalance();
                    String closing_bal = responseModel.getClosingBalance();
                    String credit_bal = responseModel.getCreditNotes();
                    String debit_bal = responseModel.getDebitNotes();
                    String billed_bal = responseModel.getBillAmount();
                    String cheque_bal = responseModel.getCashCheque();
                    String unbill_woe = responseModel.getUnbilledWOE();
                    String unbill_mat = responseModel.getUnbilledMaterial();


                    double openingbal = Double.parseDouble(opening_bal);
                    double closingbal = Double.parseDouble(closing_bal);
                    double creditbal = Double.parseDouble(credit_bal);
                    double debitbal = Double.parseDouble(debit_bal);
                    double billedbal = Double.parseDouble(billed_bal);
                    double chequebal = Double.parseDouble(cheque_bal);
                    double credit_bal_ltd = Double.parseDouble(credit_limit_value);
                    double unbillwoe = Double.parseDouble(unbill_woe);
                    double unbillmat = Double.parseDouble(unbill_mat);

                    String ope_bal_str = numberFormat.format(openingbal);
                    String clos_bal_str = numberFormat.format(closingbal);
                    String credit_bal_str = numberFormat.format(creditbal);
                    String debit_bal_str = numberFormat.format(debitbal);
                    String billed_bal_str = numberFormat.format(billedbal);
                    String cheque_bal_str = numberFormat.format(chequebal);
                    String str_unbillwoe = numberFormat.format(unbillwoe);
                    String str_unbillmat = numberFormat.format(unbillmat);
                    String credit_bal_str_value = numberFormat.format(credit_bal_ltd);


                    GlobalClass.SetText(open_bal, ope_bal_str);
                    GlobalClass.SetText(closing__bal, clos_bal_str);
                    GlobalClass.SetText(credit, credit_bal_str);
                    GlobalClass.SetText(debit, debit_bal_str);
                    GlobalClass.SetText(billed_amt, billed_bal_str);
                    GlobalClass.SetText(cash_cheque, cheque_bal_str);
                    GlobalClass.SetText(txt_unbill_woe, str_unbillwoe);
                    GlobalClass.SetText(txt_unbill_material, str_unbillmat);
                    GlobalClass.SetText(credit_limit, "Credit Limit :" + credit_bal_str_value);

                }
            } else {
                GlobalClass.showTastyToast(getActivity(), ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {

            Log.e(TAG, "on error-->" + e.getLocalizedMessage());
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
