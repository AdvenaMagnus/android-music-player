package com.example.alexander.musicplayer.view.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.SongService;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.TrackObserver;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexander on 16.08.2017.
 */

public class TrackControllerAdapter extends BaseAdapter{

    public Context ctx;
    private LayoutInflater lInflater;
    private static MediaPlayer mediaPlayer;
    public EquilizerService equilizerService;
    public Visualizer audioOutput;
    private View layout;
    private View eq;
    private SeekBar progressBar;
    public Thread progressBarUpdater;
    public boolean progressBarUpdaterStop=false;
    private Button playButton;
    private TextView fileName;
    private TextView duration_past;
    private boolean isVisualizerOn = true;

    private TrackController trackController;

    public TrackControllerAdapter(final Context ctx, TrackController trackController){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        equilizerService = new EquilizerService();
        isVisualizerOn = loadIsVisualizerOn();
        this.trackController = trackController;
        this.trackController.registerStartRunningSongObserver("trackControllerAdapter", new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                if(mediaPlayer!=null){
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song.getPath()));
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(getOnCompletionListener());
                setSongInfo(song);
                playButtonAction();
                runVisualizer(mediaPlayer.getAudioSessionId());
            }
        });

        this.trackController.registerPauseRunningSongObserverList("trackControllerAdapter", new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                playButtonAction();
            }
        });
        this.trackController.registerResumeRunningSongObserverList("trackControllerAdapter", new TrackObserver() {
            @Override
            public void update(int i, Song song) {
                playButtonAction();
            }
        });

        initMainControlButtons();
        initEq();
        runProgressBarUpdater();
        if(trackController!=null){
            if(trackController.getCurrentTrack()!=null)
                setSongInfo(trackController.getCurrentTrack());
        }
        if(mediaPlayer!=null)
            runVisualizer(mediaPlayer.getAudioSessionId());
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

    private void initMainControlButtons(){
        layout = lInflater.inflate(R.layout.track_controller, null, false);

        playButton = layout.findViewById(R.id.playButton2);
        playButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
//                        Animation shake = AnimationUtils.loadAnimation(ctx, R.anim.shake);
//                        v.startAnimation(shake);
                        playButtonAction();
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
    }

    private void setSongInfo(Song song){
        HashMap<String, String> metData = song.getMetadata();
        fileName.setText(song.getName());
        ((TextView)layout.findViewById(R.id.artist)).setText(metData.get("artist"));
        ((TextView)layout.findViewById(R.id.album)).setText(metData.get("album"));
        ((TextView)layout.findViewById(R.id.title)).setText(metData.get("title"));
        ((TextView)layout.findViewById(R.id.duration)).setText(metData.get("duration"));

        //int width = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);
        setAlbumCover(song);

        String playlistName=trackController.getPlaylist().getName();
        SpannableString content = new SpannableString(playlistName);
        content.setSpan(new UnderlineSpan(), 0, playlistName.length(), 0);
        ((TextView)layout.findViewById(R.id.playlist)).setText(content);
    }

    private void setAlbumCover(Song song){
        ImageView albumCoverImg =  layout.findViewById(R.id.album_cover);
        //ImageView albumCoverImg2 =  layout.findViewById(R.id.album_gradient);
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int slideWidth = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);
        if(song.getAlbumCover()!=null){

//            layout.findViewById(R.id.album_covers).getLayoutParams().height = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);

            Bitmap bitmap = BitmapFactory.decodeByteArray(song.getAlbumCover(), 0, song.getAlbumCover().length);
            //albumCoverImg.setImageBitmap(bitmap); //associated cover art in bitmap
            albumCoverImg.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), bitmap)); //associated cover art in bitmap
            albumCoverImg.setAdjustViewBounds(true);
            //albumCoverImg.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
            albumCoverImg.setVisibility(View.VISIBLE);
            albumCoverImg.setAlpha(0.5f);

            //layout.findViewById(R.id.album_covers).getLayoutParams().height = (int) (bitmap.getHeight()/scale);
            layout.findViewById(R.id.album_covers).getLayoutParams().height = slideWidth;
            ((ViewGroup.MarginLayoutParams)layout.findViewById(R.id.song_info).getLayoutParams()).topMargin
                    = (int) (slideWidth-layout.findViewById(R.id.song_info).getLayoutParams().height - 100*scale);
            //albumCoverImg2.getLayoutParams().height = albumCoverImg.getLayoutParams().height;
            //albumCoverImg2.getLayoutParams().height = (int) (ctx.getResources().getDisplayMetrics().widthPixels*0.8f);

        } else {
            albumCoverImg.setVisibility(View.INVISIBLE);
            albumCoverImg.setAlpha(0.5f);
            layout.findViewById(R.id.album_covers).getLayoutParams().height = slideWidth;
            ((ViewGroup.MarginLayoutParams)layout.findViewById(R.id.song_info).getLayoutParams()).topMargin
                    = (int) (slideWidth-layout.findViewById(R.id.song_info).getLayoutParams().height - 100*scale);

            //albumCoverImg2.getLayoutParams().height = albumCoverImg.getLayoutParams().height;
        }
    }

    private View.OnClickListener getOnPlaylistClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)ctx).showPlayListContent(trackController.getPlaylist());
            }
        };
    }

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

    private MediaPlayer.OnCompletionListener getOnCompletionListener(){
        return new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                //playNextSong();
                trackController.playNextSong();
            }
        };
    }

    public void runProgressBarUpdater(){
        if(progressBarUpdater!=null) progressBarUpdater.interrupt();
        progressBarUpdaterStop = false;
        progressBarUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!progressBarUpdaterStop) {
                    if (mediaPlayer != null) {
                        final float trackProgress = (float) mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();
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

    private void runVisualizer(int sessionId){
        offVisualizer();
        int rate = Visualizer.getMaxCaptureRate();
        audioOutput = new Visualizer(sessionId);
        audioOutput.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
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
            if (audioOutput != null) {
                if (!audioOutput.getEnabled()) audioOutput.setEnabled(true);
            }
        }
        //isVisualizerOn = true;
    }

    public void offVisualizer(){
        if(audioOutput!=null){
            if(audioOutput.getEnabled()) audioOutput.setEnabled(false);
        }
        //isVisualizerOn = false;
    }

    public boolean isVisualizerOn() {
        return isVisualizerOn;
    }

    public boolean loadIsVisualizerOn(){
        SharedPreferences sharedPref = ((MainActivity)ctx).getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("Sound levels onOff", true);
    }

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
        if(audioOutput!=null)
            this.audioOutput.release();
        //this.trackControllerAdapter.audioOutput = null;
        this.progressBarUpdater.interrupt();
        this.progressBarUpdaterStop = true;
        this.equilizerService.waterfallStop=true;
        this.equilizerService.changePillars(new ArrayList<View>());
        //this.trackControllerAdapter.equilizerService = null;
    }
}
