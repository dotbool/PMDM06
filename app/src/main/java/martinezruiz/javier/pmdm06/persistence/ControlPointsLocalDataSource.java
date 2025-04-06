package martinezruiz.javier.pmdm06.persistence;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import martinezruiz.javier.pmdm06.R;
import martinezruiz.javier.pmdm06.models.ControlPoint;

public class ControlPointsLocalDataSource {

    public ControlPointsLocalDataSource(Context ctx) {
        this.ctx = ctx;
    }

    public ArrayList<ControlPoint> getControlPoints() {

        ArrayList<ControlPoint> controlPointArrayList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = ctx.getResources().openRawResource(R.raw.control_points);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            ControlPoint controlPoint = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if ("controlPoint".equals(tagName)) {
                            controlPoint = new ControlPoint();
                        } else if (controlPoint != null) {
                            if ("name".equals(tagName)) {
                                controlPoint.setName(parser.nextText());
                            } else if ("activity".equals(tagName)) {
                                controlPoint.setActivity(parser.nextText());
                            } else if ("goal".equals(tagName)) {
                                controlPoint.setGoal(parser.nextText());
                            } else if ("latitude".equals(tagName)) {
                                controlPoint.setLatitude(Double.parseDouble(parser.nextText()));
                            } else if ("longitude".equals(tagName)) {
                                controlPoint.setLongitude(Double.parseDouble(parser.nextText()));
                            }

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if("controlPoint".equals(tagName)){
                            controlPointArrayList.add(controlPoint);
                        }
                        break;

                }
                eventType = parser.next();
            }
            inputStream.close();
        }
        catch (Resources.NotFoundException e){
            Log.d("RESOURCES EXCEPTION", Objects.requireNonNull(e.getMessage()));
        }
        catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("Acabando el list","");

        return controlPointArrayList;
    }

    private Context ctx;
}
