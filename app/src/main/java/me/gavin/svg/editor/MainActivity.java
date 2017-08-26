package me.gavin.svg.editor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import me.gavin.svg.editor.model.Vector;
import me.gavin.svg.editor.util.L;
import me.gavin.svg.editor.util.VectorParser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VectorView vv = (VectorView) findViewById(R.id.vectorView);
        try {
            Vector vector = VectorParser.parse(getResources(), R.xml.ic_thumb_up_24px);
            vv.setVector(vector);

            ((TextView) findViewById(R.id.textView)).setText(vector.toString());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
