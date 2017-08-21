package com.example.alexander.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.alexander.musicplayer.entities.Playlist;

/**
 * Created by Alexander on 16.08.2017.
 */

public class TrackControllerAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    MediaPlayer mediaPlayer;
    View layout;
    Playlist playlist;
    public int currentTrackNumber =0;
    SeekBar progressBar;
    Thread progressBarUpdater;

    TrackControllerAdapter(Context ctx){
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initMainControlButtons();
        setupProgressBarUpdater();
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
//        if(layout==null) {
//
//        }
        return layout;
    }

    private void initMainControlButtons(){
        layout = lInflater.inflate(R.layout.track_controller, null, false);
        layout.findViewById(R.id.playButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(mediaPlayer!=null && !mediaPlayer.isPlaying()){
                            mediaPlayer.start();
                        }
                    }
                }
        );

        layout.findViewById(R.id.pauseButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                    }
                }
        );

        layout.findViewById(R.id.nextButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        playNextSong();
                    }
                }
        );
        layout.findViewById(R.id.prevButton).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        playSong(--currentTrackNumber);
                    }
                }
        );

        progressBar = layout.findViewById(R.id.seekBar);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });

    }

    private MediaPlayer.OnCompletionListener getOnCompletionListener(){
        return new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                playNextSong();
            }
        };
    }

    public void playSong(int i){
        currentTrackNumber = i;
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(ctx, Uri.parse(playlist.getTracks().get(currentTrackNumber)));
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(getOnCompletionListener());
        mediaPlayer.start();
    }

    public void playNextSong(){
        playSong(++currentTrackNumber);
    }

    private void setupProgressBarUpdater(){
        progressBarUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
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

    public Playlist getPlaylist() {
        return playlist;
    }
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
