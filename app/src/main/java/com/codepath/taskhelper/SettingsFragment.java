package com.codepath.taskhelper;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * SettingsFragment is used simply in combination with SettingsActivity to display the actual
 * preferences available.
 *
 * @author Rowan-James Tran
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
