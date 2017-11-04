package com.example.alexander.musicplayer.view.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.TrackCallBack;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.TrackObserver;
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

    MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) inflater.getContext();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_content, container, false);

        final TrackListAdapter trackListAdapter = new TrackListAdapter(mainActivity, currentPlaylist, mainActivity.getBeanContext().getTrackController());
        ListView trackList = ll.findViewById(R.id.tracks_list);
        trackList.setAdapter(trackListAdapter);
        trackList.setOnItemClickListener(getOnTrackClickListener());
        trackList.setOnItemLongClickListener(getOnLongClickListener(mainActivity, trackListAdapter));

        ll.findViewById(R.id.buttonChooseFiles).setOnClickListener(getChooseFilesButtonListener());
        ll.findViewById(R.id.back).setOnClickListener(getBackButtonListener());

        ((TextView)ll.findViewById(R.id.playlist_title)).setText(currentPlaylist.getName());

        mainActivity.getBeanContext().getTrackController().registerStartRunningSongObserver("playListContent", new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                trackListAdapter.notifyDataSetChanged();
            }
        });

        return ll;
    }

    /** Add/remove files from playlist handler */
    private View.OnClickListener getChooseFilesButtonListener(){
        final List<String> tracks = mainActivity.getBeanContext().getSongService().getPaths(currentPlaylist.getSongs());
        final View.OnClickListener callback = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getBeanContext().getSongService().updateSongs(currentPlaylist, tracks);
            }
        };
        return new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.getBeanContext().getViewChanger().showChooseFilesFragment((ArrayList<String>) tracks, callback);
            }
        };
    }

    /**Back button handler*/
    private View.OnClickListener getBackButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
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
                mainActivity.getBeanContext().getViewChanger().openSlide();
                //TrackControllerAdapter trackController = mainActivity.getTrackController();
                mainActivity.getBeanContext().getTrackController().setPlaylist(currentPlaylist);
                mainActivity.getBeanContext().getTrackController().playSong(position);
            }
        };
    }

    /** Long click on track of playlist handler - show extra info about track/file */
    private AdapterView.OnItemLongClickListener getOnLongClickListener(final MainActivity mainActivity, final TrackListAdapter trackListAdapter){
        return new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrackDetailsDialog trackDetailsDialog = new TrackDetailsDialog();
                trackDetailsDialog.setSong((Song)adapterView.getItemAtPosition(i));
                trackDetailsDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                trackDetailsDialog.show(mainActivity.getFragmentManager(), "Track Details");
                trackDetailsDialog.setRemoveCallBack(new TrackCallBack() {
                    @Override
                    public void invoke(Song song) {
                        mainActivity.getBeanContext().getSongsDAO().deleteSongWithRelations(song);
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
}
