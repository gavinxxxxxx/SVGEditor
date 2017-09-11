package me.gavin.svg.editor.svg.parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.List;

import me.gavin.svg.editor.svg.model.Drawable;
import me.gavin.svg.editor.svg.model.ICircle;
import me.gavin.svg.editor.svg.model.IPath;
import me.gavin.svg.editor.svg.model.IRect;

import static me.gavin.svg.editor.svg.parser.ParserHelper.matches;

/**
 * 图形解析
 *
 * @author gavin.xiong 2017/9/8
 */
class FigureHelper {

    static void transform(Path path, Drawable drawable) {
        if (drawable instanceof IPath) {
            transform(path, ((IPath) drawable).getPath());
        } else if (drawable instanceof IRect) {
            IRect rect = (IRect) drawable;
            path.addRoundRect(rect.getX(), rect.getY(),
                    rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(),
                    rect.getRx(), rect.getRy(), Path.Direction.CCW);
        } else if (drawable instanceof ICircle) {
            ICircle circle = (ICircle) drawable;
            path.addCircle(circle.getCx(), circle.getCy(), circle.getR(), Path.Direction.CCW);
        }
    }

    private static void transform(Path path, String d) {
        PointF[] points = new PointF[]{new PointF(), new PointF()};
        List<String> functions = matches(d);
        for (String fun : functions) {
            if (PathParser.m(fun)) {
                PathParser.m(path, fun, points);
            } else if (PathParser.l(fun)) {
                PathParser.l(path, fun, points);
            } else if (PathParser.h(fun)) {
                PathParser.h(path, fun, points);
            } else if (PathParser.v(fun)) {
                PathParser.v(path, fun, points);
            } else if (PathParser.q(fun)) {
                PathParser.q(path, fun, points);
            } else if (PathParser.t(fun)) {
                PathParser.t(path, fun, points);
            } else if (PathParser.c(fun)) {
                PathParser.c(path, fun, points);
            } else if (PathParser.s(fun)) {
                PathParser.s(path, fun, points);
            } else if (PathParser.a(fun)) {
                PathParser.a(path, fun, points);
            } else if (PathParser.z(fun)) {
                PathParser.z(path);
            }
        }
    }

}
