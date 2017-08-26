package com.example.alexander.musicplayer.controller;

import android.media.MediaPlayer;

import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 26.08.2017.
 */

public class TrackController {

    private Playlist playlist;
    private Song currentTrack;
    private int currentTrackNumber;
    private MediaPlayer mediaPlayer;
    //private List<TrackObserver> startRunningSongObserverList = new ArrayList<>();
    private HashMap<String, TrackObserver> startRunningSongObserverList = new HashMap<>();
    private List<TrackObserver> pauseRunningSongObserverList = new ArrayList<>();
    private List<TrackObserver> resumeRunningSongObserverList = new ArrayList<>();

    public void playSong(int i){
        if(playlist.getSongs().size()>i && i>=0){
            currentTrackNumber = i;
        }
        else {
            currentTrackNumber = 0;
        }
        currentTrack = playlist.getSongs().get(currentTrackNumber);
        notifyAboutRunNewSong();
    }

    public void playNextSong(){
        playSong(++currentTrackNumber);
    }

    public void playPrevSong(){
        playSong(--currentTrackNumber);
    }

    public void pause(){
        for(TrackObserver observer : this.pauseRunningSongObserverList){
            observer.update(currentTrackNumber, currentTrack);
        }
    }

    public void resume(){
        for(TrackObserver observer : this.resumeRunningSongObserverList){
            observer.update(currentTrackNumber, currentTrack);
        }
    }

//    public void registerStartRunningSongObserver(TrackObserver trackObserver){
//        this.startRunningSongObserverList.add(trackObserver);
//    }

    public void registerStartRunningSongObserver(String name, TrackObserver trackObserver){
        this.startRunningSongObserverList.put(name, trackObserver);
    }

    public void registerPauseRunningSongObserverList(TrackObserver trackObserver){
        this.pauseRunningSongObserverList.add(trackObserver);
    }

    public void registerResumeRunningSongObserverList(TrackObserver trackObserver){
        this.resumeRunningSongObserverList.add(trackObserver);
    }

    void notifyAboutRunNewSong(){
//        for(TrackObserver observer : this.startRunningSongObserverList){
//            observer.update(currentTrackNumber, currentTrack);
//        }

        for(TrackObserver observer : this.startRunningSongObserverList.values()){
            observer.update(currentTrackNumber, currentTrack);
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
