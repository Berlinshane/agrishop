package com.example.agrishop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SmsSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() == AppCompatActivity.RESULT_OK) {
            Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "SMS sending failed", Toast.LENGTH_SHORT).show();
        }
    }
}
