package com.sogoodlabs.silvia.musicplayer.model.entities;


import java.util.List;

/**
 * Created by Alexander on 18.08.2017.
 */

public class Playlist {

    long id;
    String name;
//    List<String> tracks;
    Song currentTrack;
    long currentTrackLastStop = 0;
    List<Song> songs;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

//    public List<String> getTracks() {
//        return tracks;
//    }
//    public void setTracks(List<String> tracks) {
//        this.tracks = tracks;
//    }

    public List<Song> getSongs() {
        return songs;
    }
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Song getCurrentTrack() {
        return currentTrack;
    }
    public void setCurrentTrack(Song currentTrack) {
        this.currentTrack = currentTrack;
    }

    public long getCurrentTrackLastStop() {
        return currentTrackLastStop;
    }
    public void setCurrentTrackLastStop(long currentTrackLastStop) {
        this.currentTrackLastStop = currentTrackLastStop;
    }
}
