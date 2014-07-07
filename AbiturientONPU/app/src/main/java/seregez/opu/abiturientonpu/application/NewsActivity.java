package seregez.opu.abiturientonpu.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.service.MenuHelper;

public class NewsActivity extends ActionBarActivity{

    /*
Вопросы
- Запись файлов. Нужна или нет?
Нет.
- Можно ли сделать massege в отдельном xml файле.
Нет.
- Если нет, то когда выводить уведомления?
    Из вариантов:
    1. По нажатию кнопки[т.е. по желанию юзера].
    Да.
    2. Автоматически.
    И при получании новых. Хехе. А как определять новые? ...
Парсить xml ради 1 строчки
 ИЛИ сделать еще одно активити, где выводить все сообщения. Если их много. Созранять прогресс. етс.
 */
    /*
    Как задавать мап?
    ? Если вся будет делатся через базу данных, подавать на вход выборку из базы.
     */

    //виджеты
    private ListView    resultsList;

    private static final String LOG_TAG = "=== Notification Activity ===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Map<Integer ,ArrayList<String>> map = null;


        map = new HashMap<Integer, ArrayList<String>>();

        for (int i = 0; i < ShowResult.info.size(); i++) {
            map.put(i, ShowResult.info);
        }

        resultsList = (ListView) findViewById(R.id.results);

        CustomAdaptorNotification adapter = new CustomAdaptorNotification(this, map);

        resultsList.setAdapter(adapter);

        /*resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
                //Intent intent = new Intent(getApplicationContext(), ShowFullResult.class);
                //intent.putExtra("key", position);
                //startActivity(intent);
            }
        });*/
        //makeToast("Обновление...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id                = item.getItemId();
        MenuHelper menuHelper = new MenuHelper();

        menuHelper.setContext(this);
        Intent result = menuHelper.start(id);
        if (result != null){
            startActivity(result);
        }


        return super.onOptionsItemSelected(item);
    }

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}
