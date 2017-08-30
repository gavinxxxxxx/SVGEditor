package me.gavin.svg.editor.vector.model;

import java.io.Serializable;

/**
 * 圆
 *
 * @author gavin.xiong 2017/8/28
 */
public class ICircle extends IBase implements Serializable {

    private float cx;
    private float cy;
    private float r;

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

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
