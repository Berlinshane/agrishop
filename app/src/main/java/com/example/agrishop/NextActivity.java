package com.example.agrishop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class NextActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "Agrishop_Channel";
    public static final int NOTIFICATION_ID = 1;

    private BroadcastReceiver airplaneModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
                if (isAirplaneModeOn) {
                    showToast("Airplane mode turned ON");
                    disableButtons();
                } else {
                    showToast("Airplane mode turned OFF");
                    enableButtons();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        Button homeButton = findViewById(R.id.widget1);
        Button vegetablesButton = findViewById(R.id.widget2);
        Button fruitsButton = findViewById(R.id.widget3);
        Button liveMarketButton = findViewById(R.id.widget4);
        Button weatherButton = findViewById(R.id.widget5);
        Button fertilizersButton = findViewById(R.id.widget6);
        Button pesticidesButton = findViewById(R.id.widget7);
        Button blogButton = findViewById(R.id.widget8);
        Button FarmersButton = findViewById(R.id.FarmersButton);
        Button vendorInfoButton = findViewById(R.id.vendorInfoButton);
        Button awareness = findViewById(R.id.widget10);
        Button camera = findViewById(R.id.widget11);
        Button hard = findViewById(R.id.widget12);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button SettingsButton = findViewById(R.id.SettingsButton);
        Button smsButton = findViewById(R.id.smsButton);
        welcomeMessage.setText("Welcome to Agri-Shop!");

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Add click listeners for each button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Home Button Clicked!");
            }
        });

        vegetablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Vegetables Button Clicked!");
                // Example: Start VegetablesActivity
                startActivity(new Intent(NextActivity.this, VegetablesActivity.class));
            }
        });

        fruitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, FruitsActivity.class));
            }
        });

        liveMarketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, LiveMarketActivity.class));
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, WeatherActivity.class));
            }
        });

        fertilizersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Fertilizers Button Clicked!");

                // Sample fertilizer details
                ArrayList<String> fertilizerDetails = new ArrayList<>();
                fertilizerDetails.add("Miracle-Gro Water Soluble All Purpose Plant Food");
                fertilizerDetails.add("Osmocote Smart-Release Flower and Vegetable Plant Food");
                fertilizerDetails.add("Jobe's Organics Vegetable & Tomato Fertilizer Spikes");
                fertilizerDetails.add("Espoma Organic Garden-Tone All Purpose Plant Food");
                fertilizerDetails.add("Scotts Turf Builder Lawn Food");
                fertilizerDetails.add("Dr. Earth Premium Gold All Purpose Fertilizer");
                fertilizerDetails.add("Fish Emulsion Organic Liquid Fertilizer");
                fertilizerDetails.add("Schultz All Purpose Liquid Plant Food");
                fertilizerDetails.add("FoxFarm Ocean Forest Potting Soil");
                fertilizerDetails.add("Dyna-Gro Liquid Grow Plant Food");

                // Create an intent to start FertilizerDetailsActivity
                Intent intent = new Intent(NextActivity.this, FertilizerDetailsActivity.class);
                intent.putStringArrayListExtra("fertilizerDetails", fertilizerDetails);

                // Start the activity
                startActivity(intent);
            }
        });

        pesticidesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start PesticidesActivity
                startActivity(new Intent(NextActivity.this, PesticidesActivity.class));
            }
        });

        blogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NextActivity.this, BlogActivity.class));
            }
        });

        FarmersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NextActivity.this, FarmersActivity.class));
            }
        });


        vendorInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start PesticidesActivity
                startActivity(new Intent(NextActivity.this, FarmerDetailsActivity.class));
            }
        });

        awareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, AwarenessSong.class));
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, MediaRecord.class));
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextActivity.this, Hardware.class));
            }
        });

        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start PesticidesActivity
                startActivity(new Intent(NextActivity.this, SettingsActivity.class));
            }
        });


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start PesticidesActivity
                startActivity(new Intent(NextActivity.this, SMSsender.class));
            }
        });

        Button notificationButton = findViewById(R.id.notificationButton);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificationDialog();
            }
        });

        // Register the dynamic broadcast receiver for airplane mode changes
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the dynamic broadcast receiver when the activity is destroyed
        unregisterReceiver(airplaneModeReceiver);
    }

    private void showNotificationDialog() {
        // Create a layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = ((LayoutInflater) inflater).inflate(R.layout.custom_notification_dialog, null);

        // Build a dialog with a custom view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        //builder.setTitle("Notification");
        // builder.setMessage("Purchase on Friday to grab 10% offer");

        // Set a positive button and its click listener
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle the OK button click if needed
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void disableButtons() {
        Button vegetablesButton = findViewById(R.id.widget2);
        Button fruitsButton = findViewById(R.id.widget3);
        Button liveMarketButton = findViewById(R.id.widget4);
        Button weatherButton = findViewById(R.id.widget5);
        Button fertilizersButton = findViewById(R.id.widget6);
        Button pesticidesButton = findViewById(R.id.widget7);
        Button blogButton = findViewById(R.id.widget8);

        vegetablesButton.setEnabled(false);
        fruitsButton.setEnabled(false);
        liveMarketButton.setEnabled(false);
        weatherButton.setEnabled(false);
        fertilizersButton.setEnabled(false);
        pesticidesButton.setEnabled(false);
        blogButton.setEnabled(false);

    }

    private void enableButtons() {
        Button vegetablesButton = findViewById(R.id.widget2);
        Button fruitsButton = findViewById(R.id.widget3);
        Button liveMarketButton = findViewById(R.id.widget4);
        Button weatherButton = findViewById(R.id.widget5);
        Button fertilizersButton = findViewById(R.id.widget6);
        Button pesticidesButton = findViewById(R.id.widget7);
        Button blogButton = findViewById(R.id.widget8);

        vegetablesButton.setEnabled(true);
        fruitsButton.setEnabled(true);
        liveMarketButton.setEnabled(true);
        weatherButton.setEnabled(true);
        fertilizersButton.setEnabled(true);
        pesticidesButton.setEnabled(true);
        blogButton.setEnabled(true);
    }
}
