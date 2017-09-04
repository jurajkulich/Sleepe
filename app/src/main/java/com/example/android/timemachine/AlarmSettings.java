package com.example.android.timemachine;

import android.net.Uri;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;


/**
 * Created by Juraj on 13.4.2017.
 */
@Entity
abstract class AlarmSettings  {

    @Key @Generated
    int id;


    String alarmRingtone;
    int alarmvibration;
    int alarmHour;
    int alarmMinute;

    AlarmSettings() {}

    AlarmSettings(long id, String ringtone,int vibration, int hour, int minute)
    {
    }

    public long getAlarmID() {
        return id;
    }

    public void setAlarmID(long alarmID) {
        this._id = alarmID;
    }

    public String getAlarmRingtone() {
        return alarmRingtone;
    }

    public int isAlarmvibration() {
        return alarmvibration;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmRingtone(String alarmRingtone) {
        this.alarmRingtone = alarmRingtone;
    }

    public void setAlarmvibration(int alarmvibration) {
        this.alarmvibration = alarmvibration;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }
}
