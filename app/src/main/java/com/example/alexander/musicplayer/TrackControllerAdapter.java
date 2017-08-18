package com.example.alexander.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alexander on 16.08.2017.
 */

public class TrackControllerAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    MediaPlayer mediaPlayer = new MediaPlayer();
    List<String> tracks;

    TrackControllerAdapter(Context ctx){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //this.tracks = tracks;
    }

    enum Parts {
        trackController(1);

        long id;
        Parts(long id){
            this.id = id;
        }
    }

    @Override
    public int getCount() {
        return Parts.values().length;
    }

    @Override
    public Object getItem(int i) {
        return Parts.values()[i];
    }

    @Override
    public long getItemId(int i) {
        return Parts.values()[i].id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = lInflater.inflate(R.layout.track_controller, viewGroup, false);
        layout.findViewById(R.id.playButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
//                        try {
//                            mediaPlayer.setDataSource(ctx, Uri.parse(tracks.get(0)));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        mediaPlayer = MediaPlayer.create(ctx, Uri.parse(tracks.get(0)));
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                }
        );

        layout.findViewById(R.id.stopButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        mediaPlayer.stop();
                    }
                }
        );

        return layout;
    }
}
