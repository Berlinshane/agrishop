package com.example.agrishop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Example usage of Message class
        Message messageObject = new Message("John", "Hello, how are you?");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView senderTextView = findViewById(R.id.sender_text_view);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView messageTextView = findViewById(R.id.message_text_view);
        senderTextView.setText(messageObject.getSender());
        messageTextView.setText(messageObject.getMessage());
    }
}
