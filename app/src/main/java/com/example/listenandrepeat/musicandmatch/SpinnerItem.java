package com.example.listenandrepeat.musicandmatch;

import android.graphics.drawable.Drawable;

/**
 * Created by ListenAndRepeat on 2016. 3. 15..
 */
public class SpinnerItem {
    private int icon;
    private String name;

    public SpinnerItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
