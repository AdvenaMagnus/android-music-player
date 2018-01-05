package com.sogoodlabs.fileschooser;

/**
 * Created by Alexander on 05.01.2018.
 */

public class FilesChooserViewImplDeafault implements FilesChooserViewAPI {

    @Override
    public int fileChoosingLayout() {
        return R.layout.file_choosing_layout_default;
    }

    @Override
    public int listView() {
        return R.id.fileChoosingList;
    }

    @Override
    public int currentDirPathTextView() {
        return R.id.current_dir_path;
    }

    @Override
    public int okButton() {
        return R.id.backToMain;
    }

    @Override
    public int selectAllButton() {
        return R.id.selectall;
    }

    @Override
    public int fileCheckBox() {
        return R.layout.file_chooser_checkbox_default;
    }

    @Override
    public int directoryLayout() {
        return R.layout.file_chooser_dir_default;
    }

    @Override
    public int directoryTextView() {
        return R.id.text_for_directory;
    }
}
