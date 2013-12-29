package com.smilehacker.dongxi.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.smilehacker.dongxi.Utils.UrlUtils;
import com.smilehacker.dongxi.app.Constants;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kleist on 13-12-24.
 */
public class NetManager {

    private final static String TAG = NetManager.class.getName();
    private volatile static NetManager Instance;
    private static RequestQueue mRequestQueue;

    private NetManager(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static NetManager getInstance(Context context) {
        if (Instance == null) {
            synchronized (NetManager.class) {
                if (Instance == null) {
                    Instance = new NetManager(context);
                }
            }
        }

        return Instance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


}
