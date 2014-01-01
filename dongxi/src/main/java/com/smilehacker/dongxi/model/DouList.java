package com.smilehacker.dongxi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class DouList implements Parcelable{

    @Expose
    public String url;

    @Expose
    public String id;

    @Expose
    public String title;

    @Expose
    public List<Picture> pictures;

    protected DouList(Parcel in) {
        url = in.readString();
        id = in.readString();
        title = in.readString();
        if (in.readByte() == 0x01) {
            pictures = new ArrayList<Picture>();
            in.readList(pictures, Picture.class.getClassLoader());
        } else {
            pictures = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(title);
        if (pictures == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(pictures);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DouList> CREATOR = new Parcelable.Creator<DouList>() {
        @Override
        public DouList createFromParcel(Parcel in) {
            return new DouList(in);
        }

        @Override
        public DouList[] newArray(int size) {
            return new DouList[size];
        }
    };
}
