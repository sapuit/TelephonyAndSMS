package com.example.sapui.telephonyandsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by sapui on 11/12/2015.
 */
public class MySMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    for (SmsMessage message : messages) {
                        String msg = message.getMessageBody();
                        long when = message.getTimestampMillis();
                        String from = message.getOriginatingAddress();
                        Toast.makeText(context, from + ":" + msg,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
//                        Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
