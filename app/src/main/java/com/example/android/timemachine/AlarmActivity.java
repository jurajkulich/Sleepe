package com.example.android.timemachine;

import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class AlarmActivity extends AppCompatActivity  implements AddAlarmFragment.AlarmInterface{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Box<AlarmSettings> alarmBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        alarmBox = boxStore.boxFor(AlarmSettings.class);

        // TextView for showing time
        final TextView timeShow = (TextView) findViewById(R.id.time_text_view_id);

        // FAB for adding alarm
        FloatingActionButton addAlarmIcon = (FloatingActionButton) findViewById(R.id.add_alarm_fab);

        mRecyclerView = (RecyclerView) findViewById(R.id.alarms_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AlarmAdapter(alarmBox.getAll());
        mRecyclerView.setAdapter(mAdapter);


        // Showing and updating time
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

    public void setAlarmSettings(AlarmSettings settings)
    {
        alarmBox.put(settings);

        mAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Added with id: " + settings.getAlarmID(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide the status and action bar onResume
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();
    }
}

/*


    public void setAlarmSettings(AlarmSettings settings)
    {
        // Add new alarm to database and adapter
        long id = db.addSettings(settings);
        settings.setAlarmID(id);
        Toast.makeText(getBaseContext(), "Alarm ID: " + id , Toast.LENGTH_SHORT).show();
        customAdapter.clear();
        customAdapter.addAll(db.getAllAlarmTimes());
        customAdapter.notifyDataSetChanged();
        //StartAlarmActivty(settings);
    }
 */