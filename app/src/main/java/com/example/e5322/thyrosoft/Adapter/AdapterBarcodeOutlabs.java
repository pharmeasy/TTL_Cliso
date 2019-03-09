package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Interface.SendScanBarcodeDetails;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AdapterBarcodeOutlabs extends RecyclerView.Adapter<AdapterBarcodeOutlabs.ViewHolder> implements SendScanBarcodeDetails {

    Scan_Barcode_Outlabs context;
    ArrayList<Outlabdetails_OutLab> selctedProductArraylist;
    RecyclerInterface listener;
    SharedPreferences prefs;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, response1, previouseBarcode, afterBarcode;
    RequestQueue barcodeDetails;
    ArrayList<Integer> getPosition;
    ArrayList<ScannedBarcodeDetails> distinctspecimentlist;
    private OnItemClickListener onItemClickListener;
    private String searchBarcode;
    private String storeResponse;
    ProgressDialog progressDialog;

    public AdapterBarcodeOutlabs(Scan_Barcode_Outlabs scan_barcode_outlabs, ArrayList<Outlabdetails_OutLab> selectedlist, ArrayList<ScannedBarcodeDetails> distinctspecimentlist, RecyclerInterface listener) {
        this.context = scan_barcode_outlabs;
        this.selctedProductArraylist = selectedlist;
        this.listener = listener;
        this.distinctspecimentlist = distinctspecimentlist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scanBarcode;
        public EditText enter_barcode, reenter;
        public ImageView img_edt, setback;

        //        public ImageView setback;
        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            scanBarcode = (TextView) itemView.findViewById(R.id.serum);
            img_edt = (ImageView) itemView.findViewById(R.id.img_edt);
            setback = (ImageView) itemView.findViewById(R.id.setback);
            linearEditbarcode = (LinearLayout) itemView.findViewById(R.id.lineareditbarcode);
            barcode_linear = (LinearLayout) itemView.findViewById(R.id.barcode_linear);
//            setback=(ImageView)itemView.findViewById(R.id.setback);
            enter_barcode = (EditText) itemView.findViewById(R.id.enter_barcode);
            reenter = (EditText) itemView.findViewById(R.id.reenter);
            MyTextWatcher textWatcher = new MyTextWatcher(enter_barcode);
            enter_barcode.addTextChangedListener(textWatcher);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AdapterBarcodeOutlabs.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_barcode_list, parent, false);
        AdapterBarcodeOutlabs.ViewHolder vh = new AdapterBarcodeOutlabs.ViewHolder(v);
        return vh;
    }

    @Override
    public void barcodeDetails(Context v, String s) {
        GlobalClass.getscannedData = s;
    }

    @Override
    public void onBindViewHolder(final AdapterBarcodeOutlabs.ViewHolder holder, int position) {

        prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        holder.reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(context,
                            ToastFile.crt_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        holder.reenter.setText(enteredString.substring(1));
                    } else {
                        holder.reenter.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 8) {
                    afterBarcode = s.toString();
                    searchBarcode = s.toString();
                    if (previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        checkBarcode();
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        holder.scanBarcode.setText("Barcode" + " : " + searchBarcode);

                    } else {
                        Toast.makeText(context, ToastFile.brcd_not_match, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    TastyToast.makeText(context, ToastFile.ent_brcd, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }

            }
        });

        holder.setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.GONE);
                holder.barcode_linear.setVisibility(View.VISIBLE);
            }
        });

        if (GlobalClass.finalspecimenttypewiselist.get(position).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(position).getBarcode().isEmpty()) {

            searchBarcode = GlobalClass.finalspecimenttypewiselist.get(position).getBarcode();
            checkBarcode();

            holder.scanBarcode.setText("Barcode" + " : " + searchBarcode);

        } else {
            holder.scanBarcode.setText("Barcode");
        }
        System.out.println("Barcode :::: " + GlobalClass.finalspecimenttypewiselist.get(position).getBarcode());
        holder.img_edt.setVisibility(View.VISIBLE);
        holder.linearEditbarcode.setVisibility(View.GONE);

        holder.scanBarcode.setOnClickListener(onScanbarcodeClickListener);
        holder.scanBarcode.setTag(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type());

        holder.img_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBarcode != null) {
                    holder.enter_barcode.setText(searchBarcode);
                    holder.reenter.setText(searchBarcode);
                }
                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);
            }
        });


    }

    private void checkBarcode() {
        barcodeDetails = Volley.newRequestQueue(context);//2c=/TAM03/TAM03136166236000078/geteditdata
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkBarcode + api_key + "/" + searchBarcode + "/getcheckbarcode"
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("barcode respponse" + response);

                String finalJson = response.toString();
                JSONObject parentObjectOtp = null;
                try {
                    parentObjectOtp = new JSONObject(finalJson);
                    ERROR = parentObjectOtp.getString("ERROR");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    barcode = parentObjectOtp.getString("barcode");
                    response1 = parentObjectOtp.getString("response");

                    if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                        storeResponse = response1;
                    } else {
                        storeResponse = response1;
                        Toast.makeText(context, "" + response1, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
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
        barcodeDetails.add(jsonObjectRequestPop);

    }

    View.OnClickListener onScanbarcodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Specimenttype = (String) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onScanbarcodeClickListener(Specimenttype, context);
            }
        }
    };

    @Override
    public int getItemCount() {
        return GlobalClass.finalspecimenttypewiselist.size();
    }

    public interface OnItemClickListener {
        void onScanbarcodeClickListener(String Specimenttype, Activity activity);

    }

    class MyTextWatcher implements TextWatcher {
        private EditText enter_barcode, reenter;
        LinearLayout linearEditbarcode, barcode_linear;
        Button scanBarcode;
        boolean flag = false;
        private String TAG = AdapterBarcodeOutlabs.class.getSimpleName().toString();

        public MyTextWatcher(EditText editText) {
            this.enter_barcode = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (int) enter_barcode.getTag();

            String enteredString = s.toString();
            if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                Toast.makeText(context,
                        ToastFile.crt_brcd,
                        Toast.LENGTH_SHORT).show();
                if (enteredString.length() > 0) {
                    enter_barcode.setText(enteredString.substring(1));
                } else {
                    enter_barcode.setText("");
                }
            }

            // Do whatever you want with position
        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) enter_barcode.getTag();

            if (s.length() == 8) {
                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                //checkBarcode(position,s.toString());

                if (flag == false) {
                    flag = true;
                    barcodeDetails = Volley.newRequestQueue(context);//2c=/TAM03/TAM03136166236000078/geteditdata
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("Kindly wait ...");
                    progressDialog.setMessage(ToastFile.processing_request);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(20);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkBarcode + api_key + "/" + s + "/getcheckbarcode"
                            , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("barcode respponse" + response);

                            Log.e(TAG, "onResponse: response" + response);
                            String finalJson = response.toString();
                            JSONObject parentObjectOtp = null;
                            try {
                                parentObjectOtp = new JSONObject(finalJson);
                                ERROR = parentObjectOtp.getString("ERROR");
                                RES_ID = parentObjectOtp.getString("RES_ID");
                                barcode = parentObjectOtp.getString("barcode");
                                response1 = parentObjectOtp.getString("response");
                                if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                                    enter_barcode.setText(searchBarcode);
                                    progressDialog.dismiss();
                                } else {
                                    enter_barcode.setText("");
                                    storeResponse = response1;
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "" + response1, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
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
                    barcodeDetails.add(jsonObjectRequestPop);
                    Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
                } else {

                }


            } else {
            }

        }
    }

}


