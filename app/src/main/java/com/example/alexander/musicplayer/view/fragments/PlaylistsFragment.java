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
    ListView playListsListView;
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
        ((ArrayAdapter) playListsListView.getAdapter()).clear();
        ((ArrayAdapter) playListsListView.getAdapter()).addAll(PlaylistService.playlistsNames(mainActivity.getPlaylists()));
    }

    private void openDialogToCreatePlaylist(){
        final CreatePlayListDialog dialog = new CreatePlayListDialog();
        dialog.setOnOkClick(new View.OnClickListener() {
            public void onClick(View v) {
                Playlist playlist = mainActivity.getBeanContext().getPlaylistDAO().createNew(dialog.getPlayListName());
                playlist.setSongs(new ArrayList<Song>());
                mainActivity.playlists.add(playlist);
                mainActivity.getBeanContext().getViewChanger().showPlayListContent(playlist);
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
        playListsListView = ll.findViewById(R.id.playlists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        playListsListView.setAdapter(adapter);
        playListsListView.setOnItemClickListener(getOnPlaylistClickListener(mainActivity));
        playListsListView.setOnItemLongClickListener(getOnLongClickListener(mainActivity));
    }

    private AdapterView.OnItemClickListener getOnPlaylistClickListener(final MainActivity mainActivity){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                // adapter.getItemAtPosition(position) returns String - name of playlist, not the playlist
                mainActivity.getBeanContext().getViewChanger().showPlayListContent(PlaylistService.getPlayListByName((String)adapter.getItemAtPosition(position), mainActivity.getPlaylists()));
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
                        if(mainActivity.getBeanContext().getTrackController().getPlaylist() == mainActivity.playlists.get(i)){
                            mainActivity.getBeanContext().getTrackController().setPlaylist(null);
                        }
                        mainActivity.getBeanContext().getPlaylistDAO().deletePlaylist(mainActivity.playlists.get(i));
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
