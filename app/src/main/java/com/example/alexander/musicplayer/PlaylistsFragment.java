package com.example.alexander.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.entities.Playlist;

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

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(ll==null) {
            ll = (LinearLayout) inflater.inflate(R.layout.playlists, container, false);
            Button buttonForChosingFiles = ll.findViewById(R.id.buttonChoseFiles);
            buttonForChosingFiles.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            //((MainActivity) inflater.getContext()).showChooseFilesFragment();
                            CreatePlayListDialog dialog = new CreatePlayListDialog();
                            dialog.setMainActivity((MainActivity) inflater.getContext());
                            dialog.show(((Activity)inflater.getContext()).getFragmentManager(), "Create playlist");
                        }
                    }
            );

            playLists = ll.findViewById(R.id.playlists);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                    android.R.layout.simple_list_item_1, new ArrayList<String>());
            playLists.setAdapter(adapter);
        }
        ((ArrayAdapter)playLists.getAdapter()).clear();
        ((ArrayAdapter)playLists.getAdapter()).addAll(Playlist.playlistsNames(((MainActivity)inflater.getContext()).getPlaylists()));
        return ll;
    }
}
