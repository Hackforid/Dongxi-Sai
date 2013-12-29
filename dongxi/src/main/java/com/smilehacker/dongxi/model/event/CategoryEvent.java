package com.smilehacker.dongxi.model.event;

/**
 * Created by kleist on 13-12-28.
 */
public class CategoryEvent {
    public int id;
    public String title;

    public CategoryEvent(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
