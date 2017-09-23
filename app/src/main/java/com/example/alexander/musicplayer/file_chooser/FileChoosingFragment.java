package com.example.alexander.musicplayer.file_chooser;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 17.08.2017.
 */

public class FileChoosingFragment extends Fragment {

    ListView listView;
    List<String> paths;

    View.OnClickListener callback;
    private HashMap<String, Boolean> extensionsToFilter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String rootDir = Environment.getExternalStorageDirectory().toString();
        paths = getArguments().getStringArrayList("paths");
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.file_choosing_layout, container, false);
        listView = ll.findViewById(R.id.fileChoosingList);
        final FilesListAdapter filesListAdapter = new FilesListAdapter(inflater.getContext(), rootDir, paths, extensionsToFilter);
        listView.setAdapter(filesListAdapter);

        Button buttonForChosingFiles =  ll.findViewById(R.id.backToMain);
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(callback!=null) callback.onClick(v);
                        ((FragmentActivity)inflater.getContext()).getSupportFragmentManager().popBackStack();
                    }
                }
        );

        ll.findViewById(R.id.selectall).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        filesListAdapter.selectAll();
                    }
                }
        );

        return ll;
    }

//    public void setPaths(List<String> paths) {
//        this.paths = paths;
//    }

    public void setCallback(View.OnClickListener callback) {
        this.callback = callback;
    }

    public void setExtensionsToFilter(HashMap<String, Boolean> extensionsToFilter) {
        this.extensionsToFilter = extensionsToFilter;
    }
}
