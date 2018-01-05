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
    FilesChooserViewAPI viewAPI;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String rootDir = Environment.getExternalStorageDirectory().toString();
        paths = getArguments().getStringArrayList("paths");
        LinearLayout ll = (LinearLayout) inflater.inflate(viewAPI.fileChoosingLayout(), container, false);
        listView = ll.findViewById(viewAPI.listView());
        TextView currentPath = ll.findViewById(viewAPI.currentDirPathTextView());
        final FilesListAdapter filesListAdapter = new FilesListAdapter(inflater.getContext(), rootDir, paths, extensionsToFilter, currentPath, viewAPI);
        listView.setAdapter(filesListAdapter);

        Button buttonForChosingFiles =  ll.findViewById(viewAPI.okButton());
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(okButtonCallback !=null) okButtonCallback.onClick(v);
                        ((FragmentActivity)inflater.getContext()).getSupportFragmentManager().popBackStack();
                    }
                }
        );

        ll.findViewById(viewAPI.selectAllButton()).setOnClickListener(
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

    public void setViewAPI(FilesChooserViewAPI viewAPI) {
        this.viewAPI = viewAPI;
    }
}
