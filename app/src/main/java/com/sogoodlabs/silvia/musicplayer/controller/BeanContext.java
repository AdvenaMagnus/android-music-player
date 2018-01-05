package com.sogoodlabs.silvia.musicplayer.controller;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.sogoodlabs.fileschooser.configuration.FilesChooserConfiguration;
import com.sogoodlabs.silvia.musicplayer.model.PlaylistDAO;
import com.sogoodlabs.silvia.musicplayer.model.SongsDAO;
import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;
import com.sogoodlabs.silvia.musicplayer.view.FilesChooserViewImpl;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistContentFragment;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 04.11.2017.
 */

public class BeanContext {

    static TrackController trackController;

    List<Playlist> playlists = new ArrayList<>();

    private SongsDAO songsDAO = new SongsDAO();
    private PlaylistService playlistService = new PlaylistService();
    private PlaylistDAO playlistDAO = new PlaylistDAO();
    private SongService songService = new SongService();
    private CurrentPlaylistStorageService currentPlaylistStorageService = new CurrentPlaylistStorageService();
    private ViewChanger viewChanger = new ViewChanger(this);
    private ThemeService themeService = new ThemeService();
    private FilesChooserConfiguration filesChooserConfiguration = new FilesChooserConfiguration(extensionsToFilter,
            new FilesChooserViewImpl(),
            Environment.getExternalStorageDirectory().toString());

    public BeanContext(){
        songsDAO.setPlaylistDAO(playlistDAO);
        playlistDAO.setPlaylistService(playlistService);
        playlistDAO.setSongsDAO(songsDAO);
        playlistDAO.setSongService(songService);
        songService.setSongsDAO(songsDAO);
        currentPlaylistStorageService.setPlaylistService(playlistService);
    }

    public void injectDB(SQLiteDatabase db){
        songsDAO.setDb(db);
        playlistDAO.setDb(db);
    }

    public void injectInTrackController(){
        if(trackController!=null) {
            trackController.setPlaylistDAO(playlistDAO);
            trackController.setCurrentPlaylistStorageService(currentPlaylistStorageService);
        } else throw new NullPointerException("Trying to inject in not existing Track Controller");
    }

    public void injectActivity(Activity activity){
        currentPlaylistStorageService.setActivity(activity);
        viewChanger.setActivity(activity);
        themeService.setActivity(activity);
    }

    public void injectInPlaylistContentFragment(PlaylistContentFragment playlistContentFragment){
        playlistContentFragment.setTrackController(trackController);
        playlistContentFragment.setSongsDAO(songsDAO);
        playlistContentFragment.setSongService(songService);
        playlistContentFragment.setViewChanger(viewChanger);
    }

    public void injectInPlaylistsFragment(PlaylistsFragment playlistsFragment){
        playlistsFragment.setTrackController(trackController);
        playlistsFragment.setViewChanger(viewChanger);
        playlistsFragment.setPlaylists(playlists);
        playlistsFragment.setPlaylistDAO(playlistDAO);
        playlistsFragment.setPlaylistService(playlistService);
    }

    public SongsDAO getSongsDAO() {
        return songsDAO;
    }
    public PlaylistService getPlaylistService() {
        return playlistService;
    }
    public PlaylistDAO getPlaylistDAO() {
        return playlistDAO;
    }
    public SongService getSongService() {
        return songService;
    }
    public CurrentPlaylistStorageService getCurrentPlaylistStorageService() {
        return currentPlaylistStorageService;
    }
    public ViewChanger getViewChanger() {
        return viewChanger;
    }
    public ThemeService getThemeService() {
        return themeService;
    }
    public FilesChooserConfiguration getFilesChooserConfiguration() {
        return filesChooserConfiguration;
    }

    public static TrackController getTrackController() {
        return trackController;
    }
    public static void setTrackController(TrackController trackController) {
        BeanContext.trackController = trackController;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    private static HashMap<String, Boolean> extensionsToFilter = new HashMap<>(); // Permitted files
    static {
        extensionsToFilter.put("3gp", true);
        extensionsToFilter.put("mp4", true);
        extensionsToFilter.put("m4a", true);
        extensionsToFilter.put("aac", true);
        extensionsToFilter.put("ts", true);
        extensionsToFilter.put("flac", true);
        extensionsToFilter.put("mid", true);
        extensionsToFilter.put("xmf", true);
        extensionsToFilter.put("mxmf", true);
        extensionsToFilter.put("rtttl", true);
        extensionsToFilter.put("rtttl", true);
        extensionsToFilter.put("rtx", true);
        extensionsToFilter.put("ota", true);
        extensionsToFilter.put("imy", true);
        extensionsToFilter.put("mp3", true);
        extensionsToFilter.put("mkv", true);
        extensionsToFilter.put("wav", true);
        extensionsToFilter.put("ogg", true);
    }

}
