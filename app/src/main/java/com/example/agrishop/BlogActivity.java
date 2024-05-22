package com.example.agrishop;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.io.IOException;

public class BlogActivity extends AppCompatActivity {

    EditText title, des;
    ImageView image;
    Uri imageUri = null;
    Button upload;

    private static final int CAMERA_REQUEST = 101;
    private static final int SELECT_IMAGE = 102;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_blogs);

        title = findViewById(R.id.ptitle);
        des = findViewById(R.id.pdes);
        image = findViewById(R.id.imagep);
        upload = findViewById(R.id.pupload);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                                image.setImageBitmap(bitmap);
                                imageUri = selectedImageUri; // Set image URI
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        image.setImageBitmap(bitmap);
                        imageUri = getImageUri(getApplicationContext(), bitmap);
                    }
                });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl = title.getText().toString().trim();
                String description = des.getText().toString().trim();

                if (TextUtils.isEmpty(titl)) {
                    title.setError("Title Cant be empty");
                    Toast.makeText(BlogActivity.this, "Title can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    des.setError("Description Cant be empty");
                    Toast.makeText(BlogActivity.this, "Description can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(BlogActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
                    return;
                }

                // Navigate to the next activity with the entered data
                Intent intent = new Intent(BlogActivity.this, DisplayBlogActivity.class);
                intent.putExtra("title", titl);
                intent.putExtra("description", description);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
            }
        });
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"}; // Both camera and gallery options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    pickFromCamera();
                } else if (which == 1) {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void pickFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private Boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getImageUri(android.content.Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
