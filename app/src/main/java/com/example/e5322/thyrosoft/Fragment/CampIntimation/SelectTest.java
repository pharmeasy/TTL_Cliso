package com.example.e5322.thyrosoft.Fragment.CampIntimation;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Activity.CampIntimation;
import com.example.e5322.thyrosoft.Adapter.Camp_Test_LIst;
import com.example.e5322.thyrosoft.Interface.SendTestListfromCampList;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.RateCalculatorForModels.GetMainModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.RateCalB2B_MASTERS_Main_Model;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectTest.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTest extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SharedPreferences prefs;
    ArrayList<Base_Model_Rate_Calculator> finalproductlist;
    String user, passwrd, access, api_key;
    ArrayList<RateCalB2B_MASTERS_Main_Model> b2bmasterarraylistRate;
    ArrayList<Base_Model_Rate_Calculator> getAllTests, filteredFilesttl;
    CampIntimation mContext;
    List<String> showTestNmaesRateCal;
    private ArrayList<Base_Model_Rate_Calculator> totalproductlist;
    GetMainModel obj;
    Button btn_clear, btn_save;
    TextView show_selected_tests_list_test_ils1;
    private RecyclerView recycler_all_test;
    Camp_Test_LIst camp_test_lIst;
    ArrayList<Base_Model_Rate_Calculator> selectedTestsListRateCal = new ArrayList<>();
    private String displayslectedtestToShow="";
    EditText sv_testsList1;

    public SelectTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTest.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTest newInstance(String param1, String param2) {
        SelectTest fragment = new SelectTest();
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
        View view = inflater.inflate(R.layout.fragment_select_test, container, false);

        mContext = (CampIntimation) getActivity();
        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        recycler_all_test = (RecyclerView) view.findViewById(R.id.recycler_all_test);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_clear = (Button) view.findViewById(R.id.btn_clear);
        sv_testsList1 = (EditText) view.findViewById(R.id.sv_testsList1);
        show_selected_tests_list_test_ils1 = (TextView) view.findViewById(R.id.show_selected_tests_list_test_ils1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_all_test.setLayoutManager(linearLayoutManager);

        sv_testsList1.setFilters(new InputFilter[]{EMOJI_FILTER});

        if(displayslectedtestToShow.length()>0)
        show_selected_tests_list_test_ils1.setText(displayslectedtestToShow);

        sv_testsList1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(),
                            ToastFile.ent_feedback,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        sv_testsList1.setText(enteredString.substring(1));
                    } else {
                        sv_testsList1.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().toLowerCase();
                filteredFilesttl = new ArrayList<Base_Model_Rate_Calculator>();
                String name = "";
                String code = "";
                String product = "";

                if (finalproductlist != null && finalproductlist.size() != 0) {
                    for (int i = 0; i < finalproductlist.size(); i++) {
                        final String text = finalproductlist.get(i).getName().toLowerCase();
                        if (finalproductlist.get(i).getName() != null || !finalproductlist.get(i).getName().equals("")) {
                            name = finalproductlist.get(i).getName().toLowerCase();
                        }
                        if (finalproductlist.get(i).getCode() != null || !finalproductlist.get(i).getCode().equals("")) {
                            code = finalproductlist.get(i).getCode().toLowerCase();
                        }
                        if (finalproductlist.get(i).getProduct() != null || !finalproductlist.get(i).getProduct().equals("")) {
                            product = finalproductlist.get(i).getProduct().toLowerCase();
                        }
                        if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                (code != null && code.contains(s1)) ||
                                (product != null && product.contains(s1))) {
                            String testname = finalproductlist.get(i).getName();
                            filteredFilesttl.add(finalproductlist.get(i));
                        } else {

                        }
                        callAdapaterTosetData(filteredFilesttl, totalproductlist);
                    }
                }
            }
        });



        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTest c1 = new SelectTest();
                replaceFragment(c1);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (show_selected_tests_list_test_ils1.getText().toString().length()==0) {
                    TastyToast.makeText(getActivity(), "Please select a test !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                } else {
                    EnterDataCampIntimation fragmentB = new EnterDataCampIntimation();
                    Bundle bundle = new Bundle();
                    bundle.putString("testName", displayslectedtestToShow);
                    fragmentB.setArguments(bundle);
                    replaceFragment(fragmentB);
                }
            }
        });
        getDataFromSharedPref();

        // Inflate the layout for this fragment
        return view;
    }

    private void getDataFromSharedPref() {
        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        Gson gsondtaa = new Gson();
        String jsondata = appSharedPrefsdata.getString("MyObject", "");
        obj = gsondtaa.fromJson(jsondata, GetMainModel.class);


        if (obj != null) {
            if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                b2bmasterarraylistRate = new ArrayList<>();
                b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                getAllTests = new ArrayList<>();
                totalproductlist = new ArrayList<>();

                finalproductlist = new ArrayList<Base_Model_Rate_Calculator>();
                for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                    for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
                        finalproductlist.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                        b2bmasterarraylistRate.get(i).getPOP().get(j).setIsCart("no");
                        b2bmasterarraylistRate.get(i).getPOP().get(j).setIs_lock("no");

                        getAllTests.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                    }
                    for (int j = 0; j < b2bmasterarraylistRate.get(i).getPROFILE().size(); j++) {
                        b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIsCart("no");
                        b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIs_lock("no");

                        getAllTests.add(b2bmasterarraylistRate.get(i).getPROFILE().get(j));
                    }

                }
                totalproductlist = getAllTests;

                callAdapaterTosetData(finalproductlist, totalproductlist);


            } else {
                //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void callAdapaterTosetData(ArrayList<Base_Model_Rate_Calculator> finalproductlist, ArrayList<Base_Model_Rate_Calculator> totalproductlist) {
        camp_test_lIst = new Camp_Test_LIst((CampIntimation) getActivity(), finalproductlist, totalproductlist, selectedTestsListRateCal, new SendTestListfromCampList() {

            @Override
            public void onClisktheTest(ArrayList<Base_Model_Rate_Calculator> selectedTests) {
                System.out.println("check changed");
                selectedTestsListRateCal = selectedTests;
                camp_test_lIst.notifyDataSetChanged();
                if (selectedTestsListRateCal.size() != 0) {
                    showTestNmaesRateCal = new ArrayList<>();

                    for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                        showTestNmaesRateCal.add(selectedTestsListRateCal.get(i).getName().toString());
                    }
                    displayslectedtestToShow = TextUtils.join(", ", showTestNmaesRateCal);
                    show_selected_tests_list_test_ils1.setText(displayslectedtestToShow);
                } else if (selectedTestsListRateCal.size() == 0) {
                    show_selected_tests_list_test_ils1.setText("");
                }


            }
        });
        recycler_all_test.setAdapter(camp_test_lIst);
        camp_test_lIst.notifyDataSetChanged();
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

    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

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
