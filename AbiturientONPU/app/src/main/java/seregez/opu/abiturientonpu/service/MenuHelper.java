package seregez.opu.abiturientonpu.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.application.NewsActivity;
import seregez.opu.abiturientonpu.application.PreferencesActivity;
import seregez.opu.abiturientonpu.application.UpdateInformation;

/**
 * Created by zabrodin-yevgen on 16.06.14.
 */
public class MenuHelper {

    public  final String UPDATE_DATE_URL        = "http://ac.opu.ua/2014/xml/xml_vstup_onpu.php?mode=update";

    private Context context;

    final String              ONLY_WIFI_MODE_PREFERENCE  = "pref_onlyWifi";
    final String              LAST_UPDATE_DATE_PARAM     = "pref_lastUpdateDate";
          String              lastUpdateDate;
          SharedPreferences   preferences;

    public void setContext(Context context) {
        this.context = context;
    }

    //передаем сюда айди во время обработки клика по экшн бару
    public Intent start(int id) {

        Intent settings = null;

        if ( id == R.id.action_settings ) {
            settings = new Intent(context,PreferencesActivity.class);
        } else
        if ( id == R.id.action_news){
            settings = new Intent(context, NewsActivity.class);
        } else
        if ( id == R.id.action_update){

            try {
                connectToUrl(context,true);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return settings;

    }//end of start

    private void connectToUrl(Context context,boolean isUpdate) throws ExecutionException, InterruptedException, IOException {

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean     onlyWifiMode        = preferences.getBoolean(ONLY_WIFI_MODE_PREFERENCE, false);
        lastUpdateDate                  = preferences.getString (LAST_UPDATE_DATE_PARAM, "25.06.2014 20:41:45");
        MyRunnable  myRunnable          = new MyRunnable(UPDATE_DATE_URL,true);
        String      serverDate          = myRunnable.execute().get();

        WifiManager wifiManager         = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        boolean     wifi                = wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;

        if ( wifi && onlyWifiMode && serverDate != null){
            updateDatabase(context,serverDate);

        } else  if ( !wifi  && onlyWifiMode && serverDate != null){

            showToast(context, "Используем только Wi-Fi подключение, включите Wi-Fi и повторите попытку.");

        } else if (serverDate == null){

            showToast(context,"Нет подключения к сети!");

        } else if (serverDate != null){
            updateDatabase(context,serverDate);
        }

    } // networkInfo.isConnected

    private void updateDatabase(Context context,String serverDate) throws IOException {


        /*todo это делать при запуске надо*/
        boolean flag = serverDate.equals(lastUpdateDate);
        if (flag) {
            showToast(context,"Нет обновлений :( ");
        } else {
            preferences.edit().putString(PreferencesActivity.LAST_UPDATE_DATE_PARAMETER,serverDate).commit();
            preferences.edit().putString(PreferencesActivity.PREV_UPDATE_DATE_PARAMETER,lastUpdateDate).commit();
            showToast(context,"Подключение установлено!");
            UpdateInformation upd   =   new UpdateInformation(flag);
            upd.updateData(context);
        }


    }

    private void showToast(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
