package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.app.Activity;
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
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Controller.Checkbarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SetBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;


public class ScanBarcodeAdapter extends RecyclerView.Adapter<ScanBarcodeAdapter.ViewHolder> {
    Activity mContext;
    String user, passwrd, access, api_key, ERROR, RES_ID, barcode, storeResponse;
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
    private String TAG = ScanBarcodeAdapter.class.getSimpleName();
    private RequestQueue barcodeDetails;
    private SharedPreferences prefs;
    private String response1;
    private boolean setFlagToBack = false;
    public ArrayList<SetBarcodeDetails> setScannedBarcodesULC = new ArrayList<>();
    private int position;
    TextView barcodescanbtn;
    private EditText edit_enter_barcode;

    public ScanBarcodeAdapter(ProductLisitngActivityNew mContext, ArrayList<ScannedBarcodeDetails> setAllTestWithBArcodeList) {
        this.mContext = mContext;
        this.scannedBarcodeDetails = setAllTestWithBArcodeList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ScanBarcodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_barcode_list, parent, false);
        ScanBarcodeAdapter.ViewHolder vh = new ScanBarcodeAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScanBarcodeAdapter.ViewHolder holder, final int position) {
        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
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

        holder.enter_barcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        holder.reenter.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        holder.linearEditbarcode.setVisibility(View.GONE);
        holder.element1_iv.setOnClickListener(onScanbarcodeClickListener);
        holder.element1_iv.setTag(scannedBarcodeDetails.get(position).getSpecimen_type());

        if (!GlobalClass.isNull(scannedBarcodeDetails.get(position).getBarcode())) {

            if (!GlobalClass.isNetworkAvailable(mContext)) {
                GlobalClass.SetText(holder.barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type() + " : " + scannedBarcodeDetails.get(position).getBarcode());
                SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
                setBarcodeDetails.setSpecimenType(scannedBarcodeDetails.get(position).getSpecimen_type());
                setBarcodeDetails.setBarcode_number(searchBarcode);
                setScannedBarcodesULC.add(setBarcodeDetails);

                Set<SetBarcodeDetails> hs = new HashSet<>();
                hs.addAll(setScannedBarcodesULC);
                setScannedBarcodesULC.clear();
                setScannedBarcodesULC.addAll(hs);
            } else {
                searchBarcode = scannedBarcodeDetails.get(position).getBarcode();

                if (!GlobalClass.isNetworkAvailable(mContext)) {
                    GlobalClass.SetText(holder.barcodescanbtn, searchBarcode);
                } else {
                    barcodeDetails = GlobalClass.setVolleyReq(mContext);

                    String url = Api.checkBarcode + api_key + "/" + searchBarcode + "/getcheckbarcode";

                    this.position = position;
                    barcodescanbtn = holder.barcodescanbtn;
                    try {
                        if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                            ControllersGlobalInitialiser.checkbarcode_controller = null;
                        }
                        ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity) mContext, ScanBarcodeAdapter.this, searchBarcode, "1");
                        ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(url, barcodeDetails);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        } else {
            GlobalClass.SetText(holder.barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type());
        }
        holder.barcodescanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearEditbarcode.setVisibility(View.VISIBLE);
                holder.barcode_linear.setVisibility(View.GONE);
                setFlagToBack = true;
            }
        });


        GlobalClass.SetText(holder.barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type());
        GlobalClass.SetEditText(holder.enter_barcode, scannedBarcodeDetails.get(position).getBarcode());
        GlobalClass.SetEditText(holder.reenter, scannedBarcodeDetails.get(position).getBarcode());

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
                if (storeflag == true) {
                    if (previouseBarcode.equals("")) {
                        GlobalClass.SetEditText(holder.reenter, "");
                    }
                } else {
                    GlobalClass.SetEditText(holder.reenter, enteredString);
                }

                if (enteredString.length() > 8) {
                    GlobalClass.SetEditText(holder.reenter, enteredString.substring(1));

                }

                if (s.length() == 8) {
                    afterBarcode = s.toString();
                    searchBarcode = s.toString();
                    if (!GlobalClass.isNull(previouseBarcode) && !GlobalClass.isNull(afterBarcode) &&
                            previouseBarcode.equalsIgnoreCase(afterBarcode)) {
                        GlobalClass.showTastyToast((Activity) mContext, ToastFile.mtch_brcd, 2);
                        holder.linearEditbarcode.setVisibility(View.GONE);
                        holder.barcode_linear.setVisibility(View.VISIBLE);

                        GlobalClass.SetText(holder.barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type() + ":" + searchBarcode);
                        scannedBarcodeDetails.get(position).setBarcode(searchBarcode);

                    } else {
                        GlobalClass.SetEditText(holder.reenter, "");
                        GlobalClass.showTastyToast(mContext, ToastFile.ent_crt_brcd, 2);
                    }
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

    public void getpassbarcode(JSONObject response, String barcodeDetails, String fromcscanbarcodeadapter) {

        if (!GlobalClass.isNull(fromcscanbarcodeadapter) && fromcscanbarcodeadapter.equalsIgnoreCase("1")) {
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
                    GlobalClass.SetText(barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type() + " : " + scannedBarcodeDetails.get(position).getBarcode());

                    SetBarcodeDetails setBarcodeDetails = new SetBarcodeDetails();
                    setBarcodeDetails.setSpecimenType(scannedBarcodeDetails.get(position).getSpecimen_type());
                    setBarcodeDetails.setBarcode_number(searchBarcode);
                    setScannedBarcodesULC.add(setBarcodeDetails);

                    Set<SetBarcodeDetails> hs = new HashSet<>();
                    hs.addAll(setScannedBarcodesULC);
                    setScannedBarcodesULC.clear();
                    setScannedBarcodesULC.addAll(hs);

                } else {
                    GlobalClass.SetText(barcodescanbtn, scannedBarcodeDetails.get(position).getSpecimen_type());
                    storeResponse = response1;

                    GlobalClass.showTastyToast(mContext, "" + response1, 2);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (!GlobalClass.isNull(fromcscanbarcodeadapter) && fromcscanbarcodeadapter.equalsIgnoreCase("2")) {
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
                    GlobalClass.EditSetText(edit_enter_barcode, searchBarcode);
                } else {
                    GlobalClass.SetEditText(edit_enter_barcode, "");
                    storeResponse = response1;
                    GlobalClass.showTastyToast(mContext, "" + response1, 2);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
        ImageView setback, element1_iv;
        LinearLayout linearEditbarcode, barcode_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            barcodescanbtn = (TextView) itemView.findViewById(R.id.serum);
            linearEditbarcode = (LinearLayout) itemView.findViewById(R.id.lineareditbarcode);
            barcode_linear = (LinearLayout) itemView.findViewById(R.id.barcode_linear);
            setback = (ImageView) itemView.findViewById(R.id.setback);
            element1_iv = (ImageView) itemView.findViewById(R.id.element1_iv);
            enter_barcode = (EditText) itemView.findViewById(R.id.enter_barcode);
            reenter = (EditText) itemView.findViewById(R.id.reenter);
            enter_barcode.setFilters(new InputFilter[]{EMOJI_FILTER});
            reenter.setFilters(new InputFilter[]{EMOJI_FILTER});
            MyTextWatcher textWatcher = new MyTextWatcher(enter_barcode);
            enter_barcode.addTextChangedListener(textWatcher);
        }
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

            }
            if (s.length() == 8) {

                previouseBarcode = s.toString();
                searchBarcode = s.toString();
                if (GlobalClass.CheckArrayList(scannedBarcodeDetails)) {
                    for (int i = 0; i < scannedBarcodeDetails.size(); i++) {
                        if (!GlobalClass.isNull(scannedBarcodeDetails.get(i).getBarcode())) {
                            if (!GlobalClass.isNull(searchBarcode) && scannedBarcodeDetails.get(i).getBarcode().equalsIgnoreCase(searchBarcode)) {
                                if (setFlagToBack) {
                                    GlobalClass.showTastyToast(mContext, ToastFile.duplicate_barcd, 2);
                                    GlobalClass.SetEditText(enter_barcode, "");
                                }

                                flag = true;
                            }
                        } else {
                            if (!flag) {
                                flag = true;

                                if (!GlobalClass.isNetworkAvailable(mContext)) {
                                    GlobalClass.SetEditText(enter_barcode, searchBarcode);
                                } else {

                                    barcodeDetails = GlobalClass.setVolleyReq(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata

                                    String url = Api.checkBarcode + api_key + "/" + s + "/getcheckbarcode";

                                    edit_enter_barcode = enter_barcode;

                                    try {
                                        if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                                            ControllersGlobalInitialiser.checkbarcode_controller = null;
                                        }
                                        ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller((Activity) mContext, ScanBarcodeAdapter.this, s.toString(), "2");
                                        ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(url, barcodeDetails);
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
