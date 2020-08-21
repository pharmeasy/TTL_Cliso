package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Downloadreceipt_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.Receiptmail_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Mail_Model_Receipt;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

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
    int[] SelectedArray;
    LinearLayout cancelled_Layout;
    public static RequestQueue PostQue;
    private String email_id_string;
    SharedPreferences sharedpreferences;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String user;
    String Barcodesend = "";
    private String api_key;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private int fposition;

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


        GlobalClass.SetText(name, trackdet.get(position).getName().toString());
        GlobalClass.SetText(Refby, "Ref by:" + trackdet.get(position).getRef_By().toString());
        GlobalClass.SetText(barcode, trackdet.get(position).getBarcode().toString());
        GlobalClass.SetText(tests, trackdet.get(position).getTests().toString());


        if (GlobalClass.isNull(trackdet.get(position).getEmail())) {
            mail.setVisibility(View.GONE);
        } else {
            mail.setVisibility(View.VISIBLE);
        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    try {
                        if (ControllersGlobalInitialiser.downloadreceipt_controller!= null) {
                            ControllersGlobalInitialiser.downloadreceipt_controller = null;
                        }
                        ControllersGlobalInitialiser.downloadreceipt_controller = new Downloadreceipt_Controller((Activity)mContext, TrackDetAdapter.this,1);
                        ControllersGlobalInitialiser.downloadreceipt_controller.downrecpcontroller(jsonObject,PostQue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    try {
                        if (ControllersGlobalInitialiser.downloadreceipt_controller!= null) {
                            ControllersGlobalInitialiser.downloadreceipt_controller = null;
                        }
                        ControllersGlobalInitialiser.downloadreceipt_controller = new Downloadreceipt_Controller((Activity)mContext, TrackDetAdapter.this,2);
                        ControllersGlobalInitialiser.downloadreceipt_controller.downrecpcontroller(jsonObject,queue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    fposition=position;
                    try {
                        if (ControllersGlobalInitialiser.downloadreceipt_controller!= null) {
                            ControllersGlobalInitialiser.downloadreceipt_controller = null;
                        }
                        ControllersGlobalInitialiser.downloadreceipt_controller = new Downloadreceipt_Controller((Activity)mContext, TrackDetAdapter.this,3);
                        ControllersGlobalInitialiser.downloadreceipt_controller.downrecpcontroller(jsonObject,queue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    GlobalClass.showTastyToast((Activity)mContext, ToastFile.crt_mob_num, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(mob,enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(mob,"");
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
            GlobalClass.SetEditText(emailid,"");
        } else {
            GlobalClass.SetEditText(emailid,barCodeDetail.get(0).getEmail());
        }
        if (barCodeDetail.get(0).getName().equalsIgnoreCase("null")) {
            GlobalClass.SetEditText(name,"");
        } else {
            GlobalClass.SetEditText(name,barCodeDetail.get(0).getName());
        }
        if (barCodeDetail.get(0).getMobile().equalsIgnoreCase("null")) {
            GlobalClass.SetEditText(mob,"");
        } else {
            GlobalClass.SetEditText(mob,barCodeDetail.get(0).getMobile());
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
                        GlobalClass.showTastyToast((Activity) mContext, ToastFile.invalid_eml, 2);
                    } else {
                        SendMail();
                        dialog.dismiss();
                    }

                } else {
                    GlobalClass.showTastyToast((Activity) mContext, ToastFile.crt_eml,2);
                }

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

        try {
            if (ControllersGlobalInitialiser.receiptmail_controller != null) {
                ControllersGlobalInitialiser.receiptmail_controller = null;
            }
            ControllersGlobalInitialiser.receiptmail_controller = new Receiptmail_Controller((Activity)mContext, TrackDetAdapter.this);
            ControllersGlobalInitialiser.receiptmail_controller.getreceiptmail(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilter(List<TrackDetModel> countryModels) {
        trackdet = new ArrayList<>();
        trackdet.addAll(countryModels);
        cancelled_Layout.setBackgroundResource(R.drawable.maroon_border_white_bg);
        notifyDataSetChanged();

    }

    public void getResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            GlobalClass.showTastyToast((Activity)mContext, response.optString("response"), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiptdownResponse(JSONObject response, int fromcome) {
        if (fromcome==0){
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
                    GlobalClass.showTastyToast((Activity)mContext, model.getResponse(),2);

                } else {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(model.getUrl()));
                    mContext.startActivity(browserIntent);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (fromcome==2){
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


                if (!GlobalClass.isNull(model.getRes_id()) && model.getRes_id().equalsIgnoreCase(Constants.RES0000)) {
                    if (model.getUrl().equalsIgnoreCase("null")) {
                        GlobalClass.showTastyToast((Activity)mContext, model.getResponse(),2);
                    } else {
                        Intent browserIntent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(model.getUrl()));
                        mContext.startActivity(browserIntent);

                    }
                } else {
                    GlobalClass.showTastyToast((Activity)mContext, model.getResponse(), 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (fromcome==3){
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
                    GlobalClass.showTastyToast((Activity)mContext, model.getResponse(), 2);

                } else {
                    ShowMailAlert(trackdet.get(fposition).getPatient_id().toString(), trackdet.get(fposition).getBarcode().toString(),
                            trackdet.get(fposition).getEmail().toString(), trackdet.get(fposition).getDate().toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
