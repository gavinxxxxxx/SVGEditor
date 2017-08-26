package me.gavin.svg.editor.util;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * 路径转换器
 *
 * @author gavin.xiong 2017/8/26
 */
public class PathHelper {

    public static boolean m(String fun) {
        return fun.startsWith("M") || fun.startsWith("m");
    }

    public static void m(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("??????");
        }
        for (int i = 0; i < args.length; i += 2) {
            if (f.startsWith("M")) {
                points[0].x = Float.parseFloat(args[i]) * scale;
                points[0].y = Float.parseFloat(args[i + 1]) * scale;
            } else if (f.startsWith("m")) {
                points[0].x += Float.parseFloat(args[i]) * scale;
                points[0].y += Float.parseFloat(args[i + 1]) * scale;
            }
            path.moveTo(points[0].x, points[0].y);
        }
    }

    public static boolean l(String fun) {
        return fun.startsWith("L") || fun.startsWith("l");
    }

    public static void l(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("??????");
        }
        for (int i = 0; i < args.length; i += 2) {
            if (f.startsWith("L")) {
                points[0].x = Float.parseFloat(args[i]) * scale;
                points[0].y = Float.parseFloat(args[i + 1]) * scale;
            } else if (f.startsWith("l")) {
                points[0].x += Float.parseFloat(args[i]) * scale;
                points[0].y += Float.parseFloat(args[i + 1]) * scale;
            }
            path.lineTo(points[0].x, points[0].y);
        }
    }

    public static boolean h(String fun) {
        return fun.startsWith("H") || fun.startsWith("h");
    }

    public static void h(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        for (int i = 0; i < args.length; i++) {
            if (f.startsWith("H")) {
                points[0].x = Float.parseFloat(args[i]) * scale;
            } else if (f.startsWith("h")) {
                points[0].x += Float.parseFloat(args[i]) * scale;
            }
            path.lineTo(points[0].x, points[0].y);
        }
    }

    public static boolean v(String fun) {
        return fun.startsWith("V") || fun.startsWith("v");
    }

    public static void v(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        for (int i = 0; i < args.length; i++) {
            if (f.startsWith("V")) {
                points[0].y = Float.parseFloat(args[i]) * scale;
            } else if (f.startsWith("v")) {
                points[0].y += Float.parseFloat(args[i]) * scale;
            }
            path.lineTo(points[0].x, points[0].y);
        }
    }

    public static boolean q(String fun) {
        return fun.startsWith("Q") || fun.startsWith("q");
    }

    public static void q(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (f.startsWith("Q")) {
            points[0].x = Float.parseFloat(args[2]) * scale;
            points[0].y = Float.parseFloat(args[3]) * scale;
        } else if (f.startsWith("q")) {
            points[0].x += Float.parseFloat(args[2]) * scale;
            points[0].y += Float.parseFloat(args[3]) * scale;
        }
        path.quadTo(Float.parseFloat(args[0]) * scale, Float.parseFloat(args[1]) * scale, points[0].x, points[0].y);
    }

    public static boolean t(String fun) {
        return fun.startsWith("T") || fun.startsWith("t");
    }

    public static void t(Path path, String f, float scale, PointF[] points) {
//        String[] args = f.substring(1).split(" ");
//        if (f.startsWith("T")) {
//            path.quadTo(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
//        } else if (f.startsWith("t")) {
//            path.rQuadTo(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
//        }
    }

    public static boolean c(String fun) {
        return fun.startsWith("C") || fun.startsWith("c");
    }

    public static void c(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        float x1 = 0, y1 = 0;
        if (f.startsWith("C")) {
            x1 = Float.parseFloat(args[0]) * scale;
            y1 = Float.parseFloat(args[1]) * scale;
            points[1].x = Float.parseFloat(args[2]) * scale;
            points[1].y = Float.parseFloat(args[3]) * scale;
            points[0].x = Float.parseFloat(args[4]) * scale;
            points[0].y = Float.parseFloat(args[5]) * scale;
        } else if (f.startsWith("c")) {
            x1 = points[0].x + Float.parseFloat(args[0]) * scale;
            y1 = points[0].y + Float.parseFloat(args[1]) * scale;
            points[1].x = points[0].x + Float.parseFloat(args[2]) * scale;
            points[1].y = points[0].y + Float.parseFloat(args[3]) * scale;
            points[0].x += Float.parseFloat(args[4]) * scale;
            points[0].y += Float.parseFloat(args[5]) * scale;
        }
        path.cubicTo(x1, y1, points[1].x, points[1].y, points[0].x, points[0].y);
    }

    public static boolean z(String fun) {
        return fun.startsWith("V") || fun.startsWith("v");
    }

    public static void z(Path path) {
        path.close();
    }

}
