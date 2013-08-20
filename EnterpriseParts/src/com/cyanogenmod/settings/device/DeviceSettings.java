package com.cyanogenmod.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.SystemProperties;

import android.preference.CheckBoxPreference;
import android.preference.TwoStatePreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

public class DeviceSettings extends PreferenceActivity  {
;
    public static final String KEY_BACKLIGHTDISABLE = "backlight_disable";
    public static final String KEY_SMARTDIMMERSWITCH = "smartdimmer_switch";
	public static final String SWITCH_STORAGE_PREF = "pref_switch_storage";

    private TwoStatePreference mBacklightDisable;
    private TwoStatePreference mSmartDimmerSwitch;
	private CheckBoxPreference mSwitchStoragePref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);

        mBacklightDisable = (TwoStatePreference) findPreference(KEY_BACKLIGHTDISABLE);
        mBacklightDisable.setEnabled(BacklightDisable.isSupported());
        mBacklightDisable.setOnPreferenceChangeListener(new BacklightDisable());

        mSmartDimmerSwitch = (TwoStatePreference) findPreference(KEY_SMARTDIMMERSWITCH);
        mSmartDimmerSwitch.setEnabled(SmartDimmerSwitch.isSupported());
        mSmartDimmerSwitch.setOnPreferenceChangeListener(new SmartDimmerSwitch());

		mSwitchStoragePref = (CheckBoxPreference) findPreference(SWITCH_STORAGE_PREF);
        mSwitchStoragePref.setChecked((SystemProperties.getInt("persist.sys.vold.switchexternal", 0) == 1));

        mSwitchStoragePref.setOnPreferenceChangeListener(new StorageSwithcer());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
