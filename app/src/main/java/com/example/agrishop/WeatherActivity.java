package com.example.agrishop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WeatherActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private TextView addressTextView;
    private TextView weatherTextView;
    private Geocoder geocoder;
    private Button showMapButton;
    private Button reverseGeocoderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        addressTextView = findViewById(R.id.addressTextView);
        weatherTextView = findViewById(R.id.weatherTextView);
        showMapButton = findViewById(R.id.showMapButton);
        reverseGeocoderButton = findViewById(R.id.reverseGeocoderButton);
        geocoder = new Geocoder(this, Locale.getDefault());

        reverseGeocoderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performReverseGeocoding();
            }
        });

        // Set OnClickListener for the showMapButton
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

        // Set OnClickListener for the reverseGeocoderButton

        if (checkLocationPermission()) {
            fetchLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLocation() {
        if (checkLocationPermission()) {
            android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
                if (location != null) {
                    displayAddress(location);
                    displayRandomWeather();
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            requestLocationPermission();
        }
    }

    private void displayAddress(Location location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressString = address.getAddressLine(0);
                addressTextView.setText(addressString);
            } else {
                addressTextView.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressTextView.setText("Error fetching address");
        }
    }

    private void displayRandomWeather() {
        String[] weatherConditions = {"Sunny", "Cloudy", "Rainy", "Snowy", "Windy"};
        Random random = new Random();
        int randomIndex = random.nextInt(weatherConditions.length);
        String randomWeather = weatherConditions[randomIndex];
        String temperature = generateRandomTemperature();
        String weatherInfo = randomWeather + ", " + temperature;
        weatherTextView.setText(weatherInfo);
    }

    private String generateRandomTemperature() {
        Random random = new Random();
        int minTemp = -20; // Minimum temperature
        int maxTemp = 40; // Maximum temperature
        int temperature = random.nextInt(maxTemp - minTemp + 1) + minTemp;
        return temperature + "°C";
    }
    private void performReverseGeocoding() {
        startActivity(new Intent(WeatherActivity.this, ReverseGeocoderActivity.class));
    }

    private void showMap() {
        // Add your code to show the map activity here
        startActivity(new Intent(WeatherActivity.this, MapActivity.class));
    }

}
