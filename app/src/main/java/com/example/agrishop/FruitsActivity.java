package com.example.agrishop;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FruitsActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView fruitImageView;
    private TextView fruitNameTextView;
    private TextView fruitPriceTextView;
    private Button quantityButton;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private static final int SHAKE_THRESHOLD = 1000; // Adjust as needed
    private boolean isZoomedIn = false;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);

        fruitImageView = findViewById(R.id.fruit_image_view);
        fruitNameTextView = findViewById(R.id.fruit_name_text_view);
        fruitPriceTextView = findViewById(R.id.fruit_price_text_view);
        quantityButton = findViewById(R.id.quantity_button);

        // Set fruit image, name, and price (Replace with your actual data)
        fruitImageView.setImageResource(R.drawable.apple); // Example image
        fruitNameTextView.setText("Apple"); // Example fruit name
        fruitPriceTextView.setText("$2.99"); // Example price

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastShakeTime = System.currentTimeMillis();

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isZoomedIn) {
                    // Zoom out
                    ScaleAnimation scaleAnimation = new ScaleAnimation(2f, 1f, 2f, 1f, fruitImageView.getWidth() / 2, fruitImageView.getHeight() / 2);
                    scaleAnimation.setDuration(500);
                    fruitImageView.startAnimation(scaleAnimation);
                    isZoomedIn = false;
                } else {
                    // Zoom in
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, fruitImageView.getWidth() / 2, fruitImageView.getHeight() / 2);
                    scaleAnimation.setDuration(500);
                    fruitImageView.startAnimation(scaleAnimation);
                    isZoomedIn = true;
                }
                return true;
            }
        });

        quantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastShakeTime) > SHAKE_THRESHOLD) {
                if ((Math.abs(x) > 15 || Math.abs(y) > 15 || Math.abs(z) > 15)) {
                    // Shake detected, add fruits to cart
                    Toast.makeText(this, "Shake detected! Adding fruits to cart...", Toast.LENGTH_SHORT).show();
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(500); // Vibrate for 500 milliseconds
                    }
                    lastShakeTime = currentTime;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_1_kg:
                        // Handle 1 kg selection
                        Toast.makeText(FruitsActivity.this, "1 kg selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_2_kg:
                        // Handle 2 kg selection
                        Toast.makeText(FruitsActivity.this, "2 kg selected", Toast.LENGTH_SHORT).show();
                        return true;
                    // Add cases for other quantities as needed
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.quantity_menu); // You might want to create a quantity menu XML
        popupMenu.show();
    }
}
