package me.gavin.svg.editor.svg;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.List;

import me.gavin.svg.editor.util.PathHelper;
import me.gavin.svg.editor.util.VectorParser;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/8
 */
public class PathParser {

    /**
     * path 解析
     */
    public static void transform(Path path, Drawable base, float scale) {
        if (base instanceof IPath) {
            transform(path, ((IPath) base).getPath(), scale);
//        } else if (base instanceof IRect) {
//            IRect rect = (IRect) base;
//            path.addRoundRect(rect.getX() * scale, rect.getY() * scale, rect.getX() * scale + rect.getWidth() * scale, rect.getY() * scale + rect.getHeight() * scale, rect.getRx() * scale, rect.getRy() * scale, Path.Direction.CCW);
//        } else if (base instanceof ICircle) {
//            ICircle circle = (ICircle) base;
//            path.addCircle(circle.getCx() * scale, circle.getCy() * scale, circle.getR() * scale, Path.Direction.CCW);
        }
    }

    /**
     * path 解析
     */
    public static void transform(Path path, String d, float scale) {
        PointF[] points = new PointF[]{new PointF(), new PointF()};
        List<String> functions = VectorParser.matches(d);
        for (String fun : functions) {
            if (PathHelper.m(fun)) {
                PathHelper.m(path, fun, scale, points);
            } else if (PathHelper.l(fun)) {
                PathHelper.l(path, fun, scale, points);
            } else if (PathHelper.h(fun)) {
                PathHelper.h(path, fun, scale, points);
            } else if (PathHelper.v(fun)) {
                PathHelper.v(path, fun, scale, points);
            } else if (PathHelper.q(fun)) {
                PathHelper.q(path, fun, scale, points);
            } else if (PathHelper.t(fun)) {
                PathHelper.t(path, fun, scale, points);
            } else if (PathHelper.c(fun)) {
                PathHelper.c(path, fun, scale, points);
            } else if (PathHelper.s(fun)) {
                PathHelper.s(path, fun, scale, points);
            } else if (PathHelper.a(fun)) {
                PathHelper.a(path, fun, scale, points);
            } else if (PathHelper.z(fun)) {
                PathHelper.z(path);
            }
        }
    }
}
