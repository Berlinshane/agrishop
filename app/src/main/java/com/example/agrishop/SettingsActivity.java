package com.example.agrishop;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Button btnToggleDark, btnToggleWifi;
    private EditText edtUserName, edtPassword;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnToggleDark = findViewById(R.id.btnToggleDark);
        btnToggleWifi = findViewById(R.id.btnToggleWifi);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);

        // Initialize WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        SharedPreferences sharedPreferences =
                getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        final String savedUserName = sharedPreferences.getString("userName", "");
        final String savedPassword = sharedPreferences.getString("password", "");

        setDarkMode(isDarkModeOn);
        edtUserName.setText(savedUserName);
        edtPassword.setText(savedPassword);

        btnToggleDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newMode = !isDarkModeOn;
                setDarkMode(newMode);
                editor.putBoolean("isDarkModeOn", newMode);
                editor.apply();
            }
        });

        btnToggleWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWifi();
            }
        });
    }

    private void setDarkMode(boolean isDarkModeOn) {
        if (isDarkModeOn) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnToggleDark.setText("Disable Dark Mode");
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnToggleDark.setText("Enable Dark Mode");
        }
    }

    private void toggleWifi() {
        // Toggle Wi-Fi state
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences =
                getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", edtUserName.getText().toString());
        editor.putString("password", edtPassword.getText().toString());
        editor.apply();
    }
}
