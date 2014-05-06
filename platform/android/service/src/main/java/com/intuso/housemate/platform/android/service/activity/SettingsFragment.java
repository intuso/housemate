package com.intuso.housemate.platform.android.service.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.intuso.housemate.platform.android.service.R;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/10/13
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
