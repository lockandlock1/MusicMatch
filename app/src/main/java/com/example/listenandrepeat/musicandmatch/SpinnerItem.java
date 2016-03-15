package com.example.listenandrepeat.musicandmatch;

import android.graphics.drawable.Drawable;

/**
 * Created by ListenAndRepeat on 2016. 3. 15..
 */
public class SpinnerItem {
    private Drawable icon;
    private String name;

    public SpinnerItem(Drawable icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
