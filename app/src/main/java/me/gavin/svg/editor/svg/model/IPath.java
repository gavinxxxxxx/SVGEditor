package me.gavin.svg.editor.svg.model;

import java.io.Serializable;

import me.gavin.svg.editor.svg.model.Drawable;

public class IPath extends Drawable implements Serializable {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "path='" + path + '\'';
    }

}