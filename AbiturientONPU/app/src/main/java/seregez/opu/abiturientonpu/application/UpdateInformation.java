package seregez.opu.abiturientonpu.application;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import seregez.opu.abiturientonpu.service.DatabaseHelper;
import seregez.opu.abiturientonpu.service.MyRunnable;

/**
 * Created on 03.06.14.
 * класс отвечает за обновление и считывание информации из
 * таблиц в базе данных
 */



public class UpdateInformation {

                 String lastUpdateDate;
                 String prevUpdateDate;
                 String id;
          static String URL               = "http://ac.opu.ua/2014/xml/xml_vstup_onpu.php?id=%s";
    final static String UPDATE_DATE_URL   = "http://ac.opu.ua/2014/xml/xml_vstup_onpu.php?mode=update";
    final        String LOG_TAG           = "=== update information ===";
    private      boolean offlineMode;

    DatabaseHelper dbHelper;

    final MyRunnable my;

    SharedPreferences   preferences;

    public UpdateInformation(boolean offlineMode) throws MalformedURLException {
        my                  = new MyRunnable(URL,false);
        this.offlineMode    = offlineMode;
    }

// 25-06-2014
//    public String createConnection(final String id, final Context context) throws ExecutionException, InterruptedException,MalformedURLException {
//
//        byte[] info;
//        final String[] t    = new String[1];
//        AsyncTask<String, Integer, byte[]> data = my.execute(id);
//
//        info = data.get();
//        if (info == null)
//            return null;
//
//        t[0] = new String(info);//Arrays.toString(data);
//
//        return t[0];
//
//    }


    public boolean updateData(Context context) throws IOException {

        dbHelper          = new DatabaseHelper(context);
        String result     = null;
        boolean flag      = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        preferences       = PreferenceManager.getDefaultSharedPreferences(context);
        lastUpdateDate    = preferences.getString(PreferencesActivity.LAST_UPDATE_DATE_PARAMETER, "");
        prevUpdateDate    = preferences.getString(PreferencesActivity.PREV_UPDATE_DATE_PARAMETER,"");
        id                = preferences.getString(PreferencesActivity.SAVED_ID_PARAMETER,"");

        //SaxLister sax1 = new SaxLister();
        // Тут мы парсим дату. Сравниваем. Делаем выводы.
        //выполнение идёт в главном потоке, поэтому на версиях > 3.0 это вызовет ошибку
        //почитай комменты к следующему методу
        //updateTime = sax1.parseUpdate();
        //if (updateTime.equals(Settings.getUpdateTime()))
        //    return false;
        //else
        //    Settings.setUpdateTime(updateTime);*/


        if (!offlineMode) {


            try {
                String idURL            =   String.format(URL, id);
                MyRunnable myRunnable   =   new MyRunnable(idURL,false);
                //result = createConnection(id, context);
                result                  =   myRunnable.execute().get();

                if (result == null) {

                    //result = MainActivity.s;//todo Можно попробовать тут брать сохранившиеся данные в базе, если есть.
                    //return false;
                    Toast.makeText(context, "Введен неверный номер личного дела.", Toast.LENGTH_LONG).show();
                    //имена таблиц находятся в DatabaseHelper
                    ReadValuesFromDatabase(db);
                    ReadValuesFromTableNews(context);
                    flag = false;

                } else if (result.length() > 38) {

                    SaxLister sax = new SaxLister();
                    sax.gogo(result);// offline mode On

                    /* вклиниваюсь здесь для перехвата данных и записи в бд*/
                    fillDatabase(sax, db);
                    ReadValuesFromDatabase(db);
                    ReadValuesFromTableNews(context);
                    db.close();
                    /*прекращаю*/
                    flag = true;

                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } else {

            ReadValuesFromDatabase(db);
            ReadValuesFromTableNews(context);
            flag    =   true;

        }

        return flag;

    }

    private void fillDatabase(SaxLister sax, SQLiteDatabase db) throws IOException, ExecutionException, InterruptedException {

        ArrayList<String> tags  =   sax.getElement();
        ArrayList<String> vals  =   sax.getValue();

        Log.d(LOG_TAG, dbHelper.getWritableDatabase().toString());
        commitLastUpdateDate();
        boolean flag = true;

        try {
            flag = db.rawQuery("SELECT * FROM abiturient WHERE id = ?"
                     ,new String[]{id}).getCount() == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*todo проверить, сделать чтоб каждая таблица
        * todo заполнялась по отдельности*/

        //таблица абитуриент
        ContentValues cb        = new ContentValues();

        if (flag) {
            cb.put("fio"            , vals.get(0));
            cb.put("dataObnovleniya", lastUpdateDate);
            cb.put("studentId"      , id);

            long rowid      =   db.insert("abiturient",null,cb);

            Log.d(LOG_TAG, "record inserted "+rowid);

            cb.clear();
        }

        //таблица новости
        int i = 0;
        for (String tag : tags){
            if (tag.equals("message")) {
                cb.put(tag,vals.get(i));
                cb.put("dataObnovleniya",lastUpdateDate);
                long rowId = db.insert("news",null,cb);
                cb.clear();
            }
            i++;
        }



        //таблица студент
        //vals.get(0) - ФИО студента
        i = 1;
        for (String tag : tags){

            cb.put(tag,vals.get(i));


            if (tag.equals("originplace")) {
                cb.put("dataObnovleniya",lastUpdateDate);
                cb.put("studentID",id);
                long rowId  =   db.insert("student",null,cb);
                cb.clear();
            }

            i++;

        }

    }

    private void commitLastUpdateDate() throws ExecutionException, InterruptedException {
        String serverDate       =   new MyRunnable(UPDATE_DATE_URL,true).execute().get();
        if (serverDate != null) {
            prevUpdateDate          =   lastUpdateDate;
            lastUpdateDate          =   serverDate;
            commitStringPreference(PreferencesActivity.LAST_UPDATE_DATE_PARAMETER,lastUpdateDate);
            commitStringPreference(PreferencesActivity.PREV_UPDATE_DATE_PARAMETER,prevUpdateDate);
        }
    }

    private void commitStringPreference(String name, String value) {
        preferences.edit().putString(name,value).commit();
    }

    private void ReadValuesFromDatabase(SQLiteDatabase db) {

        //это короче вроде костыля
        ArrayList<String> values   =   new ArrayList<String>();
        ArrayList<String> elements =   new ArrayList<String>();

        //переделать чтобы учитывал
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("SELECT")
                .append("\ttab1.id,\n")
                .append("\ttab1.studentID,\n")
                .append("\ttab1.dataObnovleniya,\n")
                .append("\ttab1.department,\n")
                .append("\ttab1.speciality,\n")
                .append("\ttab1.qualification,\n")
                .append("\ttab1.licence,\n")
                .append("\ttab1.budget,\n")
                .append("\ttab1.rating,\n")
                .append("\ttab1.privilege,\n")
                .append("\ttab1.docstate,\n")
                .append("\ttab1.title,\n")
                .append("\ttab1.comment,\n")
                .append("\ttab1.place,\n")
                .append("\ttab1.originplace,\n")
                .append("\ttab2.place-tab1.place AS placeDx,\n")
                .append("\ttab2.originplace - tab1.originplace AS originplaceDx\n")
                .append("\t\n")
                .append("FROM\n")
                .append("\tstudent as tab1, student as tab2\n")
                .append("WHERE")
                .append("tab1.dataObnovleniya=")
                .append("\"")
                .append(prevUpdateDate)
                .append("\" AND")
                .append("tab2.dataObnovleniya=")
                .append("\"")
                .append(lastUpdateDate)
                .append("\"")
                .append("tab1.department\t\t= tab2.department\t  AND\n")
                .append("\ttab1.speciality\t\t= tab2.speciality\t\t  AND\n")
                .append("\ttab1.qualification\t\t= tab2.qualification\t  AND")
                .append("and tab1.studentID = ")
                .append(id)
                .append("and tab1.studentID = tab2.studentID");

        String[] args                 =   null;

        Cursor c = null;
        try {
            c = db.rawQuery(queryBuilder.toString(), args);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (c != null) {
            if (c.moveToFirst()) {

                do {

                    for (int i = 0; i < c.getColumnCount(); i++) {

                        if (!(c.getColumnName(i).equals("id")
                                || c.getColumnName(i).equals("studentID")
                                || c.getColumnName(i).equals("dataObnovleniya"))) {
                            values.add(c.getString(i));
                            elements.add(c.getColumnName(i));
                        }
                    }

                } while (c.moveToNext());
            }//if
        }//if

        //это работает
        //ShowResult.getData(sax.getElement(), sax.getValue());

        //а это надо проверять
        ShowResult.getData(elements,values);//todo нада - проверено, работает.

    }

    //пока в методе нет нужды
    public static void ReadValuesFromTableNews(Context context){

        SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(context);
        String lastUpdateDate           = preferences.getString (PreferencesActivity.LAST_UPDATE_DATE_PARAMETER,"");
        String id                       = preferences.getString (PreferencesActivity.SAVED_ID_PARAMETER,"");

        //вспоогательный класс облегчающий работу с базами данных
        DatabaseHelper dbHelper       =   new DatabaseHelper(context);
        //получаем базы данных в которые можно записывать информацию (у нас одна)
        SQLiteDatabase  db            =   dbHelper.getWritableDatabase();

        ArrayList<String> values      = new ArrayList<String>();
        ArrayList<String> elements    = new ArrayList<String>();

        StringBuilder queryBuilder    = new StringBuilder();

        queryBuilder.append("SELECT * FROM news");

        String[] args                 = null;
        Cursor c                      = null;

        try {
            c = db.rawQuery(queryBuilder.toString(), args);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }


        try {
            if (c.moveToFirst()) {

                do {

                    for (int i = 0; i < c.getColumnCount(); i++) {
                        values.add(c.getString(i));
                        elements.add(c.getColumnName(i));
                    }

                } while (c.moveToNext());
            }//c.move to first
        } catch (NullPointerException e) {
            Toast.makeText(context,"Нет сохраненных записей",Toast.LENGTH_SHORT).show();
        }

    ShowResult.getData(elements,values);

    }//read values from database

}