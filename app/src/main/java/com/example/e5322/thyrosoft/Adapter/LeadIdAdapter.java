package com.example.e5322.thyrosoft.Adapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SetBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.ScanBarcodeLeadId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class LeadIdAdapter extends RecyclerView.Adapter<LeadIdAdapter.ViewHolder> implements SendScanBarcodeDetails {
    RequestQueue barcodeDetails;
    ScanBarcodeLeadId context;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, response1, previouseBarcode, afterBarcode, searchBarcode, storeResponse;
    RecyclerInterface listener;
    private OnItemClickListener onItemClickListener;
    boolean storeflag = true;
    ArrayList<ScannedBarcodeDetails> sampleTypes;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;

    public LeadIdAdapter(ScanBarcodeLeadId scanBarcodeLeadId, ArrayList<ScannedBarcodeDetails> sample_type_array, RecyclerInterface listener) {
        this.context = scanBarcodeLeadId;

        this.sampleTypes = sample_type_array;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scanBarcode;
        public EditText enter_barcode, reenter;
        public ImageView img_edt, setback, element1_iv;

        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            scanBarcode = (TextView) itemView.findViewById(R.id.serum);
            img_edt = (ImageView) itemView.findViewById(R.id.img_edt);
            linearEditbarcode = (LinearLayout) itemView.findViewById(R.id.lineareditbarcode);
            barcode_linear = (LinearLayout) itemView.findViewById(R.id.barcode_linear);
            setback = (ImageView) itemView.findViewById(R.id.setback);
            element1_iv = (ImageView) itemView.findViewById(R.id.element1_iv);
            enter_barcode = (EditText) itemView.findViewById(R.id.enter_barcode);
            reenter = (EditText) itemView.findViewById(R.id.reenter);
            MyTextWatcher textWatcher = new MyTextWatcher(enter_barcode);
            enter_barcode.addTextChangedListener(textWatcher);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LeadIdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_barcode_list, parent, false);
        LeadIdAdapter.ViewHolder vh = new LeadIdAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void barcodeDetails(Context v, String s) {
//        GlobalClass.getscannedData = s;
    }

    @Override
    public void onBindViewHolder(final LeadIdAdapter.ViewHolder holder, final int position) {
        holder.linearEditbarcode.setVisibility(View.GONE);
        holder.setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.GONE);
                holder.barcode_linear.setVisibility(View.VISIBLE);
            }
        });

        holder.element1_iv.setOnClickListener(onScanbarcodeClickListener);
        holder.element1_iv.setTag(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type());
        holder.scanBarcode.setText(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type());
        holder.scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);
            }
        });

        holder.enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        holder.enter_barcode.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        holder.reenter.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


        prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        if (GlobalClass.finalspecimenttypewiselist.get(position).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(position).getBarcode().isEmpty()) {
            searchBarcode = GlobalClass.finalspecimenttypewiselist.get(position).getBarcode();
            // checkBarcode(position, searchBarcode);
            holder.scanBarcode.setText(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type() + " : " + GlobalClass.finalspecimenttypewiselist.get(position).getBarcode());
            SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
            setBarcodeDetails.setSpecimenType(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type());
            setBarcodeDetails.setBarcode_number(searchBarcode);
            GlobalClass.setScannedBarcodes.add(setBarcodeDetails);
            Set<SetBarcodeDetails> hs = new HashSet<>();
            hs.addAll(GlobalClass.setScannedBarcodes);
            GlobalClass.setScannedBarcodes.clear();
            GlobalClass.setScannedBarcodes.addAll(hs);
            Log.e(TAG, "onBindViewHolder: unique barcodes" + GlobalClass.setScannedBarcodes.size());
            for (int i = 0; i < GlobalClass.setScannedBarcodes.size(); i++) {
                Log.e(TAG, "onBindViewHolder: specimen type & barcode" + GlobalClass.setScannedBarcodes.get(i).getBarcode_number() + GlobalClass.setScannedBarcodes.get(i).getSpecimenType());
            }
            System.out.println("length of barcodes" + GlobalClass.setScannedBarcodes.size());
            Log.e(TAG, "onBindViewHolder: size of array" + GlobalClass.setScannedBarcodes.size());

            for (int i = 0; i < GlobalClass.setScannedBarcodes.size(); i++) {
                String getbarcodeType = GlobalClass.setScannedBarcodes.get(i).getSpecimenType();
                String barcode_number = GlobalClass.setScannedBarcodes.get(i).getBarcode_number();
                String products = GlobalClass.setScannedBarcodes.get(i).getProductType();
            }

        } else {
            holder.scanBarcode.setText(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type());
        }


        holder.reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
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
                String enteredString = s.toString();

                previouseBarcode = holder.enter_barcode.getText().toString();
                if (storeflag == true) {
                    if (previouseBarcode.equals("")) {
                        Toast.makeText(context, ToastFile.first_ent_brcd, Toast.LENGTH_SHORT).show();
                        holder.reenter.setText("");
                    }
                } else {

                    holder.reenter.setText(enteredString);
                }

                if (enteredString.length() > 8) {
                    holder.reenter.setText(enteredString.substring(1));
                    Toast.makeText(context, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                } else {

                }

                if (s.length() == 8) {
                    afterBarcode = s.toString();

                    searchBarcode = s.toString();
                    if (previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        holder.scanBarcode.setText(GlobalClass.finalspecimenttypewiselist.get(position).getSpecimen_type() + ":" + searchBarcode);
                        GlobalClass.finalspecimenttypewiselist.get(position).setBarcode(searchBarcode);
                    } else {
                        holder.reenter.setText("");
                        Toast.makeText(context, ToastFile.ent_crt_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });


    }


    public interface OnItemClickListener {
        void onScanbarcodeClickListener(String Specimenttype, Activity activity);
    }

    View.OnClickListener onScanbarcodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Specimenttype = (String) v.getTag();
            String code = (String) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onScanbarcodeClickListener(Specimenttype, context);
            }
        }
    };

    @Override
    public int getItemCount() {
        return GlobalClass.finalspecimenttypewiselist.size();
    }


    class MyTextWatcher implements TextWatcher {
        private EditText enter_barcode, reenter;
        LinearLayout linearEditbarcode, barcode_linear;
        Button scanBarcode;
        boolean flag = false;

        public MyTextWatcher(EditText editText) {
            this.enter_barcode = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

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
            String eneterString = s.toString();

            if (s.length() < 8) {
                flag = false;
            }
            if (s.length() > 8) {
                enter_barcode.setText(eneterString.substring(1));
                Toast.makeText(context, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
            }


            if (s.length() == 8) {

                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                //checkBarcode(position,s.toString());
                final ArrayList<String> storeBarcode = new ArrayList<>();
                if (GlobalClass.finalspecimenttypewiselist.size() != 0) {
                    for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                        if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() != null) {
                            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                Toast.makeText(context, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
                                enter_barcode.setText("");
                                flag = true;
                            }
                        } else {
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
                                        Log.e(TAG, "onResponse: Response" + response);
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
                                                enter_barcode.setText(searchBarcode);
                                                progressDialog.dismiss();
                                            } else if (ERROR.equalsIgnoreCase(caps_invalidApikey)) {
                                                if (progressDialog != null && progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                GlobalClass.redirectToLogin(context);
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
                            }

                        }
                    }

                } else {

                }

            } else {
            }

        }
    }


}
