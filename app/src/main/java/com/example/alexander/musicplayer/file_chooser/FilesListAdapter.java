package com.example.alexander.musicplayer.file_chooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.alexander.musicplayer.R;
import com.example.alexander.musicplayer.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 16.08.2017.
 */

public class FilesListAdapter extends BaseAdapter {

    List<File> files;
    Context ctx;
    LayoutInflater ltInflater;
    static String forbiddenDirectoryLevel = "/storage";
    List<String> paths;
    String path;
    boolean firstElementIsPrevFolder = false;


    public FilesListAdapter(Context ctx, String path, List<String> filePaths){
        this.ctx = ctx;
        ltInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paths = filePaths;
        this.path = path;
        updateFilesToShow(new File(path));
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int i) {
        return files.get(i);
    }

    @Override
    public long getItemId(int i) {
        return files.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i==0 && firstElementIsPrevFolder && files.get(0)!=null)
            return createDirWidget(files.get(i), "...");

        if(files.get(i).isDirectory()) return createDirWidget(files.get(i));
        else return createFileWidget(files.get(i));
    }

    View createFileWidget(final File file){
        CheckBox cb = (CheckBox) ltInflater.inflate(R.layout.file_chooser_checkbox, null, false);
        cb.setText(file.getName());
        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(paths.contains(file.getAbsolutePath())) paths.remove(file.getAbsolutePath());
                else paths.add(file.getAbsolutePath());
            }
        });
        return cb;
    }

    View createDirWidget(final File file, String text){
        TextView txv = (TextView) ltInflater.inflate(R.layout.file_chooser_dir, null, false);
        txv.setText(text);
        txv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //updateFilesToShow(FileUtils.filesInDirectoryHMap(file.getAbsolutePath()), file.getParentFile().getAbsolutePath());
                updateFilesToShow(file);
                notifyDataSetChanged();
            }
        });
        return txv;
    }

    View createDirWidget(File file){
        return createDirWidget(file, file.getName());
    }

    void updateFilesToShow(File path) {
        ArrayList<File> filesInNewDir = new ArrayList<File>();
        if (path.getParentFile()!=null && !path.getParentFile().getAbsolutePath().equals(forbiddenDirectoryLevel)) {
//            View prevDirView = createDirWidget(new File(prevDir), "...");
//            checkBoxesLL.addView(prevDirView);
            filesInNewDir.add(path.getParentFile());
            firstElementIsPrevFolder = true;
        } else firstElementIsPrevFolder = false;


        //filesInNewDir.addAll(FileUtils.filesInDirectory(path.getAbsolutePath()));
        HashMap<String, List<File>> filesAndDirs = FileUtils.filesInDirectoryHMap(path.getAbsolutePath());
        filesInNewDir.addAll(filesAndDirs.get("dirs"));
        filesInNewDir.addAll(filesAndDirs.get("files"));

        files = filesInNewDir;
        //this.files = new ArrayList<File>();
    }

}
