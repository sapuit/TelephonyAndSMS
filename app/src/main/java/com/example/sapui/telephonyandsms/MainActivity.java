package com.example.sapui.telephonyandsms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity{

    EditText etPhoneNumber;
    String sdt;
    Button btnCall;
    String srvcName;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srvcName = Context.TELEPHONY_SERVICE;
        telephonyManager =
                (TelephonyManager) getSystemService(srvcName);

        // PHONE
        initiatingViews();
//        getTelephoneProperties();
//        readingSIMDetail();
//        readDataConnectAndTransfer();
//        listenPhoneState();
//        trackingCellLocationChanges();
        // show detail call log
//        txtDetailCall.setText(getCallDetails());
//        getCallDetails();

    }

    private void getCallDetails() {
        try {
            StringBuffer sb = new StringBuffer();
            Cursor managedCursor = managedQuery(
                    CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number   = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type     = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date     = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            Log.i("Call Details :", "------------------------");
            while (managedCursor.moveToNext()) {
                String phNumber     = managedCursor.getString(number);
                String callType     = managedCursor.getString(type);
                String callDate     = managedCursor.getString(date);
                Date callDayTime    = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + callDayTime
                        + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
                Log.i("Call Details :", "Phone Number : " + phNumber);
                Log.i("Call Details :", "Call Type : " + dir);
                Log.i("Call Details :", "Call Date : " + callDayTime);
                Log.i("Call Details :", "Call duration in sec  : " + callDuration);
                Log.i("Call Details :", "------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void trackingCellLocationChanges() {
        PhoneStateListener cellLocationListener = new PhoneStateListener() {
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {
                    GsmCellLocation gsmLocation = (GsmCellLocation) location;
                    Toast.makeText(getApplicationContext(),
                            String.valueOf(gsmLocation.getCid()),
                            Toast.LENGTH_LONG).show();
                } else if (location instanceof CdmaCellLocation) {
                    CdmaCellLocation cdmaLocation = (CdmaCellLocation) location;
                    StringBuilder sb = new StringBuilder();
                    sb.append(cdmaLocation.getBaseStationId());
                    sb.append("\n@");
                    sb.append(cdmaLocation.getBaseStationLatitude());
                    sb.append(cdmaLocation.getBaseStationLongitude());
                    Toast.makeText(getApplicationContext(),
                            sb.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        telephonyManager.listen(cellLocationListener,
                PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    private void listenPhoneState() {
        telephonyManager.listen(new TeleListener(),
                PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                        PhoneStateListener.LISTEN_CALL_STATE |
                        PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                        PhoneStateListener.LISTEN_SERVICE_STATE |
                        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                String callStateStr = "Unknown";
                switch (state) {
//                    case TelephonyManager.CALL_STATE_IDLE :
//                        callStateStr = "idle"; break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        callStateStr = "offhook; ";
                        break;

                    case TelephonyManager.CALL_STATE_RINGING:
                        callStateStr = "ringing. Incoming number is: "
                                + incomingNumber;
                        break;

                    default:
                        break;
                }
                Toast.makeText(MainActivity.this,
                        callStateStr, Toast.LENGTH_LONG).show();
            }
        };
        telephonyManager.listen(callStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void readDataConnectAndTransfer() {

        PhoneStateListener dataStateListener = new PhoneStateListener() {
            public void onDataActivity(int direction) {
                String dataActivityStr = "None";
                switch (direction) {
                    case TelephonyManager.DATA_ACTIVITY_IN:
                        dataActivityStr = "Downloading";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_OUT:
                        dataActivityStr = "Uploading";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_INOUT:
                        dataActivityStr = "Uploading/Downloading";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_NONE:
                        dataActivityStr = "No Activity";
                        break;
                }
                Toast.makeText(MainActivity.this,
                        "Data Activity is " + dataActivityStr,
                        Toast.LENGTH_LONG).show();
            }

            public void onDataConnectionStateChanged(int state) {
                String dataStateStr = "Unknown";
                switch (state) {
                    case TelephonyManager.DATA_CONNECTED:
                        dataStateStr = "Connected";
                        break;
                    case TelephonyManager.DATA_CONNECTING:
                        dataStateStr = "Connecting";
                        break;
                    case TelephonyManager.DATA_DISCONNECTED:
                        dataStateStr = "Disconnected";
                        break;
                    case TelephonyManager.DATA_SUSPENDED:
                        dataStateStr = "Suspended";
                        break;
                }
                Toast.makeText(MainActivity.this,
                        "Data Connectivity is " + dataStateStr,
                        Toast.LENGTH_LONG).show();
            }
        };
        telephonyManager.listen(dataStateListener,
                PhoneStateListener.LISTEN_DATA_ACTIVITY |
                        PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

    private void readingSIMDetail() {
        int simState = telephonyManager.getSimState();
        switch (simState) {
            case (TelephonyManager.SIM_STATE_ABSENT):
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                break;
            case (TelephonyManager.SIM_STATE_READY): {
                // Get the SIM country ISO code
                String simCountry = telephonyManager.getSimCountryIso();
                Log.i("SIM country", simCountry);
                // Get the operator code of the active SIM (MCC + MNC)
                String simOperatorCode = telephonyManager.getSimOperator();
                Log.i("operator code", simOperatorCode);
                // Get the name of the SIM operator
                String simOperatorName = telephonyManager.getSimOperatorName();
                Log.i("name of the SIM", simOperatorName);
                // -- Requires READ_PHONE_STATE uses-permission --
                // Get the SIM’s serial number
                String simSerial = telephonyManager.getSimSerialNumber();
                Log.i("SIM’s serial", simSerial);
                break;
            }
            default:
                break;
        }
    }

    private void getTelephoneProperties() {

        String phoneTypeStr = "unknown";
        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                phoneTypeStr = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                phoneTypeStr = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                phoneTypeStr = "SIP";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                phoneTypeStr = "None";
                break;
            default:
                break;
        }

        Log.i("Phone type", phoneTypeStr);
        // -- These require READ_PHONE_STATE uses-permission --
        // Read the IMEI for GSM or MEID for CDMA
        String deviceId = telephonyManager.getDeviceId();
        Log.i("IMEI", deviceId);
        // Read the software version on the phone (note -- not the SDK version)
        String softwareVersion = telephonyManager.getDeviceSoftwareVersion();
        Log.i("software version", softwareVersion);
        // Get the phone’s number (if available)
        String phoneNumber = telephonyManager.getLine1Number();

        Log.i("Phone number", telephonyManager.getLine1Number());
    }

    // Initiating Views
    private void initiatingViews() {
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        btnCall = (Button) findViewById(R.id.btnCall);
//        txtDetailCall = (TextView) findViewById(R.id.txtDetailCall);
        sdt = etPhoneNumber.getText().toString();
    }

    // Click event
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCall:
                makePhoneCall();
                break;
            case R.id.btnSendSMSIntent:
                sendSMSUsingItents();
                break;
            case R.id.btnSendSMSManager:
                sendSMSUsingManager();
                break;
        }
    }

    private void makePhoneCall() {
        Uri phoneNumber = Uri.parse("tel:" + sdt);
        Intent intent = new Intent(Intent.ACTION_CALL, phoneNumber);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void sendSMSUsingItents() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("sms:" + sdt));
        smsIntent.putExtra("sms_body", "Press send to send me");
        startActivity(smsIntent);
    }

    private void sendSMSUsingManager() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String sendTo = sdt;
            String myMessage = "Android supports programmatic SMS messaging!";
            smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Thay đầu số 0 -> 84 ", Toast.LENGTH_SHORT).show();
        }
    }

}
