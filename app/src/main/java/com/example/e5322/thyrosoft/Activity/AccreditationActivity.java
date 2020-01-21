package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.e5322.thyrosoft.R;

/**
 * Created by kalpesh Borane
 */

public class AccreditationActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_accredation);
        initview();
    }

    private void initview() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.home:
                startActivity(new Intent(AccreditationActivity.this, ManagingTabsActivity.class));
                break;
        }
    }
}
