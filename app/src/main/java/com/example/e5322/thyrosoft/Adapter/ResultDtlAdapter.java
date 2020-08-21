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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.Validator;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.Postreportmail_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
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

public class ResultDtlAdapter extends BaseAdapter {
    public static RequestQueue PostQue;
    ImageView mail, clon;
    Context mContext;
    ArrayList<TrackDetModel> trackdet = new ArrayList<>();
    int[] SelectedArray;
    SharedPreferences sharedpreferences;
    private String TAG = ManagingTabsActivity.class.getSimpleName();
    private String user_code;

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
        TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
        TextView tests = (TextView) convertView.findViewById(R.id.tests);
        ImageView download = (ImageView) convertView.findViewById(R.id.download);
        ImageView print = (ImageView) convertView.findViewById(R.id.print);
        ImageView share = (ImageView) convertView.findViewById(R.id.share);
        mail = (ImageView) convertView.findViewById(R.id.mail);
        clon = (ImageView) convertView.findViewById(R.id.colon);


        if (!GlobalClass.isNull(trackdet.get(position).getChn_pending())) {
            if (trackdet.get(position).getChn_pending().equals("null")) {
                GlobalClass.SetText(name, trackdet.get(position).getName());
                GlobalClass.SetText(Refby, "Ref by:" + trackdet.get(position).getRef_By());
                GlobalClass.SetText(barcode, trackdet.get(position).getBarcode());
                GlobalClass.SetText(tests, trackdet.get(position).getTests());
                clon.setVisibility(View.GONE);
            } else {
                GlobalClass.SetText(name, trackdet.get(position).getName());
                clon.setVisibility(View.VISIBLE);
                GlobalClass.SetText(Refby, "Ref by:" + trackdet.get(position).getRef_By());
                GlobalClass.SetText(barcode, trackdet.get(position).getBarcode());
                GlobalClass.SetText(tests, trackdet.get(position).getTests());
            }
        } else {
            GlobalClass.SetText(name, trackdet.get(position).getName());
            GlobalClass.SetText(Refby, "Ref by:" + trackdet.get(position).getRef_By());
            GlobalClass.SetText(barcode, trackdet.get(position).getBarcode());
            GlobalClass.SetText(tests, trackdet.get(position).getTests());
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
                try {
                    if (trackdet.get(position).getPdflink() != null && !trackdet.get(position).getPdflink().isEmpty() && trackdet.get(position).getPdflink().length() > 0) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trackdet.get(position).getPdflink()));
                        mContext.startActivity(browserIntent);
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

    private void GetData(String patient_ID, String barcode, String email, String date) {

        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);

            jsonObject.put("api_key", prefs.getString("API_KEY", null));
            jsonObject.put("Patient_id", patient_ID);
            jsonObject.put("barcodes", barcode);
            jsonObject.put("tomailid", email);
            jsonObject.put("tsp", prefs.getString("Username", null));
            jsonObject.put(Constants.date, date);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(mContext);

        try {
            if (ControllersGlobalInitialiser.postreportmail_controller != null) {
                ControllersGlobalInitialiser.postreportmail_controller = null;
            }
            ControllersGlobalInitialiser.postreportmail_controller = new Postreportmail_Controller((Activity)mContext, ResultDtlAdapter.this);
            ControllersGlobalInitialiser.postreportmail_controller.getreportcontroller(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    GlobalClass.showTastyToast((Activity)mContext,
                            ToastFile.crt_eml,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(emailid,enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(emailid,"");
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
            GlobalClass.SetEditText(emailid,email);
        }

        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validator.emailValidation((Activity)mContext,emailid)){
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

    public void getreceiptresp(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);

            if (parentObjectOtp != null) {
                String response1 = parentObjectOtp.getString("response");
                GlobalClass.showTastyToast((Activity) mContext, "" + response1,0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
