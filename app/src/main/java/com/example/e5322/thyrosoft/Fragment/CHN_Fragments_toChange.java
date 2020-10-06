package com.example.e5322.thyrosoft.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CHN_Fragments_toChange.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CHN_Fragments_toChange#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CHN_Fragments_toChange extends Fragment {
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
    TextView enetered, enter;
    private OnFragmentInteractionListener mListener;

    public CHN_Fragments_toChange() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CHN_Fragments_toChange.
     */
    // TODO: Rename and change types and number of parameters
    public static CHN_Fragments_toChange newInstance(String param1, String param2) {
        CHN_Fragments_toChange fragment = new CHN_Fragments_toChange();
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
        mContext = (ManagingTabsActivity) getActivity();










//
        viewMain = (View) inflater.inflate(R.layout.fragment_chn__fragments_to_change, container, false);

        enetered = (TextView)viewMain. findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);

        enter.setBackgroundColor(Color.parseColor("#FFFFFF"));//FFFFFF
        enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));

        CHNfragment fragment = new CHNfragment();
        mContext.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


        enetered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));//
                enter.setBackgroundColor(Color.parseColor("#FFFFFF"));

                GlobalClass.flagToSend=true;
                GlobalClass.flagToSendfromnavigation=false;

//                Updated_CHN_Entries fragment = new Updated_CHN_Entries();
//                mContext.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enetered.setBackgroundColor(Color.parseColor("#FFFFFF"));//
                enter.setBackgroundColor(Color.parseColor("#FFFFFF"));

                CHNfragment fragment = new CHNfragment();
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();




//                Old_Woe_Flow old_woe_flow = new Old_Woe_Flow();
//                mContext.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, old_woe_flow, old_woe_flow.getClass().getSimpleName()).addToBackStack(null).commit();


            }
        });

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
