package com.example.e5322.thyrosoft.Fragment;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Adapter.LedgerDetail_Debit;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listview;
    public static RequestQueue PostQue;
    int monthSEND=0;
    int thisYear=0;
    ArrayList<String> years;
    ArrayList<String> monthlist;
    int thismonth=0;
    ArrayList<Ledger_DetailsModel> ledgerDetList ;
    private FirstFragment.OnFragmentInteractionListener mListener;
    LedgerDetail_Debit Adapter;
    ArrayList spinnerdataCredit;
    Spinner typespinner;
    public ThirdFragment() {
        // Required empty public constructor
    }


    public static ThirdFragment newInstance(String text) {

        ThirdFragment f = new ThirdFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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

        View view = inflater.inflate(R.layout.fragment_third, container, false);

        listview=(ListView)view.findViewById(R.id.listview);
        typespinner=(Spinner)view.findViewById(R.id.typespinner) ;

        //adapter=new Fragment1_ledgerDet_adapter(getContext(),)

        ArrayAdapter yearadap = new ArrayAdapter(getContext(),R.layout.spinner_item_single_view, GlobalClass.debitlist);
        yearadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(yearadap);

        if(GlobalClass.DEBIT.size()>0&&getContext()!=null){
            Adapter= new LedgerDetail_Debit(getContext(), GlobalClass.DEBIT);
            listview.setAdapter(Adapter);


        }
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<Ledger_DetailsModel> filteredModelList = filter(GlobalClass.DEBIT, typespinner.getSelectedItem().toString());

                Adapter.setFilter(filteredModelList);

                listview.setAdapter(Adapter);
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    private List<Ledger_DetailsModel> filter(List<Ledger_DetailsModel> models, String query) {
        query = query.toLowerCase();final List<Ledger_DetailsModel> filteredModelList = new ArrayList<>();
        for (Ledger_DetailsModel model : models) {
            final String name = model.getNarration().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
/*    private void GetData() {


        PostQue = Volley.newRequestQueue(getContext());


        JSONObject jsonObject = new JSONObject();
        try {
            // monthSEND= Integer.parseInt(month.getSelectedItem().toString());

            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);

            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

           *//* {
                "apiKey":"qpa6@YJ9XY@LP8Hzxn4PFz3M5WU4NaGo)bsELEn8lFY=",
                    "month":"04" ,
                    "userCode":"TC001" ,
                    "year":"2018"
            }*//*
            jsonObject.put("apiKey",api_key);



            jsonObject.put(Constants.month, GlobalClass.MONTH);

            jsonObject.put(Constants.UserCode_billing,user);
            jsonObject.put(Constants.year,GlobalClass.YEAR);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.LedgerDetLive, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.optJSONArray(Constants.ledgerListDetails);
                           // JSONArray jsonArraycredit = response.optJSONArray(Constants.credits);
                             JSONArray jsonArraydebit = response.optJSONArray(Constants.debits);


                            ArrayList<String> listdata = new ArrayList<String>();
                            JSONArray jArray = (JSONArray)jsonArraydebit;
                            if (jArray != null) {
                                for (int i=0;i<jArray.length();i++){
                                    listdata.add(jArray.getString(i));
                                }
                                ArrayAdapter yearadap = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,listdata);
                                yearadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                typespinner.setAdapter(yearadap);
                            }

                            ledgerDetList = new ArrayList<Ledger_DetailsModel>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                try {


                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Ledger_DetailsModel ledgerModel = new Ledger_DetailsModel();

                                    ledgerModel.setAmount(jsonObject.optString(Constants.amount).toString());
                                    ledgerModel.setAmountType(jsonObject.optString(Constants.amountType).toString());
                                    ledgerModel.setDate(jsonObject.optString(Constants.date).toString());
                                    ledgerModel.setNarration(jsonObject.optString(Constants.narration).toString());
                                    ledgerModel.setTransactionType(jsonObject.optString(Constants.transactionType).toString());

                                    if(jsonObject.optString(Constants.amountType).toString().equals("DEBIT")){
                                        ledgerDetList.add(ledgerModel);

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if(ledgerDetList.size()>0&&getContext()!=null){
                                Adapter= new LedgerDetail_Debit(getContext(),ledgerDetList);
                                listview.setAdapter(Adapter);


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

    }*/

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
