package com.example.e5322.thyrosoft.Cliso_BMC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_BaseModel;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Interface.SendScanBarcodeDetails;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SetBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class BMC_ScanBarcodeAdapter extends RecyclerView.Adapter<BMC_ScanBarcodeAdapter.ViewHolder> implements SendScanBarcodeDetails {
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
    SharedPreferences prefs;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, response1, previouseBarcode, afterBarcode, searchBarcode, storeResponse;
    RequestQueue barcodeDetails;
    BMC_Scan_BarcodeActivity context;
    ArrayList<BMC_BaseModel> selctedProductArraylist;
    RecyclerInterface listener;
    boolean storeflag = true;
    ProgressDialog progressDialog;
    String TAG = BMC_ScanBarcodeAdapter.class.getSimpleName();
    ArrayList<ScannedBarcodeDetails> distinctspecimentlist;
    private OnItemClickListener onItemClickListener;

    View.OnClickListener onScanbarcodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Specimenttype = (String) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onScanbarcodeClickListener(Specimenttype, context);
            }
        }
    };

    public BMC_ScanBarcodeAdapter(BMC_Scan_BarcodeActivity bmc_scan_barcodeActivity, ArrayList<BMC_BaseModel> selectedlist, ArrayList<ScannedBarcodeDetails> distinctspecimentlist, RecyclerInterface listener) {
        this.context = bmc_scan_barcodeActivity;
        this.selctedProductArraylist = selectedlist;
        this.listener = listener;
        this.distinctspecimentlist = distinctspecimentlist;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BMC_ScanBarcodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.bmc_scan_barcode_item, parent, false);
        BMC_ScanBarcodeAdapter.ViewHolder vh = new BMC_ScanBarcodeAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void barcodeDetails(Context v, String s) {
        GlobalClass.getscannedData = s;
    }

    @Override
    public void onBindViewHolder(final BMC_ScanBarcodeAdapter.ViewHolder holder, final int position) {

        prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        holder.enter_barcode.setTag(position);
        holder.setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.GONE);
                holder.barcode_linear.setVisibility(View.VISIBLE);
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

        holder.enter_barcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        holder.reenter.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (distinctspecimentlist.get(position).getBarcode() != null && !distinctspecimentlist.get(position).getBarcode().isEmpty()) {

            if (!GlobalClass.isNetworkAvailable(context)) {
                Toast.makeText(context, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
            } else {
                searchBarcode = distinctspecimentlist.get(position).getBarcode();

                if (!GlobalClass.isNetworkAvailable(context)) {
                    Toast.makeText(context, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
                } else {
                    barcodeDetails = GlobalClass.setVolleyReq(context);//2c=/TAM03/TAM03136166236000078/geteditdata
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("Kindly wait...");
                    progressDialog.setMessage(ToastFile.processing_request);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(20);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    //progressDialog.show();
                    try {
                        progressDialog.show();
                    } catch (WindowManager.BadTokenException e) {
                        //use a log message
                    }

                    JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkBarcode + api_key + "/" + searchBarcode + "/getcheckbarcode"
                            , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("barcode respponse" + response);

                            Log.e(TAG, "onResponse: " + response);
                            String finalJson = response.toString();
                            JSONObject parentObjectOtp = null;
                            try {
                                parentObjectOtp = new JSONObject(finalJson);
                                ERROR = parentObjectOtp.getString("ERROR");
                                RES_ID = parentObjectOtp.getString("RES_ID");
                                barcode = parentObjectOtp.getString("barcode");
                                response1 = parentObjectOtp.getString("response");
                                if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                                    holder.scanBarcode.setText(distinctspecimentlist.get(position).getSpecimen_type() + " : " + distinctspecimentlist.get(position).getBarcode());
                                    progressDialog.dismiss();

                                    SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
                                    setBarcodeDetails.setSpecimenType(distinctspecimentlist.get(position).getSpecimen_type());
                                    setBarcodeDetails.setBarcode_number(searchBarcode);
                                    GlobalClass.BMC_setScannedBarcodes.add(setBarcodeDetails);

                                    Set<SetBarcodeDetails> hs = new HashSet<>();
                                    hs.addAll(GlobalClass.BMC_setScannedBarcodes);
                                    GlobalClass.BMC_setScannedBarcodes.clear();
                                    GlobalClass.BMC_setScannedBarcodes.addAll(hs);

                                    Log.e(TAG, "onBindViewHolder: unique barcodes" + GlobalClass.BMC_setScannedBarcodes.size());
                                    for (int i = 0; i < GlobalClass.BMC_setScannedBarcodes.size(); i++) {
                                        Log.e(TAG, "onBindViewHolder: specimen type & barcode" + GlobalClass.BMC_setScannedBarcodes.get(i).getBarcode_number() + GlobalClass.BMC_setScannedBarcodes.get(i).getSpecimenType());
                                    }

                                    System.out.println("length of barcodes" + GlobalClass.BMC_setScannedBarcodes.size());
                                } else if (ERROR.equalsIgnoreCase(caps_invalidApikey)) {
                                    if (context instanceof Activity) {
                                        if (!((Activity) context).isFinishing())
                                            progressDialog.dismiss();
                                    }
                                    GlobalClass.redirectToLogin(context);
                                } else {
                                    if (position < distinctspecimentlist.size()) {
                                        holder.scanBarcode.setText(distinctspecimentlist.get(position).getSpecimen_type());
                                        storeResponse = response1;
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "" + response1, Toast.LENGTH_SHORT).show();
                                    }
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
                    progressDialog.dismiss();
                    jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                            300000,
                            3,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    barcodeDetails.add(jsonObjectRequestPop);
                    Log.e(TAG, "onBindViewHolder: url" + jsonObjectRequestPop);
                }
            }
        } else {
            holder.scanBarcode.setText(distinctspecimentlist.get(position).getSpecimen_type());
        }

        System.out.println("Barcode :::: " + distinctspecimentlist.get(position).getBarcode());
        holder.linearEditbarcode.setVisibility(View.GONE);
        holder.element1_iv.setOnClickListener(onScanbarcodeClickListener);
        holder.element1_iv.setTag(distinctspecimentlist.get(position).getSpecimen_type());

        holder.scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);

            }
        });

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
                }

                //getData = reg_pincode.getText().toString();
                if (s.length() == 8) {
                    afterBarcode = s.toString();

                    searchBarcode = s.toString();
                    if (previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        Toast.makeText(context, ToastFile.mtch_brcd, Toast.LENGTH_SHORT).show();
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        holder.scanBarcode.setText(distinctspecimentlist.get(position).getSpecimen_type() + ":" + searchBarcode);
                        distinctspecimentlist.get(position).setBarcode(searchBarcode);
                    } else {
                        holder.reenter.setText("");
                        Toast.makeText(context, ToastFile.ent_crt_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return distinctspecimentlist.size();//GlobalClass.BMC_TTLBarcodeDetailsList.size()
    }

    public interface OnItemClickListener {
        void onScanbarcodeClickListener(String Specimenttype, Activity activity);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView scanBarcode;
        EditText enter_barcode, reenter;
        ImageView img_edt, setback, element1_iv;
        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            scanBarcode = (TextView) itemView.findViewById(R.id.serum);
            img_edt = (ImageView) itemView.findViewById(R.id.img_edt);
            element1_iv = (ImageView) itemView.findViewById(R.id.element1_iv);
            linearEditbarcode = (LinearLayout) itemView.findViewById(R.id.lineareditbarcode);
            barcode_linear = (LinearLayout) itemView.findViewById(R.id.barcode_linear);
            setback = (ImageView) itemView.findViewById(R.id.setback);
            enter_barcode = (EditText) itemView.findViewById(R.id.enter_barcode);
            reenter = (EditText) itemView.findViewById(R.id.reenter);
            enter_barcode.setFilters(new InputFilter[]{EMOJI_FILTER});
            reenter.setFilters(new InputFilter[]{EMOJI_FILTER});
            MyTextWatcher textWatcher = new MyTextWatcher(enter_barcode);
            enter_barcode.addTextChangedListener(textWatcher);
        }
    }

    class MyTextWatcher implements TextWatcher {
        LinearLayout linearEditbarcode, barcode_linear;
        Button scanBarcode;
        ImageView element1_iv;
        boolean flag = false;
        private EditText enter_barcode, reenter;

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
            String eneterString = s.toString();
            int position = (int) enter_barcode.getTag();
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
                if (distinctspecimentlist.size() != 0) {
                    for (int i = 0; i < distinctspecimentlist.size(); i++) {
                        if (distinctspecimentlist.get(i).getBarcode() != null) {
                            if (distinctspecimentlist.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                Toast.makeText(context, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
                                enter_barcode.setText("");
                                flag = true;
                            }
                        } else {
                            if (flag == false) {
                                flag = true;
                                if (!GlobalClass.isNetworkAvailable(context)) {
                                    Toast.makeText(context, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
                                } else {
                                    barcodeDetails = GlobalClass.setVolleyReq(context);//2c=/TAM03/TAM03136166236000078/geteditdata
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
                                            Log.e(TAG, "onResponse: " + response);
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
                                                    /*if (progressDialog != null && progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                    }*/
                                                    if (context instanceof Activity) {
                                                        if (!((Activity) context).isFinishing())
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
                    }

                } else {

                }

            } else {
            }
        }
    }
}
