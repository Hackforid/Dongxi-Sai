package com.smilehacker.dongxi.network.task;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.network.SimpleVolleyTask;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kleist on 14-1-4.
 */
public class UserCreatedTask extends SimpleVolleyTask<List<Dongxi>> {

    private Gson mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type mDongxiListType = new TypeToken<List<Dongxi>>() {}.getType();

    public UserCreatedTask(Context context, String id, VolleyTaskCallBack<List<Dongxi>> callBack) {
        super(context, null, null, RequestMethod.GET, callBack);
        String url = new StringBuilder()
                .append(Constants.DONGXI_API)
                .append(Constants.DONGXI_USER)
                .append(id)
                .append("/created")
                .toString();
        setTask(url, null, RequestMethod.GET);
    }

    @Override
    protected Throwable onErrorResponse(VolleyError volleyError) {
        return volleyError;
    }

    @Override
    protected List<Dongxi> onResponse(JSONObject json) throws Throwable {
        String shows = json.getJSONArray("shows").toString();
        return mGson.fromJson(shows, mDongxiListType);
    }
}
