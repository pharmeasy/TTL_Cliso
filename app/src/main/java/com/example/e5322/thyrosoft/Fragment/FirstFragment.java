package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.Ledger_Details_listAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.LedgerDetail_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listview;
    public static RequestQueue PostQue;
    LinearLayout type;
    ArrayList<Ledger_DetailsModel> ledgerDetList;
    private OnFragmentInteractionListener mListener;
    Ledger_Details_listAdapter Adapter;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    Activity mActivity;
    ConnectionDetector cd;

    public FirstFragment() {
        // Required empty public constructor
    }


    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
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
        mActivity = getActivity();
        cd = new ConnectionDetector(mActivity);

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        initViews(view);

        GetData();

        return view;
    }

    private void initViews(View view) {
        listview = (ListView) view.findViewById(R.id.listview);
        type = (LinearLayout) view.findViewById(R.id.type);
    }

    private void GetData() {

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);

            String user = prefs.getString("Username", null);
            String api_key = prefs.getString("API_KEY", null);
            jsonObject.put("apiKey", api_key);
            jsonObject.put(Constants.month, GlobalClass.MONTH);
            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put(Constants.year, GlobalClass.YEAR);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(getContext());

        try {
            if (ControllersGlobalInitialiser.ledgerDetail_controller != null) {
                ControllersGlobalInitialiser.ledgerDetail_controller = null;
            }
            ControllersGlobalInitialiser.ledgerDetail_controller = new LedgerDetail_Controller(mActivity, FirstFragment.this);
            ControllersGlobalInitialiser.ledgerDetail_controller.getledgerdetail(jsonObject,queue);
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

    public void getResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            JSONArray jsonArray = response.optJSONArray(Constants.ledgerListDetails);
            JSONArray jsonArraycredit = response.optJSONArray(Constants.credits);
            JSONArray jsonArraydebit = response.optJSONArray(Constants.debits);
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


                    ledgerDetList.add(ledgerModel);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (GlobalClass.CheckArrayList(ledgerDetList) && getContext() != null) {
                Adapter = new Ledger_Details_listAdapter(getContext(), ledgerDetList);
                listview.setAdapter(Adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
