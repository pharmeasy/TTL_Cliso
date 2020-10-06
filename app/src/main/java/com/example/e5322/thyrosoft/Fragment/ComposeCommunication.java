package com.example.e5322.thyrosoft.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComposeCommunication.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComposeCommunication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeCommunication extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnercomm;
    EditText commuTXT;
    public static RequestQueue PostQue;
    private SimpleDateFormat sdf;
    String Date = "";
    ProgressDialog barProgressDialog;

    private OnFragmentInteractionListener mListener;
    Button sendcomm;
    private String TAG = getClass().getSimpleName();

    public ComposeCommunication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeCommunication.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeCommunication newInstance(String param1, String param2) {
        ComposeCommunication fragment = new ComposeCommunication();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_compose_communication, container, false);
        spinnercomm = (Spinner) view.findViewById(R.id.spinnercomm);
        sendcomm = (Button) view.findViewById(R.id.sendcomm);
        commuTXT = (EditText) view.findViewById(R.id.commuTXT);
        // TextView dateview = getActivity().findViewById(R.id.show_date);
        //  dateview.setVisibility(View.GONE);
        String[] string = new String[GlobalClass.commSpinner.size()];
        for (int i = 0; i < GlobalClass.commSpinner.size(); i++) {
            string[i] = GlobalClass.commSpinner.get(i).getDisplayName();
        }
        Log.e(TAG, "");
        commuTXT.setFilters(new InputFilter[]{EMOJI_FILTER});

        commuTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8") || enteredString.startsWith("9")) {
                    TastyToast.makeText(getActivity(), ToastFile.crt_txt, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        commuTXT.setText(enteredString.substring(1));
                    } else {
                        commuTXT.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date = sdf.format(cl.getTime());
      /*  DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = inputFormat.parse(Date);
        String outputDateStr = outputFormat.format(date);
*/
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, string);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercomm.setAdapter(aa);


        sendcomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String getCommunication = commuTXT.getText().toString();
                if (getCommunication.equals("")) {
                    TastyToast.makeText(getContext(), "Compose your message", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {

                    barProgressDialog = new ProgressDialog(getContext());
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    PostData();
                }

                commuTXT.setText("");
                spinnercomm.setSelection(0);
            }
        });


        return view;
    }

    private void PostData() {

        PostQue = GlobalClass.setVolleyReq(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            String spinnerItem = spinnercomm.getSelectedItem().toString();

            if (spinnercomm.getSelectedItem().equals("WOE & REPORTS")) {
                spinnerItem = "WOE-REPORTS";
            } else {
                spinnerItem = spinnercomm.getSelectedItem().toString();
            }

            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            jsonObject.put("apiKey", api_key);
            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put("type", "Request");
            jsonObject.put("communication", commuTXT.getText());
            jsonObject.put("commId", "");
            jsonObject.put("forwardTo", spinnerItem);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.commPost, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            TastyToast.makeText(getContext(), response.optString(Constants.response).toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                    Log.v(TAG,"error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);
        Log.e(TAG, "PostData: URL" + jsonObjectRequest);
        Log.e(TAG, "PostData: json" + jsonObject);

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
