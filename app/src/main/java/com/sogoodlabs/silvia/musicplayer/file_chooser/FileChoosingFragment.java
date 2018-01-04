package com.sogoodlabs.silvia.musicplayer.file_chooser;


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
import android.widget.TextView;

import com.sogoodlabs.silvia.musicplayer.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 17.08.2017.
 */

public class FileChoosingFragment extends Fragment {

    ListView listView;
    List<String> paths;

    View.OnClickListener okButtonCallback;
    private HashMap<String, Boolean> extensionsToFilter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String rootDir = Environment.getExternalStorageDirectory().toString();
        paths = getArguments().getStringArrayList("paths");
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.file_choosing_layout, container, false);
        listView = ll.findViewById(R.id.fileChoosingList);
        TextView currentPath = ll.findViewById(R.id.current_dir_path);
        final FilesListAdapter filesListAdapter = new FilesListAdapter(inflater.getContext(), rootDir, paths, extensionsToFilter, currentPath);
        listView.setAdapter(filesListAdapter);

        Button buttonForChosingFiles =  ll.findViewById(R.id.backToMain);
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(okButtonCallback !=null) okButtonCallback.onClick(v);
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

    /** Callback when exiting files chooser */
    public void setOkButtonCallback(View.OnClickListener okButtonCallback) {
        this.okButtonCallback = okButtonCallback;
    }

    public void setExtensionsToFilter(HashMap<String, Boolean> extensionsToFilter) {
        this.extensionsToFilter = extensionsToFilter;
    }
}
