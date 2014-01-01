package com.smilehacker.dongxi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class Dongxi implements Parcelable {

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
    public List<Picture> pictures;

    @Expose
    public Author author;

    protected Dongxi(Parcel in) {
        text = in.readString();
        price = in.readString();
        id = in.readString();
        fid = in.readString();
        url = in.readString();
        title = in.readString();
        commentCount = in.readInt();
        forkCount = in.readInt();
        likeCount = in.readInt();
        purchaseUrl = in.readString();
        shareReason = in.readString();
        if (in.readByte() == 0x01) {
            pictures = new ArrayList<Picture>();
            in.readList(pictures, Picture.class.getClassLoader());
        } else {
            pictures = null;
        }
        author = in.readParcelable(Author.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(price);
        dest.writeString(id);
        dest.writeString(fid);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeInt(commentCount);
        dest.writeInt(forkCount);
        dest.writeInt(likeCount);
        dest.writeString(purchaseUrl);
        dest.writeString(shareReason);
        if (pictures == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(pictures);
        }
        dest.writeParcelable(author, 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Dongxi> CREATOR = new Parcelable.Creator<Dongxi>() {
        @Override
        public Dongxi createFromParcel(Parcel in) {
            return new Dongxi(in);
        }

        @Override
        public Dongxi[] newArray(int size) {
            return new Dongxi[size];
        }
    };

}
