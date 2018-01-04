package com.sogoodlabs.silvia.musicplayer.controller.callbacks;


import com.sogoodlabs.silvia.musicplayer.model.entities.Song;

/**
 * Created by Alexander on 23.09.2017.
 */

public interface TrackCallBack {
    void invoke(Song song);
}
