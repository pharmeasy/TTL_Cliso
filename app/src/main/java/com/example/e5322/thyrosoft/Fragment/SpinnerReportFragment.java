package com.example.e5322.thyrosoft.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.TrackDetAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpinnerReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpinnerReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinnerReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView ListReportStatus;
    ArrayList<TrackDetModel> FilterReport = new ArrayList<TrackDetModel>();
    private OnFragmentInteractionListener mListener;
    public static RequestQueue PostQue;
    TrackDetAdapter adapter;
    String TAG= ManagingTabsActivity.class.getSimpleName().toString();
    public SpinnerReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpinnerReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpinnerReportFragment newInstance(String param1, String param2) {
        SpinnerReportFragment fragment = new SpinnerReportFragment();
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
        View view =inflater.inflate(R.layout.fragment_spinner_report, container, false);
                ListReportStatus = (ListView)view.findViewById(R.id.ListReportStatus);
        GetData();







        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void GetData() {

        PostQue = GlobalClass.setVolleyReq(getContext());




            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
           String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
          String api_key = prefs.getString("API_KEY", null);

        JSONObject jsonObject = new JSONObject();



        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.ResultLIVE+"/"+api_key+"/"+ GlobalClass.type+"/"+user+"/"+ GlobalClass.date+"/"+"key/value", jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE"+response );
                            JSONArray jsonArray = response.optJSONArray(Constants.patients);
                            if (jsonArray != null) {

                                FilterReport = new ArrayList<TrackDetModel>();

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    TrackDetModel trackdetails = new TrackDetModel();

                                    trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                                    trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                                    trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                                    trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                  //  trackdetails.setCancel_tests_with_remark(jsonObject.optString(Constants.cancel_tests_with_remark).toString());
                                    trackdetails.setChn_pending(jsonObject.optString(Constants.chn_pending).toString());
                                    trackdetails.setChn_test(jsonObject.optString(Constants.chn_test).toString());
                                    trackdetails.setConfirm_status(jsonObject.optString(Constants.confirm_status).toString());
                                    trackdetails.setDate(jsonObject.optString(Constants.date).toString());
                                    trackdetails.setEmail(jsonObject.optString(Constants.email).toString());
                                    trackdetails.setIsOrder(jsonObject.optString(Constants.isOrder).toString());
                                    trackdetails.setLabcode(jsonObject.optString(Constants.labcode).toString());
                                    trackdetails.setLeadId(jsonObject.optString(Constants.leadId).toString());
                                    trackdetails.setName(jsonObject.optString(Constants.name).toString());
                                    trackdetails.setPatient_id(jsonObject.optString(Constants.patient_id).toString());
                                    trackdetails.setPdflink(jsonObject.optString(Constants.pdflink).toString());
                                    trackdetails.setSample_type(jsonObject.optString(Constants.sample_type).toString());
                                    trackdetails.setScp(jsonObject.optString(Constants.scp).toString());
                                    trackdetails.setSct(jsonObject.optString(Constants.sct).toString());
                                    trackdetails.setSu_code2(jsonObject.optString(Constants.su_code2).toString());
                                    trackdetails.setWo_sl_no(jsonObject.optString(Constants.wo_sl_no).toString());

                                    FilterReport.add(trackdetails);
                                }
                                adapter = new TrackDetAdapter(getContext(), FilterReport);
                                ListReportStatus .setAdapter(adapter);
                            }
                        }catch (Exception e){
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
        Log.e(TAG, "GetData: URL"+jsonObjectRequest );
        Log.e(TAG, "GetData: json"+jsonObject );
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
