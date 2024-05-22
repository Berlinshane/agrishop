package com.example.agrishop;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayBlogActivity extends AppCompatActivity {

    private boolean isDrawingMode = false;
    private DrawingView drawingView;
    private ImageView imageView;

    private SeekBar strokeWidthSeekBar;
    private Button colorButton;

    private Button clearButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blog);

        // Get data from the intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUriString = getIntent().getStringExtra("imageUri");

        // Set title and description
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Set image
        imageView = findViewById(R.id.imageView);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        }

        // Initialize drawing view
        drawingView = findViewById(R.id.drawingView);
        strokeWidthSeekBar = findViewById(R.id.strokeWidthSeekBar);
        colorButton = findViewById(R.id.colorButton);
        clearButton = findViewById(R.id.clearButton);

        // Set listeners for color and stroke width selection
        strokeWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the stroke width of the drawing view
                drawingView.setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for stroke width adjustment
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for stroke width adjustment
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show color picker dialog or implement your color selection logic here
                drawingView.setColor(Color.RED); // Example: Set color to red
            }
        });

        // Set click listener for the edit button
        Button editButton = findViewById(R.id.editButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the drawing
                drawingView.clearDrawing();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawingMode) {
                    // Exit drawing mode
                    drawingView.setVisibility(View.VISIBLE);
                    isDrawingMode = false;
                    // Apply changes made during drawing mode here if needed
                    editButton.setText("Edit");
                } else {
                    // Enter drawing mode
                    drawingView.setVisibility(View.VISIBLE);
                    isDrawingMode = true;
                    editButton.setText("Done");
                }
            }
        });

        // Initialize animation button
        Button animationButton = findViewById(R.id.animationButton);

        // Set click listener for the animation button
        animationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimationOptions();
            }
        });
    }

    private void showAnimationOptions() {
        // Show options for different animations
        String[] options = {"Rotate Animation", "Move Animation", "Slide Up Animation",
                "Slide Down Animation", "Bounce Animation", "Sequential Animation", "Together Animation",
                "Fade In Animation", "Fade Out Animation", "Cross Fading Animation",
                "Blink Animation", "Zoom In Animation", "Zoom Out Animation"};

        // Create a dialog to display animation options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Animation");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the selected animation
                switch (which) {
                    case 0:
                        performRotateAnimation();
                        break;
                    case 1:
                        performMoveAnimation();
                        break;
                    case 2:
                        performSlideUpAnimation();
                        break;
                    case 3:
                        performSlideDownAnimation();
                        break;
                    case 4:
                        performBounceAnimation();
                        break;
                    case 5:
                        performSequentialAnimation();
                        break;
                    case 6:
                        performTogetherAnimation();
                        break;
                    case 7:
                        performFadeInAnimation();
                        break;
                    case 8:
                        performFadeOutAnimation();
                        break;
                    case 9:
                        performCrossFadingAnimation();
                        break;
                    case 10:
                        performBlinkAnimation();
                        break;
                    case 11:
                        performZoomInAnimation();
                        break;
                    case 12:
                        performZoomOutAnimation();
                        break;
                }
            }
        });
        builder.show();
    }

    // Example of Rotate Animation
    private void performRotateAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Move Animation
    private void performMoveAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, 200f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Slide Up Animation
    private void performSlideUpAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -200f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Slide Down Animation
    private void performSlideDownAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, 200f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Bounce Animation
    private void performBounceAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }

    // Example of Sequential Animation
    private void performSequentialAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }

    // Example of Together Animation
    private void performTogetherAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }

    // Example of Fade In Animation
    private void performFadeInAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Fade Out Animation
    private void performFadeOutAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
        animator.setDuration(1000); // Set duration for the animation in milliseconds
        animator.start();
    }

    // Example of Cross Fading Animation
    private void performCrossFadingAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }

    // Example of Blink Animation
    private void performBlinkAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        animator.setDuration(500); // Set duration for each half of the animation in milliseconds
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(Animation.INFINITE); // Infinite blinking
        animator.start();
    }

    // Example of Zoom In Animation
    private void performZoomInAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }

    // Example of Zoom Out Animation
    private void performZoomOutAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.5f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1.5f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setDuration(1000); // Set duration for the animation in milliseconds
        animatorSet.start();
    }
}
