package com.anhinla.mobileasm11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.SeekBar;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ObjectActivity extends AppCompatActivity {

    private ToggleButton currentCheckedButton = null;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearContainer;
    private ArrayList<String> itemList;
    private Button selectedButton = null;
    private boolean isinfo = false;
    private boolean ispos = false;
    private ImageButton radar ;
    private RecyclerView wheelRecyclerView;
    private boolean isWheelVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_object);
        ConstraintLayout mainLayout = findViewById(R.id.main); // Ensure this ID matches your XML layout
        // Receive captured image URI from ScanActivity
        String imageUriString = getIntent().getStringExtra("captured_image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            mainLayout.setBackground(Drawable.createFromPath(imageUri.getPath()));
        }

        linearContainer = findViewById(R.id.linearContainer);

        // Step 1: Populate the ArrayList
        itemList = new ArrayList<>();
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 4");
        itemList.add("Item 5");
        itemList.add("Item 6");

        // Step 2: Create buttons dynamically with the same style
        createButtons();

        ToggleButton btnInfo = findViewById(R.id.btnInfo);
        ToggleButton btnPractice = findViewById(R.id.btnPractice);
        ToggleButton btnMore = findViewById(R.id.btnMore);

        horizontalScrollView = findViewById(R.id.horizontalScrollView);

        btnInfo.setOnClickListener(v -> {
            setupToggleButton(btnInfo);
            isinfo = !isinfo;
            btnInfo.setChecked(isinfo);
            if(isinfo){
                btnInfo.setBackgroundResource(R.drawable.toggled_corner_radius_button);
            }
            else{
                btnInfo.setBackgroundResource(R.drawable.corner_radius_button);
            }
        });

        setupToggleButton(btnInfo);
        setupToggleButton(btnPractice);
        setupToggleButton(btnMore);

        ImageView imgQr = findViewById(R.id.qr);

        imgQr.setOnClickListener(v -> {
            Intent intent = new Intent(ObjectActivity.this, ScanActivity.class);
            startActivity(intent);
        });
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v ->{
            Intent intent = new Intent(ObjectActivity.this, ChooseActivity.class);
            startActivity(intent);
        });
        radar = findViewById(R.id.radar);
        radar.setOnClickListener(v ->{
            Intent intent = new Intent(ObjectActivity.this, SurfaceActivity.class);
            startActivity(intent);
        });
        ImageButton pos =findViewById(R.id.position);
        Joystick joy = findViewById(R.id.joy);
        SeekBar seekbar = findViewById(R.id.zoomSeekBar);
        LinearLayout info_practice = findViewById(R.id.info_practice);
        pos.setOnClickListener( v-> {
                    ispos = !ispos; // Toggle the state
                    if (ispos) {
                        pos.setBackground(ContextCompat.getDrawable(ObjectActivity.this, R.drawable.selected_round_button));
                        pos.setImageResource(R.drawable.selected_position);
                        joy.setVisibility(View.VISIBLE);
                        seekbar.setVisibility(View.VISIBLE);
                        info_practice.setVisibility(View.GONE);
                        hideHorizontalScrollView();
                    } else {
                        pos.setBackground(ContextCompat.getDrawable(ObjectActivity.this, R.drawable.round_button));
                        pos.setImageResource(R.drawable.position);
                        joy.setVisibility(View.GONE);
                        seekbar.setVisibility(View.GONE);
                        info_practice.setVisibility(View.VISIBLE);
                        if(btnMore.isChecked()) {
                            showHorizontalScrollView();
                        }
                        else{
                            hideHorizontalScrollView();
                        }
                    }
                }
        );
        wheelRecyclerView = findViewById(R.id.wheelRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        wheelRecyclerView.setLayoutManager(layoutManager);
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            items.add("Item " + i);
        }
        WheelAdapter adapter = new WheelAdapter(this, items);
        wheelRecyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(wheelRecyclerView);
        wheelRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                updateOpacity(recyclerView);
            }
        });
        ImageButton settingButton = findViewById(R.id.setting);
        settingButton.setOnClickListener(v -> {
            isWheelVisible = !isWheelVisible;
            if (isWheelVisible) {
                wheelRecyclerView.setVisibility(View.GONE);
            } else {
                wheelRecyclerView.setVisibility(View.VISIBLE);
                settingButton.setBackgroundResource(R.drawable.selected_round_button);
                settingButton.setImageResource(R.drawable.selected_setting);
            }
        });
    }
    private void updateOpacity(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager == null) return;
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        int centerPosition = (firstVisible + lastVisible) / 2;
        for (int i = firstVisible; i <= lastVisible; i++) {
            View itemView = layoutManager.findViewByPosition(i);
            if (itemView != null) {
                float distance = Math.abs(i - centerPosition);
                float alpha = 1.0f - (distance / (float)(lastVisible - firstVisible + 1));
                itemView.setAlpha(Math.max(alpha, 0.5f)); // minimum opacity 50%
            }
        }
    }
    private void createButtons() {
        for (String item : itemList) {
            final Button button = new Button(this);
            button.setText(item);
            button.setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
            button.setBackgroundResource(R.drawable.item_background);
            button.setPadding(20, 10, 20, 10);
            button.setAllCaps(false);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                if (selectedButton != null) {
                    selectedButton.setBackgroundResource(R.drawable.item_background);
                    selectedButton.setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
                }
                button.setBackgroundResource(R.drawable.selected_item_background);
                button.setTextColor(ContextCompat.getColor(this, R.color.white));
                selectedButton = button;
            });
            linearContainer.addView(button);
        }
    }

    private void setupToggleButton(ToggleButton toggleButton) {
        toggleButton.setOnClickListener(v -> {
            boolean isChecked = toggleButton.isChecked();

            if (isChecked) {
                if (currentCheckedButton != null && currentCheckedButton != toggleButton) {
                    currentCheckedButton.setChecked(false);
                    currentCheckedButton.setBackgroundResource(R.drawable.corner_radius_button);
                }

                toggleButton.setBackgroundResource(R.drawable.toggled_corner_radius_button);
                currentCheckedButton = toggleButton;

                if (toggleButton.getId() == R.id.btnMore) {
                    showHorizontalScrollView();
                } else if (toggleButton.getId() == R.id.btnPractice) {
                    Intent intent = new Intent(ObjectActivity.this, ScanActivity.class);
                    startActivity(intent);
                }
                else{
                    hideHorizontalScrollView();
                }
            } else {
                toggleButton.setBackgroundResource(R.drawable.corner_radius_button);

                if (toggleButton.getId() == R.id.btnMore) {
                    hideHorizontalScrollView();
                }

                currentCheckedButton = null;
            }
        });
    }

    private void showHorizontalScrollView() {
        horizontalScrollView.setVisibility(View.VISIBLE);
    }

    private void hideHorizontalScrollView() {
        horizontalScrollView.setVisibility(View.GONE);
    }
}