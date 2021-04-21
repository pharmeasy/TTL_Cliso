package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.MaterialDetailsModel;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

public class BMC_UpdateMaterialActivity extends AppCompatActivity {

    private ImageView back, home;
    private TextView title;
    private SharedPreferences prefs;
    private String user, passwrd, access, api_key;
    private Global globalClass;
    private Activity mActivity;
    private ConnectionDetector cd;
    private EditText edt_opStock, edt_usedStock, edt_wastageStock, edt_defStock, edt_clStock;
    private Button btn_submit;
    private MaterialDetailsModel materialDetailsModel;
    private int opStock = 0, usedStock = 0, wastageStock = 0, defStock = 0, clStock = 0, calStock = 0;
    private boolean usedFlag = false, wastFlag = false, defFlag = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmc_update_material);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        mActivity = BMC_UpdateMaterialActivity.this;
        cd = new ConnectionDetector(mActivity);
        initUI();

        title.setText("STOCK AVAILABILITY");

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        initListeners();
    }

    private void initUI() {
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        title = (TextView) findViewById(R.id.title);
        edt_opStock = (EditText) findViewById(R.id.edt_opStock);
        edt_usedStock = (EditText) findViewById(R.id.edt_usedStock);
        edt_wastageStock = (EditText) findViewById(R.id.edt_wastageStock);
        edt_defStock = (EditText) findViewById(R.id.edt_defStock);
        edt_clStock = (EditText) findViewById(R.id.edt_clStock);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        materialDetailsModel = getIntent().getExtras().getParcelable("mater_model");

        opStock = Integer.parseInt(materialDetailsModel.getOpeningStock());
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_UpdateMaterialActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        edt_usedStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = true;
                wastFlag = false;
                defFlag = false;
                if (!edt_usedStock.getText().toString().trim().isEmpty()) {
                    usedStock = Integer.parseInt(edt_usedStock.getText().toString().trim());
                } else
                    usedStock = 0;
                if (usedStock != 0) {
                    calculateCLstock(usedStock, wastageStock, defStock, edt_usedStock);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_wastageStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = true;
                defFlag = false;
                if (!edt_wastageStock.getText().toString().trim().isEmpty()) {
                    wastageStock = Integer.parseInt(edt_wastageStock.getText().toString().trim());
                } else
                    wastageStock = 0;
                if (wastageStock != 0)
                    calculateCLstock(usedStock, wastageStock, defStock, edt_wastageStock);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_defStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = false;
                defFlag = true;
                if (!edt_defStock.getText().toString().trim().isEmpty()) {
                    defStock = Integer.parseInt(edt_defStock.getText().toString().trim());
                } else
                    defStock = 0;
                if (defStock != 0)
                    calculateCLstock(usedStock, wastageStock, defStock, edt_defStock);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_opStock.setText(materialDetailsModel.getOpeningStock());
        edt_usedStock.setText(materialDetailsModel.getUsedStock());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_usedStock.getText().toString().isEmpty() && edt_wastageStock.getText().toString().isEmpty() && edt_defStock.getText().toString().isEmpty()) {
                    Toast.makeText(mActivity, "Enter proper values", Toast.LENGTH_SHORT).show();
                } else {
                    if (clStock > 0) {
                        if (clStock < opStock) {
                            if (usedStock == 0 && wastageStock == 0 && defStock == 0) {
                                Toast.makeText(mActivity, "Enter proper values", Toast.LENGTH_SHORT).show();
                            } else {
                                if (cd.isConnectingToInternet())
                                    UpdateAvailableStock();
                                else
                                    Toast.makeText(mActivity, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mActivity, "Enter proper values", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mActivity, "Enter proper values", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void calculateCLstock(int usedStock, int wastageStock, int defStock, EditText edt_usedStock) {
        calStock = usedStock + wastageStock + defStock;
        Log.e("TAG", "Totalcalstock: " + calStock);
        clStock = opStock - calStock;
        Log.e("TAG", "Clstock: " + clStock);


        if (clStock < 0) {
            edt_usedStock.setText("0");
            if (usedFlag) {
                calStock = 0 + wastageStock + defStock;
            } else if (wastFlag) {
                calStock = usedStock + 0 + defStock;
            } else {
                calStock = usedStock + wastageStock + 0;
            }
            clStock = opStock - calStock;
            edt_clStock.setText("" + clStock);
        } else {
            edt_clStock.setText("" + clStock);
        }
    }

    private void UpdateAvailableStock() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("APIKey", api_key);
            jsonObject.put("Dac_code", user);
            jsonObject.put("Material_id", materialDetailsModel.getMaterialId());
            jsonObject.put("Used_stock", "" + usedStock);
            jsonObject.put("Wastage_stock", "" + wastageStock);
            jsonObject.put("Defective_stock", "" + defStock);
            jsonObject.put("Closing_stock", edt_clStock.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ControllersGlobalInitialiser.updateStockController != null) {
            ControllersGlobalInitialiser.updateStockController = null;
        }
        ControllersGlobalInitialiser.updateStockController = new UpdateStockController(BMC_UpdateMaterialActivity.this, mActivity);
        ControllersGlobalInitialiser.updateStockController.UpdateAvailableStock(jsonObject);
    }

    public void getUpdatedResponse(JSONObject response) {
        String Response = response.optString("Response");
        String ResponseId = response.optString("Responseid");
        if (ResponseId.equalsIgnoreCase("RES0000")) {
            Toast.makeText(mActivity, "Stock updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(mActivity, Response, Toast.LENGTH_SHORT).show();
        }
    }
}
