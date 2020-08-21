package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.CampIntimation_Adapter;
import com.example.e5322.thyrosoft.Fragment.SecondFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

       initView();


        return viewMain;
    }

    private void initView() {
        camp_intimation_recycle= (RecyclerView)viewMain.findViewById(R.id.camp_intimation_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        camp_intimation_recycle.setLayoutManager(linearLayoutManager);


        if(GlobalClass.global_camp_intimatgion_list_models_arrlst!=null && GlobalClass.global_camp_intimatgion_list_models_arrlst.size()!=0){
            campIntimation_adapter = new CampIntimation_Adapter(getActivity(), GlobalClass.global_camp_intimatgion_list_models_arrlst);
            camp_intimation_recycle.setAdapter(campIntimation_adapter);
        }else{
            GlobalClass.showTastyToast(getActivity(), MessageConstants.NODATA, 2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
