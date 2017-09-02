package com.example.alexander.musicplayer.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 28.08.2017.
 */

public class PlaylistDAO {

    SQLiteDatabase db;

    SongsDAO songsDAO;

    public PlaylistDAO(){
    }

    public List<Playlist> getAllPlayLists(){
        List<Playlist> result = new ArrayList<>();

        String[] projection = {
                PlaylistContract.PlaylistEntry._ID,
                PlaylistContract.PlaylistEntry.PLAYLIST_NAME
        };

        Cursor cursor = db.query(PlaylistContract.PlaylistEntry.TABLE_NAME,
                projection, null, null, null, null, null);

        while(cursor.moveToNext()) {
            Playlist playlist = new Playlist();
            playlist.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PlaylistContract.PlaylistEntry._ID)));
            playlist.setName(cursor.getString(cursor.getColumnIndexOrThrow(PlaylistContract.PlaylistEntry.PLAYLIST_NAME)));
            playlist.setSongs(songsDAO.getSongsInPlayList(playlist));
            result.add(playlist);
        }
        cursor.close();

        return result;
    }

    public Playlist createNew(String name){
        long id = getNextId(PlaylistContract.PlaylistEntry._ID, PlaylistContract.PlaylistEntry.TABLE_NAME);

        Playlist newPlaylist = new Playlist();
        newPlaylist.setId(id);
        newPlaylist.setName(name);

        save(newPlaylist);

        return newPlaylist;
    }

    public long getNextId(String idCol, String tableName){
        Cursor cursor = db.rawQuery("SELECT MAX(" + idCol + ") FROM " + tableName + "", null);
        long id = 1;
        while (cursor.moveToNext()) {
            if (cursor.getLong(0) >= id) id = cursor.getLong(0)+1;
        }
        return id;
    }

    public void save(Playlist playlist){
        ContentValues values = new ContentValues();
        values.put(PlaylistContract.PlaylistEntry._ID, playlist.getId());
        values.put(PlaylistContract.PlaylistEntry.PLAYLIST_NAME, playlist.getName());
        db.insert(PlaylistContract.PlaylistEntry.TABLE_NAME, null, values);
    }

    public void saveOrUpdate(Playlist playlist){
        if(isPersist(playlist)){

        } else{
            save(playlist);
        }
    }

    public boolean isPersist(Playlist playlist){
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+ PlaylistContract.PlaylistEntry.TABLE_NAME +
                " WHERE " + PlaylistContract.PlaylistEntry._ID + " = " + playlist.getId(), null);
        cursor.moveToNext();
        if(cursor.getLong(0)==0) return false;
        else return true;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public void setSongsDAO(SongsDAO songsDAO) {
        this.songsDAO = songsDAO;
    }

    public void deletePlaylist(Playlist playlist){
        if(isPersist(playlist)){
//            Cursor cursor = db.rawQuery("DELETE FROM "+ SongContract.SongEntry.TABLE_NAME +
//                    " WHERE " + SongContract.SongEntry._ID + " in (SELECT "+ PlaylistContract.PlaylistToSongsEntry.SONG_ID +
//                    " FROM "+ PlaylistContract.PlaylistToSongsEntry.TABLE_NAME +" WHERE "+ PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID +" = "+ playlist.getId() +")", null);
//
//            db.rawQuery("DELETE FROM "+ PlaylistContract.PlaylistToSongsEntry.TABLE_NAME +" WHERE "+ PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID +" = "+ playlist.getId(), null);
//            db.rawQuery("DELETE FROM "+ PlaylistContract.PlaylistEntry.TABLE_NAME +" WHERE "+ PlaylistContract.PlaylistEntry._ID +" = "+ playlist.getId(), null);

            songsDAO.deleteSongsInPlaylist(playlist);
            deleteSongsPlaylistRelation(playlist);

            int rows = db.delete(PlaylistContract.PlaylistEntry.TABLE_NAME, PlaylistContract.PlaylistEntry._ID +" = "+ playlist.getId(), null);
            System.out.println("Deleted "+rows+" playlist");

        } else throw new SQLiteException("Playlist to delete is not persist");
    }

    public void deleteSongsPlaylistRelation(Playlist playlist){
        int rows = db.delete(PlaylistContract.PlaylistToSongsEntry.TABLE_NAME, PlaylistContract.PlaylistToSongsEntry.PLAYLIST_ID +" = "+ playlist.getId(), null);
        System.out.println("Deleted "+rows+" relations songs-playlist");
    }

}
