package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;
import me.gavin.svg.editor.util.L;

/**
 * SVGView
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    public SVG mSvg;
    private boolean newTag;

    // svg
    private float mInherentWidthScale, mInherentHeightScale;

    private final Paint backgroundPaint, backgroundPaint1;

    private final float[] mMatrixValues = new float[9];

    public SVGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0x80ffffff);
        backgroundPaint1 = new Paint();
        backgroundPaint1.setColor(0x80cccccc);
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

        drawBefore();

        canvas.setMatrix(mSvg.matrix);

        drawBackground(canvas);

        for (int i = 0; i < mSvg.paths.size(); i++) {
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getFillPaint());
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getStrokePaint());
        }
    }

    private void drawBefore() {
        if (newTag) {
            newTag = false;
            mSvg.matrix.reset();
            mInherentWidthScale = mSvg.width / mSvg.viewBox.width;
            mInherentHeightScale = mSvg.height / mSvg.viewBox.height;
            mSvg.matrix.postScale(mSvg.width / mSvg.viewBox.width, mSvg.height / mSvg.viewBox.height);
            float size = Math.min(getWidth(), getHeight()) * 0.8f;
            float scale = Math.min(size / mSvg.width, size / mSvg.height);
            mSvg.matrix.postTranslate((getWidth() - mSvg.width) / 2f, (getHeight() - mSvg.height) / 2f);
            mSvg.matrix.postScale(scale, scale, getWidth() / 2f, getHeight() / 2f);
        }
    }

    private void drawBackground(Canvas canvas) {
        final float backgroundGridWidth = 64f;
        float scaleX = getValue(Matrix.MSCALE_X);
        float scaleY = getValue(Matrix.MSCALE_Y);
        float gridW = backgroundGridWidth / scaleX;
        float gridH = backgroundGridWidth / scaleY;
        float wc = mSvg.width * scaleX / backgroundGridWidth / mInherentWidthScale;
        float hc = mSvg.height * scaleY / backgroundGridWidth / mInherentHeightScale;
        int widthCount = ((int) wc);
        int heightCount = ((int) hc);
        float wr = wc - widthCount;
        float hr = hc - heightCount;

        float l, l1, t, t1, r, r1, b, b1;
        for (int v = 0; v <= heightCount; v++) {
            for (int h = 0; h <= widthCount; h++) {
                l = h * gridW;
                l1 = l + gridW * 0.5f;
                t = v * gridH;
                t1 = t + gridH * 0.5f;

                if (v == heightCount) {
                    L.e(wr);
                }

                if (h == widthCount && wr > 0) {
                    r = wr > 0.5f ? l1 : (l + gridW * wr);
                    r1 = wr > 0.5f ? (l + gridW * wr) : l1;
                } else {
                    r = l1;
                    r1 = l + gridW;
                }
                if (v == heightCount && hr > 0) {
                    b = hr > 0.5f ? t1 : (t + gridH * hr);
                    b1 = hr > 0.5f ? (t + gridH * hr) : t1;
                } else {
                    b = t1;
                    b1 = t + gridH;
                }

                canvas.drawRect(l, t, r, b, backgroundPaint);
                canvas.drawRect(l1, t, r1, b, backgroundPaint1);
                canvas.drawRect(l, t1, r, b1, backgroundPaint1);
                canvas.drawRect(l1, t1, r1, b1, backgroundPaint);
            }
        }

    }

    public void set(SVG svg) {
        this.mSvg = svg;
        newTag = true;
        requestLayout();
        invalidate();
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

    private float getValue(int whichValue) {
        mSvg.matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

}
