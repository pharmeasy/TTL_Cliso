package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Checkbarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Interface.SendScanBarcodeDetails;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class AdapterBarcodeOutlabs extends RecyclerView.Adapter<AdapterBarcodeOutlabs.ViewHolder> implements SendScanBarcodeDetails {

    Scan_Barcode_Outlabs context;
    ArrayList<Outlabdetails_OutLab> selctedProductArraylist;
    RecyclerInterface listener;
    SharedPreferences prefs;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, response1, previouseBarcode, afterBarcode;
    RequestQueue barcodeDetails;
    ArrayList<ScannedBarcodeDetails> distinctspecimentlist;
    private OnItemClickListener onItemClickListener;
    private String searchBarcode;
    private String storeResponse;
    private EditText enter_barcode;

    public AdapterBarcodeOutlabs(Scan_Barcode_Outlabs scan_barcode_outlabs, ArrayList<Outlabdetails_OutLab> selectedlist, ArrayList<ScannedBarcodeDetails> distinctspecimentlist, RecyclerInterface listener) {
        this.context = scan_barcode_outlabs;
        this.selctedProductArraylist = selectedlist;
        this.listener = listener;
        this.distinctspecimentlist = distinctspecimentlist;
    }

    public void getpassbarcode(JSONObject response, String barcodeDetails, String fromoutlabadapter) {
        if (!GlobalClass.isNull(fromoutlabadapter) && fromoutlabadapter.equalsIgnoreCase("1")){
            Log.v("TAG", "barcode respponse" + response);

            String finalJson = response.toString();
            JSONObject parentObjectOtp = null;
            try {
                parentObjectOtp = new JSONObject(finalJson);
                ERROR = parentObjectOtp.getString("ERROR");
                RES_ID = parentObjectOtp.getString("RES_ID");
                barcode = parentObjectOtp.getString("barcode");
                response1 = parentObjectOtp.getString("response");

                if (!GlobalClass.isNull(response1) && response1.equalsIgnoreCase(MessageConstants.BRCD_NT_EXIT)) {
                    storeResponse = response1;
                } else {
                    storeResponse = response1;
                    GlobalClass.showTastyToast(context,response1,2);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (!GlobalClass.isNull(fromoutlabadapter) && fromoutlabadapter.equalsIgnoreCase("2")){
            Log.v("TAG", "barcode respponse" + response);

            String finalJson = response.toString();
            JSONObject parentObjectOtp = null;

            try {
                parentObjectOtp = new JSONObject(finalJson);
                ERROR = parentObjectOtp.getString("ERROR");
                RES_ID = parentObjectOtp.getString("RES_ID");
                barcode = parentObjectOtp.getString("barcode");
                response1 = parentObjectOtp.getString("response");
                if (!GlobalClass.isNull(response1) && response1.equalsIgnoreCase(MessageConstants.BRCD_NT_EXIT)) {
                    GlobalClass.SetEditText(enter_barcode,searchBarcode);

                } else {
                    GlobalClass.SetEditText(enter_barcode,"");
                    storeResponse = response1;
                    GlobalClass.showTastyToast(context,response1,2);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scanBarcode;
        public EditText enter_barcode, reenter;
        public ImageView img_edt, setback;
        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            scanBarcode = (TextView) itemView.findViewById(R.id.serum);
            img_edt = (ImageView) itemView.findViewById(R.id.img_edt);
            setback = (ImageView) itemView.findViewById(R.id.setback);
            linearEditbarcode = (LinearLayout) itemView.findViewById(R.id.lineareditbarcode);
            barcode_linear = (LinearLayout) itemView.findViewById(R.id.barcode_linear);
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
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        holder.reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {

                    GlobalClass.showTastyToast(context,ToastFile.crt_brcd,5);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(holder.reenter,enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(holder.reenter,"");
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
                    if (!GlobalClass.isNull(previouseBarcode) && !GlobalClass.isNull(afterBarcode) && previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        checkBarcode();
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(holder.scanBarcode, "Barcode" + " : " + searchBarcode);

                    } else {
                        GlobalClass.showTastyToast(context,ToastFile.brcd_not_match,2);
                    }


                } else {
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_brcd,2);

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

        if (!GlobalClass.isNull(distinctspecimentlist.get(position).getBarcode())) {

            searchBarcode = distinctspecimentlist .get(position).getBarcode();
            checkBarcode();
            GlobalClass.SetText(holder.scanBarcode, "Barcode" + " : " + searchBarcode);
        } else {
            GlobalClass.SetText(holder.scanBarcode, "Barcode");
        }

        Log.v("TAG", "Barcode :::: " + distinctspecimentlist .get(position).getBarcode());

        holder.img_edt.setVisibility(View.VISIBLE);
        holder.linearEditbarcode.setVisibility(View.GONE);
        holder.scanBarcode.setOnClickListener(onScanbarcodeClickListener);
        holder.scanBarcode.setTag(distinctspecimentlist .get(position).getSpecimen_type());

        holder.img_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalClass.isNull(searchBarcode)) {
                    GlobalClass.SetEditText(holder.enter_barcode,searchBarcode);
                    GlobalClass.SetEditText(holder.reenter,searchBarcode);
                }

                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);

            }
        });


    }

    private void checkBarcode() {
        barcodeDetails = Volley.newRequestQueue(context);
        String strurl = Api.checkBarcode + api_key + "/" + searchBarcode + "/getcheckbarcode";

        try {
            if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                ControllersGlobalInitialiser.checkbarcode_controller = null;
            }
            ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity)context, AdapterBarcodeOutlabs.this,searchBarcode,"1");
            ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(strurl, barcodeDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return distinctspecimentlist .size();
    }

    public interface OnItemClickListener {
        void onScanbarcodeClickListener(String Specimenttype, Activity activity);

    }

    class MyTextWatcher implements TextWatcher {
        private EditText enter_barcode;
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

            String enteredString = s.toString();
            if (!GlobalClass.isNull(enteredString) && enteredString.startsWith(".") || enteredString.startsWith("0")) {

                GlobalClass.showTastyToast(context,ToastFile.crt_brcd,2);
                if (enteredString.length() > 0) {
                    GlobalClass.SetEditText(enter_barcode,enteredString.substring(1));
                } else {
                    GlobalClass.SetEditText(enter_barcode,"");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() == 8) {
                previouseBarcode = s.toString();
                searchBarcode = s.toString();

                if (!flag) {
                    flag = true;
                    barcodeDetails = Volley.newRequestQueue(context);//2c=/TAM03/TAM03136166236000078/geteditdata
                    String strurl = Api.checkBarcode + api_key + "/" + s + "/getcheckbarcode";
                    enter_barcode=enter_barcode;

                    try {
                        if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                            ControllersGlobalInitialiser.checkbarcode_controller = null;
                        }
                        ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity)context, AdapterBarcodeOutlabs.this,searchBarcode,"2");
                        ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(strurl, barcodeDetails);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}


