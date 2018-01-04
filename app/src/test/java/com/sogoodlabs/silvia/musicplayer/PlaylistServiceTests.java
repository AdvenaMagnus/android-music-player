package com.sogoodlabs.silvia.musicplayer;

import com.sogoodlabs.silvia.musicplayer.controller.PlaylistService;
import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Alexander on 11.09.2017.
 */

public class PlaylistServiceTests {

    @Test
    public void sortByIdTest(){
        Playlist pl1 = new Playlist();
        pl1.setId(1);
        Playlist pl2 = new Playlist();
        pl2.setId(2);
        Playlist pl3 = new Playlist();
        pl3.setId(3);

        List<Playlist> listOfPlaylist = new ArrayList<>();
        listOfPlaylist.add(pl3);
        listOfPlaylist.add(pl1);
        listOfPlaylist.add(pl2);

        PlaylistService playlistService = new PlaylistService();
        playlistService.sortById(listOfPlaylist);

        assertTrue(listOfPlaylist.get(0)==pl1);
        assertTrue(listOfPlaylist.get(1)==pl2);
        assertTrue(listOfPlaylist.get(2)==pl3);

    }

}
