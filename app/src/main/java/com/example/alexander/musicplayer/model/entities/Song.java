package com.example.alexander.musicplayer.model.entities;

/**
 * Created by Alexander on 23.08.2017.
 */

public class Song {

    long id;
    String name;
    String path;

    public Song(){}

    public Song(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
