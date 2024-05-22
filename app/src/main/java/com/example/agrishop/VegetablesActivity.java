package com.example.agrishop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class VegetablesActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "vegetable_offer";
    private static final int NOTIFICATION_ID = 1;
    private Menu menu;
    private SharedPreferences sharedPreferences;
    private static final String DARK_MODE_PREF = "dark_mode_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetables);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        ImageView imgTomato = findViewById(R.id.imgTomato);
        ImageView imgOnion = findViewById(R.id.imgOnion);
        ImageView imgBrinjal = findViewById(R.id.imgBrinjal);
        ImageView imgLadyFinger = findViewById(R.id.imgLadyFinger);
        ImageView imgCabbage = findViewById(R.id.imgCabbage);
        ImageView imgMushroom = findViewById(R.id.imgMushroom);

        registerForContextMenu(findViewById(R.id.imgTomato));
        registerForContextMenu(findViewById(R.id.imgOnion));
        registerForContextMenu(findViewById(R.id.imgBrinjal));
        registerForContextMenu(findViewById(R.id.imgLadyFinger));
        registerForContextMenu(findViewById(R.id.imgCabbage));
        registerForContextMenu(findViewById(R.id.imgMushroom));

        // Set click listeners for each vegetable image
        imgTomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Tomato successfully");
            }
        });

        imgOnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Onion successfully");
            }
        });

        imgBrinjal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Brinjal successfully");
            }
        });

        imgLadyFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Lady Finger successfully");
            }
        });
        imgCabbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Cabbage successfully");
            }
        });
        imgMushroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You selected Mushroom successfully");
            }
        });

        // Find the "Show Notification" button
        Button showNotificationButton = findViewById(R.id.show_notification_button);
        showNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationWithOffer();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Set the menu header based on the selected vegetable
        if (v.getId() == R.id.imgTomato) {
            menu.setHeaderTitle("Tomato");
        } else if (v.getId() == R.id.imgOnion) {
            menu.setHeaderTitle("Onion");
        } else if (v.getId() == R.id.imgBrinjal) {
            menu.setHeaderTitle("Brinjal");
        } else if (v.getId() == R.id.imgLadyFinger) {
            menu.setHeaderTitle("Lady Finger");
        } else if (v.getId() == R.id.imgCabbage) {
            menu.setHeaderTitle("Cabbage");
        } else if (v.getId() == R.id.imgMushroom) {
            menu.setHeaderTitle("Mushroom");
        }

        // Inflate the context menu from the XML file
        getMenuInflater().inflate(R.menu.context_menu_vegetables, menu);
    }

    // Helper method to get the vegetable name based on the ImageView ID
    private String getVegetableName(int imageViewId) {
        switch (imageViewId) {
            case R.id.imgTomato:
                return "Tomato";
            case R.id.imgOnion:
                return "Onion";
            case R.id.imgBrinjal:
                return "Brinjal";
            case R.id.imgLadyFinger:
                return "Lady Finger";
            case R.id.imgCabbage:
                return "Cabbage";
            case R.id.imgMushroom:
                return "Mushroom";
            default:
                return "";
        }
    }

    // Handle long-click events on ImageView items
    public void onImageLongClick(View view) {
        // Show the context menu
        view.showContextMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vegetables_menu, menu);
        this.menu = menu; // Assign the menu object to the member variable
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Toggle visibility of Cart and Settings items
                MenuItem cartItem = menu.findItem(R.id.action_cart);
                cartItem.setVisible(!cartItem.isVisible());

                MenuItem settingsItem = menu.findItem(R.id.action_settings);
                settingsItem.setVisible(!settingsItem.isVisible());

                // Handle other actions for the Search item if needed
                showToast("Search item clicked");
                return true;
            case R.id.action_cart:
                // Toggle visibility of Search and Settings items
                MenuItem searchItemCart = menu.findItem(R.id.action_search);
                searchItemCart.setVisible(!searchItemCart.isVisible());

                MenuItem settingsItemCart = menu.findItem(R.id.action_settings);
                settingsItemCart.setVisible(false);

                // Handle other actions for the Cart item if needed
                showToast("Cart item clicked");
                return true;
            case R.id.action_settings:
                // Navigate to SettingsActivity
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showNotificationWithOffer() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Special Offer!")
                .setContentText("Get 10% off on all vegetables!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Vegetable Offer";
            String description = "Notification for vegetable offers";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
