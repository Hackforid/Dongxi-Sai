package com.smilehacker.dongxi.app;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by kleist on 13-12-29.
 */
public class DeviceInfo {

    private Context context;

    public int screenWidth;
    public int screenHeight;
    public float density;
    public int dpi;

    public DeviceInfo(Context context) {
        this.context = context;
        getScreenInfo();
    }

    private void getScreenInfo() {
        DisplayMetrics dm = this.context.getResources().getDisplayMetrics();
        this.screenHeight = dm.heightPixels;
        this.screenWidth = dm.widthPixels;
        this.density = dm.density;
        this.dpi = dm.densityDpi;
    }

}
