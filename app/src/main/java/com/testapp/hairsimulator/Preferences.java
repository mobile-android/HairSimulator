package com.testapp.hairsimulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bharath.simha on 16/01/16.
 */
public class Preferences {

    private static Preferences instance = null;

    private SharedPreferences sharedPreferences;

    public static final String USERS_LOCAL_TIMESTAMP = "pref_user_local_timestamp";
    public static final String IS_FIRST_TIME = "pref_is_first_time_launch";
    public static final String BUTTON_VALUE = "pref_button_value";

    public static Preferences instance() {
        if (instance == null) {
            synchronized (Preferences.class) {
                if (instance == null)
                    instance = new Preferences();
            }
        }
        return instance;
    }

    public void initialize(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public long getLocalFirstTimeStamp() {
        return this.sharedPreferences.getLong(USERS_LOCAL_TIMESTAMP, 0);
    }

    public void setLocalFirstTimeStamp(long localFirstTimeStamp) {
        this.sharedPreferences.edit().putLong(USERS_LOCAL_TIMESTAMP, localFirstTimeStamp).apply();
    }

    public boolean isFirstTime() {
        return this.sharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        this.sharedPreferences.edit().putBoolean(IS_FIRST_TIME, isFirstTime).apply();
    }

    public String getButtonValue() {
        return this.sharedPreferences.getString(BUTTON_VALUE, null);
    }

    public void setButtonValue(String value) {
        this.sharedPreferences.edit().putString(getButtonValue(), value).apply();

    }
}
