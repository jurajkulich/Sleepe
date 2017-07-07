package com.example.android.timemachine;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddAlarmFragment.AlarmInterface {

    private ListView alarmListView;
    private AlarmDatabaseHandler db;
    private List<String> AlarmSettingsList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    CustomAlarmAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, KeepActiveService.class));
        setContentView(R.layout.activity_main);

        // Hide the status and action bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        db = new AlarmDatabaseHandler(this);
        // db.clearDatabase();
        alarmListView = (ListView) findViewById(R.id.show_alarms_list_view);
        AlarmSettingsList = db.getAllAlarmTimes();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AlarmSettingsList);
        customAdapter = new CustomAlarmAdapter(this, AlarmSettingsList, db);
        alarmListView.setAdapter(customAdapter);

        final ImageView addAlarmIcon = (ImageView) findViewById(R.id.add_alarm_icon);

        // TextView for showing time
        final TextView timeShow = (TextView) findViewById(R.id.time_text_view_id);

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

        // Show fragment by clicking on addAlarm Icon
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

    @Override
    protected void onResume() {
        super.onResume();
        // Hide the status and action bar onResume
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();
    }

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


}


