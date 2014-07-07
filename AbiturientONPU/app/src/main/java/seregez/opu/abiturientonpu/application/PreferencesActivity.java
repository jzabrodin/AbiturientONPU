package seregez.opu.abiturientonpu.application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import seregez.opu.abiturientonpu.dialogs.FirstLaunchDialog;
import seregez.opu.abiturientonpu.service.UpdateService;

/**
 * Created by yevgen on 24.06.14.
 */

public class PreferencesActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public final static String SAVED_ID_PARAMETER         = "pref_savedID";
    public final static String AUTO_UPDATE_PARAMETER      = "pref_autoUpdate";
    public final static String ONLY_WIFI_PARAMETER        = "pref_onlyWifi";
    public final static String TIMER_PARAMETER            = "pref_timerPreference";
    public final static String LAST_UPDATE_DATE_PARAMETER = "pref_lastUpdateDate";
    public final static String PREV_UPDATE_DATE_PARAMETER = "pref_prevUpdateDate";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new Preferences())
                            .commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        boolean firstLaunch = false;
        try {
            firstLaunch = getIntent().getBooleanExtra("first_launch", false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (firstLaunch){
            FirstLaunchDialog firstLaunchDialog =   new FirstLaunchDialog(this);
            firstLaunchDialog.show(getSupportFragmentManager(),"");
            boolean b  = sharedPreferences.edit().putString(LAST_UPDATE_DATE_PARAMETER, "01.01.2014 00:00:00").commit();
        }


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
