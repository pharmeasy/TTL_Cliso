package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Mail_Model_Receipt;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e5426@thyrocare.com on 18/5/18.
 */

public class TrackDetAdapter extends BaseAdapter {
    ImageView mail, print, download;
    ArrayList<Mail_Model_Receipt> barCodeDetail = new ArrayList<Mail_Model_Receipt>();
    Context mContext;
    ArrayList<TrackDetModel> trackdet = new ArrayList<>();
    int selectedPosition = 0;
    int[] SelectedArray;
    LinearLayout cancelled_Layout;
    public static RequestQueue PostQue;
    private String email_id_string;
    SharedPreferences sharedpreferences;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String user;
    String Barcodesend = "";
    private String api_key;
    ProgressDialog barProgressDialog_nxt;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public TrackDetAdapter(Context ManagingTabsActivity, ArrayList<TrackDetModel> arrayList) {
        sharedpreferences = ManagingTabsActivity.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        mContext = ManagingTabsActivity;
        trackdet = arrayList;
        SelectedArray = new int[arrayList.size()];

    }

    @Override
    public int getCount() {
        return trackdet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.track_det_row, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView Refby = (TextView) convertView.findViewById(R.id.Refby);
        TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
        TextView tests = (TextView) convertView.findViewById(R.id.tests);
        download = (ImageView) convertView.findViewById(R.id.download);
        mail = (ImageView) convertView.findViewById(R.id.mail);
        print = (ImageView) convertView.findViewById(R.id.print);
        cancelled_Layout = (LinearLayout) convertView.findViewById(R.id.cancelled_Layout);

        name.setText(trackdet.get(position).getName().toString());
        Refby.setText("Ref by:" + trackdet.get(position).getRef_By().toString());
        barcode.setText(trackdet.get(position).getBarcode().toString());
        tests.setText(trackdet.get(position).getTests().toString());

        /*if (trackdet.get(position).getPdflink().toString().equals("") || trackdet.get(position).getPdflink().toString().equals(null)) {
            download.setVisibility(View.INVISIBLE);
        } else {
            download.setVisibility(View.VISIBLE);
        }*/

        if (trackdet.get(position).getEmail().equals("") || trackdet.get(position).getEmail().equals(null)) {
            mail.setVisibility(View.GONE);
        } else {
            mail.setVisibility(View.VISIBLE);
        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barProgressDialog_nxt = new ProgressDialog(mContext);
                barProgressDialog_nxt.setTitle("Kindly wait ...");
                barProgressDialog_nxt.setMessage(ToastFile.processing_request);
                barProgressDialog_nxt.setProgressStyle(barProgressDialog_nxt.STYLE_SPINNER);
                barProgressDialog_nxt.setProgress(0);
                barProgressDialog_nxt.setMax(20);
                barProgressDialog_nxt.show();
                barProgressDialog_nxt.setCanceledOnTouchOutside(false);
                barProgressDialog_nxt.setCancelable(false);

                PostQue = GlobalClass.setVolleyReq(mContext);


                try {
                    JSONObject jsonObject = new JSONObject();
                    SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
                    user = prefs.getString("Username", null);
                    String passwrd = prefs.getString("password", null);
                    String access = prefs.getString("ACCESS_TYPE", null);
                    api_key = prefs.getString("API_KEY", null);

                    try {

                        jsonObject.put("apikey", api_key);
                        jsonObject.put("barcode", trackdet.get(position).getBarcode());

                        Barcodesend = trackdet.get(position).getBarcode();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = GlobalClass.setVolleyReq(mContext);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, Api.downloadreceipt, jsonObject,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                        barProgressDialog_nxt.dismiss();
                                    }
                                    Log.e(TAG, "onResponse: RESPONSE" + response);
                                    try { //Track_BarcodeModel
                                        barCodeDetail = new ArrayList<Mail_Model_Receipt>();
                                        Mail_Model_Receipt model = new Mail_Model_Receipt();
                                        model.setAddress(response.optString("address"));
                                        model.setAmount(response.optString("amount"));
                                        model.setAmount_word(response.optString("amount_word"));
                                        model.setEmail(response.optString("email"));
                                        model.setMobile(response.optString("mobile"));
                                        model.setName(response.optString("name"));
                                        model.setOrderdate(response.optString("orderdate"));
                                        model.setResponse(response.optString("response"));
                                        model.setTest(response.optString("test"));
                                        model.setUrl(response.optString("url"));
                                        barCodeDetail.add(model);
/*{"address":"","amount":"200","amount_word":"Two Hundred ","email":"sarathykodai@gmail.com","mobile":"9842187270","name":"MRS LINDA (31Y\/F)",
"orderdate":"31-05-2018","res_id":"RES0000","response":"SUCCESS","test":",FT3,FT4,TSH","url":"https:\\www.thyrocare.com\\Wellness\\AutoReceipt\\G7919583.pdf"}*/

                                        if (model.getUrl().equalsIgnoreCase("null")) {
                                            TastyToast.makeText(mContext, model.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                        } else {
                                            Intent browserIntent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(model.getUrl()));
                                            mContext.startActivity(browserIntent);

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
                    jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: URL" + jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: json" + jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barProgressDialog_nxt = new ProgressDialog(mContext);
                barProgressDialog_nxt.setTitle("Kindly wait ...");
                barProgressDialog_nxt.setMessage(ToastFile.processing_request);
                barProgressDialog_nxt.setProgressStyle(barProgressDialog_nxt.STYLE_SPINNER);
                barProgressDialog_nxt.setProgress(0);
                barProgressDialog_nxt.setMax(20);
                barProgressDialog_nxt.show();
                barProgressDialog_nxt.setCanceledOnTouchOutside(false);
                barProgressDialog_nxt.setCancelable(false);

                PostQue = GlobalClass.setVolleyReq(mContext);


                try {
                    JSONObject jsonObject = new JSONObject();
                    SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
                    user = prefs.getString("Username", null);
                    String passwrd = prefs.getString("password", null);
                    String access = prefs.getString("ACCESS_TYPE", null);
                    api_key = prefs.getString("API_KEY", null);

                    try {

                        jsonObject.put("apikey", api_key);
                        jsonObject.put("barcode", trackdet.get(position).getBarcode());

                        Barcodesend = trackdet.get(position).getBarcode();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = GlobalClass.setVolleyReq(mContext);
                    Log.e(TAG, "Post Data---" + jsonObject.toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, Api.downloadreceipt, jsonObject,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                        barProgressDialog_nxt.dismiss();
                                    }

                                    Log.e(TAG, "onResponse: RESPONSE" + response);

                                    try {
                                        //Track_BarcodeModel
                                        barCodeDetail = new ArrayList<Mail_Model_Receipt>();
                                        Mail_Model_Receipt model = new Mail_Model_Receipt();
                                        model.setAddress(response.optString("address"));
                                        model.setAmount(response.optString("amount"));
                                        model.setAmount_word(response.optString("amount_word"));
                                        model.setEmail(response.optString("email"));
                                        model.setMobile(response.optString("mobile"));
                                        model.setName(response.optString("name"));
                                        model.setOrderdate(response.optString("orderdate"));
                                        model.setResponse(response.optString("response"));
                                        model.setTest(response.optString("test"));
                                        model.setUrl(response.optString("url"));
                                        model.setRes_id(response.optString("res_id"));
                                        barCodeDetail.add(model);


/*{"address":"","amount":"200","amount_word":"Two Hundred ","email":"sarathykodai@gmail.com","mobile":"9842187270","name":"MRS LINDA (31Y\/F)",
"orderdate":"31-05-2018","res_id":"RES0000","response":"SUCCESS","test":",FT3,FT4,TSH","url":"https:\\www.thyrocare.com\\Wellness\\AutoReceipt\\G7919583.pdf"}*/
                                        if (model.getRes_id().equalsIgnoreCase(Constants.RES0000)) {
                                            if (model.getUrl().equalsIgnoreCase("null")) {
                                                TastyToast.makeText(mContext, model.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                            } else {
                                                Intent browserIntent = new Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(model.getUrl()));
                                                mContext.startActivity(browserIntent);

                                            }
                                        } else {
                                            GlobalClass.toastyError(mContext, model.getResponse(), false);
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
                                // TastyToast.makeText(mContext, error.getLocalizedMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: URL" + jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: json" + jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barProgressDialog_nxt = new ProgressDialog(mContext);
                barProgressDialog_nxt.setTitle("Kindly wait ...");
                barProgressDialog_nxt.setMessage(ToastFile.processing_request);
                barProgressDialog_nxt.setProgressStyle(barProgressDialog_nxt.STYLE_SPINNER);
                barProgressDialog_nxt.setProgress(0);
                barProgressDialog_nxt.setMax(20);
                barProgressDialog_nxt.show();
                barProgressDialog_nxt.setCanceledOnTouchOutside(false);
                barProgressDialog_nxt.setCancelable(false);

                PostQue = GlobalClass.setVolleyReq(mContext);


                try {
                    JSONObject jsonObject = new JSONObject();
                    SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
                    user = prefs.getString("Username", null);
                    String passwrd = prefs.getString("password", null);
                    String access = prefs.getString("ACCESS_TYPE", null);
                    api_key = prefs.getString("API_KEY", null);

                    try {

                        jsonObject.put("apikey", api_key);
                        jsonObject.put("barcode", trackdet.get(position).getBarcode());

                        Barcodesend = trackdet.get(position).getBarcode();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = GlobalClass.setVolleyReq(mContext);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, Api.downloadreceipt, jsonObject,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                        barProgressDialog_nxt.dismiss();
                                    }
                                    Log.e(TAG, "onResponse: RESPONSE" + response);
                                    try { //Track_BarcodeModel
                                        barCodeDetail = new ArrayList<Mail_Model_Receipt>();
                                        Mail_Model_Receipt model = new Mail_Model_Receipt();
                                        model.setAddress(response.optString("address"));
                                        model.setAmount(response.optString("amount"));
                                        model.setAmount_word(response.optString("amount_word"));
                                        model.setEmail(response.optString("email"));
                                        model.setMobile(response.optString("mobile"));
                                        model.setName(response.optString("name"));
                                        model.setOrderdate(response.optString("orderdate"));
                                        model.setResponse(response.optString("response"));
                                        model.setTest(response.optString("test"));
                                        model.setUrl(response.optString("url"));
                                        barCodeDetail.add(model);

                                        if (model.getUrl().equalsIgnoreCase("null")) {
                                            TastyToast.makeText(mContext, model.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                        } else {
                                            ShowMailAlert(trackdet.get(position).getPatient_id().toString(), trackdet.get(position).getBarcode().toString(),
                                                    trackdet.get(position).getEmail().toString(), trackdet.get(position).getDate().toString());
                                        }
/*{"address":"","amount":"200","amount_word":"Two Hundred ","email":"sarathykodai@gmail.com","mobile":"9842187270","name":"MRS LINDA (31Y\/F)",
"orderdate":"31-05-2018","res_id":"RES0000","response":"SUCCESS","test":",FT3,FT4,TSH","url":"https:\\www.thyrocare.com\\Wellness\\AutoReceipt\\G7919583.pdf"}*/

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
                    jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: URL" + jsonObjectRequest);
                    Log.e(TAG, "DownloadReceipt: json" + jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return convertView;
    }

    public void ShowMailAlert(final String patient_ID, final String barcode, final String email, final String date) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.track_receipt_mail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 200;
        lp.gravity = Gravity.CENTER;
        final EditText emailid = (EditText) dialog.findViewById(R.id.email);
        final EditText mob = (EditText) dialog.findViewById(R.id.mob);
        final EditText name = (EditText) dialog.findViewById(R.id.name);

        mob.addTextChangedListener(new TextWatcher() {
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
                ) {
                    TastyToast.makeText(mContext, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        mob.setText(enteredString.substring(1));
                    } else {
                        mob.setText("");
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


        if (barCodeDetail.get(0).getEmail().equalsIgnoreCase("null")) {
            emailid.setText("");
        } else {
            emailid.setText(barCodeDetail.get(0).getEmail());
        }
        if (barCodeDetail.get(0).getName().equalsIgnoreCase("null")) {
            name.setText("");
        } else {
            name.setText(barCodeDetail.get(0).getName());
        }
        if (barCodeDetail.get(0).getMobile().equalsIgnoreCase("null")) {
            mob.setText("");
        } else {
            mob.setText(barCodeDetail.get(0).getMobile());
        }


        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_id_string = emailid.getText().toString();
                if (!email_id_string.equals("")) {
                    if (!emailid.getText().toString().matches(emailPattern)) {
                        Toast.makeText(mContext, ToastFile.invalid_eml, Toast.LENGTH_SHORT).show();
                    } else {
                        SendMail();
                        dialog.dismiss();
                    }

                } else {
                    Toast.makeText(mContext, ToastFile.crt_eml, Toast.LENGTH_SHORT).show();
                }
                //GetData(patient_ID,barcode,email,date);
//                SendMail();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void SendMail() {
        PostQue = GlobalClass.setVolleyReq(mContext);

        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            api_key = prefs.getString("API_KEY", null);

            jsonObject.put("apikey", api_key);
            jsonObject.put("barcode", Barcodesend);

            String[] twoStringArray = barCodeDetail.get(0).getName().split("\\(", 2); //the main line

            jsonObject.put("name", twoStringArray[0]);
            jsonObject.put("mob", barCodeDetail.get(0).getMobile());
            jsonObject.put("email", email_id_string);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.Receipt_mail, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            /*"name":"SELVI HARSHINI III (14Y\/F)"*/
                            TastyToast.makeText(mContext, response.optString("response"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


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
        Log.e(TAG, "SendMail: " + jsonObjectRequest);
        Log.e(TAG, "SendMail: json" + jsonObject);
    }

    public void setFilter(List<TrackDetModel> countryModels) {
        trackdet = new ArrayList<>();
        trackdet.addAll(countryModels);
        cancelled_Layout.setBackgroundResource(R.drawable.maroon_border_white_bg);
        notifyDataSetChanged();

    }
}
