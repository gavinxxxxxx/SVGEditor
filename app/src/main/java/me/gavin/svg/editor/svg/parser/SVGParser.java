package me.gavin.svg.editor.svg.parser;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.XmlRes;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import me.gavin.svg.editor.svg.model.ICircle;
import me.gavin.svg.editor.svg.model.IPath;
import me.gavin.svg.editor.svg.model.IRect;
import me.gavin.svg.editor.svg.model.SVG;
import me.gavin.svg.editor.svg.model.ViewBox;

import static me.gavin.svg.editor.svg.parser.ParserHelper.getFloat;
import static me.gavin.svg.editor.svg.parser.ParserHelper.getFloats;
import static me.gavin.svg.editor.svg.parser.ParserHelper.getString;
import static me.gavin.svg.editor.svg.parser.ParserHelper.pathFormat;

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
                        IPath iPath = new IPath();
                        iPath.setFillPaint(parseFill(parser));
                        iPath.setStrokePaint(parseStroke(parser));
                        iPath.setStrokeWidth(getFloat(parser, "stroke-width"));

                        iPath.setPath(pathFormat(getString(parser, "d")));
                        svg.getDrawables().add(iPath);

                        Path path = new Path();
                        FigureHelper.transform(path, iPath);
                        svg.getPaths().add(path);
                        break;
                    case "rect":
                        if (svg == null) {
                            break;
                        }
                        IRect iRect = new IRect();
                        iRect.setFillPaint(parseFill(parser));
                        iRect.setStrokePaint(parseStroke(parser));
                        iRect.setStrokeWidth(getFloat(parser, "stroke-width"));

                        iRect.setX(getFloat(parser, "x"));
                        iRect.setY(getFloat(parser, "y"));
                        iRect.setWidth(getFloat(parser, "width"));
                        iRect.setHeight(getFloat(parser, "height"));
                        iRect.setRx(getFloat(parser, "rx"));
                        iRect.setRy(getFloat(parser, "ry"));
                        svg.getDrawables().add(iRect);
                        break;
                    case "circle":
                        if (svg == null) {
                            break;
                        }
                        ICircle iCircle = new ICircle();
                        iCircle.setFillPaint(parseFill(parser));
                        iCircle.setStrokePaint(parseStroke(parser));
                        iCircle.setStrokeWidth(getFloat(parser, "stroke-width"));

                        iCircle.setCx(getFloat(parser, "cx"));
                        iCircle.setCy(getFloat(parser, "cy"));
                        iCircle.setR(getFloat(parser, "r"));
                        svg.getDrawables().add(iCircle);
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
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

//        paint.setPathEffect(new CornerPathEffect(100));
//        paint.setPathEffect(new DashPathEffect(new float[]{50, 20}, 0));
//        paint.setPathEffect(new ComposePathEffect(new CornerPathEffect(1), new DashPathEffect(new float[]{50, 20}, 0)));

        paint.setColor(ParserHelper.getColor(parser, "stroke"));
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
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(ParserHelper.getColor(parser, "fill"));

        String fillOpacity = getString(parser, "fill-opacity");
        if (fillOpacity != null) {
            float alpha = getFloat(fillOpacity);
            paint.setAlpha((int) (255f * alpha));
        }

        return paint;
    }
}
