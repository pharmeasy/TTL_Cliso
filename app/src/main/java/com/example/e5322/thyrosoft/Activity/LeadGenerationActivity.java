package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.Controller.LeadController;
import com.example.e5322.thyrosoft.Controller.PostLeadResponseController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadRequestModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class LeadGenerationActivity extends AppCompatActivity {

    EditText edt_name, edt_mobile, edt_mail, edt_address, edt_pincode, edt_remarks;
    Spinner spr_package;
    Button btn_submit;
    private ArrayList<LeadResponseModel.ProductlistBean> leadResponseModel;
    LeadController leadController;
    LeadRequestModel leadRequestModel;
    TextView tv_reset;
    PostLeadResponseController postLeadResponseController;
    PostLeadDataModel postLeadDataModel;
    LeadDataResponseModel leadDataResponseModel;

    String Product_id, tests;
    int Rate, is_booking, pass_rate = 0;
    String pass_product = "", pass_reportcode = "";
    private Activity mActivity;
    ImageView back, home;
    private String user, api_key, mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_generation);

        mActivity = LeadGenerationActivity.this;

        init();
        GetpackageAPI();
        initlistner();
    }

    private void GetpackageAPI() {
        leadRequestModel = new LeadRequestModel();
        leadRequestModel.setRef_code("CLISO");
        leadController = new LeadController(this, mActivity);
        leadController.CallAPI(leadRequestModel);
    }

    private void initlistner() {

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_name.setText("");
                edt_mobile.setText("");
                edt_mail.setText("");
                edt_address.setText("");
                edt_pincode.setText("");
                edt_remarks.setText("");
                spr_package.setSelection(0);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(LeadGenerationActivity.this);
            }
        });


        spr_package.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    if (i != 0) {
                        if (leadResponseModel != null) {
                            if (leadResponseModel.size() != 0) {
                                Product_id = leadResponseModel.get(i).getProduct_id();
                                tests = leadResponseModel.get(i).getTests();
                                is_booking = leadResponseModel.get(i).getIs_booking();
                                Rate = leadResponseModel.get(i).getRate();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    SetModel();
                    postLeadResponseController = new PostLeadResponseController(LeadGenerationActivity.this, postLeadDataModel);
                    postLeadResponseController.CallAPI();
                }
            }
        });
    }

    private void SetModel() {
        try {
            if (is_booking == 1) {
                pass_product = tests;
                pass_rate = Rate;
                pass_reportcode = Product_id;
            }
            postLeadDataModel = new PostLeadDataModel();

            postLeadDataModel.setApi_key("DAuWZtKHyVb8OhjhHSuAwCB6uAyzVzASVTxXcCT8@0fkrvZ5fG9lw533tKKmxVnu");
            postLeadDataModel.setOrderid(GlobalClass.generateBookingID());
            postLeadDataModel.setAddress(edt_address.getText().toString());
            postLeadDataModel.setPincode(edt_pincode.getText().toString());
            postLeadDataModel.setProduct(pass_product);
            postLeadDataModel.setMobile(edt_mobile.getText().toString());
            postLeadDataModel.setEmail(edt_mail.getText().toString());
            postLeadDataModel.setTsp("");
            postLeadDataModel.setService_type("H");
            postLeadDataModel.setOrder_by(edt_name.getText().toString());
            postLeadDataModel.setReport_code(pass_reportcode);
            postLeadDataModel.setRate(pass_rate);
            postLeadDataModel.setHc(0);
            postLeadDataModel.setReports("N");
            postLeadDataModel.setRef_code(mobile);
            postLeadDataModel.setPay_type("Postpaid");
            postLeadDataModel.setBencount("1");
            postLeadDataModel.setBendataxml("<NewDataSet><Ben_details><Name>" + edt_name.getText().toString() + "</Name></Ben_details></NewDataSet>");
            postLeadDataModel.setRemarks(edt_remarks.getText().toString() + " ~" + spr_package.getSelectedItem().toString() + " ~CLISO");
            postLeadDataModel.setIs_booking(is_booking);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean Validate() {

        if (edt_name.getText().toString().equals("")) {
            edt_name.setError("Enter Valid Name");
            edt_name.requestFocus();
            return false;
        }


        if (edt_name.getText().toString().length() < 2) {
            edt_name.setError("Minimum length should be 2");
            edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().startsWith(" ")) {
            edt_name.setError("Cannot start with Space");
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().contains("  ")) {
            edt_name.setError("Cannot have Double Space");
            edt_name.requestFocus();
            return false;
        }

        if (edt_mobile.getText().length() != 10) {
            edt_mobile.setError("Enter Valid Number");
            edt_mobile.requestFocus();
            return false;
        }

        if (edt_mobile.getText().toString().startsWith("0") || edt_mobile.getText().toString().startsWith("1") || edt_mobile.getText().toString().startsWith("2") || edt_mobile.getText().toString().startsWith("3") || edt_mobile.getText().toString().startsWith("4") || edt_mobile.getText().toString().startsWith("5")) {
            edt_mobile.setError("Cannot start with 0,1,2,3,4,5");
            edt_mobile.requestFocus();
            return false;
        }

        if (!GlobalClass.isValidEmail(edt_mail.getText().toString())) {
            edt_mail.setError("Enter valid Email-id");
            edt_mail.requestFocus();
            return false;
        }
        if (edt_mail.getText().toString().equals("")) {
            edt_mail.setError("Enter Valid Email");
            edt_mail.requestFocus();
            return false;
        }
        if (edt_address.getText().toString().equals("")) {
            edt_address.setError("Enter Valid Address");
            edt_address.requestFocus();
            return false;
        }

        if (edt_address.getText().toString().length() < 20) {
            edt_address.setError("Minimum length should be 20");
            edt_address.requestFocus();
            return false;
        }

        if (edt_address.getText().toString().startsWith(" ")) {
            edt_address.setError("Cannot start with Space");
            edt_address.requestFocus();
            return false;
        }

        if (edt_address.getText().toString().contains("  ")) {
            edt_address.setError("Cannot have Double Space");
            edt_address.requestFocus();
            return false;
        }

        if (edt_pincode.getText().toString().equals("")) {
            edt_pincode.setError("Enter Valid Pincode");
            edt_pincode.requestFocus();
            return false;
        }

        if (edt_pincode.getText().toString().startsWith("0")) {
            edt_pincode.setError("Cannot start with 0");
            edt_pincode.requestFocus();
            return false;
        }

        if (edt_pincode.getText().toString().startsWith("9")) {
            edt_pincode.setError("Cannot start with 9");
            edt_pincode.requestFocus();
            return false;
        }

        if (edt_pincode.getText().length() != 6) {
            edt_pincode.setError("Enter Valid Pincode");
            edt_pincode.requestFocus();
            return false;
        }

        if (spr_package.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select Package Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edt_remarks.getText().toString().startsWith(" ")) {
            edt_remarks.setError("Cannot start with Space");
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    public void init() {
        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        api_key = prefs.getString("API_KEY", null);
        mobile = prefs.getString("mobile_user", null);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        edt_name = findViewById(R.id.edt_name);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_address = findViewById(R.id.edt_address);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_remarks = findViewById(R.id.edt_remarks);
        edt_mail = findViewById(R.id.edt_mail);
        tv_reset = findViewById(R.id.tv_reset);
        tv_reset.setText(Html.fromHtml("<u>" + "Reset" + "</u>"));
        spr_package = findViewById(R.id.spr_package);
        btn_submit = findViewById(R.id.btn_submit);
    }

    public void getleadresponse(LeadResponseModel body) {
        try {
            CallLeadResponse(body.getProductlist());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CallLeadResponse(ArrayList<LeadResponseModel.ProductlistBean> productlist) {
      /*  if (package_names != null) {
            package_names = null;
        }
        package_names = new ArrayList<>();

        package_names.add("Select Package*");

        leadResponseModel = productlist;*/

        leadResponseModel = new ArrayList<>();

        LeadResponseModel.ProductlistBean ent = new LeadResponseModel.ProductlistBean();
        ent.setProduct_name("Select Package*");
        leadResponseModel.add(ent);

        if (productlist != null) {
            for (int i = 0; i < productlist.size(); i++) {

                leadResponseModel.add(productlist.get(i));

               /* if (productlist.get(i).getProduct_name() != null) {
                    package_names.add(leadResponseModel.get(i).getProduct_name());
                }*/
            }

        }

        ArrayAdapter<LeadResponseModel.ProductlistBean> adapter = new ArrayAdapter<LeadResponseModel.ProductlistBean>(this, R.layout.spinner_layout, leadResponseModel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_package.setAdapter(adapter);
    }

    public void getleaddataresponse(LeadDataResponseModel body) {
        try {
            leadDataResponseModel = body;
            if (leadDataResponseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Success")
                        .setCancelable(false)
                        .setMessage(leadDataResponseModel.getRESPONSE())
                        .addButton("OK", -1, getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (true) {

                                    Intent intent = new Intent(LeadGenerationActivity.this.getApplicationContext(), LeadGenerationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    LeadGenerationActivity.this.startActivity(intent);
                                }
                            }
                        });

                builder.show();

            } else {
                GlobalClass.ShowError(this, "", "" + leadDataResponseModel.getRESPONSE(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
