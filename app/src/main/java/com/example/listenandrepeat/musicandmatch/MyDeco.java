package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ListenAndRepeat on 2016. 3. 21..
 */
public class MyDeco extends RecyclerView.ItemDecoration {

    Drawable mDivider;

    int[] ATTR = {android.R.attr.listDivider};
    public MyDeco(Context context) {
        TypedArray ta = context.obtainStyledAttributes(ATTR);
    }
}
