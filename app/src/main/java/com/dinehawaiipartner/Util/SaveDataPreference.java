package com.dinehawaiipartner.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveDataPreference {
    public static final String PREFERENCES = "save_data_pref";
    public static final String SAVEID = "save_id";
    public static final String SAVEPASSWD = "save_pass";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SaveDataPreference(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setSaveIdPass(Context context, String id, String pass) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVEID, id);
        editor.putString(SAVEPASSWD, pass);
        editor.commit();
    }

    public static String getSaveid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                PREFERENCES, 0);
        return pereference.getString(SAVEID, "");
    }


    public static String getSavepass(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                PREFERENCES, 0);
        return pereference.getString(SAVEPASSWD, "");
    }

}
