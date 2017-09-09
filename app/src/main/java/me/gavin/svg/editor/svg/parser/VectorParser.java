package me.gavin.svg.editor.svg.parser;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.XmlRes;
import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gavin.svg.editor.vector.model.IBase;
import me.gavin.svg.editor.vector.model.ICircle;
import me.gavin.svg.editor.vector.model.IPath;
import me.gavin.svg.editor.vector.model.IRect;
import me.gavin.svg.editor.vector.model.Vector;

/**
 * SVG 解析器
 *
 * @author gavin.xiong 2017/8/25
 */
public class VectorParser {

    public static Vector parse(Resources resources, @XmlRes int resId) throws Resources.NotFoundException, XmlPullParserException, IOException {
        try (XmlResourceParser parser = resources.getXml(resId)) {
            try {
                return parse(parser);
            } catch (XmlPullParserException | IOException e) {
                throw e;
            }
        }
    }

    public static Vector parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        return parse(parser);
    }

    /**
     * 解析 xml
     */
    private static Vector parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        Vector vector = null;
        while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if ("svg".equals(parser.getName())) {
                    vector = new Vector();
                    vector.setWidth(Integer.parseInt(parser.getAttributeValue(null, "width")));
                    vector.setHeight(Integer.parseInt(parser.getAttributeValue(null, "height")));
                    String box = parser.getAttributeValue(null, "viewBox");
                    String[] rect = box.split(" ");
                    if (rect.length == 4) {
                        vector.setViewportWidth(Integer.parseInt(rect[2]));
                        vector.setViewportHeight(Integer.parseInt(rect[3]));
                    }
                } else if ("defs".equals(parser.getName())) {
                    if (!parser.isEmptyElementTag()) {
                        parser.nextTag();
                    }
                } else if (vector != null && "path".equals(parser.getName())) {
                    IPath path = new IPath();
                    String d = parser.getAttributeValue(null, "d");
                    path.setPath(pathFormat(d));
                    path.setFillColor(parseColor(parser.getAttributeValue(null, "fill")));
                    path.setStrokeColor(parseColor(parser.getAttributeValue(null, "stroke")));
                    String strokeWidth = parser.getAttributeValue(null, "stroke-width");
                    path.setStrokeWidth(strokeWidth == null ? 0f : Float.parseFloat(strokeWidth));
                    if (Color.alpha(path.getFillColor()) > 0) {
                        vector.getPathList().add(path);
                    }
                } else if (vector != null && "rect".equals(parser.getName())) {
                    IRect rect = new IRect();
                    rect.setX(Float.parseFloat(parser.getAttributeValue(null, "x")));
                    rect.setY(Float.parseFloat(parser.getAttributeValue(null, "y")));
                    rect.setWidth(Float.parseFloat(parser.getAttributeValue(null, "width")));
                    rect.setHeight(Float.parseFloat(parser.getAttributeValue(null, "height")));
                    String r = parser.getAttributeValue(null, "rx");
                    if (!TextUtils.isEmpty(r)) {
                        rect.setRx(Float.parseFloat(r));
                    }
                    r = parser.getAttributeValue(null, "ry");
                    if (!TextUtils.isEmpty(r)) {
                        rect.setRy(Float.parseFloat(r));
                    }
                    rect.setFillColor(parseColor(parser.getAttributeValue(null, "fill")));
                    rect.setStrokeColor(parseColor(parser.getAttributeValue(null, "stroke")));
                    String strokeWidth = parser.getAttributeValue(null, "stroke-width");
                    rect.setStrokeWidth(strokeWidth == null ? 0f : Float.parseFloat(strokeWidth));
                    if (Color.alpha(rect.getFillColor()) > 0) {
                        vector.getPathList().add(rect);
                    }
                } else if (vector != null && "circle".equals(parser.getName())) {
                    ICircle circle = new ICircle();
                    circle.setCx(Float.parseFloat(parser.getAttributeValue(null, "cx")));
                    circle.setCy(Float.parseFloat(parser.getAttributeValue(null, "cy")));
                    circle.setR(Float.parseFloat(parser.getAttributeValue(null, "r")));
                    circle.setFillColor(parseColor(parser.getAttributeValue(null, "fill")));
                    circle.setStrokeColor(parseColor(parser.getAttributeValue(null, "stroke")));
                    String strokeWidth = parser.getAttributeValue(null, "stroke-width");
                    circle.setStrokeWidth(strokeWidth == null ? 0f : Float.parseFloat(strokeWidth));
                    if (Color.alpha(circle.getFillColor()) > 0) {
                        vector.getPathList().add(circle);
                    }
                }
            }
            parser.next();
        }
        return vector;
    }

    private static int parseColor(String colorStr) {
        int result;
        if (TextUtils.isEmpty(colorStr)) {
            result = 0xFF000000;
        } else if ("none".equalsIgnoreCase(colorStr)) {
            result = 0x00000000;
        } else {
            result = Color.parseColor(colorStr);
        }
        return result;
    }

    /**
     * 路径格式化
     */
    public static String pathFormat(String path) {
        // 逗号全部转换成空格
        path = path.trim().replaceAll(",", " ");
        // 负参分离
        String rex = "[M|m|L|l|H|h|V|v|Q|q|T|t|C|c|S|s|A|a|Z|z]\\s+]";
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            path = path.replaceFirst(group, group.charAt(0) + "");
        }
        // 负参分离
        rex = "[0-9|.]-";
        pattern = Pattern.compile(rex);
        matcher = pattern.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            path = path.replaceFirst(group, group.charAt(0) + " -");
        }
        // 点参分离
        rex = "\\.[0-9]*\\.";
        pattern = Pattern.compile(rex);
        matcher = pattern.matcher(path);
        while (matcher.find()) {
            String group = matcher.group();
            path = path.replaceFirst("\\." + group.substring(1, group.length() - 1) + "\\.", group.substring(0, group.length() - 1) + " .");
            matcher.reset(path);
        }
        // 去除多余空格
        path = path.replaceAll("\\s+", " ");
        return path;
    }

    /**
     * path 解析
     */
    public static void transform(Path path, IBase base, float scale) {
        if (base instanceof IPath) {
            transform(path, ((IPath) base).getPath(), scale);
        } else if (base instanceof IRect) {
            IRect rect = (IRect) base;
            path.addRoundRect(rect.getX() * scale, rect.getY() * scale, rect.getX() * scale + rect.getWidth() * scale, rect.getY() * scale + rect.getHeight() * scale, rect.getRx() * scale, rect.getRy() * scale, Path.Direction.CCW);
        } else if (base instanceof ICircle) {
            ICircle circle = (ICircle) base;
            path.addCircle(circle.getCx() * scale, circle.getCy() * scale, circle.getR() * scale, Path.Direction.CCW);
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

    /**
     * 路径方法分离
     */
    public static List<String> matches(String path) {
        String rex = "[M|m|L|l|H|h|V|v|Q|q|T|t|C|c|S|s|A|a|Z|z][0-9|\\-.\\s]*";
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(path);
        List<String> functions = new ArrayList<>();
        while (matcher.find()) {
            functions.add(matcher.group());
        }
        return functions;
    }

}
