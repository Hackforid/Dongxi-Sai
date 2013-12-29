package com.smilehacker.dongxi.network.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by kleist on 13-10-9.
 */
public class LruImageCache implements ImageLoader.ImageCache {

    private DiskLruImageCache mDiskLruImageCache;
    private BitmapLruImageCache mBitmapLruImageCache;

    public LruImageCache(Context context, String uniqueName, int diskCacheSize,
                         Bitmap.CompressFormat compressFormat, int quality,
                         int memCacheSize) {
        mDiskLruImageCache = new DiskLruImageCache(context, uniqueName, diskCacheSize, compressFormat, quality);
        mBitmapLruImageCache = new BitmapLruImageCache(memCacheSize);
    }


    @Override
    public Bitmap getBitmap(String url) {
        String key = createKey(url);
        Bitmap bitmap = mBitmapLruImageCache.getBitmap(key);
        if (bitmap == null) {
            bitmap = mDiskLruImageCache.getBitmap(key);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key = createKey(url);
        mBitmapLruImageCache.putBitmap(key, bitmap);
        mDiskLruImageCache.putBitmap(key, bitmap);
    }

    private String createKey(String url){
        return String.valueOf(url.hashCode());
    }
}
