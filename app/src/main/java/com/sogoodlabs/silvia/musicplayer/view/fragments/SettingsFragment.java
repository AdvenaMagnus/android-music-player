package com.sogoodlabs.silvia.musicplayer.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sogoodlabs.silvia.musicplayer.MainActivity;
import com.sogoodlabs.silvia.musicplayer.R;
import com.sogoodlabs.silvia.musicplayer.view.Theme;

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
            ll.findViewById(R.id.change_theme).setOnClickListener(changeThemeButtonListener());
            ((TextView)ll.findViewById(R.id.theme_name)).setText(mainActivity.getBeanContext().getThemeService().getCurrentTheme().getName());

            ((Switch)ll.findViewById(R.id.sound_levels_switch)).setChecked(mainActivity.getTrackControllerAdapter().isVisualizerOn());
            ll.findViewById(R.id.sound_levels_switch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getTrackControllerAdapter().switchVisualizer();
                }
            });
            ll.findViewById(R.id.back).setOnClickListener(getBackButtonListener());
        }
        return ll;
    }

    private View.OnClickListener changeThemeButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getBeanContext().getThemeService().setCurrentTheme(mainActivity.getBeanContext().getThemeService().getCurrentTheme()==Theme.Dark? Theme.Light: Theme.Dark);
                TaskStackBuilder.create(getActivity())
                        .addNextIntent(new Intent(getActivity(), MainActivity.class))
                        .addNextIntent(getActivity().getIntent())
                        .startActivities();
            }
        };
    }

    /**Back button handler*/
    private View.OnClickListener getBackButtonListener(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        };
    }

}
