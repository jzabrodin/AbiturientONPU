package seregez.opu.abiturientonpu.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zabrodin-yevgen on 26.05.14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    final public String LOG_TAG = "=== database helper ===";

    public DatabaseHelper(Context context) {
        super(context, "superBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(LOG_TAG,sqLiteDatabase.toString());
        sqLiteDatabase.execSQL("CREATE TABLE abiturient(id integer primary key autoincrement,studId int,fio text,dataObnovleniya text);");

        sqLiteDatabase.execSQL(
                "CREATE TABLE news(id integer primary key autoincrement,dataObnovleniya text,message text);"
        );



        sqLiteDatabase.execSQL("CREATE TABLE student(id integer primary key autoincrement," +
                "studentID text,dataObnovleniya text,department text," +
                "speciality text,qualification text,licence integer,budget integer,rating real,privilege text," +
                "docstate text,title text,comment text,place integer,originplace integer,FOREIGN KEY (id) REFERENCES abiturient(id));");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
