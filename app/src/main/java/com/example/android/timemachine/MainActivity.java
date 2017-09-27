package com.example.android.timemachine;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;
import io.objectbox.Box;




public class MainActivity extends AppCompatActivity  {

    private ListView alarmListView;
    private List<String> AlarmSettingsList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    // private BoxStore boxStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, KeepActiveService.class));
        setContentView(R.layout.activity_main);

        // Hide the status and action bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(intent);
        finish();
    }


}


