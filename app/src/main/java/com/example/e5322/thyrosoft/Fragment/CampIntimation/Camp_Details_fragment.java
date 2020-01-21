package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Camp_Details_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camp_Details_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txt_camp_date, txt_camp_profile, txt_camp_rate, txt_camp_days, txt_camp_address, txt_camp_duration;
    private OnFragmentInteractionListener mListener;
    private View viewMain;
    private String camp_date, camp_profile, camp_rate, camp_days, camp_address, camp_duration;

    public Camp_Details_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Camp_Details_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Camp_Details_fragment newInstance(String param1, String param2) {
        Camp_Details_fragment fragment = new Camp_Details_fragment();
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

        viewMain = (View) inflater.inflate(R.layout.fragment_camp__details_fragment, container, false);

        txt_camp_date = (TextView) viewMain.findViewById(R.id.txt_camp_date);
        txt_camp_profile = (TextView) viewMain.findViewById(R.id.txt_camp_profile);
        txt_camp_rate = (TextView) viewMain.findViewById(R.id.txt_camp_rate);
        txt_camp_days = (TextView) viewMain.findViewById(R.id.txt_camp_days);
        txt_camp_address = (TextView) viewMain.findViewById(R.id.txt_camp_address);
        txt_camp_duration = (TextView) viewMain.findViewById(R.id.txt_camp_duration);

        camp_date =getArguments().getString("camp_date");
        camp_profile =getArguments().getString("camp_profile");
        camp_rate=getArguments().getString("camp_rate");
        camp_days =getArguments().getString("camp_days");
        camp_address =getArguments().getString("camp_address");
        camp_duration=getArguments().getString("camp_duration");

        txt_camp_date.setText(camp_date);
        txt_camp_profile.setText(camp_profile);
        txt_camp_rate.setText(camp_rate);
        txt_camp_days.setText(camp_days);
        txt_camp_address.setText(camp_address);
        txt_camp_duration.setText(camp_duration);

        // Inflate the layout for this fragment
        return viewMain;
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
