package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class AttachBarcodeAdpter extends RecyclerView.Adapter<AttachBarcodeAdpter.ViewHolder> {
    Activity mContext;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, response, storeResponse;
    ArrayList<ScannedBarcodeDetails> scannedBarcodeDetails;
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
    private OnItemClickListener onItemClickListener;
    private String previouseBarcode;
    private boolean storeflag = true;
    private String afterBarcode;
    private String searchBarcode;
    private String TAG = AttachBarcodeAdpter.class.getSimpleName();
    private RequestQueue barcodeDetails;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private String response1;
    private boolean setFlagToBack = false;

    boolean preventUpdate = false;

    public AttachBarcodeAdpter(Offline_edt_activity offline_edt_activity, ArrayList<ScannedBarcodeDetails> setAllTestWithBArcodeList) {
        this.mContext = offline_edt_activity;
        this.scannedBarcodeDetails = setAllTestWithBArcodeList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AttachBarcodeAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_barcode_list_offline_woe, parent, false);
        AttachBarcodeAdpter.ViewHolder vh = new AttachBarcodeAdpter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AttachBarcodeAdpter.ViewHolder holder, final int position) {
        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        holder.enter_barcode.setTag(position);
        holder.setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.GONE);
                holder.barcode_linear.setVisibility(View.VISIBLE);
            }
        });

        holder.enter_barcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        holder.reenter.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        holder.barcodescanbtn.setVisibility(View.VISIBLE);
        holder.linearEditbarcode.setVisibility(View.GONE);
        holder.element1_iv.setOnClickListener(onScanbarcodeClickListener);
        holder.element1_iv.setTag(scannedBarcodeDetails.get(position).getSpecimen_type());

        holder.barcodescanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);
                setFlagToBack = true;
            }
        });

        holder.barcodescanbtn.setText(scannedBarcodeDetails.get(position).getSpecimen_type() + ":" + scannedBarcodeDetails.get(position).getBarcode());
        holder.enter_barcode.setText(scannedBarcodeDetails.get(position).getBarcode());
        holder.reenter.setText(scannedBarcodeDetails.get(position).getBarcode());

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

           /*     if (storeflag == true) {
                    if (previouseBarcode.equals("")) {
                        holder.reenter.setText("");
                    }
                } else {
                    holder.reenter.setText(enteredString);
                }*/


                if (enteredString.length() > 8) {

                    Toast.makeText(mContext, "Barcode length should be 8 digits", Toast.LENGTH_SHORT).show();
                    holder.reenter.setText("");

                }

                //getData = reg_pincode.getText().toString();
                if (s.length() == 8) {
                    afterBarcode = s.toString();
                    searchBarcode = s.toString();
                    if (previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        Toast.makeText(mContext, ToastFile.mtch_brcd, Toast.LENGTH_SHORT).show();
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);

                        holder.barcodescanbtn.setText(scannedBarcodeDetails.get(position).getSpecimen_type() + ":" + searchBarcode);
                        scannedBarcodeDetails.get(position).setBarcode(searchBarcode);

                    } else {
                        holder.reenter.setText("");
                        Toast.makeText(mContext, ToastFile.ent_crt_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });

    }


    View.OnClickListener onScanbarcodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Specimenttype = (String) v.getTag();
            String code = (String) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onScanbarcodeClickListener(Specimenttype, mContext);
            }
        }
    };

    public interface OnItemClickListener {

        void onScanbarcodeClickListener(String Specimenttype, Activity activity);

    }

    @Override
    public int getItemCount() {
        return scannedBarcodeDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcodescanbtn;
        EditText enter_barcode, reenter;
        ImageView img_edt, setback, element1_iv;
        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            barcodescanbtn = (TextView) itemView.findViewById(R.id.serum);
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
            int position = (int) enter_barcode.getTag();

            String enteredString = s.toString();
            if (enteredString.startsWith(".") || enteredString.startsWith("0")) {

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
                enter_barcode.setText("");
                Toast.makeText(mContext, "Barcode length should be 8 digits", Toast.LENGTH_SHORT).show();
            }
            if (s.length() == 8) {

                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                //checkBarcode(position,s.toString());
                if (scannedBarcodeDetails.size() != 0) {
                    for (int i = 0; i < scannedBarcodeDetails.size(); i++) {
                        if (scannedBarcodeDetails.get(i).getBarcode() != null) {
                            if (scannedBarcodeDetails.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                if (setFlagToBack == true) {
                                    Toast.makeText(mContext, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
                                    enter_barcode.setText("");
                                } else {

                                }
                                flag = true;
                            }
                        } else {
                            if (flag == false) {
                                flag = true;

                                if (!GlobalClass.isNetworkAvailable(mContext)) {
                                    GlobalClass.showAlertDialog(mContext);
                                } else {

                                    barcodeDetails = GlobalClass.setVolleyReq(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata
                                    progressDialog = new ProgressDialog(mContext);
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

                                                } else {
                                                    enter_barcode.setText("");
                                                    storeResponse = response1;
                                                    progressDialog.dismiss();
                                                    Toast.makeText(mContext, "" + response1, Toast.LENGTH_SHORT).show();
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

                }

            }

        }
    }

}
