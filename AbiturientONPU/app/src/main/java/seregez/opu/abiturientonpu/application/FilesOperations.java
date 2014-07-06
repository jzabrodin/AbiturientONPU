package seregez.opu.abiturientonpu.application;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FilesOperations {
    public FilesOperations() {
    }

    final static String LOG_TAG  = "FILES OPERATIONS";
    final static String FILENAME = "data";

    public String readFile(Context context) {
        String r = "";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));

            String str;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                r += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    static void writeFile(String data, Context context) {

        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            context.openFileOutput(FILENAME, Context.MODE_PRIVATE))
            );
            // пишем данные
            bw.write(data);
            // закрываем поток
            bw.close();

            Log.d(LOG_TAG, "Файл записан");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}