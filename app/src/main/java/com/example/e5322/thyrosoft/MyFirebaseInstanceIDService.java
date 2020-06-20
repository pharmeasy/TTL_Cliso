package com.example.e5322.thyrosoft;

import com.example.e5322.thyrosoft.Controller.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        GlobalClass.printLog("Token ---->", TAG, "<< onTokenRefresh >> ", refreshedToken);

        Log.e(TAG, "TOKEN ----->" + refreshedToken);

        // Saving reg id to shared preferences



    }


}
