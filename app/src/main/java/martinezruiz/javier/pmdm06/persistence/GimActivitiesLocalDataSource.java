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
import martinezruiz.javier.pmdm06.models.GimActivity;

public class GimActivitiesLocalDataSource {

    public GimActivitiesLocalDataSource(Context ctx) {
        this.ctx = ctx;
    }

    public ArrayList<GimActivity> getGimActivities() {

        ArrayList<GimActivity> gimActivityArrayList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = ctx.getResources().openRawResource(R.raw.gim_activities);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            GimActivity gimActivity = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if ("gimActivity".equals(tagName)) {
                            gimActivity = new GimActivity();
                        } else if (gimActivity != null) {
                            if ("name".equals(tagName)) {
                                gimActivity.setName(parser.nextText());
                            } else if ("activity".equals(tagName)) {
                                gimActivity.setActivity(parser.nextText());
                            } else if ("goal".equals(tagName)) {
                                gimActivity.setGoal(parser.nextText());
                            } else if ("latitude".equals(tagName)) {
                                gimActivity.setLatitude(Double.parseDouble(parser.nextText()));
                            } else if ("longitude".equals(tagName)) {
                                gimActivity.setLongitude(Double.parseDouble(parser.nextText()));
                            }

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if("gimActivity".equals(tagName)){
                            gimActivityArrayList.add(gimActivity);
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

        return gimActivityArrayList;
    }

    private Context ctx;
}
