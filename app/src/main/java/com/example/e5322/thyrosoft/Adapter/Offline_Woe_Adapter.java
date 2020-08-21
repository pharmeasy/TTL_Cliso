package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.WoeController;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.GlobalClass;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class Offline_Woe_Adapter extends RecyclerView.Adapter<Offline_Woe_Adapter.ViewHolder> {
    Context mContext;
    DatabaseHelper myDb;
    Summary_model summary_model;
    ArrayList<MyPojoWoe> myPojoWoe;
    private String getBarcode_string;
    private String getSampleType_string;
    Offline_woe mfragment;
    SharedPreferences prefs;
    public ArrayList<Summary_model> summary_models;

    RefreshOfflineWoe refreshOfflineWoe;
    private MyPojoWoe myPojoWoeSend;
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
    private RequestQueue POstQue;
    private RequestQueue sendGPSDetails;
    String user, passwrd, access, api_key;
    private String getIMEINUMBER;
    private String mobileModel;
    private static ManagingTabsActivity activity;
    ArrayList<String> setResponse;
    ArrayList<TRFModel> trflist = new ArrayList<>();
    Offline_woe offline_woe;
    Activity activity1;
    int Finalpos;
    private TextView error_msg;


    public Offline_Woe_Adapter(ManagingTabsActivity mContext, ArrayList<MyPojoWoe> allWoe, Offline_woe fragment, ArrayList<String> setResponse, Offline_woe offline_woe, Activity activity) {
        this.mContext = mContext;
        this.myPojoWoe = allWoe;
        this.mfragment = fragment;
        this.setResponse = setResponse;
        this.offline_woe = offline_woe;
        this.activity1 = activity;
    }

    public void onClickDeleteOffWoe(RefreshOfflineWoe refreshOfflineWoe) {
        this.refreshOfflineWoe = refreshOfflineWoe;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_layout_woe_patient_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        summary_model = new Summary_model();

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        final SharedPreferences getIMIE = mContext.getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", "");
        SharedPreferences getModelNumber = mContext.getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", "");

        holder.linear_summary_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary_models = new ArrayList<>();
            }
        });

        if (GlobalClass.CheckArrayList(setResponse) &&  !GlobalClass.isNull(setResponse.get(position))) {
            holder.error_msg.setVisibility(View.VISIBLE);
            GlobalClass.SetText(holder.error_msg, setResponse.get(position));

        }

        holder.image_tag.setVisibility(View.GONE);
        holder.send_woe.setVisibility(View.VISIBLE);

        GlobalClass.SetText(holder.patientName, myPojoWoe.get(position).getWoe().getPATIENT_NAME());
        GlobalClass.SetText(holder.put_testName, myPojoWoe.get(position).getWoe().getPRODUCT());


        holder.send_woe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNetworkAvailable((Activity) mContext)) {
                    GlobalClass.showAlertDialog((Activity) mContext);
                } else {
                    ArrayList<String> getBrcd = new ArrayList<>();
                    if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.get(position).getBarcodelist())) {
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
                    }


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


                    POstQue = GlobalClass.setVolleyReq(mContext);
                    Finalpos = position;
                    error_msg = holder.error_msg;
                    try {
                        if (ControllersGlobalInitialiser.woeController != null) {
                            ControllersGlobalInitialiser.woeController = null;
                        }
                        ControllersGlobalInitialiser.woeController = new WoeController(activity, Offline_Woe_Adapter.this);
                        ControllersGlobalInitialiser.woeController.woeDoneController(jsonObj, POstQue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        myDb = new DatabaseHelper(mContext);
        getSampleTypes = new ArrayList<>();
        getbarcode = new ArrayList<>();

        if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.get(position).getBarcodelist())) {

            for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                getSampleType_string = myPojoWoe.get(position).getBarcodelist().get(i).getSAMPLE_TYPE();
                getBarcode_string = myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE();
                getSampleTypes.add(myPojoWoe.get(position).getBarcodelist().get(i).getSAMPLE_TYPE());
                getbarcode.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
            }
        }


        try {
            if (GlobalClass.CheckArrayList(getSampleTypes) && getSampleTypes.size() == 1) {
                holder.sample_type_txt.setVisibility(View.VISIBLE);
                holder.barcode_txt.setVisibility(View.VISIBLE);
                for (int i = 0; i < getSampleTypes.size(); i++) {
                    GlobalClass.SetText(holder.sample_type_txt, getSampleTypes.get(i));
                }

                if (GlobalClass.CheckArrayList(getbarcode)){
                    for (int j = 0; j < getbarcode.size(); j++) {
                        GlobalClass.SetText(holder.barcode_txt, getbarcode.get(j));
                    }
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

                if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.get(position).getBarcodelist())) {
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
                }


                Intent i = new Intent(mContext, Offline_edt_activity.class);
                i.putExtra("WoeJson", woejsontoPass);
                mContext.startActivity(i);
            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> getBrcd = new ArrayList<>();

                if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.get(position).getBarcodelist())) {
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
                }

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
                alertDialogBuilder.setPositiveButton(MessageConstants.YES,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                ArrayList<String> getBrcd = new ArrayList<>();

                                if (myPojoWoe != null && GlobalClass.CheckArrayList(myPojoWoe.get(position).getBarcodelist())) {
                                    for (int i = 0; i < myPojoWoe.get(position).getBarcodelist().size(); i++) {
                                        getBrcd.add(myPojoWoe.get(position).getBarcodelist().get(i).getBARCODE());
                                    }
                                    String getBarcodes = TextUtils.join(",", getBrcd);

                                    boolean deletedRows = myDb.deleteData(getBarcodes);
                                    if (deletedRows) {
                                        mfragment.setNewFragment();
                                        GlobalClass.showTastyToast(activity, ToastFile.woeDelete, 1);

                                    } else {
                                        GlobalClass.showTastyToast(activity, ToastFile.woeDeleteUnsuccess, 2);
                                    }

                                }

                            }
                        });

                alertDialogBuilder.setNegativeButton(MessageConstants.NO, new DialogInterface.OnClickListener() {
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
        if (GlobalClass.CheckArrayList(trflist))
            new AsyncTaskPost_uploadfile(offline_woe, activity1, api_key, user, barcode_patient_id, trflist).execute();
    }

    @Override
    public int getItemCount() {
        return myPojoWoe.size();
    }

    public void getWoeResponse(JSONObject response) {
        try {

            Log.e(TAG, "onResponse: RESPONSE" + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            RES_ID = parentObjectOtp.getString("RES_ID");
            barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
            message = parentObjectOtp.getString("message");
            status = parentObjectOtp.getString("status");
            barcode_id = parentObjectOtp.getString("barcode_id");

            if (!GlobalClass.isNull(message) && message.equalsIgnoreCase(MessageConstants.WOE_SUCCESS)) {
                myDb = new DatabaseHelper(mContext);
                if (!GlobalClass.isNull(barcode_id) && barcode_id.endsWith(",")) {
                    barcode_id = barcode_id.substring(0, barcode_id.length() - 1);
                }


                sendTrf(Finalpos);
                boolean deletedRows = myDb.deleteData(barcode_id);
                if (deletedRows) {
                    mfragment.setNewFragment();
                    GlobalClass.showTastyToast(activity, message, 1);
                } else {
                    boolean deletedRowsagain = myDb.deleteData(barcode_id);
                    if (deletedRowsagain) {
                        mfragment.setNewFragment();
                        GlobalClass.showTastyToast(activity, message, 1);
                    } else {
                        GlobalClass.showTastyToast(activity, "", 2);
                    }
                }

            } else if (!GlobalClass.isNull(message) && message.equals(MessageConstants.CRDIT_LIMIT)) {

                GlobalClass.showTastyToast(activity, message, 2);

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        mContext).create();

                alertDialog.setTitle(ToastFile.updateledger);
                alertDialog.setMessage(ToastFile.update_ledger);
                alertDialog.setButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(mContext, Payment_Activity.class);
                        i.putExtra("COMEFROM", "Offline_Woe_Adapter");
                        mContext.startActivity(i);

                    }
                });
                alertDialog.show();

            } else {
                error_msg.setVisibility(View.VISIBLE);
                GlobalClass.SetText(error_msg, message);
                GlobalClass.showTastyToast(activity, message, 2);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
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

            gridLayoutManager = new GridLayoutManager(mContext, 1);
            barcode_and_sample_recycler = (RecyclerView) itemView.findViewById(R.id.barcode_and_sample_recycler);
            barcode_and_sample_recycler.setLayoutManager(gridLayoutManager);
            linear_summary_open = (LinearLayout) itemView.findViewById(R.id.linear_summary_open);
            deleteWoe = (ImageView) itemView.findViewById(R.id.deleteWoe);

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

            GlobalClass.SetText(holder.putbarcodeName, getbarcode.get(position));
            GlobalClass.SetText(holder.barcodeName, sample_type.get(position));
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
