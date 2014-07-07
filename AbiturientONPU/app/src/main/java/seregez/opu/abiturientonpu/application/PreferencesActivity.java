package seregez.opu.abiturientonpu.application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import seregez.opu.abiturientonpu.R;
import seregez.opu.abiturientonpu.service.UpdateService;

/**
 * Created by yevgen on 24.06.14.
 */

public class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public final static String SAVED_ID_PARAMETER         = "pref_savedID";
    public final static String AUTO_UPDATE_PARAMETER      = "pref_autoUpdate";
    public final static String ONLY_WIFI_PARAMETER        = "pref_onlyWifi";
    public final static String TIMER_PARAMETER            = "pref_timerPreference";
    public final static String LAST_UPDATE_DATE_PARAMETER = "pref_lastUpdateDate";
    public final static String PREV_UPDATE_DATE_PARAMETER = "pref_prevUpdateDate";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean b = sharedPreferences.edit().putString(LAST_UPDATE_DATE_PARAMETER, "01.01.2014 00:00:00").commit();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(AUTO_UPDATE_PARAMETER) || key.equals(TIMER_PARAMETER)) {
            boolean flag    = sharedPreferences.getBoolean(AUTO_UPDATE_PARAMETER, false);
            String timer    = sharedPreferences.getString (TIMER_PARAMETER, "");
            UpdateService.setServiceAlarm(getBaseContext(), flag, Integer.valueOf(timer));
        }

    }

}
