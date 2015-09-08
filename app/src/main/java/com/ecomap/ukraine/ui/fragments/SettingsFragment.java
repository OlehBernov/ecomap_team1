package com.ecomap.ukraine.ui.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ecomap.ukraine.R;

/**
 * Application settings fragment.
 */
public class SettingsFragment extends PreferenceFragment {

    /**
     * Shows available settings.
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
