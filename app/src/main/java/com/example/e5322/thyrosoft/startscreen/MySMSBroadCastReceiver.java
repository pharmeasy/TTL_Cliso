package com.example.e5322.thyrosoft.startscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.e5322.thyrosoft.Interface.SmsListener;

/**
 * Created by e5426@thyrocare.com on 12/2/18.
 */

public class MySMSBroadCastReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String abcd, xyz;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {


            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String sender = smsMessage.getDisplayOriginatingAddress();
                b = sender.endsWith("");  //Just to fetch otp sent from WNRCRP
                String messageBody = smsMessage.getMessageBody();
                abcd = messageBody.replaceAll("[^0-9]", "");   // here abcd contains otp


                if (abcd != null) {
                    mListener.messageReceived(abcd);
                }
                // attach value to interface

            }
        } catch (Exception e) {
             e.printStackTrace();
        }
    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}