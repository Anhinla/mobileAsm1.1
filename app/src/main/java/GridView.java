package com.anhinla.mobileasm11;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GridView extends View {
    private int rows = 8;
    private int columns = 8;
    private Paint paint = new Paint();

    public GridView(Context context) {
        super(context);
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Calculate cell width and height
        float cellWidth = (float) width / columns;
        float cellHeight = (float) height / rows;

        // Draw vertical lines
        for (int i = 1; i < columns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paint);
        }

        // Draw horizontal lines
        for (int i = 1; i < rows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, paint);
        }
    }

    public void setGridSize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        invalidate();
    }
}