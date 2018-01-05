package com.sogoodlabs.silvia.musicplayer;

import com.sogoodlabs.silvia.musicplayer.controller.SongService;
import com.sogoodlabs.silvia.musicplayer.model.SongsDAO;
import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;
import com.sogoodlabs.silvia.musicplayer.model.entities.Song;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Alexander on 23.08.2017.
 */

public class SongServiceTests {


    @Test
    public void updateSongsTest(){
        SongsDAO mockSongsDao = mock(SongsDAO.class);
        SongService songService = new SongService();
        songService.setSongsDAO(mockSongsDao);

        String newTrack = "path1";
        Song newSong = new Song("song1", newTrack);

        Playlist playlist = new Playlist();
        playlist.setName("playlist 1");

        doReturn(newSong).when(mockSongsDao).createSong(newTrack, playlist);

        List<String> paths =  new ArrayList<>();
        paths.add(newTrack);
        paths.add("path2");

        List<Song> songs = new ArrayList<>();
        songs.add(new Song("song2", "path2"));
        songs.add(new Song("song3", "path3"));
        playlist.setSongs(songs);

        songService.updateSongs(playlist, paths);

        assertTrue(songs.size()==2);
        assertTrue(songs.get(1)==newSong);
    }



}
