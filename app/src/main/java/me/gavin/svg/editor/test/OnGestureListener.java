package me.gavin.svg.editor.test;

interface OnGestureListener {

    void onDrag(float dx, float dy);

    void onFling(float startX, float startY, float velocityX, float velocityY);

    void onScale(float scaleFactor, float focusX, float focusY);

    // TODO: 2017/8/30
    void onRotate(float degrees, float focusX, float focusY);

}