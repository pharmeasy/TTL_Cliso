package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import static com.example.e5322.thyrosoft.API.Api.APIKEYFORPAYMENTGATEWAY_PAYU;
import static com.example.e5322.thyrosoft.API.Api.BASE_URL_TOCHECK;
import static com.example.e5322.thyrosoft.API.Constants.AMOUNT;
import static com.example.e5322.thyrosoft.API.Constants.CALLBACK_URL;
import static com.example.e5322.thyrosoft.API.Constants.CLIENT_STATUS;
import static com.example.e5322.thyrosoft.API.Constants.CLIENT_TYPE;
import static com.example.e5322.thyrosoft.API.Constants.DICTIONARY;
import static com.example.e5322.thyrosoft.API.Constants.DOMAIN;
import static com.example.e5322.thyrosoft.API.Constants.FAILURE;
import static com.example.e5322.thyrosoft.API.Constants.GATEWAY;
import static com.example.e5322.thyrosoft.API.Constants.MOBILE;
import static com.example.e5322.thyrosoft.API.Constants.M_ID;
import static com.example.e5322.thyrosoft.API.Constants.NAME;
import static com.example.e5322.thyrosoft.API.Constants.NHF;
import static com.example.e5322.thyrosoft.API.Constants.ORDERID;
import static com.example.e5322.thyrosoft.API.Constants.ORDER_NO;
import static com.example.e5322.thyrosoft.API.Constants.ORDER_TYPE;
import static com.example.e5322.thyrosoft.API.Constants.PAYMENTSTATUS;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_AMOUNT;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_APIKEY;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_EMAIL;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_FIRSTNAME;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_MERCHANTKEY;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION1;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION10;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION2;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION3;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION4;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION5;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION6;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION7;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION8;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_OPTION9;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_PRODUCT;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_REQUEST_CHECKSUM;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_TRANSACTIONID;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_TRANSACTION_HASH;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_TRANSACTION_STATUS;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_USERCREDENTIALS;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEY_REQUEST;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEY_RESPONSE;
import static com.example.e5322.thyrosoft.API.Constants.SUCCESS;
import static com.example.e5322.thyrosoft.API.Constants.UPDATE_PAYMENT_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.CleverTapHelper;
import com.example.e5322.thyrosoft.Controller.DynamicPaymentController;
import com.example.e5322.thyrosoft.Controller.GeneratePayTMchecksum;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.VerifyPayTmChecksumController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.DyanamicPaymentReqModel;
import com.example.e5322.thyrosoft.Models.DynamicPaymentResponseModel;
import com.example.e5322.thyrosoft.Models.PayTmChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.PayTmChecksumResponseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.PaytmVerifyChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PaytmVerifyChecksumResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Payment_Activity extends AppCompatActivity {

    private Activity mActivity;
    public String merchantKey = "", userCredentials;
    public String closing_balance_pref;
    TextView txt_minamt, txt_mulamt, txt_tsp_name, title;
    Button btn_payu;
    ImageView back, home, img_add_money_payTm, img_add_money_payU;
    EditText edt_enter_amt, edt_closing_bal, et_avg_bill, et_per_day;
    ConnectionDetector cd;
    String name_tsp, user, passwrd, access, api_key, email_id, email_pref, mobile_pref, address_pref, pincode_pref, usercode, billavg, dayavg;
    Double CBamount = 0.0;
    private int tlog_paymentrequest_status_code = 0;
    private int updatepayment_status_code = 0;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private String amountTopass;
    private Global globalClass;
    private String TAG = Payment_Activity.class.getSimpleName().toString();
    private RequestQueue PostQueOtp;
    private Double Today_bill;
    private String RESPONSE;
    private String ordno;
    private String COME_FROM_SCREEN = "";
    private String unbillwoe, unbillmt, crd_amt;
    private String CLIENT_TYP = "";
    private int PaytmrequestCode = 83745;
    private String selGateway = "";
    LinearLayout ll_noauth, ll_activity_payment;
    String version, Header;
    CleverTapHelper cleverTapHelper;
    private String BalancePref;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_);

        mActivity = Payment_Activity.this;
        globalClass = new Global(mActivity);

        title = (TextView) findViewById(R.id.title);
        txt_minamt = (TextView) findViewById(R.id.txt_minamt);
        txt_mulamt = (TextView) findViewById(R.id.txt_mulamt);
        txt_tsp_name = (TextView) findViewById(R.id.txt_tsp_name);
        btn_payu = (Button) findViewById(R.id.btn_payu);
        edt_enter_amt = (EditText) findViewById(R.id.edt_enter_amt);
        edt_closing_bal = (EditText) findViewById(R.id.edt_closing_bal);
        et_avg_bill = findViewById(R.id.edt_per_billing);
        et_per_day = findViewById(R.id.edt_day_per);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        img_add_money_payTm = (ImageView) findViewById(R.id.img_add_money_payTm);
        img_add_money_payU = (ImageView) findViewById(R.id.img_add_money_payU);
        ll_activity_payment = findViewById(R.id.ll_activity_payment);
        ll_noauth = findViewById(R.id.ll_noauth);

        Payu.setInstance(this);
        int environment = PayuConstants.STAGING_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        mPaymentParams = new PaymentParams();

        cd = new ConnectionDetector(Payment_Activity.this);
        GetDynamicPayment();

        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
        name_tsp = getProfileName.getString("name", null);
        usercode = getProfileName.getString("user_code", null);
        email_id = getProfileName.getString("email", null);
        address_pref = getProfileName.getString("address", null);
        pincode_pref = getProfileName.getString("pincode", null);
        billavg = getProfileName.getString(Constants.Billamount, null);
        BalancePref = getProfileName.getString(Constants.balance, null);

        // closing_balance_pref = getProfileName.getString("closing_balance", null);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        email_pref = prefs.getString("email", null);
        mobile_pref = prefs.getString("mobile_user", null);
        CLIENT_TYP = prefs.getString("CLIENT_TYPE", "");


        COME_FROM_SCREEN = getIntent().getStringExtra("COMEFROM");
        title.setText("Online Payment");
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        Header = "Cliso App " + ", " + version;
        cleverTapHelper = new CleverTapHelper(mActivity);

        if (Global.getLoginType(mActivity) == Constants.PEflag || CLIENT_TYP.equalsIgnoreCase(NHF)) {
            ll_activity_payment.setVisibility(View.GONE);
            ll_noauth.setVisibility(View.VISIBLE);
        } else {
            if (globalClass.checkForApi21()) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.limaroon));
            }

            getProfileDetails();


            txt_minamt.setText("\u2022 " + Html.fromHtml("Minimum amount to be paid 5000"));
            txt_mulamt.setText("\u2022 " + Html.fromHtml("Amount needs to be paid in multiples of 5000"));
            txt_tsp_name.setText(name_tsp + " - " + usercode);
            /* edt_closing_bal.setText(closing_balance_pref);*/


            edt_enter_amt.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String enteredString = s.toString();
                    if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                            enteredString.startsWith("#") || enteredString.startsWith("$") ||
                            enteredString.startsWith("%") || enteredString.startsWith("^") ||
                            enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("0")) {
                        Toast.makeText(Payment_Activity.this,
                                ToastFile.ent_crt_amt,
                                Toast.LENGTH_SHORT).show();

                        if (enteredString.length() > 0) {
                            edt_enter_amt.setText(enteredString.substring(1));
                        } else {
                            edt_enter_amt.setText("");
                        }

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btn_payu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                proceedWithPayment();
                }
            });

            img_add_money_payU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selGateway = "payU";
                    proceedWithPayment();
                }
            });

            img_add_money_payTm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selGateway = "payTm";
                    proceedWithPayment();
                }
            });
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Payment_Activity.this);

            }
        });


    }

    private void GetDynamicPayment() {
        if (cd.isConnectingToInternet()) {
            DyanamicPaymentReqModel dyanamicPaymentReqModel = new DyanamicPaymentReqModel();
            dyanamicPaymentReqModel.setAppId("" + Constants.USER_APPID);
            DynamicPaymentController dynamicPaymentController = new DynamicPaymentController(Payment_Activity.this);
            dynamicPaymentController.CallAPI(dyanamicPaymentReqModel);
        } else {
            Global.showCustomToast(mActivity, ToastFile.intConnection);
        }
    }

    private void proceedWithPayment() {
        amountTopass = edt_enter_amt.getText().toString();
        Log.e(TAG, "Entered Amount ----->" + amountTopass);
        Log.e(TAG, "CB Amount ----->" + CBamount);
        // System.out.println("Recharge Start : " + Header + "," + selGateway + "," + amountTopass + "," + BalancePref + "," + crd_amt);
        cleverTapHelper.rechargeStartEvent(Header, selGateway, amountTopass, BalancePref, crd_amt);
        if (amountTopass.equals("")) {
            Toast.makeText(Payment_Activity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
        } else if (CBamount < Constants.PAYAMOUNT) {
            if (Integer.parseInt(amountTopass) >= Constants.PAYAMOUNT) {
                gotopayGatway();
            } else {
                new SweetAlertDialog(Payment_Activity.this, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(getString(R.string.amt_str))
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        } else if (Constants.PAYAMOUNT <= CBamount) {
            if (Integer.parseInt(amountTopass) >= CBamount) {
                gotopayGatway();
            } else {
                new SweetAlertDialog(Payment_Activity.this, SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Enter Minimum Amount of Rs " + CBamount + " to Proceed")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        }
    }

    private void gotopayGatway() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(Payment_Activity.this);
            try {
                PostQueOtp = GlobalClass.setVolleyReq(Payment_Activity.this);
                Log.e(TAG, "ORDER NO URL ---->" + Api.GenerateId + api_key + "/TSP/Generatedordnum");
                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.GET, Api.GenerateId + api_key + "/TSP/Generatedordnum", new com.android.volley.Response.Listener<JSONObject>() {
                    public String RES_ID;

                    @Override
                    public void onResponse(JSONObject response) {
                        GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            String finalJson = response.toString();
                            JSONObject parentObjectOtp = new JSONObject(finalJson);
                            RESPONSE = parentObjectOtp.getString("RESPONSE");
                            RES_ID = parentObjectOtp.getString("RES_ID");
                            ordno = parentObjectOtp.getString("ordno");
                            if (!GlobalClass.isNull(RESPONSE) && RESPONSE.equals("SUCCESS")) {
                                if (selGateway.equalsIgnoreCase("payU")) {
                                    startPayUTransaction(name_tsp, "", amountTopass, ordno);
                                } else if (selGateway.equalsIgnoreCase("payTm")) {
                                    generatePayTmChecksum(amountTopass, ordno);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
                    }
                });
                PostQueOtp.add(jsonObjectRequest1);
                GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
                Log.e(TAG, "SendFeedbackToAPI: url" + jsonObjectRequest1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    /**
                     * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                     * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                     *
                     * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                     * for identifying status of transaction. There are two possible status like, success or failure
                     **/
                    Log.e("onActivityResult: ", "Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"));
                    JSONObject Payu_response;
                    try {
                        edt_enter_amt.setText("");
                        Payu_response = new JSONObject(data.getStringExtra("payu_response"));
                        if (cd.isConnectingToInternet())

                            new AsyncTaskVerifypayUmoneyTransaction(Payu_response).execute();
                        else
                            GlobalClass.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet), 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
                }
            }


        } else if (requestCode == PaytmrequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

    public void startPayUTransaction(String name, String product, String amount, String orderid) {
        merchantKey = PAYUMONEYKEY_MERCHANTKEY;
        String email = email_id;
        String mobile = mobile_pref;
        userCredentials = merchantKey + ":" + email;

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setProductInfo("CLISO LEDGER UPDATE");
        mPaymentParams.setFirstName(name);
        mPaymentParams.setEmail(email);
        mPaymentParams.setPhone(mobile);
        mPaymentParams.setTxnId(orderid);// Transaction Id should be kept unique for each transaction.
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");//Surl --> Success url is where the transaction response is posted by PayU on successful transaction
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");//Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay
        //udf1 to udf5 are options params where you can pass additional information related to transaction.
        //If you don't want to use it, then send them as empty string like, udf1=""
        mPaymentParams.setUdf1(user);
        mPaymentParams.setUdf2("");
        mPaymentParams.setUdf3("");
        mPaymentParams.setUdf4("");
        mPaymentParams.setUdf5("");
        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        mPaymentParams.setUserCredentials(userCredentials);
        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");
        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();

        if (merchantKey.equalsIgnoreCase("y04wX2")) {
            payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);// Live
        } else {
            payuConfig.setEnvironment(PayuConstants.STAGING_ENV);//Staging-----
        }

        if (cd.isConnectingToInternet())
            new AsyncTaskstartpayUmoneyTransaction(mPaymentParams).execute();
        else
            GlobalClass.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet), 0);

        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        // generateHashFromServer(mPaymentParams);
        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
//         String salt = "V76vIBJq";
//          generateHashFromSDK(mPaymentParams, salt);
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public void launchSdkUI(PayuHashes payuHashes) {

        mPaymentParams.setHash(payuHashes.getPaymentHash());
        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
//        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
        fetchMerchantHashes(intent);
    }

    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);
                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }

    public void getProfileDetails() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(Payment_Activity.this);
        RequestQueue queue = GlobalClass.setVolleyReq(Payment_Activity.this);

        Log.e(TAG, "Get my Profile ---->" + Api.Cloud_base + api_key + "/" + user + "/" + "getmyprofile");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.Cloud_base + api_key + "/" + user + "/" + "getmyprofile",
                new Response.Listener<JSONObject>() {
                    public String tsp_img;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
                            Log.e(TAG, "onResponse: " + response);

                            if (!GlobalClass.isNull(response.getString(Constants.closing_balance))) {
                                closing_balance_pref = response.getString(Constants.closing_balance);
                            } else {
                                closing_balance_pref = "0";
                            }

                            if (!GlobalClass.isNull(response.getString(Constants.credit_limit))) {
                                crd_amt = response.getString(Constants.credit_limit);
                            } else {
                                crd_amt = "0";
                            }

                            if (!GlobalClass.isNull(response.getString(Constants.unbilledMaterial))) {
                                unbillmt = response.getString(Constants.unbilledMaterial);
                            } else {
                                unbillmt = "0";
                            }

                            unbillwoe = response.getString(Constants.unbilledWOE);
                            billavg = response.getString(Constants.Billamount);
                            dayavg = response.getString(Constants.DAYAVG);
                            Today_bill = Double.parseDouble(unbillmt) + Double.parseDouble(unbillwoe);

                            CBamount = (Double.parseDouble(closing_balance_pref) + Today_bill) - Double.parseDouble(crd_amt);


                            if (!GlobalClass.isNull(billavg)) {
                                et_avg_bill.setText(billavg);
                            } else {
                                et_avg_bill.setText(0);
                            }

                            if (!GlobalClass.isNull(dayavg)) {
                                et_per_day.setText(dayavg);
                            } else {
                                et_per_day.setText(0);
                            }


                            edt_closing_bal.setText("" + String.format("%.2f", CBamount));

                            SharedPreferences.Editor saveProfileDetails = getSharedPreferences("profile", 0).edit();
                            saveProfileDetails.putString("closing_balance", closing_balance_pref);
                            saveProfileDetails.commit();

                        } catch (Exception e) {
                            Log.e(TAG, "on error --->" + e.getLocalizedMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "getProfileDetails: url" + jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.putExtra("message", "ok");//Put Message to pass over intent
        setResult(1, intent);
    }

    class AsyncTaskstartpayUmoneyTransaction extends AsyncTask<Void, Void, JSONObject> {

        String orderamount, orderno;
        PaymentParams mPayment_Params;
        JSONObject jobj, jobj1, postdata;
        ProgressDialog progressDialog;

        public AsyncTaskstartpayUmoneyTransaction(PaymentParams mPaymentParams) {
            this.mPayment_Params = mPaymentParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GlobalClass.setLoadingGIF(Payment_Activity.this);
            progressDialog = GlobalClass.ShowprogressDialog(Payment_Activity.this);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            String strUrl = BASE_URL_TOCHECK + "PaymentGateway.svc/PayUChecksum";
            Log.e(TAG, "doInBackground: " + strUrl);
            Log.e(TAG, "CHECKSUM  URL ---->" + BASE_URL_TOCHECK + "PaymentGateway.svc/PayUChecksum");
            jobj = new JSONObject();
            try {
                jobj.put(PAYUMONEYKEY_APIKEY, APIKEYFORPAYMENTGATEWAY_PAYU);
                jobj.put(PAYUMONEYKEY_TRANSACTIONID, mPayment_Params.getTxnId());
                jobj.put(PAYUMONEYKEY_AMOUNT, mPayment_Params.getAmount());
                jobj.put(PAYUMONEYKEY_PRODUCT, mPayment_Params.getProductInfo());
                jobj.put(PAYUMONEYKEY_FIRSTNAME, mPayment_Params.getFirstName());
                jobj.put(PAYUMONEYKEY_EMAIL, mPayment_Params.getEmail());
                jobj.put(PAYUMONEYKEY_OPTION1, user);
                jobj.put(PAYUMONEYKEY_OPTION2, "");
                jobj.put(PAYUMONEYKEY_OPTION3, "");
                jobj.put(PAYUMONEYKEY_OPTION4, "");
                jobj.put(PAYUMONEYKEY_OPTION5, "");
                jobj.put(PAYUMONEYKEY_OPTION6, "");
                jobj.put(PAYUMONEYKEY_OPTION7, "");
                jobj.put(PAYUMONEYKEY_OPTION8, "");
                jobj.put(PAYUMONEYKEY_OPTION9, "");
                jobj.put(PAYUMONEYKEY_OPTION10, "");
                jobj.put(PAYUMONEYKEY_USERCREDENTIALS, mPayment_Params.getUserCredentials());

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(strUrl);

            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = jobj.toString();
                System.out.println("Sending data: " + strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
                    Log.e(TAG, "doInBackground_response: " + result);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                if (result != null) {
                    json = new JSONObject(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    Log.e(TAG, "onPostExecute: result" + result);
                    if (result.getString("RESPONSE").equalsIgnoreCase("SUCCESS")) {
                        if (!GlobalClass.isNull(result.getString("encCheckSum"))) {
                            PayuHashes payuHashes = new PayuHashes();
                            Iterator<String> payuHashIterator = result.keys();
                            while (payuHashIterator.hasNext()) {
                                String key = payuHashIterator.next();
                                switch (key) {
                                    //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                                    case "encCheckSum":
                                        payuHashes.setPaymentHash(result.getString(key));
                                        break;
                                    case "vas_for_mobile_sdk_hash":
                                        payuHashes.setVasForMobileSdkHash(result.getString(key));
                                        break;
                                    case "payment_related_details_for_mobile_sdk":
                                        payuHashes.setPaymentRelatedDetailsForMobileSdkHash(result.getString(key));
                                        break;
                                    //TODO Below hashes only needs to be generated if you are using Store card feature
                                    case "delete_user_card":
                                        payuHashes.setDeleteCardHash(result.getString(key));
                                        break;
                                    case "get_user_cards":
                                        payuHashes.setStoredCardsHash(result.getString(key));
                                        break;
                                    case "edit_user_card":
                                        payuHashes.setEditCardHash(result.getString(key));
                                        break;
                                    case "save_user_card":
                                        payuHashes.setSaveCardHash(result.getString(key));
                                        break;
                                    //TODO This hash needs to be generated if you are using any offer key
                                    case "check_offer_status_hash":
                                        payuHashes.setCheckOfferStatusHash(result.getString(key));
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if (cd.isConnectingToInternet()) {

                                jobj1 = new JSONObject();
                                jobj1.put(PAYUMONEYKEY_APIKEY, api_key);
                                jobj1.put(PAYUMONEYKEY_PRODUCT, "");
                                jobj1.put(PAYUMONEYKEY_USERCREDENTIALS, mPayment_Params.getUserCredentials());
                                jobj1.put(PAYUMONEYKEY_REQUEST_CHECKSUM, result.getString("encCheckSum"));

                                postdata = new JSONObject();
                                postdata.put(DICTIONARY, jobj1.toString());//jobj contains all the parameters which are used for Payu payment request.
                                postdata.put(ORDER_NO, mPayment_Params.getTxnId());
                                postdata.put(PAYMENTSTATUS, result.optString("RESPONSE", ""));
                                postdata.put(ORDER_TYPE, "TSP");
                                postdata.put(DOMAIN, "CLISOAPP");
                                postdata.put(AMOUNT, mPayment_Params.getAmount());
                                postdata.put(MOBILE, user);
                                postdata.put(NAME, mPayment_Params.getFirstName());
                                postdata.put(GATEWAY, PAYUMONEY_REQUEST);

                                new AsyncTask_Log_Payment_Request(postdata, payuHashes, "").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                GlobalClass.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet), 0);
                            }

                        } else {
                            System.out.println("Success but no Checksum");
                        }
                    } else {
                        System.out.println("failed to generate Checksum 1");
                    }
                } else {
                    System.out.println("failed to generate Checksum 2");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
        }
    }

    class AsyncTask_Log_Payment_Request extends AsyncTask<Void, Void, JSONObject> {

        JSONObject postdata = new JSONObject();
        PayuHashes payuHashes;
        String checksum_token = "";

        public AsyncTask_Log_Payment_Request(JSONObject result, PayuHashes payuHashes1, String checksum_token1) {
            this.postdata = result;
            this.payuHashes = payuHashes1;
            this.checksum_token = checksum_token1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String strUrl = BASE_URL_TOCHECK + UPDATE_PAYMENT_URL;
            System.out.println(strUrl);
            Log.e(TAG, "doInBackground: " + strUrl);
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = postdata.toString();
                System.out.println("Sending data: " + strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                tlog_paymentrequest_status_code = httpResponse.getStatusLine().getStatusCode();
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                if (result != null) {
                    json = new JSONObject(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                if (tlog_paymentrequest_status_code == 200) {
                    if (result != null) {
                        System.out.println("Response logs " + result.toString());
                        String value = result.optString("RESPONSE", "");
                        String res_id = result.optString("RES_ID", "");
                        System.out.println("Response from server: " + value);
                        if (GlobalClass.checkEqualIgnoreCase(value, "SUCCESS")) {
                            GlobalClass.showCustomToast(Payment_Activity.this, "Redirecting to Payment gateway..", 0);
                            System.out.println("Successful");
                            if (selGateway.equalsIgnoreCase("payU")) {
                                launchSdkUI(payuHashes);// Start PayU transaction request.
                            } else if (selGateway.equalsIgnoreCase("payTm")) {
                                start_paytm_transaction(checksum_token, postdata.optString(AMOUNT, ""), postdata.optString(ORDER_NO, ""));
                            }
                        } else {
                            System.out.println("Unsuccessful");
                            GlobalClass.showCustomToast(Payment_Activity.this, MessageConstants.RETRY, 0);
                        }
                    } else {
                        System.out.println("failed result is null ");
                        GlobalClass.showCustomToast(Payment_Activity.this, MessageConstants.RETRY, 0);
                    }
                } else {
                    System.out.println("failed status code ");
                    GlobalClass.showCustomToast(Payment_Activity.this, MessageConstants.RETRY, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getpostresponse(DynamicPaymentResponseModel dynamicPaymentResponseModel) {

        if (dynamicPaymentResponseModel != null) {
            if (!GlobalClass.isNull(dynamicPaymentResponseModel.getRespId()) && dynamicPaymentResponseModel.getRespId().equalsIgnoreCase("RES0000")) {
                if (GlobalClass.CheckArrayList(dynamicPaymentResponseModel.getPayModeLists())) {
                    for (int i = 0; i < dynamicPaymentResponseModel.getPayModeLists().size(); i++) {
                        if (!GlobalClass.isNull(dynamicPaymentResponseModel.getPayModeLists().get(i).getPaymentGatway())) {
                            if (dynamicPaymentResponseModel.getPayModeLists().get(i).getPaymentGatway().equalsIgnoreCase("PayTM")) {
                                img_add_money_payTm.setVisibility(View.VISIBLE);
                            }
                            if (dynamicPaymentResponseModel.getPayModeLists().get(i).getPaymentGatway().equalsIgnoreCase("PayU")) {
                                img_add_money_payU.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    img_add_money_payTm.setVisibility(View.VISIBLE);
                }
            } else {
                img_add_money_payTm.setVisibility(View.VISIBLE);
                Global.showCustomToast(mActivity, dynamicPaymentResponseModel.getResponse());
            }
        } else {
            img_add_money_payTm.setVisibility(View.VISIBLE);
        }


    }

    class AsyncTaskVerifypayUmoneyTransaction extends AsyncTask<Void, Void, JSONObject> {

        String orderamount, orderno;
        JSONObject mPayU_response, postdata;
        ProgressDialog progressDialog;

        public AsyncTaskVerifypayUmoneyTransaction(JSONObject mPayUresponse) {

            this.mPayU_response = mPayUresponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GlobalClass.setLoadingGIF(Payment_Activity.this);
            progressDialog = GlobalClass.ShowprogressDialog(Payment_Activity.this);

        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String strUrl = BASE_URL_TOCHECK + "PaymentGateway.svc/PayUChecksumVerification";
            Log.e(TAG, "doInBackground: " + strUrl);
            JSONObject jobj = new JSONObject();
            try {
                jobj.put(PAYUMONEYKEY_APIKEY, APIKEYFORPAYMENTGATEWAY_PAYU);
                jobj.put(PAYUMONEYKEY_TRANSACTIONID, mPayU_response.getString("txnid"));
                jobj.put(PAYUMONEYKEY_AMOUNT, mPayU_response.getString("amount"));
                jobj.put(PAYUMONEYKEY_PRODUCT, mPayU_response.getString("productinfo"));
                jobj.put(PAYUMONEYKEY_FIRSTNAME, mPayU_response.getString("firstname"));
                jobj.put(PAYUMONEYKEY_EMAIL, mPayU_response.getString("email"));
                jobj.put(PAYUMONEYKEY_OPTION1, user);
                jobj.put(PAYUMONEYKEY_OPTION2, "");
                jobj.put(PAYUMONEYKEY_OPTION3, "");
                jobj.put(PAYUMONEYKEY_OPTION4, "");
                jobj.put(PAYUMONEYKEY_OPTION5, "");
                jobj.put(PAYUMONEYKEY_OPTION6, "");
                jobj.put(PAYUMONEYKEY_OPTION7, "");
                jobj.put(PAYUMONEYKEY_OPTION8, "");
                jobj.put(PAYUMONEYKEY_OPTION9, "");
                jobj.put(PAYUMONEYKEY_OPTION10, "");
                jobj.put(PAYUMONEYKEY_USERCREDENTIALS, mPayU_response.getString("key") + ":" + mPayU_response.getString("email"));
                jobj.put(PAYUMONEYKEY_TRANSACTION_STATUS, mPayU_response.getString("status"));//this will be empty in case of generate Checksum
                jobj.put(PAYUMONEYKEY_TRANSACTION_HASH, mPayU_response.getString("hash"));//this will be empty in case of generate Checksum

                Gson gson = new Gson();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(strUrl);

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = jobj.toString();
                System.out.println("Sending data: " + strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader(Constants.HEADER_USER_AGENT, Constants.APPNAME + "/" + GlobalClass.getversioncode(mActivity) + "(" + GlobalClass.getversioncode(mActivity) + ")" + GlobalClass.getSerialnum(mActivity));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                if (result != null) {
                    json = new JSONObject(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
            try {
                if (result != null) {
                    Log.e(TAG, "onPostExecute: " + result);
                    postdata = null;
                    postdata = new JSONObject();
                    postdata.put(DICTIONARY, mPayU_response.toString());//mPayU_response data contains all data got from payU transaction response
                    postdata.put(GATEWAY, PAYUMONEY_RESPONSE);
                    postdata.put(ORDERID, mPayU_response.getString("txnid"));
                    postdata.put(MOBILE, user);
                    postdata.put(CLIENT_TYPE, "TSP");

                    if (result.getString("trnVer_status") != null && !result.getString("trnVer_status").isEmpty() && result.getString("trnVer_status").equalsIgnoreCase("SUCCESS")) {
                        if (mPayU_response.getString("status").equalsIgnoreCase("SUCCESS")) {
                            postdata.put(CLIENT_STATUS, SUCCESS);
                            postdata.put(PAYMENTSTATUS, SUCCESS);
                            callResponseLogAPI(postdata, SUCCESS);
                        } else {
                            postdata.put(CLIENT_STATUS, FAILURE);
                            postdata.put(PAYMENTSTATUS, FAILURE);
                            callResponseLogAPI(postdata, FAILURE);
                        }
                    } else {
                        postdata.put(CLIENT_STATUS, FAILURE);
                        postdata.put(PAYMENTSTATUS, FAILURE);
                        callResponseLogAPI(postdata, FAILURE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
        }
    }

    class AsyncTaskupdatepaymentstatus extends AsyncTask<Void, Void, JSONObject> {
        JSONObject postdata = new JSONObject();
        String paymentstatus, currency;
        ProgressDialog progressDialog;

        public AsyncTaskupdatepaymentstatus(JSONObject result, String paymentstatus) {
            this.postdata = result;
            this.paymentstatus = paymentstatus;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GlobalClass.setLoadingGIF(Payment_Activity.this);
            progressDialog = GlobalClass.ShowprogressDialog(Payment_Activity.this);

        }

        @Override
        protected JSONObject doInBackground(Void... params) {


            String strUrl = Api.THYROCARE + "PaymentGateway.svc/PaymentLog_franchise";
            Log.e(TAG, "doInBackground: " + strUrl);
            System.out.println(strUrl);

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = postdata.toString();
                System.out.println("Sending data: " + strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader(Constants.HEADER_USER_AGENT, Constants.APPNAME + "/" + GlobalClass.getversioncode(mActivity) + "(" + GlobalClass.getversioncode(mActivity) + ")" + GlobalClass.getSerialnum(mActivity));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                updatepayment_status_code = httpResponse.getStatusLine().getStatusCode();

                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                GlobalClass.hideProgress(Payment_Activity.this, progressDialog);
                if (updatepayment_status_code == 200) {
                    if (result != null) {
                        Log.e(TAG, "onPostExecute: " + result);
                        JSONArray Dictionary_Array = result.getJSONArray("dictionary");
                        if (Dictionary_Array != null) {
                            JSONObject jObject = new JSONObject();
                            for (int i = 0; i < Dictionary_Array.length(); i++) {
                                jObject = Dictionary_Array.getJSONObject(i);
                            }
                            String value = jObject.getString("Value");
                            String Key = jObject.getString("Key");
                            System.out.println("Response from server: " + value);
    /*
                            if (!value.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(Payment_Activity.this, "Something went wrong - Response log failed", Toast.LENGTH_SHORT).show();
                            }*/

                            if (paymentstatus.equalsIgnoreCase(SUCCESS)) {
                                cleverTapHelper.rechargeSuccessEvent(Header, selGateway, amountTopass, BalancePref, crd_amt, ordno, true, "");
                                Toast.makeText(Payment_Activity.this, "Transaction successful...", Toast.LENGTH_SHORT).show();
                                edt_enter_amt.setText("");
                            } else {
                                cleverTapHelper.rechargeSuccessEvent(Header, selGateway, amountTopass, BalancePref, crd_amt, ordno, false, "Transaction failed.");
                                Toast.makeText(mActivity, "Transaction failed.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            System.out.println("Dictionary_Array is null ");
                            Toast.makeText(Payment_Activity.this, "UNSUCCESSFUL ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("Result is null ");
                        Toast.makeText(Payment_Activity.this, "UNSUCCESSFUL ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("failed status code " + result.toString());
                    Toast.makeText(Payment_Activity.this, "UNSUCCESSFUL ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generatePayTmChecksum(String amountTopass, String ordno) {
        try {
            PayTmChecksumRequestModel payTmChecksumRequestModel = new PayTmChecksumRequestModel();
            payTmChecksumRequestModel.setApi_key(APIKEYFORPAYMENTGATEWAY_PAYU);
            payTmChecksumRequestModel.setTxtAmount(amountTopass);
            payTmChecksumRequestModel.setTxtORDID(ordno);
            payTmChecksumRequestModel.setType("GENCHECKSUM");
            payTmChecksumRequestModel.setTxtMobile(mobile_pref);
            payTmChecksumRequestModel.setTxtCustID(mobile_pref);
            payTmChecksumRequestModel.setTxtEmail(email_id);
            if (Api.THYROCARE.contains("APIs")) {
                payTmChecksumRequestModel.setTxtCallBack(CALLBACK_URL + ordno);
            } else {
                payTmChecksumRequestModel.setTxtCallBack(CALLBACK_URL);
            }
            payTmChecksumRequestModel.setTxtWebsite(Constants.WEBSITE);
            payTmChecksumRequestModel.setTxtINDID(Constants.INDUSTRY_TYPE_ID);

            if (cd.isConnectingToInternet()) {
                GeneratePayTMchecksum generatePayTMchecksum = new GeneratePayTMchecksum(this);
                generatePayTMchecksum.CallAPI(payTmChecksumRequestModel);
            } else {
                GlobalClass.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getChecksumResponse(PayTmChecksumResponseModel responseModel, String txtAmount, String txtORDID) {
        if (!GlobalClass.isNull(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase(Constants.RES0001)) {
            if (!GlobalClass.isNull(responseModel.getTransToken())) {
                logPaymentRequest(responseModel.getTransToken(), txtAmount, txtORDID, responseModel.getRESPONSE());
            } else {
                GlobalClass.showCustomToast(Payment_Activity.this, "Checksum token generation failed", 0);
            }
        } else {
            GlobalClass.printLog(Constants.ERROR, TAG, "PayTm Checksum token", responseModel.getRESPONSE());
            GlobalClass.showCustomToast(Payment_Activity.this, GlobalClass.checkAndGetStringValue(responseModel.getRESPONSE(), MessageConstants.RETRY), 0);
        }
    }

    private void logPaymentRequest(String Checksum_token, String orderamount, String orderno, String response) {
        try {
            if (cd.isConnectingToInternet()) {
                JSONObject jobj1 = new JSONObject();
                jobj1.put(PAYUMONEYKEY_APIKEY, api_key);
                jobj1.put(PAYUMONEYKEY_REQUEST_CHECKSUM, Checksum_token);
                JSONObject postdata = new JSONObject();
                postdata.put(DICTIONARY, jobj1.toString());
                postdata.put(ORDER_NO, orderno);
                postdata.put(PAYMENTSTATUS, response);
                postdata.put(ORDER_TYPE, "TSP");
                postdata.put(DOMAIN, "CLISOAPP");
                postdata.put(AMOUNT, orderamount);
                postdata.put(MOBILE, user);
                postdata.put(NAME, name_tsp);
                postdata.put(GATEWAY, Constants.PAYTM_REQUEST);
                new AsyncTask_Log_Payment_Request(postdata, null, Checksum_token).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                GlobalClass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start_paytm_transaction(String Checksum_token, String orderamount, String orderno) {
       /* PaytmPGService Service;
        if (Api.THYROCARE.contains("APIs")) {
            Service = PaytmPGService.getProductionService();//live ---
        } else {
            Service = PaytmPGService.getStagingService();//Staging-----
        }*/

        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        final Map<String, String> paramMap = new HashMap<String, String>();
        //TODO NHL conditions
        if (CLIENT_TYP.equalsIgnoreCase(Constants.NHF)) {
            paramMap.put("MID", Constants.M_ID_NHL);
        } else {
            paramMap.put("MID", M_ID);
        }

        paramMap.put("ORDER_ID", orderno);
        paramMap.put("CUST_ID", mobile_pref);
        paramMap.put("INDUSTRY_TYPE_ID", Constants.INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID", Constants.CHANNEL_ID);
        paramMap.put("TXN_AMOUNT", orderamount);
        paramMap.put("WEBSITE", Constants.WEBSITE);
        if (Api.THYROCARE.contains("APIs")) {
            paramMap.put("CALLBACK_URL", CALLBACK_URL + orderno);//live ---
        } else {
            paramMap.put("CALLBACK_URL", CALLBACK_URL);//Staging-----
        }
        paramMap.put("EMAIL", email_id);
        paramMap.put("MOBILE_NO", mobile_pref);
        paramMap.put("CHECKSUMHASH", Checksum_token);
//        PaytmOrder Order = new PaytmOrder(paramMap);

        //TODO Earlier Log request API call

       /* try {
            Service.initialize(Order, null);
            GlobalClass.showCustomToast(mActivity, ToastFile.PAYTM_REDITECTION, 0);

            Service.startPaymentTransaction(mActivity, true, true,
                    new PaytmPaymentTransactionCallback() {

                        @Override
                        public void someUIErrorOccurred(String inErrorMessage) {
                            // Some UI Error Occurred in Payment Gateway Activity.
                            // // This may be due to initialization of views in
                            // Payment Gateway Activity or may be due to //
                            // initialization of webview. // Error Message details
                            // the error occurred.
                            GlobalClass.showCustomToast(mActivity, inErrorMessage, 0);
                        }

                        @Override
                        public void onTransactionResponse(Bundle inResponse) {
                            String STATUS, CHECKSUMHASH, BANKNAME, ORDERID, TXNAMOUNT, TXNDATE, MID, TXNID, RESPCODE, PAYMENTMODE, BANKTXNID, CURRENCY, GATEWAYNAME, RESPMSG;
                            STATUS = inResponse.getString("STATUS");
                            CHECKSUMHASH = inResponse.getString("CHECKSUMHASH");
                            BANKNAME = inResponse.getString("BANKNAME");
                            ORDERID = inResponse.getString("ORDERID");
                            TXNAMOUNT = inResponse.getString("TXNAMOUNT");
                            TXNDATE = inResponse.getString("TXNDATE");
                            MID = inResponse.getString("MID");
                            TXNID = inResponse.getString("TXNID");
                            RESPCODE = inResponse.getString("RESPCODE");
                            PAYMENTMODE = inResponse.getString("PAYMENTMODE");
                            BANKTXNID = inResponse.getString("BANKTXNID");
                            CURRENCY = inResponse.getString("CURRENCY");
                            GATEWAYNAME = inResponse.getString("GATEWAYNAME");
                            RESPMSG = inResponse.getString("RESPMSG");

                            callVerifyPayTmChecksum(MID, ORDERID);
                        }

                        @Override
                        public void networkNotAvailable() {
                            // If network is not
                            // available, then this
                            // method gets called.
                            GlobalClass.showCustomToast(mActivity, ToastFile.NETWORK_NOT_AVAILABLE, 0);
                        }

                        @Override
                        public void clientAuthenticationFailed(String inErrorMessage) {
                            // This method gets called if client authentication
                            // failed. // Failure may be due to following reasons //
                            // 1. Server error or downtime. // 2. Server unable to
                            // generate checksum or checksum response is not in
                            // proper format. // 3. Server failed to authenticate
                            // that client. That is value of payt_STATUS is 2. //
                            // Error Message describes the reason for failure.
                            GlobalClass.showCustomToast(mActivity, "clientAuthenticationFailed : " + inErrorMessage, 0);
                        }

                        @Override
                        public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                            GlobalClass.showCustomToast(mActivity, "onErrorLoadingWebPage : " + inErrorMessage, 0);
                        }

                        // had to be added: NOTE
                        @Override
                        public void onBackPressedCancelTransaction() {
                            // TODO Auto-generated method stub
                            Global.showCustomToast(mActivity, ToastFile.TRANSACTION_CANCELLED);
                        }

                        @Override
                        public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                            Global.showCustomToast(mActivity, ToastFile.PAYMENT_TRANSACTION_FAILED);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            PaytmOrder paytmOrder = new PaytmOrder(orderno, M_ID, Checksum_token, orderamount, CALLBACK_URL + orderno);
            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(@Nullable @org.jetbrains.annotations.Nullable Bundle inResponse) {
                    System.out.println("onTransactionResponse >> " + inResponse);
                    String STATUS, CHECKSUMHASH, BANKNAME, ORDERID, TXNAMOUNT, TXNDATE, MID, TXNID, RESPCODE, PAYMENTMODE, BANKTXNID, CURRENCY, GATEWAYNAME, RESPMSG;
                    STATUS = inResponse.getString("STATUS");
                    CHECKSUMHASH = inResponse.getString("CHECKSUMHASH");
                    BANKNAME = inResponse.getString("BANKNAME");
                    ORDERID = inResponse.getString("ORDERID");
                    TXNAMOUNT = inResponse.getString("TXNAMOUNT");
                    TXNDATE = inResponse.getString("TXNDATE");
                    MID = inResponse.getString("MID");
                    TXNID = inResponse.getString("TXNID");
                    RESPCODE = inResponse.getString("RESPCODE");
                    PAYMENTMODE = inResponse.getString("PAYMENTMODE");
                    BANKTXNID = inResponse.getString("BANKTXNID");
                    CURRENCY = inResponse.getString("CURRENCY");
                    GATEWAYNAME = inResponse.getString("GATEWAYNAME");
                    RESPMSG = inResponse.getString("RESPMSG");

                    callVerifyPayTmChecksum(MID, ORDERID);
                }

                @Override
                public void networkNotAvailable() {
                    GlobalClass.showCustomToast(mActivity, ToastFile.NETWORK_NOT_AVAILABLE, 0);
                }

                @Override
                public void onErrorProceed(String s) {
                    System.out.println("PayTM Res >> " + s);
                }

                @Override
                public void clientAuthenticationFailed(String s) {
                    GlobalClass.showCustomToast(mActivity, "clientAuthenticationFailed : " + s, 0);
                }

                @Override
                public void someUIErrorOccurred(String s) {
                    GlobalClass.showCustomToast(mActivity, s, 0);
                }

                @Override
                public void onErrorLoadingWebPage(int i, String s, String s1) {
                    System.out.println("PayTM Res >> " + s + " >> " + s1 + " >> " + i);
                    GlobalClass.showCustomToast(mActivity, "onErrorLoadingWebPage : " + s, 0);
                }

                @Override
                public void onBackPressedCancelTransaction() {
                    System.out.println("PayTM Res >> onBackPressedCancelTransaction");
                    Global.showCustomToast(mActivity, ToastFile.TRANSACTION_CANCELLED);
                }

                @Override
                public void onTransactionCancel(String s, Bundle bundle) {
                    System.out.println("PayTM Res >> " + s);
                    Global.showCustomToast(mActivity, ToastFile.PAYMENT_TRANSACTION_FAILED);
                }
            });

            transactionManager.setShowPaymentUrl(Constants.CALLBACK_URLPAYTM + "/theia/api/v1/showPaymentPage");
            transactionManager.setAppInvokeEnabled(false);
            transactionManager.startTransaction(this, PaytmrequestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVerifyPayTmChecksum(String MID, String ORDERID) {
        try {
            PaytmVerifyChecksumRequestModel requestModel = new PaytmVerifyChecksumRequestModel();
            requestModel.setApi_key(APIKEYFORPAYMENTGATEWAY_PAYU);
            requestModel.setMID(MID);
            requestModel.setORDERID(ORDERID);
            if (CLIENT_TYP.equalsIgnoreCase(Constants.NHF)) {
                requestModel.setCompany("NHL");
            } else {
                requestModel.setCompany("TTL");
            }

            if (cd.isConnectingToInternet()) {
                VerifyPayTmChecksumController verifyPayTmChecksumController = new VerifyPayTmChecksumController(Payment_Activity.this);
                verifyPayTmChecksumController.verifyChecksum(requestModel);
            } else {
                GlobalClass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVerifyPayTmChecksum(PaytmVerifyChecksumResponseModel responseModel, String orderid) {
        try {
            JSONObject postdata = new JSONObject();
            postdata.put(DICTIONARY, new Gson().toJson(responseModel));
            postdata.put(GATEWAY, Constants.PAYTM_RESPONSE);
            postdata.put(ORDERID, orderid);
            postdata.put(MOBILE, user);
            postdata.put(CLIENT_TYPE, "TSP");
            if (!GlobalClass.isNull(responseModel.getSTATUS()) && responseModel.getSTATUS().equalsIgnoreCase("TXN_SUCCESS") && !GlobalClass.isNull(responseModel.getRESPCODE()) && responseModel.getRESPCODE().equalsIgnoreCase("01")) {
                postdata.put(CLIENT_STATUS, SUCCESS);
                postdata.put(PAYMENTSTATUS, SUCCESS);
                callResponseLogAPI(postdata, SUCCESS);
            } else {
                postdata.put(CLIENT_STATUS, FAILURE);
                postdata.put(PAYMENTSTATUS, FAILURE);
                callResponseLogAPI(postdata, FAILURE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callResponseLogAPI(JSONObject postdata, String status) {
        if (cd.isConnectingToInternet()) {
            new AsyncTaskupdatepaymentstatus(postdata, status).execute();
        } else {
            GlobalClass.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet), 0);
        }
    }
}