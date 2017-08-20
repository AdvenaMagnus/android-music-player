package com.example.alexander.musicplayer;

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

import com.example.alexander.musicplayer.entities.Playlist;
import com.example.alexander.musicplayer.utils.PlaylistsUtils;

import java.util.ArrayList;

/**
 * Created by Alexander on 18.08.2017.
 */

public class PlaylistsFragment extends Fragment {

//    String[] plLists = { "Play list 1", "Play list 2", "Play list 3", "Play list 4", "Play list 5",
//            "Play list 6", "Play list 7", "Play list 8", "Play list 9", "Play list 10",
//            "Play list 11", "Play list 12", "Play list 13", "Play list 14", "Play list 15"};

    LinearLayout ll;
    ListView playLists;
    MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(ll==null) {
            mainActivity = (MainActivity) inflater.getContext();
            ll = (LinearLayout) inflater.inflate(R.layout.playlists, container, false);
            Button buttonCreateNewPlaylist = ll.findViewById(R.id.create_new_playlist);
            buttonCreateNewPlaylist.setOnClickListener(getCreateNewPlaylistListener());
            prepareListOfPlaylists(mainActivity);
        }
        ((ArrayAdapter)playLists.getAdapter()).clear();
        ((ArrayAdapter)playLists.getAdapter()).addAll(PlaylistsUtils.playlistsNames(((MainActivity)inflater.getContext()).getPlaylists()));
        return ll;
    }

    private View.OnClickListener getCreateNewPlaylistListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                Playlist playlist = new Playlist();
                playlist.setTracks(new ArrayList<String>());
                mainActivity.playlists.add(playlist);
                openDialogToCreatePlaylist(playlist);
            }
        };
    }

    private void openDialogToCreatePlaylist(final Playlist playlist){
        final CreatePlayListDialog dialog = new CreatePlayListDialog();
        dialog.setPlaylist(playlist);
        dialog.setGoToNewPlaylist(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.showPlayListContent(playlist);
            }
        });
        dialog.show(mainActivity.getFragmentManager(), "Create playlist");
    }

    private void prepareListOfPlaylists(MainActivity mainActivity){
        playLists = ll.findViewById(R.id.playlists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        playLists.setAdapter(adapter);
        playLists.setOnItemClickListener(getOnPlaylistClickListener(mainActivity));
    }

    private AdapterView.OnItemClickListener getOnPlaylistClickListener(final MainActivity mainActivity){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                mainActivity.showPlayListContent(PlaylistsUtils.getPlayListByName((String)adapter.getItemAtPosition(position), mainActivity.getPlaylists()));
            }
        };
    }


}