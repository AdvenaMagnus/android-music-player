package com.sogoodlabs.silvia.musicplayer.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.sogoodlabs.fileschooser.FileChoosingFragment;
import com.sogoodlabs.fileschooser.view.FilesChooserViewAPI;
import com.sogoodlabs.silvia.musicplayer.R;
import com.sogoodlabs.silvia.musicplayer.controller.callbacks.CreatePlaylistCallback;
import com.sogoodlabs.silvia.musicplayer.controller.callbacks.PlaylistMenuDialogActionCallback;
import com.sogoodlabs.silvia.musicplayer.controller.callbacks.TrackCallBack;
import com.sogoodlabs.silvia.musicplayer.model.entities.Playlist;
import com.sogoodlabs.silvia.musicplayer.model.entities.Song;
import com.sogoodlabs.silvia.musicplayer.view.FilesChooserViewImpl;
import com.sogoodlabs.silvia.musicplayer.view.fragments.CreatePlayListDialog;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistContentFragment;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistDetailDialog;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistMenuDialog;
import com.sogoodlabs.silvia.musicplayer.view.fragments.PlaylistsFragment;
import com.sogoodlabs.silvia.musicplayer.view.fragments.SettingsFragment;
import com.sogoodlabs.silvia.musicplayer.view.fragments.TrackDetailsDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 04.11.2017.
 */

public class ViewChanger {

    BeanContext beanContext;
    FragmentActivity activity;
    DrawerLayout drawerLayout;

    public ViewChanger(BeanContext beanContext){
        this.beanContext = beanContext;
    }

    /** Show fragment for files chooser and choose the files */
    public void showChooseFilesFragment(ArrayList<String> tracks, View.OnClickListener callback) {
        FileChoosingFragment fileChoosingFragment = new FileChoosingFragment();
        fileChoosingFragment.setOkButtonCallback(callback);
        fileChoosingFragment.setConfiguration(beanContext.getFilesChooserConfiguration());
        fileChoosingFragment.setPaths(tracks);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fileChoosingFragment).addToBackStack(null).commit();
    }

    /** Show frgment with playlists */
    public void showPlayLists() {
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();
        beanContext.injectInPlaylistsFragment(playlistsFragment);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistsFragment).addToBackStack(null).commit();
    }

    public void openDialogToCreatePlaylist(CreatePlaylistCallback onOkClickCallback){
        final CreatePlayListDialog dialog = new CreatePlayListDialog();
        dialog.setOnOkClick(onOkClickCallback);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.show(activity.getFragmentManager(), "Create playlist");
    }

    public void openPlaylistMenuDialog(PlaylistMenuDialogActionCallback onOkClickCallback){
        PlaylistMenuDialog playlistMenuDialog = new PlaylistMenuDialog();
        playlistMenuDialog.setCallback(onOkClickCallback);
        playlistMenuDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        playlistMenuDialog.show(activity.getFragmentManager(), "Playlist menu");
    }

    public void showPlaylistDetailsDialog(Playlist playlist, View.OnClickListener deleteCallback){
        final PlaylistDetailDialog playlistDetailDialog = new PlaylistDetailDialog();
        playlistDetailDialog.setPlaylist(playlist);
        playlistDetailDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        playlistDetailDialog.setDeleteCallback(deleteCallback);
        playlistDetailDialog.show(activity.getFragmentManager(), "Playlist Details");
    }

    /** Show fragment with certain playlist */
    public void showPlayListContent(Playlist playlist){
        //trackController.setPlaylist(playlist);
        PlaylistContentFragment playlistContentFragment = new PlaylistContentFragment();
        playlistContentFragment.setCurrentPlaylist(playlist);
        beanContext.injectInPlaylistContentFragment(playlistContentFragment);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistContentFragment).addToBackStack(null).commit();
    }

    public void showTrackDetailsDialog(AdapterView<?> adapterView, int i, TrackCallBack trackCallBack){
        TrackDetailsDialog trackDetailsDialog = new TrackDetailsDialog();
        trackDetailsDialog.setSong((Song)adapterView.getItemAtPosition(i));
        trackDetailsDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        trackDetailsDialog.show(activity.getFragmentManager(), "Track Details");
        trackDetailsDialog.setRemoveCallBack(trackCallBack);
    }

    /** Show fragments with settings */
    public void showSettings(){
        SettingsFragment settingsFragment = new SettingsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingsFragment).addToBackStack(null).commit();
    }

    public void openSlide(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void closeSlide(){
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void popBackStack(){
        activity.getSupportFragmentManager().popBackStack();
    }


    public void setActivity(Activity activity) {
        if(activity instanceof FragmentActivity)
            this.activity = (FragmentActivity) activity;
        else
            throw new ClassCastException("Must be FragmentActivity for injecting, but found " + activity.getClass().getName());
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }
}
