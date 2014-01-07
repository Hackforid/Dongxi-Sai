package com.smilehacker.exvolley.ex;

import com.smilehacker.exvolley.Request;
import com.smilehacker.exvolley.RequestQueue;
import com.smilehacker.exvolley.Response;
import com.smilehacker.exvolley.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kleist on 14-1-6.
 */
public class ExRequestBuilder {

    public static final String PROTOCOL_CHARSET = "utf-8";
    public static final String CONTENT_TYPE_JSON = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private RequestQueue mRequestQueue;
    private int mMethod;
    private String mUrl;
    private Map<String, String> mRequestParams;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private String mRequestBody;

    private ExRequest mRequest;
    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;
    private Object mTag;
    private Class mResponseClass;
    private Boolean mShouldCache = false;
    private String mContentType = null;


    public ExRequestBuilder(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
        mTag = this.hashCode();
    }

    public ExRequestBuilder load(String url) {
        mUrl = url;
        return this;
    }

    public ExRequestBuilder method(int method) {
        mMethod = method;
        return this;
    }

    public ExRequestBuilder setParam(String key, String value) {
        if (mRequestParams == null) {
            mRequestParams = new HashMap<String, String>();
        }
        mRequestParams.put(key, value);
        return this;
    }

    public ExRequestBuilder setParams(Map<String, String> formMap) {
        if (mRequestParams == null) {
            mRequestParams = new HashMap<String, String>();
        }
        mRequestParams.putAll(formMap);
        return this;
    }

    public ExRequestBuilder setJsonBody(JSONObject jsonBody) {
        mRequestBody = jsonBody == null ? null : jsonBody.toString();
        return this;
    }

    public ExRequestBuilder setRequestBody(String requestBody) {
        mRequestBody = requestBody;
        return this;
    }

    public ExRequestBuilder setHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<String, String>();
        }
        mHeaders.put(key, value);
        return this;
    }

    public ExRequestBuilder setContentType(String contentType) {
        mContentType = contentType;
        return this;
    }

    public ExRequestBuilder asJsonRequest() {
        mContentType = CONTENT_TYPE_JSON;
        return this;
    }

    public ExRequestBuilder shouldCache(Boolean shouldCache) {
        mShouldCache = shouldCache;
        return this;
    }

    // TODO 待到jdk8时自动判断泛型type
    public ExRequestBuilder setResponseListener(Response.Listener<?> listener, Class responseClass) {
        mListener = listener;
        mResponseClass = responseClass;
        return this;
    }

    public ExRequestBuilder setErrorListener(Response.ErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    public ExRequestBuilder setTag(Object tag) {
        mTag = tag;
        return this;
    }

    public void cancel() {
        mRequestQueue.cancelAll(mTag);
    }

    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ExRequest excute() {

        if (mMethod == Request.Method.GET && mRequestParams != null && mRequestParams.size() > 0) {
            mUrl = UrlUtils.UrlBuilder(mUrl, mRequestParams);
        }

        if (mResponseClass.equals(String.class) ) {
            mRequest = new ExRequest<String>(mMethod, mUrl, mListener, mErrorListener, mResponseClass);
        } else if (mResponseClass.equals(JSONObject.class)) {
            mRequest = new ExRequest<JSONObject>(mMethod, mUrl, mListener, mErrorListener, mResponseClass);
        } else if (mResponseClass.equals(JSONArray.class)) {
            mRequest = new ExRequest<JSONArray>(mMethod, mUrl, mListener, mErrorListener, mResponseClass);
        }

        if (mMethod == Request.Method.PUT || mMethod == Request.Method.POST) {
            mRequest.setParams(mRequestParams);
        }

        mRequest.setTag(mTag);
        mRequest.setShouldCache(mShouldCache);
        mRequest.setContentType(mContentType);
        mRequest.setHeader(mHeaders);

        mRequest.setRequestBody(mRequestBody);

        mRequestQueue.add(mRequest);

        return mRequest;
    }


}
