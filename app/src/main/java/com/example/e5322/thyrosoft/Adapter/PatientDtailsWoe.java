package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.DeleteWoe_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.PatientDetail_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Summary_Activity_WOE;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Woe_Edt_Activity;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class PatientDtailsWoe extends RecyclerView.Adapter<PatientDtailsWoe.ViewHolder> {
    private ArrayList<Patients> patients;
    private ArrayList<Patients> searchBarcode;
    String user, passwrd, access, api_key, error, pid, response1, barcodes, resID, saveAgeType, getBtechName;
    public ArrayList<Summary_model> summary_models;
    Context context1;
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


    public void getdeletewoe(JSONObject response) {
        try {
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            error = parentObjectOtp.getString("error");
            pid = parentObjectOtp.getString("pid");
            response1 = parentObjectOtp.getString("response");
            barcodes = parentObjectOtp.getString("barcodes");
            resID = parentObjectOtp.getString("RES_ID");


            if (!GlobalClass.isNull(resID) && resID.equalsIgnoreCase(Constants.RES0000)) {
                Constants.covidwoe_flag = "1";
                GlobalClass.showTastyToast((Activity) context1, ToastFile.woe_dlt, 1);
                Intent intent = new Intent(context1, ManagingTabsActivity.class);
                GlobalClass.setFlagBackToWoe = true;
                context1.startActivity(intent);
            } else {
                GlobalClass.showTastyToast((Activity) context1, response1, 2);
            }

        } catch (JSONException e) {
            GlobalClass.showTastyToast((Activity) context1, response1, 2);
            e.printStackTrace();
        }
    }

    public void getpatientdetailsResponse(JSONObject response) {
        Log.e(TAG, "onResponse: response" + response);
        Gson gson = new Gson();
        summary_model = gson.fromJson(response.toString(), Summary_model.class);
        summary_models.add(summary_model);
        String getResponse = summary_model.getResponse();


        try {
            if (!GlobalClass.isNull(getResponse) && getResponse.equals("FAILURE")) {
                Log.v("TAG", TAG + "error in geteditdata");
            } else {
                if (flagpass == 1) {
                    Intent intent = new Intent(context1, Summary_Activity_WOE.class);
                    intent.putParcelableArrayListExtra("summary_models", summary_models);
                    context1.startActivity(intent);
                } else if (flagpass == 2) {
                    Intent intent = new Intent(context1, Woe_Edt_Activity.class);
                    intent.putParcelableArrayListExtra("summary_models", summary_models);
                    context1.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView patientName;
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
            image_tag = (ImageView) v.findViewById(R.id.imageView);
            linear = (LinearLayout) v.findViewById(R.id.linear);
            linearLayoutManager = new LinearLayoutManager(context1);
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
                summary_models = new ArrayList<>();
                getPatientDetails();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();
            }
        });

        GlobalClass.setWindUpCount = String.valueOf(getcount);
        holder.image_tag.setVisibility(View.GONE);

        if (!GlobalClass.isNull(patients.get(position).getConfirm_status()) &&
                patients.get(position).getConfirm_status().equalsIgnoreCase("NO")) {
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


        GlobalClass.SetText(holder.patientName, patients.get(position).getName());
        GlobalClass.SetText(holder.put_testName, patients.get(position).getTests());


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


        SampleTypeAdpaterWithBarcode outLabRecyclerView = new SampleTypeAdpaterWithBarcode(context1, listOfSampleString, listOfBarcodeString);
        holder.barcode_and_sample_recycler.setAdapter(outLabRecyclerView);

        if (!GlobalClass.isNull(patients.get(position).getIsOrder()) &&
                patients.get(position).getIsOrder().equalsIgnoreCase(MessageConstants.YES)) {
            holder.image_tag.setVisibility(View.GONE);
        } else {

            if (GlobalClass.CheckArrayList(listOfSampleString)) {
                for (int i = 0; i < listOfSampleString.size(); i++) {
                    if (!GlobalClass.isNull(listOfSampleString.get(i))&&
                            listOfSampleString.get(i).equalsIgnoreCase("WATER")) {
                        holder.image_tag.setVisibility(View.GONE);
                    } else {
                        holder.image_tag.setVisibility(View.VISIBLE);
                    }

                }
            }

        }
        holder.image_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagpass = 2;
                patientId = patients.get(position).getPatient_id().toString();
                summary_models = new ArrayList<>();
                getPatientDetails();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();
            }
        });
        holder.deleteWoe.setOnClickListener(new View.OnClickListener() {
            public String patientBarcode;

            @Override
            public void onClick(View v) {

                patientId = patients.get(position).getPatient_id();
                summary_models = new ArrayList<>();
                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
                alertDialogBuilder.setTitle(MessageConstants.CONFIRM_DT);
                alertDialogBuilder.setMessage(ToastFile.wish_woe_dlt);
                alertDialogBuilder.setPositiveButton(MessageConstants.YES,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deletePatientDetailsandTest();
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

    private void deletePatientDetailsandTest() {
        deletePatienDetail = GlobalClass.setVolleyReq(context1);
        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("Patient_id", GlobalClass.getPatientIdforDeleteDetails);
            jsonObjectOtp.put("tsp", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if (ControllersGlobalInitialiser.deleteWoe_controller != null) {
                ControllersGlobalInitialiser.deleteWoe_controller = null;
            }
            ControllersGlobalInitialiser.deleteWoe_controller = new DeleteWoe_Controller((Activity) context1, PatientDtailsWoe.this);
            ControllersGlobalInitialiser.deleteWoe_controller.deletewoe(jsonObjectOtp, deletePatienDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPatientDetails() {
        requestQueuePatientDetails = GlobalClass.setVolleyReq(context1);//2c=/TAM03/TAM03136166236000078/geteditdata

        try {
            if (ControllersGlobalInitialiser.patientDtailsWoe != null) {
                ControllersGlobalInitialiser.patientDtailsWoe = null;
            }
            ControllersGlobalInitialiser.patientDtailsWoe = new PatientDetail_Controller((Activity) context1, PatientDtailsWoe.this);
            ControllersGlobalInitialiser.patientDtailsWoe.getpatienrdetail_controller(api_key, user, patientId, requestQueuePatientDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return patients.size();
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