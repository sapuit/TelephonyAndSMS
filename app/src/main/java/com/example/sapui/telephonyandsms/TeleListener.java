package com.example.sapui.telephonyandsms;

import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class TeleListener extends PhoneStateListener {

    public void onCallStateChanged(int state, String incomingNumber,Context context) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // CALL_STATE_IDLE;
                Toast.makeText(context.getApplicationContext(), "CALL_STATE_IDLE",
                        Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // CALL_STATE_OFFHOOK;
                Toast.makeText(context.getApplicationContext(), "CALL_STATE_OFFHOOK",
                        Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // CALL_STATE_RINGING
                Toast.makeText(context.getApplicationContext(), incomingNumber,
                        Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), "CALL_STATE_RINGING",
                        Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

}
