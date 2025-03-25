package com.anhinla.mobileasm11;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import androidx.core.content.ContextCompat;
import android.content.Context;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    float centerX;
    float centerY;
    float baseRadius;
    float hatRadius;
    private JoystickListener joystickCallback;
    private final int ratio = 5;
    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener((OnTouchListener) this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public Joystick(Context context, AttributeSet attrs) {

        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener((OnTouchListener) this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public Joystick(Context context) {

        super(context);
        getHolder().addCallback(this);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setOnTouchListener((OnTouchListener) this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }
    public interface JoystickListener{

        void onJoystickMoved(float xPercent, float yPercent, int source);
    }

    private void setupDimensions() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        baseRadius = Math.min(getWidth(), getHeight()) / 3f;
        hatRadius = Math.min(getWidth(), getHeight()) / 7f;
    }
    private  void drawJoystick(float newX, float newY){
        if (getHolder().getSurface().isValid())
        {
            Canvas myCan = this.getHolder().lockCanvas();
            if (myCan != null) {
                myCan.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                Paint colors = new Paint();
                //draw base circle
                int theme = ContextCompat.getColor(getContext(), R.color.themeTrans);
                colors.setColor(theme);
                myCan.drawCircle(centerX, centerY, baseRadius, colors);
                //draw stroke of base circle
                colors.setStyle(Paint.Style.STROKE);
                colors.setStrokeWidth(6f); // Độ dày viền
                colors.setColor(Color.WHITE); // Màu viền
                myCan.drawCircle(centerX, centerY, baseRadius, colors);

                //draw thumb
                int cursor = ContextCompat.getColor(getContext(), R.color.blue);
                colors.setColor(cursor);
                colors.setStyle(Paint.Style.FILL);
                myCan.drawCircle(newX, newY, hatRadius, colors);

                //draw stroke of thumb
                colors.setStyle(Paint.Style.STROKE);
                colors.setStrokeWidth(4f);
                colors.setColor(Color.WHITE);
                myCan.drawCircle(newX, newY, hatRadius, colors);

                getHolder().unlockCanvasAndPost(myCan);
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public float getHatRadius() {
        return hatRadius;
    }

    @SuppressLint("SuspiciousIndentation")
    public boolean onTouch(View v, MotionEvent e){
        if (v.equals(this)) {
            if (e.getAction() != MotionEvent.ACTION_UP) {
                float displacement = (float) Math.sqrt(Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2));
                if (displacement < baseRadius)
                    drawJoystick(e.getX(), e.getY());
                else {
                    float ratio = baseRadius / displacement;
                    float constrainX = centerX + (e.getX() - centerX) * ratio;
                    float constrainY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainX, constrainY);
                    if (joystickCallback != null)
                        joystickCallback.onJoystickMoved((constrainX - centerX) / baseRadius, (constrainY - centerY) / baseRadius, getId());
                }
            } else {
                drawJoystick(centerX, centerY);
                if (joystickCallback != null)
                    joystickCallback.onJoystickMoved(0, 0, getId());
            }
        }
        return true;
    }
}
