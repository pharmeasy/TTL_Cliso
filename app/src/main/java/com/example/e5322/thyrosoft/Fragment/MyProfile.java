package com.example.e5322.thyrosoft.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView nametxt, dojtxt, source_codetxt, closing_bal, credit_lim;
    Button view_aadhar, pref;
    View view;
    ProgressDialog barProgressDialog;
    ImageView aadhar;
    ImageView profimg;
    String prof, aadharimg;
    public static RequestQueue PostQue;
    private OnFragmentInteractionListener mListener;
    String aadhar_no = "";
    String URL = "";
    Bitmap decodedByte;
    String TAG= MyProfile.class.getSimpleName().toString();
    private SharedPreferences getshared;
    private String user;
    private String passwrd;
    private String api_key;
    private String access;

    public MyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfile newInstance(String param1, String param2) {
        MyProfile fragment = new MyProfile();
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
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        nametxt = (TextView) view.findViewById(R.id.name);
        dojtxt = (TextView) view.findViewById(R.id.doj);
        source_codetxt = (TextView) view.findViewById(R.id.source_code);
        closing_bal = (TextView) view.findViewById(R.id.closing_bal);
        credit_lim = (TextView) view.findViewById(R.id.credit_lim);
        /*view_aadhar = (Button) view.findViewById(R.id.view_aadhar);
        pref = (Button) view.findViewById(R.id.pref);*/
        profimg = (ImageView) view.findViewById(R.id.profimg);
        //  TextView dateview = getActivity().findViewById(R.id.show_date);
        //dateview.setVisibility(View.GONE);
        SharedPreferences getshared = getActivity().getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);


            prof = getshared.getString("prof", null);
        if (prof != null) {
            String ac_code = getshared.getString("ac_code", null);
            String address = getshared.getString("address", null);
            String email = getshared.getString("email", null);
            String mobile = getshared.getString("mobile", null);
            String name = getshared.getString("name", null);
            String pincode = getshared.getString("pincode", null);
            String user_code = getshared.getString("user_code", null);
            String closing_balance = getshared.getString("closing_balance", null);
            String credit_limit = getshared.getString("credit_limit", null);
            String doj = getshared.getString("doj", "");
            String source_code = getshared.getString("source_code", null);
            String tsp_img = getshared.getString("tsp_image", null);

            if(tsp_img!=null){
                checkFileExists(tsp_img, view);
            }else{
                Glide.with(getContext())
                        .load("")
                        .placeholder(getContext().getResources().getDrawable(R.drawable.userprofile))
                        .into(profimg);
            }

            closing_bal.setText(closing_balance);
            credit_lim.setText(credit_limit);
            dojtxt.setText(doj);
            nametxt.setText(name);
            source_codetxt.setText(source_code);

        } else {
            GetData();
        }


/*
        view_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.aadhar_imag_view);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.horizontalMargin = 200;
                lp.gravity = Gravity.CENTER;
                ImageView crossclose = (ImageView) dialog.findViewById(R.id.crossclose);
                aadhar = (ImageView) dialog.findViewById(R.id.aadhar);
            */
/*    Glide.with(getContext())
                        .load(aadhar_no+".jpg")

                        .into(aadhar);
*//*



                //    checkFileExists_Aadhar(aadhar_no, view);
                aadhar.setImageBitmap(decodedByte);
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                crossclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();

                    }
                });
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
        });
*/

      /*  pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefrences filt = new Prefrences();
                android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_mainLayout, filt);
                fragmentTransaction.commit();

            }
        });
*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void GetData() {
        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        PostQue = GlobalClass.setVolleyReq(getContext());

        JSONObject jsonObject = new JSONObject();


        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
         user = prefs.getString("Username", null);
         passwrd = prefs.getString("password", null);
         access = prefs.getString("ACCESS_TYPE", null);
         api_key = prefs.getString("API_KEY", null);


        try {
            jsonObject.put("API_Key", api_key);
            jsonObject.put("tsp", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = GlobalClass.setVolleyReq(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile", jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: "+response );
                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                         /*   Glide.with(getContext())
                                    .load(response.getString(Constants.tsp_image)+".jpg")

                                    .into(profimg);
*/
                            checkFileExists(response.getString(Constants.tsp_image), view);
                            prof = response.getString(Constants.tsp_image);
                            String ac_code = response.getString(Constants.ac_code);
                            String address = response.getString(Constants.address);
                            String email = response.getString(Constants.email);
                            String mobile = response.getString(Constants.mobile);
                            String pincode = response.getString(Constants.pincode);
                            String user_code = response.getString(Constants.user_code);

                            closing_bal.setText(response.getString(Constants.closing_balance));
                            credit_lim.setText(response.getString(Constants.credit_limit));
                            dojtxt.setText(response.getString(Constants.doj));
                            nametxt.setText(response.getString(Constants.name));
                            source_codetxt.setText(response.getString(Constants.source_code));


                            try {
                                String decode = response.getString(Constants.aadhar_no).substring(response.getString(Constants.aadhar_no).lastIndexOf(",") + 1);
                                byte[] decodedString = Base64.decode(decode, Base64.DEFAULT);
                                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: json"+jsonObject );
        Log.e(TAG, "GetData: URL"+jsonObjectRequest );
    }

    String replaceString(String string) {
        string.replaceAll("[^a-zA-Z0-9]", "_");
        aadharimg = string;
        return string;
    }

    public void checkFileExists(String str, View view) {

        String url = str;
        if (!url.equals("")) {
            CheckFileExistTask task = new CheckFileExistTask();
            task.execute(url);
        }
    }

    private class CheckFileExistTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // This connection won't follow redirects returned by the remote server.
                HttpURLConnection.setFollowRedirects(false);
                // Open connection to the remote server
                java.net.URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // Set request method
                con.setRequestMethod("HEAD");
                // get returned code
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);

            } catch (Exception e) {
                e.printStackTrace();

                return false;

            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Update status message
            if (result == true) {
                Glide.with(getContext())
                        .load(prof)
                        .into(profimg);

            } else {
                Glide.with(getContext())
                        .load("")
                        .placeholder(getContext().getResources().getDrawable(R.drawable.user_profile))
                        .into(profimg);

            }
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

    public void checkFileExists_Aadhar(String str, View view) {

        String url = str;
        if (!url.equals("")) {
            CheckFileExistTask_Aadhar task = new CheckFileExistTask_Aadhar();
            task.execute(url);
        }
    }

    private class CheckFileExistTask_Aadhar extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // This connection won't follow redirects returned by the remote server.
                HttpURLConnection.setFollowRedirects(false);
                // Open connection to the remote server
                java.net.URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // Set request method
                con.setRequestMethod("HEAD");
                // get returned code
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);

            } catch (Exception e) {
                e.printStackTrace();

                return false;

            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Update status message
            if (result == true) {
                Glide.with(getContext())
                        .load(aadharimg)

                        .into(aadhar);

            } else {
                Glide.with(getContext())
                        .load("")
                        .placeholder(getContext().getResources().getDrawable(R.drawable.userprofile))
                        .into(aadhar);

            }
        }
    }

}
