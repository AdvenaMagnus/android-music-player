package com.example.alexander.musicplayer.controller;

import com.example.alexander.musicplayer.model.SongsDAO;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 23.08.2017.
 */

public class SongService {

    SongsDAO songsDAO;

    public SongService(SongsDAO songsDAO){
        this.songsDAO = songsDAO;
    }

    public List<String> getPaths(List<Song> songs){
        List<String> result = new ArrayList<>();
        for (Song s : songs){
            result.add(s.getPath());
        }
        return result;
    }

    public void updateSongs(List<Song> songs, List<String> paths){
        addSongsIfNotExist(songs, paths);
        removeSongsIfNotExist(songs, paths);
    }

    public void addSongsIfNotExist(List<Song> songs, List<String> paths){
        List<String> toAdd = new ArrayList<>();
        for(String path : paths){
            if(!isContain(songs, path)){
                toAdd.add(path);
            }
        }
        for(String path : toAdd){
            songs.add(songsDAO.createSong(path));
        }
    }

    public void removeSongsIfNotExist(List<Song> songs, List<String> paths){
        List<Song> toRemove = new ArrayList<>();
        for(Song song : songs){
            if(!paths.contains(song.getPath())){
                toRemove.add(song);
            }
        }
        songs.removeAll(toRemove);
    }

    public boolean isContain(List<Song> songs, String path){
        for(Song song : songs){
            if(song.getPath().equals(path))
                return true;
        }
        return false;
    }

}
