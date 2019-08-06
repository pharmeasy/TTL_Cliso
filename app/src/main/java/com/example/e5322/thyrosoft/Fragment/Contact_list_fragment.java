package com.example.e5322.thyrosoft.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.e5322.thyrosoft.Adapter.Company_Adapter;
import com.example.e5322.thyrosoft.Models.Company_Contact_Model;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Contact_list_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Contact_list_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contact_list_fragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView title;
    ImageView back,home;
    RecyclerView contact_list;
    Spinner contact_type_spinner;
    private RequestQueue requestQueue_CompanyContact;
    Company_Contact_Model company_contact_model;
    LinearLayoutManager linearLayoutManager;
    Company_Adapter company_adapter;
    private OnFragmentInteractionListener mListener;
    private static ManagingTabsActivity mContext;
    ImageView add;
    View viewfab;
    View viewMain;
    View view;
    private String passSpinner_value;
    private String TAG= ManagingTabsActivity.class.getSimpleName().toString();

    public Contact_list_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contact_list_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Contact_list_fragment newInstance(String param1, String param2) {
        Contact_list_fragment fragment = new Contact_list_fragment();
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
        viewMain = (View) inflater.inflate(R.layout.fragment_contact_list_fragment, container, false);
        contact_list=(RecyclerView)viewMain.findViewById(R.id.contact_list);
        contact_type_spinner=(Spinner)viewMain.findViewById(R.id.contact_type_spinner);
        linearLayoutManager = new LinearLayoutManager(mContext);
        contact_list.setLayoutManager(linearLayoutManager);
        ArrayAdapter company_spinner = ArrayAdapter.createFromResource(mContext, R.array.company_contact_spinner_values, R.layout.spinnerproperty);
        contact_type_spinner.setAdapter(company_spinner);

        getCompany_contact_details();

        contact_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCompany_contact_details();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Inflate the layout for this fragment
        return viewMain;
    }

    private void getCompany_contact_details() {
        String getSpinnertype=contact_type_spinner.getSelectedItem().toString();
        requestQueue_CompanyContact = Volley.newRequestQueue(mContext);

        if(getSpinnertype.equals("STATE OFFICER")){
            passSpinner_value="STATE%20OFFICER";
        }else {
            passSpinner_value=getSpinnertype;
        }

        JsonObjectRequest jsonObjectRequestFAQ = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + passSpinner_value +"/"+ "Contact_Details", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: "+response );
                company_contact_model=null;
                company_contact_model = new Company_Contact_Model();
                Gson gson = new Gson();
                company_contact_model = gson.fromJson(response.toString(), Company_Contact_Model.class);
                if(company_contact_model.getResponse().equals("Success")){
                    //company_adapter = new Company_Adapter(mContext, company_contact_model.getContact_Array_list());
                   // contact_list.setAdapter(company_adapter);
                   // company_adapter.notifyDataSetChanged();
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
        requestQueue_CompanyContact.add(jsonObjectRequestFAQ);
        Log.e(TAG, "getCompany_contact_details: URL"+jsonObjectRequestFAQ );
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
