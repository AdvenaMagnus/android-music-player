package com.example.alexander.musicplayer.utils;

import com.example.alexander.musicplayer.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 20.08.2017.
 */

public class PlaylistsUtils {

    public static List<String> playlistsNames(List<Playlist> playlists){
        List<String> result =  new ArrayList<>();
        for(Playlist pl: playlists){
            result.add(pl.getName());
        }
        return result;
    }

    public static Playlist getPlayListByName(String name, List<Playlist> pls){
        for(Playlist pl : pls){
            if(pl.getName().equals(name)) return pl;
        }
        return null;
    }

}
