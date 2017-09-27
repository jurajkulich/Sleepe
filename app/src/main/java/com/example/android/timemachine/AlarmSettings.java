package com.example.android.timemachine;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;


/**
 * Created by Juraj on 13.4.2017.
 */
@Entity
public class AlarmSettings  {

    @Id
    private Long id;

    private String alarmRingtone;
    private int alarmvibration;
    private int alarmHour;
    private int alarmMinute;
    private boolean isActive;

    AlarmSettings() {}

    AlarmSettings(String ringtone,int vibration, int hour, int minute, boolean active)
    {
        this.alarmRingtone = ringtone;
        this.alarmvibration =vibration;
        this.alarmHour = hour;
        this.alarmMinute = minute;
        this.isActive = active;
    }


    public long getAlarmID() {
        return id;
    }

    public void setAlarmID(long alarmID) {
        this.id = alarmID;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAlarmvibration() {
        return this.alarmvibration;
    }

    public void setIsActive(boolean active) { this.isActive = active; }

    public boolean getIsActive() { return this.isActive; }

}
