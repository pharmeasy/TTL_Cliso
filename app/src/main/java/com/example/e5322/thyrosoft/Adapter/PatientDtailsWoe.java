package com.example.e5322.thyrosoft.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Summary_Activity_WOE;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Woe_Edt_Activity;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientDtailsWoe extends RecyclerView.Adapter<PatientDtailsWoe.ViewHolder> {
    private ArrayList<Patients> patients;
    private ArrayList<Patients> searchBarcode;
    String user, passwrd, genderId, access, api_key, error, pid, response1, barcodes, resID, saveAgeType, getBtechName;
    Context context1;
    ProgressDialog barProgressDialog;
    public static com.android.volley.RequestQueue deletePatienDetail;
    Summary_model summary_model;
    int getcount = 0;
    RequestQueue requestQueuePatientDetails;
    SharedPreferences prefs;
    String patientId;
    private String countData;
    boolean flag = false;
    ArrayList<String> getNoStatus = new ArrayList<>();
    private int flagpass = 0;
    private String TAG = PatientDtailsWoe.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView patientName, tv_brandlabel, tv_brand;
        public TextView testName;
        public TextView put_testName;
        public ImageView image_tag;
        public RecyclerView barcode_and_sample_recycler;
        public LinearLayout linear;
        public LinearLayoutManager linearLayoutManager;

        public LinearLayout linear_summary_open;
        ImageView deleteWoe;

        public ViewHolder(View v) {
            super(v);
            patientName = (TextView) v.findViewById(R.id.patientName);
            testName = (TextView) v.findViewById(R.id.testName);
            put_testName = (TextView) v.findViewById(R.id.puttestName);
            tv_brandlabel = (TextView) v.findViewById(R.id.tv_brandlabel);
            tv_brand = (TextView) v.findViewById(R.id.tv_brand);
            image_tag = (ImageView) v.findViewById(R.id.imageView);
            linear = (LinearLayout) v.findViewById(R.id.linear);
            linearLayoutManager = new LinearLayoutManager(context1);
            //gridLayoutManager = new GridLayoutManager(context1, 2);
            barcode_and_sample_recycler = (RecyclerView) v.findViewById(R.id.barcode_and_sample_recycler);
            barcode_and_sample_recycler.setLayoutManager(linearLayoutManager);
            linear_summary_open = (LinearLayout) v.findViewById(R.id.linear_summary_open);
            deleteWoe = (ImageView) v.findViewById(R.id.deleteWoe);
            searchBarcode = new ArrayList<>();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PatientDtailsWoe(Context context, ArrayList<Patients> patientsArray) {
        this.patients = patientsArray;
        this.context1 = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PatientDtailsWoe.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_ll_woe_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        summary_model = new Summary_model();

        holder.linear_summary_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagpass = 1;
                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.summary_models = new ArrayList<>();
                getPatientDetails();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();
            }
        });
//            int getCount = patients.get(position).getIsOrder().equals("NO");
        GlobalClass.setWindUpCount = String.valueOf(getcount);
        holder.image_tag.setVisibility(View.GONE);

        if (patients.get(position).getConfirm_status().equals("NO")) {
            holder.linear.setBackgroundResource(R.color.light_pink);
            holder.deleteWoe.setVisibility(View.VISIBLE);
            getNoStatus.add(patients.get(position).getName());
        } else {
            holder.linear.setBackgroundResource(R.color.white);
            holder.deleteWoe.setVisibility(View.GONE);
        }
        int getCount = getNoStatus.size();
        countData = String.valueOf(getCount);
        GlobalClass.windupCountDataToShow = countData;

        prefs = context1.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        if (!GlobalClass.isNull(patients.get(position).getCategory())) {
            holder.tv_brandlabel.setVisibility(View.VISIBLE);
            holder.tv_brand.setVisibility(View.VISIBLE);
            holder.tv_brand.setText(patients.get(position).getCategory());
            if (patients.get(position).getCategory().equalsIgnoreCase("BN")) {
                holder.tv_brand.setTextColor(Color.RED);
                holder.tv_brand.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                holder.tv_brand.setTextColor(Color.BLACK);
                holder.tv_brand.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

        } else {
            holder.tv_brandlabel.setVisibility(View.GONE);
            holder.tv_brand.setVisibility(View.GONE);
        }
        holder.patientName.setText("" + patients.get(position).getName());

        if (!GlobalClass.isNull(patients.get(position).getLocation())) {
            if (patients.get(position).getLocation().equalsIgnoreCase("CPL")) {
                holder.patientName.setTextColor(context1.getResources().getColor(R.color.colorred));
            } else if (patients.get(position).getLocation().equalsIgnoreCase("ZPL")) {
                holder.patientName.setTextColor(context1.getResources().getColor(R.color.tabindicatorColor));
            } else if (patients.get(position).getLocation().equalsIgnoreCase("RPL")) {
                holder.patientName.setTextColor(context1.getResources().getColor(R.color.colorPrimary));
            }

        }


        holder.put_testName.setText(patients.get(position).getTests());


        ArrayList<String> getSampleTypes = new ArrayList<>();
        ArrayList<String> getbarcode = new ArrayList<>();

        String getSampleType_string = patients.get(position).getSample_type().toString();
        String getBarcode_string = patients.get(position).getBarcode().toString();

        getSampleTypes.add(patients.get(position).getSample_type());
        getbarcode.add(patients.get(position).getBarcode());

        String[] samples = getSampleType_string.split(",");
        String[] barcodes = getBarcode_string.split(",");


        List<String> fixedSampleList = Arrays.asList(samples);
        List<String> fixedBarcodeList = Arrays.asList(barcodes);

        ArrayList<String> listOfSampleString = new ArrayList<String>(fixedSampleList);
        ArrayList<String> listOfBarcodeString = new ArrayList<String>(fixedBarcodeList);

       /* if (listOfSampleString.size() < 2) {
            gridLayoutManager = new GridLayoutManager(context1, 1);
        } else {
            gridLayoutManager = new GridLayoutManager(context1, 2);
        }*/

        //holder.barcode_and_sample_recycler.setLayoutManager(linearLayoutManager);
        SampleTypeAdpaterWithBarcode outLabRecyclerView = new SampleTypeAdpaterWithBarcode(context1, listOfSampleString, listOfBarcodeString);
        holder.barcode_and_sample_recycler.setAdapter(outLabRecyclerView);

        if (patients.get(position).getIsOrder().equalsIgnoreCase("YES")) {
            holder.image_tag.setVisibility(View.GONE);
        } else {
            //holder.image_tag.setVisibility(View.VISIBLE);
            for (int i = 0; i < listOfSampleString.size(); i++) {
                if (listOfSampleString.get(i).equalsIgnoreCase("WATER")) {
                    holder.image_tag.setVisibility(View.GONE);
                } else {
                    holder.image_tag.setVisibility(View.VISIBLE);
                }

            }
        }
        holder.image_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagpass = 2;
                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.summary_models = new ArrayList<>();
                getPatientDetails();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();
            }
        });
        holder.deleteWoe.setOnClickListener(new View.OnClickListener() {
            public String patientBarcode;

            @Override
            public void onClick(View v) {

                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.summary_models = new ArrayList<>();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
                alertDialogBuilder.setTitle("Confirm delete !");
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deletePatientDetailsandTest();
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

    private void deletePatientDetailsandTest() {
        barProgressDialog = new ProgressDialog(context1);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        deletePatienDetail = GlobalClass.setVolleyReq(context1);
        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", GlobalClass.getPatientIdforDeleteDetails);
            jsonObjectOtp.put("tsp", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    error = parentObjectOtp.getString("error");
                    pid = parentObjectOtp.getString("pid");
                    response1 = parentObjectOtp.getString("response");
                    barcodes = parentObjectOtp.getString("barcodes");
                    resID = parentObjectOtp.getString("RES_ID");

                    if (resID.equals("RES0000")) {
                        Constants.covidwoe_flag = "1";
                        TastyToast.makeText(context1, ToastFile.woe_dlt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //Intent intent = new Intent(context1, HomeMenuActivity.class);
                        GlobalClass.setFlagBackToWoe = true;
                      //  context1.startActivity(intent);
                        GlobalClass.redirectToPreviousPosition(context1,Constants.NOVID_WOE_MENU_POS);


                    } else {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        TastyToast.makeText(context1, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(context1, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    System.out.println(error);
                }
            }
        });
        deletePatienDetail.add(jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);

    }

    private void getPatientDetails() {
        requestQueuePatientDetails = GlobalClass.setVolleyReq(context1);//2c=/TAM03/TAM03136166236000078/geteditdata
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + "" + api_key + "/" + "" + user + "/" + "" + patientId + "/" + "geteditdata", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response" + response);
                Gson gson = new Gson();
                summary_model = gson.fromJson(response.toString(), Summary_model.class);
                GlobalClass.summary_models.add(summary_model);

                try {
                    if (!GlobalClass.isNull(summary_model.getRES_ID())) {
                        if (summary_model.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                            if (flagpass == 1) {
                                Intent intent = new Intent(context1, Summary_Activity_WOE.class);
                                context1.startActivity(intent);
                            } else if (flagpass == 2) {
                                Intent i = new Intent(context1, Woe_Edt_Activity.class);
                                context1.startActivity(i);
                            }
                        } else {
                            TastyToast.makeText(context1, "" + summary_model.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(context1, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuePatientDetails.add(jsonObjectRequestPop);
        Log.e(TAG, "getPatientDetails: URL" + jsonObjectRequestPop);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void filterList(ArrayList<Patients> filterdNames) {
        this.searchBarcode = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private class SampleTypeAdpaterWithBarcode extends RecyclerView.Adapter<SampleTypeAdpaterWithBarcode.ViewHolder> {

        Context context;
        ArrayList<String> sample_type;
        ArrayList<String> getbarcode;

        public SampleTypeAdpaterWithBarcode(Context context1, ArrayList<String> getSampleTypes, ArrayList<String> getbarcode) {
            this.context = context1;
            this.sample_type = getSampleTypes;
            this.getbarcode = getbarcode;
        }

        @NonNull
        @Override
        public SampleTypeAdpaterWithBarcode.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.sample_and_barcode, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SampleTypeAdpaterWithBarcode.ViewHolder holder, int position) {

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
   /* public void redirectToActivity(int position) {
        Intent intent = new Intent(context1,HomeMenuActivity.class);
        intent.putExtra("position",position);
        context1.startActivity(intent);
    }*/
}