package com.example.alexander.musicplayer.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.file_chooser.FileChoosingFragment;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.view.fragments.PlaylistContentFragment;
import com.example.alexander.musicplayer.view.fragments.PlaylistsFragment;
import com.example.alexander.musicplayer.view.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 04.11.2017.
 */

public class ViewChanger {

    FragmentActivity activity;
    HashMap<String, Boolean> extensionsToFilter;

    DrawerLayout drawerLayout;

    public ViewChanger(){

    }

    /** Show fragment for files chooser and choose the files */
    public void showChooseFilesFragment(ArrayList<String> tracks, View.OnClickListener callback) {
        FileChoosingFragment fileChoosingFragment = new FileChoosingFragment();
        fileChoosingFragment.setOkButtonCallback(callback);
        fileChoosingFragment.setExtensionsToFilter(extensionsToFilter);
        Bundle args = new Bundle();
        args.putStringArrayList("paths", tracks);
        fileChoosingFragment.setArguments(args);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fileChoosingFragment).addToBackStack(null).commit();
    }

    /** Show frgment with playlists */
    public void showPlayLists() {
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistsFragment).addToBackStack(null).commit();
    }

    /** Show fragment with certain playlist */
    public void showPlayListContent(Playlist playlist){
        //trackController.setPlaylist(playlist);
        PlaylistContentFragment playlistContentFragment = new PlaylistContentFragment();
        playlistContentFragment.setCurrentPlaylist(playlist);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistContentFragment).addToBackStack(null).commit();
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


    public void setActivity(Activity activity) {
        if(activity instanceof FragmentActivity)
            this.activity = (FragmentActivity) activity;
        else
            throw new ClassCastException("Must be FragmentActivity for injecting, but found " + activity.getClass().getName());
    }
    public void setExtensionsToFilter(HashMap<String, Boolean> extensionsToFilter) {
        this.extensionsToFilter = extensionsToFilter;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }
}
