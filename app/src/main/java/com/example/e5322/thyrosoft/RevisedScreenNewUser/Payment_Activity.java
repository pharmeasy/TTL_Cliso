package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.google.gson.Gson;
import com.payu.india.Extras.PayUChecksum;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.e5322.thyrosoft.API.Api.APIKEYFORPAYMENTGATEWAY_PAYU;
import static com.example.e5322.thyrosoft.API.Api.BASE_URL_TOCHECK;
import static com.example.e5322.thyrosoft.API.Constants.AMOUNT;
import static com.example.e5322.thyrosoft.API.Constants.CLIENT_STATUS;
import static com.example.e5322.thyrosoft.API.Constants.CLIENT_TYPE;
import static com.example.e5322.thyrosoft.API.Constants.DICTIONARY;
import static com.example.e5322.thyrosoft.API.Constants.DOMAIN;
import static com.example.e5322.thyrosoft.API.Constants.FAILURE;
import static com.example.e5322.thyrosoft.API.Constants.GATEWAY;
import static com.example.e5322.thyrosoft.API.Constants.MOBILE;
import static com.example.e5322.thyrosoft.API.Constants.NAME;
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

public class Payment_Activity extends AppCompatActivity {

    private static Activity context;
    private static Global globalData;
    private static android.support.v7.app.AlertDialog.Builder alertDialogBuilder1;
    public String merchantKey = "", userCredentials;
    TextView txt_minamt, txt_mulamt, txt_tsp_name, title;
    Button btn_payu;
    ImageView back, home;
    EditText edt_enter_amt, edt_closing_bal;
    ConnectionDetector cd;
    String name_tsp, user, passwrd, access, api_key, email_id, email_pref, mobile_pref, address_pref, pincode_pref, usercode;
    android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
    String strProductsName = "", strProductsPrice = "", strBenCount = "1", strReportCharge = "no", strPayType = "", strReportCode = "", strMaleTest = "";
    String strCollectCharg = "0", strVisitChrg = "0", strPayAmount = "0";
    String strOrderNo = "";
    int ben_count = 0;
    private boolean flag_for_same_orderno = false;
    private int tlog_paymentrequest_status_code = 0;
    private int updatepayment_status_code = 0;
    private int buttonclickFlag = 0;
    private boolean isemailvalid = false;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    private String _appointmentDate, _displaydate, _displaytime, _pinCode, _strTID, _straddress, _strlandmark, _access_token, _merchantid, _orderid, _curreny, _amount, _redirect_url, _cancel_url, _rsa_key_url, _responsedata;
    private String amountTopass;
    private Global globalClass;
    private String appointmentDate;
    private String dataToSave;
    private String TAG = Payment_Activity.class.getSimpleName().toString();
    private String randomTid;
    private String closing_balance_pref;
    private RequestQueue PostQueOtp;
    private String RESPONSE;
    private String ordno;
    private boolean flagForOnce = false;
    private String COME_FROM_SCREEN = "";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_);

        title = (TextView) findViewById(R.id.title);
        txt_minamt = (TextView) findViewById(R.id.txt_minamt);
        txt_mulamt = (TextView) findViewById(R.id.txt_mulamt);
        txt_tsp_name = (TextView) findViewById(R.id.txt_tsp_name);
        btn_payu = (Button) findViewById(R.id.btn_payu);
        edt_enter_amt = (EditText) findViewById(R.id.edt_enter_amt);
        edt_closing_bal = (EditText) findViewById(R.id.edt_closing_bal);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        Payu.setInstance(this);
        int environment = PayuConstants.STAGING_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        mPaymentParams = new PaymentParams();

        cd = new ConnectionDetector(Payment_Activity.this);

        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
        name_tsp = getProfileName.getString("name", null);
        usercode = getProfileName.getString("user_code", null);
        email_id = getProfileName.getString("email", null);
        address_pref = getProfileName.getString("address", null);
        pincode_pref = getProfileName.getString("pincode", null);

        closing_balance_pref = getProfileName.getString("closing_balance", null);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        email_pref = prefs.getString("email", null);
        mobile_pref = prefs.getString("mobile_user", null);

        COME_FROM_SCREEN = getIntent().getStringExtra("COMEFROM");

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        globalData = new Global(Payment_Activity.this);
        title.setText("Online Payment");
        txt_minamt.setText("\u2022 " + Html.fromHtml("Minimum amount to be paid 5000"));
        txt_mulamt.setText("\u2022 " + Html.fromHtml("Amount needs to be paid in multiples of 5000"));
        txt_tsp_name.setText(name_tsp + " - " + usercode);
        edt_closing_bal.setText(closing_balance_pref);


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


        btn_payu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountTopass = edt_enter_amt.getText().toString();
                if (amountTopass.equals("")) {
                    Toast.makeText(Payment_Activity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                } else {
                    int getAmt = Integer.parseInt(edt_enter_amt.getText().toString());
                    int modulus = getAmt % 5000;
                    System.out.println("getAmount ::" + modulus);

                    if (getAmt < 5000) {
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
                    } else if (modulus != 0) {
                        new SweetAlertDialog(Payment_Activity.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText(getString(R.string.multiple_amt))
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        if (cd.isConnectingToInternet()) {
                            globalData.showProgressDialog();
                            try {
                                PostQueOtp = Volley.newRequestQueue(Payment_Activity.this);
                                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.GET, Api.GenerateId + api_key + "/TSP/Generatedordnum", new com.android.volley.Response.Listener<JSONObject>() {
                                    public String RES_ID;

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        globalData.hideProgressDialog();
                                        try {
                                            Log.e(TAG, "onResponse: " + response);
                                            String finalJson = response.toString();
                                            JSONObject parentObjectOtp = new JSONObject(finalJson);
                                            RESPONSE = parentObjectOtp.getString("RESPONSE");
                                            RES_ID = parentObjectOtp.getString("RES_ID");
                                            ordno = parentObjectOtp.getString("ordno");

                                            if (RESPONSE.equals("SUCCESS")) {
                                                startPayUTransaction(name_tsp, "", amountTopass, ordno);
                                            } else {

                                            }

                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        globalData.hideProgressDialog();
                                        if (error != null) {
                                        } else {
                                            globalData.hideProgressDialog();
                                            System.out.println(error);
                                        }
                                    }
                                });
                                PostQueOtp.add(jsonObjectRequest1);
                                Log.e(TAG, "SendFeedbackToAPI: url" + jsonObjectRequest1);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }


                }

            }
        });


/*        btn_payu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountTopass = edt_enter_amt.getText().toString();

                Log.e(TAG, "Entered Amount ----->" + amountTopass);

                Log.e(TAG, "CB Amount ----->" + closing_balance_pref);

                if (amountTopass.equals("")) {
                    Toast.makeText(Payment_Activity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(closing_balance_pref) < 5000) {

                    if (Integer.parseInt(amountTopass) >= 5000) {
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

                } else if (5000 <= Integer.parseInt(closing_balance_pref)) {

                    if (Integer.parseInt(amountTopass) >= Integer.parseInt(closing_balance_pref)) {
                        gotopayGatway();
                    } else {
                        new SweetAlertDialog(Payment_Activity.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Enter Minimum Amount of Rs " + closing_balance_pref + " to Proceed")
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

        });*/
    }

    private void gotopayGatway() {
        if (cd.isConnectingToInternet()) {
            globalData.showProgressDialog();
            try {
                PostQueOtp = Volley.newRequestQueue(Payment_Activity.this);
                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.GET, Api.GenerateId + api_key + "/TSP/Generatedordnum", new com.android.volley.Response.Listener<JSONObject>() {
                    public String RES_ID;

                    @Override
                    public void onResponse(JSONObject response) {
                        globalData.hideProgressDialog();
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            String finalJson = response.toString();
                            JSONObject parentObjectOtp = new JSONObject(finalJson);
                            RESPONSE = parentObjectOtp.getString("RESPONSE");
                            RES_ID = parentObjectOtp.getString("RES_ID");
                            ordno = parentObjectOtp.getString("ordno");

                            if (RESPONSE.equals("SUCCESS")) {
                                startPayUTransaction(name_tsp, "", amountTopass, ordno);
                            } else {

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        globalData.hideProgressDialog();
                        if (error != null) {
                        } else {
                            globalData.hideProgressDialog();
                            System.out.println(error);
                        }
                    }
                });
                PostQueOtp.add(jsonObjectRequest1);
                Log.e(TAG, "SendFeedbackToAPI: url" + jsonObjectRequest1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
                }
            }


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
            globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));

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

    class AsyncTaskstartpayUmoneyTransaction extends AsyncTask<Void, Void, JSONObject> {

        String orderamount, orderno;
        PaymentParams mPayment_Params;
        JSONObject jobj, jobj1, postdata;


        public AsyncTaskstartpayUmoneyTransaction(PaymentParams mPaymentParams) {
            this.mPayment_Params = mPaymentParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            globalData.setLoadingGIF(Payment_Activity.this);
            globalData.showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            String strUrl = BASE_URL_TOCHECK + "PaymentGateway.svc/PayUChecksum";
            Log.e(TAG, "doInBackground: " + strUrl);
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
                        if (result.getString("encCheckSum") != null && !result.getString("encCheckSum").isEmpty()) {
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

                                new AsyncTask_Log_Payment_Request(postdata).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else
                                globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));
                            launchSdkUI(payuHashes);// Start PayU transaction request.

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

            globalData.hideProgressDialog();
        }
    }

    class AsyncTask_Log_Payment_Request extends AsyncTask<Void, Void, JSONObject> {

        JSONObject postdata = new JSONObject();

        public AsyncTask_Log_Payment_Request(JSONObject result) {
            this.postdata = result;

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
                        if (value.equalsIgnoreCase("SUCCESS")) {

                            globalData.showCustomToast(Payment_Activity.this, "Redirecting to Payment gateway..");
                            System.out.println("Successful");

                        } else {
                            System.out.println("Unsuccessful");
                        }
                    } else {
                        System.out.println("failed result is null ");
                    }
                } else {
                    System.out.println("failed status code ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class AsyncTaskVerifypayUmoneyTransaction extends AsyncTask<Void, Void, JSONObject> {

        String orderamount, orderno;
        JSONObject mPayU_response, postdata;

        public AsyncTaskVerifypayUmoneyTransaction(JSONObject mPayUresponse) {

            this.mPayU_response = mPayUresponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            globalData.setLoadingGIF(Payment_Activity.this);
            globalData.showProgressDialog();

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

            globalData.hideProgressDialog();
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
                            if (cd.isConnectingToInternet()) {
                                flag_for_same_orderno = false;// this flag is to use same order no if transaction fails
                                new AsyncTaskupdatepaymentstatus(postdata, SUCCESS).execute();
                            } else {
                                globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));
                            }
                        } else {
                            postdata.put(CLIENT_STATUS, FAILURE);
                            postdata.put(PAYMENTSTATUS, FAILURE);
                            if (cd.isConnectingToInternet()) {
                                flag_for_same_orderno = true; // this flag is to use same order no transaction fails
                                new AsyncTaskupdatepaymentstatus(postdata, FAILURE).execute();
                            } else {
                                globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));
                            }
                        }


                    } else {
                        postdata.put(CLIENT_STATUS, FAILURE);
                        postdata.put(PAYMENTSTATUS, FAILURE);
                        if (cd.isConnectingToInternet()) {
                            flag_for_same_orderno = true; // this flag is to use same order no transaction fails
                            new AsyncTaskupdatepaymentstatus(postdata, FAILURE).execute();
                        } else {
                            globalData.showCustomToast(Payment_Activity.this, getResources().getString(R.string.plz_chk_internet));
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            globalData.hideProgressDialog();
        }
    }

    class AsyncTaskupdatepaymentstatus extends AsyncTask<Void, Void, JSONObject> {
        JSONObject postdata = new JSONObject();
        String paymentstatus, currency;

        public AsyncTaskupdatepaymentstatus(JSONObject result, String paymentstatus) {
            this.postdata = result;
            this.paymentstatus = paymentstatus;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            globalData.setLoadingGIF(Payment_Activity.this);
            globalData.showProgressDialog();

        }

        @Override
        protected JSONObject doInBackground(Void... params) {


            String strUrl = Api.GenerateTid + "PaymentGateway.svc/PaymentLog_franchise";
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
                globalData.hideProgressDialog();
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
                            if (!value.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(Payment_Activity.this, "Something went wrong - Response log failed", Toast.LENGTH_SHORT).show();
                            }

                            if (paymentstatus.equalsIgnoreCase(SUCCESS)) {
                                Toast.makeText(Payment_Activity.this, "Transaction successful...", Toast.LENGTH_SHORT).show();
                                edt_enter_amt.setText("");
                                System.out.println("Success " + result.toString());
                            } else {
                                Toast.makeText(context, "Transaction failed.", Toast.LENGTH_SHORT).show();
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


}
