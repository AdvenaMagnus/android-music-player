package com.example.alexander.musicplayer.file_chooser;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.alexander.musicplayer.R;

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
        setContentView(R.layout.file_choosing_layout);

        //checkBoxesLL = (LinearLayout) findViewById(R.id.checkboxesLayout);

        String rootDir = Environment.getExternalStorageDirectory().toString();
//        updateFilesToShow(FileUtils.filesInDirectoryHMap(rootDir), "");

        listView = (ListView) findViewById(R.id.fileChoosingList);
        listView.setAdapter(new FilesListAdapter(this, rootDir, paths, null, null));

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
