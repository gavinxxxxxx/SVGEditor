package me.gavin.svg.editor.vector.model;

import java.io.Serializable;

public class IPath extends IBase implements Serializable {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path='" + path + '\'' +
                '}';
    }
}