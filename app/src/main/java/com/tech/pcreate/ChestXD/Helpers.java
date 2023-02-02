package com.tech.pcreate.ChestXD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Helpers {
    Context context;
    private static final String SP_NAME =  "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Helpers(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(SP_NAME, 0);
    }

    //    sharedpreference starts here
    public void put(String key, String value){
        editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value){
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key){
        return preferences.getString(key, "");
    }

    public boolean getBool(String key){
        return preferences.getBoolean(key, false);
    }

    public boolean clearStore(){
        editor = preferences.edit();
        editor.clear();
        return editor.commit();
    }


    //    sharedpreference ends here

    public void goToHome(){
        Intent gohome = new Intent(Intent.ACTION_MAIN);
        gohome.addCategory(Intent.CATEGORY_HOME);
        gohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(gohome);
    }

    public String timestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    public String timeformat(String timestamp){
        long now = System.currentTimeMillis();

        String timeStr = timestamp;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dateObj = null;
        try{
            dateObj = sdf.parse(timeStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(DateUtils.getRelativeTimeSpanString(dateObj.getTime(), now, DateUtils.MINUTE_IN_MILLIS));
    }

}
