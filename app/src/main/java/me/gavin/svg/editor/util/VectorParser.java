package me.gavin.svg.editor.util;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.XmlRes;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gavin.svg.editor.app.model.IPath;
import me.gavin.svg.editor.app.model.Vector;

/**
 * SVG 解析器
 *
 * @author gavin.xiong 2017/8/25
 */
public class VectorParser {

    public static Vector parse(Resources resources, @XmlRes int resId) throws XmlPullParserException, IOException {
        XmlResourceParser parser = resources.getXml(resId);
        try {
            return parse(parser);
        } finally {
            parser.close();
        }
    }

    public static Vector parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        return parse(parser);
    }

    /**
     * 解析 xml todo 基本形状
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
                } else if (vector != null && "path".equals(parser.getName())) {
                    IPath path = new IPath();
                    String d = parser.getAttributeValue(null, "d");
                    path.setPath(pathFormat(d));
                    path.setFill(parser.getAttributeValue(null, "fill"));
                    if (vector.getPathList() == null) {
                        vector.setPathList(new ArrayList<>());
                    }
                    if (!"none".equals(path.getFill())) {
                        vector.getPathList().add(path);
                    }
                }
            }
            parser.next();
        }
        return vector;
    }

    /**
     * 路径格式化
     */
    public static String pathFormat(String path) {
        // 逗号全部转换成空格
        path = path.trim().replaceAll(",", " ");
        // 负参分离
        String rex = "[0-9|.]-";
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(path);
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
            L.e(matcher.group());
            functions.add(matcher.group());
        }
        return functions;
    }

}
