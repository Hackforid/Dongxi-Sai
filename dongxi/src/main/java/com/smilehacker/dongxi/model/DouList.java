package com.smilehacker.dongxi.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class DouList {

    @Expose
    public String url;

    @Expose
    public String id;

    @Expose
    public String title;

    @Expose
    public List<Pictures> pictures;
}
