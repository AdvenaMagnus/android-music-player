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
        setContentView(R.layout.activity_file_chosing);

        //checkBoxesLL = (LinearLayout) findViewById(R.id.checkboxesLayout);

        String rootDir = Environment.getExternalStorageDirectory().toString();
//        updateFilesToShow(FileUtils.filesInDirectoryHMap(rootDir), "");

        listView = (ListView) findViewById(R.id.fileChoosingList);
        listView.setAdapter(new FilesListAdapter(this, rootDir, paths));

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

//    void updateFilesToShow(HashMap<String, List<File>> files, String prevDir) {
//        checkBoxesLL.removeAllViews();
//        if (!prevDir.isEmpty() && !prevDir.equals(forbiddenDirectoryLevel)) {
//            View prevDirView = createDirWidget(new File(prevDir), "...");
//            checkBoxesLL.addView(prevDirView);
//        }
//        for (File file : files.get("dirs")) {
//            checkBoxesLL.addView(createDirWidget(file));
//        }
//        for (File file : files.get("files")) {
//            checkBoxesLL.addView(createFileWidget(file));
//        }
//    }
//
//    View createFileWidget(final File file){
//        //CheckBox cb = new CheckBox(this);
//        LayoutInflater ltInflater = getLayoutInflater();
//        CheckBox cb = (CheckBox) ltInflater.inflate(R.layout.file_chooser_checkbox, null, false);
//        cb.setText(file.getName());
//        cb.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if(paths.contains(file.getAbsolutePath())) paths.remove(file.getAbsolutePath());
//                else paths.add(file.getAbsolutePath());
//            }
//        });
//        return cb;
//    }
//
//    View createDirWidget(final File file, String text){
//        //TextView txv = new TextView(this);
//        LayoutInflater ltInflater = getLayoutInflater();
//        TextView txv = (TextView) ltInflater.inflate(R.layout.file_chooser_dir, null, false);
//        txv.setText(text);
//        txv.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                updateFilesToShow(FileUtils.filesInDirectoryHMap(file.getAbsolutePath()), file.getParentFile().getAbsolutePath());
//            }
//        });
//        return txv;
//    }
//
//    View createDirWidget(File file){
//        return createDirWidget(file, file.getName());
//    }

}
