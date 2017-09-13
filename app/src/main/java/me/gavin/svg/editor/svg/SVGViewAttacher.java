package me.gavin.svg.editor.svg;

import android.view.MotionEvent;
import android.view.View;

/**
 * 手势操作助手
 *
 * @author gavin.xiong 2017/8/30
 */
class SVGViewAttacher implements View.OnTouchListener, OnGestureListener {

    private SVGView mView;

    private CustomGestureDetector mScaleDragDetector;

    static SVGViewAttacher attach(SVGView view) {
        return new SVGViewAttacher(view);
    }

    private SVGViewAttacher(SVGView view) {
        mView = view;
        view.setOnTouchListener(this);

        if (view.isInEditMode()) {
            return;
        }

        mScaleDragDetector = new CustomGestureDetector(view.getContext(), this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        return mView.drawable()
                && mScaleDragDetector != null
                && mScaleDragDetector.onTouchEvent(ev);
    }

    @Override
    public void onDrag(float dx, float dy) {
        mView.mSvg.matrix.postTranslate(dx, dy);
        mView.postInvalidate();
    }

    @Override
    public void onFling(float startX, float startY, float velocityX, float velocityY) {

    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {
        mView.mSvg.matrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
        mView.postInvalidate();
    }

    @Override
    public void onRotate(float degrees, float focusX, float focusY) {
        mView.mSvg.matrix.postRotate(degrees, focusX, focusY);
        mView.postInvalidate();
    }

}
