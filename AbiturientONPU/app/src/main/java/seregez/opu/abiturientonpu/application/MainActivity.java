package seregez.opu.abiturientonpu.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.service.DatabaseHelper;
import seregez.opu.abiturientonpu.service.MenuHelper;
import seregez.opu.abiturientonpu.service.MyRunnable;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    //настройки
    SharedPreferences sharedPreferences;
    private boolean offlineMode;
    //подключение к сети
    DatabaseHelper dbHelper;

    //константы
    private final String LOG_TAG                 = " === Settings Activity === ";
    public static  final String UPDATE_DATE_URL  = "http://ac.opu.ua/2014/xml/xml_vstup_onpu.php?mode=update";

/*todo
     * 5. Пофиксить кнопку обновления.
     * 6. Проверка на доступ к интернету.
     * 7. "заяв." -> "заявления"
 */
    /**see com.seregez.xmlparser.app.service.UpdateDate#doInBackground(String...)*/

    static String s = getSuperString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences     = PreferenceManager.getDefaultSharedPreferences(this);

        String savedId        = sharedPreferences.getString(PreferencesActivity.SAVED_ID_PARAMETER        ,"");
        String lastUpdateDate = sharedPreferences.getString(PreferencesActivity.LAST_UPDATE_DATE_PARAMETER,"");

        if (savedId.length() == 0){
            Intent settings = new Intent(this,PreferencesActivity.class);
            settings.putExtra("first_launch", true);
            startActivity(settings);
            /*todo придумасть способ обновлять макет после возврата из настроек */
        }

        dbHelper         = new DatabaseHelper(this);

        TextView savedIdTV = (TextView) findViewById(R.id.pref_savedID);
        TextView lastUpdateDateTV = (TextView) findViewById(R.id.pref_lastUpdateDate);
        TextView pref_autoUpdate = (TextView) findViewById(R.id.pref_autoUpdate);

        Button resultsBtn = (Button) findViewById(R.id.buttonResults);
        Button notificationBtn = (Button) findViewById(R.id.buttonNotification);


        resultsBtn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);
        savedIdTV.setText(savedId);

        dbHelper = new DatabaseHelper(this);

        lastUpdateDateTV.setText(lastUpdateDate);

        checkConnectionAndServerDate();
        boolean flag = sharedPreferences.getBoolean(PreferencesActivity.AUTO_UPDATE_PARAMETER,false);
        pref_autoUpdate.setText(flag ? " включено" : " отключено");


    }

    private void checkConnectionAndServerDate() {
        String statusMessage  =   "";

        try {
            MyRunnable myRunnable           = new MyRunnable(UPDATE_DATE_URL,true);
            statusMessage = myRunnable.execute().get() != null?"Доступны обновления!":"Нет подключения";
            offlineMode   = !statusMessage.equals("Доступны обновления!");
        } catch (InterruptedException e) {
            statusMessage = "Нет подключения!";
        } catch (ExecutionException e) {
            statusMessage = "Нет подключения!";
        }

        makeToast(statusMessage);
    }

    @Override
    public void onClick(View view) {

        boolean flag = false;
        ShowResult.updateArrays();
        checkConnectionAndServerDate();

        try {
            UpdateInformation upd = new UpdateInformation(offlineMode);
            flag                  = upd.updateData(this);//запилить норм ид.todo
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!flag){
            makeToast("Подключение отсутствует. Повторите попытку позже.");
            return;
        }
        switch (view.getId()) {
            //запускает активити
            case R.id.buttonResults:

                //ShowResult.updateXD();Эта штука не работает=(
                ShowResult.fillXD();//Если дата и ид один  и тот же.
                Intent intent = new Intent(this, ShowResult.class);
                startActivity(intent);
                break;

            case R.id.buttonNotification:
                Intent inten = new Intent(this, NewsActivity.class);
                startActivity(inten);
                break;
        }
    }


    public void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private static String getSuperString() {
        return "<?xml version='1.0' encoding='UTF-8'?><abiturient>\n" +
                "\t<person>Петров Петро Петрович</person>\n" +
                "\t<requests>\n" +
                "\t\t<request>\n" +
                "\t\t\t<department>ИБЕИТ</department>\n" +
                "\t\t\t<speciality>6.050201 Системна інженерія</speciality>\n" +
                "\t\t\t<qualification>Бакалавр</qualification>\n" +
                "\t\t\t<licence>110</licence>\n" +
                "\t\t\t<budget>50</budget>\n" +
                "\t\t\t<rating>763.5</rating>\n" +
                "\t\t\t<privilege>Вступ поза конкурсом</privilege>\n" +
                "\t\t\t<docstate>Подано оригінали</docstate>\n" +
                "\t\t\t<state>\n" +
                "\t\t\t\t<title>Рекомендовано до зарахування на бюджет</title>\n" +
                "\t\t\t\t<comment>Рекомендація діє до 11.08.2014</comment>\n" +
                "\t\t\t</state>\n" +
                "\t\t\t<place>15</place>\n" +
                "\t\t\t<originplace>2</originplace>\n" +
                "\t\t</request>\n" +
                "\t\t<request>\n" +
                "\t\t\t<department>ІКС</department>\n" +
                "\t\t\t<speciality>6.030502 Економічна кібернетика</speciality>\n" +
                "\t\t\t<qualification>Бакалавр</qualification>\n" +
                "\t\t\t<licence>802</licence>\n" +
                "\t\t\t<budget>82</budget>\n" +
                "\t\t\t<rating>700.5</rating>\n" +
                "\t\t\t<privilege>Вступ на загальних засадах</privilege>\n" +
                "\t\t\t<docstate>Подано копії</docstate>\n" +
                "\t\t\t<state>\n" +
                "\t\t\t\t<title>Рекомендовано до зарахування на контракт</title>\n" +
                "\t\t\t\t<comment>Рекомендація діє до 11.08.2014</comment>\n" +
                "\t\t\t</state>\n" +
                "\t\t\t<place>267</place>\n" +
                "\t\t\t<originplace>31</originplace>\n" +
                "\t\t</request>\n" +
                "\t\t<request>\n" +
                "\t\t\t<department>ІКС</department>\n" +
                "\t\t\t<speciality>6.030502 Економічна кібернетика</speciality>\n" +
                "\t\t\t<qualification>Бакалавр</qualification>\n" +
                "\t\t\t<licence>8</licence>\n" +
                "\t\t\t<budget>8</budget>\n" +
                "\t\t\t<rating>700.5</rating>\n" +
                "\t\t\t<privilege>Вступ на загальних засадах</privilege>\n" +
                "\t\t\t<docstate>Подано копії</docstate>\n" +
                "\t\t\t<state>\n" +
                "\t\t\t\t<title>Рекомендовано до зарахування на контракт</title>\n" +
                "\t\t\t\t<comment>Рекомендація діє до 11.08.2014</comment>\n" +
                "\t\t\t</state>\n" +
                "\t\t\t<place>267</place>\n" +
                "\t\t\t<originplace>31</originplace>\n" +
                "\t\t</request>\n" +
                "\t\t<request>\n" +
                "\t\t\t<department>ІДЗО</department>\n" +
                "\t\t\t<speciality>7.050201 Електромеханічні системи автоматизації та електропривод</speciality>\n" +
                "\t\t\t<qualification>Спеціаліст</qualification>\n" +
                "\t\t\t<licence>120</licence>\n" +
                "\t\t\t<budget>20</budget>\n" +
                "\t\t\t<rating>363.5</rating>\n" +
                "\t\t\t<privilege>Вступ на загальних засадах</privilege>\n" +
                "\t\t\t<docstate>Подано довідку</docstate>\n" +
                "\t\t\t<state>\n" +
                "\t\t\t\t<title>Рекомендовано до зарахування на бюджет</title>\n" +
                "\t\t\t\t<comment>Рекомендація діє до 05.08.2014</comment>\n" +
                "\t\t\t</state>\n" +
                "\t\t\t<place>25</place>\n" +
                "\t\t\t<originplace>12</originplace>\n" +
                "\t\t</request>\n" +
                "\t</requests>\n" +
                "\t<message>Информационное сообщение 90002: По данным известных независимых рейтингов ВУЗов Украины (рейтинга «Компас», рейтинга журнала «Деньги», рейтинга ЮНЕСКО «Топ 200» и консолидированного рейтинга от блога «Учись») Одесский национальный политехнический университет входит в десятку лучших технических университетов страны и имеет наивысший рейтинг среди высших учебных заведение Одессы.</message>\n" +
                "</abiturient>";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id                        = item.getItemId();
        MenuHelper menuHelper         =   new MenuHelper();
        menuHelper.setContext(this);
        Intent result = menuHelper.start(id);
        if (result != null){
            startActivity(result);
        }
        return super.onOptionsItemSelected(item);
    }
}//end of RatingActivity
