package com.example.alexander.musicplayer.controller;

import com.example.alexander.musicplayer.controller.callbacks.TrackObserver;
import com.example.alexander.musicplayer.model.PlaylistDAO;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.WeakHashMap;

/**
 * Created by Alexander on 26.08.2017.
 */

public class TrackController {

    private PlaylistDAO playlistDAO;
    private CurrentPlaylistStorageService currentPlaylistStorageService;

    private Playlist playlist;
    private int currentTrackNumber;
    private boolean isNowPlaying = false;
    private WeakHashMap<String, TrackObserver> startRunningSongObserverList = new WeakHashMap<>();
    private WeakHashMap<String, TrackObserver> pauseRunningSongObserverList = new WeakHashMap<>();
    private WeakHashMap<String, TrackObserver> resumeRunningSongObserverList = new WeakHashMap<>();
    private WeakHashMap<String, TrackObserver> changePlaylistObserverList = new WeakHashMap<>();

    public TrackController(Playlist playlist){
        if(playlist!=null) {
            this.playlist = playlist;
            if (playlist.getCurrentTrack() != null)
                currentTrackNumber = playlist.getSongs().indexOf(playlist.getCurrentTrack());
        }
    }

    public void playSong(int i){
        if(playlist!=null) {
            if (playlist.getSongs().size() > i && i >= 0) {
                currentTrackNumber = i;
            } else {
                currentTrackNumber = 0;
            }
            if(playlist.getCurrentTrack()!=playlist.getSongs().get(currentTrackNumber)){
                playlist.setCurrentTrack(playlist.getSongs().get(currentTrackNumber));
                this.updateLastSongDuartion(0);
            }
            isNowPlaying = true;
            currentPlaylistStorageService.updateLastPlaylist(playlist);
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
        isNowPlaying = false;
        for(TrackObserver observer : this.pauseRunningSongObserverList.values()){
            observer.update(currentTrackNumber, playlist);
        }
    }

    public void resume(){
        isNowPlaying = true;
        for(TrackObserver observer : this.resumeRunningSongObserverList.values()){
            observer.update(currentTrackNumber, playlist);
        }
    }

    void notifyAboutRunNewSong(){
        for(TrackObserver observer : this.startRunningSongObserverList.values()){
            observer.update(currentTrackNumber, playlist);
        }
    }

    public void registerStartRunningSongObserver(String name, TrackObserver trackObserver){
        this.startRunningSongObserverList.put(name, trackObserver);
    }

    public void registerPauseRunningSongObserverList(String name, TrackObserver trackObserver){
        this.pauseRunningSongObserverList.put(name, trackObserver);
    }

    public void registerResumeRunningSongObserverList(String name, TrackObserver trackObserver){
        this.resumeRunningSongObserverList.put(name, trackObserver);
    }
    public void registerChangePlaylistObserverList(String name, TrackObserver trackObserver){
        this.changePlaylistObserverList.put(name, trackObserver);
    }

    public Playlist getPlaylist() {
        return playlist;
    }
    public void setPlaylist(Playlist playlist) {
        if(this.playlist!=playlist) {
            for (TrackObserver observer : this.changePlaylistObserverList.values()) {
                observer.update(currentTrackNumber, this.playlist);
            }
        }
        this.playlist = playlist;
    }

    public Song getCurrentTrack() {
        return playlist!=null? playlist.getCurrentTrack(): null;
    }

    public boolean isNowPlaying() {
        return isNowPlaying;
    }

    public void updateLastSongDuartion(long duration){
        playlistDAO.updateCurrentSong(playlist, playlist.getCurrentTrack(), duration);
        playlist.setCurrentTrackLastStop(duration);
    }

    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public void setCurrentPlaylistStorageService(CurrentPlaylistStorageService currentPlaylistStorageService) {
        this.currentPlaylistStorageService = currentPlaylistStorageService;
    }
}
