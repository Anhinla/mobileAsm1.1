package com.anhinla.mobileasm11;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.filament.gltfio.FilamentInstance;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import dev.romainguy.kotlin.math.Float3;
import io.github.sceneview.ar.ArSceneView;
import io.github.sceneview.ar.node.ArModelNode;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.widget.ToggleButton;


public class SurfaceActivity extends AppCompatActivity implements Joystick.JoystickListener{
    private ArSceneView sceneView;
    private ExtendedFloatingActionButton placeButton;
    private ArModelNode modelNode;
    private Handler handler = new Handler();

    private Joystick joystick;
    private float currentRotationY = 0f;
    private float currentRotationX = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);

        joystick = findViewById(R.id.joy);
        joystick.setZOrderOnTop(true);
        sceneView = findViewById(R.id.sceneView);
        sceneView.setLightEstimationMode(io.github.sceneview.ar.arcore.LightEstimationMode.DISABLED);

        placeButton = findViewById(R.id.placeButton);
        placeButton.setOnClickListener(v -> placeModel());

        modelNode = new ArModelNode(
                "models/cat_dispenser.glb",
                false, // autoAnimate
                1f,    // scaleToUnits
                new Float3(-0.5f, 0f, 0f), // centerOrigin (you can set Y, Z as needed)
                new Function1<Exception, Unit>() {
                    @Override
                    public Unit invoke(Exception exception) {
                        exception.printStackTrace(); // Handle loading error
                        return Unit.INSTANCE;
                    }
                },
                new Function1<FilamentInstance, Unit>() {
                    @Override
                    public Unit invoke(FilamentInstance instance) {
                        sceneView.getPlaneRenderer().setVisible(true);
                        // You can access material instances if needed:
                        // instance.getMaterialInstances().get(0);
                        return Unit.INSTANCE;
                    }
                }
        );

        modelNode.setOnAnchorChanged(anchor -> {
//            placeButton.setVisibility(anchor != null ? View.GONE : View.VISIBLE);
            if(anchor != null){
                placeButton.setVisibility(View.GONE);
                joystick.setVisibility(View.VISIBLE);
            }
            else{
                placeButton.setVisibility(View.VISIBLE);
                joystick.setVisibility(View.GONE);
            }
            return null;
        });

        sceneView.addChild(modelNode);

//        // Initial scale setup
//        float initialScale = 1.0f;
//        modelNode.setScale(new Float3(initialScale, initialScale, initialScale));

        // Set up back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SurfaceActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void placeModel() {
        modelNode.anchor();
        sceneView.getPlaneRenderer().setVisible(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null); // Stop rotation on pause
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        float rotationSpeed = 5.0f;

        currentRotationY += xPercent * rotationSpeed;
        currentRotationX += -yPercent * rotationSpeed; // Invert for natural feel

        currentRotationY = currentRotationY % 360;
        currentRotationX = Math.max(-45f, Math.min(currentRotationX, 45f)); // clamp

        if (modelNode != null) {
            modelNode.setModelRotation(new Float3(currentRotationX, currentRotationY, 0f));
        }
    }
}