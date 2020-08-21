package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class AbstractActivity extends AppCompatActivity {

    protected AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appPreferenceManager = new AppPreferenceManager(this);
        appPreferenceManager.setIsAppInBackground(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {


        } else {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {

                if (GlobalClass.checkArray(info)){
                    for (int i = 0; i < info.length; i++) {

                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                            return true;
                        }
                    }
                }

            }
        }

        return false;
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        appPreferenceManager.setIsAppInBackground(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appPreferenceManager.isAppInBackground()) {
            appPreferenceManager.setIsAppInBackground(false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {

        appPreferenceManager.setIsAppInBackground(false);
        super.onPause();

    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}