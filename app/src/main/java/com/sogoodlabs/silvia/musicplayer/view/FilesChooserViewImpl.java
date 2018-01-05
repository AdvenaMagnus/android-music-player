package com.sogoodlabs.silvia.musicplayer.view;

import com.sogoodlabs.fileschooser.FilesChooserViewImplDeafault;
import com.sogoodlabs.silvia.musicplayer.R;

/**
 * Created by Alexander on 05.01.2018.
 */

public class FilesChooserViewImpl extends FilesChooserViewImplDeafault {

    @Override
    public int fileChoosingLayout() {
        return R.layout.file_choosing_layout;
    }

    @Override
    public int listView() {
        return com.sogoodlabs.fileschooser.R.id.fileChoosingList;
    }

    @Override
    public int currentDirPathTextView() {
        return com.sogoodlabs.fileschooser.R.id.current_dir_path;
    }

    @Override
    public int okButton() {
        return com.sogoodlabs.fileschooser.R.id.backToMain;
    }

    @Override
    public int selectAllButton() {
        return com.sogoodlabs.fileschooser.R.id.selectall;
    }

    @Override
    public int fileCheckBox() {
        return com.sogoodlabs.fileschooser.R.layout.file_chooser_checkbox_default;
    }

    @Override
    public int directoryLayout() {
        return com.sogoodlabs.fileschooser.R.layout.file_chooser_dir_default;
    }

    @Override
    public int directoryTextView() {
        return com.sogoodlabs.fileschooser.R.id.text_for_directory;
    }
}
