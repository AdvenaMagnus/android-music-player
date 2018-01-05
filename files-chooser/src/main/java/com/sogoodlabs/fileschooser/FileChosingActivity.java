package com.sogoodlabs.fileschooser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FileChosingActivity extends AppCompatActivity {

    LinearLayout checkBoxesLL;
    ListView listView;
    static String forbiddenDirectoryLevel = "/storage";
    List<String> paths = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_choosing_layout_default);

        //checkBoxesLL = (LinearLayout) findViewById(R.id.checkboxesLayout);

        String rootDir = Environment.getExternalStorageDirectory().toString();
//        updateFilesToShow(FileUtils.filesInDirectoryHMap(rootDir), "");

        listView = (ListView) findViewById(R.id.fileChoosingList);
        listView.setAdapter(new FilesListAdapter(this, rootDir, paths, null, null, null));

        Button buttonForChosingFiles = (Button)  findViewById(R.id.backToMain);
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("name", (ArrayList<String>) paths);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }

}
