package com.example.android.timemachine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class AlarmActivity extends AppCompatActivity  implements AddAlarmFragment.AlarmInterface {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlarmManager mAlarmManager;
    private Box<AlarmSettings> alarmBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set custom toolbar with Title - Name
        Toolbar toolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alarmino");

        // Add icons to toolabr
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set CTL title to white
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.alarm__collaps_toolbar);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        alarmBox = boxStore.boxFor(AlarmSettings.class);

        mAlarmManager = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);

        // FAB for adding alarm
        FloatingActionButton addAlarmIcon = (FloatingActionButton) findViewById(R.id.add_alarm_fab);

        mRecyclerView = (RecyclerView) findViewById(R.id.alarms_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AlarmAdapter(alarmBox.getAll(), new RowSwitchClickListener() {
            @Override
            public void onSwitch(boolean status, long  position, AlarmSettings alarmSettings) {
                // Toast.makeText(getApplicationContext(), "Switch: " + Boolean.toString(status)+ " with id: " + position, Toast.LENGTH_SHORT).show();
                alarmSettings.setIsActive(status);
                alarmBox.put(alarmSettings);
                if( status == true)
                {
                    activeAlarm(alarmSettings);
                }
                else
                {
                    int request = (int)(long) alarmSettings.getAlarmID();
                    deactiveAlarm(request);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        /*
           TextView for showing time
           final TextView timeShow = (TextView) findViewById(R.id.time_text_view_id);
           Showing and updating time
        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            //pHour = cal.get(Calendar.HOUR_OF_DAY);
            //pMinute = cal.get(Calendar.MINUTE);
            @Override
            public void run() {
                timeShow.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                handler.postDelayed(this, 1000);
            }
        }, 10);
        */

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

    // Adds new alarm to database and updates RecyclerView
    public void setAlarmSettings(AlarmSettings settings)
    {
        alarmBox.put(settings);
        ((AlarmAdapter) mAdapter).update(alarmBox.getAll());
        //Toast.makeText(this, "Added with id: " + settings.getAlarmID(), Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.activity_alarm_coordinator_layout),"Added alarm: " + settings.getAlarmHour() + ":" + settings.getAlarmMinute(), Snackbar.LENGTH_SHORT ).show();
        activeAlarm(settings);
    }

    public void activeAlarm(AlarmSettings alarmSettings)
    {
        PendingIntent alarmIntent;
        Intent intent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
        intent.putExtra("ringtone", alarmSettings.getAlarmRingtone());
        intent.putExtra("vibration", alarmSettings.getAlarmvibration());
        int requestCode = (int)(long) alarmSettings.getId();
        alarmIntent = PendingIntent.getActivity(getApplicationContext(),requestCode , intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarmSettings.getAlarmHour());
        calendar.set(Calendar.MINUTE, alarmSettings.getAlarmMinute());
        calendar.set(Calendar.SECOND, 0);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void deactiveAlarm(int request)
    {
        Intent intent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),request , intent, 0);
        mAlarmManager.cancel(alarmIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide the status bar onResume
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }



}

