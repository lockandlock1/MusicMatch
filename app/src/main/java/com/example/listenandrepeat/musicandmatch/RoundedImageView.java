package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ListenAndRepeat on 2016. 3. 18..
 */
public class RoundedImageView extends ImageView{

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context) {
        super(context);

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(getResources(),bm);
        rbd.setCircular(true);
        setImageDrawable(rbd);
    }
}
