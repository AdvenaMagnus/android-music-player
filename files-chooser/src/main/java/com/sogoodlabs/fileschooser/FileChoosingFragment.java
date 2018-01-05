package com.sogoodlabs.fileschooser;


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

import com.sogoodlabs.fileschooser.configuration.FilesChooserConfiguration;
import com.sogoodlabs.fileschooser.view.FilesChooserViewAPI;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 17.08.2017.
 */

public class FileChoosingFragment extends Fragment {

    ListView listView;
    List<String> paths;

    View.OnClickListener okButtonCallback;
    FilesChooserConfiguration configuration;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String rootDir = Environment.getExternalStorageDirectory().toString();
        //paths = getArguments().getStringArrayList("paths");
        if(paths == null)
            throw new NullPointerException("FileChoosingFragment.paths must not be null");
        LinearLayout ll = (LinearLayout) inflater.inflate(configuration.getViewAPI().fileChoosingLayout(), container, false);
        listView = ll.findViewById(configuration.getViewAPI().listView());
        TextView currentPath = ll.findViewById(configuration.getViewAPI().currentDirPathTextView());
        final FilesListAdapter filesListAdapter = new FilesListAdapter(inflater.getContext(), paths, currentPath, configuration);
        listView.setAdapter(filesListAdapter);

        Button buttonForChosingFiles =  ll.findViewById(configuration.getViewAPI().okButton());
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(okButtonCallback !=null) okButtonCallback.onClick(v);
                        ((FragmentActivity)inflater.getContext()).getSupportFragmentManager().popBackStack();
                    }
                }
        );

        ll.findViewById(configuration.getViewAPI().selectAllButton()).setOnClickListener(
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

    public FilesChooserConfiguration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(FilesChooserConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
