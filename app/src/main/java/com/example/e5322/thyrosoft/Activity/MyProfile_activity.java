package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.ProfileDetailsResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class MyProfile_activity extends AppCompatActivity {
    public static RequestQueue PostQue;
    TextView nametxt, dojtxt, source_codetxt, closing_bal, credit_lim;
    Button view_aadhar, pref;
    ProgressDialog barProgressDialog;
    ImageView aadhar;
    ImageView back, home;
    ImageView profimg;
    String prof = "", aadharimg;
    String aadhar_no = "";
    String URL = "";
    Bitmap decodedByte;
    private SharedPreferences getshared;
    private String user;
    private String passwrd;
    private String api_key;
    private Global globalClass;
    private String access;
    private String TAG = MyProfile_activity.class.getSimpleName();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);

        nametxt = (TextView) findViewById(R.id.name);
        dojtxt = (TextView) findViewById(R.id.doj);
        source_codetxt = (TextView) findViewById(R.id.source_code);
        closing_bal = (TextView) findViewById(R.id.closing_bal);
        credit_lim = (TextView) findViewById(R.id.credit_lim);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(MyProfile_activity.this);
            }
        });
//        view_aadhar = (Button) findViewById(R.id.view_aadhar);
//        pref = (Button) findViewById(R.id.pref);

        profimg = (ImageView) findViewById(R.id.profimg);

        SharedPreferences getshared = getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

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

            if (tsp_img != null) {
                checkFileExists(tsp_img);
            } else {
                Glide.with(MyProfile_activity.this)
                        .load("")
                        .placeholder(MyProfile_activity.this.getResources().getDrawable(R.drawable.userprofile))
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
                final Dialog dialog = new Dialog(MyProfile_activity.this);
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
/*    Glide.with(MyProfile_activity.this)
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
                android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) MyProfile_activity.this).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_mainLayout, filt);
                fragmentTransaction.commit();

            }
        });*/

    }

    private void GetData() {
        barProgressDialog = new ProgressDialog(MyProfile_activity.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        PostQue = Volley.newRequestQueue(MyProfile_activity.this);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        RequestQueue queue = Volley.newRequestQueue(MyProfile_activity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile",
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            ProfileDetailsResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ProfileDetailsResponseModel.class);

                            if (responseModel != null) {
                                if (responseModel.getTsp_image() != null) {
                                    prof = responseModel.getTsp_image();
                                    checkFileExists(prof);
                                }

                                closing_bal.setText(responseModel.getClosing_balance());
                                credit_lim.setText(responseModel.getCredit_limit());
                                dojtxt.setText(responseModel.getDoj());
                                nametxt.setText(responseModel.getName());
                                source_codetxt.setText(responseModel.getSource_code());

                            } else {
                                Toast.makeText(MyProfile_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }

                            /*try {
                                String decode = response.getString(Constants.aadhar_no).substring(response.getString(Constants.aadhar_no).lastIndexOf(",") + 1);
                                byte[] decodedString = Base64.decode(decode, Base64.DEFAULT);
                                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        } catch (Exception e) {
                            Log.e(TAG, "on ERROR ------>" + e.getLocalizedMessage());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(MyProfile_activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(jsonObjectRequest);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
    }

    public void checkFileExists(String str) {
        String url = str;
        if (!url.equals("")) {
            CheckFileExistTask task = new CheckFileExistTask();
            task.execute(url);
        }
    }

    public void checkFileExists_Aadhar(String str, View view) {
        String url = str;
        if (!url.equals("")) {
            CheckFileExistTask_Aadhar task = new CheckFileExistTask_Aadhar();
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
            if (result) {
                Glide.with(MyProfile_activity.this)
                        .load(prof)
                        .into(profimg);
            } else {
                Glide.with(MyProfile_activity.this)
                        .load("")
                        .placeholder(MyProfile_activity.this.getResources().getDrawable(R.drawable.user_profile))
                        .into(profimg);
            }
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
            if (result) {
                Glide.with(MyProfile_activity.this)
                        .load(aadharimg)
                        .into(aadhar);
            } else {
                Glide.with(MyProfile_activity.this)
                        .load("")
                        .placeholder(MyProfile_activity.this.getResources().getDrawable(R.drawable.userprofile))
                        .into(aadhar);
            }
        }
    }

}
