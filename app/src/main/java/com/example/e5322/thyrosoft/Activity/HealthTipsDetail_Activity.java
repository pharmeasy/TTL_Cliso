package com.example.e5322.thyrosoft.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.MyWebViewClient;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthTipsDetail_Activity extends AppCompatActivity implements View.OnClickListener {


    private WebView web_view_flash_Sale;
    String url;
    TextView tvname;

    public HealthTipsDetail_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_health_tips_detail);

        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
        }

        initView();
    }


    private void initView() {

        tvname = findViewById(R.id.txt_name);
        tvname.setText("Health Articles");
        tvname.setAllCaps(true);
        tvname.setTextColor(getResources().getColor(R.color.maroon));
        tvname.setTextSize(20f);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);

        web_view_flash_Sale = findViewById(R.id.web_view_flash_Sale);
        // globalClass.showProgressDialog();;
        web_view_flash_Sale.loadUrl(url);
        web_view_flash_Sale.getSettings().setJavaScriptEnabled(true);
        web_view_flash_Sale.getSettings().setLoadsImagesAutomatically(true);
        web_view_flash_Sale.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view_flash_Sale.setVerticalScrollBarEnabled(true);
        web_view_flash_Sale.setHorizontalScrollBarEnabled(true);

        /**We create MyWebViewClient class for webview click redirect to other page*/
        web_view_flash_Sale.setWebViewClient(new MyWebViewClient(HealthTipsDetail_Activity.this));

        web_view_flash_Sale.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;

                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.home:
                startActivity(new Intent(HealthTipsDetail_Activity.this, ManagingTabsActivity.class));
                break;
        }
    }
}
