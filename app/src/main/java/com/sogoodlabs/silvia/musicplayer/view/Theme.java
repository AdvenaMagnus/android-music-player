package com.sogoodlabs.silvia.musicplayer.view;

/**
 * Created by Alexander on 11.09.2017.
 */

public enum Theme {

    Dark("Dark"),
    Light("Light");

    String name;

    Theme(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
