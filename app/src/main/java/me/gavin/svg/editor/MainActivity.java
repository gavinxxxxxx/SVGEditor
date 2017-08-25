package me.gavin.svg.editor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import me.gavin.svg.editor.model.Vector;
import me.gavin.svg.editor.util.VectorParser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VectorView vv = (VectorView) findViewById(R.id.vectorView);
        try {
            ((TextView) findViewById(R.id.textView)).setText(
                    VectorParser.parse(getResources(), R.xml.ic_thumb_up_24px).toString());

            Vector vector = VectorParser.parse(getResources(), R.xml.ic_thumb_up_24px);
            vv.setVector(vector);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
