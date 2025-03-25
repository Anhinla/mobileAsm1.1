package com.anhinla.mobileasm11;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;

public class SurfaceActivity extends AppCompatActivity {
    private TextView animatedText;
    public static final int PERMISSION_CAMERA = 1;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private GridViewB gridView;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private boolean isCameraInitialized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);

        // Initialize views
        gridView = findViewById(R.id.gridView);
        gridView.setGridSize(8, 4);
        previewView = findViewById(R.id.previewView);
        animatedText = findViewById(R.id.animatedText);


        // Check permissions first
        if (checkPermissions()) {
            initializeCamera();
        }

        // Start grid scanning
        gridView.startScanning();

        // Set up animations
        animateText();

        // Set up back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SurfaceActivity.this, SurfaceObjectActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return false;
        }
        return true;
    }

    private void initializeCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Initialize ImageCapture
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                // Bind preview and image capture
                bindPreview(cameraProvider);

                // Schedule image capture after initialization
                new Handler(Looper.getMainLooper()).postDelayed(this::captureImage, 6000);

                isCameraInitialized = true;
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Camera initialization failed", e);
                Toast.makeText(this, "Camera initialization failed", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Unbind any previous use cases first
        cameraProvider.unbindAll();

        // Create preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Camera selector
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        try {
            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture);
        } catch (Exception e) {
            Log.e("CameraX", "Use case binding failed", e);
        }
    }

    private void captureImage() {
        if (!isCameraInitialized || imageCapture == null) {
            Log.e("CameraX", "Camera not initialized, cannot capture image");
            return;
        }

        File photoFile = new File(getExternalFilesDir(null), "IMG_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    Uri savedUri = Uri.fromFile(photoFile);
                    openObjectActivity(savedUri);
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> {
                    Log.e("CameraX", "Image capture failed", exception);
                    Toast.makeText(SurfaceActivity.this, "Image capture failed", Toast.LENGTH_SHORT).show();
                    // Fallback: proceed to next activity even if capture fails
                    openObjectActivity(null);
                });
            }
        });
    }

    private void openObjectActivity(Uri imageUri) {
        Intent intent = new Intent(SurfaceActivity.this, SurfaceObjectActivity.class);
        if (imageUri != null) {
            intent.putExtra("captured_image_uri", imageUri.toString());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                // Fallback: proceed without camera
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        openObjectActivity(null), 6000);
            }
        }
    }

    private void animateText() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(6000);
        fadeIn.setFillAfter(true);

        animatedText.startAnimation(fadeIn);
        animatedText.setVisibility(View.VISIBLE);
        animatedText.setOnClickListener(view -> {
            openObjectActivity(null);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gridView != null) {
            gridView.stopScanning();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}