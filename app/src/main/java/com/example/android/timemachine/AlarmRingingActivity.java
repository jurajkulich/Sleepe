package com.example.android.timemachine;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmRingingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        String ringtone = getIntent().getStringExtra("ringtone");
        int vibration = getIntent().getIntExtra("vibration", -1);
        Toast.makeText(getBaseContext(), vibration + "", Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(ringtone));
        r.play();
        if(vibration != 0)
            v.vibrate(3000);

    }
}
