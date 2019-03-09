package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Preanalytical_test_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Preanalytical_test_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Preanalytical_test_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ManagingTabsActivity mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View viewMain;
    ImageView smallicon;
    View view_line,view_line1;
    TextView enetered, enter;

    private OnFragmentInteractionListener mListener;

    public Preanalytical_test_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Preanalytical_test_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Preanalytical_test_fragment newInstance(String param1, String param2) {
        Preanalytical_test_fragment fragment = new Preanalytical_test_fragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (ManagingTabsActivity) getActivity();

        viewMain = (View) inflater.inflate(R.layout.fragment_preanalytical_test_fragment, container, false);

        enetered = (TextView)viewMain. findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);
        view_line = (View)viewMain.findViewById(R.id.view_line);
        view_line1 =(View) viewMain.findViewById(R.id.view_line1);


        enter.setBackgroundColor(Color.parseColor("#FFFFFF"));//FFFFFF
        enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));
        view_line.setVisibility(View.VISIBLE);

        Start_New_Woe fragment = new Start_New_Woe();
        mContext.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        if (GlobalClass.setFlagBackToWoe == true) {
            Woe_fragment fragment1 = new Woe_fragment();
            mContext.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
            GlobalClass.setFlagBackToWoe=false;
        }else if(GlobalClass.setFlag_back_toWOE==true){
            Woe_fragment fragment1 = new Woe_fragment();
            mContext.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_mainLayout, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
            GlobalClass.setFlag_back_toWOE=false;
        }else{

        }

        enetered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));//
                enter.setBackgroundColor(Color.parseColor("#FFFFFF"));
                view_line.setVisibility(View.GONE);
                view_line1.setVisibility(View.VISIBLE);
                GlobalClass.flagToSend=true;
                GlobalClass.flagToSendfromnavigation=false;

                Woe_fragment fragment = new Woe_fragment();
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view_line.setVisibility(View.VISIBLE);
                view_line1.setVisibility(View.GONE);
                enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));//
                enter.setBackgroundColor(Color.parseColor("#FFFFFF"));

                Start_New_Woe fragment = new Start_New_Woe();
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();




//                Old_Woe_Flow old_woe_flow = new Old_Woe_Flow();
//                mContext.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_mainLayout, old_woe_flow, old_woe_flow.getClass().getSimpleName()).addToBackStack(null).commit();


            }
        });
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
