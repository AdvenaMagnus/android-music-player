package com.example.alexander.musicplayer.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.SongService;
import com.example.alexander.musicplayer.controller.callbacks.PlaylistMenuDialogActionCallback;
import com.example.alexander.musicplayer.controller.callbacks.TrackCallBack;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.callbacks.TrackObserver;
import com.example.alexander.musicplayer.controller.ViewChanger;
import com.example.alexander.musicplayer.model.SongsDAO;
import com.example.alexander.musicplayer.model.entities.Song;
import com.example.alexander.musicplayer.view.adapters.TrackListAdapter;
import com.example.alexander.musicplayer.model.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 18.08.2017.
 */

public class PlaylistContentFragment extends Fragment {

    /** Current listed playlist */
    Playlist currentPlaylist;

    TrackController trackController;
    SongService songService;
    ViewChanger viewChanger;
    SongsDAO songsDAO;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = inflater.getContext();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_content, container, false);

        final TrackListAdapter trackListAdapter = new TrackListAdapter(ctx, currentPlaylist, trackController);
        ListView trackList = ll.findViewById(R.id.tracks_list);
        trackList.setAdapter(trackListAdapter);
        trackList.setOnItemClickListener(getOnTrackClickListener());
        trackList.setOnItemLongClickListener(getOnLongClickListener(trackListAdapter));

//        ll.findViewById(R.id.buttonChooseFiles).setOnClickListener(getChooseFilesButtonListener());
//        ll.findViewById(R.id.back).setOnClickListener(getBackButtonListener());

        TextView playlistTitle = ll.findViewById(R.id.playlist_title);
        playlistTitle.setText(currentPlaylist.getName());
        //playlistTitle.setWidth((int)(playlistTitle.getWidth() + playlistTitle.getWidth()*0.3));
        //ll.findViewById(R.id.playlist_title).setOnClickListener(openPlaylistMenuListener());
        ll.findViewById(R.id.playlist_title_layout).setOnClickListener(openPlaylistMenuListener());

        trackController.registerStartRunningSongObserver("playListContent", new TrackObserver() {
            @Override
            public void update(int i, Playlist playlist) {
                trackListAdapter.notifyDataSetChanged();
            }
        });

        return ll;
    }

    /** Open dialog with menu for playlist */
    private View.OnClickListener openPlaylistMenuListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewChanger.openPlaylistMenuDialog(new PlaylistMenuDialogActionCallback() {
                    @Override
                    public void invoke(PlaylistMenuDialog.Buttons button) {
                        if(button == PlaylistMenuDialog.Buttons.AddNewTracks){
                            final List<String> tracks = songService.getPaths(currentPlaylist.getSongs());
                            final View.OnClickListener callback = new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    songService.updateSongs(currentPlaylist, tracks);
                                }
                            };
                            viewChanger.showChooseFilesFragment((ArrayList<String>) tracks, callback);
                        }
                        if(button == PlaylistMenuDialog.Buttons.BackToPlaylists){
                            viewChanger.popBackStack();
                        }
                    }
                });
            }
        };
    }

    /** Add/remove files from playlist handler */
    private View.OnClickListener getChooseFilesButtonListener(){
        final List<String> tracks = songService.getPaths(currentPlaylist.getSongs());
        final View.OnClickListener callback = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                songService.updateSongs(currentPlaylist, tracks);
            }
        };
        return new View.OnClickListener() {
            public void onClick(View v) {
                viewChanger.showChooseFilesFragment((ArrayList<String>) tracks, callback);
            }
        };
    }

    /**Back button handler*/
    private View.OnClickListener getBackButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                viewChanger.popBackStack();
            }
        };
    }

    /** Click on track of playlist handler - start song and open track controller*/
    private AdapterView.OnItemClickListener getOnTrackClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
//                Animation shake = AnimationUtils.loadAnimation(mainActivity, R.anim.rotation);
//                v.startAnimation(shake);
                //TrackControllerAdapter trackController = mainActivity.getTrackController();
                trackController.setPlaylist(currentPlaylist);
                trackController.playSong(position);
                viewChanger.openSlide();
            }
        };
    }

    /** Long click on playlist track handler - show extra info about track/file */
    private AdapterView.OnItemLongClickListener getOnLongClickListener(final TrackListAdapter trackListAdapter){
        return new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewChanger.showTrackDetailsDialog(adapterView, i, new TrackCallBack() {
                    @Override
                    public void invoke(Song song) {
                        songsDAO.deleteSongWithRelations(song);
                        currentPlaylist.getSongs().remove(song);
                        trackListAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        };
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    public void setCurrentPlaylist(Playlist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public void setTrackController(TrackController trackController) {
        this.trackController = trackController;
    }
    public void setSongService(SongService songService) {
        this.songService = songService;
    }
    public void setViewChanger(ViewChanger viewChanger) {
        this.viewChanger = viewChanger;
    }
    public void setSongsDAO(SongsDAO songsDAO) {
        this.songsDAO = songsDAO;
    }
}
