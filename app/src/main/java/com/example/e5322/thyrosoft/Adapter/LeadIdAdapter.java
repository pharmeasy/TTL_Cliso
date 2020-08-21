package com.example.e5322.thyrosoft.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
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
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Checkbarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private SharedPreferences prefs;
    ArrayList<ScannedBarcodeDetails> sample_type_array;
    ArrayList<SetBarcodeDetails> setScannedBarcodes = new ArrayList<>();
    private EditText edit_enter_barcode;

    public LeadIdAdapter(ScanBarcodeLeadId scanBarcodeLeadId, ArrayList<ScannedBarcodeDetails> sample_type_array, RecyclerInterface listener) {
        this.context = scanBarcodeLeadId;
        this.sampleTypes = sample_type_array;
        this.sample_type_array = sample_type_array;
        this.listener = listener;
    }

    public void getpassbarcode(JSONObject response, String barcodeDetails) {
        Log.e(TAG, "onResponse: Response" + response);
        Log.v("barcode respponse", "" + response);
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
                GlobalClass.showTastyToast(context, "" + response1, 2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        holder.element1_iv.setTag(sample_type_array.get(position).getSpecimen_type());


        GlobalClass.SetText(holder.scanBarcode, sample_type_array.get(position).getSpecimen_type());

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
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        if (!GlobalClass.isNull(sample_type_array.get(position).getBarcode())) {
            searchBarcode = sample_type_array.get(position).getBarcode();
            GlobalClass.SetText(holder.scanBarcode, sample_type_array.get(position).getSpecimen_type() + " : " + sample_type_array.get(position).getBarcode());
            SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
            setBarcodeDetails.setSpecimenType(sample_type_array.get(position).getSpecimen_type());
            setBarcodeDetails.setBarcode_number(searchBarcode);
            setScannedBarcodes.add(setBarcodeDetails);
            Set<SetBarcodeDetails> hs = new HashSet<>();
            hs.addAll(setScannedBarcodes);
            setScannedBarcodes.clear();
            setScannedBarcodes.addAll(hs);

        } else {
            if (!GlobalClass.isNull(sample_type_array.get(position).getSpecimen_type())) {
                if (sample_type_array.get(position).getSpecimen_type().equalsIgnoreCase(Constants.FLUORIDE)) {
                    GlobalClass.SetText(holder.scanBarcode, "(" + sample_type_array.get(position).getProducts() + ")" + sample_type_array.get(position).getSpecimen_type());
                } else {
                    GlobalClass.SetText(holder.scanBarcode, sample_type_array.get(position).getSpecimen_type());
                }
            }
        }


        holder.reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
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
                    GlobalClass.SetEditText(holder.reenter, enteredString.substring(1));
                    GlobalClass.showTastyToast(context, ToastFile.crt_brcd, 2);
                }

                if (s.length() == 8) {
                    afterBarcode = s.toString();

                    searchBarcode = s.toString();
                    if (!GlobalClass.isNull(previouseBarcode) &&
                            !GlobalClass.isNull(afterBarcode) &&
                            previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(holder.scanBarcode, sample_type_array.get(position).getSpecimen_type() + ":" + searchBarcode);
                        sample_type_array.get(position).setBarcode(searchBarcode);
                    } else {
                        GlobalClass.SetEditText(holder.reenter, "");
                        GlobalClass.showTastyToast((Activity) context, ToastFile.crt_brcd, 2);
                    }
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
        return sample_type_array.size();
    }


    class MyTextWatcher implements TextWatcher {
        private EditText enter_barcode;
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
                GlobalClass.showTastyToast(context,
                        ToastFile.crt_brcd,
                        2);
                if (enteredString.length() > 0) {
                    GlobalClass.SetEditText(enter_barcode, enteredString.substring(1));
                } else {
                    GlobalClass.SetEditText(enter_barcode, "");
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
                GlobalClass.SetEditText(enter_barcode, eneterString.substring(1));
                GlobalClass.showTastyToast(context, ToastFile.crt_brcd, 2);
            }


            if (s.length() == 8) {

                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                final ArrayList<String> storeBarcode = new ArrayList<>();
                if (GlobalClass.CheckArrayList(sample_type_array)) {
                    for (int i = 0; i < sample_type_array.size(); i++) {
                        if (!GlobalClass.isNull(sample_type_array.get(i).getBarcode())) {
                            if (!GlobalClass.isNull(searchBarcode) && sample_type_array.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                GlobalClass.showTastyToast(context, ToastFile.duplicate_barcd, 2);
                                GlobalClass.SetEditText(enter_barcode, "");
                                flag = true;
                            }
                        } else {
                            if (!flag) {
                                flag = true;
                                barcodeDetails = GlobalClass.setVolleyReq(context);//2c=/TAM03/TAM03136166236000078/geteditdata

                                String strurl = null;

                                edit_enter_barcode = enter_barcode;
                                try {
                                    if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                                        ControllersGlobalInitialiser.checkbarcode_controller = null;
                                    }
                                    ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity) context, LeadIdAdapter.this, searchBarcode);
                                    strurl = Api.checkBarcode + api_key + "/" + s + "/getcheckbarcode";
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
