package com.smilehacker.dongxi.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.smilehacker.dongxi.network.image.ImageCacheManager;

/**
 * Created by kleist on 13-12-24.
 */
public class App extends Application{

    private static int DISK_IMAGECACHE_SIZE = 1024*1024*100;
    private static int MEM_IMAGECACHE_SIZE = 1024*1024*10;
    private static Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided

    @Override
    public void onCreate() {
        super.onCreate();
        ImageCacheManager.getInstance().init(this,
                this.getPackageCodePath(),
                DISK_IMAGECACHE_SIZE,
                DISK_IMAGECACHE_COMPRESS_FORMAT,
                DISK_IMAGECACHE_QUALITY,
                MEM_IMAGECACHE_SIZE);
    }

}
