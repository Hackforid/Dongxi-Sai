package com.smilehacker.dongxi.network.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.exvolley.Request;
import com.smilehacker.exvolley.VolleyError;
import com.smilehacker.exvolley.ex.ExRequestBuilder;
import com.smilehacker.exvolley.ex.ExVolley;
import com.smilehacker.exvolley.ex.ExVolleyTask;
import com.smilehacker.exvolley.utils.JsonUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kleist on 14-1-7.
 */
public class ExDongxiTask extends ExVolleyTask<List<Dongxi>> {

    private Gson mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type mDongxiListType = new TypeToken<List<Dongxi>>() {}.getType();
    private String mTag;
    private String mUtilId;
    private String mUrl = Constants.DONGXI_API + Constants.DONGXI_SHOWS;

    public ExDongxiTask(Context context, String tag, String untilId, ExVolleyTaskCallBack<List<Dongxi>> callBack) {
        super(context, callBack);
        mTag = tag;
        mUtilId = untilId;
    }

    @Override
    protected ExRequestBuilder buildRequest() {
        ExRequestBuilder builder = ExVolley.with(getContext()).load(mUrl)
                .method(Request.Method.GET)
                .setParam("tag_id", mTag)
                .setParam("until_id", mUtilId);

        return builder;
    }

    @Override
    protected List<Dongxi> onResponse(String response) throws Throwable {
        JSONObject json = JsonUtils.parseToJson(response);
        String shows = json.getJSONArray("shows").toString();
        return mGson.fromJson(shows, mDongxiListType);
    }

    @Override
    protected Throwable onErrorResponse(VolleyError volleyError) {
        return volleyError;
    }
}
