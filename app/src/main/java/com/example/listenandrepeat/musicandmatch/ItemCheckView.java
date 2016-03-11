package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by ListenAndRepeat on 2016. 3. 10..
 */
public class ItemCheckView extends FrameLayout implements Checkable {
    public ItemCheckView(Context context) {
        super(context);
        init();
    }

    public ItemCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    ImageView checkView;

    private void init(){
        inflate(getContext(),R.layout.view_check_item,this);
        checkView = (ImageView)findViewById(R.id.image_check);
    }
    boolean isChecked;

    private void drawCheck(){
        if(isChecked) {
            checkView.setVisibility(View.VISIBLE);
        } else {
            checkView.setVisibility(View.GONE);
        }
    }
    @Override
    public void setChecked(boolean checked) {
        if(isChecked != checked){
            isChecked = checked;
            drawCheck();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
