package me.gavin.svg.editor.model;

import java.io.Serializable;

public class IPath implements Serializable {

    private String path;
    private String fill;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path='" + path + '\'' +
                ", fill='" + fill + '\'' +
                '}';
    }
}