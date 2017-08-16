package com.example.alexander.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.musicplayer.file_chooser.FileChosingActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] plLists = { "Play list 1", "Play list 2", "Play list 3", "Play list 4", "Play list 5",
            "Play list 6", "Play list 7", "Play list 8", "Play list 9", "Play list 10",
            "Play list 11", "Play list 12", "Play list 13", "Play list 14", "Play list 15"};

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final TextView textView = (TextView) findViewById(R.id.text1);

        Button buttonForChosingFiles = (Button)  findViewById(R.id.buttonChoseFiles);
        buttonForChosingFiles.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        textView.setText("Files chose");

                        Intent intent = new Intent(MainActivity.this, FileChosingActivity.class);
                        //startActivity(intent);
                        startActivityForResult(intent, 1);
                    }
                }
        );

        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Arrays.asList(plLists));

        lvMain.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new TrackControllerAdapter(this));
        //mDrawerLayout =  getLayoutInflater().inflate(R.layout.slide_menu_activity, null).findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle("Title");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Title");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerLayout.closeDrawers();
        mDrawerToggle.syncState();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        List<String> paths = data.getStringArrayListExtra("name");
        StringBuilder pathsSB = new StringBuilder();
        for(String s: paths){
            pathsSB.append(s);
            pathsSB.append(" ");
        }
        ((TextView) findViewById(R.id.text1)).setText(pathsSB.toString());
        System.out.println(paths);
    }

    /* Called whenever we call invalidateOptionsMenu() */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }


}
