package com.example.alexander.musicplayer.model;

import com.example.alexander.musicplayer.model.entities.Song;

import java.io.File;

/**
 * Created by Alexander on 23.08.2017.
 */

public class SongsDAO {

    public Song createSong(String path){
        File file = new File(path);
        if(file.exists()){
            Song song = new Song(file.getName(), file.getAbsolutePath());
            return song;
        }
        return null;
    }

}
