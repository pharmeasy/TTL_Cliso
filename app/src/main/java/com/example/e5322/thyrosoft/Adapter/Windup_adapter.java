package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.CountInterface;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Summary_Activity_WOE;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Windup_adapter extends RecyclerView.Adapter<Windup_adapter.ViewHolder> {

    private final CountInterface countInterface;
    private ArrayList<Patients> patients;
    private ArrayList<Patients> searchBarcode;

    Context context1;
    Summary_model summary_model;
    int getcount = 0;
    RequestQueue requestQueuePatientDetails;
    SharedPreferences prefs;
    String user, passwrd, access, api_key, patientId;
    private String countData;
    boolean flag = false;
    ArrayList<String> getNoStatus = new ArrayList<>();
    private String TAG = Windup_adapter.class.getSimpleName().toString();
    int clickcount = 0;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView patientName;
        public TextView testName;

        public TextView puttestName, show_amt_txt;

        public ImageView image_tag;
        public CardView linear;
        public RecyclerView barcode_and_sample_recycler;
        public LinearLayoutManager linearLayoutManager;
        GridLayoutManager gridLayoutManager;
        ImageView checkbox, iv_checked_img;

        public ViewHolder(View v) {
            super(v);
            patientName = (TextView) v.findViewById(R.id.patientName);
            testName = (TextView) v.findViewById(R.id.testName);
            puttestName = (TextView) v.findViewById(R.id.puttestName);
            show_amt_txt = (TextView) v.findViewById(R.id.show_amt_txt);
            image_tag = (ImageView) v.findViewById(R.id.imageView);
            linear = (CardView) v.findViewById(R.id.linear);
            linearLayoutManager = new LinearLayoutManager(context1);
            gridLayoutManager = new GridLayoutManager(context1, 2);
            barcode_and_sample_recycler = (RecyclerView) v.findViewById(R.id.barcode_and_sample_recycler);
            barcode_and_sample_recycler.setLayoutManager(gridLayoutManager);
            checkbox = (ImageView) v.findViewById(R.id.iv_checked);
            iv_checked_img = (ImageView) v.findViewById(R.id.iv_checked_img);
            searchBarcode = new ArrayList<>();
            //iv_checked_img.setVisibility(View.GONE);
            //checkbox.setVisibility(View.VISIBLE);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Windup_adapter(Context context, ArrayList<Patients> patientsArray, CountInterface countInterface) {
        this.patients = patientsArray;
        this.context1 = context;
        this.countInterface = countInterface;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Windup_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_layout_windup, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        summary_model = new Summary_model();
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.summary_models = new ArrayList<>();
//                getPatientDetails();
//                GlobalClass.getPatientIdforDeleteDetails = patients.get(position).getPatient_id().toString();
            }
        });
//            int getCount = patients.get(position).getIsOrder().equals("NO");
        GlobalClass.setWindUpCount = String.valueOf(getcount);
        holder.image_tag.setVisibility(View.GONE);

        if (patients.get(position).getConfirm_status().equals("NO")) {
         //   holder.checkbox.setVisibility(View.VISIBLE);
            //holder.iv_checked_img.setVisibility(View.GONE);
            holder.linear.setBackgroundResource(R.color.light_pink);
            getNoStatus.add(patients.get(position).getName());
        } else {
            holder.checkbox.setVisibility(View.GONE);
            holder.iv_checked_img.setVisibility(View.GONE);
            holder.linear.setBackgroundResource(R.color.white);
        }
        int getCount = getNoStatus.size();
        countData = String.valueOf(getCount);
        GlobalClass.windupCountDataToShow = countData;

        prefs = context1.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        holder.patientName.setText(patients.get(position).getName());
        holder.puttestName.setText(patients.get(position).getTests());
        holder.show_amt_txt.setText(patients.get(position).getB2B());


        holder.image_tag.setVisibility(View.VISIBLE);

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


        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            public String patientBarcode;

            @Override
            public void onClick(View v) {
                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.windupBarcodeList.add(patientId);
                clickcount = clickcount + 1;
                patients.get(position).flag = false;
                holder.iv_checked_img.setVisibility(View.VISIBLE);
                holder.checkbox.setVisibility(View.GONE);
                countInterface.getclickcount(clickcount);

            }
        });
        holder.iv_checked_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientId = patients.get(position).getPatient_id().toString();
                GlobalClass.windupBarcodeList.remove(patientId);
                patients.get(position).flag = true;
                clickcount = clickcount - 1;
                holder.iv_checked_img.setVisibility(View.GONE);
                holder.checkbox.setVisibility(View.VISIBLE);
                countInterface.getclickcount(clickcount);
            }
        });
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
                String getResponse = summary_model.getResponse();
                if (getResponse.equals("FAILURE")) {

                } else {
                    Intent intent = new Intent(context1, Summary_Activity_WOE.class);
                    context1.startActivity(intent);
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

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}