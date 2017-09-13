package com.example.alexander.musicplayer.view.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.controller.PlaylistService;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;

/**
 * Created by Alexander on 18.08.2017.
 */

public class PlaylistsFragment extends Fragment {

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
//        ((ArrayAdapter)playLists.getAdapter()).clear();
//        ((ArrayAdapter)playLists.getAdapter()).addAll(PlaylistService.playlistsNames(mainActivity.getPlaylists()));
        refreshAdapter();
        return ll;
    }

    private View.OnClickListener getCreateNewPlaylistListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                openDialogToCreatePlaylist();
            }
        };
    }

    private void refreshAdapter(){
        ((ArrayAdapter)playLists.getAdapter()).clear();
        ((ArrayAdapter)playLists.getAdapter()).addAll(PlaylistService.playlistsNames(mainActivity.getPlaylists()));
    }

    private void openDialogToCreatePlaylist(){
        final CreatePlayListDialog dialog = new CreatePlayListDialog();
        dialog.setOnOkClick(new View.OnClickListener() {
            public void onClick(View v) {
                Playlist playlist = mainActivity.playlistDAO.createNew(dialog.getPlayListName());
                playlist.setSongs(new ArrayList<Song>());
                mainActivity.playlists.add(playlist);
                mainActivity.showPlayListContent(playlist);
            }
        });
//        dialog.setOnCancelCallback(new View.OnClickListener() {
//            public void onClick(View v) {
//                //mainActivity.playlists.remove(playlist);
//            }
//        });
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.show(mainActivity.getFragmentManager(), "Create playlist");
    }

    private void prepareListOfPlaylists(MainActivity mainActivity){
        playLists = ll.findViewById(R.id.playlists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        playLists.setAdapter(adapter);
        playLists.setOnItemClickListener(getOnPlaylistClickListener(mainActivity));
        playLists.setOnItemLongClickListener(getOnLongClickListener(mainActivity));
    }

    private AdapterView.OnItemClickListener getOnPlaylistClickListener(final MainActivity mainActivity){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                mainActivity.showPlayListContent(PlaylistService.getPlayListByName((String)adapter.getItemAtPosition(position), mainActivity.getPlaylists()));
            }
        };
    }

    private AdapterView.OnItemLongClickListener getOnLongClickListener(final MainActivity mainActivity){
        return new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final PlaylistDetailDialog playlistDetailDialog = new PlaylistDetailDialog();
                playlistDetailDialog.setPlaylist(mainActivity.playlists.get(i));
                playlistDetailDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                playlistDetailDialog.setDeleteCallback(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(MainActivity.getTrackController().getPlaylist() == mainActivity.playlists.get(i)){
                            MainActivity.getTrackController().setPlaylist(null);
                        }
                        mainActivity.playlistDAO.deletePlaylist(mainActivity.playlists.get(i));
                        mainActivity.playlists.remove(i);
                        refreshAdapter();
                        playlistDetailDialog.dismiss();
                    }
                });

                playlistDetailDialog.show(mainActivity.getFragmentManager(), "Playlist Details");

                return true;
            }
        };
    }


}
