package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    private SVGViewAttacher mAttacher;

    public final Matrix mPathMatrix;

    private SVG mSvg;

    private List<Path> mPaths = new ArrayList<>();

    public SVGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPathMatrix = new Matrix();
        mAttacher = SVGViewAttacher.attach(this);
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
            Path path = mPaths.get(i);
            path.reset();

            float scale = getWidth() * 1.0f / mSvg.getViewBox().width;
            PathParser.transform(path, mSvg.getDrawables().get(i), scale);

            mPaths.add(path);

            mSvg.getDrawables().get(i).getStrokePaint().setStrokeWidth(
                    mSvg.getDrawables().get(i).getStrokeWidth() * scale * mAttacher.getScale());
        }

        for (int i = 0; i < mSvg.getDrawables().size(); i++) {
            mPaths.get(i).transform(mPathMatrix);
            canvas.drawPath(mPaths.get(i), mSvg.getDrawables().get(i).getFillPaint());
            canvas.drawPath(mPaths.get(i), mSvg.getDrawables().get(i).getStrokePaint());
        }

    }

    public void set(SVG svg) {
        this.mSvg = svg;
        if (drawable()) {
            mPaths = new ArrayList<>();
            for (int i = 0; i < svg.getDrawables().size(); i++) {
                mPaths.add(new Path());
            }
        }
        postInvalidate();
    }

    public boolean drawable() {
        return mSvg != null && mSvg.getDrawables() != null && !mSvg.getDrawables().isEmpty();
    }
}
