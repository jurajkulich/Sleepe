package com.example.android.timemachine;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import io.objectbox.Box;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Juraj on 1.4.2017.
 */

public class AddAlarmFragment extends Fragment  {

    static final int PICK_ALARM_REQUEST = 1;
    private Uri currentRingtone;
    private int vibrationStatus;
    private int alarmHour, alarmMinute;

    private TextView ringtoneNameTextView;

    AlarmInterface alarmInterface;
    public interface AlarmInterface {
        void setAlarmSettings(AlarmSettings settings);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            alarmInterface = (AlarmInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootFragmentView = inflater.inflate(R.layout.add_alarm_view, container, false);




        final Calendar cal = Calendar.getInstance();

        TimePicker alarmTimePicker = (TimePicker) rootFragmentView.findViewById(R.id.id_time_picker);
        alarmTimePicker.setIs24HourView(true);

        ringtoneNameTextView = (TextView) rootFragmentView.findViewById(R.id.id_sound_name_text_view);
        currentRingtone = RingtoneManager.getActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_ALARM);
        Ringtone ring = RingtoneManager.getRingtone(getContext(), currentRingtone);
        String name = ring.getTitle(getContext());
        name = name.replace(".ogg", "");
        ringtoneNameTextView.setText(name);

        vibrationStatus = 1;

        // Set time from TimePicker, or current time by default
        alarmHour = (cal.get(Calendar.HOUR_OF_DAY));
        alarmMinute = (cal.get(Calendar.MINUTE));
        alarmTimePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
        alarmTimePicker.setMinute(cal.get(Calendar.MINUTE));
        alarmTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                alarmHour = hourOfDay;
                alarmMinute = minute;
            }
        });

        // Set ringtone from RingtoneManager
        final TextView ringtoneTextView = (TextView) rootFragmentView.findViewById(R.id.id_sound_text_view);
        ringtoneNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, PICK_ALARM_REQUEST);
            }


        });

        // Set vibration, default is on
        final SwitchCompat vibrationSwitch = (SwitchCompat) rootFragmentView.findViewById(R.id.id_vibration_switch);
        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    vibrationStatus = 1;
                else
                    vibrationStatus = 0;
            }
        });


        // Cancel button cancels fragment
        TextView cancelButton = (TextView) rootFragmentView.findViewById(R.id.id_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        // OK button saves new Alarm
        TextView okButton = (TextView) rootFragmentView.findViewById(R.id.id_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmInterface.setAlarmSettings(new AlarmSettings(currentRingtone.toString(), vibrationStatus, alarmHour, alarmMinute, true));
                getFragmentManager().popBackStack();
            }
        });

        return rootFragmentView;
    }

    // onActivityResult for ringtone pick
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == PICK_ALARM_REQUEST)
        {
            if( resultCode == RESULT_OK)
            {
                currentRingtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ring = RingtoneManager.getRingtone(getContext(), currentRingtone);
                ringtoneNameTextView.setText(ring.getTitle(getContext()));

            }

        }
    }



}

