package me.gavin.svg.editor.util;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * 路径转换器
 *
 * @author gavin.xiong 2017/8/26
 */
public class PathHelper {

    /**
     * 移动画笔
     */
    public static boolean m(String fun) {
        return fun.startsWith("M") || fun.startsWith("m");
    }

    /**
     * 移动画笔
     */
    public static void m(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("path parse error for M(m): parameter error @" + f);
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

    /**
     * 线段
     */
    public static boolean l(String fun) {
        return fun.startsWith("L") || fun.startsWith("l");
    }

    /**
     * 线段
     */
    public static void l(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("path parse error for L(l): parameter error @" + f);
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

    /**
     * 水平线段
     */
    public static boolean h(String fun) {
        return fun.startsWith("H") || fun.startsWith("h");
    }

    /**
     * 水平线段
     */
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

    /**
     * 垂直线段
     */
    public static boolean v(String fun) {
        return fun.startsWith("V") || fun.startsWith("v");
    }

    /**
     * 垂直线段
     */
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

    /**
     * 二次贝塞尔曲线
     */
    public static boolean q(String fun) {
        return fun.startsWith("Q") || fun.startsWith("q");
    }

    /**
     * 二次贝塞尔曲线
     */
    public static void q(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 4 != 0) {
            throw new IllegalArgumentException("path parse error for Q(q): parameter error @" + f);
        }
        for (int i = 0; i < args.length; i += 4) {
            if (f.startsWith("Q")) {
                points[1].x = Float.parseFloat(args[i]) * scale;
                points[1].y = Float.parseFloat(args[i + 1]) * scale;
                points[0].x = Float.parseFloat(args[i + 2]) * scale;
                points[0].y = Float.parseFloat(args[i + 3]) * scale;
            } else if (f.startsWith("q")) {
                points[1].x += Float.parseFloat(args[i]) * scale;
                points[1].y += Float.parseFloat(args[i + 1]) * scale;
                points[0].x += Float.parseFloat(args[i + 2]) * scale;
                points[0].y += Float.parseFloat(args[i + 3]) * scale;
            }
            path.quadTo(points[1].x, points[1].y, points[0].x, points[0].y);
        }
    }

    /**
     * 二次贝塞尔曲线 - 缩写
     */
    public static boolean t(String fun) {
        return fun.startsWith("T") || fun.startsWith("t");
    }

    /**
     * 二次贝塞尔曲线 - 缩写
     */
    public static void t(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("path parse error for T(t): parameter error @" + f);
        }
        for (int i = 0; i < args.length; i += 2) {
            points[1].x = points[0].x + points[0].x - points[1].x;
            points[1].y = points[0].y + points[0].y - points[1].y;
            if (f.startsWith("T")) {
                points[0].x = Float.parseFloat(args[i]) * scale;
                points[0].y = Float.parseFloat(args[i + 1]) * scale;
            } else if (f.startsWith("t")) {
                points[0].x += Float.parseFloat(args[i]) * scale;
                points[0].y += Float.parseFloat(args[i + 1]) * scale;
            }
            path.quadTo(points[1].x, points[1].y, points[0].x, points[0].y);
        }
    }

    /**
     * 三次贝塞尔曲线
     */
    public static boolean c(String fun) {
        return fun.startsWith("C") || fun.startsWith("c");
    }

    /**
     * 三次贝塞尔曲线
     */
    public static void c(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 6 != 0) {
            throw new IllegalArgumentException("path parse error for C(c): parameter error @" + f);
        }
        for (int i = 0; i < args.length; i += 6) {
            float x1 = 0, y1 = 0;
            if (f.startsWith("C")) {
                x1 = Float.parseFloat(args[i]) * scale;
                y1 = Float.parseFloat(args[i + 1]) * scale;
                points[1].x = Float.parseFloat(args[i + 2]) * scale;
                points[1].y = Float.parseFloat(args[i + 3]) * scale;
                points[0].x = Float.parseFloat(args[i + 4]) * scale;
                points[0].y = Float.parseFloat(args[i + 5]) * scale;
            } else if (f.startsWith("c")) {
                x1 = points[0].x + Float.parseFloat(args[i]) * scale;
                y1 = points[0].y + Float.parseFloat(args[i + 1]) * scale;
                points[1].x = points[0].x + Float.parseFloat(args[i + 2]) * scale;
                points[1].y = points[0].y + Float.parseFloat(args[i + 3]) * scale;
                points[0].x += Float.parseFloat(args[i + 4]) * scale;
                points[0].y += Float.parseFloat(args[i + 5]) * scale;
            }
            path.cubicTo(x1, y1, points[1].x, points[1].y, points[0].x, points[0].y);
        }
    }

    /**
     * 三次贝塞尔曲线 - 缩写
     */
    public static boolean s(String fun) {
        return fun.startsWith("S") || fun.startsWith("s");
    }

    /**
     * 三次贝塞尔曲线 - 缩写
     */
    public static void s(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 4 != 0) {
            throw new IllegalArgumentException("path parse error for C(c): parameter error @" + f);
        }
        for (int i = 0; i < args.length; i += 4) {
            float x1 = points[0].x + points[0].x - points[1].x;
            float y1 = points[0].y + points[0].y - points[1].y;
            if (f.startsWith("S")) {
                points[1].x = Float.parseFloat(args[i]) * scale;
                points[1].y = Float.parseFloat(args[i + 1]) * scale;
                points[0].x = Float.parseFloat(args[i + 2]) * scale;
                points[0].y = Float.parseFloat(args[i + 3]) * scale;
            } else if (f.startsWith("s")) {
                points[1].x = points[0].x + Float.parseFloat(args[i]) * scale;
                points[1].y = points[0].y + Float.parseFloat(args[i + 1]) * scale;
                points[0].x += Float.parseFloat(args[i + 2]) * scale;
                points[0].y += Float.parseFloat(args[i + 3]) * scale;
            }
            path.cubicTo(x1, y1, points[1].x, points[1].y, points[0].x, points[0].y);
        }
    }

    /**
     * 弧形
     */
    public static boolean a(String fun) {
        return fun.startsWith("A") || fun.startsWith("a");
    }

    /**
     * 弧形
     */
    public static void a(Path path, String f, float scale, PointF[] points) {
        String[] args = f.substring(1).split(" ");
        if (args.length % 7 != 0) {
            throw new IllegalArgumentException("path parse error for A(a): parameter error @" + f);
        }
        for (int i = 0; i < args.length; i += 7) {
            if (f.startsWith("A")) {
                ArcHelper.drawArc(path,
                        points[0].x,
                        points[0].y,
                        Float.parseFloat(args[5]) * scale,
                        Float.parseFloat(args[6]) * scale,
                        Float.parseFloat(args[0]) * scale,
                        Float.parseFloat(args[1]) * scale,
                        Float.parseFloat(args[2]),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]));
                points[0].x = Float.parseFloat(args[5]) * scale;
                points[0].y = Float.parseFloat(args[6]) * scale;
            } else if (f.startsWith("a")) {
                ArcHelper.drawArc(path,
                        points[0].x,
                        points[0].y,
                        points[0].x += Float.parseFloat(args[5]) * scale,
                        points[0].x += Float.parseFloat(args[6]) * scale,
                        Float.parseFloat(args[0]) * scale,
                        Float.parseFloat(args[1]) * scale,
                        Float.parseFloat(args[2]),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]));
                points[0].x += Float.parseFloat(args[5]) * scale;
                points[0].y += Float.parseFloat(args[6]) * scale;
            }
        }
    }

    /**
     * 闭合
     */
    public static boolean z(String fun) {
        return fun.startsWith("V") || fun.startsWith("v");
    }

    /**
     * 闭合
     */
    public static void z(Path path) {
        path.close();
    }

}
