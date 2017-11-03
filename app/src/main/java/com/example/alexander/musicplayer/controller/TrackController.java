package com.example.alexander.musicplayer.controller;

import android.media.MediaPlayer;

import com.example.alexander.musicplayer.model.PlaylistDAO;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Alexander on 26.08.2017.
 */

public class TrackController {

    PlaylistDAO playlistDAO;

    private Playlist playlist;
    private Song currentTrack;
    private int currentTrackNumber;
    private boolean isNowPlaying = false;
    //private List<TrackObserver> startRunningSongObserverList = new ArrayList<>();
    private WeakHashMap<String, TrackObserver> startRunningSongObserverList = new WeakHashMap<>();
    private WeakHashMap<String, TrackObserver> pauseRunningSongObserverList = new WeakHashMap<>();
    private WeakHashMap<String, TrackObserver> resumeRunningSongObserverList = new WeakHashMap<>();
//    private List<TrackObserver> pauseRunningSongObserverList = new ArrayList<>();
//    private List<TrackObserver> resumeRunningSongObserverList = new ArrayList<>();

    public void playSong(int i){
        if(playlist!=null) {
            if (playlist.getSongs().size() > i && i >= 0) {
                currentTrackNumber = i;
            } else {
                currentTrackNumber = 0;
            }
            currentTrack = playlist.getSongs().get(currentTrackNumber);
            isNowPlaying = true;
            playlistDAO.updateCurrentSong(playlist, playlist.getSongs().get(currentTrackNumber), 0);
            notifyAboutRunNewSong();
        }
    }

    public void playNextSong(){
        playSong(++currentTrackNumber);
    }

    public void playPrevSong(){
        playSong(--currentTrackNumber);
    }

    public void pause(){
//        for(TrackObserver observer : this.pauseRunningSongObserverList){
//            observer.update(currentTrackNumber, currentTrack);
//        }
        isNowPlaying = false;
        for(TrackObserver observer : this.pauseRunningSongObserverList.values()){
            observer.update(currentTrackNumber, currentTrack);
        }
    }

    public void resume(){
//        for(TrackObserver observer : this.resumeRunningSongObserverList){
//            observer.update(currentTrackNumber, currentTrack);
//        }
        isNowPlaying = true;
        for(TrackObserver observer : this.resumeRunningSongObserverList.values()){
            observer.update(currentTrackNumber, currentTrack);
        }
    }

//    public void registerStartRunningSongObserver(TrackObserver trackObserver){
//        this.startRunningSongObserverList.add(trackObserver);
//    }

    public void registerStartRunningSongObserver(String name, TrackObserver trackObserver){
        this.startRunningSongObserverList.put(name, trackObserver);
    }

    public void registerPauseRunningSongObserverList(String name, TrackObserver trackObserver){
        this.pauseRunningSongObserverList.put(name, trackObserver);
    }

    public void registerResumeRunningSongObserverList(String name, TrackObserver trackObserver){
        this.resumeRunningSongObserverList.put(name, trackObserver);
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

    public Song getCurrentTrack() {
        return currentTrack;
    }

    public boolean isNowPlaying() {
        return isNowPlaying;
    }

    public void updateLastSong(){

    }

    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }
}
