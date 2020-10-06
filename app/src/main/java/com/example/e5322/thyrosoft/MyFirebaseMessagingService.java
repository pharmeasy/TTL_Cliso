package com.example.e5322.thyrosoft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.example.e5322.thyrosoft.Controller.Log;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private int Screen_category = 0;

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.e(TAG, "Token  0----->" + refreshedToken);
        Constants.PUSHNOT_FLAG=true;

        storeRegIdInPref(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent("demokey");
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.F_TOKEN, token);
        editor.putString(Constants.servertoken, "no");
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        GlobalClass.printLog("Meaasge received", TAG, "From: ", remoteMessage.getFrom());

        Log.e(TAG, "MEASSAGE --->" + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            GlobalClass.printLog("Error", TAG, "Notification Body: ", remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            GlobalClass.printLog("Error", TAG, "Data Payload: ", remoteMessage.getData().toString());
            try {
                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.putAll(remoteMessage.getData());
                handleDataMessage(notificationData);
            } catch (Exception e) {
                GlobalClass.printLog("Error", TAG, "Exception: ", e.getMessage());
            }
        }
    }


    private void handleDataMessage(HashMap<String, String> json) {
        GlobalClass.printLog("Error", TAG, "push json: ", json.toString());
        try {
            String title = json.get("title");
            String message = json.get("message");
            String onGoing = json.get("onGoing");
            String autoCancel = json.get("autoCancel");
            String bigText = json.get("bigText");
            String imageUrl = json.get("image");
//            String timestamp = json.get("timestamp");
            String timestamp = String.valueOf(System.currentTimeMillis());
//            String NotificationID = json.get("notifyID");
            String NotificationID = json.get("Screen_Category");
            String Product_name = json.get("Product_name");
            String Order_ID = json.get("Order_ID");
            String mobile = json.get("userid");
            String App_ID = json.get("App_ID");
            Screen_category = Integer.parseInt(json.get("Screen_Category"));

            Log.e(TAG, "Screen_category ----->" + Screen_category);

            if (!GlobalClass.isNull(App_ID) && App_ID.equalsIgnoreCase(Constants.APPID)) {
                Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.putExtra("IsFromNotification", true);
                resultIntent.putExtra("Screen_category", Screen_category);


             /*   if (Screen_category == 0) {
                    resultIntent.putExtra("Screen_category", Screen_category);
                } else if (Screen_category > 0 && Screen_category <= 100) {
                    if (!GlobalClass.isNull(Order_ID)) {
                        resultIntent.putExtra("TechsoID", Screen_category);
                        resultIntent.putExtra("Screen_category", Constants.SCR_1);
                        resultIntent.putExtra("Order_ID", Order_ID);
                    } else {
                        resultIntent.putExtra("Screen_category", 0);
                    }
                } else if (Screen_category > 100) {
                    int position = Screen_category - 100;
                    resultIntent.putExtra("Screen_category", position);
                    if (position == Constants.SCR_1) {
                        resultIntent.putExtra("Order_ID", Order_ID);
                    }
                } else {
                    resultIntent.putExtra("Screen_category", 0);
                }*/


                SharedPreferences prefs_user = getApplicationContext().getSharedPreferences("login_detail", 0);
                String loginMobileNumber = prefs_user.getString("mobile", "");

                if (!GlobalClass.isNull(mobile)) {
                    if (mobile.equalsIgnoreCase(loginMobileNumber)) {
                        if (GlobalClass.isNull(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, NotificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
                        } else {
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, NotificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
                        }
                    }
                } else {
                    if (GlobalClass.isNull(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, NotificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
                    } else {
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, NotificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
                    }
                }
            }
        } catch (Exception e) {
            GlobalClass.printLog("E r r o r ----->", TAG, "E x c e p t i o n ------>", e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, String notificationID, String onGoing, String autoCancel, String bigText, int Screen_category, String Product_name) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, notificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl, String notificationID, String onGoing, String autoCancel, String bigText, int Screen_category, String Product_name) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, notificationID, onGoing, autoCancel, bigText, Screen_category, Product_name);
    }
}
