package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

import android.os.SystemProperties;

import java.io.File;

public class StorageSwithcer implements OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
		SystemProperties.set("persist.sys.vold.switchexternal",
                    enabled ? "1" : "0");
        return true;
    }

}
