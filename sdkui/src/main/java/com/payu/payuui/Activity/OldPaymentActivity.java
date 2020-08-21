package com.payu.payuui.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.PayUWebChromeClient;
import com.payu.custombrowser.PayUWebViewClient;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuUtils;
import com.payu.magicretry.Helpers.Util;
import com.payu.magicretry.MagicRetryFragment;
import com.payu.payuui.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Minie on 2/8/16.
 *
 * This class is deprecated. It is for CB version before 6.0.0
 */

public class OldPaymentActivity  extends AppCompatActivity implements MagicRetryFragment.ActivityCallback{
    Bundle bundle;
    String url;
    boolean cancelTransaction = false;
    PayuConfig payuConfig;
    private BroadcastReceiver mReceiver = null;
    private String UTF = "UTF-8";
    private  boolean viewPortWide = false;
    private WebView mWebView;
    private Boolean smsPermission;
    private int storeOneClickHash;
    private String merchantHash;
    MagicRetryFragment magicRetryFragment;
    String txnId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * when the device runing out of memory we dont want the user to restart the payment. rather we close it and redirect them to previous activity.
         */
        if(savedInstanceState!=null){
            super.onCreate(null);
            finish();//call activity u want to as activity is being destroyed it is restarted
        }else {
            super.onCreate(savedInstanceState);
        }
        setContentView(R.layout.activity_payments);
        mWebView = (WebView) findViewById(R.id.webview);
        //     WebView.setWebContentsDebuggingEnabled(true);
        //region Replace the whole code by the commented code if you are NOT using custombrowser
        // Replace the whole code by the commented code if you are NOT using custombrowser.

        bundle = getIntent().getExtras();
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        storeOneClickHash = bundle.getInt(PayuConstants.STORE_ONE_CLICK_HASH);
        mWebView = (WebView) findViewById(R.id.webview);

        url = payuConfig.getEnvironment() == PayuConstants.PRODUCTION_ENV?  PayuConstants.PRODUCTION_PAYMENT_URL : PayuConstants.TEST_PAYMENT_URL ;

        String [] list =  payuConfig.getData().split("&");

        String merchantKey = null;
        for (String item : list) {
            String[] items = item.split("=");
            if(items.length >= 2) {
                String id = items[0];
                switch (id) {
                    case "txnid":
                        txnId = items[1];
                        break;
                    case "key":
                        merchantKey = items[1];
                        break;
                    case "pg":
                        if (items[1].contentEquals("NB")) {
                            viewPortWide = true;
                        }
                        break;
                }
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void initMagicRetry() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        magicRetryFragment = new MagicRetryFragment();
        Bundle newInformationBundle = new Bundle();
        newInformationBundle.putString(MagicRetryFragment.KEY_TXNID, txnId);
        magicRetryFragment.setArguments(newInformationBundle);

        Map<String, String> urlList = new HashMap<String, String>();
        urlList.put(url, payuConfig.getData());
        magicRetryFragment.setUrlListWithPostData(urlList);

        fragmentManager.beginTransaction().add(R.id.magic_retry_container, magicRetryFragment, "magicRetry").commit();
        // magicRetryFragment = (MagicRetryFragment) fragmentManager.findFragmentBy(R.id.magicretry_fragment);

        toggleFragmentVisibility(Util.HIDE_FRAGMENT);

        magicRetryFragment.isWhiteListingEnabled(true);
    }


    public void toggleFragmentVisibility(int flag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!isFinishing()) {
            if (flag == Util.SHOW_FRAGMENT) {
                // Show fragment
                ft.show(magicRetryFragment).commitAllowingStateLoss();
            } else if (flag == Util.HIDE_FRAGMENT) {
                // Hide fragment
                ft.hide(magicRetryFragment).commitAllowingStateLoss();
                // ft.hide(magicRetryFragment);

            }
        }

    }



    @Override
    public void showMagicRetry() {
        toggleFragmentVisibility(Util.SHOW_FRAGMENT);
    }

    @Override
    public void hideMagicRetry() {
        toggleFragmentVisibility(Util.HIDE_FRAGMENT);
    }
}
