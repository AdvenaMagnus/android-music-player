package com.example.alexander.musicplayer.controller;

import com.example.alexander.musicplayer.model.entities.Song;

/**
 * Created by Alexander on 26.08.2017.
 */

public interface TrackObserver {

    void update(int i, Song song);

}
