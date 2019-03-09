package com.example.e5322.thyrosoft.RateCalculator;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Test_Rate_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Test_Rate_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<String> fastingOrNot1;
    ArrayList<String> setTestNames;
    ArrayList<Integer> getTestRate;
    TextView chargeRate, logisticRate, collectionCharge, companycost, afterCharge, afterlogistic, aftercollection, aftercompany;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bundle bundle;
    Button nxt;

    LinearLayout afterDiscountLayout;
    Button calculateDiscount,resetbutton;
    String B2BRate,B2CRate,CollCharge,toCompany,LogCost,resID,responseData,yours,discountedRate,fastingOrNot;
    private static ManagingTabsActivity mContext;
    View view;
    EditText discountEdit;
    ArrayList<Base_Model_Rate_Calculator> value;
    private OnFragmentInteractionListener mListener;
    TextView show_selected_tests_list_test_ils,TextView11;
    public Test_Rate_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Test_Rate_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Test_Rate_Fragment newInstance(String param1, String param2) {
        Test_Rate_Fragment fragment = new Test_Rate_Fragment();
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





        mContext = (ManagingTabsActivity) getActivity();
        final FragmentManager fragmentManager = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_test__rate_, container, false);
        show_selected_tests_list_test_ils = (TextView) view.findViewById(R.id.show_selected_tests_list_test_ils);
        discountEdit = (EditText) view.findViewById(R.id.discountEdit);
        calculateDiscount = (Button) view.findViewById(R.id.calculateDiscount);
        resetbutton = (Button) view.findViewById(R.id.resetbutton);
        chargeRate       = (TextView) view.findViewById(R.id.chargeRate);
        logisticRate     = (TextView) view.findViewById(R.id.logisticRate);
        collectionCharge = (TextView) view.findViewById(R.id.collectionCharge);
        companycost      = (TextView) view.findViewById(R.id.companycost);
        afterCharge      = (TextView) view.findViewById(R.id.afterCharge);
        afterlogistic    = (TextView) view.findViewById(R.id.afterlogistic);
        aftercollection  = (TextView) view.findViewById(R.id.aftercollection);
        aftercompany     = (TextView) view.findViewById(R.id.aftercompany);
        nxt     = (Button) view.findViewById(R.id.nxt);
        afterDiscountLayout     = (LinearLayout) view.findViewById(R.id.afterDiscountLayout);

        B2BRate = getArguments().getString("B2B");
        B2CRate = getArguments().getString("B2C");
        CollCharge = getArguments().getString("CHC");
        toCompany = getArguments().getString("COMPANY");
        LogCost = getArguments().getString("PI");
        resID = getArguments().getString("RESID");
        responseData = getArguments().getString("RESPONSE");
        yours = getArguments().getString("YOURS");
        value = getArguments().getParcelableArrayList("send");
        if(value.size()!=0){

//            Toast.makeText(mContext, ""+value.toString(), Toast.LENGTH_SHORT).show();
            setTestNames=new ArrayList<>();
            fastingOrNot1=new ArrayList<>();
            for (int i = 0; i <value.size() ; i++) {
                setTestNames.add(value.get(i).getName().toString());
                fastingOrNot1.add(value.get(i).getFasting().toString());
            }
        }



        final String displayslectedtest = TextUtils.join(" , ", setTestNames);
        show_selected_tests_list_test_ils.setText(displayslectedtest);
        chargeRate.setText(B2CRate);
        logisticRate.setText(LogCost);
        collectionCharge.setText(CollCharge);
        companycost.setText(toCompany);



//        send_estimation_fragment = new Send_Estimation_Fragment();
        calculateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterDiscountLayout.setVisibility(View.VISIBLE);
                int getDiscount = Integer.parseInt(discountEdit.getText().toString());
                if(getDiscount!=0){

                    int dicountCharge = Integer.parseInt(B2CRate);
                    int dicountLogistic = Integer.parseInt(LogCost);
                    int diffCharge = dicountCharge-getDiscount;
                    int diffLogisticAmt = dicountLogistic-getDiscount;
                    afterCharge.setText(String.valueOf(diffCharge));
                     discountedRate=String.valueOf(diffCharge);
                    afterlogistic.setText(String.valueOf(diffLogisticAmt));
                    aftercollection.setText(CollCharge);
                    aftercompany.setText(toCompany);
                }else{
                    afterDiscountLayout.setVisibility(View.VISIBLE);
                    chargeRate.setText(B2CRate);
                    logisticRate.setText(LogCost);
                    collectionCharge.setText(CollCharge);
                    companycost.setText(toCompany);
                }
            }
        });


        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                send_estimation_fragment = new Send_Estimation_Fragment();
//
//                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_mainLayout,send_estimation_fragment);
//                fragmentTransaction.commit();
//
//                Bundle args = new Bundle();
//                args.putString("selectedTest", displayslectedtest);
//                args.putString("B2C", B2CRate);
//                args.putString("B2CDiscountRate", discountedRate);
//                args.putString("fasting", "NON FASTING");
//
//                send_estimation_fragment.setArguments(args);


            }
        });








        // Inflate the layout for this fragment
        return view;
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
