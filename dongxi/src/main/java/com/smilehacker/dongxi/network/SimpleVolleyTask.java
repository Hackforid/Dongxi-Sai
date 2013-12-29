package com.smilehacker.dongxi.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kleist on 13-12-28.
 */
public abstract class SimpleVolleyTask<CallBackType> {


    public static enum RequestMethod {
        GET, POST, PUT, DELETE
    }

    private RequestQueue mRequestQueue;

    private String mUrl;
    private HashMap mParams;
    private int mMethod;
    private JSONObject mJson;
    private VolleyTaskCallBack<CallBackType> mCallBack;
    private Context mContext;
    private Object mTag;

    protected SimpleVolleyTask() {
    }

    /**
     *
     * @param context
     * @param url
     * @param json
     * @param method
     * @param callback
     */
    public SimpleVolleyTask(Context context, String url, JSONObject json, RequestMethod method, VolleyTaskCallBack<CallBackType> callback) {
        mContext = context;
        mRequestQueue = NetManager.getInstance(context).getRequestQueue();
        mTag = this.hashCode();
        mCallBack = callback;
        setTask(url, json, method);
    }

    protected  void setTask(String url, JSONObject json, RequestMethod method) {
        mUrl = url;
        mJson = json;
        if (method != null) {
            switch (method) {
                case GET: mMethod = Request.Method.GET; break;
                case POST: mMethod = Request.Method.POST; break;
                case PUT: mMethod = Request.Method.PUT; break;
                case DELETE: mMethod = Request.Method.DELETE; break;
            }
        }
    }

    public void execute() {

        mCallBack.onStart();

        JsonObjectRequest request = new JsonObjectRequest(
                mMethod,
                mUrl,
                mJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            CallBackType result = SimpleVolleyTask.this.onResponse(jsonObject);
                            mCallBack.onSuccess(result);
                        } catch (Throwable e) {
                            mCallBack.onFail(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Throwable e = SimpleVolleyTask.this.onErrorResponse(volleyError);
                        mCallBack.onFail(e);
                    }
                }
        );

        request.setTag(mTag);
        mRequestQueue.add(request);
    }

    public void cancel() {
        mRequestQueue.cancelAll(mTag);
    }

    protected  abstract Throwable onErrorResponse(VolleyError volleyError);

    protected abstract CallBackType onResponse(JSONObject json) throws Throwable;

    public interface VolleyTaskCallBack<T> {
        public void onSuccess(T result);
        public void onFail(Throwable e);
        public void onStart();
    }

}
