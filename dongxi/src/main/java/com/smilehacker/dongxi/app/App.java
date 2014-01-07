package com.smilehacker.dongxi.app;

import android.app.Application;


/**
 * Created by kleist on 13-12-24.
 */
public class App extends Application{

    public DeviceInfo deviceInfo;

    @Override
    public void onCreate() {
        super.onCreate();

        deviceInfo = new DeviceInfo(this);
    }

}
