package seregez.opu.abiturientonpu.application;

import android.os.Bundle;
import android.preference.PreferenceFragment;


import seregez.opu.abiturientonpu.R;

/**
 * Created by yevgen on 24.06.14.
 */
public class Preferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
