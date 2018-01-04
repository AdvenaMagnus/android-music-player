package com.sogoodlabs.silvia.musicplayer.controller.callbacks;

import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;

/**
 * Created by Alexander on 26.08.2017.
 */

public interface TrackObserver {

    void update(int i, Playlist playlist);

}
