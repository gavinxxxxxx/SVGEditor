package me.gavin.svg.editor.svg;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.XmlRes;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import static me.gavin.svg.editor.svg.ParserHelper.getColor;
import static me.gavin.svg.editor.svg.ParserHelper.getFloat;
import static me.gavin.svg.editor.svg.ParserHelper.getFloats;
import static me.gavin.svg.editor.svg.ParserHelper.getString;
import static me.gavin.svg.editor.svg.ParserHelper.pathFormat;

/**
 * SVG 解析器
 *
 * @author gavin.xiong 2017/9/4
 */
public class SVGParser {

    public static SVG parse(Resources resources, @XmlRes int resId)
            throws Resources.NotFoundException, XmlPullParserException, IOException {
        try (XmlResourceParser parser = resources.getXml(resId)) {
            return parse(parser);
        }
    }

    public static SVG parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        return parse(parser);
    }

    private static SVG parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        SVG svg = null;
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case "svg":
                        svg = new SVG();
                        svg.setWidth(getFloat(parser, "width"));
                        svg.setHeight(getFloat(parser, "height"));
                        String viewBox = getString(parser, "viewBox");
                        if (viewBox != null) {
                            float[] fs = getFloats(viewBox);
                            svg.setViewBox(new ViewBox(fs));
                        }
                        svg.setPreserveAspectRatio(getString(parser, "preserveAspectRatio"));
                        svg.setStyle(getString(parser, "style"));
                        break;
                    case "path":
                        if (svg == null) {
                            break;
                        }
                        IPath path = new IPath();
                        path.setPath(pathFormat(getString(parser, "d")));
                        path.setStrokePaint(parseStroke(parser));
                        path.setFillPaint(parseFill(parser));

                        path.setStrokeWidth(getFloat(parser, "stroke-width"));
                        svg.getDrawables().add(path);
                        break;
                    case "defs":
                        if (!parser.isEmptyElementTag()) {
                            parser.nextTag();
                        }
                        break;
                    case "title":
                    case "desc":
                    case "linearGradient":
                    case "filter":
                        break;
                    default:
                        break;
                }
            }
            parser.next();
        }
        return svg;
    }

    private static Paint parseStroke(XmlPullParser parser) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(getColor(parser, "stroke"));
        paint.setStrokeWidth(getFloat(parser, "stroke-width"));

        String cap = getString(parser, "stroke-linecap");
        if ("round".equals(cap)) {
            paint.setStrokeCap(Paint.Cap.ROUND);
        } else if ("square".equals(cap)) {
            paint.setStrokeCap(Paint.Cap.SQUARE);
        } else if ("butt".equals(cap)) {
            paint.setStrokeCap(Paint.Cap.BUTT);
        }

        String join = getString(parser, "stroke-linejoin");
        if ("miter".equals(join)) {
            paint.setStrokeJoin(Paint.Join.MITER);
        } else if ("round".equals(join)) {
            paint.setStrokeJoin(Paint.Join.ROUND);
        } else if ("bevel".equals(join)) {
            paint.setStrokeJoin(Paint.Join.BEVEL);
        }

        String dashStyle = getString(parser, "stroke-dasharray");
        if (dashStyle != null) {
            if ("none".equalsIgnoreCase(dashStyle)) {
                paint.setPathEffect(null);
            } else {
                String offset = getString(parser, "stroke-dashoffset");

                StringTokenizer st = new StringTokenizer(dashStyle, " ,");
                int count = st.countTokens();
                float[] intervals = new float[(count & 1) == 1 ? count * 2 : count];
                float max = 0;
                float current = 1f;
                int i = 0;
                while (st.hasMoreTokens()) {
                    intervals[i++] = current = getFloat(st.nextToken(), current);
                    max += current;
                }

                // in svg speak, we double the intervals on an odd count
                for (int start = 0; i < intervals.length; i++, start++) {
                    max += intervals[i] = intervals[start];
                }

                float off = 0f;
                if (offset != null) {
                    try {
                        off = Float.parseFloat(offset) % max;
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }

                paint.setPathEffect(new DashPathEffect(intervals, off));
            }
        }

        return paint;
    }

    private static Paint parseFill(XmlPullParser parser) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(getColor(parser, "fill"));

        String fillOpacity = getString(parser, "fill-opacity");
        if (fillOpacity != null) {
            float alpha = getFloat(fillOpacity);
            paint.setAlpha((int) (255f * alpha));
        }

        return paint;
    }
}
