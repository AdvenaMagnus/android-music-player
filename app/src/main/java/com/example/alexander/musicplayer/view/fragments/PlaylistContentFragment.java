package com.example.alexander.musicplayer.view.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
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

    Playlist currentPlaylist;
    MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) inflater.getContext();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_content, container, false);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
//                android.R.layout.simple_list_item_1, currentPlaylist.getTracks());

        final TrackListAdapter trackListAdapter = new TrackListAdapter(mainActivity, MainActivity.getTrackController());
        ListView trackList = ll.findViewById(R.id.tracks_list);
        trackList.setAdapter(trackListAdapter);
        trackList.setOnItemClickListener(getOnTrackClickListener());
        trackList.setOnItemLongClickListener(getOnLongClickListener(mainActivity));

        ll.findViewById(R.id.buttonChooseFiles).setOnClickListener(getChooseFilesButtonListener());
        ll.findViewById(R.id.back).setOnClickListener(getBackButtonListener());

        MainActivity.getTrackController().registerStartRunningSongObserver("playListContent", new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                //((TextView) trackListAdapter.views.get(i).findViewById(R.id.song_title)).setTextColor(ContextCompat.getColor(mainActivity, R.color.primary));
                trackListAdapter.setCurrentTrack(i);
            }
        });

        return ll;
    }

    private View.OnClickListener getChooseFilesButtonListener(){
        final List<String> tracks = mainActivity.getSongService().getPaths(currentPlaylist.getSongs());
        final View.OnClickListener callback = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mainActivity.getSongService().updateSongs(currentPlaylist, tracks);
            }
        };
        return new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.showChooseFilesFragment((ArrayList<String>) tracks, callback);
            }
        };
    }

    private View.OnClickListener getBackButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        };
    }

    private AdapterView.OnItemClickListener getOnTrackClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
//                Animation shake = AnimationUtils.loadAnimation(mainActivity, R.anim.rotation);
//                v.startAnimation(shake);
                mainActivity.dl.openDrawer(Gravity.LEFT);
                //TrackControllerAdapter trackController = mainActivity.getTrackController();
                TrackController trackController = mainActivity.getTrackController();
                //trackController.setPlaylist(currentPlaylist);
                trackController.playSong(position);
            }
        };
    }

    private AdapterView.OnItemLongClickListener getOnLongClickListener(final MainActivity mainActivity){
        return new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrackDetailsDialog trackDetailsDialog = new TrackDetailsDialog();
                trackDetailsDialog.setSong((Song)adapterView.getItemAtPosition(i));
                trackDetailsDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                trackDetailsDialog.show(mainActivity.getFragmentManager(), "Track Details");

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
