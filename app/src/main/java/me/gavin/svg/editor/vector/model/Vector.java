package me.gavin.svg.editor.vector.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SVG 矢量图
 *
 * @author gavin.xiong 2017/8/25
 */
public class Vector implements Serializable {

    /**
     * 显示大小
     */
    private int width;
    private int height;

    /**
     * 视图拆分大小
     */
    private int viewportWidth;
    private int viewportHeight;

    /**
     * 路径
     */
    private List<IBase> pathList = new ArrayList<>();

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public List<IBase> getPathList() {
        return pathList;
    }

    public void setPathList(List<IBase> pathList) {
        this.pathList = pathList;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "width=" + width +
                ", height=" + height +
                ", viewportWidth=" + viewportWidth +
                ", viewportHeight=" + viewportHeight +
                ", pathList=" + pathList +
                '}';
    }
}
