package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.InterfaceSendOfflineWoe;
import com.example.e5322.thyrosoft.Interface.RefreshOfflineWoe;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Offline_edt_activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Offline_Woe_Adapter extends RecyclerView.Adapter<Offline_Woe_Adapter.ViewHolder> {
    Context mContext;
    DatabaseHelper myDb;
    Summary_model summary_model;
    ArrayList<MyPojoWoe> myPojoWoe;
    private String getBarcode_string;
    private String getSampleType_string;
    Offline_woe mfragment;
    String[] samples;
    SharedPreferences prefs, sharedPrefe;
    RefreshOfflineWoe refreshOfflineWoe;
    private MyPojoWoe myPojoWoeSend;
    String[] barcodes;
    private String MY_DEBUG_TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String woejsontoPass;
    ArrayList<String> getSampleTypes;
    ArrayList<String> getbarcode;
    private String TAG = ManagingTabsActivity.class.getSimpleName();
    private String RES_ID;
    private String barcode_patient_id;
    private String message;
    private String status;
    private String barcode_id;
    private ProgressDialog barProgressDialog;
    private RequestQueue POstQue;
    private RequestQueue sendGPSDetails;
    String user, passwrd, access, api_key;
    private String getIMEINUMBER;
    private String mobileModel;
    InterfaceSendOfflineWoe interfaceSendOfflineWoe;
    private static ManagingTabsActivity activity;
    ArrayList<String> setResponse;

    ArrayList<TRFModel> trflist = new ArrayList<>();

    Offline_woe offline_woe;
    Activity activity1;


    public Offline_Woe_Adapter(ManagingTabsActivity mContext, ArrayList<MyPojoWoe> allWoe, Offline_woe fragment, ArrayList<String> setResponse, Offline_woe offline_woe, Activity activity) {
        this.mContext = mContext;
        this.myPojoWoe = allWoe;
        this.mfragment = fragment;
        this.setResponse = setResponse;
        this.offline_woe=offline_woe;
        this.activity1=activity;
    }

    public void onClickDeleteOffWoe(RefreshOfflineWoe refreshOfflineWoe) {
        this.refreshOfflineWoe = refreshOfflineWoe;
    }

    public void onClickSendOfflineWoe(InterfaceSendOfflineWoe interfaceSendOfflineWoe) {
        this.interfaceSendOfflineWoe = interfaceSendOfflineWoe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_layout_woe_patient_details, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        summary_model = new Summary_model();

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        final SharedPreferences getIMIE = mContext.getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);
        SharedPreferences getModelNumber = mContext.getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", null);

        holder.linear_summary_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.summary_models = new ArrayList<>();
            }
        });

        if (setResponse.get(position) != null && !setResponse.get(position).equalsIgnoreCase("")) {
            holder.error_msg.setVisibility(View.VISIBLE);
            holder.error_msg.setText(setResponse.get(position).toString());
        }
        holder.image_tag.setVisibility(View.GONE);
        holder.send_woe.setVisibility(View.VISIBLE);

        holder.patientName.setText(myPojoWoe.get(position).getWoe().getPATIENT_NAME());
        holder.put_testName.setText(myPojoWoe.get(position).getWoe().getPRODUCT());


        holder.send_woe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNetworkAvailable((Activity) mContext)) {
                    GlobalClass.showAlertDialog((Activity) mContext);
                } else {
                    ArrayList<String> getBrcd = new ArrayList<>();
                    for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                        getBrcd.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
                    }
                    String getBarcodes = TextUtils.join(",", getBrcd);
                    DatabaseHelper db = new DatabaseHelper(mContext); //my database helper file
                    db.open();
                    Cursor c = db.selectData(getBarcodes); //function to retrieve all values from a table- written in MyDb.java file
                    c.moveToFirst();
                    if (c.moveToFirst()) {
                        do {
                            woejsontoPass = c.getString(0);
                        } while (c.moveToNext());
                    }

                    c.close();
                    db.close();

                    try {
                        Gson gson = new Gson();
                        myPojoWoeSend = new MyPojoWoe();
                        myPojoWoeSend = gson.fromJson(woejsontoPass, MyPojoWoe.class);

                    } catch (Exception e) {
                        Log.e(MY_DEBUG_TAG, "Error " + e.toString());
                    }
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(myPojoWoeSend);
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    barProgressDialog = new ProgressDialog(mContext);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    barProgressDialog.show();
                    POstQue = Volley.newRequestQueue(mContext);
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                String finalJson = response.toString();
                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                RES_ID = parentObjectOtp.getString("RES_ID");
                                barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                message = parentObjectOtp.getString("message");
                                status = parentObjectOtp.getString("status");
                                barcode_id = parentObjectOtp.getString("barcode_id");

                                if (message.equalsIgnoreCase("WORK ORDER ENTRY DONE SUCCESSFULLY")) {
                                    myDb = new DatabaseHelper(mContext);
                                    if (barcode_id.endsWith(",")) {
                                        barcode_id = barcode_id.substring(0, barcode_id.length() - 1);
                                    }

                                    sendTrf(position);
                                    boolean deletedRows = myDb.deleteData(barcode_id);
                                    if (deletedRows == true) {
                                        mfragment.setNewFragment();
                                        TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        boolean deletedRowsagain = myDb.deleteData(barcode_id);
                                        if (deletedRowsagain == true) {
                                            mfragment.setNewFragment();
                                            TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        } else {
                                            TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }
                                    }

                                } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {


                                    TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(
                                            mContext).create();

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Update Ledger !");

                                    // Setting Dialog Message
                                    alertDialog.setMessage(ToastFile.update_ledger);
                                    // Setting OK Button
                                    alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent i = new Intent(mContext, Payment_Activity.class);
                                            i.putExtra("COMEFROM", "Offline_Woe_Adapter");
                                            mContext.startActivity(i);
                                           /* Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                            httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                            mContext.startActivity(httpIntent);*/
                                            // Write your code here to execute after dialog closed
                                        }
                                    });
                                    alertDialog.show();

                                } else {
                                    holder.error_msg.setVisibility(View.VISIBLE);
                                    holder.error_msg.setText(message);
                                    TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            if (error != null) {
                            } else {

                                System.out.println(error);
                            }
                        }
                    });
                    jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                            150000,
                            3,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    POstQue.add(jsonObjectRequest1);
                    Log.e(TAG, "fetchData: URL" + jsonObjectRequest1);
                    Log.e(TAG, "fetchData: JSON" + jsonObj);
                }
            }
        });


        myDb = new DatabaseHelper(mContext);
        getSampleTypes = new ArrayList<>();
        getbarcode = new ArrayList<>();

        for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
            getSampleType_string = myPojoWoe.get(position).getBarcodelist().get(i).getSAMPLE_TYPE();
            getBarcode_string = myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE();

            getSampleTypes.add(myPojoWoe.get(position).getBarcodelist().get(i).getSAMPLE_TYPE());
            getbarcode.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
        }

        try {
            if (getSampleTypes.size() == 1) {
                holder.sample_type_txt.setVisibility(View.VISIBLE);
                holder.barcode_txt.setVisibility(View.VISIBLE);
                for (int i = 0; i < getSampleTypes.size(); i++) {
                    holder.sample_type_txt.setText(getSampleTypes.get(i));
                }
                for (int j = 0; j < getbarcode.size(); j++) {
                    holder.barcode_txt.setText(getbarcode.get(j));
                }
                holder.barcode_and_sample_recycler.setVisibility(View.GONE);

            } else {
                holder.sample_type_txt.setVisibility(View.GONE);
                holder.barcode_txt.setVisibility(View.GONE);
                holder.barcode_and_sample_recycler.setVisibility(View.VISIBLE);
                SampleTypeAdpater sampleRecyclerView = new SampleTypeAdpater(mContext, getSampleTypes, getbarcode);
                holder.barcode_and_sample_recycler.setAdapter(sampleRecyclerView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.image_tag.setVisibility(View.VISIBLE);

        holder.image_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> getBrcd = new ArrayList<>();
                for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                    getBrcd.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
                }
                String barcodes = TextUtils.join(",", getBrcd);
                DatabaseHelper db = new DatabaseHelper(mContext); //my database helper file
                db.open();
                Cursor c = db.selectData(barcodes); //function to retrieve all values from a table- written in MyDb.java file
                c.moveToFirst();
                if (c.moveToFirst()) {
                    do {
                        woejsontoPass = c.getString(0);
                    } while (c.moveToNext());
                }

                c.close();
                db.close();
                Intent i = new Intent(mContext, Offline_edt_activity.class);
                i.putExtra("WoeJson", woejsontoPass);
                mContext.startActivity(i);
            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> getBrcd = new ArrayList<>();
                for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                    getBrcd.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
                }
                String barcodes = TextUtils.join(",", getBrcd);
                DatabaseHelper db = new DatabaseHelper(mContext); //my database helper file
                db.open();
                Cursor c = db.selectData(barcodes); //function to retrieve all values from a table- written in MyDb.java file
                c.moveToFirst();
                if (c.moveToFirst()) {
                    do {
                        woejsontoPass = c.getString(0);
                    } while (c.moveToNext());
                }

                c.close();
                db.close();
                Intent i = new Intent(mContext, Offline_edt_activity.class);
                i.putExtra("WoeJson", woejsontoPass);
                mContext.startActivity(i);
            }
        });

        holder.deleteWoe.setOnClickListener(new View.OnClickListener() {
            public String patientBarcode;

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle(ToastFile.delete);
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                ArrayList<String> getBrcd = new ArrayList<>();
                                for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                                    getBrcd.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
                                }
                                String getBarcodes = TextUtils.join(",", getBrcd);

                                boolean deletedRows = myDb.deleteData(getBarcodes);
                                if (deletedRows == true) {
                                    mfragment.setNewFragment();
                                    TastyToast.makeText(mContext, ToastFile.woeDelete, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                } else {
                                    TastyToast.makeText(mContext, ToastFile.woeDeleteUnsuccess, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void sendTrf(int finalI) {
        trflist = new ArrayList<TRFModel>();
        try {
            JSONArray jsonArray = new JSONArray(myPojoWoe.get(finalI).getTrfjson());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TRFModel trfModel = new TRFModel();
                trfModel.setProduct(jsonObject.getString("Product"));
                String path = jsonObject.getString("trf_image");
                JSONObject jsonObject1 = new JSONObject(path);
                trfModel.setTrf_image(new File(jsonObject1.getString("path")));
                trflist.add(trfModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uploadTRf(trflist);
    }

    private void uploadTRf(ArrayList<TRFModel> trflist) {
        if (trflist.size() > 0)
            new AsyncTaskPost_uploadfile(offline_woe, activity1, api_key, user, barcode_patient_id, trflist).execute();
    }

    @Override
    public int getItemCount() {
        return myPojoWoe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName, sample_type_txt, barcode_txt;
        public TextView testName, error_msg;
        public TextView put_testName;
        public ImageView image_tag;
        public LinearLayout send_woe;
        public LinearLayout edt_offline_woe_ll;
        public RecyclerView barcode_and_sample_recycler;
        public LinearLayout linear, parent_ll_click;
        CardView card_view;
        public LinearLayoutManager linearLayoutManager;
        GridLayoutManager gridLayoutManager;
        public LinearLayout linear_summary_open;

        ImageView deleteWoe;

        public ViewHolder(View itemView) {
            super(itemView);
            patientName = (TextView) itemView.findViewById(R.id.patientName);
            sample_type_txt = (TextView) itemView.findViewById(R.id.sample_type_txt);
            barcode_txt = (TextView) itemView.findViewById(R.id.barcode_txt);
            testName = (TextView) itemView.findViewById(R.id.testName);
            error_msg = (TextView) itemView.findViewById(R.id.error_msg);

            put_testName = (TextView) itemView.findViewById(R.id.puttestName);
            send_woe = (LinearLayout) itemView.findViewById(R.id.send_woe);

            send_woe.setVisibility(View.VISIBLE);
            image_tag = (ImageView) itemView.findViewById(R.id.imageView);
            edt_offline_woe_ll = (LinearLayout) itemView.findViewById(R.id.edt_offline_woe_ll);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            parent_ll_click = (LinearLayout) itemView.findViewById(R.id.parent_ll_click);
            card_view = (CardView) itemView.findViewById(R.id.card_view);

//            linearLayoutManager = new LinearLayoutManager(context1);
            gridLayoutManager = new GridLayoutManager(mContext, 1);
            barcode_and_sample_recycler = (RecyclerView) itemView.findViewById(R.id.barcode_and_sample_recycler);
            barcode_and_sample_recycler.setLayoutManager(gridLayoutManager);
            linear_summary_open = (LinearLayout) itemView.findViewById(R.id.linear_summary_open);
            deleteWoe = (ImageView) itemView.findViewById(R.id.deleteWoe);
//            searchBarcode = new ArrayList<>();
        }
    }

    private class SampleTypeAdpater extends RecyclerView.Adapter<SampleTypeAdpater.ViewHolder> {

        Context context;
        ArrayList<String> sample_type;
        ArrayList<String> getbarcode;

        public SampleTypeAdpater(Context mContext, ArrayList<String> listOfSampleString, ArrayList<String> listOfBarcodeString) {
            this.context = mContext;
            this.sample_type = listOfSampleString;
            this.getbarcode = listOfBarcodeString;
        }

        @NonNull
        @Override
        public SampleTypeAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.sample_and_barcode, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SampleTypeAdpater.ViewHolder holder, int position) {
            holder.putbarcodeName.setText(getbarcode.get(position));
            holder.barcodeName.setText(sample_type.get(position));
        }

        @Override
        public int getItemCount() {
            return sample_type.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView putbarcodeName;
            public TextView barcodeName;


            public ViewHolder(View itemView) {
                super(itemView);
                putbarcodeName = (TextView) itemView.findViewById(R.id.putbarcodeName);
                barcodeName = (TextView) itemView.findViewById(R.id.barcodeName);
            }
        }
    }
}
