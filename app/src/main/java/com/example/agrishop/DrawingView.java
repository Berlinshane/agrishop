package com.example.agrishop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    private Paint paint;
    private Path currentPath;
    private List<Path> paths;
    private List<Integer> colors;
    private List<Integer> strokeWidths;
    private int currentColor;
    private int strokeWidth;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        currentPath = new Path();
        paths = new ArrayList<>();
        colors = new ArrayList<>();
        strokeWidths = new ArrayList<>();
        currentColor = Color.BLACK; // Default color
        strokeWidth = 5; // Default stroke width

        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw all paths
        for (int i = 0; i < paths.size(); i++) {
            paint.setColor(colors.get(i));
            paint.setStrokeWidth(strokeWidths.get(i));
            canvas.drawPath(paths.get(i), paint);
        }
        // Draw the current path
        canvas.drawPath(currentPath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start a new path
                currentPath.reset();
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                // Add points to the current path
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // Save the current path and its properties
                paths.add(new Path(currentPath));
                colors.add(currentColor);
                strokeWidths.add(strokeWidth);
                // Reset the current path
                currentPath.reset();
                break;
        }

        invalidate(); // Redraw the view
        return true;
    }

    public void setColor(int color) {
        currentColor = color;
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void clearDrawing() {
        // Clear all paths
        paths.clear();
        colors.clear();
        strokeWidths.clear();
        invalidate(); // Redraw the view
    }
}
