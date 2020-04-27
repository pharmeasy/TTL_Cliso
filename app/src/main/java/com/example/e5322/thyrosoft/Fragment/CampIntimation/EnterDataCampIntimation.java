package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.e5322.thyrosoft.GlobalClass.global_camp_intimatgion_list_models_arrlst;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterDataCampIntimation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterDataCampIntimation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterDataCampIntimation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView txt_camp_date;
    EditText txt_camp_rate, txt_camp_days, txt_camp_address, txt_camp_duration, txt_reameks;
    Button btn_save;
    Camp_Intimatgion_List_Model camp_intimatgion_list_model;
    ArrayList<Camp_Intimatgion_List_Model> camp_intimatgion_list_models_arrlst;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final Calendar myCalendar = Calendar.getInstance();
    private OnFragmentInteractionListener mListener;

    public EnterDataCampIntimation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterDataCampIntimation.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterDataCampIntimation newInstance(String param1, String param2) {
        EnterDataCampIntimation fragment = new EnterDataCampIntimation();
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
        View view = inflater.inflate(R.layout.fragment_enter_data_camp_intimation, container, false);
        final String testName = getArguments().getString("testName");
        txt_camp_rate = (EditText) view.findViewById(R.id.txt_camp_rate);
        txt_camp_days = (EditText) view.findViewById(R.id.txt_camp_days);
        txt_camp_address = (EditText) view.findViewById(R.id.txt_camp_address);
        txt_camp_duration = (EditText) view.findViewById(R.id.txt_camp_duration);
        txt_reameks = (EditText) view.findViewById(R.id.txt_reameks);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        txt_camp_date = (TextView) view.findViewById(R.id.txt_camp_date);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        txt_camp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add validation

                if(validate()){
                    camp_intimatgion_list_model = new Camp_Intimatgion_List_Model();
                    camp_intimatgion_list_models_arrlst = new ArrayList<>();

                    camp_intimatgion_list_model.setCamp_address(txt_camp_address.getText().toString());
                    camp_intimatgion_list_model.setCamp_rate(txt_camp_rate.getText().toString());
                    camp_intimatgion_list_model.setSample_per_day(txt_camp_days.getText().toString());
                    camp_intimatgion_list_model.setCamp_date(txt_camp_date.getText().toString());
                    camp_intimatgion_list_model.setCamp_duration(txt_camp_duration.getText().toString());
                    camp_intimatgion_list_model.setCamp_remark(txt_reameks.getText().toString());
                    camp_intimatgion_list_model.setCamp_profile(testName);

                    camp_intimatgion_list_models_arrlst.add(camp_intimatgion_list_model);
                    global_camp_intimatgion_list_models_arrlst.add(camp_intimatgion_list_model);

                    CampSummary fragmentB=new CampSummary();
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("list",camp_intimatgion_list_models_arrlst);
                    fragmentB.setArguments(bundle);
                    replaceFragment(fragmentB);
                }


            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private boolean validate() {

        if(txt_camp_rate.getText().toString().length()==0){
            txt_camp_rate.setError("Camp rate is empty");
            return false;
        }

        else if(txt_camp_days .getText().toString().length()==0){
            txt_camp_days.setError("Camp days is empty");
            return false;
        }

        else if(txt_camp_address .getText().toString().length()==0){
            txt_camp_address.setError("Camp address is empty");
            return false;
        }

        else if(txt_camp_date .getText().toString().equals("")){
            txt_camp_address.setError("Kindly select the camp date");
            return false;
        }

        else if(txt_camp_duration.getText().toString().length()==0){
            txt_camp_duration.setError("Camp duration is empty");
            return false;
        }

        else
        return true;
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txt_camp_date.setText(sdf.format(myCalendar.getTime()));
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
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Begin Fragment transaction.
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }


}
