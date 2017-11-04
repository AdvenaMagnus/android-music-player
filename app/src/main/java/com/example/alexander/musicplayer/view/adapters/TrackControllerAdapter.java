package com.example.alexander.musicplayer.view.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.SongService;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.TrackObserver;
import com.example.alexander.musicplayer.model.entities.Song;
import com.example.alexander.musicplayer.view.fragments.PlaylistContentFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 16.08.2017.
 */

public class TrackControllerAdapter extends BaseAdapter{

    static final String adapterName = "trackControllerAdapter";

    public Context ctx;
    private LayoutInflater lInflater;
    private static MediaPlayer mediaPlayer;
    public EquilizerService equilizerService;
    public Visualizer visualizer;
    private View layout;
    private View eq;
    private SeekBar progressBar;
    public Thread progressBarUpdater;
    public boolean progressBarUpdaterStop=false;
    private Button playButton;
    private TextView fileName;
    private TextView duration_past;
    private boolean isVisualizerOn = true;
    ImageView albumCoverImg;

    private TrackController trackController;

    public TrackControllerAdapter(final Context ctx, TrackController trackController){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        equilizerService = new EquilizerService();
        isVisualizerOn = loadIsVisualizerOn();
        this.trackController = trackController;
        this.trackController.registerStartRunningSongObserver(adapterName, new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                setupTrack(song);
                playButtonAction();
                runVisualizer(mediaPlayer.getAudioSessionId());
            }
        });

        this.trackController.registerPauseRunningSongObserverList(adapterName, new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                playButtonAction();
            }
        });
        this.trackController.registerResumeRunningSongObserverList(adapterName, new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                playButtonAction();
            }
        });

        initMainControlButtons();
        initEq();
        runProgressBarUpdater();
        if(trackController!=null && trackController.getCurrentTrack()!=null){
            setupTrack(trackController.getCurrentTrack());
            mediaPlayer.seekTo((int) trackController.getPlaylist().getCurrentTrackLastStop());
            runVisualizer(mediaPlayer.getAudioSessionId());
        }

    }

    private void setupTrack(Song song){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song.getPath()));
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(getOnCompletionListener());
        setSongInfo(song);
    }

    /**Play button handler */
    private void playButtonAction(){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playButton.setText("Play");
            }
            else{
                mediaPlayer.start();
                playButton.setText("Pause");
            }
        }
    }

    enum SlideMenuItems {
        trackController(1);
        long id;
        SlideMenuItems(long id){
            this.id = id;
        }
    }

    @Override
    public int getCount() {
        return SlideMenuItems.values().length;
    }

    @Override
    public Object getItem(int i) {
        return SlideMenuItems.values()[i];
    }

    @Override
    public long getItemId(int i) {
        return SlideMenuItems.values()[i].id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return layout;
    }

    /** Initialize views in track controller*/
    private void initMainControlButtons(){
        layout = lInflater.inflate(R.layout.track_controller, null, false);

        playButton = layout.findViewById(R.id.playButton2);
        playButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
//                        Animation shake = AnimationUtils.loadAnimation(ctx, R.anim.shake);
//                        v.startAnimation(shake);
                        //playButtonAction();
                        if(trackController.isNowPlaying()){
                            trackController.updateLastSongDuartion(mediaPlayer.getCurrentPosition());
                            trackController.pause();
                        } else trackController.resume();
                    }
                }
        );

        fileName = layout.findViewById(R.id.file_name);
        fileName.setSelected(true);

        layout.findViewById(R.id.nextButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        equilizerService.effect1();
                        trackController.playNextSong();
                    }
                }
        );
        layout.findViewById(R.id.prevButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        equilizerService.effect1();
                        trackController.playPrevSong();
                    }
                }
        );

        progressBar = layout.findViewById(R.id.seekBar);
        progressBar.setOnSeekBarChangeListener(getProgressBarListener());

        eq = layout.findViewById(R.id.eqLayout);

        layout.findViewById(R.id.artist).setSelected(true);
        layout.findViewById(R.id.album).setSelected(true);
        layout.findViewById(R.id.title).setSelected(true);

        duration_past = layout.findViewById(R.id.duration_past);

        layout.findViewById(R.id.playlist).setOnClickListener(getOnPlaylistClickListener());

        albumCoverImg =  layout.findViewById(R.id.album_cover);
        setNoAlbumCover();
    }

    /** Set track information in appropriate views */
    private void setSongInfo(Song song){
        HashMap<String, String> metData = song.getMetadata();
        fileName.setText(song.getName());
        ((TextView)layout.findViewById(R.id.artist)).setText(metData.get("artist"));
        ((TextView)layout.findViewById(R.id.album)).setText(metData.get("album"));
        ((TextView)layout.findViewById(R.id.title)).setText(metData.get("title"));
        ((TextView)layout.findViewById(R.id.duration)).setText(metData.get("duration"));
        setAlbumCover(song);

        String playlistName=trackController.getPlaylist().getName();
        SpannableString content = new SpannableString(playlistName);
        content.setSpan(new UnderlineSpan(), 0, playlistName.length(), 0);
        ((TextView)layout.findViewById(R.id.playlist)).setText(content);
    }

    /** Set image for track */
    private void setAlbumCover(Song song){
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int slideWidth = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);
        if(song.getAlbumCover()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(song.getAlbumCover(), 0, song.getAlbumCover().length);
            albumCoverImg.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), bitmap)); //associated cover art in bitmap
            albumCoverImg.setAdjustViewBounds(true);
            albumCoverImg.setVisibility(View.VISIBLE);
            albumCoverImg.setAlpha(0.5f);
            layout.findViewById(R.id.album_covers).getLayoutParams().height = slideWidth;
            ((ViewGroup.MarginLayoutParams)layout.findViewById(R.id.song_info).getLayoutParams()).topMargin
                    = (int) (slideWidth-layout.findViewById(R.id.song_info).getLayoutParams().height - 100*scale);
        } else {
            setNoAlbumCover();
        }
    }

    /** If track has no image - set default image*/
    private void setNoAlbumCover(){
        int slideWidth = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);
        final float scale = ctx.getResources().getDisplayMetrics().density;
        albumCoverImg.setBackgroundResource(R.drawable.no_album_cover); //associated cover art in bitmap
        albumCoverImg.setAdjustViewBounds(true);
        albumCoverImg.setVisibility(View.VISIBLE);
        albumCoverImg.setAlpha(0.5f);

        layout.findViewById(R.id.album_covers).getLayoutParams().height = slideWidth;
        ((ViewGroup.MarginLayoutParams)layout.findViewById(R.id.song_info).getLayoutParams()).topMargin
                = (int) (slideWidth-layout.findViewById(R.id.song_info).getLayoutParams().height - 100*scale);
    }

    /** Link on current play list handler */
    private View.OnClickListener getOnPlaylistClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity)ctx;
                Fragment currentFragment = mainActivity.getSupportFragmentManager().getFragments().get(mainActivity.getSupportFragmentManager().getBackStackEntryCount());
                if(!(currentFragment instanceof PlaylistContentFragment && ((PlaylistContentFragment) currentFragment).getCurrentPlaylist() == trackController.getPlaylist())){
                    ((MainActivity)ctx).getBeanContext().getViewChanger().showPlayListContent(trackController.getPlaylist());
                }
                mainActivity.getBeanContext().getViewChanger().closeSlide();
            }
        };
    }

    /** Seek bar click handler */
    private SeekBar.OnSeekBarChangeListener getProgressBarListener(){
        return new SeekBar.OnSeekBarChangeListener() {
            boolean changeProgressByHand=false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null) {
                    int newPosition = (mediaPlayer.getDuration() * i) / 100;
                    if (changeProgressByHand) {
                        mediaPlayer.seekTo(newPosition);
                    }
                    duration_past.setText(SongService.formatDuration(newPosition));
                    //mConstraintSet.setHorizontalBias(duration_past.getId(), (float)i/100);
                    //mConstraintSet.applyTo((ConstraintLayout) layout);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                changeProgressByHand = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changeProgressByHand = false;
            }
        };
    }

    /** Initialize sound levels views*/
    private void initEq(){
        equilizerService.register(eq.findViewById(R.id.eq1));
        equilizerService.register(eq.findViewById(R.id.eq2));
        equilizerService.register(eq.findViewById(R.id.eq3));
        equilizerService.register(eq.findViewById(R.id.eq4));
        equilizerService.register(eq.findViewById(R.id.eq5));
        equilizerService.register(eq.findViewById(R.id.eq6));
        equilizerService.register(eq.findViewById(R.id.eq7));
        equilizerService.register(eq.findViewById(R.id.eq8));
        equilizerService.register(eq.findViewById(R.id.eq9));
        equilizerService.register(eq.findViewById(R.id.eq10));
    }

    /** Track end handler */
    private MediaPlayer.OnCompletionListener getOnCompletionListener(){
        return new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                //playNextSong();
                trackController.playNextSong();
            }
        };
    }

    /** Run seek bar updating in another thread */
    public void runProgressBarUpdater(){
        if(progressBarUpdater!=null) progressBarUpdater.interrupt();
        progressBarUpdaterStop = false;
        progressBarUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!progressBarUpdaterStop) {
                    if (mediaPlayer != null) {
                        final float trackProgress = (float) mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();

                        /** Android documentation says that views must be updated from main thread either like this */
                        progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((int) (trackProgress*100));
                            }
                        });
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        progressBarUpdater.start();
    }

    /** Stop and start new Visualizer */
    private void runVisualizer(int sessionId){
        offVisualizer();
        int rate = Visualizer.getMaxCaptureRate();
        visualizer = new Visualizer(sessionId);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                equilizerService.updatePillars(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        },rate , true, false); // waveform not freq data
        onVisualizer();
    }

    public void onVisualizer(){
        if(isVisualizerOn) {
            if (visualizer != null) {
                if (!visualizer.getEnabled()) visualizer.setEnabled(true);
            }
        }
        //isVisualizerOn = true;
    }

    public void offVisualizer(){
        if(visualizer !=null){
            if(visualizer.getEnabled()) visualizer.setEnabled(false);
        }
        //isVisualizerOn = false;
    }

    public boolean isVisualizerOn() {
        return isVisualizerOn;
    }

    /** Is visualizer must be enabled - check in preferences*/
    public boolean loadIsVisualizerOn(){
        SharedPreferences sharedPref = ((MainActivity)ctx).getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("Sound levels onOff", true);
    }

    /** On/off visualizer enabling and write it in preferences*/
    public void switchVisualizer(){
        SharedPreferences sharedPref = ((MainActivity)ctx).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Sound levels onOff", !isVisualizerOn);
        editor.commit();
        isVisualizerOn = !isVisualizerOn;
        if(!isVisualizerOn)
            offVisualizer();
    }


    public void onDestroy(){
        this.offVisualizer();
        if(visualizer !=null)
            this.visualizer.release();
        //this.trackControllerAdapter.visualizer = null;
        this.progressBarUpdater.interrupt();
        this.progressBarUpdaterStop = true;
        this.equilizerService.waterfallStop=true;
        this.equilizerService.changePillars(new ArrayList<View>());
        //this.trackControllerAdapter.equilizerService = null;
    }
}
