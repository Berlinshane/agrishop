package com.example.agrishop;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;

public class LiveMarketActivity extends AppCompatActivity {

    private TextView selectedDateTimeTextView;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button showRateButton;
    private LinearLayout vegetablesLayout;
    private ProgressBar progressBar;

    private String[] vegetables = {"Tomato", "Carrot", "Spinach", "Broccoli", "Cabbage", "Onion", "Potato"};
    private int[] rates = new int[vegetables.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_market);

        selectedDateTimeTextView = findViewById(R.id.selectedDateTimeTextView);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        showRateButton = findViewById(R.id.showRateButton);
        vegetablesLayout = findViewById(R.id.vegetablesLayout);
        progressBar = findViewById(R.id.progressBar);

        // Set the DatePicker and TimePicker to wrap content
        datePicker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        timePicker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        showRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedDateTimeAndRate();
            }
        });
    }

    private void displaySelectedDateTimeAndRate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        String selectedDateTime = "Selected Date and Time: " + calendar.getTime().toString();
        selectedDateTimeTextView.setText(selectedDateTime);

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Simulate a delay for demonstration purpose
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Generate random rates for vegetables...
                for (int i = 0; i < vegetables.length; i++) {
                    rates[i] = (int) getVegetablesRate(vegetables[i]);
                }
                // Clear previous views
                vegetablesLayout.removeAllViews();

                TextView selectedDateTimeTextView = new TextView(LiveMarketActivity.this);
                selectedDateTimeTextView.setText(selectedDateTime);
                vegetablesLayout.addView(selectedDateTimeTextView);

                // Display vegetables with rates...
                for (int i = 0; i < vegetables.length; i++) {
                    TextView textView = new TextView(LiveMarketActivity.this);
                    textView.setText(vegetables[i] + ": ₹" + rates[i]);
                    vegetablesLayout.addView(textView);
                }


                // Hide progress bar
                progressBar.setVisibility(View.GONE);
            }
        }, 2000); // 2 seconds delay for demonstration
    }

    private double getVegetablesRate(String vegetable) {
        // This method generates random rates between 20 and 100
        // Adjust the range as needed
        Random random = new Random();
        return Math.round(20 + (random.nextDouble() * 80)); // Generate a random value between 20 and 100 and round it to integer
    }
}
