package com.example.alexander.musicplayer.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.callbacks.CreatePlaylistCallback;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.ViewChanger;
import com.example.alexander.musicplayer.model.PlaylistDAO;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.controller.PlaylistService;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 18.08.2017.
 */

public class PlaylistsFragment extends Fragment {

    LinearLayout ll;
    ListView playListsListView;
    //MainActivity mainActivity;

    ViewChanger viewChanger;
    List<Playlist> playlists;
    PlaylistDAO playlistDAO;
    TrackController trackController;
    PlaylistService playlistService;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(ll==null) {
            Context ctx = inflater.getContext();
            ll = (LinearLayout) inflater.inflate(R.layout.playlists, container, false);
            Button buttonCreateNewPlaylist = ll.findViewById(R.id.create_new_playlist);
            buttonCreateNewPlaylist.setOnClickListener(getCreateNewPlaylistListener());
            prepareListOfPlaylists(ctx);
        }
        refreshAdapter();
        return ll;
    }

    private View.OnClickListener getCreateNewPlaylistListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                viewChanger.openDialogToCreatePlaylist(new CreatePlaylistCallback() {
                    public boolean invoke(String name) {
                        boolean result=false;
                        if(!name.isEmpty()) {
                            if (playlistService.getPlayListByName(name, playlists) == null) {
                                Playlist playlist = playlistDAO.createNew(name);
                                playlist.setSongs(new ArrayList<Song>());
                                playlists.add(playlist);
                                viewChanger.showPlayListContent(playlist);
                                return true;
                            }
                        }
                        return result;
                    }
                });
            }
        };
    }

    private void refreshAdapter(){
        ((ArrayAdapter) playListsListView.getAdapter()).clear();
        ((ArrayAdapter) playListsListView.getAdapter()).addAll(playlistService.playlistsNames(playlists));
    }

    private void prepareListOfPlaylists(Context ctx){
        playListsListView = ll.findViewById(R.id.playlists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        playListsListView.setAdapter(adapter);
        playListsListView.setOnItemClickListener(getOnPlaylistClickListener());
        playListsListView.setOnItemLongClickListener(getOnLongClickListener());
    }

    private AdapterView.OnItemClickListener getOnPlaylistClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                /** adapter.getItemAtPosition(position) returns String - name of playlist, not the playlist */
                viewChanger.showPlayListContent(playlistService.getPlayListByName((String)adapter.getItemAtPosition(position), playlists));
            }
        };
    }

    private AdapterView.OnItemLongClickListener getOnLongClickListener(){
        return new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                viewChanger.showPlaylistDetailsDialog(playlists.get(i), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(trackController.getPlaylist() == playlists.get(i)){
                            trackController.setPlaylist(null);
                        }
                        playlistDAO.deletePlaylist(playlists.get(i));
                        playlists.remove(i);
                        refreshAdapter();
                    }
                });
                return true;
            }
        };
    }

    public void setViewChanger(ViewChanger viewChanger) {
        this.viewChanger = viewChanger;
    }
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }
    public void setTrackController(TrackController trackController) {
        this.trackController = trackController;
    }
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }
}
