package com.anhinla.mobileasm11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhinla.mobileasm11.databinding.ActivityChooseBinding;
import com.anhinla.mobileasm11.databinding.ActivityMainBinding;

public class ChooseActivity extends AppCompatActivity {
    private ActivityChooseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchImgBlock.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseActivity.this, ScanActivity.class);
            startActivity(intent);
        });
        binding.searchSurfaceBlock.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseActivity.this, SurfaceActivity.class);
            startActivity(intent);
        });
    }
}