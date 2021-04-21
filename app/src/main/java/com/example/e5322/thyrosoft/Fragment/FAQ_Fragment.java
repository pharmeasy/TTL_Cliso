package com.example.e5322.thyrosoft.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.ExpandableListAdapter_FAQ;
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQ_Model;
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQandANSArray;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FAQ_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAQ_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQ_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static ManagingTabsActivity mContext;
    ImageView add;
    View viewfab;
    FAQ_Model faq_model;
    ExpandableListAdapter_FAQ expandableListAdapter;
    View viewMain;
    private RequestQueue requestQueueSpinner_value, requestQueue_FAQ;
    View view;
    ExpandableListView expandable_list_faq;
    public static ArrayList<String> type_spinner_value;
    Spinner category_spinner;
    String user, passwrd, access, api_key, client_type;
    private SharedPreferences prefs;
    private OnFragmentInteractionListener mListener;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    ProgressDialog barProgressDialog;

    public FAQ_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAQ_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQ_Fragment newInstance(String param1, String param2) {
        FAQ_Fragment fragment = new FAQ_Fragment();
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

        mContext = (ManagingTabsActivity) getActivity();

        viewfab = (View) inflater.inflate(R.layout.app_bar_main_data, container, false);


        viewMain = (View) inflater.inflate(R.layout.activity_faq_, container, false);
        expandable_list_faq = (ExpandableListView) viewMain.findViewById(R.id.faq_list_expandable);
        category_spinner = (Spinner) viewMain.findViewById(R.id.category_spinner);
        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        client_type = prefs.getString("CLIENT_TYPE", "");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, "");
        java.lang.reflect.Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> arrayList = gson.fromJson(json, type);
        type_spinner_value = new ArrayList<>();

        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                type_spinner_value.add(arrayList.get(i).toString());
            }
            ArrayAdapter type_value_spinner = new ArrayAdapter(mContext, R.layout.spinnerproperty, type_spinner_value);
            category_spinner.setAdapter(type_value_spinner);
            getAll_FAQ_data();
        } else {
            getSpinnerdata();
        }


        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getAll_FAQ_data();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return viewMain;
    }

    private void getSpinnerdata() {
        barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        requestQueueSpinner_value = GlobalClass.setVolleyReq(mContext);

        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + client_type + "/Get_Faq_Type_Spinner", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: response" + response);
                String finalJson = response.toString();
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }

                JSONObject parentObjectOtp = null;
                type_spinner_value = new ArrayList<>();
                try {
                    parentObjectOtp = new JSONObject(finalJson);
                    JSONArray getSpinner_type = parentObjectOtp.getJSONArray("FAQ_type_spinnner_names");
                    String get_response = parentObjectOtp.getString("response");

                    if (get_response.equals("Success")) {
                        for (int i = 0; i < getSpinner_type.length(); i++) {
                            type_spinner_value.add(getSpinner_type.get(i).toString());
                        }
                        ArrayAdapter type_value_spinner = new ArrayAdapter(mContext, R.layout.spinnerproperty, type_spinner_value);
                        category_spinner.setAdapter(type_value_spinner);

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(type_spinner_value);
                        editor.putString(TAG, json);
                        editor.commit();

                        getAll_FAQ_data();

                    } else {
                        //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(mContext, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });
        jsonObjectRequestProfile.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueSpinner_value.add(jsonObjectRequestProfile);
        Log.e(TAG, "getSpinnerdata: URL" + jsonObjectRequestProfile);
    }

    private void getAll_FAQ_data() {
        String getSpinner_value = category_spinner.getSelectedItem().toString();
        requestQueue_FAQ = GlobalClass.setVolleyReq(mContext);

        Log.e(TAG,"FAQ API--->"+Api.static_pages_link + client_type + "/" + getSpinner_value + "/GetAllFAQ");
        JsonObjectRequest jsonObjectRequestFAQ = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + client_type + "/" + getSpinner_value + "/GetAllFAQ", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: response" + response);
                Gson gson = new Gson();
                faq_model = gson.fromJson(response.toString(), FAQ_Model.class);

                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                Gson gson22 = new Gson();
                String json22 = gson22.toJson(faq_model);
                prefsEditor1.putString("FAQ_DATA", json22);
                prefsEditor1.commit();

                if (faq_model.getResponse().equals("Success")) {
                    if (faq_model.getFAQandANSArray() != null) {
                        ArrayList<FAQandANSArray> faq_list = new ArrayList<>();
                        for (int i = 0; i < faq_model.getFAQandANSArray().length; i++) {
                            faq_list.add(faq_model.getFAQandANSArray()[i]);
                        }
                        expandableListAdapter = new ExpandableListAdapter_FAQ(faq_list, mContext);
                        expandable_list_faq.setAdapter(expandableListAdapter);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(mContext, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });
        jsonObjectRequestFAQ.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_FAQ.add(jsonObjectRequestFAQ);
        Log.e(TAG, "getAll_FAQ_data: URL" + jsonObjectRequestFAQ);
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
