package com.dinehawaiipartner.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreference {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String MBPREFERENCES = "user_pref";
    public static final String USERID = "user_id";
    public static final String USEREMAIL = "email";
    public static final String USERCONTACT = "contact_number";
    public static final String USERNAME = "name";
    public static final String BUSINESSNAME = "business_name";
    public static final String LOCALITY = "locality";
    public static final String BUSADDRESS = "business_address";
    public static final String BUSINESSID = "business_id";
    public static final String CUSTOMER_PIC = "customerpic";
    public static final String SAVEID = "saveid";
    public static final String SAVEPASS = "savepass";
    public static final String USER_TYPE = "userType";

    private final SharedPreferences sharedPreferences;
    private final Editor editor;

    public AppPreference(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String getLatitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LATITUDE, "");
    }

    public static void setLatitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LATITUDE, id);
        editor.commit();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LONGITUDE, "");
    }

    public static void setLongitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LONGITUDE, id);
        editor.commit();
    }

    public static String getCustomerPic(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_PIC, "");
    }

    public static void setCustomerPic(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_PIC, id);
        editor.commit();
    }


    public static void setSaveIdPass(Context context, String id, String pass) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SAVEID, id);
        editor.putString(SAVEPASS, pass);
        editor.commit();
    }

    public static String getSaveid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SAVEID, "");
    }


    public static String getSavepass(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SAVEPASS, "");
    }

    public static String getUsername(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERNAME, "");
    }

    public static void setUsername(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERNAME, name);
        editor.commit();
    }

    public static String getUserid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERID, "");
    }

    public static void setUserid(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERID, name);
        editor.commit();
    }

    public static String getUseremail(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USEREMAIL, "");
    }

    public static void setUseremail(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USEREMAIL, name);
        editor.commit();
    }

    public static String getUsercontact(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERCONTACT, "");
    }

    public static void setUsercontact(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERCONTACT, name);
        editor.commit();
    }

    public static String getBusinessname(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSINESSNAME, "");
    }

    public static void setBusinessname(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSINESSNAME, name);
        editor.commit();
    }

    public static String getLocality(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LOCALITY, "");
    }

    public static void setLocality(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LOCALITY, name);
        editor.commit();
    }

    public static String getBusaddress(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSADDRESS, "");
    }

    public static void setBusaddress(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSADDRESS, name);
        editor.commit();
    }

    public static String getBusinessid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSINESSID, "");
    }

    public static void setBusinessid(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSINESSID, name);
        editor.commit();
    }

    public static String getUserType(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USER_TYPE, "");
    }

    public static void setUserType(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USER_TYPE, name);
        editor.commit();
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}
