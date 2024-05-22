package com.example.agrishop;
import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PesticidesActivity extends AppCompatActivity {

    private ArrayList<String> pesticideList;
    private ArrayAdapter<String> adapter;

    private WifiManager wifiManager;
    private ArrayList<String> wifiList;
    private ArrayAdapter<String> wifiAdapter;

    private static final int REQUEST_CHANGE_WIFI_STATE = 123;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticides);

        // Initialize the pesticide list
        pesticideList = new ArrayList<>();
        pesticideList.add("Acetamiprid");
        pesticideList.add("Imidacloprid");
        pesticideList.add("Neem Oil");
        pesticideList.add("Bacillus thuringiensis");
        pesticideList.add("Pyrethrum");

        // Initialize the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pesticideList);

        // Set up the ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Initialize WiFi
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiList = new ArrayList<>();
        wifiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);

        // Check and request ACCESS_FINE_LOCATION permission if needed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Handle search action
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_sort_by_name:
                // Sort the pesticide list by name
                sortPesticidesByName();
                return true;
            case R.id.action_settings:
                // Handle settings action
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_cart:
                // Show the AlertDialog with the list of pesticides
                showPesticideListDialog();
                return true;
            case R.id.action_toggle_bluetooth:
                // Toggle Bluetooth
                toggleBluetooth();
                return true;
            case R.id.action_toggle_wifi:
                // Toggle WiFi
                toggleWiFi();
                return true;
            case R.id.action_view_wifi:
                // Show available WiFi networks
                showAvailableWifiNetworks();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortPesticidesByName() {
        // Sort the pesticide list alphabetically
        Collections.sort(pesticideList);
        // Notify the adapter of the data change
        adapter.notifyDataSetChanged();
        // Display a toast message
        Toast.makeText(this, "Pesticides sorted by name", Toast.LENGTH_SHORT).show();
    }

    private void showPesticideListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Pesticides")
                .setMultiChoiceItems(pesticideList.toArray(new CharSequence[pesticideList.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // Handle checkbox clicks if needed
                            }
                        })
                .setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the selected items and add them to the cart
                        StringBuilder selectedItems = new StringBuilder();
                        for (int i = 0; i < pesticideList.size(); i++) {
                            if (((AlertDialog) dialog).getListView().isItemChecked(i)) {
                                selectedItems.append(pesticideList.get(i)).append(", ");
                            }
                        }
                        if (selectedItems.length() > 0) {
                            selectedItems.setLength(selectedItems.length() - 2); // Remove the last comma and space
                            Toast.makeText(PesticidesActivity.this, "Added to cart: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PesticidesActivity.this, "No items selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void toggleBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bluetoothAdapter.isEnabled()) {
            // Bluetooth is enabled, disable it
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothAdapter.disable();
            Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_SHORT).show();
        } else {
            // Bluetooth is disabled, enable it
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            Toast.makeText(this, "Bluetooth turned on", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleWiFi() {
        // Check if the app has permission to change Wi-Fi state
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CHANGE_WIFI_STATE);
        } else {
            // If the app has the permission, toggle Wi-Fi
            performWiFiToggle();
        }
    }

    private void performWiFiToggle() {
        // Check if Wi-Fi is enabled or disabled
        if (wifiManager.isWifiEnabled()) {
            // If Wi-Fi is enabled, disable it
            wifiManager.setWifiEnabled(false);
            Toast.makeText(this, "Wi-Fi turned off", Toast.LENGTH_SHORT).show();
        } else {
            // If Wi-Fi is disabled, enable it
            wifiManager.setWifiEnabled(true);
            Toast.makeText(this, "Wi-Fi turned on", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAvailableWifiNetworks() {
        wifiManager.startScan();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<ScanResult> results = wifiManager.getScanResults();
        wifiList.clear();
        for (ScanResult result : results) {
            wifiList.add(result.SSID);
        }
        wifiAdapter.notifyDataSetChanged();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Available WiFi Networks")
                .setAdapter(wifiAdapter, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CHANGE_WIFI_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, toggle WiFi
                performWiFiToggle();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied. Cannot toggle WiFi.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
