package me.gavin.svg.editor.svg;

import java.io.Serializable;

public class IPath extends Drawable implements Serializable {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    @Override
//    public String toString() {
//        return "path='" + path + '\'';
//    }


    @Override
    public String toString() {
        return "IPath{" +
                "path='" + path + '\'' +
                "} " + super.toString();
    }
}