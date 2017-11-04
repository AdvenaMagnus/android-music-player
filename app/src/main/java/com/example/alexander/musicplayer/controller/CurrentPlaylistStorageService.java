package com.example.alexander.musicplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.alexander.musicplayer.model.entities.Playlist;

import java.util.List;

/**
 * Created by Alexander on 04.11.2017.
 */

public class CurrentPlaylistStorageService {

    private final static String lastPlaylistKey = "LastPlaylist";

    Activity activity;
    PlaylistService playlistService;

    public CurrentPlaylistStorageService(){
    }

    public Playlist getLastPlayList(List<Playlist> playlists){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        long lastCurPlaylistID = sharedPref.getLong(lastPlaylistKey, 0L);
        return playlistService.getFromListByID(playlists, lastCurPlaylistID);
    }

    public void updateLastPlaylist(Playlist playlist){
        SharedPreferences sharedPref =  activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(lastPlaylistKey, playlist.getId());
        editor.commit();
    }

    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
