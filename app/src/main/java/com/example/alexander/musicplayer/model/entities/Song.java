package com.example.alexander.musicplayer.model.entities;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.example.alexander.musicplayer.controller.SongService;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander on 23.08.2017.
 */

public class Song {

    long id;
    String name;
    String path;

    HashMap<String, String> metadata;
    byte [] albumCover;

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

    public HashMap<String, String> getMetadata() {
        if(metadata==null){
            metadata = new HashMap<>();
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this.getPath());
            metadata.put("album", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            metadata.put("artist", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            metadata.put("author", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
            metadata.put("bitrate", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            long durationLong = java.lang.Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            String duration = SongService.formatDuration(durationLong);
            metadata.put("duration milli", ""+ durationLong);
            metadata.put("duration", duration);
            metadata.put("title", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        }
        return metadata;
    }

    public byte[] getAlbumCover() {
        if(albumCover==null){
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this.getPath());
            albumCover = mmr.getEmbeddedPicture();
        }
        return albumCover;
    }
}
