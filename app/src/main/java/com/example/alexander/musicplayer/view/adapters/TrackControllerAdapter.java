package com.example.alexander.musicplayer.view.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.controller.TrackController;
import com.example.alexander.musicplayer.controller.TrackObserver;
import com.example.alexander.musicplayer.model.entities.Song;

import java.util.ArrayList;
import java.util.List;

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
    private TextView songTitle;

    private TrackController trackController;

    public TrackControllerAdapter(final Context ctx, TrackController trackController){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        equilizerService = new EquilizerService();
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
                songTitle.setText(song.getName());
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

        songTitle = layout.findViewById(R.id.song_title);
        songTitle.setSelected(true);

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
                if(mediaPlayer!=null && changeProgressByHand){
                    int newPosition = (mediaPlayer.getDuration()*i)/100;
                    mediaPlayer.seekTo(newPosition);
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
                        float trackProgress = (float) mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();
                        progressBar.setProgress((int) (trackProgress*100));
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
        if(audioOutput!=null && audioOutput.getEnabled()){
            audioOutput.setEnabled(false);
        }
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
        audioOutput.setEnabled(true);
    }

    public void onVisualizer(){
        if(audioOutput!=null){
            if(!audioOutput.getEnabled()) audioOutput.setEnabled(true);
        }
    }

    public void offVisualizer(){
        if(audioOutput!=null){
            if(audioOutput.getEnabled()) audioOutput.setEnabled(false);
        }
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
