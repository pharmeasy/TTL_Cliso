package com.example.e5322.thyrosoft.Adapter;


import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CleverTapHelper;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e5426@thyrocare.com on 18/5/18.
 */

public class ResultDtlAdapter extends BaseAdapter {
    public static RequestQueue PostQue;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView mail, clon, print;
    Context mContext;
    ArrayList<TrackDetModel> trackdet = new ArrayList<>();
    int selectedPosition = 0;
    int[] SelectedArray;
    SharedPreferences sharedpreferences;
    private String TAG = ManagingTabsActivity.class.getSimpleName();
    private String user_code;
    CleverTapHelper cleverTapHelper;

    public ResultDtlAdapter(Context context, ArrayList<TrackDetModel> arrayList) {
        sharedpreferences = context.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        mContext = context;
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
            convertView = inflater.inflate(R.layout.single_result_details, parent, false);
        }

        user_code = sharedpreferences.getString("USER_CODE", null);
        Log.e(TAG, "<< USER_CODE >> " + user_code);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView Refby = (TextView) convertView.findViewById(R.id.Refby);
        final TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
        TextView tests = (TextView) convertView.findViewById(R.id.tests);
        ImageView download = (ImageView) convertView.findViewById(R.id.download);
        ImageView print = (ImageView) convertView.findViewById(R.id.print);
        ImageView share = (ImageView) convertView.findViewById(R.id.share);
        mail = (ImageView) convertView.findViewById(R.id.mail);
        clon = (ImageView) convertView.findViewById(R.id.colon);
        cleverTapHelper = new CleverTapHelper((Activity) mContext);

        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        String versionName = pInfo.versionName;

       /* if (trackdet.get(position).getEmail().equals("") || trackdet.get(position).getEmail().equals(null)) {
            mail.setVisibility(View.GONE);
        } else {
            mail.setVisibility(View.VISIBLE);
        }*/

        if (!GlobalClass.isNull(trackdet.get(position).getChn_pending())) {
            if (trackdet.get(position).getChn_pending().equals("null")) {
                name.setText(trackdet.get(position).getName());
                Refby.setText("Ref by:" + trackdet.get(position).getRef_By());
                barcode.setText(trackdet.get(position).getBarcode());
                tests.setText(trackdet.get(position).getTests());
                clon.setVisibility(View.GONE);
            } else {
                name.setText(trackdet.get(position).getName());
                clon.setVisibility(View.VISIBLE);
                Refby.setText("Ref by:" + trackdet.get(position).getRef_By());
                barcode.setText(trackdet.get(position).getBarcode());
                tests.setText(trackdet.get(position).getTests());
            }
        } else {
            name.setText(trackdet.get(position).getName());
            Refby.setText("Ref by:" + trackdet.get(position).getRef_By());
            barcode.setText(trackdet.get(position).getBarcode());
            tests.setText(trackdet.get(position).getTests());
            clon.setVisibility(View.GONE);
        }

        try {

            if (!GlobalClass.isNull(trackdet.get(position).getPdflink())) {
                download.setVisibility(View.VISIBLE);
                if (!GlobalClass.isNull(user_code) && user_code.startsWith("BM")) {
                    share.setVisibility(View.VISIBLE);
                }
            } else if (trackdet.get(position).getPdflink().equals("null")) {
                download.setVisibility(View.INVISIBLE);
                share.setVisibility(View.INVISIBLE);
            }

            if (!GlobalClass.isNull(trackdet.get(position).getEmail())) {
                mail.setVisibility(View.VISIBLE);
            } else if (trackdet.get(position).getEmail().equals("null")) {
                mail.setVisibility(View.INVISIBLE);
                print.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = trackdet.get(position).getName();
                String Test = trackdet.get(position).getTests();
                String refyBy = trackdet.get(position).getRef_By();
                String email = trackdet.get(position).getEmail();
                String labcode = trackdet.get(position).getLabcode();
                String sct = trackdet.get(position).getSct();
                String sample_type = trackdet.get(position).getSample_type();
                String PatientDetails = name + "," + Test +","+ refyBy + "," + email + ","+labcode + "," + sct + "," + sample_type;

                String header = "Cliso App" + versionName;
                long currentTime = System.currentTimeMillis();

                try {
                    if (trackdet.get(position).getPdflink() != null && !trackdet.get(position).getPdflink().isEmpty() && trackdet.get(position).getPdflink().length() > 0) {
                        new LogUserActivityTagging((Activity)mContext,Constants.REPORT_DOWNLOAD,""+barcode.getText().toString()+"- DOWNLOAD");
                        long endTime = System.currentTimeMillis();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trackdet.get(position).getPdflink()));
                        mContext.startActivity(browserIntent);
                        long calTime = endTime - currentTime;

                        System.out.println("cal time : " + calTime/1000.0);
                        cleverTapHelper.reportDownloadEvent(header,PatientDetails,Constants.REPORT_DOWNLOAD, String.valueOf(calTime),"","ClisoApp","Mobile");
                    }else {
                        cleverTapHelper.reportDownloadEvent(header,PatientDetails,"Report not found","","Not Download","ClisoApp","Mobile");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (trackdet.get(position).getPdflink() != null && !trackdet.get(position).getPdflink().isEmpty() && trackdet.get(position).getPdflink().length() > 0) {
                        new LogUserActivityTagging((Activity)mContext,Constants.REPORT_DOWNLOAD,""+barcode.getText().toString()+"- SHARE");

                        sharePDFLink(trackdet.get(position).getPdflink());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LogUserActivityTagging((Activity)mContext,Constants.REPORT_DOWNLOAD,""+barcode.getText().toString()+"- PRINT");

                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(trackdet.get(position).getPdflink()));
                    mContext.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ShowMailAlert(trackdet.get(position).getPatient_id(), trackdet.get(position).getBarcode(), trackdet.get(position).getEmail(), trackdet.get(position).getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    private void sharePDFLink(String pdflink) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, pdflink);
        mContext.startActivity(Intent.createChooser(share, "Share PDF link!"));
    }

    private void GetData(String patient_ID, final String barcode, String email, String date) {

        PostQue = GlobalClass.setVolleyReq(mContext);

        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);

            jsonObject.put("api_key", prefs.getString("API_KEY", ""));
            jsonObject.put("Patient_id", patient_ID);
            jsonObject.put("barcodes", barcode);
            jsonObject.put("tomailid", email);
            jsonObject.put("tsp", prefs.getString("Username", ""));
            jsonObject.put(Constants.date, date);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.postmailLive, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            String finalJson = response.toString();
                            JSONObject parentObjectOtp = new JSONObject(finalJson);
//                            JSONArray jsonArray = response.optJSONArray(Constants.billingDetails);
                            new LogUserActivityTagging((Activity)mContext,Constants.REPORT_DOWNLOAD,""+barcode+"- MAIL");

                            if (parentObjectOtp != null) {
                                String response1 = parentObjectOtp.getString("response");
                                String RES_ID = parentObjectOtp.getString("RES_ID");
                                String error = parentObjectOtp.getString("error");
                                Toast.makeText(mContext, "" + response1, Toast.LENGTH_SHORT).show();

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
        Log.e(TAG, "GetData: JSON" + jsonObject);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);

    }

    public void ShowMailAlert(final String patient_ID, final String barcode, final String email, final String date) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.mail_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 200;
        lp.gravity = Gravity.CENTER;
        final EditText emailid = (EditText) dialog.findViewById(R.id.email);


        emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext,
                            ToastFile.crt_eml,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        emailid.setText(enteredString.substring(1));
                    } else {
                        emailid.setText("");
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


        if (!email.equals("")) {
            emailid.setText(email);
        } else {

        }
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailid.equals("")) {
                    Toast.makeText(mContext, "Please enter E-mail id", Toast.LENGTH_SHORT).show();
                } else {
                    GetData(patient_ID, barcode, emailid.getText().toString(), date);
                    dialog.dismiss();
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

    public void setFilter(List<TrackDetModel> countryModels) {
        trackdet = new ArrayList<>();
        trackdet.addAll(countryModels);
        notifyDataSetChanged();
    }
}
