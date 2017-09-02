package com.example.alexander.musicplayer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.SongService;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.HashMap;

/**
 * Created by Alexander on 30.08.2017.
 */

public class TrackDetailsDialog extends DialogFragment {

    Song song;

    public TrackDetailsDialog(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        HashMap<String, String> metData = song.getMetadata();

        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.track_details, container, false);

        ((TextView)ll.findViewById(R.id.filename)).setText(song.getName());
        ((TextView)ll.findViewById(R.id.artist)).setText(metData.get("artist"));
        ((TextView)ll.findViewById(R.id.album)).setText(metData.get("album"));
        ((TextView)ll.findViewById(R.id.title)).setText(metData.get("title"));
        ((TextView)ll.findViewById(R.id.duration)).setText(metData.get("duration"));
        return ll;
    }

    public void setSong(Song song) {
        this.song = song;
    }

}
