package com.example.agrishop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            if (isAirplaneModeOn) {
                Toast.makeText(context, "Airplane mode turned ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Airplane mode turned OFF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
