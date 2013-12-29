package com.smilehacker.dongxi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class Dongxi {

    @Expose
    public String text;

    @Expose
    public String price;

    @Expose
    public String id;

    @Expose
    public String fid;

    @Expose
    public String url;

    @Expose
    public String title;

    @Expose
    @SerializedName("comment_count")
    public int commentCount;

    @Expose
    @SerializedName("fork_count")
    public int forkCount;

    @Expose
    @SerializedName("like_count")
    public int likeCount;

    @Expose
    @SerializedName("purchase_url")
    public  String purchaseUrl;

    @Expose
    @SerializedName("share_reason")
    public String shareReason;

    @Expose
    public List<Pictures> pictures;

    @Expose
    public Author author;

}
