package com.smilehacker.dongxi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kleist on 13-12-25.
 */
public class Author implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    private Author(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        uid = in.readString();
        id = in.readString();
        isSuicide = in.readByte() == 1;
        largeAvatar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(uid);
        dest.writeString(id);
        dest.writeByte((byte) (isSuicide ? 1 : 0));
        dest.writeString(largeAvatar);
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel parcel) {
            return new Author(parcel);
        }

        @Override
        public Author[] newArray(int i) {
            return new Author[i];
        }
    };
}
