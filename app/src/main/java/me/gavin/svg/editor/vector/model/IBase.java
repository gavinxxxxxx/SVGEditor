package me.gavin.svg.editor.vector.model;

import java.io.Serializable;

/**
 * SVG图形基类
 *
 * @author gavin.xiong 2017/8/28
 */
public abstract class IBase implements Serializable {

    private float strokeWidth = 0;
    private int strokeColor = 0xFF000000;
    private int fillColor = 0x00000000;

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
}
