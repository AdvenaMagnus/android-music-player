package com.example.alexander.musicplayer.view.adapters;

import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 23.08.2017.
 */

public class EquilizerService {

    List<ProgressBar> pillars = new ArrayList<>();
    int span = 0;
    static int[] values = new int[255];
    static {
        for(int i = 0; i < values.length; i++){
            values[i] = transformWave(i);
        }
    }
//    final ExecutorService es = Executors.newFixedThreadPool(100);

    public EquilizerService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    for(int i = 0; i<pillars.size(); i++){
                        ProgressBar pb =  pillars.get(i);
                        int newVal = pb.getProgress()-2;
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
        }).start();
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
            changeVal(pillars.get(i), mean(waveform, j, j+span-1));
            j = j + span;
        }
    }

    public void changeVal(final View view, final int newVal){
        ProgressBar pb = (ProgressBar) view;
        if(pb.getProgress()<newVal) pb.setProgress(newVal);
    }

    private byte mean(byte[] bytes, int start, int end){
        int resultInt = 0;
        for(int i = start; i<end; i++){
            resultInt = resultInt+bytes[i];
        }
        return (byte) (resultInt/(start-end));
    }

    private int transformWave(byte b){
        return values[b];
        //return (int) (((float)(b+128)/255) * 100);
    }
    private static int transformWave(int b){
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
