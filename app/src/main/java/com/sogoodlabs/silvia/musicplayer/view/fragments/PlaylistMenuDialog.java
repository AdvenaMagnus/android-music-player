package com.sogoodlabs.silvia.musicplayer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sogoodlabs.silvia.musicplayer.R;
import com.sogoodlabs.silvia.musicplayer.controller.callbacks.PlaylistMenuDialogActionCallback;

/**
 * Created by Alexander on 01.01.2018.
 */

public class PlaylistMenuDialog extends DialogFragment {

    PlaylistMenuDialogActionCallback callback;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.playlist_menu_dialog, container, false);
        ll.findViewById(R.id.buttonAddTracks).setOnClickListener(getOnClickListener(Buttons.AddNewTracks));
        ll.findViewById(R.id.returnToPlaylists).setOnClickListener(getOnClickListener(Buttons.BackToPlaylists));
        ll.findViewById(R.id.buttonClosePopup).setOnClickListener(getOnClickListener(Buttons.Back));
        return ll;
    }

    private View.OnClickListener getOnClickListener(final Buttons button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistMenuDialog.this.dismiss();
                callback.invoke(button);
            }
        };
    }

    public void setCallback(PlaylistMenuDialogActionCallback callback) {
        this.callback = callback;
    }

    public enum Buttons {
        AddNewTracks, BackToPlaylists, Back;
    }

}
