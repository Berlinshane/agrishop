package com.example.agrishop;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSSenderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("SEND_SMS".equals(intent.getAction())) {
            String phoneNumber = intent.getStringExtra("phone_number");
            String message = intent.getStringExtra("sms_body");

            try {
                // Send SMS
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                // Display toast message
                Toast.makeText(context, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Log any exceptions
                Log.e("SMSSenderReceiver", "Failed to send SMS", e);

                // Display toast message for failure
                Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}