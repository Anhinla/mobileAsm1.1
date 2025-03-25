package com.anhinla.mobileasm11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ObjectActivity extends AppCompatActivity {

    private ToggleButton currentCheckedButton = null;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearContainer;
    private ArrayList<String> itemList;
    private Button selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_object);

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

        setupToggleButton(btnInfo);
        setupToggleButton(btnPractice);
        setupToggleButton(btnMore);

        ImageView imgQr = findViewById(R.id.qr);

        imgQr.setOnClickListener(v -> {
            Intent intent = new Intent(ObjectActivity.this, ScanActivity.class);
            startActivity(intent);
        });
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