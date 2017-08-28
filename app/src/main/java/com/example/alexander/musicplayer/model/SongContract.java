package com.example.alexander.musicplayer.model;

import android.provider.BaseColumns;

/**
 * Created by Alexander on 28.08.2017.
 */

public class SongContract {

    public  static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                    SongEntry._ID + " INTEGER PRIMARY KEY," +
                    SongEntry.SONG_NAME + " TEXT," +
                    SongEntry.SONG_PATH + " TEXT);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME + ";";

    public static class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "song";
        public static final String SONG_NAME = "name";
        public static final String SONG_PATH = "path";
    }

}
