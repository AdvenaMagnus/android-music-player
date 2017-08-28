package com.example.alexander.musicplayer.model;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 28.08.2017.
 */

public class PlaylistContract {

    public static List<String> SQL_CREATE_ENTRIES;
    static {
        SQL_CREATE_ENTRIES = new ArrayList<>();

        SQL_CREATE_ENTRIES.add("CREATE TABLE " + PlaylistEntry.TABLE_NAME + " (" +
                PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
                PlaylistEntry.PLAYLIST_NAME + " TEXT);");

        SQL_CREATE_ENTRIES.add("CREATE TABLE " + PlaylistToSongsEntry.TABLE_NAME + " (" +
                //PlaylistToSongsEntry._ID + " INTEGER PRIMARY KEY," +
                PlaylistToSongsEntry.PLAYLIST_ID + " INTEGER," +
                PlaylistToSongsEntry.SONG_ID + " INTEGER);");
    }

    public static List<String> SQL_DELETE_ENTRIES;
    static {
        SQL_DELETE_ENTRIES = new ArrayList<>();

        SQL_DELETE_ENTRIES.add("DROP TABLE IF EXISTS " + PlaylistEntry.TABLE_NAME + ";");
        SQL_DELETE_ENTRIES.add("DROP TABLE IF EXISTS " + PlaylistToSongsEntry.TABLE_NAME + ";");
    }

    public static class PlaylistEntry implements BaseColumns {
        public static final String TABLE_NAME = "playlist";
        public static final String PLAYLIST_NAME = "name";
    }

    public static class PlaylistToSongsEntry {
        public static final String TABLE_NAME = "playlist_to_songs";
        public static final String PLAYLIST_ID = "playlist_id";
        public static final String SONG_ID = "song_id";
    }

}
