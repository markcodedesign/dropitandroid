package com.gowiapps.nahulog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lemark on 7/15/17.
 */

public class BackgroundService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int mX = 0;
    private int mY = 0;
    private int mZ = 0;
    private int mSensitivity = 0;

    boolean mIsDropped = false;

    MediaPlayer mMediaPlayer = null;

    Timer mTimer = null;

    PowerManager.WakeLock mWakeLock = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Background Service Start");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        if(mSensor != null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

            mMediaPlayer = MediaPlayer.create(this, R.raw.mahulog_android);
        }else {
            System.out.println("NO ACCELERATION SENSOR");
            Toast.makeText(this, "NO ACCELERATION SENSOR", Toast.LENGTH_LONG).show();
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyPartialWakeLock");
        mWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //mWakeLock.release();


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        this.mX = Math.round(Math.abs(event.values[0]));
        this.mY = Math.round(Math.abs(event.values[1]));
        this.mZ = Math.round(Math.abs(event.values[2]));

        this.mSensitivity = (this.mX|this.mY|this.mZ);

//        System.out.println("ORED: " + this.mSensitivity);

        if(!mIsDropped) {
            if (this.mSensitivity >= 40) {
//                System.out.println("PHONE DROPPED");

                mMediaPlayer.start();

                if (mTimer == null) {
                    mTimer = new Timer();

                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
//                            System.out.println("TIMER FIRED");
                            mIsDropped = true;
                            mTimer.cancel();
                        }

                    }, 2000);
                }
            }
        }else if(mIsDropped){

            if(this.mSensitivity >= 2){
//                System.out.println("PHONE PICKED UP");

                mMediaPlayer.stop();

                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mIsDropped = false;
                mTimer = null;

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
