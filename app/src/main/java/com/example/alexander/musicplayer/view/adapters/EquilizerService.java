package com.example.alexander.musicplayer.view.adapters;

import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 23.08.2017.
 */

public class EquilizerService {

    List<ProgressBar> pillars = new ArrayList<>();
    Thread waterfallThread;
    public boolean waterfallStop = false;
    int span = 0;
    static HashMap<Byte, Integer> alignedValues = new HashMap<>();
    static {
        for(byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++){
            alignedValues.put(i, (int) (((float)(i+128)/255) * 100));
        }
        alignedValues.put((byte)127, 100);
    }
//    final ExecutorService es = Executors.newFixedThreadPool(100);

    public EquilizerService(){
        waterfallThread =  createNewWaterfallThread();
        waterfallThread.start();
    }

    private Thread createNewWaterfallThread(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(!waterfallStop){
                    for(int i = 0; i<pillars.size(); i++){
                        final ProgressBar pb =  pillars.get(i);
                        final int newVal = pb.getProgress()-2;
//                        pb.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(newVal>0) pb.setProgress(newVal);
//                                else pb.setProgress(0);
//                            }
//                        });
                        // Sorry, but this works better
                        if(newVal>0) pb.setProgress(newVal);
                        else pb.setProgress(0);
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void changePillars(List<View> pillars){
        this.pillars.clear();
        for(View pillar : pillars)
            register(pillar);
    }

    public void register(View view){
        if(view instanceof ProgressBar && !pillars.contains(view)) {
            pillars.add((ProgressBar) view);
            span = 1024/pillars.size();
        }
    }

    public void updatePillars(byte[] waveform){
        int j=0;
        for(int i = 0; i<pillars.size(); i++){
            //changeVal(pillars.get(i),mean(waveform, j, j+span-1));
            changeVal(pillars.get(i), transformWaveCached(mean(waveform, j, j+span-1)));
            j = j + span;
        }
    }

    public void changeVal(final View view, final int newVal){
        final ProgressBar pb = (ProgressBar) view;
        if(pb.getProgress()<newVal){
            pb.setProgress(newVal);
//            pb.post(new Runnable() {
//                @Override
//                public void run() {
//                    pb.setProgress(newVal);
//                }
//            });
        }
    }

    private byte mean(byte[] bytes, int start, int end){
        int resultInt = 0;
        for(int i = start; i<end; i++){
            resultInt = resultInt+bytes[i];
        }
        return (byte) (resultInt/(start-end));
    }

    private int transformWaveCached(byte b){
        return alignedValues.get(b);
        //return (int) (((float)(b+128)/255) * 100);
    }
    private int transformWave(int b){
        return (int) (((float)(b+128)/255) * 100);
    }


    public void effect1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(View pillar : pillars){
                    ((ProgressBar) pillar).setProgress(100);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
