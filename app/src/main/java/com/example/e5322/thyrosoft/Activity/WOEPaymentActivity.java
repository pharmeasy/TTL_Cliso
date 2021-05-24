package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Controller.PayTMStartController;
import com.example.e5322.thyrosoft.Controller.PayUStartController;
import com.example.e5322.thyrosoft.Controller.VerifyPayTmController;
import com.example.e5322.thyrosoft.Controller.VerifyPayUController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PayUReqModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.BundleConstants;
import com.example.e5322.thyrosoft.Models.StartPayTmRespModel;
import com.example.e5322.thyrosoft.Models.StartPayURespModel;
import com.example.e5322.thyrosoft.Models.StartPaytmReqModel;
import com.example.e5322.thyrosoft.Models.VerifyPayTMReqModel;
import com.example.e5322.thyrosoft.Models.VerifyPayTMRespModel;
import com.example.e5322.thyrosoft.Models.VerifyPayURespModel;
import com.example.e5322.thyrosoft.Models.VerifyPayUreqModel;
import com.example.e5322.thyrosoft.R;

public class WOEPaymentActivity extends AppCompatActivity {

    ConnectionDetector connectionDetector;
    Activity activity;
    ImageView img_add_money_payTm, img_add_money_payU;
    LinearLayout ll_paymentview, ll_payment;
    private String usercode;
    TextView tv_name, tv_mobile, tv_orderid, tv_amt;
    EditText edt_remarks;
    Button btn_proceed, btn_verify;
    private String name, mobile, amount, order_id, email, transid;
    Toolbar mToolbar;
    private int flag_pay = 0;
    WebView web_payment;
    private Integer urlid;
    private double PIC_WIDTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woepayment);

        initui();
        Navigation();
        listner();

    }

    private void Navigation() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("PAYMENT");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelTransaction();

            }
        });
    }

    private void listner() {


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag_pay == 1) {
                    StartPayUTransaction();
                } else {
                    StartPayTMTransaction();
                }


            }
        });


        img_add_money_payU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_pay = 1;
                SetViews();

            }
        });


        img_add_money_payTm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_pay = 2;
                SetViews();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_pay == 2) {
                    VerifyPayTM();
                } else {
                    VerifyPayU();
                }
            }
        });
    }

    private void VerifyPayTM() {
        VerifyPayTMReqModel verifyPayTMReqModel = new VerifyPayTMReqModel();
        verifyPayTMReqModel.setModeId("11");
        verifyPayTMReqModel.setTransactionId("" + transid);
        verifyPayTMReqModel.setURLId(urlid);
        if (connectionDetector.isConnectingToInternet()) {
            VerifyPayTmController verifyPayTmController = new VerifyPayTmController(WOEPaymentActivity.this);
            verifyPayTmController.CallAPI(verifyPayTMReqModel);
        } else {
            Toast.makeText(activity, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void StartPayUTransaction() {

        PayUReqModel payUReqModel = new PayUReqModel();

        payUReqModel.setURLId(19);
        payUReqModel.setACCode("");
        payUReqModel.setAmount("" + amount);
        payUReqModel.setBillingEmail("" + email);
        payUReqModel.setBillingMob("" + mobile);
        payUReqModel.setBillingName("" + name);
        payUReqModel.setModeId("12");
        payUReqModel.setNarrationId("2");
        payUReqModel.setOrderNo(order_id);
        payUReqModel.setSourceCode(usercode);
        payUReqModel.setTransactionDtls("");
        payUReqModel.setUserId(usercode);

        if (connectionDetector.isConnectingToInternet()) {
            PayUStartController payUStartController = new PayUStartController(WOEPaymentActivity.this);
            payUStartController.CallAPI(payUReqModel);
        } else {
            Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }


    }

    private void StartPayTMTransaction() {
        StartPaytmReqModel startPaytmReqModel = new StartPaytmReqModel();
        startPaytmReqModel.setACCode("");
        startPaytmReqModel.setUserId("9");
        startPaytmReqModel.setTransactionDtls("");
        startPaytmReqModel.setModeId("11");
        startPaytmReqModel.setNarrationId("2");
        startPaytmReqModel.setOrderNo(order_id);
        startPaytmReqModel.setSourceCode("" + usercode);
        startPaytmReqModel.setAmount("" + amount);
        startPaytmReqModel.setUserId("" + mobile);
        startPaytmReqModel.setRemarks("" + edt_remarks.getText().toString());

        if (connectionDetector.isConnectingToInternet()) {
            PayTMStartController payTMStartController = new PayTMStartController(WOEPaymentActivity.this);
            payTMStartController.CallAPI(startPaytmReqModel);
        } else {
            Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private void SetViews() {
        if (flag_pay == 1) {
            img_add_money_payTm.setVisibility(View.GONE);
            img_add_money_payU.setVisibility(View.VISIBLE);
        } else {
            img_add_money_payTm.setVisibility(View.VISIBLE);
            img_add_money_payU.setVisibility(View.GONE);
        }
        ll_paymentview.setVisibility(View.VISIBLE);
        order_id = GlobalClass.generateRandomOrderID();
        tv_name.setText("" + name);
        tv_mobile.setText("" + mobile);
        tv_amt.setText("" + amount + " /-");
        tv_orderid.setText("" + order_id);

    }

    private void initui() {
        activity = this;
        connectionDetector = new ConnectionDetector(activity);
        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
        usercode = getProfileName.getString("user_code", "");
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
//        amount = getIntent().getStringExtra("amount");
        amount = "1";
        email = getIntent().getStringExtra("email");

        img_add_money_payTm = findViewById(R.id.img_add_money_payTm);
        img_add_money_payU = findViewById(R.id.img_add_money_payU);
        tv_name = findViewById(R.id.tv_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_amt = findViewById(R.id.tv_amt);
        edt_remarks = findViewById(R.id.edt_remarks);
        btn_proceed = findViewById(R.id.btn_proceed);
        ll_paymentview = findViewById(R.id.ll_paymentview);
        ll_payment = findViewById(R.id.ll_payment);
        web_payment = findViewById(R.id.web_payment);
        btn_verify = findViewById(R.id.btn_verify);


    }

    public void getPaytmStartresponse(StartPayTmRespModel body) {
        if (!GlobalClass.isNull(body.getResponseCode()) && body.getResponseCode().equalsIgnoreCase("RES000")) {
            if (!GlobalClass.isNull(body.getTokenData())) {
                urlid = body.getReqParameters().getURLId();
                transid = body.getTransactionId();
                GlobalClass.transID = transid;
                ll_paymentview.setVisibility(View.GONE);
                web_payment.setVisibility(View.VISIBLE);
                btn_verify.setVisibility(View.VISIBLE);
                web_payment.loadUrl("about:blank");
                web_payment.canGoBack();
                web_payment.setInitialScale(getScale());
                web_payment.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        activity.setProgress(progress * 1000);
                    }
                });
                web_payment.setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(activity, "Error while loading the QR Code!" + description, Toast.LENGTH_SHORT).show();
                    }
                });
                WebSettings settings = web_payment.getSettings();
                settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                web_payment.setVerticalScrollBarEnabled(false);
                web_payment.setHorizontalScrollBarEnabled(false);
                web_payment.loadDataWithBaseURL(null, body.getTokenData(), "text/html", "UTF-8", null);


            } else {
                Toast.makeText(activity, "" + body.getResponseMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "" + body.getResponseMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public int getScale() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }


    public void getPayUstartResponse(StartPayURespModel body) {
        if (!GlobalClass.isNull(body.getResponseCode()) && body.getResponseCode().equalsIgnoreCase("RES000")) {
            Toast.makeText(activity, "" + body.getTokenData(), Toast.LENGTH_SHORT).show();
            urlid = body.getReqParameters().getURLId();
            btn_proceed.setVisibility(View.GONE);
            btn_verify.setVisibility(View.VISIBLE);
            transid = body.getTransactionId();
            GlobalClass.transID = transid;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Verify Payment")
                    .setMessage("Please click Retry to check payment status again.")
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerifyPayU();
                        }
                    })
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelTransaction();
                        }
                    }).show();
        } else {
            Toast.makeText(activity, "" + body.getResponseMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void VerifyPayU() {

        VerifyPayUreqModel verifyPayUreqModel = new VerifyPayUreqModel();
        verifyPayUreqModel.setModeId("12");
        verifyPayUreqModel.setOrderNo("" + order_id);
        verifyPayUreqModel.setTransactionId("" + transid);
        verifyPayUreqModel.setURLId(urlid);

        if (connectionDetector.isConnectingToInternet()) {
            VerifyPayUController verifyPayUController = new VerifyPayUController(WOEPaymentActivity.this);
            verifyPayUController.CallAPI(verifyPayUreqModel);
        } else {
            Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        CancelTransaction();
    }

    private void CancelTransaction() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancel Transaction")
                .setMessage("Are you sure you want to cancel this Transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void getVerifyPayTmResponse(VerifyPayTMRespModel body) {

        VerifyPayTMRespModel tempPDCRAPRM = body;
        if (tempPDCRAPRM.getStatus() != null) {

            switch (tempPDCRAPRM.getStatus()) {
                case "PAYMENT SUCCESS": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Payment Status")
                            .setMessage(tempPDCRAPRM.getResponseMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, true);

                                    finish();
                                }
                            }).show();
                    break;
                }
                case "PAYMENT FAILED": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Payment Status")
                            .setMessage(tempPDCRAPRM.getResponseMessage())
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerifyPayTM();
                        }
                    }).show();
                    break;
                }
                default: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Verify Payment")
                            .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    VerifyPayTM();
                                }
                            })
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CancelTransaction();
                                }
                            }).show();
                    break;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Verify Payment")
                    .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerifyPayTM();
                        }
                    })
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelTransaction();
                        }
                    }).show();
        }

    }

    public void getVerifyPayUResponse(VerifyPayURespModel body) {
        VerifyPayURespModel tempPDCRAPRM = body;
        if (tempPDCRAPRM.getStatus() != null) {

            switch (tempPDCRAPRM.getStatus()) {
                case "PAYMENT SUCCESS": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Payment Status")
                            .setMessage(tempPDCRAPRM.getResponseMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, true);
                                    finish();
                                }
                            }).show();
                    break;
                }
                case "PAYMENT FAILED": {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Payment Status")
                            .setMessage(tempPDCRAPRM.getResponseMessage())
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CancelTransaction();
                                }
                            }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerifyPayTM();
                        }
                    }).show();
                    break;
                }
                default: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Verify Payment")
                            .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    VerifyPayTM();
                                }
                            })
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CancelTransaction();
                                }
                            }).show();
                    break;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Verify Payment")
                    .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerifyPayTM();
                        }
                    })
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelTransaction();
                        }
                    }).show();
        }
    }
}