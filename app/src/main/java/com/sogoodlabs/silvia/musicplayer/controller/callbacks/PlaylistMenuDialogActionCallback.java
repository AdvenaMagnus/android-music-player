package com.sogoodlabs.silvia.musicplayer.controller.callbacks;

import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistMenuDialog;

/**
 * Created by Alexander on 01.01.2018.
 */

public interface PlaylistMenuDialogActionCallback {
    void invoke(PlaylistMenuDialog.Buttons button);
}
