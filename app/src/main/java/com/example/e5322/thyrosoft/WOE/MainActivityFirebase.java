package com.example.e5322.thyrosoft.WOE;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;

public class MainActivityFirebase extends AppCompatActivity {

    private TextView tvNotificationDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_firebase);
        initControls();
        setNotificationData(getIntent().getExtras());
    }
    private void initControls() {
        tvNotificationDetails = (TextView) findViewById(R.id.tvNotificationDetails);
    }
    private void setNotificationData(Bundle extras) {
        if (extras == null)
            return;
        StringBuilder text = new StringBuilder("");
        text.append("Message Details:");
        text.append("\n");
        text.append("\n");
        if (extras.containsKey("title")) {
            text.append("Title: ");
            text.append(extras.get("title"));
        }
        text.append("\n");
        if (extras.containsKey("message")) {
            text.append("Message: ");
            text.append(extras.get("message"));
        }
        tvNotificationDetails.setText(text);
    }

}
