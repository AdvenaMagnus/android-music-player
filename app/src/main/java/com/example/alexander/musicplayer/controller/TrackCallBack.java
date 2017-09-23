package com.example.alexander.musicplayer.controller;


import com.example.alexander.musicplayer.model.entities.Song;

/**
 * Created by Alexander on 23.09.2017.
 */

public interface TrackCallBack {
    void invoke(Song song);
}
