package com.example.alexander.musicplayer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alexander.musicplayer.entities.Playlist;

import java.util.ArrayList;

/**
 * Created by Alexander on 18.08.2017.
 */

public class CreatePlayListDialog extends DialogFragment {

    LayoutInflater inflater;
    LinearLayout ll;
    MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        ll = (LinearLayout) inflater.inflate(R.layout.create_playlist_dialog, container, false);
        ll.findViewById(R.id.ok).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Playlist playlist = new Playlist();
                        playlist.setName(((EditText)ll.findViewById(R.id.playlist_title)).getText().toString());
                        playlist.setTracks(new ArrayList<String>());
                        mainActivity.playlists.add(playlist);
                        mainActivity.showPlayListContent(playlist);
                        //mainActivity.showChooseFilesFragment();
                        CreatePlayListDialog.this.dismiss();
                    }
                }
        );

        ll.findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                       CreatePlayListDialog.this.dismiss();
                    }
                }
        );

        return ll;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
