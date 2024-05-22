package com.example.agrishop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class FarmerInfoActivity extends AppCompatActivity {

    private ArrayList<String> farmerNames; // List of predefined farmer names
    private ArrayList<Farmer> farmers; // List of Farmer objects
    private FarmerAdapter adapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_info);

        // Predefined farmer names
        farmerNames = new ArrayList<>();
        farmerNames.add("John Doe");
        farmerNames.add("Jane Smith");
        farmerNames.add("Michael Johnson");
        // Add more farmer names as needed

        // Retrieve selected fertilizer from the intent
        String selectedFertilizer = getIntent().getStringExtra("selectedFertilizer");

        // Generate random farmers with the selected fertilizer
        farmers = generateFarmers(selectedFertilizer);

        // Display the farmers in a ListView
        ListView farmersListView = findViewById(R.id.farmersListView);
        adapter = new FarmerAdapter(this, farmers);
        farmersListView.setAdapter(adapter);

        // Set up item click listener for the ListView
        farmersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Farmer selectedFarmer = farmers.get(position);
                sendPaymentSMS(selectedFarmer.getPhoneNumber());
            }
        });
    }

    private ArrayList<Farmer> generateFarmers(String selectedFertilizer) {
        ArrayList<Farmer> generatedFarmers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) { // Generate 5 random farmers
            String farmerName = farmerNames.get(random.nextInt(farmerNames.size())); // Select random farmer name
            String phoneNumber = "8903294682"; // Assuming a default phone number for now
            generatedFarmers.add(new Farmer(farmerName, phoneNumber, selectedFertilizer));
        }

        return generatedFarmers;
    }

    private void sendPaymentSMS(String phoneNumber) {
        String message = "Hello, I'm interested in purchasing from your farm. Could you please provide me with more information on availability and pricing?";

        // Create an intent with the ACTION_SENDTO action
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // Specify the phone number to send the SMS to
        intent.putExtra("sms_body", message);  // Specify the message body

        // Check if there is an app to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle case where no app can handle the intent
            showToast("No app to handle SMS");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
