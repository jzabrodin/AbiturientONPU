package seregez.opu.abiturientonpu.application;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Simple lister - extract name and email tags from a user file. Updated for SAX
 * 2.0
 */
public class SaxLister {

    private ArrayList<String> element = new ArrayList<String>();
    private ArrayList<String> value   = new ArrayList<String>();

    String  LOG_TAG                   = "SaxLister";

    public void gogo(String s){
        try {
            parse(prepareXpp(s));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void gogo(URL url){
        try {
            parse(prepareXpp(url));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XmlPullParser prepareXpp(String s) throws XmlPullParserException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        xpp.setInput(new StringReader(s));
        return xpp;
    }

    private XmlPullParser prepareXpp(URL url) throws XmlPullParserException, IOException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        URLConnection uc = url.openConnection();
        uc.connect();
        InputStream raw = uc.getInputStream();
        xpp.setInput(raw, null);
        return xpp;
    }

    private void parse(XmlPullParser xpp){
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        //Log.d(LOG_TAG, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        if (!(xpp.getName().equals("request") || xpp.getName().equals("requests")
                                || xpp.getName().equals("state") || xpp.getName().equals("abiturient")
                                || xpp.getName().equals("messages"))) {
                            element.add(xpp.getName());
                        }
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        //Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        if (!xpp.isWhitespace()){
                            //Log.d(LOG_TAG, "text = " + xpp.getText());
                            value.add(xpp.getText());
                        }
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            //Log.d(LOG_TAG, "END_DOCUMENT");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // Для получения даты обновления.
    // дата обновления хранится по отдельному адресу?
    // там просто строка лежит
    public String parseUpdate(String str) throws XmlPullParserException {
        XmlPullParser xpp = prepareXpp(str);
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        //Log.d(LOG_TAG, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        if (!(xpp.getName().equals("request") || xpp.getName().equals("requests")
                                || xpp.getName().equals("state") || xpp.getName().equals("abiturient")
                                || xpp.getName().equals("messages"))) {
                            element.add(xpp.getName());
                        }
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        //Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        if (xpp.getText() != null)
                            return (xpp.getText());
                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getElement() {
        return element;
    }

    public ArrayList<String> getValue() {
        return value;
    }
}