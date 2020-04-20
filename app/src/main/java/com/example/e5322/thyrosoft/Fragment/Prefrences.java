package com.example.e5322.thyrosoft.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Prefrences.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Prefrences#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Prefrences extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView bcc, No, Nosms;
    public static RequestQueue PostQue;
    private OnFragmentInteractionListener mListener;
    Spinner spinnerbrand, deftype, report_type, action;
    Switch report_mail, auto_mail, auto_SMS;
    Button save;
    String automail;
    String TAG= ManagingTabsActivity.class.getSimpleName().toString();
    String autosms;
    String brand;
    String cancelaction;
    String reportformat;
    ProgressDialog barProgressDialog;

    String reportmail;
    String sample_type;

    public Prefrences() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Prefrences.
     */
    // TODO: Rename and change types and number of parameters
    public static Prefrences newInstance(String param1, String param2) {
        Prefrences fragment = new Prefrences();
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
        View view = inflater.inflate(R.layout.fragment_prefrences, container, false);
        spinnerbrand = (Spinner) view.findViewById(R.id.spinnerbrand);
        deftype = (Spinner) view.findViewById(R.id.deftype);
        report_type = (Spinner) view.findViewById(R.id.report_type);
        action = (Spinner) view.findViewById(R.id.action);
        report_mail = (Switch) view.findViewById(R.id.report_mail);
        auto_mail = (Switch) view.findViewById(R.id.auto_mail);
        auto_SMS = (Switch) view.findViewById(R.id.auto_SMS);
        bcc = (TextView) view.findViewById(R.id.bcc);
        No = (TextView) view.findViewById(R.id.No);
        Nosms = (TextView) view.findViewById(R.id.Nosms);
        save = (Button) view.findViewById(R.id.save);









        // TextView dateview = getActivity().findViewById(R.id.show_date);

        //dateview.setVisibility(View.GONE);



        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barProgressDialog = new ProgressDialog(getContext());
                barProgressDialog.setTitle("Kindly wait ...");
                barProgressDialog.setMessage(ToastFile.processing_request);
                barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                barProgressDialog.setProgress(0);
                barProgressDialog.setMax(20);
                barProgressDialog.show();
                barProgressDialog.setCanceledOnTouchOutside(false);
                barProgressDialog.setCancelable(false);

                PostData();
            }
        });

        String[] spinner = {"TTL", "DDL"};
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerbrand.setAdapter(aa);

        String[] spinner1 = {"CAMP", "DPS", "HOME", "ILS"};
        ArrayAdapter BB = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner1);
        BB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deftype.setAdapter(BB);

        String[] spinner2 = {"LETTER HEAD", "WITHOUT LETTER HEAD"};
        ArrayAdapter CC = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner2);
        CC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report_type.setAdapter(CC);

        String[] spinner4 = {"LEAD", "CREDIT"};
        ArrayAdapter DD = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner4);
        DD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        action.setAdapter(CC);


        report_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bcc.setText("CC");
                } else {
                    bcc.setText("BCC");

                }
            }
        });
        auto_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    No.setText("YES");
                } else {
                    No.setText("No");

                }
            }
        });


        auto_SMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Nosms.setText("YES");
                } else {
                    Nosms.setText("No");

                }
            }
        });


        GetPrefrences();


        return view;
    }

    private void PostData() {
        PostQue = GlobalClass.setVolleyReq(getContext());


        JSONObject jsonObject = new JSONObject();


        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
        String passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);


        try {
            jsonObject.put("api_key", api_key);
            jsonObject.put("tsp", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray jsonArr = new JSONArray();
        JSONArray jsonArr2 = new JSONArray();


        JSONObject pnObj = new JSONObject();
        JSONObject pnObj2 = new JSONObject();
        try {
            pnObj.put("AUTO_EMAIL", No.getText().toString());
            pnObj.put("AUTO_SMS", Nosms.getText().toString());
            pnObj.put("BRAND", spinnerbrand.getSelectedItem());
            pnObj.put("CANCEL_ACTION", action.getSelectedItem());
            pnObj.put("REPORT_FORMAT", report_type.getSelectedItem());
            pnObj.put("REPORT_MAIL", bcc.getText().toString());
            pnObj.put("SAMPLE_TYPE", deftype.getSelectedItem());
            jsonArr.put(pnObj);

            pnObj2.put("key", "");
            pnObj2.put("value", "");
            jsonArr2.put(pnObj2);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            jsonObject.put("tsppreference", jsonArr);
            jsonObject.put("tsppreferences", jsonArr2);
            jsonObject.put("error", "");
            jsonObject.put("response", "");
            jsonObject.put("RES_ID", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Api.PostPref, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {   /*{"RES_ID":"RES0000","api_key":null,"error":"","response":"PREFERENCES UPDATED SUCCESSFULLY","tsp":null,"tsppreference":null,"tsppreferences":null}*/
                        try {
                            Log.e(TAG, "onResponse: "+response );
                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                            TastyToast.makeText(getContext(), response.getString("response"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);
        Log.e(TAG, "PostData: URL"+jsonObjectRequest );
        Log.e(TAG, "PostData: "+jsonObject );

    }

    private void GetPrefrences() {

        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
        PostQue = GlobalClass.setVolleyReq(getContext());

        JSONObject jsonObject = new JSONObject();


        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
        String api_key = prefs.getString("API_KEY", null);


        try {
            jsonObject.put("api_key", api_key);
            jsonObject.put("tsp", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/getpreference", jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            /*{"RES_ID":"RES0000","api_key":"foxrUwNm2skUZWz6CggX4tlla16FI4HDDZbXECkng2c=",
                            "error":null,"response":"SUCCESS","tsp":"TAM03",
                            "tsppreference":[{"automail":"NO","autosms":"NO","brand":"TTL","cancelaction":"",
                            "reportformat":"WITHOUT LETTER HEAD","reportmail":"BCC","sample_type":"DPS"}],
                            "tsppreferences":null}*/

                            JSONArray jsonArray = response.optJSONArray(Constants.tsppreference);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                automail = jsonObject.getString(Constants.automail).toString();
                                autosms = jsonObject.getString(Constants.autosms).toString();
                                brand = jsonObject.getString(Constants.brand).toString();
                                cancelaction = jsonObject.getString(Constants.cancelaction).toString();
                                reportformat = jsonObject.getString(Constants.reportformat).toString();
                                reportmail = jsonObject.getString(Constants.reportmail).toString();
                                sample_type = jsonObject.getString(Constants.sample_type).toString();


                                if (!automail.equals("NO")) {
                                    auto_mail.setChecked(true);
                                } else {
                                    auto_mail.setChecked(false);
                                }
                                if (!autosms.equals("NO")) {
                                    auto_SMS.setChecked(true);
                                } else {
                                    auto_SMS.setChecked(false);

                                }
                                if (reportformat.equals("WITHOUT LETTER HEAD")) {
                                    report_type.setSelection(1);
                                } else {
                                    report_type.setSelection(2);
                                }
                                if (reportmail.equals("BCC")) {
                                    report_mail.setChecked(false);
                                } else {
                                    report_mail.setChecked(true);
                                }
                                if (sample_type.equals("CAMP")) {
                                    deftype.setSelection(0);
                                } else if (sample_type.equals("DPS")) {
                                    deftype.setSelection(1);
                                } else if (sample_type.equals("HOME")) {
                                    deftype.setSelection(2);
                                } else if (sample_type.equals("ILS")) {
                                    deftype.setSelection(3);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
