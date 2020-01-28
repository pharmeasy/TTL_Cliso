package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.MainMaterialModel;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.MaterialDetailsModel;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BMC_StockAvailabilityActivity extends AppCompatActivity {

    Gson gson;
    private ImageView back, home;
    private TextView title, tv_srno, tv_materialName, tv_opStock, tv_clStock, tv_noResult;
    private SharedPreferences prefs;
    private String user, passwrd, access, api_key;
    private Global globalClass;
    private TableLayout tablelayout;
    private TableRow tableRow;
    private Activity mActivity;
    private ConnectionDetector cd;
    private LinearLayout ll_tableView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmc_activity_stock_availability);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        mActivity = BMC_StockAvailabilityActivity.this;
        cd = new ConnectionDetector(mActivity);
        initUI();

        title.setText("STOCK AVAILABILITY");

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet())
            FetchAvailableStock();
        else
            Toast.makeText(mActivity, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        title = (TextView) findViewById(R.id.title);
        tv_noResult = (TextView) findViewById(R.id.tv_noResult);
        tablelayout = (TableLayout) findViewById(R.id.tablelayout);
        ll_tableView = (LinearLayout) findViewById(R.id.ll_tableView);
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
                Intent i = new Intent(BMC_StockAvailabilityActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void FetchAvailableStock() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("APIKey", api_key);
            jsonObject.put("userCode", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ControllersGlobalInitialiser.getAvailableStockController != null) {
            ControllersGlobalInitialiser.getAvailableStockController = null;
        }
        ControllersGlobalInitialiser.getAvailableStockController = new GetAvailableStockController(BMC_StockAvailabilityActivity.this, mActivity);
        ControllersGlobalInitialiser.getAvailableStockController.getAvailableStock(jsonObject);
    }


    public void getStockResponse(JSONObject response) {
        gson = new Gson();
        MainMaterialModel mainMaterialModel = gson.fromJson(String.valueOf(response), MainMaterialModel.class);
        if (mainMaterialModel != null) {
            if (mainMaterialModel.getResponseId().equalsIgnoreCase("RES0000")) {
                if (mainMaterialModel.getMaterialDetails() != null && mainMaterialModel.getMaterialDetails().size() > 0) {
                    ll_tableView.setVisibility(View.VISIBLE);
                    tv_noResult.setVisibility(View.GONE);
                    displayData(mainMaterialModel.getMaterialDetails());
                } else {
                    ll_tableView.setVisibility(View.GONE);
                    tv_noResult.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(mActivity, mainMaterialModel.getResponse(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayData(final ArrayList<MaterialDetailsModel> materialDetailsModelArrayList) {
        tablelayout.removeAllViews();
        int srno;
        for (int i = 0; i < materialDetailsModelArrayList.size(); i++) {
            tableRow = (TableRow) LayoutInflater.from(mActivity).inflate(R.layout.material_item_view, null);

            tv_srno = (TextView) tableRow.findViewById(R.id.tv_srno);
            tv_materialName = (TextView) tableRow.findViewById(R.id.tv_materialName);
            tv_opStock = (TextView) tableRow.findViewById(R.id.tv_opStock);
            tv_clStock = (TextView) tableRow.findViewById(R.id.tv_clStock);

            tv_materialName.setSelected(true);
            tv_materialName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tv_materialName.setSingleLine(true);

            srno = i + 1;
            tv_srno.setText("" + srno);
            tv_materialName.setText(materialDetailsModelArrayList.get(i).getMaterialName());
            tv_opStock.setText(materialDetailsModelArrayList.get(i).getOpeningStock());

            tv_clStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow row = (TableRow) v.getParent();
                    int index = tablelayout.indexOfChild(row);
                    MaterialDetailsModel materialDetailsModel = materialDetailsModelArrayList.get(index);
                    System.out.println("<<index>> " + index);
                    Intent intent = new Intent(BMC_StockAvailabilityActivity.this, BMC_UpdateMaterialActivity.class);
                    intent.putExtra("mater_model", materialDetailsModel);
                    startActivity(intent);
                }
            });

            tablelayout.addView(tableRow);
        }
    }
}
