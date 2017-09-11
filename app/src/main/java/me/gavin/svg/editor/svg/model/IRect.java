package me.gavin.svg.editor.svg.model;

import java.io.Serializable;

/**
 * 矩形
 *
 * @author gavin.xiong 2017/8/28
 */
public class IRect extends Drawable implements Serializable {

    private float x;
    private float y;
    private float width;
    private float height;
    private float rx;
    private float ry;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }
}
