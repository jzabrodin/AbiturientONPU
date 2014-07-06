package seregez.opu.abiturientonpu.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yevgen on 25.06.14.
 */
public class DataFetcher {
    byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        ByteArrayOutputStream out;

        try {
            out = new ByteArrayOutputStream();
            InputStream           in  = connection.getInputStream();

            if ( connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int    bytesRead = 0;
            //наверное можно сделать поменьше размер
            byte[] buffer    = new byte[1024];

            while ( ( bytesRead = in.read( buffer ) ) > 0 ){
                out.write(buffer,0,bytesRead);
            }
            out.close();
        } finally {
            connection.disconnect();
        }
        return out.toByteArray();
    }

    public String getURL(String urlSpec, boolean parseDate) throws IOException, XmlPullParserException {

        return parseDate?parseDateXml(new String(getUrlBytes(urlSpec))):new String(getUrlBytes(urlSpec));
    }

    public String parseDateXml(String data) throws XmlPullParserException, IOException {

        String lastUpdateDate = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser        parser  = factory.newPullParser();
        parser.setInput(new StringReader(data));
        int eventType   =   parser.next();

        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.TEXT){
                lastUpdateDate   =   parser.getText();
            }
            eventType   =   parser.next();
        }


        return lastUpdateDate;
    }

}
