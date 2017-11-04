package com.example.alexander.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.musicplayer.controller.BeanContext;
import com.example.alexander.musicplayer.controller.CurrentPlaylistStorageService;
import com.example.alexander.musicplayer.controller.PlaylistService;
import com.example.alexander.musicplayer.controller.ThemeService;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.model.PlaylistDAO;
import com.example.alexander.musicplayer.view.Theme;
import com.example.alexander.musicplayer.view.adapters.TrackControllerAdapter;
import com.example.alexander.musicplayer.view.fragments.PlaylistContentFragment;
import com.example.alexander.musicplayer.view.fragments.PlaylistsFragment;
import com.example.alexander.musicplayer.controller.SongService;
import com.example.alexander.musicplayer.model.SongsDAO;
import com.example.alexander.musicplayer.model.entities.Playlist;
import com.example.alexander.musicplayer.file_chooser.FileChoosingFragment;
import com.example.alexander.musicplayer.view.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TrackControllerAdapter trackControllerAdapter;
    static TrackController trackController;
    public List<Playlist> playlists = new ArrayList<>();
    public DrawerLayout dl;

    BeanContext beanContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAndBindBeans();
        beanContext.getThemeService().applyTheme();
        setContentView(R.layout.activity_main);

        if(trackController != null){
            playlists = trackController.getPlaylist()!=null? beanContext.getPlaylistDAO().getAllPlayLists(trackController.getPlaylist()): beanContext.getPlaylistDAO().getAllPlayLists();
            beanContext.injectInTrackController(trackController);
        } else {
            playlists = beanContext.getPlaylistDAO().getAllPlayLists();
            Playlist playlist = beanContext.getCurrentPlaylistStorageService().getLastPlayList(playlists);
            trackController = new TrackController(playlist);
            beanContext.injectInTrackController(trackController);
        }

        beanContext.getViewChanger().showPlayLists();

        this.findViewById(R.id.track_controller_button).setOnClickListener(getTrackControllerButtonListener());
        this.findViewById(R.id.settings_button).setOnClickListener(getSettingsButtonListener());

        Typeface face= Typeface.createFromAsset(getAssets(), "font/GreatVibes-Regular.otf");
        ((TextView)this.findViewById(R.id.header)).setTypeface(face);

        setDrawerLayout();
    }

    /** Initialize track controller */
    private void setDrawerLayout(){
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        trackControllerAdapter = new TrackControllerAdapter(this, trackController);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(trackControllerAdapter);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dl,
                R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                trackControllerAdapter.offVisualizer();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                trackControllerAdapter.onVisualizer();
            }
        };

        // Set the drawer toggle as the DrawerListener
        dl.setDrawerListener(mDrawerToggle);
        setWidthForSlide(mDrawerList, 0.8f);
    }

    /** Set width for track controller slide
     * @param widthMultiplier  - multiplier for all screen width (slide width = multiplyer * screen width) */
    private void setWidthForSlide(ListView mDrawerList, float widthMultiplier){
        int width = (int) (getResources().getDisplayMetrics().widthPixels*widthMultiplier);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        params.width = width;
        mDrawerList.setLayoutParams(params);
    }

    /** Button handler for button which opens track controller slide*/
    private View.OnClickListener getTrackControllerButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                dl.openDrawer(Gravity.LEFT);
            }
        };
    }

    /** Button handler for opening settings */
    private View.OnClickListener getSettingsButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.getBeanContext().getViewChanger().showSettings();
            }
        };
    }

    /** Some spring-like stuff */
    void createAndBindBeans(){
        DBhelper dBhelper = new DBhelper(this);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        beanContext = new BeanContext();
        beanContext.injectDB(db);
        beanContext.injectActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        trackControllerAdapter.onDestroy();
        System.gc();
    }


    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public static TrackController getTrackController() {
        return trackController;
    }

    public TrackControllerAdapter getTrackControllerAdapter() {
        return trackControllerAdapter;
    }

    public BeanContext getBeanContext() {
        return beanContext;
    }
}
