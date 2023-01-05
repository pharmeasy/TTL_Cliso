package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView webview;
    ImageView back, home;
    String ThyroUrl = "https://www.thyrocare.com/PrivacyPolicy";
    String PEurl = "https://pharmeasy.in/privacy-policy";
    Activity activity;
    ProgressDialog progressDialog;
    GlobalClass globalClass;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_privacy_policy);
        activity = this;
        cd = new ConnectionDetector(activity);
        globalClass = new GlobalClass(this);
        initui();
        initListeners();
        displayWebView();

    }

    private void displayWebView() {
        if (cd.isConnectingToInternet()) {
            webview.getSettings().setJavaScriptEnabled(true);
            if (Global.getLoginType(activity) == Constants.PEflag) {
                webview.loadUrl(PEurl);
            } else {
                webview.loadUrl(ThyroUrl);
            }
            progressDialog = GlobalClass.ShowprogressDialog(activity);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progressDialog.show();
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                  GlobalClass.hideProgress(activity,progressDialog);
                }
            });

        } else {
            Toast.makeText(activity, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
        }
    }

    private void initui() {
        webview = findViewById(R.id.webview);
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.goToHome(PrivacyPolicyActivity.this);
            }
        });
    }
}