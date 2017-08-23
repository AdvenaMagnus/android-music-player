package com.example.alexander.musicplayer.adapters;

import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 23.08.2017.
 */

public class EquilizerService {

    List<View> pillars = new ArrayList<>();
    //List<View> pillarsInversed = new ArrayList<>();

//    final ExecutorService es = Executors.newFixedThreadPool(100);

    public EquilizerService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    //for(View view : pillars.keySet()){
                    for(int i = 0; i<pillars.size(); i++){
                        ProgressBar pb = (ProgressBar) pillars.get(i);
                        //ProgressBar pb2 = (ProgressBar) pillarsInversed.get(i);
                        int newVal = pb.getProgress()-2;
                        if(newVal>0) pb.setProgress(newVal);
                        else pb.setProgress(0);

                        //pb2.setProgress(pb.getProgress());
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
        if(!pillars.contains(view)) {
            pillars.add(view);
        }
    }

    public void updatePillars(byte[] waveform){
        int j=0;
        int span = 1024/pillars.size();
        for(int i = 0; i<pillars.size(); i++){
            //((ProgressBar)view).setProgress(transformWave(mean(waveform, i, i+span-1)));
            changeVal(pillars.get(i), transformWave(mean(waveform, j, j+span-1)));
            //((ProgressBar)pillarsInversed.get(i)).setProgress(((ProgressBar)pillars.get(i)).getProgress());
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
        return (int) (((float)(b+128)/255) * 100);
    }

}
