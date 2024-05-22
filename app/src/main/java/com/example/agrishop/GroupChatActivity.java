package com.example.agrishop;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    private static final int REQUEST_SMS_PERMISSION = 101;
    private EditText messageEditText;
    private Button sendButton;
    private Map<String, List<String>> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Initialize groups
        groups = new HashMap<>();
        List<String> fertilizerGroupPhoneNumbers = new ArrayList<>();
        fertilizerGroupPhoneNumbers.add("7639306401");
        fertilizerGroupPhoneNumbers.add("8903294682");
        groups.put("Fertilizer Recommendation", fertilizerGroupPhoneNumbers);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> recipients = groups.get("Fertilizer Recommendation");
                String message = messageEditText.getText().toString().trim();

                // Get the desired time to send the message (for example, 10 seconds from now)
                long delayInMillis = System.currentTimeMillis() + 10000; // 10 seconds delay

                // Schedule the SMS
                scheduleSMS(recipients, message, delayInMillis);

                // Inform the user
                Toast.makeText(GroupChatActivity.this, "Message scheduled to be sent", Toast.LENGTH_SHORT).show();
            }
        });

        // Request SMS permission if not granted
        if (!isSmsPermissionGranted()) {
            requestSmsPermission();
        }
    }

    private boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
    }

    private void sendMessageToGroup(List<String> phoneNumbers, String message) {
        if (!message.isEmpty()) {
            if (isSmsPermissionGranted()) {
                for (String phoneNumber : phoneNumbers) {
                    sendSingleMessage(phoneNumber, message);
                }
                Toast.makeText(this, "Message sent to group", Toast.LENGTH_SHORT).show();
            } else {
                requestSmsPermission();
            }
        } else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSingleMessage(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void scheduleSMS(List<String> phoneNumbers, String message, long timeInMillis) {
        Intent intent = new Intent(this, SMSSenderBroadcastReceiver.class);
        intent.setAction("com.example.agrishop.SEND_SMS");
        intent.putStringArrayListExtra("phoneNumbers", (ArrayList<String>) phoneNumbers);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister any receivers if needed
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
