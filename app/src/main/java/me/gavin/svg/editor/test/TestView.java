package me.gavin.svg.editor.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {

    private final Paint backgroundPaint, backgroundPaint1;
    private final int backgroundGridWidth = 64;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xffffffff);
        backgroundPaint1 = new Paint();
        backgroundPaint1.setColor(0xffcccccc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : 1024,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : 1024);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawBackground(canvas, 512, 512);

    }

    private void drawBackground(Canvas canvas, int width, int height) {
        int widthCount = (width + backgroundGridWidth - 1) / backgroundGridWidth;
        int heightCount = (height + backgroundGridWidth - 1) / backgroundGridWidth;
        int hOffset = (getWidth() - width) / 2;
        int vOffset = (getHeight() - height) / 2;
        canvas.translate(hOffset, vOffset);
        for (int v = 0; v < heightCount; v++) {
            for (int h = 0; h < widthCount; h++) {
                canvas.drawRect(h * backgroundGridWidth, v * backgroundGridWidth,
                        h * backgroundGridWidth + backgroundGridWidth / 2, v * backgroundGridWidth + backgroundGridWidth / 2, backgroundPaint);
                canvas.drawRect(h * backgroundGridWidth + backgroundGridWidth / 2, v * backgroundGridWidth,
                        h * backgroundGridWidth + backgroundGridWidth, v * backgroundGridWidth + backgroundGridWidth / 2, backgroundPaint1);
                canvas.drawRect(h * backgroundGridWidth, v * backgroundGridWidth + backgroundGridWidth / 2,
                        h * backgroundGridWidth + backgroundGridWidth / 2, v * backgroundGridWidth + backgroundGridWidth, backgroundPaint1);
                canvas.drawRect(h * backgroundGridWidth + backgroundGridWidth / 2, v * backgroundGridWidth + backgroundGridWidth / 2,
                        h * backgroundGridWidth + backgroundGridWidth, v * backgroundGridWidth + backgroundGridWidth, backgroundPaint);
            }
        }
    }
}
