package com.smilehacker.dongxi.network.task;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smilehacker.dongxi.Utils.UrlUtils;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.network.SimpleVolleyTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kleist on 13-12-28.
 */
public class DongxiTask extends SimpleVolleyTask<List<Dongxi>>{

    private final static String TAG = DongxiTask.class.getName();

    private Gson mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type mDongxiListType = new TypeToken<List<Dongxi>>() {}.getType();


    public DongxiTask(Context context, VolleyTaskCallBack<List<Dongxi>> callBack) {
        this(context, null, null, null, callBack);
    }

    public DongxiTask(Context context, String url, JSONObject json, RequestMethod method, VolleyTaskCallBack<List<Dongxi>> callback) {
        super(context, url, json, method, callback);
    }

    public void setParams(int tag, String untilId) {

        HashMap params = new HashMap();
        params.put("tag_id", tag);
        params.put("until_id", untilId);
        String url = UrlUtils.UrlBuilder(Constants.DONGXI_API + Constants.DONGXI_SHOWS, params);
        setTask(url, null, RequestMethod.GET);
    }

    @Override
    public Throwable onErrorResponse(VolleyError volleyError) {
        Log.i(TAG, "error:" + volleyError.toString());
        return null;
    }

    @Override
    public List<Dongxi> onResponse(JSONObject json) throws JSONException {
        String shows = json.getJSONArray("shows").toString();
        return mGson.fromJson(shows, mDongxiListType);
    }
}
