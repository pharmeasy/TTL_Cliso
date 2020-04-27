package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.API.Product;
import com.example.e5322.thyrosoft.API.SqliteDatabase;
import com.example.e5322.thyrosoft.Adapter.ProductAdapter;
import com.example.e5322.thyrosoft.Fragment.NotificationFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import java.util.List;

public class Notification_activity extends AppCompatActivity {
    TextView notifyHeader, notifycontent, noNotification;
    private SqliteDatabase mDatabase;
    private Global globalClass;
    ImageView back, home;

    RecyclerView item_list;
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    LinearLayoutManager linearLayoutManager;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);

        noNotification = (TextView) findViewById(R.id.noNotification);
        item_list = (RecyclerView) findViewById(R.id.item_list);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Notification_activity.this);
            }
        });

        linearLayoutManager = new LinearLayoutManager(Notification_activity.this);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        item_list.setLayoutManager(linearLayoutManager);
        item_list.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(Notification_activity.this);
        List<Product> allProducts = mDatabase.listProducts();
        if (allProducts.size() > 0) {
            item_list.setVisibility(View.VISIBLE);
            ProductAdapter mAdapter = new ProductAdapter(Notification_activity.this, allProducts);
            item_list.setAdapter(mAdapter);
        } else {
            item_list.setVisibility(View.GONE);
            noNotification.setVisibility(View.VISIBLE);
//            Toast.makeText(Notification_activity.this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }
}
