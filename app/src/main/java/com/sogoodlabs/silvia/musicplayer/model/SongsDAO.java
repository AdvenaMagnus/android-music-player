package com.sogoodlabs.silvia.musicplayer.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;
import com.sogoodlabs.silvia.musicplayer.model.entities.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 23.08.2017.
 */

public class SongsDAO {

    SQLiteDatabase db;

    PlaylistDAO playlistDAO;

    public SongsDAO(){
    }

    public Song createSong(String path, Playlist playlist){
        if(playlistDAO.isPersist(playlist)) {
            File file = new File(path);
            if (file.exists()) {
                long id = playlistDAO.getNextId(SongContract.SongEntry._ID, SongContract.SongEntry.TABLE_NAME);

                Song song = new Song(file.getName(), file.getAbsolutePath());
                song.setId(id);
                save(song);
                attachToPlaylist(song, playlist);
                return song;
            }
        } else throw new SQLiteException("Playlist is not persist");
        return null;
    }

    public void save(Song song){
        ContentValues values = new ContentValues();
        values.put(SongContract.SongEntry._ID, song.getId());
        values.put(SongContract.SongEntry.SONG_NAME, song.getName());
        values.put(SongContract.SongEntry.SONG_PATH, song.getPath());
        db.insert(SongContract.SongEntry.TABLE_NAME, null, values);
    }

    public void attachToPlaylist(Song song, Playlist playlist){
        ContentValues values = new ContentValues();
        values.put(PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID, playlist.getId());
        values.put(PlaylistContract.PlaylistToSongsEntry.SONG_ID, song.getId());
        db.insert(PlaylistContract.PlaylistToSongsEntry.TABLE_NAME, null, values);

    }

    public List<Song> getSongsInPlayList(Playlist playlist){
        List<Song> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+ SongContract.SongEntry.TABLE_NAME +
                " where "+ SongContract.SongEntry._ID +
                " in (select " +PlaylistContract.PlaylistToSongsEntry.SONG_ID +" from "+PlaylistContract.PlaylistToSongsEntry.TABLE_NAME+
                " where " + PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID + " = "+ playlist.getId() +")", null);

        while (cursor.moveToNext()){
            Song song =  new Song();
            song.setId(cursor.getLong(cursor.getColumnIndexOrThrow(SongContract.SongEntry._ID)));
            song.setName(cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.SONG_NAME)));
            song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.SONG_PATH)));
            result.add(song);
        }

        return result;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public void deleteSongsInPlaylist(Playlist playlist){
        int rows = db.delete(SongContract.SongEntry.TABLE_NAME, SongContract.SongEntry._ID + " in (SELECT "+ PlaylistContract.PlaylistToSongsEntry.SONG_ID +
                " FROM "+ PlaylistContract.PlaylistToSongsEntry.TABLE_NAME +" WHERE "+ PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID +" = "+ playlist.getId() +")", null);
        System.out.println("Deleted "+rows+" songs");
    }

    public void deleteSongWithRelations(Song song){
        if(song.getId()>0){
            deleteSongsPlaylistRelation(song);
            int rows = db.delete(SongContract.SongEntry.TABLE_NAME, SongContract.SongEntry._ID +" = "+ song.getId(), null);
            System.out.println("Deleted "+rows+" songs");
        } else throw new SQLiteException("Not persistent song to delete");
    }

    public void deleteSongsPlaylistRelation(Song song){
        int rows = db.delete(PlaylistContract.PlaylistToSongsEntry.TABLE_NAME, PlaylistContract.PlaylistToSongsEntry.SONG_ID +" = "+ song.getId(), null);
        System.out.println("Deleted "+rows+" relations songs-playlist");
    }

}
