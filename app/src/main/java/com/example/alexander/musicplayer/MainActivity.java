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

import com.example.alexander.musicplayer.controller.PlaylistService;
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

    PlaylistsFragment playlistsFragment;
    public List<Playlist> playlists = new ArrayList<>();
    private ListView mDrawerList;
    public DrawerLayout dl;

    public PlaylistDAO playlistDAO;
    SongsDAO songsDAO;
    SongService songService;
    PlaylistService playlistService;

    public static Theme currentTheme = null;

    private static HashMap<String, Boolean> extensionsToFilter = new HashMap<>(); // Permitted files
    static {
        extensionsToFilter.put("3gp", true);
        extensionsToFilter.put("mp4", true);
        extensionsToFilter.put("m4a", true);
        extensionsToFilter.put("aac", true);
        extensionsToFilter.put("ts", true);
        extensionsToFilter.put("flac", true);
        extensionsToFilter.put("mid", true);
        extensionsToFilter.put("xmf", true);
        extensionsToFilter.put("mxmf", true);
        extensionsToFilter.put("rtttl", true);
        extensionsToFilter.put("rtttl", true);
        extensionsToFilter.put("rtx", true);
        extensionsToFilter.put("ota", true);
        extensionsToFilter.put("imy", true);
        extensionsToFilter.put("mp3", true);
        extensionsToFilter.put("mkv", true);
        extensionsToFilter.put("wav", true);
        extensionsToFilter.put("ogg", true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_main);

        createAndBindBeans();
        playlists = trackController != null && trackController.getPlaylist()!=null? playlistDAO.getAllPlayLists(trackController.getPlaylist()): playlistDAO.getAllPlayLists();

        if(trackController==null)
            trackController = new TrackController();

        this.findViewById(R.id.track_controller_button).setOnClickListener(getTrackControllerButtonListener());
        this.findViewById(R.id.settings_button).setOnClickListener(getSettingsButtonListener());

        Typeface face= Typeface.createFromAsset(getAssets(), "font/GreatVibes-Regular.otf");
        ((TextView)this.findViewById(R.id.header)).setTypeface(face);

        if(playlistsFragment==null) {
            playlistsFragment = new PlaylistsFragment();
            //playlistsFragment.setArguments(getIntent().getExtras());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, playlistsFragment).commit();

        setDrawerLayout();
    }

    /** Initialize track controller */
    private void setDrawerLayout(){
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        trackControllerAdapter = new TrackControllerAdapter(this, trackController);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
        setWidthForSlide(0.8f);
    }

    /** Set width for track controller slide
     * @param widthMultiplier  - multiplier for all screen width (slide width = multiplyer * screen width) */
    private void setWidthForSlide(float widthMultiplier){
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
                MainActivity.this.showSettings();
            }
        };
    }


    /** Some spring-like stuff */
    void createAndBindBeans(){
        DBhelper dBhelper = new DBhelper(this);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        songsDAO = new SongsDAO();
        songsDAO.setDb(db);

        playlistService = new PlaylistService();

        playlistDAO = new PlaylistDAO();
        playlistDAO.setDb(db);
        playlistDAO.setSongsDAO(songsDAO);
        playlistDAO.setPlaylistService(playlistService);

        songsDAO.setPlaylistDAO(playlistDAO);
        songService = new SongService(songsDAO);
    }

    /** Show fragment for files chooser and choose the files */
    public void showChooseFilesFragment(ArrayList<String> tracks, View.OnClickListener callback) {
        FileChoosingFragment fileChoosingFragment = new FileChoosingFragment();
        fileChoosingFragment.setCallback(callback);
        fileChoosingFragment.setExtensionsToFilter(extensionsToFilter);
        Bundle args = new Bundle();
        args.putStringArrayList("paths", tracks);
        fileChoosingFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fileChoosingFragment).addToBackStack(null).commit();
    }

    /** Show frgment with playlists */
    public void showPlayLists() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistsFragment).addToBackStack(null).commit();
    }

    /** Show fragment with certain playlist */
    public void showPlayListContent(Playlist playlist){
        //trackController.setPlaylist(playlist);
        PlaylistContentFragment playlistContentFragment = new PlaylistContentFragment();
        playlistContentFragment.setCurrentPlaylist(playlist);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playlistContentFragment).addToBackStack(null).commit();
    }

    /** Show fragments with settings */
    public void showSettings(){
        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingsFragment).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        trackControllerAdapter.onDestroy();
        System.gc();
    }

    private void applyTheme(){
        if(currentTheme==null){
            currentTheme = loadCurrentTheme();
        }
        switch (currentTheme){
            case Dark: {
                setTheme(R.style.AppTheme);
                break;
            }
            case Light: {
                setTheme(R.style.AppThemeLight);
                break;
            }
        }
    }

    public void setCurrentTheme(Theme theme){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Theme", theme.getName());
        editor.commit();
        currentTheme = theme;
    }

    public Theme loadCurrentTheme(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String themeString = sharedPref.getString("Theme", "Dark");
        switch (themeString){
            case "Dark": {
                return Theme.Dark;
            }
            case "Light": {
                return Theme.Light;
            }
        }
        return null;
    }


    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public static TrackController getTrackController() {
        return trackController;
    }

    public SongService getSongService() {
        return songService;
    }

    public SongsDAO getSongsDAO() {
        return songsDAO;
    }

    public TrackControllerAdapter getTrackControllerAdapter() {
        return trackControllerAdapter;
    }
}
