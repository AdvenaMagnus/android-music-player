package com.example.alexander.musicplayer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alexander.musicplayer.entities.Playlist;

/**
 * Created by Alexander on 18.08.2017.
 */

public class CreatePlayListDialog extends DialogFragment {

    Playlist playlist;
    View.OnClickListener goToNewPlaylist;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.create_playlist_dialog, container, false);
        ll.findViewById(R.id.ok).setOnClickListener( getOkButtonListener(((EditText)ll.findViewById(R.id.playlist_title)).getText().toString()));
        ll.findViewById(R.id.cancel).setOnClickListener(getCancelButtonListener());
        return ll;
    }

    private View.OnClickListener getOkButtonListener(final String playlistName){
        return new View.OnClickListener() {
            public void onClick(View v) {
                playlist.setName(playlistName);
                goToNewPlaylist.onClick(v);
                CreatePlayListDialog.this.dismiss();
            }
        };
    }

    private View.OnClickListener getCancelButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                CreatePlayListDialog.this.dismiss();
            }
        };
    }

    public View.OnClickListener getGoToNewPlaylist() {
        return goToNewPlaylist;
    }
    public void setGoToNewPlaylist(View.OnClickListener goToNewPlaylist) {
        this.goToNewPlaylist = goToNewPlaylist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
