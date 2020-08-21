package com.example.e5322.thyrosoft.Adapter;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Scan_Barcode_Outlabs_Activity;
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
import com.example.e5322.thyrosoft.SetBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class CLISO_ScanBarcodeAdapter extends RecyclerView.Adapter<CLISO_ScanBarcodeAdapter.ViewHolder> implements SendScanBarcodeDetails {
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
    Scan_Barcode_Outlabs_Activity context;
    ArrayList<Outlabdetails_OutLab> selctedProductArraylist;
    RecyclerInterface listener;
    boolean storeflag = true;
    ProgressDialog progressDialog;
    String TAG = CLISO_ScanBarcodeAdapter.class.getSimpleName();
    ArrayList<ScannedBarcodeDetails> distinctspecimentlist;
    private CLISO_ScanBarcodeAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<SetBarcodeDetails> BMC_setScannedBarcodes = new ArrayList<>();

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
    private int position;
    TextView scanBarcode;
    public EditText edit_enter_barcode;

    public CLISO_ScanBarcodeAdapter(Scan_Barcode_Outlabs_Activity bmc_scan_barcodeActivity, ArrayList<Outlabdetails_OutLab> selectedlist, ArrayList<ScannedBarcodeDetails> distinctspecimentlist, RecyclerInterface listener) {
        this.context = bmc_scan_barcodeActivity;
        this.selctedProductArraylist = selectedlist;
        this.listener = listener;
        this.distinctspecimentlist = distinctspecimentlist;
    }

    public void setOnItemClickListener(CLISO_ScanBarcodeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CLISO_ScanBarcodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.bmc_scan_barcode_item, parent, false);
        CLISO_ScanBarcodeAdapter.ViewHolder vh = new CLISO_ScanBarcodeAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void barcodeDetails(Context v, String s) {
        GlobalClass.getscannedData = s;
    }

    @Override
    public void onBindViewHolder(final CLISO_ScanBarcodeAdapter.ViewHolder holder, final int position) {

        prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
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

        if (!GlobalClass.isNull(distinctspecimentlist.get(position).getBarcode())) {

            if (!GlobalClass.isNetworkAvailable(context)) {
                GlobalClass.showTastyToast((Activity) context, ToastFile.intConnection, 2);
            } else {
                searchBarcode = distinctspecimentlist.get(position).getBarcode();

                if (!GlobalClass.isNetworkAvailable(context)) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.intConnection, 2);
                } else {
                    barcodeDetails = Volley.newRequestQueue(context);//2c=/TAM03/TAM03136166236000078/geteditdata

                    String strurl = Api.checkBarcode + api_key + "/" + searchBarcode + "/getcheckbarcode";

                    this.position = position;
                    scanBarcode = holder.scanBarcode;
                    try {
                        if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                            ControllersGlobalInitialiser.checkbarcode_controller = null;
                        }
                        ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity) context, CLISO_ScanBarcodeAdapter.this, searchBarcode, "1");
                        ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(strurl, barcodeDetails);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            GlobalClass.SetText(holder.scanBarcode, distinctspecimentlist.get(position).getSpecimen_type());
        }

        Log.v("TAG", "Barcode :::: " + distinctspecimentlist.get(position).getBarcode());
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
                if (!GlobalClass.isNull(enteredString) && enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(holder.reenter, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(holder.reenter, "");
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
                if (storeflag) {
                    if (GlobalClass.isNull(previouseBarcode)) {
                        GlobalClass.showTastyToast(context, ToastFile.first_ent_brcd, 2);
                        GlobalClass.SetEditText(holder.reenter, "");
                    }

                } else {
                    GlobalClass.SetEditText(holder.reenter, enteredString);
                }

                if (enteredString.length() > 8) {
                    GlobalClass.SetEditText(holder.reenter, "");
                    GlobalClass.showTastyToast(context, ToastFile.crt_brcd, 2);
                }

                if (s.length() == 8) {
                    afterBarcode = s.toString();

                    searchBarcode = s.toString();
                    if (!GlobalClass.isNull(previouseBarcode)
                            && !GlobalClass.isNull(afterBarcode) &&
                            previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        GlobalClass.showTastyToast(context, ToastFile.mtch_brcd, 5);
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(holder.scanBarcode, distinctspecimentlist.get(position).getSpecimen_type() + ":" + searchBarcode);
                        distinctspecimentlist.get(position).setBarcode(searchBarcode);
                    } else {
                        GlobalClass.SetEditText(holder.reenter, "");
                        GlobalClass.showTastyToast(context, ToastFile.ent_crt_brcd, 5);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return distinctspecimentlist.size();
    }

    public void getpassbarcode(JSONObject response, String barcodeDetails, String fromclisoadapter) {

        if (fromclisoadapter.equalsIgnoreCase("1")) {
            Log.v("TAG", "barcode respponse" + response);

            Log.e(TAG, "onResponse: " + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = null;


            try {
                parentObjectOtp = new JSONObject(finalJson);
                ERROR = parentObjectOtp.getString("ERROR");
                RES_ID = parentObjectOtp.getString("RES_ID");
                barcode = parentObjectOtp.getString("barcode");
                response1 = parentObjectOtp.getString("response");
                if (!GlobalClass.isNull(response1) && response1.equalsIgnoreCase(MessageConstants.BRCD_NT_EXIT)) {
                    GlobalClass.SetText(scanBarcode, distinctspecimentlist.get(position).getSpecimen_type() + " : " + distinctspecimentlist.get(position).getBarcode());
                    GlobalClass.hideProgress((Activity) context, progressDialog);

                    SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
                    setBarcodeDetails.setSpecimenType(distinctspecimentlist.get(position).getSpecimen_type());
                    setBarcodeDetails.setBarcode_number(searchBarcode);
                    BMC_setScannedBarcodes.add(setBarcodeDetails);

                    Set<SetBarcodeDetails> hs = new HashSet<>();
                    hs.addAll(BMC_setScannedBarcodes);
                    BMC_setScannedBarcodes.clear();
                    BMC_setScannedBarcodes.addAll(hs);

                } else if (!GlobalClass.isNull(ERROR) && ERROR.equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(context);
                } else {
                    if (position < distinctspecimentlist.size()) {
                        GlobalClass.SetText(scanBarcode, distinctspecimentlist.get(position).getSpecimen_type());
                        storeResponse = response1;
                        GlobalClass.hideProgress((Activity) context, progressDialog);
                        GlobalClass.showTastyToast((Activity) context, response1, 2);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (!GlobalClass.isNull(fromclisoadapter) && fromclisoadapter.equalsIgnoreCase("2")) {
            Log.v("barcode respponse", response.toString());
            Log.e(TAG, "onResponse: " + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = null;

            try {
                parentObjectOtp = new JSONObject(finalJson);
                ERROR = parentObjectOtp.getString("ERROR");
                RES_ID = parentObjectOtp.getString("RES_ID");
                barcode = parentObjectOtp.getString("barcode");
                response1 = parentObjectOtp.getString("response");

                if (!GlobalClass.isNull(response1) && response1.equalsIgnoreCase(MessageConstants.BRCD_NT_EXIT)) {
                    GlobalClass.SetEditText(edit_enter_barcode, searchBarcode);
                } else if (!GlobalClass.isNull(ERROR) && ERROR.equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(context);
                } else {
                    GlobalClass.SetEditText(edit_enter_barcode, "");
                    storeResponse = response1;
                    GlobalClass.showTastyToast((Activity) context, response1, 2);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
            CLISO_ScanBarcodeAdapter.MyTextWatcher textWatcher = new CLISO_ScanBarcodeAdapter.MyTextWatcher(enter_barcode);
            enter_barcode.addTextChangedListener(textWatcher);
        }
    }

    class MyTextWatcher implements TextWatcher {
        boolean flag = false;
        public EditText enter_barcode;

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
                GlobalClass.showTastyToast((Activity) context, ToastFile.crt_brcd, 2);
                if (enteredString.length() > 0) {
                    GlobalClass.SetEditText(enter_barcode, enteredString.substring(1));
                } else {
                    GlobalClass.SetEditText(enter_barcode, "");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String eneterString = s.toString();

            if (s.length() < 8) {
                flag = false;
            }
            if (s.length() > 8) {
                GlobalClass.SetEditText(enter_barcode, eneterString.substring(1));
                GlobalClass.showTastyToast(context, ToastFile.crt_brcd, 2);
            }
            if (s.length() == 8) {
                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                final ArrayList<String> storeBarcode = new ArrayList<>();
                if (GlobalClass.CheckArrayList(distinctspecimentlist)) {
                    for (int i = 0; i < distinctspecimentlist.size(); i++) {
                        if (!GlobalClass.isNull(distinctspecimentlist.get(i).getBarcode())) {
                            if (!GlobalClass.isNull(distinctspecimentlist.get(i).getBarcode()) &&
                                    !GlobalClass.isNull(searchBarcode) &&
                                    distinctspecimentlist.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                GlobalClass.showTastyToast(context, ToastFile.duplicate_barcd, 2);
                                GlobalClass.SetEditText(enter_barcode, "");
                                flag = true;
                            }
                        } else {
                            if (!flag) {
                                flag = true;
                                if (!GlobalClass.isNetworkAvailable(context)) {
                                    GlobalClass.showTastyToast(context, ToastFile.intConnection, 2);
                                } else {
                                    barcodeDetails = Volley.newRequestQueue(context);//2c=/TAM03/TAM03136166236000078/geteditdata

                                    String strurl = Api.checkBarcode + api_key + "/" + s + "/getcheckbarcode";


                                    edit_enter_barcode = enter_barcode;
                                    try {
                                        if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                                            ControllersGlobalInitialiser.checkbarcode_controller = null;
                                        }
                                        ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity) context, CLISO_ScanBarcodeAdapter.this, searchBarcode, "2");
                                        ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(strurl, barcodeDetails);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}

