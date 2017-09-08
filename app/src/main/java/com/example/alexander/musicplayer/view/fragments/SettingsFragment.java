package com.example.alexander.musicplayer.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.alexander.musicplayer.MainActivity;
import com.example.alexander.musicplayer.R;

/**
 * Created by Alexander on 06.09.2017.
 */

public class SettingsFragment extends DialogFragment {

    LinearLayout ll;
    MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(ll==null) {
            mainActivity = (MainActivity) inflater.getContext();
            ll = (LinearLayout) inflater.inflate(R.layout.settings, container, false);
            ll.findViewById(R.id.change_theme).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mainActivity.setTheme(R.style.AppThemeLight);
                    MainActivity.currentTheme = R.style.AppThemeLight;
                    //mainActivity.recreate();

                    TaskStackBuilder.create(getActivity())
                            .addNextIntent(new Intent(getActivity(), MainActivity.class))
                            .addNextIntent(getActivity().getIntent())
                            .startActivities();
                }
            });
        }
        return ll;
    }

}
