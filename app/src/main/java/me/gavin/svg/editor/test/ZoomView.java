package me.gavin.svg.editor.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/30
 */
public class ZoomView extends View {

    private ZoomViewAttacher attacher;

    public Matrix canvasMatrix = new Matrix();

    Paint paint;

    public ZoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attacher = new ZoomViewAttacher(this);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getMode(
                widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : 200,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setMatrix(canvasMatrix);
        canvas.drawLine(50, 50, getWidth() - 50, getHeight() - 50, paint);
        canvas.drawLine(getWidth() - 50, 50, 50, getHeight() - 50, paint);
    }
}
