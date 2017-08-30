package me.gavin.svg.editor.vector.model;

import java.io.Serializable;

/**
 * 椭圆
 *
 * @author gavin.xiong 2017/8/28
 */
public class IEllipse extends IBase implements Serializable {

    private float cx;
    private float cy;
    private float rx;
    private float ry;

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
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
