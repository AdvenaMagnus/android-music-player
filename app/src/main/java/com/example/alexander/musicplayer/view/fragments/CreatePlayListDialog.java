package com.example.alexander.musicplayer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.callbacks.CreatePlaylistCallback;

/**
 * Created by Alexander on 18.08.2017.
 */

public class CreatePlayListDialog extends DialogFragment {

    CreatePlaylistCallback onOkClick;
    EditText playlistNameView;
    String playListName;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.create_playlist_dialog, container, false);
        playlistNameView = ll.findViewById(R.id.playlist_title);
        ll.findViewById(R.id.ok).setOnClickListener(getOkButtonListener());
        ll.findViewById(R.id.cancel).setOnClickListener(getCancelButtonListener());
        return ll;
    }

    private View.OnClickListener getOkButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                //playlist.setName(playlistNameView.getText().toString());
                playListName = playlistNameView.getText().toString();
                if(onOkClick !=null) {
                    if(onOkClick.invoke(playListName)) CreatePlayListDialog.this.dismiss();
                }else {
                    CreatePlayListDialog.this.dismiss();
                }
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

    public void setOnOkClick(CreatePlaylistCallback onOkClick) {
        this.onOkClick = onOkClick;
    }

    public String getPlayListName() {
        return playListName;
    }
}
