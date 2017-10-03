package com.example.android.timemachine;

import android.widget.CompoundButton;

/**
 * Created by Juraj on 10/3/2017.
 */

public interface RowSwitchClickListener {
    void onSwitch(boolean status, long id, AlarmSettings alarmSettings);
}
