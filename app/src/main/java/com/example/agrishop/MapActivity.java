package com.example.agrishop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView addressTextView;
    private TextView weatherTextView;
    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        addressTextView = findViewById(R.id.addressTextView);
        weatherTextView = findViewById(R.id.weatherTextView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize LocationManager and LocationListener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateAddress(location);
                displayRandomWeather(); // Display random weather information
                addMarkerToCurrentLocation(location);
            }

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        // Request location updates
        if (checkLocationPermission()) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        } else {
            requestLocationPermission();
        }
    }

    private void updateAddress(Location location) {
        if (addressTextView != null && location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String address = getAddressFromCoordinates(latitude, longitude);

            // Format the address string with latitude and longitude
            String fullAddress = String.format(Locale.getDefault(), "Latitude: %f\nLongitude: %f\n%s", latitude, longitude, address);

            addressTextView.setText(fullAddress);
        } else if (addressTextView != null) {
            addressTextView.setText("Location not available");
        }
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String addressText = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Concatenate address details into a single string
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append(", ");
                }
                addressText = stringBuilder.toString().trim();
            } else {
                addressText = "Address not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressText = "Error fetching address";
        }

        return addressText;
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
                // Permission granted, request location updates
                if (checkLocationPermission()) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove location updates to avoid memory leaks
        locationManager.removeUpdates(locationListener);
    }

    private void displayRandomWeather() {
        if (weatherTextView != null) {
            String[] weatherConditions = {"Sunny", "Cloudy", "Rainy", "Snowy", "Windy"};
            Random random = new Random();
            int randomIndex = random.nextInt(weatherConditions.length);
            String randomWeather = weatherConditions[randomIndex];
            String temperature = generateRandomTemperature();
            String weatherInfo = randomWeather + ", " + temperature;
            weatherTextView.setText(weatherInfo);
        }
    }

    private String generateRandomTemperature() {
        Random random = new Random();
        int minTemp = -20;
        int maxTemp = 40;
        int temperature = random.nextInt(maxTemp - minTemp + 1) + minTemp;
        return temperature + "°C";
    }

    private void addMarkerToCurrentLocation(Location location) {
        if (location != null && mMap != null) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            String address = getAddressFromCoordinates(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(currentLatLng)
                    .title("Your Location")
                    .snippet(address); // Set the address as the marker's snippet

            mMap.clear(); // Clear existing markers
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
        }
    }
}
