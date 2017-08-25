package me.gavin.svg.editor.util;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.XmlRes;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.gavin.svg.editor.model.IPath;
import me.gavin.svg.editor.model.Vector;

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
                    path.setPath(parser.getAttributeValue(null, "d"));
                    path.setFill(parser.getAttributeValue(null, "fill"));
                    if (vector.getPathList() == null) {
                        vector.setPathList(new ArrayList<IPath>());
                    }
                    vector.getPathList().add(path);
                }
            }
            parser.next();
        }
        return vector;
    }
}
