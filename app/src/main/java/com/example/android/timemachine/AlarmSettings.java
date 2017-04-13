package com.example.android.timemachine;

import android.net.Uri;

/**
 * Created by Juraj on 13.4.2017.
 */

public class AlarmSettings {
    private Uri alarmRingtone;
    private boolean alarmvibration;
    private int alarmHour, alarmMinute;

    AlarmSettings(Uri ringtone, boolean vibration, int hour, int minute)
    {
        alarmRingtone = ringtone;
        alarmvibration = vibration;
        alarmHour = hour;
        alarmMinute = minute;
    }

    public Uri getAlarmRingtone() {
        return alarmRingtone;
    }

    public boolean isAlarmvibration() {
        return alarmvibration;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }
}
