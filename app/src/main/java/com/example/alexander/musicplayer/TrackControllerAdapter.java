package com.example.alexander.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Alexander on 16.08.2017.
 */

public class TrackControllerAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;

    TrackControllerAdapter(Context ctx){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return lInflater.inflate(R.layout.track_controller, viewGroup, false);
    }
}
