package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;
import me.gavin.svg.editor.svg.parser.FigureHelper;

/**
 * SVGView
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    private final Matrix mMatrix = new Matrix();
    private float mScale = 1f;

    private SVG mSvg;

    private final Paint backgroundPaint, backgroundPaint1;
    private final int backgroundGridWidth = 56;

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

        for (int i = 0; i < mSvg.drawables.size(); i++) {
            mSvg.drawables.get(i).getStrokePaint().setStrokeWidth(
                    mSvg.drawables.get(i).getStrokeWidth() * mScale);

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
        mScale = 1f;
        mSvg.paths.clear();
        for (int i = 0; i < mSvg.drawables.size(); i++) {
            Path path = new Path();
            FigureHelper.transform(path, mSvg.drawables.get(i));
            svg.paths.add(path);
        }
        postScale(Math.min(getWidth() / mSvg.viewBox.width, getHeight() / mSvg.viewBox.height), 0, 0);
        invalidate();
    }

    public void postScale(float scaleFactor, float focusX, float focusY) {
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
        mScale *= scaleFactor;
        for (Path path : mSvg.paths) {
            path.transform(mMatrix);
        }
        invalidate();
    }

    public void postTranslate(float dx, float dy) {
        mMatrix.reset();
        mMatrix.postTranslate(dx, dy);
        for (Path path : mSvg.paths) {
            path.transform(mMatrix);
        }
        invalidate();
    }

    public boolean drawable() {
        return mSvg != null && mSvg.drawables != null && !mSvg.drawables.isEmpty();
    }

    public void setZoomable(boolean zoomable) {
        if (zoomable) {
            SVGViewAttacher.attach(this);
        } else {
            setOnTouchListener(null);
        }
    }

}
