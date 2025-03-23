package com.anhinla.mobileasm11;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ImageScanActivity extends AppCompatActivity {
    private View movingBar;
    private TextView animatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_scan);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        movingBar = findViewById(R.id.movingBar);
        animatedText = findViewById(R.id.animatedText);

        animateBar();

        animateText();

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ImageScanActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void animateBar() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(movingBar, "translationY", 0f, 1000f, 0f);
        animator.setDuration(6000);
        animator.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            movingBar.setVisibility(View.GONE); // Hide the bar
        }, 6000); // Delay of 5 seconds
    }

    private void animateText() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(6000);
        fadeIn.setFillAfter(true);

        animatedText.startAnimation(fadeIn);
        animatedText.setVisibility(View.VISIBLE);
    }
}