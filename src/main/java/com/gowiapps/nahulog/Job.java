package com.gowiapps.nahulog;

/**
 * Created by lemark on 7/12/17.
 */

public class Job extends Object {
    private boolean mIsReady = false;
    private int mSensorSensitivity = 0;
    private int mDelay = 0;
    private Action mAction = null;

    public Job(boolean isReady,int sensorSensitivity,int delay,Action action){
        this.mIsReady = isReady;
        this.mSensorSensitivity = sensorSensitivity;
        this.mDelay = delay;
        this.mAction = action;
    }

    public void run(){

    }

}
