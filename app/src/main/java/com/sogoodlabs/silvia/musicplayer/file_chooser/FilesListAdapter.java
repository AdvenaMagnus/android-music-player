package com.sogoodlabs.silvia.musicplayer.file_chooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sogoodlabs.silvia.musicplayer.R;
import com.sogoodlabs.silvia.musicplayer.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 16.08.2017.
 */

public class FilesListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater ltInflater;
    private List<File> files;
    private List<String> paths;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private String rootPath;
    private boolean firstElementIsPrevFolder = false;
    private HashMap<String, Boolean> extensionsToFilter; // Permitted files
    private TextView currentPathTextView;

    public FilesListAdapter(Context ctx, String path, List<String> filePaths, HashMap<String, Boolean> extensionsToFilter, TextView currentPath){
        this.extensionsToFilter = extensionsToFilter;
        this.ctx = ctx;
        ltInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paths = filePaths;
        this.rootPath = path;
        this.currentPathTextView = currentPath;
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

    /** Create list item for file */
    View createFileWidget(final File file){
        CheckBox cb = (CheckBox) ltInflater.inflate(R.layout.file_chooser_checkbox, null, false);
        cb.setText(file.getName());
        cb.setChecked(paths.contains(file.getAbsolutePath()));
        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(paths.contains(file.getAbsolutePath())) paths.remove(file.getAbsolutePath());
                else paths.add(file.getAbsolutePath());
            }
        });
        checkBoxes.add(cb);
        return cb;
    }

    /** Create list item for directory */
    View createDirWidget(final File file, String text){
        LinearLayout ll = (LinearLayout) ltInflater.inflate(R.layout.file_chooser_dir, null, false);
        TextView txv = ll.findViewById(R.id.text_for_directory);
        txv.setText(text);
        ll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //updateFilesToShow(FileUtils.filesInDirectoryHMap(file.getAbsolutePath()), file.getParentFile().getAbsolutePath());
                updateFilesToShow(file);
                notifyDataSetChanged();
            }
        });
        return ll;
    }

    View createDirWidget(File file){
        return createDirWidget(file, file.getName());
    }

    /** Open (and show) a folder */
    void updateFilesToShow(File path) {
        ArrayList<File> filesInNewDir = new ArrayList<File>();
        createOnLevelUpLinkIfNeed(filesInNewDir, path);
        //filesInNewDir.addAll(FileUtils.filesInDirectory(rootPath.getAbsolutePath()));
        HashMap<String, List<File>> filesAndDirs = FileUtils.filesInDirectoryHMap(path.getAbsolutePath());
        filesInNewDir.addAll(filesAndDirs.get("dirs"));

        if(extensionsToFilter!=null) {
            for(File file : filesAndDirs.get("files")){
                if(extensionsToFilter.get(FileUtils.getExtension(file.getAbsolutePath()))!=null)
                    filesInNewDir.add(file);
            }
        } else filesInNewDir.addAll(filesAndDirs.get("files"));
        files = filesInNewDir;
        if(checkBoxes!=null)checkBoxes.clear();
        this.showCurrentPath(path);
    }

    /** If not a root path - create level up link */
    private void createOnLevelUpLinkIfNeed(ArrayList<File> filesInNewDir, File path){
        if (!path.getAbsolutePath().equals(rootPath)) {
            filesInNewDir.add(path.getParentFile());
            firstElementIsPrevFolder = true;
        } else firstElementIsPrevFolder = false;
    }

    /** Select all files */
    public void selectAll(){
        for(File file : files){
            if(!paths.contains(file.getAbsolutePath()) && !file.isDirectory()) paths.add(file.getAbsolutePath());
        }

        for(CheckBox cb : checkBoxes){
            cb.setChecked(true);
        }
    }

    /** Show/update current path*/
    private void showCurrentPath(File folder){
        currentPathTextView.setText(folder.getAbsolutePath());
    }

}
