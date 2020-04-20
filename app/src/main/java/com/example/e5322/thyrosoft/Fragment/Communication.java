//package com.example.e5322.thyrosoft.Fragment;
//
//import android.app.ProgressDialog;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ExpandableListView;
//import android.widget.TextView;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.e5322.thyrosoft.API.Api;
//import com.example.e5322.thyrosoft.API.Constants;
//import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
//import com.example.e5322.thyrosoft.Adapter.ExpandableListCommunication;
//import com.example.e5322.thyrosoft.GlobalClass;
//import com.example.e5322.thyrosoft.Models.CommInbox_Model;
//import com.example.e5322.thyrosoft.Models.CommToCpl_Model;
//import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationRepsponseModel;
//import com.example.e5322.thyrosoft.Models.commMaster_Model;
//import com.example.e5322.thyrosoft.R;
//import com.example.e5322.thyrosoft.ToastFile;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import static android.content.Context.MODE_PRIVATE;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Communication.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link Communication#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Communication extends RootFragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    TextView FromCPL, ToCPL;
//    public static RequestQueue PostQue;
//    HashMap<ArrayList, ArrayList<CommInbox_Model>> listDataChild;
//    HashMap<ArrayList, ArrayList<CommToCpl_Model>> listToDataChild;
//    ExpandableListCommunication adapter;
//    ExpandableListView expandlistcommunication;
//    private OnFragmentInteractionListener mListener;
//    ProgressDialog barProgressDialog;
//    CommunicationRepsponseModel communicationRepsponseModel;
//    private String TAG= ManagingTabsActivity.class.getSimpleName().toString();
//
//    public Communication() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Communication.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Communication newInstance(String param1, String param2) {
//        Communication fragment = new Communication();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_communication, container, false);
//        FromCPL = (TextView) view.findViewById(R.id.FromCPL);
//        ToCPL = (TextView) view.findViewById(R.id.ToCPL);
//
//        expandlistcommunication = (ExpandableListView) view.findViewById(R.id.expandlistcommunication);
//        FromCPL.setBackgroundColor(getResources().getColor(R.color.orange));
//        FromCPL.setTextColor(getResources().getColor(R.color.colorWhite));
//        ToCPL.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//        ToCPL.setTextColor(getResources().getColor(R.color.colorBlack));
//        // TextView dateview = getActivity().findViewById(R.id.show_date);
//        //  dateview.setVisibility(View.GONE);
//        setHasOptionsMenu(true);
//
//        FromCPL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GlobalClass.FromCPLInt = 0;
//                GlobalClass.commSpinner = new ArrayList<commMaster_Model>();
//                ArrayList<CommInbox_Model> commFromCPL = new ArrayList<CommInbox_Model>();
//                ArrayList<CommToCpl_Model> commToCPL = new ArrayList<CommToCpl_Model>();
//                ArrayList<String> headerQuestionFRom = new ArrayList<String>();
//                ArrayList<String> headerQuestionTo = new ArrayList<String>();
//
//                adapter = new ExpandableListCommunication(getActivity(), headerQuestionFRom, commFromCPL, commToCPL, headerQuestionTo, listDataChild, listToDataChild);
//                expandlistcommunication.setAdapter(adapter);
//
//                FromCPL.setBackgroundColor(getResources().getColor(R.color.orange));
//                FromCPL.setTextColor(getResources().getColor(R.color.colorWhite));
//                ToCPL.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                ToCPL.setTextColor(getResources().getColor(R.color.colorBlack));
//                Getdata();
//
//
//            }
//
//        });
//
//        ToCPL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                GlobalClass.toCPLflag=true;
//
//                GlobalClass.commSpinner = new ArrayList<commMaster_Model>();
//                ArrayList<CommInbox_Model> commFromCPL = new ArrayList<CommInbox_Model>();
//                ArrayList<CommToCpl_Model> commToCPL = new ArrayList<CommToCpl_Model>();
//
//
//                ArrayList<String> headerQuestionFRom = new ArrayList<String>();
//                ArrayList<String> headerQuestionTo = new ArrayList<String>();
//
//                adapter = new ExpandableListCommunication(getActivity(), headerQuestionFRom, commFromCPL, commToCPL, headerQuestionTo, listDataChild, listToDataChild);
//                expandlistcommunication.setAdapter(adapter);
//
//                GlobalClass.FromCPLInt = 1;
//                FromCPL.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                FromCPL.setTextColor(getResources().getColor(R.color.colorBlack));
//
//                ToCPL.setBackgroundColor(getResources().getColor(R.color.orange));
//                ToCPL.setTextColor(getResources().getColor(R.color.colorWhite));
//                Getdata();
//
//            }
//        });
//        //  PrepareDataList();
//
//        Getdata();
//
//
//        return view;
//    }
//
//    private void Getdata() {
//
//        barProgressDialog = new ProgressDialog(getContext());
//        barProgressDialog.setTitle("Kindly wait ...");
//        barProgressDialog.setMessage(ToastFile.processing_request);
//        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
//        barProgressDialog.setProgress(0);
//        barProgressDialog.setMax(20);
//        barProgressDialog.show();
//        barProgressDialog.setCanceledOnTouchOutside(false);
//        barProgressDialog.setCancelable(false);
//        PostQue = GlobalClass.setVolleyReq(getContext());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//
//            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
//            String user = prefs.getString("Username", null);
//            String passwrd = prefs.getString("password", null);
//            String access = prefs.getString("ACCESS_TYPE", null);
//            String api_key = prefs.getString("API_KEY", null);
//
//            jsonObject.put("apiKey", api_key);
//            jsonObject.put(Constants.UserCode_billing, user);
//            jsonObject.put("type", "");
//            jsonObject.put("communication", "");
//            jsonObject.put("commId", "");
//            jsonObject.put("forwardTo", "");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.commGetLive, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.e(TAG, "onResponse: RESPONSE"+response );
//                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
//
//                            Gson gson = new Gson();
//                            communicationRepsponseModel = new CommunicationRepsponseModel();
//                            communicationRepsponseModel = gson.fromJson(response.toString(), CommunicationRepsponseModel.class);
//
//
//                            if (communicationRepsponseModel.getCommunicationMaster() != null) {
//                                GlobalClass.commSpinner = new ArrayList<commMaster_Model>();
//                                ArrayList<String> headerQuestionFRom = new ArrayList<String>();
//                                ArrayList<String> headerQuestionTo = new ArrayList<String>();
//                                for (int j = 0; j < communicationRepsponseModel.getCommunicationMaster().length; j++) {
//                                    try {
//
//                                        commMaster_Model commSpinnerMODEL = new commMaster_Model();
//                                        commSpinnerMODEL.setDisplayName(communicationRepsponseModel.getCommunicationMaster()[j].getDisplayName());
//                                        commSpinnerMODEL.setForwardTo(communicationRepsponseModel.getCommunicationMaster()[j].getForwardTo());
//                                        commSpinnerMODEL.setId(communicationRepsponseModel.getCommunicationMaster()[j].getId());
//                                        commSpinnerMODEL.setIsBarcode(communicationRepsponseModel.getCommunicationMaster()[j].getIsBarcode());
//                                        GlobalClass.commSpinner.add(commSpinnerMODEL);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                if(communicationRepsponseModel.getInboxes()!= null){
//                                    GlobalClass.commFromCPL=new ArrayList<>();
//                                    for (int i = 0; i < communicationRepsponseModel.getInboxes().length; i++) {
//                                        try {
//                                            CommInbox_Model commFromCPLmod = new CommInbox_Model();
//                                            commFromCPLmod.setTAT(communicationRepsponseModel.getInboxes()[i].getTAT());
//                                            commFromCPLmod.setCommBy(communicationRepsponseModel.getInboxes()[i].getCommBy());
//                                            commFromCPLmod.setCommDate(communicationRepsponseModel.getInboxes()[i].getCommDate());
//                                            commFromCPLmod.setCommId(communicationRepsponseModel.getInboxes()[i].getCommId());
//                                            commFromCPLmod.setDepartment(communicationRepsponseModel.getInboxes()[i].getDepartment());
//                                            commFromCPLmod.setQuestion(communicationRepsponseModel.getInboxes()[i].getQuestion());
//                                            commFromCPLmod.setResDate(communicationRepsponseModel.getInboxes()[i].getResDate());
//                                            commFromCPLmod.setResponse(communicationRepsponseModel.getInboxes()[i].getResponse());
//
//                                            GlobalClass.commFromCPL.add(commFromCPLmod);
//                                            headerQuestionFRom.add(communicationRepsponseModel.getInboxes()[i].getQuestion());
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//
//
//                                if(communicationRepsponseModel.getSents()!=null){
//                                    GlobalClass.commToCPL=new ArrayList<>();
//                                    for (int k = 0; k < communicationRepsponseModel.getSents().length; k++) {
//                                        try {
//                                            CommToCpl_Model commTOCPLmod = new CommToCpl_Model();
//                                            commTOCPLmod.setTAT(communicationRepsponseModel.getSents()[k].getTAT());
//                                            commTOCPLmod.setCommunication(communicationRepsponseModel.getSents()[k].getCommunication());
//                                            commTOCPLmod.setCommDate(communicationRepsponseModel.getSents()[k].getCommDate());
//                                            commTOCPLmod.setCommId(communicationRepsponseModel.getSents()[k].getCommId());
//                                            commTOCPLmod.setResponseBy(communicationRepsponseModel.getSents()[k].getResponseBy());
//                                            commTOCPLmod.setStatus(communicationRepsponseModel.getSents()[k].getStatus());
//                                            commTOCPLmod.setResponse(communicationRepsponseModel.getSents()[k].getResponse());
//
//                                            headerQuestionTo.add(communicationRepsponseModel.getSents()[k].getCommunication());
//                                            GlobalClass.commToCPL.add(commTOCPLmod);
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                                listDataChild = new HashMap<ArrayList, ArrayList<CommInbox_Model>>();
//                                listDataChild.put(headerQuestionFRom, GlobalClass.commFromCPL);
//                                listToDataChild = new HashMap<ArrayList, ArrayList<CommToCpl_Model>>();
//                                listToDataChild.put(headerQuestionTo, GlobalClass.commToCPL);
//                                adapter = new ExpandableListCommunication(getActivity(), headerQuestionFRom, GlobalClass.commFromCPL, GlobalClass.commToCPL, headerQuestionTo, listDataChild, listToDataChild);
//                                expandlistcommunication.setAdapter(adapter);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                try {
//                    System.out.println("error ala parat " + error);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        queue.add(jsonObjectRequest);
//        Log.e(TAG, "Getdata: URL"+ jsonObjectRequest);
//        Log.e(TAG, "Getdata: json"+jsonObject );
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.comm_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.addcommu:
//                ComposeCommunication filt = new ComposeCommunication();
//                android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_mainLayout, filt);
//                fragmentTransaction.commit();
//                return true;
//
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}
