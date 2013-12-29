package com.smilehacker.dongxi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kleist on 13-12-25.
 */
public class Author {

    @Expose
    public String name;

    @Expose
    public String avatar;

    @Expose
    public String uid;

    @Expose
    public String id;

    @Expose
    @SerializedName("is_suicide")
    public Boolean isSuicide;

    @Expose
    @SerializedName("large_avatar")
    public String largeAvatar;
}
