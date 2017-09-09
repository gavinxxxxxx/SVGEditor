package me.gavin.svg.editor.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.svg.editor.svg.model.SVG;
import me.gavin.svg.editor.svg.parser.PathParser;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/8
 */
public class SVGView extends View {

    private SVGViewAttacher mAttacher;

    public final Matrix mPathMatrix;

    private SVG mSvg;

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
            mSvg.getPaths().get(i).reset();

            float scale = getWidth() / mSvg.getViewBox().width;
            PathParser.transform(mSvg.getPaths().get(i), mSvg.getDrawables().get(i), scale);

            mSvg.getDrawables().get(i).getStrokePaint().setStrokeWidth(
                    mSvg.getDrawables().get(i).getStrokeWidth() * scale * mAttacher.getScale());

            mSvg.getPaths().get(i).transform(mPathMatrix);

            canvas.drawPath(mSvg.getPaths().get(i), mSvg.getDrawables().get(i).getFillPaint());
            canvas.drawPath(mSvg.getPaths().get(i), mSvg.getDrawables().get(i).getStrokePaint());
        }

    }

    public void set(SVG svg) {
        this.mSvg = svg;
        postInvalidate();
    }

    public boolean drawable() {
        return mSvg != null && mSvg.getDrawables() != null && !mSvg.getDrawables().isEmpty();
    }
}
