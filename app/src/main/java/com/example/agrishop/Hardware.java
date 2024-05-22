package com.example.agrishop;
import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Hardware extends AppCompatActivity {

    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 150;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private BluetoothAdapter bluetoothAdapter;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Button enableBluetoothButton = findViewById(R.id.enable_bluetooth_button);
        enableBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBluetooth();
            }
        });

        Button enableCameraButton = findViewById(R.id.enable_camera_button);
        enableCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableCamera();
            }
        });

        Button scanWifiButton = findViewById(R.id.scanButton);
        scanWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });

        View touchView = findViewById(R.id.touch_view);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(event);
            }
        });
        Button disableBluetoothButton = findViewById(R.id.disable_bluetooth_button);
        disableBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableBluetooth();
            }
        });

        Button disableWifiButton = findViewById(R.id.disable_wifi_button);
        disableWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableWifi();
            }
        });



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // Left to Right swipe action
                        if (x2 > x1) {
                            onSwipeRight();
                        }
                        // Right to left swipe action
                        else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // Top to bottom swipe action
                        if (y2 > y1) {
                            onSwipeBottom();
                        }
                        // Bottom to top swipe action
                        else {
                            onSwipeTop();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void onSwipeRight() {
        Toast.makeText(this, "Swipe Right", Toast.LENGTH_SHORT).show();
    }

    private void onSwipeLeft() {
        Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show();
    }

    private void onSwipeTop() {
        Toast.makeText(this, "Swipe Top", Toast.LENGTH_SHORT).show();
    }

    private void onSwipeBottom() {
        Toast.makeText(this, "Swipe Bottom", Toast.LENGTH_SHORT).show();
    }

    private void enableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT);
                return;
            }
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        } else {
            // Bluetooth is already enabled
            Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanWifi() {
        if (checkPermissions()) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSIONS_REQUEST_CODE);
    }

    private void enableCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCameraActivity();
        }
    }
    private void disableBluetooth() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth disabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableWifi() {
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(getApplicationContext(), "Wi-Fi disabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Wi-Fi is already disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanWifi();
            } else {
                Toast.makeText(this, "Permission denied. Cannot scan WiFi networks.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Opening Camera", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }
}