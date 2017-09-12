package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;

/**
 * SVGView
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    public final Matrix mMatrix = new Matrix();

    private SVG mSvg;

    private final Paint backgroundPaint, backgroundPaint1;
    private final int backgroundGridWidth = 64;

    public SVGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
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
                        : mSvg == null ? 0 : ((int) mSvg.width),
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : mSvg == null ? 0 : ((int) mSvg.height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSvg == null) {
            return;
        }

        drawBackground(canvas);

        canvas.setMatrix(mMatrix);

        for (int i = 0; i < mSvg.paths.size(); i++) {
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getFillPaint());
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getStrokePaint());
        }
    }

    private void drawBackground(Canvas canvas) {
        int widthCount = (getWidth() + backgroundGridWidth - 1) / backgroundGridWidth;
        int heightCount = (getHeight() + backgroundGridWidth - 1) / backgroundGridWidth;
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

    public void set(SVG svg) {
        this.mSvg = svg;
        mMatrix.reset();
        float scale = Math.min(getWidth() / mSvg.viewBox.width, getHeight() / mSvg.viewBox.height);
        mMatrix.postScale(scale, scale, 0, 0);
        postInvalidate();
    }

    public boolean drawable() {
        return mSvg != null && mSvg.paths != null && !mSvg.paths.isEmpty();
    }

    public void setZoomable(boolean zoomable) {
        if (zoomable) {
            SVGViewAttacher.attach(this);
        } else {
            setOnTouchListener(null);
        }
    }

}
