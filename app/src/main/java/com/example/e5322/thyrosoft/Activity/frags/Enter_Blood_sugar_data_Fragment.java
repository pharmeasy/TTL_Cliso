package com.example.e5322.thyrosoft.Activity.frags;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Enter_Blood_sugar_data_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Enter_Blood_sugar_data_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Enter_Blood_sugar_data_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spin_bs_test;
    private OnFragmentInteractionListener mListener;

    public Enter_Blood_sugar_data_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Enter_Blood_sugar_data_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Enter_Blood_sugar_data_Fragment newInstance(String param1, String param2) {
        Enter_Blood_sugar_data_Fragment fragment = new Enter_Blood_sugar_data_Fragment();
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
        View viewMain = (View) inflater.inflate(R.layout.fragment_entered__blood_sugar_activity, container, false);

        spin_bs_test=(Spinner)viewMain.findViewById(R.id.spin_bs_test);

        ArrayAdapter<BSTestDataModel> adap = new ArrayAdapter<BSTestDataModel>(getActivity().getApplicationContext(), R.layout.name_age_spinner, GlobalClass.getTestList());
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_bs_test.setAdapter(adap);

        return viewMain;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
