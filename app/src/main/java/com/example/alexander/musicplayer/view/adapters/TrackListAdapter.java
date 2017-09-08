package com.example.alexander.musicplayer.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.model.entities.Playlist;

import java.util.HashMap;

/**
 * Created by Alexander on 23.08.2017.
 */

public class TrackListAdapter extends BaseAdapter {

    //List<Song> tracks;
    Playlist playlist;
    private LayoutInflater lInflater;
    int currentTrack = Integer.MAX_VALUE;

    public TrackListAdapter(Context ctx, Playlist playlist){
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.playlist = playlist;
    }

    @Override
    public int getCount() {
        return playlist.getSongs().size();
    }

    @Override
    public Object getItem(int i) {
        return playlist.getSongs().get(i);
    }

    @Override
    public long getItemId(int i) {
        return playlist.getSongs().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout ll;
        if(i==currentTrack) {
            ll = (LinearLayout) lInflater.inflate(R.layout.track_layout_current, null, false);
            TextView title = ll.findViewById(R.id.song_title);
            title.setText(playlist.getSongs().get(i).getName());

//            ll.setBackgroundColor(ContextCompat.getColor(lInflater.getContext(), R.color.second));
//            title.setBackgroundColor(ContextCompat.getColor(lInflater.getContext(), R.color.second));
//            title.setTextColor(ContextCompat.getColor(lInflater.getContext(), R.color.primary));
            //title.setText("P: " + playlist.getSongs().get(i).getName());
        } else {
            ll = (LinearLayout) lInflater.inflate(R.layout.track_layout, null, false);
            TextView title = ll.findViewById(R.id.song_title);
            title.setText(playlist.getSongs().get(i).getName());
        }
        return ll;
    }

    public void setCurrentTrack(int i){
        currentTrack = i;
        notifyDataSetChanged();
    }
}
