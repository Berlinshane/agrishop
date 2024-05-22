package com.example.agrishop;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find views
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView ageTextView = findViewById(R.id.ageTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);

        // Retrieve data from intent extras
        String name = getIntent().getStringExtra("name");
        int age = getIntent().getIntExtra("age", -1); // Default value -1 if not provided
        String gender = getIntent().getStringExtra("gender");
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        // Set text to TextViews
        nameTextView.setText("Name: " + name);
        ageTextView.setText("Age: " + age);
        genderTextView.setText("Gender: " + gender);

        // Get address from latitude and longitude
        String completeAddress = getAddressFromLocation(latitude, longitude);
        addressTextView.setText("Address: " + completeAddress);
    }

    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                return stringBuilder.toString();
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching address";
        }
    }
}
