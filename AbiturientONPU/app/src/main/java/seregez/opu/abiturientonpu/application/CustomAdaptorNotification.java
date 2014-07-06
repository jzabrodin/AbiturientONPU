package seregez.opu.abiturientonpu.application;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import seregez.opu.abiturientonpu.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Admin on 15.05.14.
 */
public class CustomAdaptorNotification extends customArrayAdapter{

    public CustomAdaptorNotification(Context context, Map<Integer, ArrayList<String>> map) {
        super(context, map);
        resource = R.layout.list_element_notification;
    }

    @Override
    protected void setTextView(View row, int a) {
        //((TextView) row.findViewById(R.id.test_body_notification)).setText("\tИнформационное сообщение 90002: По данным известных независимых рейтингов ВУЗов Украины (рейтинга «Компас», рейтинга журнала «Деньги», рейтинга ЮНЕСКО «Топ 200» и консолидированного рейтинга от блога «Учись») Одесский национальный политехнический университет входит в десятку лучших технических университетов страны и имеет наивысший рейтинг среди высших учебных заведение Одессы.");//map.get(1).get(a) + " ";
        ((TextView) row.findViewById(R.id.test_body_notification)).setText("   " + ShowResult.info.get(a));
    }
}
