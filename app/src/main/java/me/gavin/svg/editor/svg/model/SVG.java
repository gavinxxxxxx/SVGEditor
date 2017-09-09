package me.gavin.svg.editor.svg.model;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SVG
 *
 * @author gavin.xiong 2017/9/4
 */
public class SVG implements Serializable {

    private float width;
    private float height;
    private ViewBox viewBox;
    private String preserveAspectRatio;
    private String style;
    private List<Drawable> drawables = new ArrayList<>();
    private List<Path> paths = new ArrayList<>();

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

    public ViewBox getViewBox() {
        return viewBox;
    }

    public void setViewBox(ViewBox viewBox) {
        this.viewBox = viewBox;
    }

    public String getPreserveAspectRatio() {
        return preserveAspectRatio;
    }

    public void setPreserveAspectRatio(String preserveAspectRatio) {
        this.preserveAspectRatio = preserveAspectRatio;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        return "SVG{" +
                "width=" + width +
                ", height=" + height +
                ", viewBox=" + viewBox +
                ", preserveAspectRatio='" + preserveAspectRatio + '\'' +
                ", style='" + style + '\'' +
                ", drawables=" + drawables +
                '}';
    }
}
