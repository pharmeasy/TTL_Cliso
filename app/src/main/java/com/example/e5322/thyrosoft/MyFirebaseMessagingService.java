package com.example.e5322.thyrosoft;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Product;
import com.example.e5322.thyrosoft.API.SqliteDatabase;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMSGService";
    private SqliteDatabase mDatabase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("Msg", "Message received ["+remoteMessage+"]");
        // Create Notification
        mDatabase = new SqliteDatabase(this);

        Intent intent = new Intent(this, ManagingTabsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);
        GlobalClass.setFlag = false;


        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ThyrosoftLite Notification")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Toast.makeText(this, "From"+remoteMessage.getFrom(), Toast.LENGTH_SHORT).show();

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
//            Toast.makeText(this, "Message data payload"+remoteMessage.getData(), Toast.LENGTH_SHORT).show();
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            Toast.makeText(this, "Message Notification Body"+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
//            sendNotification(remoteMessage.getNotification().getBody());
        }
// pooja firebase

         String name = "ThyrosoftLite App Notification";
         String body = remoteMessage.getNotification().getBody();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(body)){
//            Toast.makeText(MyFirebaseMessagingService.this, ToastFile.something_went_wrong, Toast.LENGTH_LONG).show();
        }
        else{
            Product newProduct = new Product(name, body);
            mDatabase.addProduct(newProduct);
        }



    }




}
