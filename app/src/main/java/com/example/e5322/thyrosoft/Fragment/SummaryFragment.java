package com.example.e5322.thyrosoft.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.GetPatientSampleDetails;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.FragmentsHandlerActivity;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    StringBuilder commaSepValueBuilder = new StringBuilder();
    Context mContext;
    SharedPreferences prefs;
    MyPojo myPojo;
    BCT_LIST[] bct_lists;
    LinearLayout SGCLinearid, refbylinear, btechlinear,linear_scp;
    String user, passwrd, genderId, access, api_key, error, pid, response1, barcodes, resID, saveAgeType, getBtechName;
    ImageView delete_patient_test;
    private String mParam2;
    public static com.android.volley.RequestQueue deletePatienDetail;
    RecyclerView sample_list;
    ArrayList<Summary_model> summary_models;
    TextView pat_type, pat_sct, tests, pat_name, pat_ref, pat_sgc, pat_scp, pat_amt_collected, btech, btechtile;
    ProgressDialog barProgressDialog;
    LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;
    private String version;
    private int versionCode,passvalue;
    private String TAG=ManagingTabsActivity.class.getSimpleName().toString();

    public SummaryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
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


        View viewSummary = (View) inflater.inflate(R.layout.fragment_summary, container, false);

        pat_type = (TextView) viewSummary.findViewById(R.id.pat_type);
        pat_sct = (TextView) viewSummary.findViewById(R.id.pat_sct);
        pat_name = (TextView) viewSummary.findViewById(R.id.pat_name);
        pat_ref = (TextView) viewSummary.findViewById(R.id.pat_ref);
        pat_sgc = (TextView) viewSummary.findViewById(R.id.pat_sgc);
        pat_scp = (TextView) viewSummary.findViewById(R.id.pat_scp);
        pat_amt_collected = (TextView) viewSummary.findViewById(R.id.pat_amt_collected);
        tests = (TextView) viewSummary.findViewById(R.id.tests);
        btech = (TextView) viewSummary.findViewById(R.id.btech);
        btechtile = (TextView) viewSummary.findViewById(R.id.btechtile);
        sample_list = (RecyclerView) viewSummary.findViewById(R.id.sample_list);
        delete_patient_test = (ImageView) viewSummary.findViewById(R.id.delete_patient_test);

        SGCLinearid = (LinearLayout) viewSummary.findViewById(R.id.SGCLinearid);
        refbylinear = (LinearLayout) viewSummary.findViewById(R.id.refbylinear);
        btechlinear = (LinearLayout) viewSummary.findViewById(R.id.btechlinear);
        linear_scp = (LinearLayout) viewSummary.findViewById(R.id.linear_scp);
        linearLayoutManager = new LinearLayoutManager(getContext());
        sample_list.setLayoutManager(linearLayoutManager);

        if (!GlobalClass.summary_models.get(0).getWoeditlist().equals(0)) {
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("Y")) {
                saveAgeType = "Year";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("M")) {
                saveAgeType = "Month";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("D")) {
                saveAgeType = "Days";
            }
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("F")) {
                genderId = "Female";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("M")) {
                genderId = "Male";
            }
        } else {
            Woe_fragment notificationFragment = new Woe_fragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mainLayout, notificationFragment, notificationFragment.getClass().getSimpleName()).addToBackStack(null).commit();
        }


        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        getDataFromServer();
        GlobalClass.barcodelists = new ArrayList<>();
        ArrayList<String> getNames = new ArrayList<>();


        pat_type.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBRAND().toString() + "/" +
                GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getTYPE() + "/" +
                GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSR_NO() + "/" +
                GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());//getMAIN_SOURCE

        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME().equalsIgnoreCase("")) {
            pat_sct.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
        } else {
            pat_sct.setVisibility(View.GONE);
        }

        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME().equalsIgnoreCase("") ) {
            pat_name.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME() +
                    "(" + GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE() + "  " + saveAgeType + "/" + genderId + ")");
        } else {
            pat_name.setVisibility(View.GONE);
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME().equals(null)  || GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME().equalsIgnoreCase("")) {
            pat_ref.setVisibility(View.GONE);
            refbylinear.setVisibility(View.GONE);
        } else {

            pat_ref.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME() == "" || GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME().equals(null)) {
            SGCLinearid.setVisibility(View.GONE);
        } else {
            pat_sgc.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
        }
        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equalsIgnoreCase("") ) {
            pat_scp.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS());
        }if(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS().equals("(),,,")) {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        }else {
            pat_scp.setVisibility(View.GONE);
            linear_scp.setVisibility(View.GONE);
        }
        if (!GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equals(null) || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equalsIgnoreCase("") || !GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED().equals("null")) {
            pat_amt_collected.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED());
        } else {
            pat_amt_collected.setVisibility(View.GONE);
        }
        if (GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length != 0) {
            for (int i = 0; i < GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                getNames.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                GlobalClass.barcodelists.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i]);
                for (int j = 0; j < getNames.size(); j++) {
                    commaSepValueBuilder.append(getNames.get(j));
                    if (i != getNames.size()) {
                        String setSelectedTest = TextUtils.join(",", getNames);
                        tests.setText(setSelectedTest);
                    }
                }
            }
        }
        GetPatientSampleDetails getPatientSampleDetails = new GetPatientSampleDetails(mContext, GlobalClass.barcodelists,passvalue);
        sample_list.setAdapter(getPatientSampleDetails);

        delete_patient_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deletePatientDetailsandTest();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Intent intent = new Intent(getContext(), FragmentsHandlerActivity.class);
                                intent.putExtra("head", "WOE Edit");
                                intent.putExtra("type", "woe_edit");
                                getContext().startActivity(intent);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirm delete !");
                builder.setMessage(ToastFile.wish_woe_dlt).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("Edit", dialogClickListener).show();

            }
        });

        return viewSummary;
    }
    private void getDataFromServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.getBCTforSummary + api_key + "/" + user + "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response"+response );
                Gson gson = new Gson();
                MyPojo myPojo = gson.fromJson(response.toString(), MyPojo.class);
                BCT_LIST[] bct_lists = myPojo.getMASTERS().getBCT_LIST();

                if (bct_lists.length != 0) {
                    for (int i = 0; i < bct_lists.length; i++) {
                        if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBCT_ID().equals(bct_lists[i].getNED_NUMBER())) {
                            String nameofbtech = bct_lists[i].getNAME();
                            btech.setText(nameofbtech);
                            btechtile.setVisibility(View.VISIBLE);
                            btechlinear.setVisibility(View.VISIBLE);
                        } else {
                            btech.setVisibility(View.GONE);
                            btechtile.setVisibility(View.GONE);
                        }
                    }
                } else {
                    btech.setVisibility(View.GONE);
                    btechtile.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequestfetchData.setRetryPolicy(new DefaultRetryPolicy(500000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequestfetchData);
        Log.e(TAG, "getDataFromServer: URL"+jsonObjectRequestfetchData );
    }


    private void deletePatientDetailsandTest() {
        barProgressDialog = new ProgressDialog(getActivity());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        deletePatienDetail = Volley.newRequestQueue(getContext());
        JSONObject jsonObjectOtp = new JSONObject();
        try {

            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", GlobalClass.getPatientIdforDeleteDetails);
            jsonObjectOtp.put("tsp", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
                try {
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    error = parentObjectOtp.getString("error");
                    pid = parentObjectOtp.getString("pid");
                    response1 = parentObjectOtp.getString("response");
                    barcodes = parentObjectOtp.getString("barcodes");
                    resID = parentObjectOtp.getString("RES_ID");

                    if (resID.equals("RES0000")) {
                        TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Intent intent = new Intent(getContext(), ManagingTabsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
/*                        if(GlobalClass.flagToSend==true){
                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                            Preanalytical_test_fragment preanalytical_test_fragment = new Preanalytical_test_fragment();
                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_mainLayout, preanalytical_test_fragment);
                            fragmentTransaction.commit();
                            GlobalClass.flagToSend=false;
                            TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        }else if(GlobalClass.flagToSendfromnavigation==true){

                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                            TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Woe_fragment woe_fragment = new Woe_fragment();
                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_mainLayout, woe_fragment);
                            fragmentTransaction.commit();
                            GlobalClass.flagToSendfromnavigation=false;
                        }else{
                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                            TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Woe_fragment woe_fragment = new Woe_fragment();
                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_mainLayout, woe_fragment);
                            fragmentTransaction.commit();
                        }*/
                    } else {
                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}

                        TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(getContext(), response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    System.out.println(error);
                }
            }
        });
        deletePatienDetail.add(jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: url"+jsonObjectRequest1 );
        Log.e(TAG, "deletePatientDetailsandTest: json"+jsonObjectOtp );
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
