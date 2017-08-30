package me.gavin.svg.editor.vector;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/30
 */
public class VectorViewAttacher implements View.OnTouchListener, OnGestureListener {

    private VectorView mZoomView;

    private CustomGestureDetector mScaleDragDetector;

    private final float[] mMatrixValues = new float[9];

    private boolean mZoomable = true;

    public VectorViewAttacher(VectorView view) {
        mZoomView = view;
        view.setOnTouchListener(this);

        if (view.isInEditMode()) {
            return;
        }

        mScaleDragDetector = new CustomGestureDetector(view.getContext(), this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        return mZoomable
                && mScaleDragDetector != null
                && mScaleDragDetector.onTouchEvent(ev);
    }

    @Override
    public void onDrag(float dx, float dy) {
        mZoomView.canvasMatrix.postTranslate(dx, dy);
        mZoomView.postInvalidate();
    }

    @Override
    public void onFling(float startX, float startY, float velocityX, float velocityY) {

    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {
        mZoomView.canvasMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
        mZoomView.postInvalidate();
    }

    @Override
    public void onRotate(float degrees, float focusX, float focusY) {
        mZoomView.canvasMatrix.postRotate(degrees, focusX, focusY);
        mZoomView.postInvalidate();
    }

    public void setZoomable(boolean zoomable) {
        this.mZoomable = zoomable;
    }

    public float getScale() {
        return (float) Math.sqrt((float)
                Math.pow(getValue(mZoomView.canvasMatrix, Matrix.MSCALE_X), 2)
                + (float) Math.pow(getValue(mZoomView.canvasMatrix, Matrix.MSKEW_Y), 2));
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }
}
