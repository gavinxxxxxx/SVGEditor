package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;

/**
 * SVGViewerView
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGViewerView extends View {

    public SVG mSvg;
    private boolean newTag;

    public SVGViewerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
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

        for (int i = 0; i < mSvg.paths.size(); i++) {
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getFillPaint());
            canvas.drawPath(mSvg.paths.get(i), mSvg.drawables.get(i).getStrokePaint());
        }
    }

    private void drawBefore() {
        if (newTag) {
            newTag = false;
            mSvg.matrix.reset();
            float scale = Math.min(getWidth() / mSvg.width, getHeight() / mSvg.height);
            mSvg.matrix.postTranslate((getWidth() - mSvg.width) / 2, (getHeight() - mSvg.height) / 2);
            mSvg.matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            mSvg.matrix.postScale(mSvg.width / mSvg.viewBox.width, mSvg.height / mSvg.viewBox.height);
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

}
