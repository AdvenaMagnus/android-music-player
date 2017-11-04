package com.example.alexander.musicplayer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.model.entities.Playlist;

/**
 * Created by Alexander on 30.08.2017.
 */

public class PlaylistDetailDialog extends DialogFragment {

    Playlist playlist;
    View.OnClickListener deleteCallback;

    public  PlaylistDetailDialog(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_details, container, false);
        if(playlist.getName()!=null)
            ((TextView)ll.findViewById(R.id.playlist_title)).setText(playlist.getName());

        ((TextView)ll.findViewById(R.id.songs)).setText(""+playlist.getSongs().size());

        ll.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteCallback!=null)
                    deleteCallback.onClick(view);
                PlaylistDetailDialog.this.dismiss();
            }
        });

        ll.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistDetailDialog.this.dismiss();
            }
        });

        return ll;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setDeleteCallback(View.OnClickListener deleteCallback) {
        this.deleteCallback = deleteCallback;
    }
}
