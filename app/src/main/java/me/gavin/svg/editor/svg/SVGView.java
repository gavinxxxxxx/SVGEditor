package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;

import static me.gavin.svg.editor.svg.parser.Util.roundToSignificantFigures;

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

    private final Matrix mTextMatrix = new Matrix();
    private final Paint mTextPaint;

    public SVGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xffffffff);
        backgroundPaint1 = new Paint();
        backgroundPaint1.setColor(0xffcccccc);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(18f);
        mTextPaint.setColor(0xfff5f5f5);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setShadowLayer(2, 2, 2, 0xff2b2b2b);
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

        drawText(canvas);
    }

    private void drawBefore() {
        if (newTag) {
            newTag = false;
            mSvg.matrix.reset();
            mInherentWidthScale = mSvg.width / mSvg.viewBox.width;
            mInherentHeightScale = mSvg.height / mSvg.viewBox.height;
            mSvg.matrix.postScale(mInherentWidthScale, mInherentHeightScale);
            mSvg.matrix.postTranslate((getWidth() - mSvg.width) / 2f, (getHeight() - mSvg.height) / 2f);
            float size = Math.min(getWidth(), getHeight()) * 0.8f;
            float scale = Math.min(size / mSvg.width, size / mSvg.height);
            mSvg.matrix.postScale(scale, scale, getWidth() / 2f, getHeight() / 2f);
        }
    }

    private void drawBackground(Canvas canvas) {
        // canvas.drawRect(0, 0, mSvg.width / mInherentWidthScale, mSvg.height / mInherentHeightScale, backgroundPaint1);

        float backgroundGridWidth = 32f;

        mSvg.matrix.getValues(mMatrixValues);
        float scaleX = mMatrixValues[Matrix.MSCALE_X];
        float scaleY = mMatrixValues[Matrix.MSCALE_Y];
        float transX = mMatrixValues[Matrix.MTRANS_X];
        float transY = mMatrixValues[Matrix.MTRANS_Y];
        float gridW = backgroundGridWidth / scaleX;
        float gridH = backgroundGridWidth / scaleY;
        float errorCorrection = 0.01f / Math.min(scaleX, scaleY);
        float xcf = mSvg.width * scaleX / mInherentWidthScale / backgroundGridWidth;
        float ycf = mSvg.height * scaleY / mInherentHeightScale / backgroundGridWidth;
        int xci = (int) xcf;
        int yci = (int) ycf;
        float xr = xcf - xci;
        float yr = ycf - yci;

        float l, t, r, b;
        int ys = Math.max(0, (int) ((0 - transY) / scaleY / gridH));
        // int ys = Math.max(0, (int) ((0 - transY) / scaleY / gridH) + 1);
        int xs = Math.max(0, (int) ((0 - transX) / scaleX / gridW));
        int ye = Math.min(yci, (int) ((getHeight() - transY) / scaleY / gridH));
        // int ye = Math.min(yci, (int) ((getHeight() - transY) / scaleY / gridH) - 1);
        int xe = Math.min(xci, (int) ((getWidth() - transX) / scaleX / gridW));
        for (int y = ys; y <= ye; y++) {
            for (int x = xs; x <= xe; x++) {
                l = x * gridW;
                t = y * gridH;
                r = l + gridW * (x == xci ? xr : 1);
                b = t + gridH * (y == yci ? yr : 1);
                canvas.drawRect(
                        l - errorCorrection, t - errorCorrection,
                        r + errorCorrection, b + errorCorrection,
                        ((x ^ y) & 1) == 1 ? backgroundPaint1 : backgroundPaint);
            }
        }
    }

    private void drawText(Canvas canvas) {
        canvas.setMatrix(mTextMatrix);
        double scale = roundToSignificantFigures(getValue(Matrix.MSCALE_X) / mInherentWidthScale * 100, 3);
        String scaleStr = String.format("%s%%", scale);
        canvas.drawText(scaleStr.endsWith(".0%") ? scaleStr.replace(".0", "") : scaleStr, 10, getHeight() - 10, mTextPaint);
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
