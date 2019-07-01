package com.example.vision.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vision.FragmentApp;
import com.example.vision.R;

public class Preference {

    private static Preference preference;
    /**
     * Preference key for userId
     */
    private final String PREFERENCE_USER_EMAIL = "USER_EMAIL";
    public static final String DASHBOARD_IS_FIRST_TIME = "DASHBOARD_IS_FIRST_TIME";
    public static final String CALL_IS_FIRST_TIME = "CALL_IS_FIRST_TIME";
    public static final String MESSAGE_IS_FIRST_TIME = "MESSAGE_IS_FIRST_TIME";
    public static final String DIARY_IS_FIRST_TIME = "DIARY_IS_FIRST_TIME";
    public static final String BATTERY_IS_FIRST_TIME = "BATERY_IS_FIRST_TIME";

    private SharedPreferences sharedPreferences;

    private Preference() {
        sharedPreferences = FragmentApp.getInstance().getSharedPreferences(FragmentApp.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    /**
     * @return the {@link SharedPreferences} object that will be used to save values in the application preference
     */
    public static Preference getInstance() {
        if (preference == null) {
            preference = new Preference();
        }
        return preference;
    }

    /**
     * Returns the userEmail from the Shared Preference file
     *
     * @return userEmail
     */
    public String getUserEmail() {
        return sharedPreferences.getString(PREFERENCE_USER_EMAIL, "");
    }

    /**
     * Stores the userEmail into Shared Preference file
     */
    public void setUserEmail(final String userEmail) {
        sharedPreferences.edit().putString(PREFERENCE_USER_EMAIL, userEmail).apply();
    }

    /**
     * Stores the {@link String} value in the preference
     *
     * @param key {@link String} parameter for the key for the values in preference
     * @param value {@link String} parameter for the value to be stored in preference
     */
    public void setData(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setData(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getData(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }



    /**
     * clearAllPreferenceData : it will clear all data from preference
     */
    public void clearAllPreferenceData() {
        sharedPreferences.edit().clear().apply();
    }


}
//Preference.getInstance().getData("KEY", "")
//        how to use
//
//        Preference.getInstance().getData(Preference.getInstance().PREFERENCE_WIFI_NAME, "")
//
