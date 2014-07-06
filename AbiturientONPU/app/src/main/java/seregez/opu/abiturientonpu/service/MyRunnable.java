package seregez.opu.abiturientonpu.service;
/* класс нужен для того чтобы выполнять операции в отдельном
потоке, т.к. начиная с версии 3.0 поддержка выполнения сетевых
операций запрещена в главном потоке (который отвечает за прорисовку)
как параметр передаётся строка, и далее строка преобразуется в URL и с URL
парсится вся информация, возвращает строку
* */
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MyRunnable extends AsyncTask<String, Integer, String> {

    //private static final String URL     = "http://ac.opu.ua/2014/xml/xml_vstup_onpu.php?id=%s";
    private String  URL;
    private boolean parseDate;
    private static final String LOG_TAG = " === MyRunnable ===";

    public MyRunnable(String URL, boolean parseDate) {
        this.URL       = URL;
        this.parseDate = parseDate;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            return new DataFetcher().getURL(URL,parseDate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;

    }


}
