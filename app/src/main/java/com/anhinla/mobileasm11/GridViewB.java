package com.anhinla.mobileasm11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

public class GridViewB extends View {
    private int rows = 8;
    private int columns = 8;
    private Paint gridPaint = new Paint();
    private Paint scanPaint = new Paint();
    private int scanningRow = 0;
    private boolean isScanning = false;
    private Handler handler = new Handler();
    private Runnable scanRunnable;

    public GridViewB(Context context) {
        super(context);
        init();
    }

    public GridViewB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Grid lines (white)
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStrokeWidth(2f);

        // Scanning line (animated blue line)
        scanPaint.setColor(Color.parseColor("#4285F4")); // Google blue
        scanPaint.setStrokeWidth(8f);
        scanPaint.setAlpha(180); // Slightly transparent
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float cellWidth = (float) width / columns;
        float cellHeight = (float) height / rows;

        // Draw grid lines
        for (int i = 1; i < columns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, gridPaint);
        }
        for (int i = 1; i < rows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, gridPaint);
        }

        // Draw scanning line (if active)
        if (isScanning) {
            float yPos = scanningRow * cellHeight;
            canvas.drawLine(0, yPos, width, yPos, scanPaint);
        }
    }

    // Start/stop scanning animation
    public void startScanning() {
        isScanning = true;
        scanRunnable = new Runnable() {
            @Override
            public void run() {
                scanningRow = (scanningRow + 1) % rows;
                invalidate(); // Redraw with new scan line position
                handler.postDelayed(this, 200); // Speed: 200ms per row
            }
        };
        handler.post(scanRunnable);
    }

    public void stopScanning() {
        isScanning = false;
        handler.removeCallbacks(scanRunnable);
        invalidate();
    }

    public void setGridSize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        invalidate();
    }
}