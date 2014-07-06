package seregez.opu.abiturientonpu.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.application.MainActivity;
import seregez.opu.abiturientonpu.application.PreferencesActivity;


public class UpdateService extends IntentService {

    private static final String TAG         =   "UpdateService";

    public UpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String lastUpdateDate = getLastUpdateDate();

        String serverDate     = lastUpdateDate;

        try {
            serverDate   =   new DataFetcher().getURL(MainActivity.UPDATE_DATE_URL,true);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (DateHelper.compareDateFromSettingsWithDateFromWeb(lastUpdateDate,serverDate)) {

                PendingIntent pi    =   PendingIntent.getActivity(
                                                            this
                                                            ,0
                                                            , new Intent(this, MainActivity.class)
                                                            ,0);

                Log.i(TAG, "Received an intent :" + intent);

                Notification notification = new NotificationCompat.Builder(this)
                                                .setTicker("Доступно обновление")
                                                .setSmallIcon(R.drawable.ic_launcher)
                                                .setContentTitle("Абитуриент ОНПУ")
                                                .setContentText("Обновление, сэр!")
                                                .setContentIntent(pi)
                                                .setAutoCancel(true)
                                                .build();

                NotificationManager notificationManager = (NotificationManager)
                                                            getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getLastUpdateDate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(PreferencesActivity.LAST_UPDATE_DATE_PARAMETER,"");
    }

    public static void setServiceAlarm(Context baseContext, boolean isOn,int timer) {

        int           interval  = timer * 1000 * 60; //miliseconds * 60 seconds * minutes
        Intent        i         = new Intent(baseContext,UpdateService.class);
        PendingIntent pi        = PendingIntent.getService(baseContext,0,i,0);
        AlarmManager  am        = (AlarmManager)baseContext.getSystemService(Context.ALARM_SERVICE);

        if (isOn){
            am.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),interval,pi);
            Toast.makeText(baseContext,"Автообновление запущено",Toast.LENGTH_SHORT).show();
        } else {
            am.cancel(pi);
            pi.cancel();
            Toast.makeText(baseContext,"Автообновление остановлено",Toast.LENGTH_SHORT).show();
        }
    }

}
