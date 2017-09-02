package com.example.alexander.musicplayer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alexander.musicplayer.R;

/**
 * Created by Alexander on 18.08.2017.
 */

public class CreatePlayListDialog extends DialogFragment {

    View.OnClickListener onOkClick;
    View.OnClickListener onCancelCallback;
    EditText playlistName;
    String playListName;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.create_playlist_dialog, container, false);
        playlistName = ll.findViewById(R.id.playlist_title);
        ll.findViewById(R.id.ok).setOnClickListener(getOkButtonListener());
        ll.findViewById(R.id.cancel).setOnClickListener(getCancelButtonListener());
        return ll;
    }

    private View.OnClickListener getOkButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                //playlist.setName(playlistName.getText().toString());
                playListName = playlistName.getText().toString();
                if(onOkClick !=null) onOkClick.onClick(v);
                CreatePlayListDialog.this.dismiss();
            }
        };
    }

    private View.OnClickListener getCancelButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(onCancelCallback!=null) onCancelCallback.onClick(v);
                CreatePlayListDialog.this.dismiss();
            }
        };
    }

    public View.OnClickListener getOnOkClick() {
        return onOkClick;
    }
    public void setOnOkClick(View.OnClickListener onOkClick) {
        this.onOkClick = onOkClick;
    }

    public View.OnClickListener getOnCancelCallback() {
        return onCancelCallback;
    }
    public void setOnCancelCallback(View.OnClickListener onCancelCallback) {
        this.onCancelCallback = onCancelCallback;
    }

    public String getPlayListName() {
        return playListName;
    }
}
