package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    private final Matrix mMatrix = new Matrix();
    private float mScale = 1f;

    private SVG mSvg;

    public SVGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        SVGViewAttacher.attach(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getMode(
                widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : mSvg == null ? 0 : ((int) mSvg.getWidth()),
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : mSvg == null ? 0 : ((int) mSvg.getHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!drawable()) {
            return;
        }

        for (int i = 0; i < mSvg.getDrawables().size(); i++) {
            mSvg.getDrawables().get(i).getStrokePaint().setStrokeWidth(
                    mSvg.getDrawables().get(i).getStrokeWidth() * mScale);

            canvas.drawPath(mSvg.getPaths().get(i), mSvg.getDrawables().get(i).getFillPaint());
            canvas.drawPath(mSvg.getPaths().get(i), mSvg.getDrawables().get(i).getStrokePaint());
        }

    }

    public void set(SVG svg) {
        this.mSvg = svg;
        mScale = 1f;
        postScale(getWidth() / mSvg.getViewBox().width, 0, 0);
        invalidate();
    }

    public void postScale(float scaleFactor, float focusX, float focusY) {
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
        mScale *= scaleFactor;
        for (Path path : mSvg.getPaths()) {
            path.transform(mMatrix);
        }
        invalidate();
    }

    public void postTranslate(float dx, float dy) {
        mMatrix.reset();
        mMatrix.postTranslate(dx, dy);
        for (Path path : mSvg.getPaths()) {
            path.transform(mMatrix);
        }
        invalidate();
    }

    public boolean drawable() {
        return mSvg != null && mSvg.getDrawables() != null && !mSvg.getDrawables().isEmpty();
    }
}
