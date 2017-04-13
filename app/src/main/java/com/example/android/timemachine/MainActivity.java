package com.example.android.timemachine;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements AddAlarmFragment.AlarmInterface {

    AlarmSettings mAlarmSettings;

    public void setAlarmSettings(AlarmSettings settings)
    {
        Toast.makeText(getBaseContext(), settings.getAlarmHour() + ":" + settings.getAlarmMinute(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        getSupportActionBar().hide();


        /* ActionBar initialization
        ActionBar actionBar = getSupportActionBar();
        // Hide ActionBar Title
        actionBar.setDisplayShowTitleEnabled(false);
        */

        //final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.add_alarm_in_anim);

        final ImageView addAlarmIcon = (ImageView) findViewById(R.id.add_alarm_icon);

        // TextView for showing time
        final TextView timeShow = (TextView) findViewById(R.id.time_text_view_id);


        timeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Showing and updating time
        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeShow.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                handler.postDelayed(this, 1000);
            }
        }, 10);

        // Add Alarm Icon + On click
        addAlarmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.add_alarm_in_anim, R.anim.add_alarm_out_anim, R.anim.add_alarm_in_anim, R.anim.add_alarm_out_anim);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(android.R.id.content, new AddAlarmFragment()).commit();


            }

        });
    }

}
