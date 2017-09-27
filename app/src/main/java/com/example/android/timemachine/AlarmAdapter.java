package com.example.android.timemachine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juraj on 9/25/2017.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private List<AlarmSettings> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CompoundButton mToggleButton;

        public ViewHolder(View alarmView) {
            super(alarmView);
            mTextView = (TextView) itemView.findViewById(R.id.alarmTextView);
            mToggleButton = (CompoundButton) itemView.findViewById(R.id.alarm_status_toggle_button);
    }
    }

    public AlarmAdapter(List alarmSettings) {
        mDataSet = alarmSettings;
    }

    public AlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alarmView = inflater.inflate(R.layout.my_text_view, parent, false);
        ViewHolder vh = new ViewHolder(alarmView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        AlarmSettings alarm = mDataSet.get(position);
        TextView textView = holder.mTextView;
        textView.setText(alarm.getAlarmHour() + ":" + alarm.getAlarmMinute() + "-ID: " + alarm.getAlarmID());
        CompoundButton toggleButton = holder.mToggleButton;
        toggleButton.setChecked(alarm.getIsActive());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AlarmSettings alarm = mDataSet.get(i);
        TextView textView = viewHolder.mTextView;
        textView.setText(alarm.getAlarmHour() + ":" + alarm.getAlarmMinute() + "-ID: " + alarm.getAlarmID());
        CompoundButton toggleButton = viewHolder.mToggleButton;
        toggleButton.setChecked(alarm.getIsActive());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size()  ;
    }
}
