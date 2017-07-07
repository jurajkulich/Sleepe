package com.example.android.timemachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class AlarmRingingActivity extends SwipeBackActivity {

    private Ringtone r;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
        setDragEdge(SwipeBackLayout.DragEdge.BOTTOM);


        View v = findViewById(android.R.id.content);
        RelativeLayout ringingRelative = (RelativeLayout) v.findViewById(R.id.activity_alarm_ringing);

        String ringtone = getIntent().getStringExtra("ringtone");
        int vibration = getIntent().getIntExtra("vibration", -1);
        // Toast.makeText(getBaseContext(), vibration + "", Toast.LENGTH_SHORT).show();
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(ringtone));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            r.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        } else {
            r.setStreamType(AudioManager.STREAM_ALARM);
        }

        r.play();

        if(vibration != 0) {
            long[] pattern = {0, 2000, 500};
            vibrator.vibrate(pattern, 1);
        }

    }

    @Override
    protected void onDestroy() {
        r.stop();
        vibrator.cancel();
        super.onStop();
    }
}
