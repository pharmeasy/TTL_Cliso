package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampSummary.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampSummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampSummary extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button btn_save;
    TextView txt_camp_date, txt_camp_profile, txt_camp_rate, txt_camp_days, txt_camp_address;

    public CampSummary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CampSummary.
     */
    // TODO: Rename and change types and number of parameters
    public static CampSummary newInstance(String param1, String param2) {
        CampSummary fragment = new CampSummary();
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
        View view = inflater.inflate(R.layout.fragment_camp_summary, container, false);

        initViews(view);
        initListner();

        return view;
    }

    private void initListner() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.showTastyToast(getActivity(), "Camp intimation completed !", 1);
                SelectTest fragmentB = new SelectTest();
                replaceFragment(fragmentB);

            }
        });
    }

    private void initViews(View view) {
        txt_camp_date = (TextView) view.findViewById(R.id.txt_camp_date);
        txt_camp_profile = (TextView) view.findViewById(R.id.txt_camp_profile);
        txt_camp_rate = (TextView) view.findViewById(R.id.txt_camp_rate);
        txt_camp_days = (TextView) view.findViewById(R.id.txt_camp_days);
        txt_camp_address = (TextView) view.findViewById(R.id.txt_camp_address);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        ArrayList<Camp_Intimatgion_List_Model> arraylist = getArguments().getParcelableArrayList("list");

        if (GlobalClass.CheckArrayList(arraylist)){
            for (int i = 0; i < arraylist.size(); i++) {

                GlobalClass.SetText(txt_camp_date, arraylist.get(i).getCamp_date());
                GlobalClass.SetText(txt_camp_profile, arraylist.get(i).getCamp_profile());
                GlobalClass.SetText(txt_camp_rate, arraylist.get(i).getCamp_rate());
                GlobalClass.SetText(txt_camp_days, arraylist.get(i).getSample_per_day());
                GlobalClass.SetText(txt_camp_address, arraylist.get(i).getCamp_address());

            }
        }

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

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

}
