package com.sogoodlabs.fileschooser.view;

/**
 * Created by Alexander on 05.01.2018.
 */

/** Logic in files chooser interacts with view only through this api */
public interface FilesChooserViewAPI {

    int fileChoosingLayout();
    int listView();
    int currentDirPathTextView();
    int okButton();
    int selectAllButton();
    int fileCheckBox();
    int directoryLayout();
    int directoryTextView();

}
