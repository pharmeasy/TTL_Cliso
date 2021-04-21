package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Edit_Woe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Edit_Woe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_Woe extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog barProgressDialog;
    TextView brand_name, selectType_txt, samplecollectionponit, referedby, sct_txt, sub_source_code, barcode_number, test_names_txt;
    EditText name_edt, age_edt;
    Spinner spinyr;
    RadioButton male, female;
    Button next_btn_patient;
    ArrayList<BarcodelistModel> barcodelists;
    BarcodelistModel barcodelist;
    public boolean genderId=false;
    private OnFragmentInteractionListener mListener;
    private RequestQueue POstQue;
    private String RES_ID;
    private String barcode_patient_id;
    private String message;
    private String status;
    SharedPreferences prefs;
    private String patientName, patientYearType, user, passwrd, access, api_key;
    private String saveGenderId;
    private String age_type;
    private String getName;
    private String getAge;
    private String getAgeType;
    int agesinteger;
    private Global globalClass;
    private int deliveryMode;
    private int Woe_mode;
    private int sr_no;
    private String TAG=Edit_Woe.class.getSimpleName().toString();

    public Edit_Woe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_Woe.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_Woe newInstance(String param1, String param2) {
        Edit_Woe fragment = new Edit_Woe();
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



        View viewWOE_edt = (View) inflater.inflate(R.layout.fragment_edit__woe, container, false);

        brand_name = (TextView) viewWOE_edt.findViewById(R.id.brand_name);
        selectType_txt = (TextView) viewWOE_edt.findViewById(R.id.selectType_txt);
        samplecollectionponit = (TextView) viewWOE_edt.findViewById(R.id.samplecollectionponit);
        referedby = (TextView) viewWOE_edt.findViewById(R.id.referedby);
        sct_txt = (TextView) viewWOE_edt.findViewById(R.id.sct_txt);
        barcode_number = (TextView) viewWOE_edt.findViewById(R.id.barcode_number);
        sub_source_code = (TextView) viewWOE_edt.findViewById(R.id.sub_source_code);
        test_names_txt = (TextView) viewWOE_edt.findViewById(R.id.test_names_txt);
        name_edt = (EditText) viewWOE_edt.findViewById(R.id.name_edt);
        age_edt = (EditText) viewWOE_edt.findViewById(R.id.age_edt);
        spinyr = (Spinner) viewWOE_edt.findViewById(R.id.spinyr);
        male = (RadioButton) viewWOE_edt.findViewById(R.id.male);
        female = (RadioButton) viewWOE_edt.findViewById(R.id.female);
        next_btn_patient = (Button) viewWOE_edt.findViewById(R.id.next_btn_patient);

        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Patientsagespinner,
                R.layout.spinner_item);
        spinyr.setAdapter(PatientsagespinnerAdapter);



        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        age_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    agesinteger = Integer.parseInt(s.toString());
                } else {

                }


                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(getContext(),
                            ToastFile.crt_age,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        age_edt.setText(enteredString.substring(1));
                    } else {
                        age_edt.setText("");
                    }
                }
                if (age_edt.getText().toString().equals("")) {

                } else {
                    if (agesinteger < 12) {
                        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Patientsagespinner,
                                R.layout.spinner_item);
                        spinyr.setAdapter(PatientsagespinnerAdapter);
                    }
                    if (agesinteger > 12) {
                        ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(getContext(), R.array.Patientspinyrday,
                                R.layout.spinner_item);
                        spinyr.setAdapter(Patientsagespinner);
                    }
                    if (agesinteger > 29) {
                        ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(getContext(), R.array.Patientspinyr,
                                R.layout.spinner_item);
                        spinyr.setAdapter(Patientsagesyr);
                    }
                }
            }
        });


        if (GlobalClass.summary_models != null) {
            brand_name.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBRAND());
            selectType_txt.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getTYPE());
            name_edt.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
            sub_source_code.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
            age_edt.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE());
            samplecollectionponit.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
            referedby.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
            sct_txt.setText(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());

            ArrayList<String> getOnlyBarcodes = new ArrayList<>();
            if (GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length != 0)
                for (int i = 0; i < GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                    getOnlyBarcodes.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getBARCODE());
                    String displayBarcodes = TextUtils.join(",", getOnlyBarcodes);
                    barcode_number.setText(displayBarcodes);
                }
            ArrayList<String> getOnlyTestNames = new ArrayList<>();
            if (GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length != 0)
                for (int i = 0; i < GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
                    getOnlyTestNames.add(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
                    String displayslectedtest = TextUtils.join(",", getOnlyTestNames);
                    test_names_txt.setText(displayslectedtest);
                }

            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("Y")) {
                spinyr.setSelection(0);
                age_type="Y";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("M")) {
                spinyr.setSelection(1);
                age_type="M";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE().equals("D")) {
                spinyr.setSelection(2);
                age_type="D";
            }
            if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("M")) {
                male.setChecked(true);
                female.setChecked(false);
                saveGenderId = "M";
            } else if (GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER().equals("F")) {
                female.setChecked(true);
                male.setChecked(false);
                saveGenderId = "F";
            }
        }

        name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(),
                            ToastFile.crt_name,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        name_edt.setText(enteredString.substring(1));
                    } else {
                        name_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "M";
                } else if (genderId == true) {
                    genderId = false;
                    saveGenderId = "M";
                }

            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "F";
                } else if (genderId == true) {
                    genderId = false;
                    saveGenderId = "F";
                }

            }
        });
        next_btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getName=name_edt.getText().toString();
                 getAge = age_edt.getText().toString();
                 getAgeType= spinyr.getSelectedItem().toString();
                if(getName.equals("") && getName.equals(null)){
                    Toast.makeText(getContext(), ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                }else if (getAge.equals("") && getAge.equals(null)){
                    Toast.makeText(getContext(), ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                }else if(getAgeType.equals("") && getAgeType.equals(null)){
                    Toast.makeText(getContext(), ToastFile.ent_age_type, Toast.LENGTH_SHORT).show();
                }else if(saveGenderId.equals("") && saveGenderId.equals(null)){
                    Toast.makeText(getContext(), ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                }else {
                    if(getAgeType.equals("Years")){
                        age_type="Y";
                    }else if(getAgeType.equals("Months")){
                        age_type="M";
                    }else if(getAgeType.equals("Days")){
                        age_type="D";
                    }
                    doWOE_edit();
                }


            }
        });
        // Inflate the layout for this fragment
        return viewWOE_edt;
    }

    private void doWOE_edit() {

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        GlobalClass.summary_models.get(0).getWoeditlist().getWoe().setPATIENT_NAME(getName);
        GlobalClass.summary_models.get(0).getWoeditlist().getWoe().setAGE(getAge);
        GlobalClass.summary_models.get(0).getWoeditlist().getWoe().setAGE_TYPE(age_type);
        GlobalClass.summary_models.get(0).getWoeditlist().getWoe().setGENDER(saveGenderId);
        String brand_type_to_send=brand_name.getText().toString();
        String type_to_send=selectType_txt.getText().toString();



        POstQue = GlobalClass.setVolleyReq(getContext());
        MyPojoWoe myPojoWoe = new MyPojoWoe();

        if(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getDELIVERY_MODE()!=null){
            deliveryMode= Integer.valueOf(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getDELIVERY_MODE());
        }else{
            Woe_mode=Integer.parseInt(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getWO_STAGE());
        }if(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getWO_MODE()!=null){
            sr_no=Integer.parseInt(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSR_NO());
        }

        Woe woe = new Woe();
        woe.setAADHAR_NO(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAADHAR_NO());
        woe.setADDRESS(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getADDRESS());
        woe.setAGE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE());
        woe.setAGE_TYPE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAGE_TYPE());
        woe.setALERT_MESSAGE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getALERT_MESSAGE());
        woe.setAMOUNT_COLLECTED(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_COLLECTED());
        woe.setAMOUNT_DUE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAMOUNT_DUE());
        woe.setAPP_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getAPP_ID());
        woe.setBCT_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getBCT_ID());
        woe.setBRAND(brand_type_to_send);
        woe.setCAMP_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getCAMP_ID());
        woe.setCONT_PERSON(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getCONT_PERSON());
        woe.setCONTACT_NO(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getCONTACT_NO());
        woe.setCUSTOMER_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getCUSTOMER_ID());
        woe.setDELIVERY_MODE(deliveryMode);

        woe.setEMAIL_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getEMAIL_ID());
        woe.setENTERED_BY(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getENTERED_BY());
        woe.setGENDER(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getGENDER());
        woe.setLAB_ADDRESS(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ADDRESS());
        woe.setLAB_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_ID());
        woe.setLAB_NAME(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLAB_NAME());
        woe.setLEAD_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getLEAD_ID());
        woe.setMAIN_SOURCE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getMAIN_SOURCE());
        woe.setORDER_NO(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getORDER_NO());
        woe.setOS(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getOS());
        woe.setPATIENT_NAME(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPATIENT_NAME());
        woe.setPINCODE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPINCODE());
        woe.setPRODUCT(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getPRODUCT());
        woe.setPurpose("mobile application");
        woe.setREF_DR_ID(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_ID());
        woe.setREF_DR_NAME(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getREF_DR_NAME());
        woe.setREMARKS("MOBILE");
        woe.setSPECIMEN_COLLECTION_TIME(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_COLLECTION_TIME());
        woe.setSPECIMEN_SOURCE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSPECIMEN_SOURCE());
        woe.setSR_NO(sr_no);
        woe.setSTATUS(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSTATUS());
        woe.setSUB_SOURCE_CODE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getSUB_SOURCE_CODE());
        woe.setTOTAL_AMOUNT(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getTOTAL_AMOUNT());
        woe.setTYPE(type_to_send);
        woe.setWATER_SOURCE(GlobalClass.summary_models.get(0).getWoeditlist().getWoe().getWATER_SOURCE());
        woe.setWO_MODE("CLISO APP");
        woe.setWO_STAGE(Woe_mode);
        myPojoWoe.setWoe(woe);

        barcodelists = new ArrayList<>();

        for (int i = 0; i < GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist().length; i++) {
            barcodelist = new BarcodelistModel();
            barcodelist.setSAMPLE_TYPE(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getSAMPLE_TYPE());
            barcodelist.setBARCODE(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getBARCODE());
            barcodelist.setTESTS(GlobalClass.summary_models.get(0).getWoeditlist().getBarcodelist()[i].getTESTS());
            barcodelists.add(barcodelist);
        }

        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WO_EDIT");
        myPojoWoe.setApi_key(api_key);//api_key


        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.e(TAG, "onResponse: "+response );
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                    message = parentObjectOtp.getString("message");
                    status = parentObjectOtp.getString("status");

                    if (status.equalsIgnoreCase("SUCCESS")) {

                        Woe_fragment fragment = new Woe_fragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {

                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                        TastyToast.makeText(getContext(), message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);


                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                getContext()).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Update Ledger !");

                        // Setting Dialog Message
                        alertDialog.setMessage(ToastFile.update_ledger);
                        // Setting OK Button
                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent(getActivity(),Payment_Activity.class);
                                i.putExtra("COMEFROM", "Edit_Woe");
                                startActivity(i);
                              /*  Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                startActivity(httpIntent);*/
                                // Write your code here to execute after dialog closed
                            }
                        });
                        alertDialog.show();

                    } else {
                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                        TastyToast.makeText(getContext(), message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    System.out.println(error);
                }
            }
        });
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        POstQue.add(jsonObjectRequest1);
        Log.e(TAG, "doWOE_edit: url"+jsonObjectRequest1 );
        Log.e(TAG, "doWOE_edit: json"+jsonObj );
        Log.e(TAG, "doWOE_edit: json"+json );

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
