package com.example.e5322.thyrosoft.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.NoticeBoard_Adapter;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Noticeboard_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Noticeboard_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Noticeboard_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static ManagingTabsActivity mContext;
    View viewfab,view,viewresultfrag,viewnoticeboard;
    RecyclerView noticeboard_list;
    String msgCode;
    private OnFragmentInteractionListener mListener;
    RequestQueue requestQueueNoticeBoard;
    ProgressDialog barProgressDialog;
    AlertDialog alert;
    NoticeBoard_Model noticeBoard_model;
    SharedPreferences prefs;
    LinearLayoutManager linearLayoutManager;
    String user, passwrd, access, api_key;
    private String TAG=Noticeboard_Fragment.class.getSimpleName().toString();

    public Noticeboard_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Noticeboard_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Noticeboard_Fragment newInstance(String param1, String param2) {
        Noticeboard_Fragment fragment = new Noticeboard_Fragment();
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


       // View view3 = getActivity().findViewById(R.id.show_date);
        //view3.setVisibility(View.GONE);












        viewnoticeboard = (View) inflater.inflate(R.layout.fragment_noticeboard_, container, false);
        noticeboard_list= (RecyclerView) viewnoticeboard.findViewById(R.id.noticeboard_list);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        noticeboard_list.setLayoutManager(linearLayoutManager);
        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        getNoticeBoardData();

        barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


//        proid = "";

        if (!isNetworkAvailable()) {
            // Create an Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            // Set the Alert Dialog Message
            builder.setMessage(ToastFile.intConnection)
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Restart the Activity
                                    /*Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);*/
                                }
                            });
            alert = builder.create();
            alert.show();
        }


        // Inflate the layout for this fragment
        return viewnoticeboard;
    }

    private void getNoticeBoardData() {
        requestQueueNoticeBoard = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.NoticeBoardData+""+api_key+"/getNoticeMessages", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
                Gson gson = new Gson();
                noticeBoard_model = new NoticeBoard_Model();
                noticeBoard_model=gson.fromJson(response.toString(), NoticeBoard_Model.class);

                ArrayList<NoticeBoard_Model> array_notice = new ArrayList<>();


                if(noticeBoard_model.getMessages()!=null){
                    array_notice.add(noticeBoard_model);
                    if(array_notice.get(0).getMessages()[0].getMessageCode()!=null){
                        msgCode = (array_notice.get(0).getMessages()[0].getMessageCode());
                        NoticeBoard_Adapter noticeBoard_adapter = new NoticeBoard_Adapter(getContext(),array_notice,msgCode);
                        noticeboard_list.setAdapter(noticeBoard_adapter);
                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                }
                   else{
                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                        Toast.makeText(getActivity(), ""+noticeBoard_model.getResponse().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(getContext(),"Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });
        jsonObjectRequestProfile.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueNoticeBoard.add(jsonObjectRequestProfile);
        Log.e(TAG, "getNoticeBoardData: url"+jsonObjectRequestProfile );
    }

    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
