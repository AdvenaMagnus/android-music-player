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
import com.example.alexander.musicplayer.controller.TrackCallBack;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.HashMap;

/**
 * Created by Alexander on 30.08.2017.
 */

public class TrackDetailsDialog extends DialogFragment {

    Song song;
    TrackCallBack removeCallBack;

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

        ll.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrackDetailsDialog.this.dismiss();
            }
        });

        ll.findViewById(R.id.remove_track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(removeCallBack!=null) removeCallBack.invoke(song);
                TrackDetailsDialog.this.dismiss();
            }
        });
        return ll;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setRemoveCallBack(TrackCallBack removeCallBack) {
        this.removeCallBack = removeCallBack;
    }
}
