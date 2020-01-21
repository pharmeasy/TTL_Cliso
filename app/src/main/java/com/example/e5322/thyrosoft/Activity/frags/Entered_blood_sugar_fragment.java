package com.example.e5322.thyrosoft.Activity.frags;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Adapter.CampIntimation_Adapter;
import com.example.e5322.thyrosoft.Adapter.Entered_blood_sugar_adapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Entered_blood_sugar_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Entered_blood_sugar_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Entered_blood_sugar_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View viewMain;
    final Calendar myCalendar = Calendar.getInstance();
    LinearLayout bs_calendar_ll;
    TextView txt_bs_cal;
    private RecyclerView entered_recycler_view;

    public Entered_blood_sugar_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Entered_blood_sugar_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Entered_blood_sugar_fragment newInstance(String param1, String param2) {
        Entered_blood_sugar_fragment fragment = new Entered_blood_sugar_fragment();
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
        viewMain = (View) inflater.inflate(R.layout.fragment_entered_blood_sugar_fragment, container, false);
        entered_recycler_view= (RecyclerView)viewMain.findViewById(R.id.entered_recycler_view);
        bs_calendar_ll= (LinearLayout) viewMain.findViewById(R.id.bs_calendar_ll);
        txt_bs_cal= (TextView) viewMain.findViewById(R.id.txt_bs_cal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        entered_recycler_view.setLayoutManager(linearLayoutManager);

        setAdaptor();

        Date d = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1= new SimpleDateFormat("dd-MM-yyyy");
        String showTotxt= sdf1.format(d);
        txt_bs_cal.setText(showTotxt);

        bs_calendar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return viewMain;
    }

    private void setAdaptor() {
        Entered_blood_sugar_adapter adp_entredbs = new Entered_blood_sugar_adapter();
        entered_recycler_view.setAdapter(adp_entredbs);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        txt_bs_cal.setText(sdf.format(myCalendar.getTime()));
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
