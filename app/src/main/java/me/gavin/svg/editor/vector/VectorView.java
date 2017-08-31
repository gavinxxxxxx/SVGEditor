package me.gavin.svg.editor.vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.gavin.svg.editor.util.VectorParser;
import me.gavin.svg.editor.vector.model.IBase;
import me.gavin.svg.editor.vector.model.Vector;

/**
 * 用来显示 svg
 *
 * @author gavin.xiong 2017/8/25
 */
public class VectorView extends View {

    public final Matrix mPathMatrix;

    private Vector mVector;

    private List<Paint> mPaints;
    private List<Path> mPaths;

    public VectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPathMatrix = new Matrix();
        VectorViewAttacher.attach(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getMode(
                widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : mVector == null ? 0 : mVector.getWidth(),
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : mVector == null ? 0 : mVector.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!drawable()) {
            return;
        }

        for (int i = 0; i < mVector.getPathList().size(); i++) {
            Path path = mPaths.get(i);
            path.reset();

            float scale = getWidth() * 1.0f / mVector.getViewportWidth();
            VectorParser.transform(path, mVector.getPathList().get(i), scale);

            mPaths.add(path);
        }

        for (int i = 0; i < mVector.getPathList().size(); i++) {
            mPaths.get(i).transform(mPathMatrix);
            canvas.drawPath(mPaths.get(i), mPaints.get(i));
        }

    }

    public void setVector(Vector vector) {
        this.mVector = vector;
        if (drawable()) {
            mPaints = new ArrayList<>();
            mPaths = new ArrayList<>();
            for (IBase ip : mVector.getPathList()) {
                Paint paint = new Paint();
                paint.setColor(ip.getFillColor());
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                mPaints.add(paint);

                mPaths.add(new Path());
            }
        }
        postInvalidate();
    }

    public boolean drawable() {
        return mVector != null && mVector.getPathList() != null && !mVector.getPathList().isEmpty();
    }
}
