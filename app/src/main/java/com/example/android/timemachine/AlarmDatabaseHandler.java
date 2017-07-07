package com.example.android.timemachine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juraj on 22.4.2017.
 */

public class AlarmDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "alarmSettingsManager";

    private static final String TABLE_SETTINGS = "settings";

    private static final String KEY_ID = "_id";
    private static final String RINGTONE = "ringtone";
    private static final String ALARM_MINUTE = "alarm_minute";
    private static final String ALARM_HOUR = "alarm_hour";
    private static final String VIBRATION = "vibration";

    AlarmDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RINGTONE + " TEXT," + ALARM_HOUR + " INT," + ALARM_MINUTE + " INT," + VIBRATION + " INT" + ")";
        sqLiteDatabase.execSQL(CREATE_SETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_SETTINGS);

        onCreate(sqLiteDatabase);
    }


    public long addSettings(AlarmSettings alarmSettings){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, alarmSettings.getAlarmID());
        values.put(RINGTONE, alarmSettings.getAlarmRingtone());
        values.put(ALARM_HOUR, alarmSettings.getAlarmHour());
        values.put(ALARM_MINUTE, alarmSettings.getAlarmMinute());
        values.put(VIBRATION, alarmSettings.isAlarmvibration());
        long id = sqLiteDatabase.insert(TABLE_SETTINGS, null, values);
        sqLiteDatabase.close();
        return id;
    }

    public AlarmSettings getAlarmSettings(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_SETTINGS, new String[] { KEY_ID, RINGTONE, ALARM_HOUR,
            ALARM_MINUTE, VIBRATION}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        AlarmSettings alarmSettings = new AlarmSettings(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));

        return alarmSettings;
    }

    public List<String> getAllAlarmTimes()
    {
        List<String> times = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String hourMinute = "SELECT * FROM " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(hourMinute, null);
        if(cursor.moveToFirst())
        {
            do {
                int hour = cursor.getInt(2);
                int minute = cursor.getInt(3);
                String time;
                if( hour < 10 && minute < 10 )
                    time  = "0" + hour + ":" + "0" + minute;
                else if(hour < 10)
                    time = "0" + hour + ":" + minute;
                else if(minute < 10 )
                    time = hour + ":" + "0" + minute;
                else
                    time = hour + ":" +  minute;
                times.add(time);
            } while(cursor.moveToNext());
        }

        return times;
    }

    public List<AlarmSettings> getAllAlarmSettings() {
        List<AlarmSettings> alarmSettingsList = new ArrayList<AlarmSettings>();

        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do {
                AlarmSettings alarmSettings = new AlarmSettings();
                alarmSettings.setAlarmID(Integer.parseInt(cursor.getString(0)));
                alarmSettings.setAlarmRingtone(cursor.getString(1));
                alarmSettings.setAlarmvibration(cursor.getInt(2));
                alarmSettings.setAlarmHour(cursor.getInt(3));
                alarmSettings.setAlarmMinute(cursor.getInt(4));
                alarmSettingsList.add(alarmSettings);
            } while(cursor.moveToNext());
        }
        return alarmSettingsList;
    }


    public int getAlarmSettingsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateAlarmSettings(AlarmSettings alarmSettings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RINGTONE, alarmSettings.getAlarmRingtone());
        values.put(ALARM_HOUR, alarmSettings.getAlarmHour());
        values.put(ALARM_MINUTE, alarmSettings.getAlarmMinute());
        values.put(VIBRATION, alarmSettings.isAlarmvibration());

        return db.update(TABLE_SETTINGS, values, KEY_ID + " = ?", new String[] { String.valueOf(alarmSettings.getAlarmID())});
    }

    public void deleteAlarmSettings(long position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SETTINGS + " WHERE " + KEY_ID + "= '" + position  + "'");
        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_SETTINGS;
        db.execSQL(clearDBQuery);
    }

    public AlarmSettings getAlarmSettingsByIndex(int position)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToPosition(position);
        AlarmSettings alarmSettings = new AlarmSettings();
        alarmSettings.setAlarmID(Integer.parseInt(cursor.getString(0)));
        alarmSettings.setAlarmRingtone(cursor.getString(1));
        alarmSettings.setAlarmvibration(cursor.getInt(4));
        alarmSettings.setAlarmHour(cursor.getInt(2));
        alarmSettings.setAlarmMinute(cursor.getInt(3));

        return alarmSettings;
    }

}
