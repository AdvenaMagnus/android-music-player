package com.example.alexander.musicplayer;

import com.example.alexander.musicplayer.model.SongService;
import com.example.alexander.musicplayer.model.SongsDAO;
import com.example.alexander.musicplayer.model.entities.Song;

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
        SongService songService = new SongService(mockSongsDao);

        String newTrack = "path1";
        Song newSong = new Song("song1", newTrack);
        doReturn(newSong).when(mockSongsDao).createSong(newTrack);

        List<String> paths =  new ArrayList<>();
        paths.add(newTrack);
        paths.add("path2");

        List<Song> songs = new ArrayList<>();
        songs.add(new Song("song2", "path2"));
        songs.add(new Song("song3", "path3"));

        songService.updateSongs(songs, paths);

        assertTrue(songs.size()==2);
        assertTrue(songs.get(1)==newSong);
    }



}
