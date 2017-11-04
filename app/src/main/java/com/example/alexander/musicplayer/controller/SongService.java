package com.example.alexander.musicplayer.controller;

import android.media.MediaMetadataRetriever;

import com.example.alexander.musicplayer.model.SongsDAO;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander on 23.08.2017.
 */

public class SongService {

    SongsDAO songsDAO;

    public SongService(){
    }

    public List<String> getPaths(List<Song> songs){
        List<String> result = new ArrayList<>();
        for (Song s : songs){
            result.add(s.getPath());
        }
        return result;
    }

    public void updateSongs(Playlist playlist, List<String> paths){
        addSongsIfNotExist(playlist, paths);
        removeSongsIfNotExist(playlist, paths);
    }

    public void addSongsIfNotExist(Playlist playlist, List<String> paths){
        List<String> toAdd = new ArrayList<>();
        for(String path : paths){
            if(!isContain(playlist.getSongs(), path)){
                toAdd.add(path);
            }
        }
        for(String path : toAdd){
            playlist.getSongs().add(songsDAO.createSong(path, playlist));
        }
    }

    public void removeSongsIfNotExist(Playlist playlist, List<String> paths){
        List<Song> toRemove = new ArrayList<>();
        for(Song song : playlist.getSongs()){
            if(!paths.contains(song.getPath())){
                toRemove.add(song);
                songsDAO.deleteSongsPlaylistRelation(song);
            }
        }
        playlist.getSongs().removeAll(toRemove);
    }

    public boolean isContain(List<Song> songs, String path){
        for(Song song : songs){
            if(song.getPath().equals(path))
                return true;
        }
        return false;
    }

    public Song getSongFromListById(List<Song> songs, long id){
        if(songs!=null){
            for(Song song: songs){
                if(song.getId()==id) return song;
            }
        }
        return null;
    }

    public HashMap<String, String> extractMetaData(Song song){
        HashMap<String, String> result = new HashMap<>();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(song.getPath());
        result.put("album", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        return result;
    }

    public static String formatDuration(long durationLong){
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationLong),
                TimeUnit.MILLISECONDS.toSeconds(durationLong) -
                        TimeUnit.MILLISECONDS.toMinutes(durationLong)*60
        );
    }

    public void setSongsDAO(SongsDAO songsDAO) {
        this.songsDAO = songsDAO;
    }
}
