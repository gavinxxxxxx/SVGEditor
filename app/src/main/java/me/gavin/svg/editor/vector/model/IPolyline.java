package me.gavin.svg.editor.vector.model;

import android.graphics.Point;

import java.io.Serializable;

/**
 * 折线
 *
 * @author gavin.xiong 2017/8/28
 */
public class IPolyline extends IBase implements Serializable {

    private Point[] points;

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
}
