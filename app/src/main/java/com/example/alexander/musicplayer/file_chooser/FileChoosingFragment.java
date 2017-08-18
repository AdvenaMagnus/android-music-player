package com.example.alexander.musicplayer.file_chooser;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 17.08.2017.
 */

public class FileChoosingFragment extends Fragment {

    ListView listView;
    List<String> paths;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if(paths==null) paths = new ArrayList<String>();
        //else {paths.clear();}

        String rootDir = Environment.getExternalStorageDirectory().toString();
//        updateFilesToShow(FileUtils.filesInDirectoryHMap(rootDir), "");

        paths = getArguments().getStringArrayList("paths");

        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.file_choosing_layout, container, false);
        listView = ll.findViewById(R.id.fileChoosingList);
        listView.setAdapter(new FilesListAdapter(inflater.getContext(), rootDir, paths));

        Button buttonForChosingFiles =  ll.findViewById(R.id.backToMain);
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //Intent intent = new Intent();
                        //intent.putStringArrayListExtra("name", (ArrayList<String>) paths);
//                        setResult(RESULT_OK, intent);
//                        finish();
                        //((MainActivity)inflater.getContext()).showPlayLists(paths);
                        ((FragmentActivity)inflater.getContext()).getSupportFragmentManager().popBackStack();
                    }
                }
        );

        return ll;
    }

//    public void setPaths(List<String> paths) {
//        this.paths = paths;
//    }
}
