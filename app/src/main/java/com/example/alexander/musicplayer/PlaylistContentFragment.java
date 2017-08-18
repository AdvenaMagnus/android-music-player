package com.example.alexander.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.entities.Playlist;

import java.util.ArrayList;

/**
 * Created by Alexander on 18.08.2017.
 */

public class PlaylistContentFragment extends Fragment {

    Playlist currentPlaylist;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_content, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                android.R.layout.simple_list_item_1, currentPlaylist.getTracks());
        ((ListView)ll.findViewById(R.id.tracks_list)).setAdapter(adapter);

        ll.findViewById(R.id.buttonChooseFiles).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        ((MainActivity)inflater.getContext()).showChooseFilesFragment((ArrayList<String>) currentPlaylist.getTracks());
                    }
                }
        );

        ll.findViewById(R.id.back).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        ((FragmentActivity)inflater.getContext()).getSupportFragmentManager().popBackStack();
                    }
                }
        );

        return ll;
    }


    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    public void setCurrentPlaylist(Playlist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }
}
