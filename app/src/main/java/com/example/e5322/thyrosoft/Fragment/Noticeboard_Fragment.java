package com.example.e5322.thyrosoft.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    View viewfab, view, viewresultfrag, viewnoticeboard;
    RecyclerView noticeboard_list;
    String msgCode;
    private OnFragmentInteractionListener mListener;
    RequestQueue requestQueueNoticeBoard;
    AlertDialog alert;
    NoticeBoard_Model noticeBoard_model;
    SharedPreferences prefs;
    LinearLayoutManager linearLayoutManager;
    LinearLayout lin_cmsoon;
    String user, passwrd, access, CLIENT_TYPE, api_key;
    private String TAG = Noticeboard_Fragment.class.getSimpleName().toString();

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

        viewnoticeboard = (View) inflater.inflate(R.layout.fragment_noticeboard_, container, false);

        initViews();

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);
        api_key = prefs.getString("API_KEY", null);




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

        return viewnoticeboard;
    }

    private void initViews() {
        noticeboard_list = (RecyclerView) viewnoticeboard.findViewById(R.id.noticeboard_list);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        noticeboard_list.setLayoutManager(linearLayoutManager);
        lin_cmsoon = view.findViewById(R.id.lin_cmsoon);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
