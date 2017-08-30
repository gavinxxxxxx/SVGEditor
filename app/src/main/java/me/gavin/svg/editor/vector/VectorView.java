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

import me.gavin.svg.editor.vector.model.IBase;
import me.gavin.svg.editor.vector.model.Vector;
import me.gavin.svg.editor.util.VectorParser;

/**
 * 用来显示 svg
 *
 * @author gavin.xiong 2017/8/25
 */
public class VectorView extends View {

    private VectorViewAttacher attacher;

    public Matrix canvasMatrix = new Matrix();

    private Vector mVector;

    private List<Paint> mPaints;
    private List<Path> mPaths;

    public VectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attacher = new VectorViewAttacher(this);
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
        if (!canDraw()) {
            return;
        }

        canvas.setMatrix(canvasMatrix);

        for (int i = 0; i < mVector.getPathList().size(); i++) {
            Path path = mPaths.get(i);
            path.reset();

            float scale = getWidth() * 1.0f / mVector.getViewportWidth();
            VectorParser.transform(path, mVector.getPathList().get(i), scale);

            mPaths.add(path);
        }

//        canvas.scale(6, 6);
//        canvas.translate(-400, -400);

        for (int i = 0; i < mVector.getPathList().size(); i++) {
            canvas.drawPath(mPaths.get(i), mPaints.get(i));
        }
    }

    public void setVector(Vector vector) {
        this.mVector = vector;
        if (canDraw()) {
            mPaints = new ArrayList<>();
            mPaths = new ArrayList<>();
            for (IBase ip : mVector.getPathList()) {
                Paint paint = new Paint();
                paint.setColor(ip.getFillColor());
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                mPaints.add(paint);

                mPaths.add(new Path());
            }
        }
        postInvalidate();
    }

    private boolean canDraw() {
        return mVector != null && mVector.getPathList() != null && !mVector.getPathList().isEmpty();
    }
}
