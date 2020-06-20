package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Adapter.CampIntimation_Adapter;
import com.example.e5322.thyrosoft.Fragment.SecondFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Camp_Mis_Data_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camp_Mis_Data_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SecondFragment.OnFragmentInteractionListener mListener;
    Camp_Intimatgion_List_Model camp_intimatgion_list_model;
    ArrayList<Camp_Intimatgion_List_Model> campIntimatgionList;
    RecyclerView camp_intimation_recycle;
    CampIntimation_Adapter campIntimation_adapter;
    private View viewMain;

    public Camp_Mis_Data_fragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static Camp_Mis_Data_fragment newInstance(String param1, String param2) {
        Camp_Mis_Data_fragment fragment = new Camp_Mis_Data_fragment();
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
        viewMain = (View) inflater.inflate(R.layout.activity_camp__intimation, container, false);
        camp_intimation_recycle= (RecyclerView)viewMain.findViewById(R.id.camp_intimation_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        camp_intimation_recycle.setLayoutManager(linearLayoutManager);


        // String sr_no,camp_date,camp_profile,camp_rate,camp_days,camp_address,camp_duration;

        if(GlobalClass.global_camp_intimatgion_list_models_arrlst!=null && GlobalClass.global_camp_intimatgion_list_models_arrlst.size()!=0){
            campIntimation_adapter = new CampIntimation_Adapter(getActivity(), GlobalClass.global_camp_intimatgion_list_models_arrlst);
            camp_intimation_recycle.setAdapter(campIntimation_adapter);
        }else{
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }

        return viewMain;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
