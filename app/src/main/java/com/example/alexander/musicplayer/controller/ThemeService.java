package com.example.alexander.musicplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.view.Theme;

/**
 * Created by Alexander on 04.11.2017.
 */

public class ThemeService {

    Activity activity;
    Theme currentTheme = null;

    public ThemeService(){
    }

    public void applyTheme(){
        if(currentTheme==null){
            currentTheme = loadCurrentTheme();
        }
        switch (currentTheme){
            case Dark: {
                activity.setTheme(R.style.AppTheme);
                break;
            }
            case Light: {
                activity.setTheme(R.style.AppThemeLight);
                break;
            }
        }
    }

    public void setCurrentTheme(Theme theme){
        SharedPreferences sharedPref =  activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Theme", theme.getName());
        editor.commit();
        currentTheme = theme;
    }

    private Theme loadCurrentTheme(){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String themeString = sharedPref.getString("Theme", "Dark");
        switch (themeString){
            case "Dark": {
                return Theme.Dark;
            }
            case "Light": {
                return Theme.Light;
            }
        }
        return null;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
