package com.example.agrishop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SMSSenderBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("com.example.agrishop.SEND_SMS")) {
            ArrayList<String> phoneNumbers = intent.getStringArrayListExtra("phoneNumbers");
            String message = intent.getStringExtra("message");

            if (phoneNumbers != null && !phoneNumbers.isEmpty() && message != null && !message.isEmpty()) {
                for (String phoneNumber : phoneNumbers) {
                    sendSingleMessage(context, phoneNumber, message);
                }
                // Display a log message for debugging
                Log.d("SMSSenderBroadcastReceiver", "Scheduled message sent");
                // Display a toast message
                Toast.makeText(context, "Scheduled message sent", Toast.LENGTH_SHORT).show();
            } else {
                // Log an error if necessary data is missing
                Log.e("SMSSenderBroadcastReceiver", "Missing phone numbers or message");
            }
        }
    }

    private void sendSingleMessage(Context context, String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
