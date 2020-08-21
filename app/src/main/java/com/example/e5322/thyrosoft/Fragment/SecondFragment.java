package com.example.e5322.thyrosoft.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Adapter.Ledger_detail_Credit;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listview;
    public static RequestQueue PostQue;
    int monthSEND = 0;
    int thisYear = 0;
    ArrayList<String> years;
    ArrayList<String> monthlist;
    int thismonth = 0;
    ArrayList<Ledger_DetailsModel> ledgerDetList;
    private FirstFragment.OnFragmentInteractionListener mListener;
    Ledger_detail_Credit Adapter;
    ArrayList spinnerdataCredit;
    Spinner typespinner;

    public SecondFragment() {
        // Required empty public constructor
    }


    public static SecondFragment newInstance(String text) {

        SecondFragment f = new SecondFragment();
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

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        initViews(view);


        ArrayList<String> getArrayTOpass = new ArrayList<>();
        try {

            if (GlobalClass.CheckArrayList(GlobalClass.listdata)) {
                for (int i = 0; i < GlobalClass.listdata.size(); i++) {
                    if (!GlobalClass.isNull(GlobalClass.listdata.get(i)) && GlobalClass.listdata.get(i).equalsIgnoreCase("RECEIPT")) {
                        GlobalClass.listdata.remove(GlobalClass.listdata.get(i));
                    }
                    getArrayTOpass.add(GlobalClass.listdata.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter yearadap = new ArrayAdapter(getContext(), R.layout.spinner_item_single_view, getArrayTOpass);
        yearadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(yearadap);

        if (getContext() != null && GlobalClass.CheckArrayList(GlobalClass.CREDITLIST)) {
            Adapter = new Ledger_detail_Credit(getContext(), GlobalClass.CREDITLIST);
            listview.setAdapter(Adapter);
        }

        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<Ledger_DetailsModel> filteredModelList = filter(GlobalClass.CREDITLIST, typespinner.getSelectedItem().toString());

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

    private void initViews(View view) {
        listview = (ListView) view.findViewById(R.id.listview);
        typespinner = (Spinner) view.findViewById(R.id.typespinner);
    }

    private List<Ledger_DetailsModel> filter(List<Ledger_DetailsModel> models, String query) {
        query = query.toLowerCase();
        final List<Ledger_DetailsModel> filteredModelList = new ArrayList<>();
        for (Ledger_DetailsModel model : models) {
            final String name = model.getNarration().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
