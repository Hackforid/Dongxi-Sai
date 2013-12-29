package com.smilehacker.dongxi.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smilehacker.dongxi.model.Dongxi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class DongxiParser {
    public static Gson mGson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static List<Dongxi> parseDongxi(JsonObject json) {
        String shows = json.get("shows").toString();
        return mGson.fromJson(shows, new TypeToken<ArrayList<Dongxi>>(){}.getType());
    }
}
