package com.sogoodlabs.fileschooser.configuration;

import com.sogoodlabs.fileschooser.view.FilesChooserViewAPI;

import java.util.HashMap;

/**
 * Created by Alexander on 05.01.2018.
 */

public class FilesChooserConfiguration {

    private HashMap<String, Boolean> extensionsToFilter; // Permitted files
    FilesChooserViewAPI viewAPI;
    String rootDir;

    public FilesChooserConfiguration(HashMap<String, Boolean> extensionsToFilter, FilesChooserViewAPI viewAPI, String rootDir){
        this.extensionsToFilter = extensionsToFilter;
        this.viewAPI = viewAPI;
        this.rootDir = rootDir;
    }

    public HashMap<String, Boolean> getExtensionsToFilter() {
        return extensionsToFilter;
    }

    public void setExtensionsToFilter(HashMap<String, Boolean> extensionsToFilter) {
        this.extensionsToFilter = extensionsToFilter;
    }

    public FilesChooserViewAPI getViewAPI() {
        return viewAPI;
    }

    public void setViewAPI(FilesChooserViewAPI viewAPI) {
        this.viewAPI = viewAPI;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
}
