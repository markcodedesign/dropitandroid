package com.gowiapps.nahulog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    Intent mBackgroundServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackgroundServiceIntent = new Intent(this,BackgroundService.class);
        startService(mBackgroundServiceIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(mBackgroundServiceIntent);
    }
}
