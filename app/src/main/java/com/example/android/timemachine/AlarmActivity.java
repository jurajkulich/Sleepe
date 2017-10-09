package com.example.android.timemachine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static com.example.android.timemachine.R.color.black;

public class AlarmActivity extends AppCompatActivity  implements AddAlarmFragment.AlarmInterface {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private AlarmManager mAlarmManager;
    private Box<AlarmSettings> alarmBox;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        // Hide status bar
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);

        // Set custom toolbar with Title - Name
        toolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sleepe");

        // Get base cover photo from drawable and convert it to bitmap
        Bitmap coverPhoto = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.night_sky);
        setStatusBarColor(coverPhoto);

        // Add icons to toolabr
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

            }
        });
        */

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

        mDrawerLayout = findViewById(R.id.my_drawer_layout);

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
        /*
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        */
    }

    // Open menu on menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    // Get swatch from bitmap
    public Palette.Swatch createPaletteSync(Bitmap bitmap)
    {
        Palette palette =  Palette.from(bitmap).generate();
        Palette.Swatch swatch = palette.getDominantSwatch();
        if( swatch != null)
            return swatch;
        else {
            swatch = palette.getLightVibrantSwatch();
            return swatch;
        }
    }

    // Set status bar color by cover photo
    public void setStatusBarColor(Bitmap bitmap)
    {
        Palette.Swatch swatch = createPaletteSync(bitmap);
        //toolbar.setBackgroundColor(swatch.getRgb());
        //toolbar.setTitleTextColor(swatch.getTitleTextColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(swatch.getRgb());
        }
    }



}

