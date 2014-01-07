package com.smilehacker.dongxi.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by kleist on 14-1-7.
 */
public class Similars {

    @Expose
    public int count;

    @Expose
    public int start;

    @Expose
    public List<Dongxi> similars;
}
