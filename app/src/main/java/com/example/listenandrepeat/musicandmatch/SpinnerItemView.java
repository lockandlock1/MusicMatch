package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ListenAndRepeat on 2016. 3. 15..
 */
public class SpinnerItemView extends FrameLayout {

    public SpinnerItemView(Context context) {
        super(context);
        init();
    }

    ImageView iconView;
    TextView nameView;

    private void init(){
        inflate(getContext(),R.layout.view_spinner,this);
        iconView = (ImageView)findViewById(R.id.image_select);
        nameView = (TextView)findViewById(R.id.text_edit);

    }

    public void setSpinner(SpinnerItem item){

       iconView.setImageResource(item.getIcon());
       nameView.setText(item.getName());

    }




}
